package com.example.messfitai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.messfitai.data.DefaultDataRepository
import com.example.messfitai.ui.screens.AboutScreen
import com.example.messfitai.ui.screens.DashboardScreen
import com.example.messfitai.ui.screens.DietPlanScreen
import com.example.messfitai.ui.screens.LoginScreen
import com.example.messfitai.ui.screens.MenuScannerScreen
import com.example.messfitai.ui.screens.OnboardingScreen
import com.example.messfitai.ui.screens.SettingsScreen
import com.example.messfitai.ui.screens.ShoppingListScreen
import com.example.messfitai.ui.screens.SplashScreen
import com.example.messfitai.ui.screens.WorkoutScreen
import com.example.messfitai.theme.CardBackground
import com.example.messfitai.theme.CardBorder
import com.example.messfitai.theme.DarkBackground
import com.example.messfitai.theme.NeonGreen
import com.example.messfitai.theme.TextGray

@Composable
fun MainNavigation() {
    val repository = remember { DefaultDataRepository() }
    val backStack = rememberNavBackStack(Splash)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Splash> {
                SplashScreen(
                    repository = repository,
                    onNavigateNext = { isComplete ->
                        backStack.removeLastOrNull() // remove splash
                        if (isComplete) {
                            backStack.add(Dashboard)
                        } else {
                            backStack.add(Login)
                        }
                    }
                )
            }
            entry<Login> {
                LoginScreen(
                    onLoginSuccess = {
                        backStack.removeLastOrNull()
                        backStack.add(Onboarding)
                    }
                )
            }
            entry<Onboarding> {
                OnboardingScreen(
                    repository = repository,
                    onComplete = {
                        backStack.removeLastOrNull()
                        backStack.add(Dashboard)
                    }
                )
            }
            entry<Dashboard> {
                AppScaffold(currentRoute = Dashboard, onNavigate = { route ->
                    if (route != Dashboard) {
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }) {
                    DashboardScreen(repository = repository, onNavigateTo = { screenName ->
                        val target = when (screenName) {
                            "About" -> About
                            "Settings" -> Settings
                            "MenuScanner" -> MenuScanner
                            else -> Dashboard
                        }
                        backStack.add(target)
                    })
                }
            }
            entry<MenuScanner> {
                AppScaffold(currentRoute = MenuScanner, onNavigate = { route ->
                    if (route != MenuScanner) {
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }) {
                    MenuScannerScreen(repository = repository)
                }
            }
            entry<DietPlan> {
                AppScaffold(currentRoute = DietPlan, onNavigate = { route ->
                    if (route != DietPlan) {
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }) {
                    DietPlanScreen(repository = repository)
                }
            }
            entry<WorkoutPlanner> {
                AppScaffold(currentRoute = WorkoutPlanner, onNavigate = { route ->
                    if (route != WorkoutPlanner) {
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }) {
                    WorkoutScreen(repository = repository)
                }
            }
            entry<ShoppingList> {
                AppScaffold(currentRoute = ShoppingList, onNavigate = { route ->
                    if (route != ShoppingList) {
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }) {
                    ShoppingListScreen(repository = repository)
                }
            }
            entry<About> {
                AppScaffold(currentRoute = About, onNavigate = { route ->
                    if (route != About) {
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }) {
                    AboutScreen()
                }
            }
            entry<Settings> {
                AppScaffold(currentRoute = Settings, onNavigate = { route ->
                    if (route != Settings) {
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }) {
                    SettingsScreen(repository = repository, onResetCompleted = {
                        backStack.removeLastOrNull()
                        backStack.add(Login)
                    })
                }
            }
        }
    )
}

@Composable
fun AppScaffold(
    currentRoute: NavKey,
    onNavigate: (NavKey) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Main Screen Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 76.dp)
        ) {
            content()
        }

        // Floating Glassmorphic Bottom Navigation Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CardBackground.copy(alpha = 0.93f))
                .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val tabs = listOf(
                    Triple(Dashboard, Icons.Default.Home, "Home"),
                    Triple(MenuScanner, Icons.Default.CameraAlt, "Scanner"),
                    Triple(DietPlan, Icons.Default.Restaurant, "Diet"),
                    Triple(WorkoutPlanner, Icons.Default.FitnessCenter, "Workout"),
                    Triple(Settings, Icons.Default.Settings, "Settings")
                )

                tabs.forEach { (route, icon, label) ->
                    val isSelected = currentRoute == route
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onNavigate(route) }
                            .padding(vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) NeonGreen else TextGray,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = label,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) NeonGreen else TextGray
                        )
                    }
                }
            }
        }
    }
}
