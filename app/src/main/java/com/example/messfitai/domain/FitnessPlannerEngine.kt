package com.example.messfitai.domain

import com.example.messfitai.data.model.Exercise
import com.example.messfitai.data.model.DailyWorkout
import com.example.messfitai.data.model.WorkoutSplit
import com.example.messfitai.data.model.UserProfile

object FitnessPlannerEngine {

    fun generateWorkoutPlan(profile: UserProfile, splitName: String): WorkoutSplit {
        val level = profile.gymExperience
        val isHome = splitName.equals("Home Workout", ignoreCase = true)
        
        val dailyRoutines = if (isHome) {
            generateHomeWorkouts(level)
        } else {
            when (splitName.lowercase().trim()) {
                "push pull legs", "ppl" -> generatePPLWorkouts(level)
                "bro split" -> generateBroSplitWorkouts(level)
                "arnold split" -> generateArnoldSplitWorkouts(level)
                "upper lower" -> generateUpperLowerWorkouts(level)
                else -> generateFullBodyWorkouts(level)
            }
        }

        val weightDiff = profile.goalWeightKg - profile.weightKg
        val cardioRec = when {
            weightDiff < -5f -> "Incline Walking: 30 mins, 4-5 times a week at 12% incline, 5.0 km/h. Or HIIT: 15 mins post-workout."
            weightDiff < -2f -> "Walking: 10,000 steps daily + 15 mins light jogging, 3 times a week."
            weightDiff > 5f -> "Minimal Cardio: 5,000 steps daily to maximize calorie surplus for muscle building."
            else -> "Regular Cardio: 20 mins cycling or brisk walking, 3 times a week for cardiovascular health."
        }

        return WorkoutSplit(
            name = splitName,
            level = level,
            dailyRoutines = dailyRoutines,
            cardioRecommendation = cardioRec
        )
    }

