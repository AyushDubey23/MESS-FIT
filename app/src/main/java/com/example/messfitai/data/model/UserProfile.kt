package com.example.messfitai.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val name: String = "",
    val age: Int = 20,
    val gender: String = "Male", // "Male", "Female", "Other"
    val heightCm: Float = 175f,
    val weightKg: Float = 75f,
    val goalWeightKg: Float = 70f,
    val monthsAvailable: Int = 3,
    val activityLevel: String = "Moderate", // "Sedentary", "Light", "Moderate", "Active", "Very Active"
    val gymExperience: String = "Beginner", // "Beginner", "Intermediate", "Advanced"
    val vegetarianPreference: String = "Vegetarian", // "Vegetarian", "Non-Vegetarian", "Eggitarian"
    val hostelName: String = "",
    val collegeName: String = "",
    val monthlyBudget: Float = 1500f, // in INR (₹)
    val supplements: String = "", // e.g. "Whey Protein, Creatine"
    val sleepHours: Float = 7.5f,
    val dailyStepsTarget: Int = 10000
)
