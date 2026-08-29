package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.viewmodel.JeevixViewModel

@Composable
fun SosEmergencyScreen(
  viewModel: JeevixViewModel,
  onNavigateToBloodRequests: () -> Unit,
  onNavigateToHospitals: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val userProfile by viewModel.userProfile.collectAsState()
  val emergencyContacts by viewModel.emergencyContacts.collectAsState()
  val isEmergencyModeActive by viewModel.isEmergencyModeActive.collectAsState()
  val showSosConfirmDialog by viewModel.showSosConfirmDialog.collectAsState()
  var isLocationSharingActive by remember { mutableStateOf(true) }

  val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (isEmergencyModeActive) 1.12f else 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(if (isEmergencyModeActive) 600 else 1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "sos_scale"
  )

  // Confirmation Alert
  if (showSosConfirmDialog) {
    AlertDialog(
      onDismissRequest = { viewModel.triggerSosConfirmation(false) },
      icon = {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = null,
          tint = JeevixEmergencyRed,
          modifier = Modifier.size(36.dp)
        )
      },
      title = {
        Text(
          text = "Trigger Emergency SOS?",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )
      },
      text = {
        Text(
          text = "This will immediately notify your primary emergency contacts (${emergencyContacts.firstOrNull()?.name ?: "Mom"}), broadcast your live GPS location, and prepare nearest hospital response.",
          style = MaterialTheme.typography.bodyMedium,
          textAlign = TextAlign.Center
        )
      },
      confirmButton = {
        Button(
          onClick = { viewModel.activateEmergencyMode(true) },
          colors = ButtonDefaults.buttonColors(containerColor = JeevixEmergencyRed),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("ACTIVATE SOS 🚨", fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { viewModel.triggerSosConfirmation(false) }) {
          Text("Cancel")
        }
      }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(
        if (isEmergencyModeActive) Color(0xFF450A0A) else MaterialTheme.colorScheme.background
      ),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Top Emergency Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = if (isEmergencyModeActive) "🚨 EMERGENCY MODE ACTIVE" else "Emergency & SOS Hub",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = if (isEmergencyModeActive) Color(0xFFFECDD3) else MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = if (isEmergencyModeActive) "Broadcasting live emergency telemetry" else "One-touch lifeline dispatch network",
            style = MaterialTheme.typography.bodySmall,
            color = if (isEmergencyModeActive) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        if (isEmergencyModeActive) {
          Button(
            onClick = { viewModel.activateEmergencyMode(false) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Cancel SOS", color = Color.White, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Huge SOS Button Centerpiece
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
      ) {
        Box(
          modifier = Modifier
            .scale(pulseScale)
            .size(190.dp)
            .clip(CircleShape)
            .background(
              Brush.radialGradient(
                listOf(
                  Color(0xFFEF4444),
                  Color(0xFFDC2626),
                  Color(0xFF991B1B)
                )
              )
            )
            .border(4.dp, Color.White.copy(alpha = 0.4f), CircleShape)
            .shadow(28.dp, CircleShape, spotColor = JeevixEmergencyRed)
            .clickable {
              if (isEmergencyModeActive) {
                viewModel.activateEmergencyMode(false)
              } else {
                viewModel.triggerSosConfirmation(true)
              }
            },
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Emergency,
              contentDescription = "SOS",
              tint = Color.White,
              modifier = Modifier.size(54.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = if (isEmergencyModeActive) "ACTIVE" else "SOS",
              style = MaterialTheme.typography.headlineMedium,
              color = Color.White,
              fontWeight = FontWeight.Black,
              letterSpacing = 2.sp
            )
            Text(
              text = if (isEmergencyModeActive) "Tap to Stop" else "PRESS FOR HELP",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White.copy(alpha = 0.9f),
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp
            )
          }
        }
      }
    }

    // Emergency Action Grid (4 Actions)
    item {
      Text(
        text = "Emergency Response Actions",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = if (isEmergencyModeActive) Color.White else MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        EmergencyActionCard(
          title = "Alert Family",
          desc = "Notify Mom & Dad",
          icon = Icons.Default.People,
          accentColor = JeevixEmergencyRed,
          onClick = {
            val primary = emergencyContacts.firstOrNull { it.isPrimary } ?: emergencyContacts.firstOrNull()
            val phone = primary?.phoneNumber ?: "+919876543210"
            val callIntent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:$phone") }
            context.startActivity(callIntent)
          },
          modifier = Modifier.weight(1f)
        )

        EmergencyActionCard(
          title = "Hospital Call",
          desc = "Nearest Trauma Care",
          icon = Icons.Default.LocalHospital,
          accentColor = JeevixTeal,
          onClick = onNavigateToHospitals,
          modifier = Modifier.weight(1f)
        )
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        EmergencyActionCard(
          title = "Blood SOS",
          desc = "Emergency Requisition",
          icon = Icons.Default.Bloodtype,
          accentColor = JeevixCrimson,
          onClick = onNavigateToBloodRequests,
          modifier = Modifier.weight(1f)
        )

        EmergencyActionCard(
          title = "Dial 108 / 112",
          desc = "National Helpline",
          icon = Icons.Default.Call,
          accentColor = Color(0xFFD97706),
          onClick = {
            val callIntent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:108") }
            context.startActivity(callIntent)
          },
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Live Location Sharing Control Card
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isEmergencyModeActive) Color(0xFF7F1D1D) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
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
                .clip(CircleShape)
                .background(JeevixTeal.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = JeevixTeal)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Live GPS Location Sharing",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isEmergencyModeActive) Color.White else MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = if (isLocationSharingActive) "Active: Indiranagar, Bengaluru" else "Location paused",
                style = MaterialTheme.typography.bodySmall,
                color = if (isEmergencyModeActive) Color(0xFFFECDD3) else MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Switch(
            checked = isLocationSharingActive,
            onCheckedChange = { isLocationSharingActive = it },
            colors = SwitchDefaults.colors(checkedThumbColor = JeevixCrimson, checkedTrackColor = JeevixCrimson.copy(alpha = 0.4f))
          )
        }
      }
    }

    // Quick Vitals Emergency Card
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isEmergencyModeActive) Color(0xFF7F1D1D) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Emergency Health Info Summary",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = if (isEmergencyModeActive) Color.White else MaterialTheme.colorScheme.onSurface
            )

            IconButton(onClick = { viewModel.showEmergencyQr(true) }) {
              Icon(
                Icons.Default.QrCode2,
                contentDescription = "QR Code",
                tint = if (isEmergencyModeActive) Color.White else JeevixCrimson
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "Blood Group",
                style = MaterialTheme.typography.labelSmall,
                color = if (isEmergencyModeActive) Color(0xFFFECDD3) else MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = userProfile?.bloodGroup ?: "O+",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = JeevixCrimson
              )
            }

            Column {
              Text(
                text = "Allergies",
                style = MaterialTheme.typography.labelSmall,
                color = if (isEmergencyModeActive) Color(0xFFFECDD3) else MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = "Penicillin",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = JeevixEmergencyRed
              )
            }

            Column {
              Text(
                text = "Primary Contact",
                style = MaterialTheme.typography.labelSmall,
                color = if (isEmergencyModeActive) Color(0xFFFECDD3) else MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = emergencyContacts.firstOrNull { it.isPrimary }?.name ?: "Mom",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isEmergencyModeActive) Color.White else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun EmergencyActionCard(
  title: String,
  desc: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  accentColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(18.dp),
    color = MaterialTheme.colorScheme.surface,
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
    modifier = modifier.shadow(3.dp, RoundedCornerShape(18.dp))
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(accentColor.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(imageVector = icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(24.dp))
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = desc,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
