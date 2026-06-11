package com.example.messfitai.domain

import com.example.messfitai.data.model.MealItem
import com.example.messfitai.data.model.WeeklyMenu

object MenuParserEngine {

    private val foodDatabase = mapOf(
        "poha" to MealItem("Poha", 250f, 4f, 50f, 3f, 2f, "Iron: 1.5mg, Vit B: 0.2mg", "Eat standard serving"),
        "tea" to MealItem("Tea", 60f, 1f, 8f, 2f, 0f, "", "Limit sugar to 1 tsp"),
        "roti" to MealItem("Roti", 80f, 3f, 17f, 0.5f, 2f, "Iron: 1mg", "Eat 3-4 pieces depending on goals"),
        "chapati" to MealItem("Roti", 80f, 3f, 17f, 0.5f, 2f, "Iron: 1mg", "Eat 3-4 pieces depending on goals"),
        "dal" to MealItem("Dal", 150f, 7f, 24f, 2f, 5f, "Iron: 2.5mg, Folate", "Eat 1.5 to 2 bowls for protein"),
        "paneer" to MealItem("Paneer Gravy", 220f, 12f, 10f, 15f, 1f, "Calcium: 200mg", "Pick out paneer cubes, limit gravy"),
        "chole" to MealItem("Chole Masala", 200f, 7f, 30f, 5f, 6f, "Iron: 2mg", "Eat 1.5 bowls"),
        "rice" to MealItem("Rice", 200f, 4f, 44f, 0.4f, 1f, "B-Vitamins", "Limit to 1 standard plate/bowl"),
        "chicken" to MealItem("Chicken Curry", 280f, 22f, 6f, 18f, 1f, "Iron: 1.5mg, Zinc", "Take 3-4 chicken pieces for high protein"),
        "egg" to MealItem("Egg Curry/Bhurji", 180f, 13f, 2f, 12f, 0f, "Vit D, Choline", "Eat full serving (usually 2 eggs)"),
        "milk" to MealItem("Milk", 150f, 8f, 12f, 8f, 0f, "Calcium: 300mg", "Add to breakfast or snacks"),
        "curd" to MealItem("Curd", 120f, 7f, 8f, 6f, 0f, "Calcium: 240mg", "Excellent probiotic. Eat with lunch"),
        "upma" to MealItem("Upma", 220f, 4f, 40f, 5f, 2f, "Iron: 1mg", "Limit portion, add roasted chana"),
        "idli" to MealItem("Idli", 150f, 3f, 32f, 0.5f, 1.5f, "B-Vitamins", "Eat 3 pieces with sambhar"),
        "dosa" to MealItem("Dosa", 180f, 3f, 35f, 3f, 1.5f, "", "Limit oil, eat with sambhar"),
        "sambhar" to MealItem("Sambhar", 80f, 3f, 12f, 2f, 3f, "Vit A, Iron", "Good source of fiber, eat 1.5 bowls"),
        "aloo paratha" to MealItem("Aloo Paratha", 250f, 4f, 40f, 8f, 3f, "", "Avoid excess butter/oil"),
        "khichdi" to MealItem("Khichdi", 220f, 6f, 40f, 3f, 4f, "Iron", "Add a spoonful of ghee, pair with curd"),
        "soybean" to MealItem("Soybean Curry", 180f, 12f, 15f, 6f, 4f, "Iron, Calcium", "Eat full serving"),
        "rajma" to MealItem("Rajma", 180f, 7f, 28f, 3f, 6f, "Iron, Folate", "Excellent carb source. Pair with rice"),
        "sprouts" to MealItem("Sprouts Salad", 120f, 8f, 20f, 0.5f, 4f, "Vit C, Iron", "Eat raw/boiled as snack"),
        "fish" to MealItem("Fish Curry", 240f, 20f, 4f, 16f, 0f, "Omega-3", "Good lean protein option"),
        "sattu" to MealItem("Sattu Drink", 110f, 6f, 20f, 1.5f, 3f, "Magnesium", "Natural cheap protein shake"),
        "biryani" to MealItem("Veg/Egg Biryani", 320f, 8f, 55f, 8f, 2f, "", "Limit portion size"),
        "pulao" to MealItem("Pulao", 220f, 4f, 45f, 4f, 1.5f, "", "Avoid excess oily parts"),
        "soya chunks" to MealItem("Soya Chunks", 170f, 26f, 15f, 0.5f, 6f, "Iron: 4mg", "Cheapest protein source in India"),
        "roasted chana" to MealItem("Roasted Chana", 180f, 11f, 29f, 3f, 6f, "Iron: 3mg", "Excellent high-protein snack"),
        "peanuts" to MealItem("Peanuts", 280f, 12f, 8f, 24f, 4f, "Vit E, Magnesium", "Cheap calorie booster for bulk"),
        "tofu" to MealItem("Tofu", 120f, 12f, 2f, 7f, 1f, "Calcium", "Good dairy-free protein"),
        "protein lassi" to MealItem("Protein Lassi", 220f, 15f, 18f, 5f, 0f, "Calcium", "Sattu + Curd mixed lassi")
    )