    private fun generatePPLWorkouts(level: String): List<DailyWorkout> {
        val sets = if (level == "Beginner") 3 else 4
        val reps = if (level == "Beginner") "10-12" else "8-10"

        return listOf(
            DailyWorkout(
                dayName = "Day 1 - Push",
                focusArea = "Chest, Shoulders & Triceps",
                exercises = listOf(
                    Exercise(
                        name = "Incline Dumbbell Press",
                        sets = sets,
                        reps = reps,
                        restSeconds = 90,
                        targetMuscle = "Upper Chest & Front Delts",
                        technique = "Keep elbows tucked at a 45-degree angle. Press up in a controlled arc.",
                        commonMistakes = listOf("Flaring elbows out too wide", "Bouncing weights off chest")
                    ),
                    Exercise(
                        name = "Overhead Barbell Press",
                        sets = sets,
                        reps = "8-10",
                        restSeconds = 120,
                        targetMuscle = "Shoulders & Triceps",
                        technique = "Brace core, squeeze glutes, press straight up and push head through at top.",
                        commonMistakes = listOf("Arching lower back excessively", "Not locking out elbows")
                    ),
                    Exercise(
                        name = "Flat Bench Press (Barbell/Dumbbell)",
                        sets = sets,
                        reps = reps,
                        restSeconds = 90,
                        targetMuscle = "Mid Chest & Triceps",
                        technique = "Retract scapula, plant feet firmly, lower bar to mid-chest, press up.",
                        commonMistakes = listOf("Lifting feet off the ground", "Half reps")
                    ),
                    Exercise(
                        name = "Dumbbell Lateral Raise",
                        sets = sets + 1,
                        reps = "12-15",
                        restSeconds = 60,
                        targetMuscle = "Side Deltoid (Shoulders)",
                        technique = "Slight forward lean, raise arms to sides, pinkies pointing slightly up.",
                        commonMistakes = listOf("Using body momentum", "Raising arms too high above shoulders")
                    ),
                    Exercise(
                        name = "Tricep Overhead Extension",
                        sets = sets,
                        reps = "10-12",
                        restSeconds = 60,
                        targetMuscle = "Triceps (Long head)",
                        technique = "Keep elbows locked near ears, lower weight behind head and press upward.",
                        commonMistakes = listOf("Elbows flaring outward", "Too rapid eccentric movement")
                    )
                )
            ),
            DailyWorkout(
                dayName = "Day 2 - Pull",
                focusArea = "Back, Rear Delts & Biceps",
                exercises = listOf(
                    Exercise(
                        name = "Lat Pulldown (or Pull-ups)",
                        sets = sets,
                        reps = "8-12",
                        restSeconds = 90,
                        targetMuscle = "Lats (Upper Back)",
                        technique = "Pull bar down to upper chest, squeeze shoulder blades together, control return.",
                        commonMistakes = listOf("Leaning back too far", "Pulling with biceps instead of back")
                    ),
                    Exercise(
                        name = "Bent Over Barbell Row",
                        sets = sets,
                        reps = reps,
                        restSeconds = 90,
                        targetMuscle = "Mid Back & Rhomboids",
                        technique = "Hinge at hips, keep back flat, pull barbell towards belly button.",
                        commonMistakes = listOf("Rounding the spine", "Standing too upright")
                    ),
                    Exercise(
                        name = "Face Pulls (Cable)",
                        sets = sets,
                        reps = "15",
                        restSeconds = 60,
                        targetMuscle = "Rear Delts & Upper Traps",
                        technique = "Pull rope toward nose, flare elbows out, squeeze back of shoulders.",
                        commonMistakes = listOf("Pulling too heavy", "Not holding the peak contraction")
                    ),
                    Exercise(
                        name = "Incline Dumbbell Bicep Curl",
                        sets = sets,
                        reps = "10-12",
                        restSeconds = 60,
                        targetMuscle = "Biceps (Long head)",
                        technique = "Sit on incline bench, keep elbows pinned back, curl dumbbells fully.",
                        commonMistakes = listOf("Swinging elbows forward", "Not squeezing at the top")
                    ),
                    Exercise(
                        name = "Dumbbell Hammer Curl",
                        sets = sets,
                        reps = "10-12",
                        restSeconds = 60,
                        targetMuscle = "Brachialis & Forearms",
                        technique = "Neutral grip (palms facing), curl weight, squeeze forearm at top.",
                        commonMistakes = listOf("Using hip swing", "Incomplete range of motion")
                    )
                )
            ),
            DailyWorkout(
                dayName = "Day 3 - Legs",
                focusArea = "Quads, Hamstrings & Calves",
                exercises = listOf(
                    Exercise(
                        name = "Barbell Back Squat",
                        sets = sets,
                        reps = "8-10",
                        restSeconds = 120,
                        targetMuscle = "Quads, Glutes & Core",
                        technique = "Bar on upper traps, feet shoulder-width, squat down until thighs are parallel to ground.",
                        commonMistakes = listOf("Knees caving inward", "Heels lifting off floor")
                    ),
                    Exercise(
                        name = "Romanian Deadlift (RDL)",
                        sets = sets,
                        reps = "10-12",
                        restSeconds = 90,
                        targetMuscle = "Hamstrings & Glutes",
                        technique = "Hinge at hips, slide bar down legs, keep back flat, squeeze glutes to stand.",
                        commonMistakes = listOf("Bending knees too much", "Rounding lower back")
                    ),
                    Exercise(
                        name = "Leg Press (or Lunges)",
                        sets = sets,
                        reps = "10-12",
                        restSeconds = 90,
                        targetMuscle = "Quads & Glutes",
                        technique = "Lower sled slowly, do not let lower back lift off pad, press through heels.",
                        commonMistakes = listOf("Locking knees at top", "Half range of motion")
                    ),
                    Exercise(
                        name = "Standing Calf Raise",
                        sets = sets + 1,
                        reps = "15-20",
                        restSeconds = 60,
                        targetMuscle = "Gastrocnemius (Calves)",
                        technique = "Get full stretch at bottom, press up on toes, hold for 1 sec.",
                        commonMistakes = listOf("Bouncing rapidly", "Not completing full stretch")
                    )
                )
            )
        )
    }

