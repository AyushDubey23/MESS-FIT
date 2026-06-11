package com.example.messfitai.domain

import com.example.messfitai.data.model.DailyDiet
import com.example.messfitai.data.model.MealItem
import com.example.messfitai.data.model.UserProfile
import com.example.messfitai.data.model.WeeklyMenu
import kotlin.math.roundToInt

object DailyDietGenerator {

    data class ShoppingItem(
        val name: String,
        val quantityText: String,
        val estimatedCost: Float
    )

    fun generateWeeklyDiet(profile: UserProfile, menu: WeeklyMenu): List<DailyDiet> {
        val analysis = BodyAnalysisEngine.analyze(profile)
        val targetProtein = analysis.recommendedProteinGrams
        val targetCalories = analysis.recommendedCalories

        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        
        return days.map { day ->
            val dailySchedule = menu.schedule[day] ?: emptyMap()
            val mealPlans = mutableMapOf<String, List<MealItem>>()
            
            var dailyMessProtein = 0f
            var dailyMessCalories = 0f
            var dailyMessCarbs = 0f
            var dailyMessFat = 0f
            var dailyMessFiber = 0f

            // 1. Process Mess Meals
            val mealNames = listOf("Breakfast", "Lunch", "Dinner")
            for (mealName in mealNames) {
                val items = dailySchedule[mealName] ?: emptyList()
                val parsedItems = items.map { itemName ->
                    val mealItem = MenuParserEngine.getMacrosForItem(itemName)
                    dailyMessProtein += mealItem.protein
                    dailyMessCalories += mealItem.calories
                    dailyMessCarbs += mealItem.carbs
                    dailyMessFat += mealItem.fat
                    dailyMessFiber += mealItem.fiber
                    mealItem
                }
                mealPlans[mealName] = parsedItems
            }

            // 2. Calculate deficit
            var currentProtein = dailyMessProtein
            var currentCalories = dailyMessCalories
            var currentCarbs = dailyMessCarbs
            var currentFat = dailyMessFat
            var currentFiber = dailyMessFiber

            val proteinDeficit = (targetProtein - currentProtein).coerceAtLeast(0f)
            val addedItems = mutableListOf<MealItem>()

            // 3. Add cheap protein sources to hit target
            if (proteinDeficit > 0) {
                var deficitToFill = proteinDeficit
                val veg = profile.vegetarianPreference // "Vegetarian", "Non-Vegetarian", "Eggitarian"
                val dailyBudgetLimit = profile.monthlyBudget / 30f

                // Decide options based on budget and diet preferences
                if (veg != "Vegetarian" && deficitToFill >= 12f && dailyBudgetLimit > 15f) {
                    // Suggest 2 boiled eggs (Cost: ₹14, Protein: 13g)
                    val eggs = MealItem(
                        name = "2 Boiled Eggs",
                        calories = 150f,
                        protein = 13f,
                        carbs = 1f,
                        fat = 10f,
                        fiber = 0f,
                        instruction = "Buy boiled eggs from canteen. Eat after workout.",
                        isAddedItem = true,
                        estimatedCost = 14f
                    )
                    addedItems.add(eggs)
                    deficitToFill -= 13f
                    currentProtein += 13f
                    currentCalories += 150f
                    currentCarbs += 1f
                    currentFat += 10f
                }

                // If still deficit and low/medium budget, add Soya Chunks (Cost: ₹6, Protein: 26g for 50g)
                if (deficitToFill >= 15f) {
                    val soya = MealItem(
                        name = "50g Soya Chunks",
                        calories = 170f,
                        protein = 26f,
                        carbs = 15f,
                        fat = 0.5f,
                        fiber = 6f,
                        instruction = "Boil in kettle or request mess worker to add in hot water. Add salt.",
                        isAddedItem = true,
                        estimatedCost = 6f
                    )
                    addedItems.add(soya)
                    deficitToFill -= 26f
                    currentProtein += 26f
                    currentCalories += 170f
                    currentCarbs += 15f
                    currentFat += 0.5f
                    currentFiber += 6f
                }

                // If still deficit, suggest Milk (Cost: ₹15 for 250ml, Protein: 8g)
                if (deficitToFill >= 6f && dailyBudgetLimit > 20f) {
                    val milk = MealItem(
                        name = "250ml Milk",
                        calories = 150f,
                        protein = 8f,
                        carbs = 12f,
                        fat = 8f,
                        fiber = 0f,
                        instruction = "Buy packet milk, drink cold or boil in hostel room.",
                        isAddedItem = true,
                        estimatedCost = 15f
                    )
                    addedItems.add(milk)
                    deficitToFill -= 8f
                    currentProtein += 8f
                    currentCalories += 150f
                    currentCarbs += 12f
                    currentFat += 8f
                }

                // If still deficit, suggest Roasted Chana (Cost: ₹9 for 50g, Protein: 11g)
                if (deficitToFill > 0f) {
                    val chana = MealItem(
                        name = "50g Roasted Chana",
                        calories = 180f,
                        protein = 11f,
                        carbs = 29f,
                        fat = 3f,
                        fiber = 6f,
                        instruction = "Eat dry with tea as evening snack. Great source of clean carbs and protein.",
                        isAddedItem = true,
                        estimatedCost = 9f
                    )
                    addedItems.add(chana)
                    deficitToFill -= 11f
                    currentProtein += 11f
                    currentCalories += 180f
                    currentCarbs += 29f
                    currentFat += 3f
                    currentFiber += 6f
                }

                // Distribute added items to appropriate meals
                addedItems.forEach { item ->
                    val targetMeal = when {
                        item.name.contains("Egg", ignoreCase = true) || item.name.contains("Milk", ignoreCase = true) -> "Breakfast"
                        item.name.contains("Chana", ignoreCase = true) -> "Snack"
                        else -> "Lunch" // Soya Chunks added to lunch / dinner
                    }
                    if (targetMeal == "Snack") {
                        mealPlans["Snack"] = (mealPlans["Snack"] ?: emptyList()) + item
                    } else {
                        mealPlans[targetMeal] = (mealPlans[targetMeal] ?: emptyList()) + item
                    }
                }
            }

            DailyDiet(
                dayOfWeek = day,
                meals = mealPlans,
                completed = false
            )
        }
    }

    fun generateWeeklyShoppingList(weeklyDiet: List<DailyDiet>): List<ShoppingItem> {
        val tally = mutableMapOf<String, Pair<Float, Float>>() // Name -> Pair(Quantity multiplier, unit cost)
        
        weeklyDiet.forEach { daily ->
            daily.meals.values.flatten().forEach { item ->
                if (item.isAddedItem) {
                    val current = tally[item.name] ?: Pair(0f, item.estimatedCost)
                    tally[item.name] = Pair(current.first + 1f, item.estimatedCost)
                }
            }
        }

        return tally.map { (name, stats) ->
            val count = stats.first
            val unitCost = stats.second
            val totalCost = count * unitCost
            
            val quantityText = when {
                name.contains("Soya Chunks") -> "${(count * 50).toInt()}g"
                name.contains("Milk") -> "${(count * 250 / 1000f)} Litres"
                name.contains("Eggs") -> "${(count * 2).toInt()} Eggs"
                name.contains("Chana") -> "${(count * 50).toInt()}g"
                else -> "${count.toInt()} packets"
            }

            ShoppingItem(
                name = name.replace("50g ", "").replace("250ml ", ""),
                quantityText = quantityText,
                estimatedCost = totalCost
            )
        }
    }
}
