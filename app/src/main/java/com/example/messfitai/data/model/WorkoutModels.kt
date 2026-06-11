package com.example.messfitai.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val sets: Int,
    val reps: String, // e.g., "12-15" or "8-10" or "Failure"
    val restSeconds: Int = 60,
    val targetMuscle: String,
    val technique: String,
    val commonMistakes: List<String> = emptyList(),
    val videoUrl: String = "",
    val animationResName: String = "" // Placeholder for visual representations
)

@Serializable
data class DailyWorkout(
    val dayName: String, // e.g., "Day 1 - Push"
    val focusArea: String, // e.g., "Chest, Shoulders, Triceps"
    val exercises: List<Exercise> = emptyList()
)

@Serializable
data class WorkoutSplit(
    val name: String, // e.g., "Push Pull Legs"
    val level: String, // e.g., "Beginner"
    val dailyRoutines: List<DailyWorkout> = emptyList(),
    val cardioRecommendation: String = ""
)
