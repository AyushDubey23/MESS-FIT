package com.example.messfitai.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messfitai.data.DataRepository
import com.example.messfitai.domain.BodyAnalysisEngine
import com.example.messfitai.theme.AlertGold
import com.example.messfitai.theme.AlertRed
import com.example.messfitai.theme.AlertRedGlow
import com.example.messfitai.theme.CardBackground
import com.example.messfitai.theme.CardBorder
import com.example.messfitai.theme.DarkBackground
import com.example.messfitai.theme.NeonGreen
import com.example.messfitai.theme.NeonGreenGlow
import com.example.messfitai.theme.TextGray
import com.example.messfitai.theme.TextGrayLight
import com.example.messfitai.theme.TextWhite
import java.util.Calendar

@Composable
fun DashboardScreen(
    repository: DataRepository,
    onNavigateTo: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    val profile by repository.userProfile.collectAsState()
    val menu by repository.weeklyMenu.collectAsState()
    val diets by repository.dailyDiets.collectAsState()
    val water by repository.waterLoggedMl.collectAsState()
    val steps by repository.stepsLogged.collectAsState()
    val split by repository.workoutSplit.collectAsState()

    // Calculate current day of the week
    val dayOfWeek = remember {
        val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1]
    }

    val analysis = remember(profile) { BodyAnalysisEngine.analyze(profile) }
    
    // Retrieve today's diet details
    val todayDiet = diets.firstOrNull { it.dayOfWeek.equals(dayOfWeek, ignoreCase = true) } ?: diets.firstOrNull()
    val isDayCompleted = todayDiet?.completed == true

    // Calculate calories & protein consumed today vs goals
    var consumedCalories = 0f
    var consumedProtein = 0f
    
    if (todayDiet != null) {
        todayDiet.meals.values.flatten().forEach {
            consumedCalories += it.calories
            consumedProtein += it.protein
        }
    }

    // Deficit math
    val targetCalories = analysis.recommendedCalories
    val targetProtein = analysis.recommendedProteinGrams
    
    val proteinDeficit = (targetProtein - consumedProtein).coerceAtLeast(0f)
    val showDeficitAlert = proteinDeficit > 15f && !isDayCompleted

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

            // Top Profile Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, ${profile.name} 👋",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "${profile.collegeName} • ${profile.hostelName}",
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NeonGreenGlow)
                        .border(1.dp, NeonGreen, CircleShape)
                        .clickable { onNavigateTo("About") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.name.firstOrNull()?.uppercase() ?: "S",
                        fontWeight = FontWeight.Black,
                        color = NeonGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Macros circular trackers
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Calories Ring
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Calories", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(12.dp))
                        ProgressRing(
                            progress = (consumedCalories / targetCalories).coerceIn(0f, 1f),
                            color = NeonGreen,
                            modifier = Modifier.size(90.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = consumedCalories.toInt().toString(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Text(
                                    text = "/${targetCalories.toInt()}",
                                    fontSize = 11.sp,
                                    color = TextGray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Protein Ring
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Protein", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        Spacer(modifier = Modifier.height(12.dp))
                        ProgressRing(
                            progress = (consumedProtein / targetProtein).coerceIn(0f, 1f),
                            color = NeonGreen,
                            modifier = Modifier.size(90.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${consumedProtein.toInt()}g",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextWhite
                                )
                                Text(
                                    text = "/${targetProtein.toInt()}g",
                                    fontSize = 11.sp,
                                    color = TextGray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Protein Deficit Detector Alert
            if (showDeficitAlert) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(AlertRedGlow)
                        .border(1.dp, AlertRed, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Deficit", tint = AlertRed)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Protein Deficit Detected! (-${proteinDeficit.toInt()}g)",
                            color = AlertRed,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Add Soya Chunks (₹6) or 2 Eggs (₹14) to hit your daily goal.",
                            color = TextGrayLight,
                            fontSize = 11.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Water Ring and Steps logs
            Row(modifier = Modifier.fillMaxWidth()) {
                // Water log
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Water Intake", fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${(water / 1000f)}L / ${analysis.dailyWaterLiters}L",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { repository.logWater(-250) },
                            modifier = Modifier
                                .size(32.dp)
                                .background(CardBorder, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Minus", tint = TextWhite, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = { repository.logWater(250) },
                            modifier = Modifier
                                .size(32.dp)
                                .background(NeonGreen, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Plus", tint = DarkBackground, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Steps log
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Walking Steps", fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "$steps / ${profile.dailyStepsTarget}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Button(
                            onClick = { repository.logSteps(1000) },
                            colors = ButtonDefaults.buttonColors(containerColor = CardBorder, contentColor = TextWhite),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("+1k Steps", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Goal weight progress card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Weight Goal Progress", fontSize = 13.sp, color = TextGray, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${profile.monthsAvailable} months",
                            fontSize = 11.sp,
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${profile.weightKg} kg", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            Text("Current", fontSize = 10.sp, color = TextGray)
                        }
                        
                        // Simple progress line
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 16.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(CardBorder)
                        ) {
                            val diff = profile.weightKg - profile.goalWeightKg
                            val percent = if (diff == 0f) 1f else 0.5f // mock weight progress percent
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(percent)
                                    .height(4.dp)
                                    .background(NeonGreen)
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("${profile.goalWeightKg} kg", fontSize = 20.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            Text("Target", fontSize = 10.sp, color = TextGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    
                    Text(
                        text = "Expected Monthly Trend: ${if (profile.goalWeightKg < profile.weightKg) "" else "+"}${analysis.expectedWeightChangeKgPerMonth} kg/month",
                        fontSize = 11.sp,
                        color = TextGrayLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weekly Mess Analysis Card
            if (menu != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .clickable { onNavigateTo("MenuScanner") }
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Weekly Mess Analysis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonGreenGlow)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Rating: ${menu!!.rating}/100",
                                    fontSize = 12.sp,
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${menu!!.hostelName} • ${menu!!.collegeName}",
                            fontSize = 11.sp,
                            color = TextGray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf(
                                "Protein" to "${menu!!.proteinScore}%",
                                "Veg Friend" to "${menu!!.vegFriendlinessScore}%",
                                "Bulking" to "${menu!!.bulkingSuitability}%",
                                "Cutting" to "${menu!!.cuttingSuitability}%"
                            ).forEach { (label, value) ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text(label, fontSize = 10.sp, color = TextGray)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // End of Day adherence button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isDayCompleted) NeonGreenGlow else CardBackground)
                    .border(1.dp, if (isDayCompleted) NeonGreen else CardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Night Adherence Logger",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Log if you completed today's AI planned diet target successfully.",
                        fontSize = 11.sp,
                        color = TextGray,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isDayCompleted) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Done", tint = NeonGreen, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Completed! Goal Adherence: 100%", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { repository.setDayCompleted(dayOfWeek, false) },
                            colors = ButtonDefaults.buttonColors(containerColor = CardBorder, contentColor = TextWhite),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Reset Completed Log", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { repository.setDayCompleted(dayOfWeek, true) },
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = DarkBackground),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("I COMPLETED TODAY'S DIET", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer branding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MESSFIT AI • Created by Ayush Dubey",
                    fontSize = 11.sp,
                    color = TextGray,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(60.dp)) // height padding for bottom bar
        }
    }
}

@Composable
fun ProgressRing(
    progress: Float,
    color: Color,
    ringWidth: Dp = 8.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = color.copy(alpha = 0.08f),
                style = Stroke(width = ringWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = progress * 360f,
                useCenter = false,
                style = Stroke(width = ringWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        content()
    }
}
