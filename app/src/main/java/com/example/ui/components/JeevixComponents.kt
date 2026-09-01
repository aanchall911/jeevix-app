package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.DonorMilestone
import com.example.data.model.DonorRewardBadge
import com.example.data.model.DonorRewards
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

  val infiniteTransition = rememberInfiniteTransition(label = "card_pulse")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "alpha_pulse"
  )

  Card(
    shape = RoundedCornerShape(26.dp),
    modifier = modifier
      .fillMaxWidth()
      .shadow(
        elevation = 16.dp,
        shape = RoundedCornerShape(26.dp),
        spotColor = Color(0xFFF43F5E).copy(alpha = 0.35f)
      ),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.linearGradient(
            colors = listOf(
              Color(0xFFE11D48), // Rose 600
              Color(0xFFBE123C), // Rose 700
              Color(0xFF881337)  // Rose 900
            ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f)
          )
        )
        .padding(22.dp)
    ) {
      // Decorative background geometric circles
      Box(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .size(140.dp)
          .clip(CircleShape)
          .background(Color.White.copy(alpha = 0.06f))
      )
      Box(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .size(90.dp)
          .clip(CircleShape)
          .background(Color.White.copy(alpha = 0.04f))
      )

      Column {
        // Top row with brand header & EMV chip + ID badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            // Official App Logo Badge on Card
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFF0F172A),
              shadowElevation = 2.dp,
              modifier = Modifier.size(32.dp)
            ) {
              Image(
                painter = painterResource(id = R.drawable.jeevix_app_logo_vector_1788238539465),
                contentDescription = "Jeevix Logo",
                modifier = Modifier
                  .fillMaxSize()
                  .clip(RoundedCornerShape(8.dp))
              )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Stylized EMV Smart Chip
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFFFDE68A), // Gold 200
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD97706)),
              modifier = Modifier.size(width = 28.dp, height = 20.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(width = 20.dp, height = 12.dp)) {
                  drawLine(
                    color = Color(0xFFB45309),
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height),
                    strokeWidth = 1.5f
                  )
                  drawLine(
                    color = Color(0xFFB45309),
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 1.5f
                  )
                }
              }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
              Text(
                text = "JEEVIX HEALTH PASSPORT",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Black,
                letterSpacing = 1.6.sp,
                fontSize = 9.5.sp
              )
              Text(
                text = "Digital Emergency Identity",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          }

          // ID Badge Pill with shield
          Surface(
            shape = RoundedCornerShape(50),
            color = Color.Black.copy(alpha = 0.25f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                Icons.Default.Security,
                contentDescription = null,
                tint = Color(0xFFFDE68A),
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = jeevixId,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Center section: Blood Group Box + Profile Completeness Progress
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          // Pure white blood emblem card with holographic-like border
          Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFFFE4E6)),
            modifier = Modifier.size(62.dp)
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Text(
                text = bloodGroup,
                color = Color(0xFFE11D48),
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                lineHeight = 24.sp
              )
              Text(
                text = "BLOOD",
                color = Color(0xFF9F1239),
                fontWeight = FontWeight.Black,
                fontSize = 8.sp,
                letterSpacing = 1.sp
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
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
              Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFF10B981).copy(alpha = 0.25f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF34D399).copy(alpha = 0.5f))
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(6.dp)
                      .clip(CircleShape)
                      .background(Color(0xFF34D399).copy(alpha = pulseAlpha))
                  )
                  Spacer(modifier = Modifier.width(5.dp))
                  Text(
                    text = if (isDonor) "Donor Ready" else "Standard",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sleek white progress track
            Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Box(
                modifier = Modifier
                  .weight(1f)
                  .height(6.dp)
                  .clip(RoundedCornerShape(3.dp))
                  .background(Color.White.copy(alpha = 0.25f))
              ) {
                Box(
                  modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White)
                )
              }
              Text(
                text = "94%",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Checkmark pills row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          PassportBadgePill(title = "Clinical History", isDone = true)
          PassportBadgePill(title = "Emergency SOS", isDone = true)
          PassportBadgePill(title = "Shield ID", isDone = profile?.isIdVerified == true)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sleek action button with frosted glow
        Button(
          onClick = onViewPassport,
          modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.White.copy(alpha = 0.22f),
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(14.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.35f))
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(Icons.Default.QrCode2, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "View Digital Health Passport & QR",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              fontSize = 12.5.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "→", fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sos_scale"
  )
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 0.8f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sos_alpha"
  )

  Card(
    shape = RoundedCornerShape(24.dp),
    modifier = modifier
      .fillMaxWidth()
      .shadow(
        elevation = 12.dp,
        shape = RoundedCornerShape(24.dp),
        spotColor = JeevixEmergencyRed.copy(alpha = 0.45f)
      ),
    colors = CardDefaults.cardColors(
      containerColor = Color(0xFFFFF1F2) // Rose 50
    ),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, JeevixEmergencyRed.copy(alpha = 0.35f))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(
              Color(0xFFFFF1F2),
              Color(0xFFFFE4E6)
            )
          )
        )
        .padding(20.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(JeevixEmergencyRed.copy(alpha = pulseAlpha))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "EMERGENCY LIFELINE SOS",
              style = MaterialTheme.typography.labelSmall,
              color = JeevixEmergencyRed,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.2.sp,
              fontSize = 10.sp
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Need Instant Medical Help?",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF881337), // Rose 900
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
          )
          Text(
            text = "Broadcasts GPS, alerts 112 & nearest emergency units",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF9F1239), // Rose 800
            fontSize = 11.5.sp
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Glowing SOS Trigger button with dual ring simulation
        Box(contentAlignment = Alignment.Center) {
          Box(
            modifier = Modifier
              .size(62.dp)
              .scale(pulseScale)
              .clip(CircleShape)
              .background(JeevixEmergencyRed.copy(alpha = 0.2f))
          )
          Button(
            onClick = onSosClick,
            modifier = Modifier
              .size(54.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = JeevixEmergencyRed,
              contentColor = Color.White
            ),
            shape = CircleShape,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Text(
                text = "SOS",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp,
                fontSize = 13.sp
              )
            }
          }
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
    shape = RoundedCornerShape(20.dp),
    color = Color.White,
    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
    shadowElevation = 3.dp,
    modifier = modifier
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(accentColor.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = accentColor,
            modifier = Modifier.size(17.dp)
          )
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = Color(0xFFF1F5F9)
        ) {
          Text(
            text = "Normal",
            color = Color(0xFF64748B),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF64748B),
        fontWeight = FontWeight.Bold,
        fontSize = 9.5.sp,
        letterSpacing = 0.6.sp,
        maxLines = 1
      )

      Spacer(modifier = Modifier.height(2.dp))

      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
      ) {
        Text(
          text = value,
          style = MaterialTheme.typography.titleMedium,
          color = Color(0xFF0F172A),
          fontWeight = FontWeight.Black,
          fontSize = 18.sp
        )
        if (unit.isNotEmpty()) {
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = unit,
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8),
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      // Sparkline mini waveform canvas
      Canvas(
        modifier = Modifier
          .fillMaxWidth()
          .height(18.dp)
      ) {
        val path = Path()
        val w = size.width
        val h = size.height

        path.moveTo(0f, h * 0.6f)
        path.lineTo(w * 0.2f, h * 0.6f)
        path.lineTo(w * 0.35f, h * 0.1f)
        path.lineTo(w * 0.5f, h * 0.9f)
        path.lineTo(w * 0.65f, h * 0.3f)
        path.lineTo(w * 0.8f, h * 0.6f)
        path.lineTo(w, h * 0.6f)

        drawPath(
          path = path,
          color = accentColor.copy(alpha = 0.85f),
          style = Stroke(width = 3.5f)
        )
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
    shape = RoundedCornerShape(26.dp),
    modifier = modifier
      .fillMaxWidth()
      .shadow(
        elevation = 14.dp,
        shape = RoundedCornerShape(26.dp),
        spotColor = Color(0xFF4338CA).copy(alpha = 0.3f)
      ),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          brush = Brush.linearGradient(
            colors = listOf(
              Color(0xFF312E81), // Deep Indigo 900
              Color(0xFF4338CA), // Indigo 700
              Color(0xFF581C87)  // Deep Purple 900
            ),
            start = Offset(0f, 0f),
            end = Offset(900f, 900f)
          )
        )
        .padding(20.dp)
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
                .size(46.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                  brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF818CF8), Color(0xFFC084FC))
                  )
                ),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "✨", fontSize = 22.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "JEEVIX AI COPILOT",
                  style = MaterialTheme.typography.labelSmall,
                  color = Color(0xFFC7D2FE),
                  fontWeight = FontWeight.Black,
                  letterSpacing = 1.4.sp,
                  fontSize = 9.5.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color(0xFF10B981).copy(alpha = 0.3f)
                ) {
                  Text(
                    text = "ONLINE",
                    color = Color(0xFF6EE7B7),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
              Text(
                text = "Clinical AI Assistant",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            }
          }

          // CHAT NOW button
          Button(
            onClick = onAskClick,
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.White,
              contentColor = Color(0xFF312E81)
            ),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
          ) {
            Text(
              text = "Ask AI →",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 12.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sleek Quick prompts row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          AiPromptChip(
            text = "⚡ Donation Readiness",
            onClick = { onPromptSelect("Check my blood donation readiness and vitals") },
            modifier = Modifier.weight(1f)
          )
          AiPromptChip(
            text = "🩸 Nearby Compatible",
            onClick = { onPromptSelect("Find matching blood donors and banks near me") },
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

/**
 * Dynamic Milestone Progress Card for Donor Profile
 */
@Composable
fun DonorMilestoneProgressCard(
  rewards: DonorRewards,
  modifier: Modifier = Modifier,
  onBadgeClick: ((DonorRewardBadge) -> Unit)? = null
) {
  val nextMilestone = rewards.nextMilestone
  val currentMilestone = rewards.currentMilestone
  val progressPercent = (rewards.progressToNextMilestone * 100).toInt()

  Card(
    shape = RoundedCornerShape(22.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Slate 900
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    modifier = modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      // Header: Level Badge & Tier
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(
                brush = Brush.linearGradient(
                  colors = listOf(Color(0xFFF59E0B), Color(0xFFD97706))
                )
              )
              .shadow(4.dp, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(currentMilestone.badgeEmoji, fontSize = 22.sp)
          }

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "LEVEL ${currentMilestone.level} • ${rewards.currentTier.uppercase()}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFFBBF24),
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                fontSize = 11.sp
              )
            }
            Text(
              text = currentMilestone.title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              fontSize = 17.sp
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF1E293B),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = Color(0xFFF59E0B),
              modifier = Modifier.size(14.dp)
            )
            Text(
              text = "${rewards.totalPoints} XP",
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White,
              fontSize = 12.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Next Milestone Target Header
      if (nextMilestone != null) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.Bottom
        ) {
          Column {
            Text(
              text = "NEXT MILESTONE: ${nextMilestone.title.uppercase()}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF94A3B8),
              fontWeight = FontWeight.ExtraBold,
              fontSize = 10.5.sp,
              letterSpacing = 0.8.sp
            )
            Text(
              text = "${rewards.donationsNeededForNextMilestone} more donation to unlock '${nextMilestone.badgeName}'",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFFE2E8F0),
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(top = 2.dp)
            )
          }

          Text(
            text = "$progressPercent%",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFBBF24),
            fontSize = 16.sp
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Multi-segment Stepped Milestone Progress Bar
        Column {
          LinearProgressIndicator(
            progress = rewards.progressToNextMilestone,
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = Color(0xFFF59E0B),
            trackColor = Color(0xFF334155)
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Stepper Node Labels
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "${currentMilestone.requiredDonations} donations (${currentMilestone.badgeName})",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF64748B),
              fontSize = 10.sp
            )
            Text(
              text = "Target: ${nextMilestone.requiredDonations} donations",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFFFBBF24),
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Reward Banner Pill
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF1E293B).copy(alpha = 0.8f),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(text = nextMilestone.badgeEmoji, fontSize = 16.sp)
            Text(
              text = "Reward: ${nextMilestone.rewardDescription} (+${nextMilestone.pointsReward} XP)",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFFFEF3C7),
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.5.sp
            )
          }
        }
      } else {
        // Top Tier Achieved
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF1E293B),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF06B6D4).copy(alpha = 0.4f))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text("💎", fontSize = 20.sp)
            Column {
              Text(
                text = "Grandmaster Diamond Legend Milestone Reached!",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF67E8F9)
              )
              Text(
                text = "You are among the top 0.1% lifesavers in the nation.",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Lives Impacted Stats Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "TOTAL DONATIONS",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8),
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp
          )
          Text(
            text = "${rewards.donationCount} Units Logged",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = "LIVES SAVED (EST.)",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8),
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp
          )
          Text(
            text = "${rewards.livesSavedEstimated}+ Lives Saved ❤️",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFF43F5E)
          )
        }
      }
    }
  }
}

