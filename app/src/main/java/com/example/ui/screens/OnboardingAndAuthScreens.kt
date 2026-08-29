package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.JeevixAiPink
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixPrimaryDark
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onSplashFinished: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "logo_scale"
  )

  LaunchedEffect(Unit) {
    delay(2200)
    onSplashFinished()
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          listOf(
            Color(0xFF0F172A),
            Color(0xFF1E1B4B),
            Color(0xFF4C0519)
          )
        )
      )
      .statusBarsPadding()
      .navigationBarsPadding(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(24.dp)
    ) {
      // Animated Logo Drop & Shield
      Box(
        modifier = Modifier
          .scale(pulseScale)
          .size(110.dp)
          .clip(CircleShape)
          .background(
            Brush.radialGradient(
              listOf(
                JeevixCrimson,
                JeevixCrimsonDark,
                Color(0xFF4C0519)
              )
            )
          )
          .border(2.dp, Color(0xFFFDA4AF).copy(alpha = 0.6f), CircleShape)
          .shadow(24.dp, CircleShape, spotColor = JeevixCrimson),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.WaterDrop,
          contentDescription = "Jeevix Logo",
          tint = Color.White,
          modifier = Modifier.size(60.dp)
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "JEEVIX",
        style = MaterialTheme.typography.displayMedium,
        color = Color.White,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 4.sp
      )

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "Your health. Your passport. Your lifeline.",
        style = MaterialTheme.typography.bodyLarge,
        color = Color(0xFFFECDD3),
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(36.dp))

      // Bottom Powered by AI badge
      Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, JeevixAiViolet.copy(alpha = 0.4f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(text = "✨", fontSize = 12.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Powered by AI",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFFE9D5FF),
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
          )
        }
      }
    }
  }
}

data class OnboardingSlide(
  val heading: String,
  val body: String,
  val iconEmoji: String,
  val accentGradient: List<Color>,
  val pillTag: String
)

