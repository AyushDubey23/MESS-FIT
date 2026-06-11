package com.example.messfitai.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.messfitai.data.DataRepository
import com.example.messfitai.theme.DarkBackground
import com.example.messfitai.theme.NeonGreen
import com.example.messfitai.theme.TextGray
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    repository: DataRepository,
    onNavigateNext: (Boolean) -> Unit // true = Dashboard, false = Login
) {
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        alpha.animateTo(1f, animationSpec = tween(1000))
        scale.animateTo(1f, animationSpec = tween(800))
        delay(1200)
        
        val profile = repository.userProfile.value
        // If profile name is empty, user needs to go to login/onboarding
        val isOnboardingComplete = profile.name.isNotEmpty()
        onNavigateNext(isOnboardingComplete)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .alpha(alpha.value)
                .scale(scale.value)
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = "Fitness Logo",
                tint = NeonGreen,
                modifier = Modifier.size(96.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "MESSFIT AI",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = NeonGreen,
                letterSpacing = 2.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "India's First AI Fitness Coach\nBuilt Specifically For Hostel Students",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextGray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        // Prominent watermarked creator branding
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
                .alpha(alpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Created by",
                fontSize = 11.sp,
                color = TextGray,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "AYUSH DUBEY",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NeonGreen,
                letterSpacing = 2.sp
            )
        }
    }
}