    fun getMacrosForItem(name: String): MealItem {
        val normalized = name.lowercase().trim()
        // Simple substring matching
        for ((key, item) in foodDatabase) {
            if (normalized.contains(key) || key.contains(normalized)) {
                return item.copy(name = name)
            }
        }
        // Default item if not found
        return MealItem(
            name = name,
            calories = 150f,
            protein = 3f,
            carbs = 25f,
            fat = 4f,
            fiber = 1.5f,
            micronutrients = "Generic Indian Food macros estimated",
            instruction = "Eat standard serving"
        )
    }

    fun parseOCRMenuText(menuText: String): WeeklyMenu {
        // Simple scanning parser (in production, this would parse OCR output using Regex/Gemini API)
        // Here we simulate the AI logic by looking for keywords and generating a score
        val lines = menuText.lowercase()
        val containsPaneer = lines.contains("paneer")
        val containsChicken = lines.contains("chicken") || lines.contains("non-veg")
        val containsEgg = lines.contains("egg") || lines.contains("anda")
        val containsDal = lines.contains("dal") || lines.contains("sambhar")
        val containsSoya = lines.contains("soya") || lines.contains("soybean")
        val containsRajma = lines.contains("rajma") || lines.contains("chole")

        // Score formulas based on protein options in mess
        var proteinScore = 30
        if (containsDal) proteinScore += 15
        if (containsRajma) proteinScore += 10
        if (containsPaneer) proteinScore += 15
        if (containsSoya) proteinScore += 10
        if (containsEgg) proteinScore += 10
        if (containsChicken) proteinScore += 10
        proteinScore = proteinScore.coerceAtMost(98)

        val carbQualityScore = if (lines.contains("roti") || lines.contains("chapati")) 65 else 50
        val fatQualityScore = if (lines.contains("fried") || lines.contains("poori") || lines.contains("bhatura")) 40 else 60
        val micronutrientScore = if (lines.contains("salad") || lines.contains("fruit") || lines.contains("vegetable") || lines.contains("sabji")) 65 else 45
        
        val rating = (proteinScore * 0.4f + carbQualityScore * 0.2f + fatQualityScore * 0.2f + micronutrientScore * 0.2f).toInt().coerceIn(10, 100)
        
        val bulkingSuitability = (rating * 0.7f + 25f).toInt().coerceAtMost(100)
        val cuttingSuitability = (proteinScore * 0.8f + 10f).toInt().coerceAtMost(100)
        
        val isNonVeg = containsChicken || containsEgg
        val vegFriendlinessScore = if (isNonVeg) 60 else 95

        return WeeklyMenu(
            rating = rating,
            proteinScore = proteinScore,
            carbQualityScore = carbQualityScore,
            fatQualityScore = fatQualityScore,
            micronutrientScore = micronutrientScore,
            bulkingSuitability = bulkingSuitability,
            cuttingSuitability = cuttingSuitability,
            vegFriendlinessScore = vegFriendlinessScore
        )
    }

