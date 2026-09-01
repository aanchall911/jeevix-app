package com.example.ui.navigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.DonateAndBloodRequestsScreen
import com.example.ui.screens.HealthPassportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JeevixAiAssistantScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NearbyHealthNetworkScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OtpVerificationScreen
import com.example.ui.screens.ProfileSetupFlow
import com.example.ui.screens.SettingsAndPrivacyScreen
import com.example.ui.screens.SignUpScreen
import com.example.ui.screens.SosEmergencyScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.viewmodel.JeevixViewModel

sealed class Screen(val route: String) {
  object Splash : Screen("splash")
  object Onboarding : Screen("onboarding")
  object Login : Screen("login")
  object SignUp : Screen("signup")
  object Otp : Screen("otp")
  object ProfileSetup : Screen("profile_setup")
  object MainApp : Screen("main_app")
  object AiAssistant : Screen("ai_assistant")
  object Notifications : Screen("notifications")
  object Settings : Screen("settings")
}

enum class BottomTab(
  val title: String,
  val icon: ImageVector,
  val isSos: Boolean = false
) {
  HOME("Home", Icons.Default.Home),
  PASSPORT("Passport", Icons.Default.MedicalServices),
  SOS("SOS", Icons.Default.Emergency, isSos = true),
  DONATE("Donate", Icons.Default.WaterDrop),
  NEARBY("Nearby", Icons.Default.LocalHospital)
}

@Composable
fun JeevixNavHost(
  navController: NavHostController = rememberNavController(),
  viewModel: JeevixViewModel = viewModel()
) {
  NavHost(
    navController = navController,
    startDestination = Screen.Splash.route
  ) {
    composable(Screen.Splash.route) {
      SplashScreen(
        onSplashFinished = {
          navController.navigate(Screen.Onboarding.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
          }
        }
      )
    }

    composable(Screen.Onboarding.route) {
      OnboardingScreen(
        onFinishOnboarding = {
          navController.navigate(Screen.Login.route)
        },
        onSkipToLogin = {
          navController.navigate(Screen.Login.route)
        }
      )
    }

    composable(Screen.Login.route) {
      LoginScreen(
        onLoginSuccess = {
          navController.navigate(Screen.Otp.route)
        },
        onNavigateToSignUp = {
          navController.navigate(Screen.SignUp.route)
        }
      )
    }

    composable(Screen.SignUp.route) {
      SignUpScreen(
        onSignUpSuccess = {
          navController.navigate(Screen.Otp.route)
        },
        onNavigateToLogin = {
          navController.navigate(Screen.Login.route)
        }
      )
    }

    composable(Screen.Otp.route) {
      OtpVerificationScreen(
        onOtpVerified = {
          navController.navigate(Screen.ProfileSetup.route)
        }
      )
    }

    composable(Screen.ProfileSetup.route) {
      ProfileSetupFlow(
        viewModel = viewModel,
        onSetupCompleted = {
          navController.navigate(Screen.MainApp.route) {
            popUpTo(Screen.Onboarding.route) { inclusive = true }
          }
        }
      )
    }

    composable(Screen.MainApp.route) {
      MainAppContainer(
        viewModel = viewModel,
        onNavigateToAi = { prompt ->
          if (prompt != null) {
            navController.navigate("${Screen.AiAssistant.route}?prompt=$prompt")
          } else {
            navController.navigate(Screen.AiAssistant.route)
          }
        },
        onNavigateToNotifications = {
          navController.navigate(Screen.Notifications.route)
        },
        onNavigateToSettings = {
          navController.navigate(Screen.Settings.route)
        }
      )
    }

    composable(
      route = "${Screen.AiAssistant.route}?prompt={prompt}",
      arguments = listOf(
        navArgument("prompt") {
          type = NavType.StringType
          nullable = true
          defaultValue = null
        }
      )
    ) { backStackEntry ->
      val prompt = backStackEntry.arguments?.getString("prompt")
      JeevixAiAssistantScreen(
        viewModel = viewModel,
        initialPrompt = prompt
      )
    }

    composable(Screen.AiAssistant.route) {
      JeevixAiAssistantScreen(viewModel = viewModel)
    }

    composable(Screen.Notifications.route) {
      NotificationsScreen(
        viewModel = viewModel,
        onNavigateBack = { navController.popBackStack() }
      )
    }

    composable(Screen.Settings.route) {
      SettingsAndPrivacyScreen(
        viewModel = viewModel
      )
    }
  }
}

