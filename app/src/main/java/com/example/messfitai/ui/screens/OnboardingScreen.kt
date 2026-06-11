package com.example.messfitai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messfitai.data.DataRepository
import com.example.messfitai.data.model.UserProfile
import com.example.messfitai.domain.DailyDietGenerator
import com.example.messfitai.domain.FitnessPlannerEngine
import com.example.messfitai.domain.MenuParserEngine
import com.example.messfitai.theme.CardBackground
import com.example.messfitai.theme.CardBorder
import com.example.messfitai.theme.DarkBackground
import com.example.messfitai.theme.NeonGreen
import com.example.messfitai.theme.TextGray
import com.example.messfitai.theme.TextGrayLight
import com.example.messfitai.theme.TextWhite

@Composable
fun OnboardingScreen(
    repository: DataRepository,
    onComplete: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    val scrollState = rememberScrollState()

    // Onboarding Form States
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("20") }
    var gender by remember { mutableStateOf("Male") }
    var height by remember { mutableStateOf("175") }
    var currentWeight by remember { mutableStateOf("75") }
    var goalWeight by remember { mutableStateOf("70") }
    var monthsAvailable by remember { mutableStateOf("3") }
    var activityLevel by remember { mutableStateOf("Moderate") }
    var gymExperience by remember { mutableStateOf("Beginner") }
    var vegetarianPreference by remember { mutableStateOf("Vegetarian") }
    var hostelName by remember { mutableStateOf("Hostel Block B") }
    var collegeName by remember { mutableStateOf("NIT Trichy") }
    var monthlyBudget by remember { mutableStateOf("1500") }
    var supplements by remember { mutableStateOf("None") }
    var sleepHours by remember { mutableStateOf("7.5") }
    var dailySteps by remember { mutableStateOf("10000") }
    var preferredSplit by remember { mutableStateOf("Push Pull Legs") }

    fun handleComplete() {
        val userProfile = UserProfile(
            name = name.ifEmpty { "Hostelite" },
            age = age.toIntOrNull() ?: 20,
            gender = gender,
            heightCm = height.toFloatOrNull() ?: 175f,
            weightKg = currentWeight.toFloatOrNull() ?: 75f,
            goalWeightKg = goalWeight.toFloatOrNull() ?: 70f,
            monthsAvailable = monthsAvailable.toIntOrNull() ?: 3,
            activityLevel = activityLevel,
            gymExperience = gymExperience,
            vegetarianPreference = vegetarianPreference,
            hostelName = hostelName.ifEmpty { "Hostel Block A" },
            collegeName = collegeName.ifEmpty { "Indian College" },
            monthlyBudget = monthlyBudget.toFloatOrNull() ?: 1500f,
            supplements = supplements,
            sleepHours = sleepHours.toFloatOrNull() ?: 7.5f,
            dailyStepsTarget = dailySteps.toIntOrNull() ?: 10000
        )

        repository.saveUserProfile(userProfile)

        // Initialize default Workout plan
        val workoutPlan = FitnessPlannerEngine.generateWorkoutPlan(userProfile, preferredSplit)
        repository.saveWorkoutSplit(workoutPlan)

        // Initialize default Scanned Menu to show some default items in Diet Plan screen
        val defaultPresetMenu = MenuParserEngine.getPresetMenu("Hostel Standard Mix")
        repository.saveWeeklyMenu(defaultPresetMenu)

        val diets = DailyDietGenerator.generateWeeklyDiet(userProfile, defaultPresetMenu)
        repository.saveDailyDiets(diets)

        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header Progress
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (step > 1) {
                    IconButton(onClick = { step-- }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NeonGreen)
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Text(
                    text = "Step $step of 4",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )

                Spacer(modifier = Modifier.width(48.dp))
            }

            // Progress bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CardBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(step / 4f)
                        .height(4.dp)
                        .background(NeonGreen)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (step) {
                1 -> {
                    Text("Personal Details", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
                    Text("Help us map your hostel environment", fontSize = 13.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(24.dp))

                    OnboardingField("Full Name", name, { name = it }, "Enter your name")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("Age", age, { age = it }, "Enter your age")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("College Name", collegeName, { collegeName = it }, "e.g. IIT Bombay")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("Hostel Name / Block", hostelName, { hostelName = it }, "e.g. Hostel 4, Room 204")
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Gender", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Male", "Female", "Other").forEach {
                            OnboardingChip(it, gender == it, { gender = it }, Modifier.weight(1f))
                        }
                    }
                }
                2 -> {
                    Text("Body Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
                    Text("We'll calculate your TDEE and target macros", fontSize = 13.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(24.dp))

                    OnboardingField("Height (in cm)", height, { height = it }, "e.g. 175")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("Current Weight (in kg)", currentWeight, { currentWeight = it }, "e.g. 75")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("Target Weight (in kg)", goalWeight, { goalWeight = it }, "e.g. 70")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("Months Available", monthsAvailable, { monthsAvailable = it }, "e.g. 3")
                }
                3 -> {
                    Text("Lifestyle & Budget", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
                    Text("Student budgets dictate diet parameters", fontSize = 13.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Vegetarian Preference", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Vegetarian", "Eggitarian", "Non-Veg").forEach {
                            val displayPref = when(it) {
                                "Non-Veg" -> "Non-Vegetarian"
                                else -> it
                            }
                            OnboardingChip(it, vegetarianPreference.startsWith(it), { vegetarianPreference = displayPref }, Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    OnboardingField("Monthly Supplements Budget (₹)", monthlyBudget, { monthlyBudget = it }, "e.g. 1500")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("Current Supplements", supplements, { supplements = it }, "e.g. Whey, none")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("Average Sleep Hours", sleepHours, { sleepHours = it }, "e.g. 7.5")
                    Spacer(modifier = Modifier.height(12.dp))
                    OnboardingField("Daily Walking Steps Goal", dailySteps, { dailySteps = it }, "e.g. 10000")
                }
                4 -> {
                    Text("Fitness Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
                    Text("Choose splits and intensity settings", fontSize = 13.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(24.dp))

                    Text("Activity Level", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Sedentary", "Light", "Moderate", "Active").forEach {
                            OnboardingListItemChip(
                                title = it,
                                subtitle = when(it) {
                                    "Sedentary" -> "Little to no exercise"
                                    "Light" -> "Exercise 1-3 times a week"
                                    "Moderate" -> "Exercise 3-5 times a week"
                                    else -> "Daily heavy exercise"
                                },
                                selected = activityLevel == it,
                                onClick = { activityLevel = it }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Gym Experience", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Beginner", "Intermediate", "Advanced").forEach {
                            OnboardingChip(it, gymExperience == it, { gymExperience = it }, Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Preferred Workout Split", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("Push Pull Legs", "Home Workout").forEach {
                            OnboardingChip(it, preferredSplit == it, { preferredSplit = it }, Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Navigation Button
            Button(
                onClick = {
                    if (step < 4) {
                        step++
                    } else {
                        handleComplete()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    contentColor = DarkBackground
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = if (step < 4) "NEXT STEP" else "CREATE PLAN",
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
internal fun OnboardingField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonGreen,
                unfocusedBorderColor = CardBorder,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite,
                focusedPlaceholderColor = TextGray,
                unfocusedPlaceholderColor = TextGray
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
internal fun OnboardingChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) NeonGreen else CardBackground)
            .border(1.dp, if (selected) NeonGreen else CardBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) DarkBackground else TextWhite
        )
    }
}

@Composable
internal fun OnboardingListItemChip(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) NeonGreen else CardBackground)
            .border(1.dp, if (selected) NeonGreen else CardBorder, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) DarkBackground else TextWhite
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = if (selected) DarkBackground.copy(alpha = 0.7f) else TextGray
            )
        }
    }
}