    // High fidelity presets that simulate actual scanned menus
    fun getPresetMenu(type: String): WeeklyMenu {
        val schedule = when (type) {
            "North Indian Standard Veg" -> mapOf(
                "Monday" to mapOf("Breakfast" to listOf("Aloo Paratha", "Tea"), "Lunch" to listOf("Rice", "Dal", "Mix Veg Sabji"), "Dinner" to listOf("Roti", "Chole Masala", "Kheer")),
                "Tuesday" to mapOf("Breakfast" to listOf("Poha", "Tea"), "Lunch" to listOf("Rice", "Dal Tadka", "Aloo Jeera"), "Dinner" to listOf("Roti", "Rajma Curry")),
                "Wednesday" to mapOf("Breakfast" to listOf("Puri Chole", "Tea"), "Lunch" to listOf("Rice", "Dal", "Paneer Butter Masala"), "Dinner" to listOf("Roti", "Lauki Sabji")),
                "Thursday" to mapOf("Breakfast" to listOf("Bread Butter Jam", "Milk"), "Lunch" to listOf("Rice", "Kadhi Pakoda"), "Dinner" to listOf("Roti", "Soybean Curry")),
                "Friday" to mapOf("Breakfast" to listOf("Dalia", "Tea"), "Lunch" to listOf("Rice", "Dal Fry", "Bhindi Do Pyaza"), "Dinner" to listOf("Roti", "Egg Curry / Paneer")),
                "Saturday" to mapOf("Breakfast" to listOf("Upma", "Tea"), "Lunch" to listOf("Khichdi", "Curd", "Aloo Chokha"), "Dinner" to listOf("Roti", "Mix Dal Curry")),
                "Sunday" to mapOf("Breakfast" to listOf("Kachori", "Jalebi"), "Lunch" to listOf("Veg Biryani", "Raita"), "Dinner" to listOf("Roti", "Shahi Paneer", "Ice Cream"))
            )
            "South Indian Standard Veg" -> mapOf(
                "Monday" to mapOf("Breakfast" to listOf("Idli", "Sambhar", "Coconut Chutney"), "Lunch" to listOf("Rice", "Rasam", "Cabbage Poriyal"), "Dinner" to listOf("Dosa", "Sambhar")),
                "Tuesday" to mapOf("Breakfast" to listOf("Upma", "Sambar", "Tea"), "Lunch" to listOf("Rice", "Kootu Curry", "Butter Milk"), "Dinner" to listOf("Chapati", "Dal Tadka")),
                "Wednesday" to mapOf("Breakfast" to listOf("Medu Vada", "Sambhar"), "Lunch" to listOf("Rice", "Veg Kurma", "Curd Rice"), "Dinner" to listOf("Roti", "Paneer Butter Masala")),
                "Thursday" to mapOf("Breakfast" to listOf("Poha", "Tea"), "Lunch" to listOf("Rice", "Sambhar", "Potato Fry"), "Dinner" to listOf("Dosa", "Chutney")),
                "Friday" to mapOf("Breakfast" to listOf("Uttapam", "Sambar"), "Lunch" to listOf("Rice", "Rasam", "Avial"), "Dinner" to listOf("Chapati", "Veg Khurma")),
                "Saturday" to mapOf("Breakfast" to listOf("Bread Toast", "Tea"), "Lunch" to listOf("Lemon Rice", "Sambhar"), "Dinner" to listOf("Roti", "Green Gram Dal")),
                "Sunday" to mapOf("Breakfast" to listOf("Appam", "Coconut Milk"), "Lunch" to listOf("South Veg Biryani", "Raita"), "Dinner" to listOf("Chapati", "Soya Chunk Curry"))
            )
            else -> mapOf( // Standard Hostel Mess Mix
                "Monday" to mapOf("Breakfast" to listOf("Poha", "Tea"), "Lunch" to listOf("Rice", "Dal", "Paneer Curry"), "Dinner" to listOf("Roti", "Chole")),
                "Tuesday" to mapOf("Breakfast" to listOf("Upma", "Tea"), "Lunch" to listOf("Rice", "Dal", "Soybean Curry"), "Dinner" to listOf("Roti", "Aloo Gobhi")),
                "Wednesday" to mapOf("Breakfast" to listOf("Aloo Paratha", "Tea"), "Lunch" to listOf("Rice", "Sambhar", "Mix Veg"), "Dinner" to listOf("Roti", "Egg Curry / Paneer")),
                "Thursday" to mapOf("Breakfast" to listOf("Idli", "Sambhar"), "Lunch" to listOf("Rice", "Dal", "Rajma"), "Dinner" to listOf("Roti", "Lauki Sabji")),
                "Friday" to mapOf("Breakfast" to listOf("Bread Butter", "Tea"), "Lunch" to listOf("Rice", "Kadhi", "Aloo Pyaz"), "Dinner" to listOf("Roti", "Chicken Curry / Paneer")),
                "Saturday" to mapOf("Breakfast" to listOf("Puri Sabji", "Tea"), "Lunch" to listOf("Khichdi", "Curd"), "Dinner" to listOf("Roti", "Dal Fry")),
                "Sunday" to mapOf("Breakfast" to listOf("Dosa", "Tea"), "Lunch" to listOf("Veg Biryani", "Raita"), "Dinner" to listOf("Roti", "Shahi Paneer"))
            )
        }

        // Parse list of meals to calculate mock scores
        val textBuilder = StringBuilder()
        schedule.values.forEach { dayMap ->
            dayMap.values.forEach { items ->
                textBuilder.append(items.joinToString(" ")).append(" ")
            }
        }

        val parsedBase = parseOCRMenuText(textBuilder.toString())
        return parsedBase.copy(
            hostelName = "Hostel Block A",
            collegeName = "Indian Institute of Technology",
            schedule = schedule
        )
    }
}
