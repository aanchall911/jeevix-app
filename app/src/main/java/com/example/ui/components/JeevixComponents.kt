package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.JeevixAiIndigo
import com.example.ui.theme.JeevixAiPink
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCardDark
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixPrimaryDark
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal

@Composable
fun HealthPassportDigitalCard(
  profile: UserProfile?,
  onViewPassport: () -> Unit,
  modifier: Modifier = Modifier
) {
  val bloodGroup = profile?.bloodGroup ?: "O+"
  val name = profile?.fullName ?: "Aanchal Singh"
  val jeevixId = profile?.jeevixId ?: "JVX-8821"
  val isDonor = profile?.isDonor ?: true
  val donorStatus = profile?.donorStatus ?: "Available"

  Card(
    shape = RoundedCornerShape(24.dp),
    modifier = modifier
      .fillMaxWidth()
      .shadow(elevation = 14.dp, shape = RoundedCornerShape(24.dp), spotColor = Color(0xFFF43F5E).copy(alpha = 0.28f)),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.linearGradient(
            colors = listOf(
              Color(0xFFF43F5E), // Rose 500
              Color(0xFFBE123C)  // Rose 700
            ),
            start = Offset(0f, 0f),
            end = Offset(900f, 900f)
          )
        )
        .padding(20.dp)
    ) {
      // Subtle glowing circle in top right
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .size(120.dp)
          .clip(CircleShape)
          .background(Color.White.copy(alpha = 0.08f))
      )

      Column {
        // Top row with brand header & ID badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Top
        ) {
          Column {
            Text(
              text = "JEEVIX HEALTH PASSPORT",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White.copy(alpha = 0.85f),
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.4.sp,
              fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = "Digital Identity",
              style = MaterialTheme.typography.titleLarge,
              color = Color.White,
              fontWeight = FontWeight.Bold
            )
          }

          // ID Badge Pill
          Surface(
            shape = RoundedCornerShape(50),
            color = Color.White.copy(alpha = 0.2f)
          ) {
            Text(
              text = "ID: $jeevixId",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Center section: Blood Group Box + Profile Completeness Progress
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Pure white blood emblem card
          Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            shadowElevation = 6.dp,
            modifier = Modifier.size(56.dp)
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Text(
                text = bloodGroup,
                color = Color(0xFFE11D48),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                lineHeight = 22.sp
              )
              Text(
                text = "BLOOD",
                color = Color(0xFFE11D48),
                fontWeight = FontWeight.Bold,
                fontSize = 8.sp,
                letterSpacing = 0.8.sp
              )
            }
          }

          // Progress and donor status info
          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Profile 92% Complete",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
              Text(
                text = if (isDonor) "🟢 Donor" else "⚪ Non-Donor",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFFFE4E6),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sleek white progress track
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color.White.copy(alpha = 0.25f))
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth(0.92f)
                  .height(6.dp)
                  .clip(RoundedCornerShape(3.dp))
                  .background(Color.White)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Checkmark pills row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          PassportBadgePill(title = "Medical History", isDone = true)
          PassportBadgePill(title = "Emergency Contact", isDone = true)
          PassportBadgePill(title = "Donations (4)", isDone = true)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sleek semi-transparent action button
        Button(
          onClick = onViewPassport,
          modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.18f),
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(12.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Text(
              text = "Open Full Health Passport",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "→", fontSize = 15.sp)
          }
        }
      }
    }
  }
}

@Composable
fun PassportBadgePill(title: String, isDone: Boolean) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = Color.White.copy(alpha = 0.08f),
    border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.15f))
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        tint = JeevixSuccess,
        modifier = Modifier.size(12.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = Color.White.copy(alpha = 0.9f),
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
      )
    }
  }
}

@Composable
fun ProminentSosBanner(
  onSosClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.04f,
    animationSpec = infiniteRepeatable(
      animation = tween(900, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sos_scale"
  )

  Card(
    shape = RoundedCornerShape(22.dp),
    modifier = modifier
      .fillMaxWidth()
      .shadow(elevation = 8.dp, shape = RoundedCornerShape(22.dp), spotColor = JeevixEmergencyRed.copy(alpha = 0.4f)),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFFEF2F2)
    ),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, JeevixEmergencyRed.copy(alpha = 0.3f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(JeevixEmergencyRed)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "EMERGENCY SOS",
            style = MaterialTheme.typography.labelMedium,
            color = JeevixEmergencyRed,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Need emergency assistance?",
          style = MaterialTheme.typography.titleMedium,
          color = Color(0xFF7F1D1D),
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Instant family alerts, GPS sharing & hospital dispatch",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF991B1B)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Glowing SOS Trigger button
      Button(
        onClick = onSosClick,
        modifier = Modifier
          .scale(pulseScale)
          .height(52.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = JeevixEmergencyRed,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Emergency,
            contentDescription = "SOS",
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "SOS",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.2.sp
          )
        }
      }
    }
  }
}

