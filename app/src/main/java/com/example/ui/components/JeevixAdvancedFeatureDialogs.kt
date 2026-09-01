package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiHealthInsightReport
import com.example.data.model.AppLanguage
import com.example.data.model.WearableDevice
import com.example.ui.theme.JeevixAiPink
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.ui.util.LocalizationManager

@Composable
fun AiHealthAnalysisDialog(
  report: AiHealthInsightReport,
  languageCode: String,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = JeevixAiViolet),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("Done", fontWeight = FontWeight.Bold)
      }
    },
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Brush.linearGradient(listOf(JeevixAiViolet, JeevixAiPink))),
            contentAlignment = Alignment.Center
          ) {
            Text("✨", fontSize = 18.sp)
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = LocalizationManager.get("ai_health_insights", languageCode),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Aggregated Health Passport Data",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Default.Close, contentDescription = "Close")
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        // Overall Vitality Status Card
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = JeevixAiViolet.copy(alpha = 0.08f)
          ),
          border = androidx.compose.foundation.BorderStroke(1.dp, JeevixAiViolet.copy(alpha = 0.3f))
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = report.overallStatus,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = JeevixAiViolet
              )
              Surface(
                shape = CircleShape,
                color = JeevixSuccess.copy(alpha = 0.15f)
              ) {
                Text(
                  text = "Score ${report.healthScore}/100",
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = JeevixSuccess
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "🩸 Donation Readiness: ${report.donationReadiness}",
              style = MaterialTheme.typography.bodySmall,
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }

        // Vital Trends Breakdown
        Text(
          text = "Biometric Vital Trends",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        report.vitalTrends.forEach { trend ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = trend.metricName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                  )
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (trend.isNormal) JeevixSuccess.copy(alpha = 0.15f) else JeevixEmergencyRed.copy(alpha = 0.15f)
                  ) {
                    Text(
                      text = trend.status,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold,
                      color = if (trend.isNormal) JeevixSuccess else JeevixEmergencyRed
                    )
                  }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = trend.trendDescription,
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = trend.value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }

        // Risk Alerts
        Text(
          text = "Clinical Risk & Allergy Screening",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        report.riskAlerts.forEach { alert ->
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(0xFFFEF3C7).copy(alpha = 0.8f),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = alert,
              modifier = Modifier.padding(10.dp),
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF92400E)
            )
          }
        }

        // Recommendations
        Text(
          text = "Personalized Recommendations",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        report.recommendations.forEach { rec ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
          ) {
            Text("• ", color = JeevixTeal, fontWeight = FontWeight.Bold)
            Text(
              text = rec,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Medical Disclaimer Card
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = "Info",
              tint = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = report.medicalDisclaimer,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 14.sp
            )
          }
        }
      }
    }
  )
}

@Composable
fun WearableDevicesDialog(
  wearables: List<WearableDevice>,
  connectedDevice: String?,
  onSyncDevice: (String) -> Unit,
  onConnectDevice: (String) -> Unit,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = JeevixTeal),
        shape = RoundedCornerShape(12.dp)
      ) {
        Text("Done", fontWeight = FontWeight.Bold)
      }
    },
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("⌚", fontSize = 22.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Wearable Health Devices",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Auto-sync Vitals & Biometric Telemetry",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = "Supported Wearables & Health Integrations",
          style = MaterialTheme.typography.labelLarge,
          fontWeight = FontWeight.Bold
        )

        wearables.forEach { device ->
          val isConnected = connectedDevice?.contains(device.brand, ignoreCase = true) == true ||
            connectedDevice?.contains(device.name, ignoreCase = true) == true

          Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isConnected) JeevixTeal.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            border = if (isConnected) androidx.compose.foundation.BorderStroke(1.5.dp, JeevixTeal.copy(alpha = 0.5f)) else null
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(device.iconEmoji, fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Column {
                    Text(
                      text = device.name,
                      style = MaterialTheme.typography.bodyMedium,
                      fontWeight = FontWeight.Bold
                    )
                    Text(
                      text = "${device.brand} • Battery ${device.batteryLevel}%",
                      style = MaterialTheme.typography.labelSmall,
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }

                if (isConnected) {
                  Surface(
                    shape = CircleShape,
                    color = JeevixSuccess.copy(alpha = 0.15f)
                  ) {
                    Row(
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Icon(Icons.Default.CheckCircle, contentDescription = null, tint = JeevixSuccess, modifier = Modifier.size(12.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text("Connected", style = MaterialTheme.typography.labelSmall, color = JeevixSuccess, fontWeight = FontWeight.Bold)
                    }
                  }
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Synced: ${device.syncedMetrics.joinToString(", ")}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )

              Spacer(modifier = Modifier.height(10.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                if (isConnected) {
                  OutlinedButton(
                    onClick = { onSyncDevice(device.name) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = JeevixTeal)
                  ) {
                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sync Live Vitals", style = MaterialTheme.typography.labelMedium)
                  }
                } else {
                  Button(
                    onClick = { onConnectDevice(device.name) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                  ) {
                    Text("Connect Device", style = MaterialTheme.typography.labelMedium)
                  }
                }
              }
            }
          }
        }
      }
    }
  )
}

