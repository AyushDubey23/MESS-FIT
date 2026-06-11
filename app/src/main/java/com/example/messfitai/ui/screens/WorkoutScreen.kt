package com.example.messfitai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messfitai.data.DataRepository
import com.example.messfitai.theme.AlertRed
import com.example.messfitai.theme.CardBackground
import com.example.messfitai.theme.CardBorder
import com.example.messfitai.theme.DarkBackground
import com.example.messfitai.theme.NeonGreen
import com.example.messfitai.theme.NeonGreenGlow
import com.example.messfitai.theme.TextGray
import com.example.messfitai.theme.TextGrayLight
import com.example.messfitai.theme.TextWhite

@Composable
fun WorkoutScreen(
    repository: DataRepository
) {
    val scrollState = rememberScrollState()
    val split by repository.workoutSplit.collectAsState()

    var activeDayIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("AI Fitness Planner", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
            Text("Scientific training routines mapped to your equipment", fontSize = 12.sp, color = TextGray)

            Spacer(modifier = Modifier.height(20.dp))

            if (split != null) {
                // Header Split Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(split!!.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text("Experience: ${split!!.level}", fontSize = 11.sp, color = TextGray)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonGreenGlow)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ACTIVE SPLIT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NeonGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Days Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    split!!.dailyRoutines.forEachIndexed { index, routine ->
                        val isSelected = activeDayIndex == index
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) NeonGreen else CardBackground)
                                .border(1.dp, if (isSelected) NeonGreen else CardBorder, RoundedCornerShape(10.dp))
                                .clickable { activeDayIndex = index }
                        ) {
                            Text(
                                text = "Day ${index + 1}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) DarkBackground else TextWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Exercises List
                val activeRoutine = split!!.dailyRoutines.getOrNull(activeDayIndex)
                if (activeRoutine != null) {
                    Text(
                        text = "${activeRoutine.dayName} • Focus: ${activeRoutine.focusArea}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    activeRoutine.exercises.forEach { exercise ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(CardBackground)
                                .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(exercise.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text(exercise.targetMuscle, fontSize = 11.sp, color = NeonGreen, fontWeight = FontWeight.Bold)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(CardBorder)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${exercise.sets} Sets x ${exercise.reps} (${exercise.restSeconds}s Rest)",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Video Demonstration Placeholder Card
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(DarkBackground)
                                        .border(1.dp, CardBorder, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Play",
                                            tint = NeonGreen,
                                            modifier = Modifier.size(36.dp)
                                        )
                                        Text(
                                            text = "Tap to load 3D Form Animation",
                                            fontSize = 10.sp,
                                            color = TextGray
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text("Technique Guide:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                Text(
                                    text = exercise.technique,
                                    fontSize = 11.sp,
                                    color = TextGrayLight,
                                    lineHeight = 16.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text("Common Mistakes:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AlertRed)
                                Column(modifier = Modifier.padding(top = 2.dp)) {
                                    exercise.commonMistakes.forEach { mistake ->
                                        Row(modifier = Modifier.padding(vertical = 1.dp)) {
                                            Text("• ", color = AlertRed, fontSize = 11.sp)
                                            Text(mistake, color = TextGrayLight, fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cardio engine recommendations card
                Text("AI Cardio Engine Recommendations", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Row {
                        Icon(Icons.Default.DirectionsRun, contentDescription = "Cardio", tint = NeonGreen)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Cardio Guideline", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = split!!.cardioRecommendation,
                                fontSize = 11.sp,
                                color = TextGrayLight,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No workout plan generated. Please complete onboarding first.", color = TextGray)
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
