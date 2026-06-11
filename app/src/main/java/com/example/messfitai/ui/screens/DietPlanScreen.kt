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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.messfitai.data.model.DailyDiet
import com.example.messfitai.data.model.MealItem
import com.example.messfitai.domain.BodyAnalysisEngine
import com.example.messfitai.theme.AlertBlue
import com.example.messfitai.theme.AlertBlueGlow
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
fun DietPlanScreen(
    repository: DataRepository
) {
    val scrollState = rememberScrollState()
    
    val profile by repository.userProfile.collectAsState()
    val diets by repository.dailyDiets.collectAsState()

    // Default to today's day of week
    val currentDay = remember {
        val days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1]
    }
    
    var activeDay by remember { mutableStateOf(currentDay) }

    val analysis = remember(profile) { BodyAnalysisEngine.analyze(profile) }
    
    // Find active day diet
    val activeDiet = diets.firstOrNull { it.dayOfWeek.equals(activeDay, ignoreCase = true) }

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
            Text("AI Diet Coach", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
            Text("Portion guidelines & low-cost protein additions", fontSize = 12.sp, color = TextGray)

            Spacer(modifier = Modifier.height(16.dp))

            // Calendar Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val fullDays = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                
                days.forEachIndexed { index, shortName ->
                    val fullName = fullDays[index]
                    val isSelected = activeDay.equals(fullName, ignoreCase = true)
                    
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(width = 44.dp, height = 44.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) NeonGreen else CardBackground)
                            .border(1.dp, if (isSelected) NeonGreen else CardBorder, CircleShape)
                            .clickable { activeDay = fullName }
                    ) {
                        Text(
                            text = shortName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) DarkBackground else TextWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (activeDiet != null) {
                // Today's completion status
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (activeDiet.completed) NeonGreenGlow else CardBackground)
                        .border(1.dp, if (activeDiet.completed) NeonGreen else CardBorder, RoundedCornerShape(12.dp))
                        .clickable { repository.setDayCompleted(activeDay, !activeDiet.completed) }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (activeDiet.completed) "Diet Target Completed" else "Diet Target Active",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Tap to toggle daily completion logs",
                            fontSize = 11.sp,
                            color = TextGray
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (activeDiet.completed) NeonGreen else CardBorder),
                        contentAlignment = Alignment.Center
                    ) {
                        if (activeDiet.completed) {
                            Icon(Icons.Default.Check, contentDescription = "Checked", tint = DarkBackground, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Meal plans list
                val mealsOrder = listOf("Breakfast", "Lunch", "Snack", "Dinner")
                mealsOrder.forEach { mealName ->
                    val items = activeDiet.meals[mealName] ?: emptyList()
                    if (items.isNotEmpty()) {
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
                                    Text(mealName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
                                    val totalMealCal = items.sumOf { it.calories.toDouble() }.toInt()
                                    val totalMealProt = items.sumOf { it.protein.toDouble() }.toInt()
                                    Text(
                                        text = "${totalMealCal} kcal • ${totalMealProt}g protein",
                                        fontSize = 11.sp,
                                        color = TextGray
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                items.forEachIndexed { idx, item ->
                                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .clip(CircleShape)
                                                        .background(if (item.isAddedItem) AlertBlue else NeonGreen)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = item.name,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = TextWhite
                                                )
                                            }
                                            if (item.isAddedItem) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(AlertBlueGlow)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "Buy outside: ₹${item.estimatedCost.toInt()}",
                                                        fontSize = 10.sp,
                                                        color = AlertBlue,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            } else {
                                                Text("Mess food", fontSize = 11.sp, color = TextGray)
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = item.instruction,
                                            fontSize = 12.sp,
                                            color = TextGrayLight,
                                            modifier = Modifier.padding(start = 14.dp)
                                        )
                                        Text(
                                            text = "Macros: ${item.protein.toInt()}g P | ${item.calories.toInt()} kcal",
                                            fontSize = 10.sp,
                                            color = TextGray,
                                            modifier = Modifier.padding(start = 14.dp, top = 2.dp)
                                        )
                                    }
                                    if (idx < items.lastIndex) {
                                        Spacer(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(CardBorder)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Summary Cost Card
                val outsideItems = activeDiet.meals.values.flatten().filter { it.isAddedItem }
                val dailyCost = outsideItems.sumOf { it.estimatedCost.toDouble() }.toFloat()
                
                if (dailyCost > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AlertBlueGlow)
                            .border(1.dp, AlertBlue.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cost", tint = AlertBlue)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Estimated outside additions cost today: ₹${dailyCost.toInt()}",
                                color = AlertBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Monthly equivalent: ₹${(dailyCost * 30).toInt()} (Target: ₹${profile.monthlyBudget.toInt()})",
                                color = TextGrayLight,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No diet generated. Please scan a hostel menu first.", color = TextGray)
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
