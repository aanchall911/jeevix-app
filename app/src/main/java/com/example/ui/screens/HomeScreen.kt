package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AiAssistantCard
import com.example.ui.components.AiHealthAnalysisDialog
import com.example.ui.components.HealthPassportDigitalCard
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.components.ProminentSosBanner
import com.example.ui.components.VitalStatTile
import com.example.ui.components.WearableDevicesDialog
import com.example.ui.theme.JeevixAiPink
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.ui.util.LocalizationManager
import com.example.viewmodel.JeevixViewModel

@Composable
fun HomeScreen(
  viewModel: JeevixViewModel,
  onNavigateToPassport: () -> Unit,
  onNavigateToDonate: () -> Unit,
  onNavigateToNearby: (String) -> Unit,
  onNavigateToAi: (String?) -> Unit,
  onNavigateToNotifications: () -> Unit,
  modifier: Modifier = Modifier
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val unreadNotificationsCount = notifications.count { !it.isRead }
  val showVitalsDialog by viewModel.showVitalsUpdateDialog.collectAsState()
  val showAiDialog by viewModel.showAiAnalysisDialog.collectAsState()
  val showWearableDialog by viewModel.showWearableDialog.collectAsState()
  val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()

  val currentLang = userProfile?.selectedLanguage ?: "en"

  if (showVitalsDialog) {
    VitalsUpdateDialog(
      currentHr = userProfile?.heartRate ?: 72,
      currentSystolic = userProfile?.systolicBp ?: 118,
      currentDiastolic = userProfile?.diastolicBp ?: 76,
      currentSpo2 = userProfile?.spo2 ?: 98,
      onDismiss = { viewModel.showVitalsUpdateDialog(false) },
      onSave = { hr, sys, dia, spo2 ->
        viewModel.updateVitals(hr, sys, dia, spo2)
        viewModel.showVitalsUpdateDialog(false)
      }
    )
  }

  if (showAiDialog) {
    AiHealthAnalysisDialog(
      report = viewModel.getAiHealthReport(),
      languageCode = currentLang,
      onDismiss = { viewModel.showAiAnalysisDialog(false) }
    )
  }

  if (showWearableDialog) {
    WearableDevicesDialog(
      wearables = viewModel.getSupportedWearables(),
      connectedDevice = userProfile?.connectedWearable,
      onSyncDevice = { deviceName -> viewModel.syncWearableDevice(deviceName) },
      onConnectDevice = { deviceName -> viewModel.syncWearableDevice(deviceName) },
      onDismiss = { viewModel.showWearableDialog(false) }
    )
  }

  if (showLanguageDialog) {
    LanguageSelectionDialog(
      currentLanguage = currentLang,
      onSelectLanguage = { lang -> viewModel.setLanguage(lang) },
      onDismiss = { viewModel.showLanguageDialog(false) }
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Header Greeting & Language / Notification Bell
    item {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            // Sleek Avatar & Official App Brand Logo
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // Branded Official App Logo
              Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF0F172A),
                shadowElevation = 4.dp,
                modifier = Modifier.size(46.dp)
              ) {
                Image(
                  painter = painterResource(id = R.drawable.jeevix_app_logo_vector_1788238539465),
                  contentDescription = "Jeevix App Logo",
                  modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp))
                )
              }

              // User Avatar with Verified indicator
              Box {
                Box(
                  modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                      brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFE4E6), Color(0xFFFECDD3))
                      )
                    )
                    .shadow(3.dp, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = userProfile?.fullName?.take(1)?.uppercase() ?: "A",
                    color = Color(0xFFE11D48),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                  )
                }
                // Green active dot
                Box(
                  modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(13.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
                ) {
                  Box(
                    modifier = Modifier
                      .fillMaxSize()
                      .clip(CircleShape)
                      .background(Color(0xFF10B981))
                  )
                }
              }
            }

            Column {
              Text(
                text = LocalizationManager.get("good_morning", currentLang).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.4.sp,
                fontSize = 10.sp
              )
              Text(
                text = userProfile?.fullName ?: "Aanchal Singh",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F172A),
                fontSize = 19.sp
              )
            }
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            // Language Switcher Icon
            Surface(
              onClick = { viewModel.showLanguageDialog(true) },
              shape = RoundedCornerShape(16.dp),
              color = Color.White,
              border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
              shadowElevation = 2.dp,
              modifier = Modifier.size(44.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = if (currentLang == "hi") "हिं" else if (currentLang == "gu") "ગુ" else "EN",
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 13.5.sp,
                  color = JeevixTeal
                )
              }
            }

            // Notification Icon
            Box {
              Surface(
                onClick = onNavigateToNotifications,
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                shadowElevation = 2.dp,
                modifier = Modifier.size(44.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF475569),
                    modifier = Modifier.size(22.dp)
                  )
                }
              }

              if (unreadNotificationsCount > 0) {
                Box(
                  modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE11D48)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "$unreadNotificationsCount",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }

        // Live Lifeline Network Status Bar
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFFF0FDF4), // Emerald 50
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCFCE7))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(Color(0xFF16A34A))
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Lifeline Network Active • Bengaluru Health Grid",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF166534),
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
              )
            }
            Text(
              text = "24x7 🛡️",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF15803D),
              fontWeight = FontWeight.ExtraBold,
              fontSize = 10.sp
            )
          }
        }
      }
    }

    // CARD 1: JEEVIX HEALTH PASSPORT DIGITAL CARD
    item {
      HealthPassportDigitalCard(
        profile = userProfile,
        onViewPassport = onNavigateToPassport
      )
    }

    // CARD 2: QUICK ACTIONS GRID (4-COLUMN PILLARS)
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickActionTile(
          title = LocalizationManager.get("find_blood", currentLang),
          icon = Icons.Default.Bloodtype,
          bgColor = Color(0xFFEFF6FF), // Blue 50
          iconColor = Color(0xFF2563EB), // Blue 600
          onClick = onNavigateToDonate,
          modifier = Modifier.weight(1f)
        )
        QuickActionTile(
          title = LocalizationManager.get("hospital", currentLang),
          icon = Icons.Default.LocalHospital,
          bgColor = Color(0xFFECFDF5), // Emerald 50
          iconColor = Color(0xFF059669), // Emerald 600
          onClick = { onNavigateToNearby("Hospitals") },
          modifier = Modifier.weight(1f)
        )
        QuickActionTile(
          title = LocalizationManager.get("blood_bank", currentLang),
          icon = Icons.Default.WaterDrop,
          bgColor = Color(0xFFFFF7ED), // Orange 50
          iconColor = Color(0xFFEA580C), // Orange 600
          onClick = { onNavigateToNearby("Blood Banks") },
          modifier = Modifier.weight(1f)
        )
        QuickActionTile(
          title = LocalizationManager.get("find_donor", currentLang),
          icon = Icons.Default.Groups,
          bgColor = Color(0xFFFAF5FF), // Purple 50
          iconColor = Color(0xFF9333EA), // Purple 600
          onClick = onNavigateToDonate,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // CARD 3: HEALTH SNAPSHOT & WEARABLE INTEGRATION
    item {
      Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .width(5.dp)
                  .height(16.dp)
                  .clip(RoundedCornerShape(3.dp))
                  .background(Color(0xFFF43F5E))
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = LocalizationManager.get("health_snapshot", currentLang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
              )
            }

            // Wearable status pill with click action
            Surface(
              onClick = { viewModel.showWearableDialog(true) },
              shape = RoundedCornerShape(12.dp),
              color = JeevixTeal.copy(alpha = 0.1f),
              border = androidx.compose.foundation.BorderStroke(1.dp, JeevixTeal.copy(alpha = 0.3f))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("⌚ ", fontSize = 11.sp)
                Text(
                  text = userProfile?.connectedWearable ?: "Fitbit Charge 6",
                  style = MaterialTheme.typography.labelSmall,
                  color = JeevixTeal,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.Default.Sync,
                  contentDescription = "Sync",
                  tint = JeevixTeal,
                  modifier = Modifier.size(12.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // 3 Vitals Tiles
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            VitalStatTile(
              title = LocalizationManager.get("heart_rate", currentLang),
              value = "${userProfile?.heartRate ?: 72}",
              unit = LocalizationManager.get("bpm", currentLang),
              icon = Icons.Default.Favorite,
              accentColor = Color(0xFFF43F5E),
              modifier = Modifier.weight(1f)
            )
            VitalStatTile(
              title = LocalizationManager.get("blood_pressure", currentLang),
              value = "${userProfile?.systolicBp ?: 118}/${userProfile?.diastolicBp ?: 76}",
              unit = "mmHg",
              icon = Icons.Default.Speed,
              accentColor = Color(0xFF059669),
              modifier = Modifier.weight(1.1f)
            )
            VitalStatTile(
              title = LocalizationManager.get("spo2", currentLang),
              value = "${userProfile?.spo2 ?: 98}",
              unit = "%",
              icon = Icons.Default.WaterDrop,
              accentColor = Color(0xFF9333EA),
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // AI Health Insights banner button
          Surface(
            onClick = { viewModel.showAiAnalysisDialog(true) },
            shape = RoundedCornerShape(14.dp),
            color = JeevixAiViolet.copy(alpha = 0.08f),
            border = androidx.compose.foundation.BorderStroke(1.dp, JeevixAiViolet.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = LocalizationManager.get("view_ai_analysis", currentLang),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = JeevixAiViolet
                  )
                  Text(
                    text = "Donation readiness & vital risk trends",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                  )
                }
              }
              Text(
                text = "Inspect →",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = JeevixAiViolet
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${userProfile?.wearableSyncStatus ?: "Synced 5m ago"} • Steps: ${userProfile?.dailySteps ?: 8420}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF94A3B8),
              fontSize = 10.sp
            )
            TextButton(
              onClick = { viewModel.showVitalsUpdateDialog(true) },
              contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
              Text(
                text = LocalizationManager.get("edit_vitals", currentLang),
                color = Color(0xFFF43F5E),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // CARD 4: JEEVIX AI ASSISTANT BANNER
    item {
      AiAssistantCard(
        onAskClick = { onNavigateToAi(null) },
        onPromptSelect = { prompt -> onNavigateToAi(prompt) }
      )
    }

    // CARD 5: PROMINENT SOS CARD
    item {
      ProminentSosBanner(
        onSosClick = { viewModel.triggerSosConfirmation(true) }
      )
    }

    // Spacer for bottom floating bar
    item {
      Spacer(modifier = Modifier.height(60.dp))
    }
  }
}

@Composable
fun QuickActionTile(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  bgColor: Color,
  iconColor: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .clickable(onClick = onClick),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = bgColor,
      border = androidx.compose.foundation.BorderStroke(1.dp, iconColor.copy(alpha = 0.2f)),
      shadowElevation = 3.dp,
      modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = iconColor,
          modifier = Modifier.size(26.dp)
        )
      }
    }
    Spacer(modifier = Modifier.height(7.dp))
    Text(
      text = title.uppercase(),
      style = MaterialTheme.typography.labelSmall,
      color = Color(0xFF475569),
      fontWeight = FontWeight.ExtraBold,
      fontSize = 10.sp,
      letterSpacing = 0.4.sp,
      textAlign = TextAlign.Center,
      maxLines = 1
    )
  }
}

