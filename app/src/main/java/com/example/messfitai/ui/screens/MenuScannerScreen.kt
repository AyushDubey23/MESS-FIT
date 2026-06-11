package com.example.messfitai.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messfitai.data.DataRepository
import com.example.messfitai.domain.DailyDietGenerator
import com.example.messfitai.domain.MenuParserEngine
import com.example.messfitai.theme.AlertBlue
import com.example.messfitai.theme.AlertBlueGlow
import com.example.messfitai.theme.AlertGold
import com.example.messfitai.theme.CardBackground
import com.example.messfitai.theme.CardBorder
import com.example.messfitai.theme.DarkBackground
import com.example.messfitai.theme.NeonGreen
import com.example.messfitai.theme.NeonGreenGlow
import com.example.messfitai.theme.TextGray
import com.example.messfitai.theme.TextGrayLight
import com.example.messfitai.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MenuScannerScreen(
    repository: DataRepository
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    
    val menu by repository.weeklyMenu.collectAsState()
    val profile by repository.userProfile.collectAsState()

    var isScanning by remember { mutableStateOf(false) }
    var scanLogText by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf("") }

    fun runScanSimulation(presetName: String) {
        scope.launch {
            isScanning = true
            selectedPreset = presetName
            
            scanLogText = "Initializing Google ML Kit OCR Engine..."
            delay(800)
            scanLogText = "Capturing high-resolution camera text layers..."
            delay(800)
            scanLogText = "Gemini Vision API: Parsing Monday to Sunday schedules..."
            delay(1000)
            scanLogText = "Gemini API: Running Macro-Nutrition Intelligence Engine..."
            delay(900)
            scanLogText = "MESSFIT AI: Computing food quality scores..."
            delay(600)
            
            // Generate parsed menu
            val scannedMenu = MenuParserEngine.getPresetMenu(presetName)
            repository.saveWeeklyMenu(scannedMenu)

            // Re-generate diets
            val diets = DailyDietGenerator.generateWeeklyDiet(profile, scannedMenu)
            repository.saveDailyDiets(diets)
            
            isScanning = false
            selectedPreset = ""
        }
    }

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
            Text("AI Menu Scanner", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
            Text("Upload weekly mess menu card for micro & macro analysis", fontSize = 12.sp, color = TextGray)
            
            Spacer(modifier = Modifier.height(20.dp))

            // Main upload options
            if (!isScanning) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                        .padding(24.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Scan icon",
                            tint = NeonGreen,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Scan Mess Menu Board",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "Supports PDF schedules, images, or direct screenshots of WhatsApp menus.",
                            fontSize = 11.sp,
                            color = TextGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { runScanSimulation("Hostel Standard Mix") },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = DarkBackground),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Camera")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("CAMERA", fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = { runScanSimulation("North Indian Standard Veg") },
                                colors = ButtonDefaults.buttonColors(containerColor = CardBorder, contentColor = TextWhite),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = "Upload")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("UPLOAD", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Presets Picker
                Text("Select Mess Presets", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("North Indian Standard Veg", "South Indian Standard Veg", "Hostel Standard Mix").forEach { preset ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardBackground)
                                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                                .clickable { runScanSimulation(preset) }
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.ListAlt, contentDescription = "Preset", tint = NeonGreen)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(preset, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                                    Text("Simulate scan analysis for this preset", fontSize = 11.sp, color = TextGray)
                                }
                            }
                        }
                    }
                }
            } else {
                // Scanning Processing Screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBackground)
                        .border(1.dp, NeonGreen, RoundedCornerShape(16.dp))
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = NeonGreen, strokeWidth = 4.dp, modifier = Modifier.size(56.dp))
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "AI Menu Scanner Active",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonGreen
                        )
                        Text(
                            text = "Scanning preset: $selectedPreset",
                            fontSize = 12.sp,
                            color = TextGray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(color = NeonGreen, trackColor = CardBorder, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = scanLogText,
                            fontSize = 13.sp,
                            color = TextWhite,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display Scanned Output if exists
            if (menu != null && !isScanning) {
                Text("Scanned Output & Scores", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(10.dp))

                // Scores block
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
                            Text("Mess Quality Score", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            Text(
                                text = "${menu!!.rating} / 100",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = NeonGreen
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))

                        // Individual score parameters
                        ScannerScoreRow("Protein Richness", menu!!.proteinScore)
                        ScannerScoreRow("Carb Quality Score", menu!!.carbQualityScore)
                        ScannerScoreRow("Fat Quality Score", menu!!.fatQualityScore)
                        ScannerScoreRow("Micronutrients & Vitamins", menu!!.micronutrientScore)
                        ScannerScoreRow("Bulking Suitability", menu!!.bulkingSuitability)
                        ScannerScoreRow("Cutting Suitability", menu!!.cuttingSuitability)
                        ScannerScoreRow("Vegetarian Friendliness", menu!!.vegFriendlinessScore)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Schedule details
                Text("Extracted Menu Schedule", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                Spacer(modifier = Modifier.height(10.dp))

                menu!!.schedule.forEach { (day, meals) ->
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
                            Text(day, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
                            Spacer(modifier = Modifier.height(6.dp))
                            meals.forEach { (mealName, items) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "$mealName: ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = TextWhite,
                                        modifier = Modifier.width(80.dp)
                                    )
                                    Text(
                                        text = items.joinToString(", "),
                                        fontSize = 12.sp,
                                        color = TextGrayLight
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
internal fun ScannerScoreRow(label: String, score: Int) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 12.sp, color = TextGrayLight)
            Text("$score%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(CardBorder)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(score / 100f)
                    .height(4.dp)
                    .background(NeonGreen)
            )
        }
    }
}
