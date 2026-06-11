package com.example.messfitai.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messfitai.data.DataRepository
import com.example.messfitai.data.model.UserProfile
import com.example.messfitai.theme.AlertRed
import com.example.messfitai.theme.CardBackground
import com.example.messfitai.theme.CardBorder
import com.example.messfitai.theme.DarkBackground
import com.example.messfitai.theme.NeonGreen
import com.example.messfitai.theme.TextGray
import com.example.messfitai.theme.TextGrayLight
import com.example.messfitai.theme.TextWhite

@Composable
fun SettingsScreen(
    repository: DataRepository,
    onResetCompleted: () -> Unit
) {
    val scrollState = rememberScrollState()
    val profile by repository.userProfile.collectAsState()
    val apiKey by repository.geminiApiKey.collectAsState()

    // Form inputs initialized from active repository state
    var name by remember(profile) { mutableStateOf(profile.name) }
    var weight by remember(profile) { mutableStateOf(profile.weightKg.toString()) }
    var goalWeight by remember(profile) { mutableStateOf(profile.goalWeightKg.toString()) }
    var budget by remember(profile) { mutableStateOf(profile.monthlyBudget.toString()) }
    var key by remember(apiKey) { mutableStateOf(apiKey) }

    var savedMessage by remember { mutableStateOf("") }

    fun handleSave() {
        val updated = profile.copy(
            name = name.ifEmpty { profile.name },
            weightKg = weight.toFloatOrNull() ?: profile.weightKg,
            goalWeightKg = goalWeight.toFloatOrNull() ?: profile.goalWeightKg,
            monthlyBudget = budget.toFloatOrNull() ?: profile.monthlyBudget
        )
        repository.saveUserProfile(updated)
        repository.saveGeminiApiKey(key)
        savedMessage = "Settings successfully saved!"
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
            Text("Settings & Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = NeonGreen)
            Text("Update onboarding variables and configuration details", fontSize = 12.sp, color = TextGray)

            Spacer(modifier = Modifier.height(20.dp))

            // Settings Fields Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = NeonGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Profile Parameters", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Display Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it },
                            label = { Text("Weight (kg)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreen,
                                unfocusedBorderColor = CardBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        OutlinedTextField(
                            value = goalWeight,
                            onValueChange = { goalWeight = it },
                            label = { Text("Target Weight (kg)") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonGreen,
                                unfocusedBorderColor = CardBorder,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = budget,
                        onValueChange = { budget = it },
                        label = { Text("Monthly supplement budget (₹)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // API Key block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, contentDescription = "Key", tint = NeonGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gemini API Configuration", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = key,
                        onValueChange = { key = it },
                        label = { Text("Gemini Flash API Key") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonGreen,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Used for menu OCR processing and diet advisory.",
                        fontSize = 11.sp,
                        color = TextGray,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (savedMessage.isNotEmpty()) {
                Text(
                    text = savedMessage,
                    color = NeonGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Save Buttons
            Button(
                onClick = { handleSave() },
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = DarkBackground),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("SAVE PROFILE CONFIGURATION", fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
            Spacer(modifier = Modifier.height(16.dp))

            // Danger Reset block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = AlertRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Danger Zone", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AlertRed)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Resets your profile parameters, scanned menus, and logs. You will need to complete onboarding again.",
                        fontSize = 11.sp,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            repository.resetAll()
                            onResetCompleted()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AlertRed, contentColor = TextWhite),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("RESET APPLICATION DATA", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer branding
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Created by Ayush Dubey",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextGray,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
