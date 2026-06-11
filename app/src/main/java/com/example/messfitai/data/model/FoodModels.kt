package com.example.messfitai.data.model

import kotlinx.serialization.Serializable

@Serializable
data class NutrientMacros(
    val calories: Float = 0f,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val fiber: Float = 0f,
    val micronutrients: String = "" // e.g. "Iron: 2mg, Calcium: 120mg"
)

@Serializable
data class MealItem(
    val name: String,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val fiber: Float = 0f,
    val micronutrients: String = "",
    val instruction: String = "Eat standard serving",
    val isAddedItem: Boolean = false, // True if outside food added for macros
    val estimatedCost: Float = 0f // Cost in INR if it is an outside item
)

@Serializable
data class DailyDiet(
    val dayOfWeek: String, // e.g. "Monday"
    val meals: Map<String, List<MealItem>> = emptyMap(), // "Breakfast" -> list, "Lunch" -> list, etc.
    val completed: Boolean = false
)

@Serializable
data class WeeklyMenu(
    val hostelName: String = "",
    val collegeName: String = "",
    val rating: Int = 60, // 0 to 100
    val proteinScore: Int = 50,
    val carbQualityScore: Int = 60,
    val fatQualityScore: Int = 55,
    val micronutrientScore: Int = 50,
    val bulkingSuitability: Int = 55,
    val cuttingSuitability: Int = 45,
    val vegFriendlinessScore: Int = 80,
    val schedule: Map<String, Map<String, List<String>>> = emptyMap() // "Monday" -> ("Breakfast" -> ["Poha", "Tea"])
)