    private fun generateHomeWorkouts(level: String): List<DailyWorkout> {
        val reps = if (level == "Beginner") "8-12" else "15-20"
        
        return listOf(
            DailyWorkout(
                dayName = "Day 1 - Upper Body (Home)",
                focusArea = "Chest, Back, Shoulders & Arms",
                exercises = listOf(
                    Exercise(
                        name = "Standard Pushups",
                        sets = 4,
                        reps = reps,
                        restSeconds = 60,
                        targetMuscle = "Chest, Shoulders & Triceps",
                        technique = "Plank position, hands shoulder-width, lower chest to floor, push up.",
                        commonMistakes = listOf("Sagging hips", "Elbows flaring 90 degrees")
                    ),
                    Exercise(
                        name = "Doorframe / Table Rows",
                        sets = 4,
                        reps = "10-15",
                        restSeconds = 60,
                        targetMuscle = "Lats & Mid Back",
                        technique = "Hold side of doorframe or bottom of sturdy table, pull chest towards handle/rim.",
                        commonMistakes = listOf("Jerking body", "Loose core")
                    ),
                    Exercise(
                        name = "Pike Pushups",
                        sets = 3,
                        reps = "8-12",
                        restSeconds = 60,
                        targetMuscle = "Shoulders & Upper Chest",
                        technique = "V-shape position (hips high), lower head towards ground between hands, push up.",
                        commonMistakes = listOf("Flat back instead of V-shape", "Lowering straight down instead of forward")
                    ),
                    Exercise(
                        name = "Chair Dips",
                        sets = 3,
                        reps = "12-15",
                        restSeconds = 60,
                        targetMuscle = "Triceps & Chest",
                        technique = "Hands on edge of chair/bed, lower hips towards floor, push up.",
                        commonMistakes = listOf("Shoulders rolling forward", "Hips too far from chair")
                    ),
                    Exercise(
                        name = "Towel Bicep Iso-Holds",
                        sets = 3,
                        reps = "30s hold",
                        restSeconds = 45,
                        targetMuscle = "Biceps",
                        technique = "Stand on middle of towel, hold ends with palms up, pull up with max force statically.",
                        commonMistakes = listOf("Poor posture", "Not contracting biceps fully")
                    )
                )
            ),
            DailyWorkout(
                dayName = "Day 2 - Lower Body & Core (Home)",
                focusArea = "Quads, Hamstrings, Calves & Abs",
                exercises = listOf(
                    Exercise(
                        name = "Bodyweight Squats",
                        sets = 4,
                        reps = "20-25",
                        restSeconds = 60,
                        targetMuscle = "Quads & Glutes",
                        technique = "Feet shoulder-width, sit back, squat below parallel, stand up.",
                        commonMistakes = listOf("Knees caving", "Looking straight down")
                    ),
                    Exercise(
                        name = "Walking Lunges",
                        sets = 3,
                        reps = "12 steps per leg",
                        restSeconds = 60,
                        targetMuscle = "Quads & Hamstrings",
                        technique = "Step forward, lower back knee close to floor, push through front heel to step up.",
                        commonMistakes = listOf("Front knee sliding past toes", "Losing balance")
                    ),
                    Exercise(
                        name = "Single-Leg Glute Bridge",
                        sets = 3,
                        reps = "12-15 per leg",
                        restSeconds = 60,
                        targetMuscle = "Glutes & Hamstrings",
                        technique = "Lie on back, bend knees, lift one leg, press through heel of other foot to raise hips.",
                        commonMistakes = listOf("Arching lower back", "Not squeezing glutes")
                    ),
                    Exercise(
                        name = "Single-Leg Calf Raise (on step)",
                        sets = 4,
                        reps = "15-20 per leg",
                        restSeconds = 45,
                        targetMuscle = "Calves",
                        technique = "Stand on edge of step on one foot, stretch down, push high on toes.",
                        commonMistakes = listOf("Using hands to push up", "Bouncing rapidly")
                    ),
                    Exercise(
                        name = "Bicycle Crunches",
                        sets = 3,
                        reps = "20 reps",
                        restSeconds = 45,
                        targetMuscle = "Rectus Abdominis & Obliques",
                        technique = "Lie down, alternate elbow-to-knee touching in a pedaling motion.",
                        commonMistakes = listOf("Pulling neck with hands", "Moving too fast")
                    )
                )
            )
        )
    }

    private fun generateBroSplitWorkouts(level: String): List<DailyWorkout> {
        val sets = if (level == "Beginner") 3 else 4
        return listOf(
            DailyWorkout("Day 1 - Chest", "Pectorals", listOf(
                Exercise("Flat Bench Press", sets, "8-10", 90, "Chest", "Plant feet, retract shoulder blades, press."),
                Exercise("Incline DB Fly", sets, "12", 60, "Upper Chest", "Slight bend in elbows, open wide, hug a tree at top.")
            )),
            DailyWorkout("Day 2 - Back", "Lats & Rhomboids", listOf(
                Exercise("Deadlift", 3, "5", 120, "Lower Back & Hamstrings", "Flat back, push through floor, stand tall."),
                Exercise("Seated Cable Row", sets, "10", 90, "Mid Back", "Pull handle to lower ribs, squeeze shoulder blades.")
            )),
            DailyWorkout("Day 3 - Shoulders", "Deltoids", listOf(
                Exercise("Seated DB Shoulder Press", sets, "8-10", 90, "Shoulders", "Press DBs up vertically, control down."),
                Exercise("DB Lateral Raise", 4, "12-15", 60, "Side Delts", "Raise arms to side, pinky up.")
            )),
            DailyWorkout("Day 4 - Arms", "Biceps & Triceps", listOf(
                Exercise("Barbell Curl", sets, "10", 60, "Biceps", "Keep elbows pinned, curl bar fully."),
                Exercise("Tricep Pushdown", sets, "12", 60, "Triceps", "Extend elbow down, squeeze tricep.")
            )),
            DailyWorkout("Day 5 - Legs", "Quads & Hamstrings", listOf(
                Exercise("Leg Press", sets, "10-12", 90, "Quads", "Control sled down, press through heels."),
                Exercise("Lying Leg Curl", sets, "12", 60, "Hamstrings", "Pull heels to glutes, squeeze.")
            ))
        )
    }