@Composable
fun EmailVerificationDialog(
  currentEmail: String,
  onVerify: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var emailInput by remember { mutableStateOf(currentEmail) }
  var otpCode by remember { mutableStateOf("482910") }
  var step by remember { mutableStateOf(1) } // 1: Send OTP, 2: Enter OTP

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      if (step == 1) {
        Button(
          onClick = { step = 2 },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = JeevixTeal)
        ) {
          Text("Send Verification Code")
        }
      } else {
        Button(
          onClick = { onVerify(emailInput) },
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = JeevixSuccess)
        ) {
          Text("Confirm & Verify (+100 XP)")
        }
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    },
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Verified, contentDescription = null, tint = JeevixTeal)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Email Verification", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        if (step == 1) {
          Text(
            text = "Verify your email address to unlock trusted donor status and receive donation confirmations.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Email Address") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
        } else {
          Text(
            text = "Enter the 6-digit verification code sent to $emailInput:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = otpCode,
            onValueChange = { otpCode = it },
            label = { Text("Verification OTP Code") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "✨ Simulator OTP auto-filled: 482910",
            style = MaterialTheme.typography.labelSmall,
            color = JeevixSuccess
          )
        }
      }
    }
  )
}

@Composable
fun GovtIdVerificationDialog(
  onVerify: (String, String) -> Unit,
  onDismiss: () -> Unit
) {
  var idType by remember { mutableStateOf("Aadhaar Card") }
  var idNumber by remember { mutableStateOf("5421 8902 4821") }
  val types = listOf("Aadhaar Card", "Passport", "Voter ID Card", "Driver's License")

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = { onVerify(idType, idNumber) },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
      ) {
        Text("Verify & Activate Shield (+200 XP)")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    },
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("🛡️", fontSize = 22.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text("Government ID Verification", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
          Text("Golden Verified Donor Badge", style = MaterialTheme.typography.labelSmall, color = Color(0xFF8B5CF6))
        }
      }
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Verifying your official government identity elevates your donor trust rating and unlocks the Golden Verified Shield across the Jeevix blood network.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("Select Document Type:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))

        types.forEach { type ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { idType = type }
              .padding(vertical = 4.dp)
          ) {
            Surface(
              shape = CircleShape,
              color = if (idType == type) Color(0xFF8B5CF6) else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.size(16.dp)
            ) {}
            Spacer(modifier = Modifier.width(8.dp))
            Text(type, style = MaterialTheme.typography.bodyMedium)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
          value = idNumber,
          onValueChange = { idNumber = it },
          label = { Text("Document ID Number") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "🔒 Document number is encrypted & masked (e.g. XXXX-XXXX-4821)",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  )
}

@Composable
fun LanguageSelectionDialog(
  currentLanguage: String,
  onSelectLanguage: (String) -> Unit,
  onDismiss: () -> Unit
) {
  val languages = listOf(
    AppLanguage.ENGLISH,
    AppLanguage.HINDI,
    AppLanguage.GUJARATI
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
        Text("Done")
      }
    },
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Language, contentDescription = null, tint = JeevixTeal)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Language / भाषा / ભાષા", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        languages.forEach { lang ->
          val isSelected = currentLanguage.equals(lang.code, ignoreCase = true)
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) JeevixTeal.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, JeevixTeal) else null,
            modifier = Modifier
              .fillMaxWidth()
              .clickable {
                onSelectLanguage(lang.code)
                onDismiss()
              }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(lang.flag, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(lang.nativeName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                  Text(lang.displayName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
              }
              if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = JeevixTeal)
              }
            }
          }
        }
      }
    }
  )
}