@Composable
fun VitalStatTile(
  title: String,
  value: String,
  unit: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(18.dp),
    color = Color(0xFFF8FAFC), // Slate 50
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)), // Slate 100
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF64748B), // Slate 500
        fontWeight = FontWeight.Bold,
        fontSize = 9.sp,
        letterSpacing = 0.6.sp,
        maxLines = 1
      )

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
      ) {
        Text(
          text = value,
          style = MaterialTheme.typography.titleMedium,
          color = Color(0xFF0F172A), // Slate 900
          fontWeight = FontWeight.Black,
          fontSize = 17.sp
        )
        if (unit.isNotEmpty()) {
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = unit,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8), // Slate 400
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 1.dp)
          )
        }
      }
    }
  }
}

@Composable
fun AiAssistantCard(
  onAskClick: () -> Unit,
  onPromptSelect: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(24.dp),
    modifier = modifier
      .fillMaxWidth()
      .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp), spotColor = Color(0xFF312E81).copy(alpha = 0.25f)),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.linearGradient(
            colors = listOf(
              Color(0xFF312E81), // Deep Indigo 900
              Color(0xFF1E1B4B)  // Slate/Indigo
            )
          )
        )
        .padding(18.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF6366F1)), // Indigo 500
              contentAlignment = Alignment.Center
            ) {
              Text(text = "⚡", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "AI ASSISTANT",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFA5B4FC), // Indigo 300
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp,
                fontSize = 10.sp
              )
              Text(
                text = "Ask Jeevix AI",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // CHAT NOW button
          Button(
            onClick = onAskClick,
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF4338CA), // Indigo 700
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp)
          ) {
            Text(
              text = "CHAT NOW",
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              letterSpacing = 0.5.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sleek Quick prompts row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          AiPromptChip(
            text = "Summarize Profile",
            onClick = { onPromptSelect("Summarize my health profile") },
            modifier = Modifier.weight(1f)
          )
          AiPromptChip(
            text = "Find Matching Donors",
            onClick = { onPromptSelect("Find matching blood donors near me") },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}

@Composable
fun AiPromptChip(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(10.dp),
    color = Color.White.copy(alpha = 0.1f),
    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
    modifier = modifier
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall,
      color = Color.White.copy(alpha = 0.9f),
      fontWeight = FontWeight.Medium,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
      maxLines = 1
    )
  }
}

@Composable
fun CanvasEmergencyQrCode(
  dataString: String,
  modifier: Modifier = Modifier
) {
  // Beautiful stylized QR code representation using Compose Canvas
  Box(
    modifier = modifier
      .size(200.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(Color.White)
      .padding(14.dp),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.size(172.dp)) {
      val cellSize = size.width / 17f
      val darkColor = Color(0xFF0F172A)
      val accentColor = Color(0xFFBE123C)

      // Draw QR corner finders
      fun drawFinder(x: Float, y: Float) {
        drawRoundRect(
          color = darkColor,
          topLeft = Offset(x, y),
          size = Size(cellSize * 5, cellSize * 5),
          cornerRadius = CornerRadius(8f, 8f)
        )
        drawRoundRect(
          color = Color.White,
          topLeft = Offset(x + cellSize, y + cellSize),
          size = Size(cellSize * 3, cellSize * 3),
          cornerRadius = CornerRadius(4f, 4f)
        )
        drawRoundRect(
          color = accentColor,
          topLeft = Offset(x + cellSize * 1.5f, y + cellSize * 1.5f),
          size = Size(cellSize * 2, cellSize * 2),
          cornerRadius = CornerRadius(4f, 4f)
        )
      }

      drawFinder(0f, 0f)
      drawFinder(size.width - cellSize * 5, 0f)
      drawFinder(0f, size.height - cellSize * 5)

      // Synthetic deterministic pattern based on hash
      val hash = dataString.hashCode()
      for (r in 0 until 17) {
        for (c in 0 until 17) {
          val inTopLeftFinder = r < 6 && c < 6
          val inTopRightFinder = r < 6 && c >= 11
          val inBottomLeftFinder = r >= 11 && c < 6
          val inCenter = r in 7..9 && c in 7..9

          if (!inTopLeftFinder && !inTopRightFinder && !inBottomLeftFinder && !inCenter) {
            val bit = ((r * 17 + c + hash) % 3) == 0 || ((r * c + hash) % 5) == 0
            if (bit) {
              drawRoundRect(
                color = if ((r + c) % 4 == 0) accentColor else darkColor,
                topLeft = Offset(c * cellSize + 1f, r * cellSize + 1f),
                size = Size(cellSize - 2f, cellSize - 2f),
                cornerRadius = CornerRadius(2f, 2f)
              )
            }
          }
        }
      }

      // Center Jeevix Shield Emblem
      val centerSize = cellSize * 4f
      val centerOffset = (size.width - centerSize) / 2f
      drawRoundRect(
        color = Color.White,
        topLeft = Offset(centerOffset, centerOffset),
        size = Size(centerSize, centerSize),
        cornerRadius = CornerRadius(8f, 8f)
      )
      drawRoundRect(
        color = accentColor,
        topLeft = Offset(centerOffset + 4f, centerOffset + 4f),
        size = Size(centerSize - 8f, centerSize - 8f),
        cornerRadius = CornerRadius(6f, 6f)
      )
    }
  }
}