@Composable
fun MainAppContainer(
  viewModel: JeevixViewModel,
  onNavigateToAi: (String?) -> Unit,
  onNavigateToNotifications: () -> Unit,
  onNavigateToSettings: () -> Unit
) {
  var currentTab by remember { mutableStateOf(BottomTab.HOME) }

  val infiniteTransition = androidx.compose.animation.core.rememberInfiniteTransition(label = "nav_sos_pulse")
  val navSosScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.08f,
    animationSpec = androidx.compose.animation.core.infiniteRepeatable(
      animation = androidx.compose.animation.core.tween(900, easing = androidx.compose.animation.core.FastOutSlowInEasing),
      repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
    ),
    label = "nav_scale"
  )

  Scaffold(
    bottomBar = {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .shadow(16.dp, spotColor = Color.Black.copy(alpha = 0.12f)),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
      ) {
        NavigationBar(
          containerColor = Color.White,
          tonalElevation = 0.dp,
          modifier = Modifier
            .height(70.dp)
            .navigationBarsPadding()
        ) {
          BottomTab.values().forEach { tab ->
            val isSelected = currentTab == tab

            if (tab.isSos) {
              NavigationBarItem(
                selected = isSelected,
                onClick = { currentTab = tab },
                icon = {
                  Box(
                    modifier = Modifier
                      .size(52.dp)
                      .scale(navSosScale)
                      .clip(CircleShape)
                      .background(Color(0xFFFFE4E6))
                      .padding(3.dp),
                    contentAlignment = Alignment.Center
                  ) {
                    Box(
                      modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(
                          brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFF43F5E), Color(0xFFBE123C))
                          )
                        ),
                      contentAlignment = Alignment.Center
                    ) {
                      Text(
                        text = "SOS",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        letterSpacing = (-0.5).sp
                      )
                    }
                  }
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                  indicatorColor = Color.Transparent
                )
              )
            } else {
              NavigationBarItem(
                selected = isSelected,
                onClick = { currentTab = tab },
                icon = {
                  Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.title,
                    tint = if (isSelected) Color(0xFFE11D48) else Color(0xFF94A3B8),
                    modifier = Modifier.size(23.dp)
                  )
                },
                label = {
                  Text(
                    text = tab.title,
                    color = if (isSelected) Color(0xFFE11D48) else Color(0xFF64748B),
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 0.2.sp
                  )
                },
                colors = NavigationBarItemDefaults.colors(
                  indicatorColor = Color(0xFFFFE4E6).copy(alpha = 0.45f)
                )
              )
            }
          }
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (currentTab) {
        BottomTab.HOME -> HomeScreen(
          viewModel = viewModel,
          onNavigateToPassport = { currentTab = BottomTab.PASSPORT },
          onNavigateToDonate = { currentTab = BottomTab.DONATE },
          onNavigateToNearby = { tabCategory ->
            viewModel.setNearbyFilterTab(tabCategory)
            currentTab = BottomTab.NEARBY
          },
          onNavigateToAi = onNavigateToAi,
          onNavigateToNotifications = onNavigateToNotifications
        )
        BottomTab.PASSPORT -> HealthPassportScreen(
          viewModel = viewModel
        )
        BottomTab.SOS -> SosEmergencyScreen(
          viewModel = viewModel,
          onNavigateToBloodRequests = { currentTab = BottomTab.DONATE },
          onNavigateToHospitals = {
            viewModel.setNearbyFilterTab("Hospitals")
            currentTab = BottomTab.NEARBY
          }
        )
        BottomTab.DONATE -> DonateAndBloodRequestsScreen(
          viewModel = viewModel
        )
        BottomTab.NEARBY -> NearbyHealthNetworkScreen(
          viewModel = viewModel
        )
      }
    }
  }
}