@Composable
fun VitalsUpdateDialog(
  currentHr: Int,
  currentSystolic: Int,
  currentDiastolic: Int,
  currentSpo2: Int,
  onDismiss: () -> Unit,
  onSave: (hr: Int, systolic: Int, diastolic: Int, spo2: Int) -> Unit
) {
  var hr by remember { mutableIntStateOf(currentHr) }
  var sys by remember { mutableIntStateOf(currentSystolic) }
  var dia by remember { mutableIntStateOf(currentDiastolic) }
  var spo2 by remember { mutableIntStateOf(currentSpo2) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "❤️ Log Health Vitals")
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
          value = "$hr",
          onValueChange = { hr = it.toIntOrNull() ?: hr },
          label = { Text("Heart Rate (BPM)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = "$sys",
            onValueChange = { sys = it.toIntOrNull() ?: sys },
            label = { Text("Systolic BP") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          )
          OutlinedTextField(
            value = "$dia",
            onValueChange = { dia = it.toIntOrNull() ?: dia },
            label = { Text("Diastolic BP") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp)
          )
        }
        OutlinedTextField(
          value = "$spo2",
          onValueChange = { spo2 = it.toIntOrNull() ?: spo2 },
          label = { Text("SpO₂ Percentage (%)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onSave(hr, sys, dia, spo2) },
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)
      ) {
        Text("Save Vitals")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  )
}