    private fun generateArnoldSplitWorkouts(level: String): List<DailyWorkout> {
        val sets = if (level == "Beginner") 3 else 4
        return listOf(
            DailyWorkout("Day 1 - Chest & Back", "Torso", listOf(
                Exercise("Incline Bench Press", sets, "8-10", 90, "Chest", "Press bar in controlled arc."),
                Exercise("Pull-ups", sets, "Failure", 90, "Back", "Pull chin over bar, control down."),
                Exercise("Flat DB Press", sets, "10", 90, "Chest", "Squeeze chest at top."),
                Exercise("One Arm DB Row", sets, "10", 60, "Back", "Pull dumbbell to hip.")
            )),
            DailyWorkout("Day 2 - Shoulders & Arms", "Shoulders/Arms", listOf(
                Exercise("Military Press", sets, "8", 90, "Shoulders", "Press barbell overhead from chest."),
                Exercise("Side Lat Raises", 4, "12", 60, "Shoulders", "Raise DBs out to sides."),
                Exercise("Incline DB Curl", sets, "10", 60, "Biceps", "Pin elbows back, curl dumbbell."),
                Exercise("Lying Skull Crushers", sets, "10", 60, "Triceps", "Lower bar to forehead, press up.")
            )),
            DailyWorkout("Day 3 - Legs", "Lower Body", listOf(
                Exercise("Back Squat", sets, "8-10", 120, "Quads", "Squat low, press up."),
                Exercise("Romanian Deadlift", sets, "10", 90, "Hamstrings", "Hinge hips back, feel hamstring stretch."),
                Exercise("Seated Calf Raise", 4, "15", 60, "Calves", "Stretch calves fully, push up.")
            ))
        )
    }

    private fun generateUpperLowerWorkouts(level: String): List<DailyWorkout> {
        val sets = if (level == "Beginner") 3 else 4
        return listOf(
            DailyWorkout("Day 1 - Upper Body", "Chest, Back, Shoulders, Arms", listOf(
                Exercise("Dumbbell Bench Press", sets, "8-10", 90, "Chest", "Press dumbbells up, control down."),
                Exercise("Lat Pulldown", sets, "10-12", 90, "Back", "Pull bar to collarbone."),
                Exercise("DB Lateral Raise", 3, "12", 60, "Shoulders", "Raise arms to side."),
                Exercise("Dumbbell Curl", 3, "12", 60, "Biceps", "Alternate curls, twist pinky up.")
            )),
            DailyWorkout("Day 2 - Lower Body", "Quads, Hamstrings, Calves", listOf(
                Exercise("Leg Press", sets, "10-12", 90, "Quads", "Press up with heels."),
                Exercise("Lying Leg Curl", sets, "12", 60, "Hamstrings", "Curl legs up, squeeze."),
                Exercise("Walking Lunges", 3, "10 steps/leg", 60, "Quads/Glutes", "Lunges in controlled steps."),
                Exercise("Standing Calf Raise", 4, "15", 45, "Calves", "Raise on toes, squeeze.")
            ))
        )
    }

    private fun generateFullBodyWorkouts(level: String): List<DailyWorkout> {
        val sets = if (level == "Beginner") 3 else 4
        return listOf(
            DailyWorkout("Day 1 - Full Body", "Quadriceps, Pectorals, Lats, Deltoids", listOf(
                Exercise("Back Squat", sets, "8-10", 120, "Quads/Glutes", "Feet shoulder-width, squat parallel."),
                Exercise("Flat Bench Press", sets, "8-10", 90, "Chest", "Press bar in straight line."),
                Exercise("Bent Over Barbell Row", sets, "10", 90, "Back", "Hinge forward, pull bar to waist."),
                Exercise("Dumbbell Shoulder Press", 3, "10", 90, "Shoulders", "Press dumbbells vertically overhead."),
                Exercise("Bicep Barbell Curl", 3, "12", 60, "Biceps", "Curl bar without swinging hips.")
            ))
        )
    }
}
