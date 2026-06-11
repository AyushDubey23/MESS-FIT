package com.example.messfitai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun AboutScreen() {
    val scrollState = rememberScrollState()

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
            Text("About MESSFIT AI", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
            Text("India's first AI fitness coach built specifically for hostelites", fontSize = 12.sp, color = TextGray)

            Spacer(modifier = Modifier.height(20.dp))

            // Premium Founder Section - Start-up Grade
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(CardBackground)
                    .border(1.dp, NeonGreen, RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Photo Placeholder
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(DarkBackground)
                            .border(2.dp, NeonGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Ayush Dubey Photo",
                            tint = NeonGreen,
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Ayush Dubey",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhite
                    )
                    Text(
                        text = "Founder & Developer of MESSFIT AI",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonGreen,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "\"MESSFIT AI was built after observing that thousands of Indian hostel students struggle to achieve their fitness goals due to limited food choices, poor nutrition awareness, and budget constraints. The app aims to bridge the gap between hostel mess food and scientific fitness planning.\"",
                        fontSize = 13.sp,
                        color = TextGrayLight,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Mission statement details
            Text("Our Mission", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(modifier = Modifier.height(8.dp))

            listOf(
                Pair("Mess Food Optimization", "Instead of telling you to cook chicken breasts or broccoli (which is impossible in a hostel room), we analyze your weekly mess menu directly and optimize your portion sizes."),
                Pair("Student-Budget Focused", "We target Indian student budgets. Our algorithm strictly prioritizes high-protein sources that are extremely cheap, like Soya Chunks (₹12/packet) and Roasted Chana (₹90/500g)."),
                Pair("Indian Fitness Culture", "Tailored recommendations that understand paneer, dal, rajma, idlis, poha, and standard diets, making macro-tracking realistic for Indian students.")
            ).forEach { (title, desc) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Star", tint = NeonGreen, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(desc, fontSize = 11.sp, color = TextGrayLight, lineHeight = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Watermarked creator branding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Created by Ayush Dubey",
                    fontSize = 11.sp,
                    color = TextGray,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
