package com.example.messfitai.domain

import com.example.messfitai.data.model.UserProfile
import kotlin.math.roundToInt

object BodyAnalysisEngine {

    data class BodyAnalysisResult(
        val bmi: Float,
        val bmiCategory: String,
        val bmr: Float,
        val tdee: Float,
        val recommendedCalories: Float,
        val recommendedProteinGrams: Float,
        val recommendedFatGrams: Float,
        val recommendedCarbsGrams: Float,
        val dailyWaterLiters: Float,
        val expectedWeightChangeKgPerMonth: Float,
        val totalExpectedWeightChangeKg: Float
    )

    fun analyze(profile: UserProfile): BodyAnalysisResult {
        // BMI Calculation
        val heightMeters = profile.heightCm / 100f
        val bmi = if (heightMeters > 0) profile.weightKg / (heightMeters * heightMeters) else 0f
        val bmiCategory = when {
            bmi < 18.5f -> "Underweight"
            bmi < 25.0f -> "Normal weight"
            bmi < 30.0f -> "Overweight"
            else -> "Obese"
        }

        // BMR Calculation (Mifflin-St Jeor Equation)
        val bmr = if (profile.gender.equals("Female", ignoreCase = true)) {
            (10f * profile.weightKg) + (6.25f * profile.heightCm) - (5f * profile.age) - 161f
        } else {
            (10f * profile.weightKg) + (6.25f * profile.heightCm) - (5f * profile.age) + 5f
        }

        // TDEE Activity Multiplier
        val multiplier = when (profile.activityLevel) {
            "Sedentary" -> 1.2f
            "Light" -> 1.375f
            "Moderate" -> 1.55f
            "Active" -> 1.725f
            "Very Active" -> 1.9f
            else -> 1.55f
        }
        val tdee = bmr * multiplier

        // Recommended Calories based on Goal
        // Goals are derived from weightKg vs goalWeightKg or explicitly
        val weightDifference = profile.goalWeightKg - profile.weightKg
        val recommendedCalories = when {
            weightDifference < -2f -> tdee - 500f // Cutting / Weight Loss
            weightDifference > 2f -> tdee + 400f  // Bulking / Weight Gain
            else -> tdee // Maintenance
        }

        // Recommended Protein (grams per kg of body weight)
        val proteinMultiplier = when (profile.gymExperience) {
            "Beginner" -> 1.6f
            "Intermediate" -> 1.9f
            "Advanced" -> 2.2f
            else -> 1.8f
        }
        val recommendedProteinGrams = profile.weightKg * proteinMultiplier

        // Recommended Fat (approx 25% of daily calories)
        // Fat is 9 calories/gram
        val recommendedFatGrams = (recommendedCalories * 0.25f) / 9f

        // Recommended Carbs (remaining calories)
        // Carbs are 4 calories/gram
        val proteinCalories = recommendedProteinGrams * 4f
        val fatCalories = recommendedFatGrams * 9f
        val remainingCalories = recommendedCalories - proteinCalories - fatCalories
        val recommendedCarbsGrams = if (remainingCalories > 0) remainingCalories / 4f else 0f

        // Daily Water Intake (35ml per kg + supplement adjustments)
        var waterLiters = (profile.weightKg * 35f) / 1000f
        if (profile.activityLevel == "Active" || profile.activityLevel == "Very Active") {
            waterLiters += 0.8f
        }

        // Expected Weight Gain/Loss per month (standard 0.5kg per week deficit math = ~2kg/month)
        val expectedWeightChangeKgPerMonth = when {
            weightDifference < -2f -> -2.0f // Lose 2kg per month
            weightDifference > 2f -> 1.5f  // Gain 1.5kg per month (muscle + minimal fat)
            else -> 0f
        }
        val totalExpectedWeightChangeKg = expectedWeightChangeKgPerMonth * profile.monthsAvailable

        return BodyAnalysisResult(
            bmi = (bmi * 10).roundToInt() / 10f,
            bmiCategory = bmiCategory,
            bmr = bmr.roundToInt().toFloat(),
            tdee = tdee.roundToInt().toFloat(),
            recommendedCalories = recommendedCalories.roundToInt().toFloat(),
            recommendedProteinGrams = recommendedProteinGrams.roundToInt().toFloat(),
            recommendedFatGrams = recommendedFatGrams.roundToInt().toFloat(),
            recommendedCarbsGrams = recommendedCarbsGrams.roundToInt().toFloat(),
            dailyWaterLiters = (waterLiters * 10).roundToInt() / 10f,
            expectedWeightChangeKgPerMonth = expectedWeightChangeKgPerMonth,
            totalExpectedWeightChangeKg = totalExpectedWeightChangeKg
        )
    }
}