@Composable
fun OnboardingScreen(
  onFinishOnboarding: () -> Unit,
  onSkipToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {
  val slides = listOf(
    OnboardingSlide(
      heading = "Know your health.",
      body = "Keep your blood profile, medical history, and essential health information securely organized in one place.",
      iconEmoji = "🩸",
      accentGradient = listOf(Color(0xFFBE123C), Color(0xFF881337)),
      pillTag = "HEALTH PASSPORT"
    ),
    OnboardingSlide(
      heading = "Be ready when it matters.",
      body = "Get quick access to your emergency contacts, nearby hospitals, and verified blood banks when you need them most.",
      iconEmoji = "🚨",
      accentGradient = listOf(Color(0xFFDC2626), Color(0xFF7F1D1D)),
      pillTag = "EMERGENCY & SOS"
    ),
    OnboardingSlide(
      heading = "One profile can save a life. 🩸",
      body = "Build your digital health passport and connect with the real-time verified blood donation network.",
      iconEmoji = "🏥",
      accentGradient = listOf(Color(0xFF0D9488), Color(0xFF134E4A)),
      pillTag = "BLOOD NETWORK"
    )
  )

  var currentSlideIndex by remember { mutableIntStateOf(0) }
  val currentSlide = slides[currentSlideIndex]

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(24.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // Top bar with skip
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "JEEVIX",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = JeevixCrimsonDark,
        letterSpacing = 2.sp
      )
      if (currentSlideIndex < slides.size - 1) {
        TextButton(onClick = onSkipToLogin) {
          Text(text = "Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      } else {
        Spacer(modifier = Modifier.width(48.dp))
      }
    }

    // Center Illustration card
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(190.dp)
          .clip(RoundedCornerShape(36.dp))
          .background(Brush.radialGradient(currentSlide.accentGradient))
          .border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(36.dp))
          .shadow(16.dp, RoundedCornerShape(36.dp), spotColor = currentSlide.accentGradient.first()),
        contentAlignment = Alignment.Center
      ) {
        Text(text = currentSlide.iconEmoji, fontSize = 72.sp)
      }

      Spacer(modifier = Modifier.height(32.dp))

      Surface(
        shape = RoundedCornerShape(12.dp),
        color = currentSlide.accentGradient.first().copy(alpha = 0.12f)
      ) {
        Text(
          text = currentSlide.pillTag,
          style = MaterialTheme.typography.labelSmall,
          color = currentSlide.accentGradient.first(),
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Text(
        text = currentSlide.heading,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = currentSlide.body,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        lineHeight = 22.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
      )
    }

    // Bottom indicators and actions
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxWidth()
    ) {
      // Dot indicators
      Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 24.dp)
      ) {
        slides.indices.forEach { index ->
          val isSelected = index == currentSlideIndex
          Box(
            modifier = Modifier
              .padding(horizontal = 4.dp)
              .height(8.dp)
              .width(if (isSelected) 24.dp else 8.dp)
              .clip(CircleShape)
              .background(
                if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
              )
          )
        }
      }

      Button(
        onClick = {
          if (currentSlideIndex < slides.size - 1) {
            currentSlideIndex++
          } else {
            onFinishOnboarding()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(54.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = JeevixCrimson,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = if (currentSlideIndex == slides.size - 1) "Get Started ❤️" else "Continue →",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Composable
fun LoginScreen(
  onLoginSuccess: () -> Unit,
  onNavigateToSignUp: () -> Unit,
  modifier: Modifier = Modifier
) {
  var emailOrPhone by remember { mutableStateOf("aanchalbb11@gmail.com") }
  var password by remember { mutableStateOf("password123") }
  var isPasswordVisible by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
      .verticalScroll(rememberScrollState())
      .padding(24.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Spacer(modifier = Modifier.height(20.dp))

      // Header
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(JeevixCrimson.copy(alpha = 0.15f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.WaterDrop,
            contentDescription = null,
            tint = JeevixCrimson,
            modifier = Modifier.size(22.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "JEEVIX",
          style = MaterialTheme.typography.titleLarge,
          color = JeevixCrimsonDark,
          fontWeight = FontWeight.Bold,
          letterSpacing = 2.sp
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "Welcome back 👋",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = "Sign in to continue to your digital health passport and lifeline network.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 6.dp)
      )

      Spacer(modifier = Modifier.height(32.dp))

      // Input 1
      OutlinedTextField(
        value = emailOrPhone,
        onValueChange = { emailOrPhone = it },
        label = { Text("Email / Phone Number") },
        leadingIcon = {
          Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = JeevixCrimson)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = JeevixCrimson,
          focusedLabelColor = JeevixCrimson
        ),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Input 2
      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        leadingIcon = {
          Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = JeevixCrimson)
        },
        trailingIcon = {
          IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
              imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
              contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
            )
          }
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = JeevixCrimson,
          focusedLabelColor = JeevixCrimson
        ),
        singleLine = true
      )

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
      ) {
        TextButton(onClick = {}) {
          Text(text = "Forgot Password?", color = JeevixCrimson, style = MaterialTheme.typography.labelMedium)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        onClick = onLoginSuccess,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
        shape = RoundedCornerShape(14.dp)
      ) {
        Text(text = "Login", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(20.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        Text(
          text = "  OR  ",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
      }

      Spacer(modifier = Modifier.height(20.dp))

      OutlinedButton(
        onClick = onLoginSuccess,
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = "🌐", fontSize = 16.sp)
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Continue with Google",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Don't have an account? ",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = "Create one",
        style = MaterialTheme.typography.bodyMedium,
        color = JeevixCrimson,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable { onNavigateToSignUp() }
      )
    }
  }
}

@Composable
fun SignUpScreen(
  onSignUpSuccess: () -> Unit,
  onNavigateToLogin: () -> Unit,
  modifier: Modifier = Modifier
) {
  var fullName by remember { mutableStateOf("Aanchal Sharma") }
  var email by remember { mutableStateOf("aanchalbb11@gmail.com") }
  var phone by remember { mutableStateOf("+91 98765 43210") }
  var password by remember { mutableStateOf("securePass123") }
  var confirmPassword by remember { mutableStateOf("securePass123") }
  var termsAccepted by remember { mutableStateOf(true) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
      .verticalScroll(rememberScrollState())
      .padding(24.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Text(
        text = "Create your Jeevix account",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.Bold
      )

      Text(
        text = "Join India's unified digital health passport & lifeline emergency network.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp)
      )

      Spacer(modifier = Modifier.height(24.dp))

      OutlinedTextField(
        value = fullName,
        onValueChange = { fullName = it },
        label = { Text("Full Name") },
        leadingIcon = { Icon(Icons.Default.Person, null, tint = JeevixCrimson) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email Address") },
        leadingIcon = { Icon(Icons.Default.Email, null, tint = JeevixCrimson) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = phone,
        onValueChange = { phone = it },
        label = { Text("Phone Number") },
        leadingIcon = { Icon(Icons.Default.Phone, null, tint = JeevixCrimson) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        leadingIcon = { Icon(Icons.Default.Lock, null, tint = JeevixCrimson) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = confirmPassword,
        onValueChange = { confirmPassword = it },
        label = { Text("Confirm Password") },
        leadingIcon = { Icon(Icons.Default.Lock, null, tint = JeevixCrimson) },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { termsAccepted = !termsAccepted }
      ) {
        Checkbox(
          checked = termsAccepted,
          onCheckedChange = { termsAccepted = it },
          colors = CheckboxDefaults.colors(checkedColor = JeevixCrimson)
        )
        Text(
          text = "I agree to the Jeevix Terms & Privacy Policy",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = onSignUpSuccess,
        enabled = termsAccepted && fullName.isNotBlank() && email.isNotBlank(),
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
        shape = RoundedCornerShape(14.dp)
      ) {
        Text(text = "Create Account", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Already have an account? ",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = "Login",
        style = MaterialTheme.typography.bodyMedium,
        color = JeevixCrimson,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable { onNavigateToLogin() }
      )
    }
  }
}

@Composable
fun OtpVerificationScreen(
  onOtpVerified: () -> Unit,
  modifier: Modifier = Modifier
) {
  var otpDigits by remember { mutableStateOf(listOf("4", "8", "9", "2", "1", "6")) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(24.dp),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Spacer(modifier = Modifier.height(20.dp))

      Box(
        modifier = Modifier
          .size(72.dp)
          .clip(CircleShape)
          .background(JeevixCrimson.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Phone,
          contentDescription = null,
          tint = JeevixCrimson,
          modifier = Modifier.size(36.dp)
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      Text(
        text = "Verify your number",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "We've sent a 6-digit verification code to your registered mobile phone +91 98765 43210.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(36.dp))

      // 6 Digit OTP Display
      Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        otpDigits.forEach { digit ->
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
              .border(1.5.dp, JeevixCrimson, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = digit,
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "Didn't receive code? ",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = "Resend OTP",
          style = MaterialTheme.typography.bodySmall,
          color = JeevixCrimson,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Button(
      onClick = onOtpVerified,
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp),
      colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
      shape = RoundedCornerShape(16.dp)
    ) {
      Text(
        text = "Verify & Continue",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
    }
  }
}