/**
 * Dynamic Chip for Donor Badges (e.g. 'First Time Donor', 'Life Saver')
 */
@Composable
fun DonorRewardBadgeChip(
  badge: DonorRewardBadge,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val isUnlocked = badge.isUnlocked
  val tierColor = Color(badge.tierColorHex)

  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(16.dp),
    color = if (isUnlocked) Color.White else Color(0xFFF8FAFC),
    border = androidx.compose.foundation.BorderStroke(
      width = if (isUnlocked) 1.5.dp else 1.dp,
      color = if (isUnlocked) tierColor.copy(alpha = 0.45f) else Color(0xFFE2E8F0)
    ),
    shadowElevation = if (isUnlocked) 2.dp else 0.dp,
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(if (isUnlocked) tierColor.copy(alpha = 0.14f) else Color(0xFFE2E8F0)),
        contentAlignment = Alignment.Center
      ) {
        if (isUnlocked) {
          Text(text = badge.emoji, fontSize = 16.sp)
        } else {
          Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Locked",
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(16.dp)
          )
        }
      }

      Column {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = badge.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold,
            color = if (isUnlocked) Color(0xFF0F172A) else Color(0xFF64748B),
            fontSize = 13.sp
          )
          if (isUnlocked) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Unlocked",
              tint = Color(0xFF10B981),
              modifier = Modifier.size(13.dp)
            )
          }
        }

        Text(
          text = if (isUnlocked) "+${badge.points} XP • Unlocked" else "${badge.points} XP • Locked",
          style = MaterialTheme.typography.labelSmall,
          color = if (isUnlocked) tierColor else Color(0xFF94A3B8),
          fontWeight = FontWeight.Bold,
          fontSize = 10.sp
        )
      }
    }
  }
}

