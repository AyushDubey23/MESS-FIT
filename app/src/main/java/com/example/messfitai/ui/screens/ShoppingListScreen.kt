package com.example.messfitai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.messfitai.data.DataRepository
import com.example.messfitai.domain.DailyDietGenerator
import com.example.messfitai.theme.AlertBlue
import com.example.messfitai.theme.AlertGold
import com.example.messfitai.theme.CardBackground
import com.example.messfitai.theme.CardBorder
import com.example.messfitai.theme.DarkBackground
import com.example.messfitai.theme.NeonGreen
import com.example.messfitai.theme.NeonGreenGlow
import com.example.messfitai.theme.TextGray
import com.example.messfitai.theme.TextGrayLight
import com.example.messfitai.theme.TextWhite

@Composable
fun ShoppingListScreen(
    repository: DataRepository
) {
    val scrollState = rememberScrollState()
    val profile by repository.userProfile.collectAsState()
    val diets by repository.dailyDiets.collectAsState()

    val shoppingList = remember(diets) {
        DailyDietGenerator.generateWeeklyShoppingList(diets)
    }

    val totalWeeklyCost = remember(shoppingList) {
        shoppingList.sumOf { it.estimatedCost.toDouble() }.toFloat()
    }
    val monthlyEquivalent = totalWeeklyCost * 4.3f
    val budgetTarget = profile.monthlyBudget

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
            Text("AI Shopping List & Budget", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
            Text("Weekly shopping logs and budget compliance", fontSize = 12.sp, color = TextGray)

            Spacer(modifier = Modifier.height(20.dp))

            // Budget Compliance Card
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = "Budget", tint = NeonGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Monthly Budget Health", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        
                        val healthText = if (monthlyEquivalent <= budgetTarget) "IN BUDGET" else "OVER BUDGET"
                        val healthColor = if (monthlyEquivalent <= budgetTarget) NeonGreen else AlertBlue
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(healthColor.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = healthText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = healthColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("₹${budgetTarget.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            Text("Your Monthly Budget", fontSize = 11.sp, color = TextGray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("₹${monthlyEquivalent.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = NeonGreen)
                            Text("Planned Added Cost", fontSize = 11.sp, color = TextGray)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            val savings = (budgetTarget - monthlyEquivalent).coerceAtLeast(0f)
                            Text("₹${savings.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = TextWhite)
                            Text("Remaining Balance", fontSize = 11.sp, color = TextGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Budget Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(CardBorder)
                    ) {
                        val fraction = if (budgetTarget > 0) (monthlyEquivalent / budgetTarget).coerceIn(0f, 1f) else 0f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .height(6.dp)
                                .background(if (fraction > 1f) AlertBlue else NeonGreen)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Shopping List Items
            Text("Weekly Shopping List", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(10.dp))

            if (shoppingList.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        shoppingList.forEachIndexed { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ListAlt, contentDescription = "Item", tint = NeonGreen, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                        Text("Qty: ${item.quantityText}", fontSize = 11.sp, color = TextGray)
                                    }
                                }
                                Text(
                                    text = "₹${item.estimatedCost.toInt()}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonGreen
                                )
                            }
                            if (index < shoppingList.lastIndex) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(CardBorder)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Weekly Added Cost", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextWhite)
                            Text("₹${totalWeeklyCost.toInt()}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = NeonGreen)
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your mess menu is high in protein! No additions needed.", color = TextGray, textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Suggestions List
            Text("Indian Budget-Friendly Protein Sources", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(10.dp))

            listOf(
                Triple("Soya Chunks", "₹120 / kg • 52g Protein per 100g", "The absolute cheapest protein in India. Boil them in water with a pinch of salt to eat or add to mess curry."),
                Triple("Roasted Chana", "₹180 / kg • 22g Protein per 100g", "Ready-to-eat dry snack. Perfect to keep in your hostel room for quick macro targets."),
                Triple("Curd (Dahi)", "₹20 / 200g • 7g Protein per packet", "Aids digestion of hostel meals. Low budget dairy option that goes with lunch."),
                Triple("Sattu Powder", "₹140 / kg • 20g Protein per 100g", "Add water, salt/sugar, and lemon. High protein drink that does not require a shaker or blender."),
                Triple("Double Toned Milk", "₹26 / 500ml • 16g Protein per packet", "Cheap clean protein. Drink directly or with sattu/oats.")
            ).forEach { (name, stats, desc) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row {
                        Icon(Icons.Default.Info, contentDescription = "Info", tint = AlertGold, modifier = Modifier.padding(top = 2.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text(stats, fontSize = 11.sp, color = NeonGreen, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(desc, fontSize = 11.sp, color = TextGrayLight, lineHeight = 16.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