/**
 * Detailed Badge Modal Dialog
 */
@Composable
fun DonorBadgeDetailDialog(
  badge: DonorRewardBadge,
  onDismiss: () -> Unit
) {
  val isUnlocked = badge.isUnlocked
  val tierColor = Color(badge.tierColorHex)

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(if (isUnlocked) tierColor.copy(alpha = 0.15f) else Color(0xFFE2E8F0)),
          contentAlignment = Alignment.Center
        ) {
          Text(badge.emoji, fontSize = 24.sp)
        }
        Column {
          Text(
            text = badge.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F172A)
          )
          Text(
            text = "Category: ${badge.category}",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF64748B)
          )
        }
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
          text = badge.description,
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF334155)
        )

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isUnlocked) Color(0xFFF0FDF4) else Color(0xFFF8FAFC),
          border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUnlocked) Color(0xFFBBF7D0) else Color(0xFFE2E8F0)
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "STATUS",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (isUnlocked) "UNLOCKED ✓" else "LOCKED 🔒",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = if (isUnlocked) Color(0xFF16A34A) else Color(0xFF64748B)
              )
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = "REWARD",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = "+${badge.points} XP",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.ExtraBold,
                color = tierColor
              )
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Close", fontWeight = FontWeight.Bold, color = Color(0xFFE11D48))
      }
    },
    shape = RoundedCornerShape(20.dp),
    containerColor = Color.White
  )
}
