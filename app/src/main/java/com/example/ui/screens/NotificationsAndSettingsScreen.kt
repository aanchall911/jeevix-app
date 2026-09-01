package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.data.model.PrivacySettings
import com.example.data.model.UserProfile
import com.example.ui.components.AiHealthAnalysisDialog
import com.example.ui.components.EmailVerificationDialog
import com.example.ui.components.GovtIdVerificationDialog
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.components.WearableDevicesDialog
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.ui.util.LocalizationManager
import com.example.viewmodel.JeevixViewModel

@Composable
fun NotificationsScreen(
  viewModel: JeevixViewModel,
  onNavigateBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val notifications by viewModel.notifications.collectAsState()
  val activeTab by viewModel.notificationTab.collectAsState()
  val filterTabs = listOf("All", "Health", "Blood", "Emergency")

  val filteredNotifications = notifications.filter {
    if (activeTab == "All") true else it.type.equals(activeTab, ignoreCase = true)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onNavigateBack) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "Notifications",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
      }

      if (notifications.isNotEmpty()) {
        TextButton(onClick = { viewModel.clearAllNotifications() }) {
          Text("Clear All", color = JeevixCrimson)
        }
      }
    }

    // Filter Tabs
    ScrollableTabRow(
      selectedTabIndex = filterTabs.indexOf(activeTab).coerceAtLeast(0),
      edgePadding = 20.dp,
      containerColor = MaterialTheme.colorScheme.background,
      contentColor = JeevixCrimson,
      indicator = { tabPositions ->
        val index = filterTabs.indexOf(activeTab).coerceAtLeast(0)
        TabRowDefaults.Indicator(
          Modifier.tabIndicatorOffset(tabPositions[index]),
          color = JeevixCrimson,
          height = 3.dp
        )
      },
      divider = {}
    ) {
      filterTabs.forEach { tab ->
        val isSelected = activeTab == tab
        Tab(
          selected = isSelected,
          onClick = { viewModel.setNotificationTab(tab) },
          text = {
            Text(
              text = tab,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        )
      }
    }

    // Notification List
    if (filteredNotifications.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(text = "🔔", fontSize = 48.sp)
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No notifications yet",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "You're all caught up with your health updates & blood alerts.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredNotifications, key = { it.id }) { notif ->
          NotificationItemCard(
            item = notif,
            onDismiss = { viewModel.dismissNotification(notif.id) }
          )
        }
      }
    }
  }
}

@Composable
fun NotificationItemCard(
  item: NotificationItem,
  onDismiss: () -> Unit
) {
  val iconBg = when (item.type) {
    "Emergency" -> JeevixEmergencyRed.copy(alpha = 0.15f)
    "Blood" -> JeevixCrimson.copy(alpha = 0.15f)
    else -> JeevixTeal.copy(alpha = 0.15f)
  }

  val iconColor = when (item.type) {
    "Emergency" -> JeevixEmergencyRed
    "Blood" -> JeevixCrimson
    else -> JeevixTeal
  }

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (item.isRead) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ),
    border = if (!item.isRead) androidx.compose.foundation.BorderStroke(1.dp, JeevixCrimson.copy(alpha = 0.2f)) else null,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = when (item.type) {
            "Emergency" -> Icons.Default.Emergency
            "Blood" -> Icons.Default.WaterDrop
            else -> Icons.Default.Notifications
          },
          contentDescription = null,
          tint = iconColor,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = item.title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = item.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 2.dp)
        )
        Text(
          text = item.timeAgo,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 10.sp,
          modifier = Modifier.padding(top = 4.dp)
        )
      }

      IconButton(onClick = onDismiss) {
        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
      }
    }
  }
}

@Composable
fun SettingsAndPrivacyScreen(
  viewModel: JeevixViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val userProfile by viewModel.userProfile.collectAsState()
  val privacySettings by viewModel.privacySettings.collectAsState()
  val showWearableDialog by viewModel.showWearableDialog.collectAsState()
  val showAiHealthDialog by viewModel.showAiAnalysisDialog.collectAsState()
  val showEmailDialog by viewModel.showEmailVerifyDialog.collectAsState()
  val showIdDialog by viewModel.showIdVerifyDialog.collectAsState()
  val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()

  val currentLang = userProfile?.selectedLanguage ?: "en"

  var shareBloodGroup by remember { mutableStateOf(privacySettings.shareBloodGroupWithRequesters) }
  var hideMedicalHistory by remember { mutableStateOf(privacySettings.hideMedicalHistoryFromPublic) }
  var sosGpsOnly by remember { mutableStateOf(privacySettings.allowGpsDuringSosOnly) }
  var allowAiPassport by remember { mutableStateOf(privacySettings.allowAiToReadPassport) }

  // Dialogs
  if (showWearableDialog) {
    WearableDevicesDialog(
      wearables = viewModel.getSupportedWearables(),
      connectedDevice = userProfile?.connectedWearable,
      onSyncDevice = { brand -> viewModel.syncWearableDevice(brand) },
      onConnectDevice = { brand -> viewModel.syncWearableDevice(brand) },
      onDismiss = { viewModel.showWearableDialog(false) }
    )
  }

  if (showAiHealthDialog) {
    AiHealthAnalysisDialog(
      report = viewModel.getAiHealthReport(),
      languageCode = currentLang,
      onDismiss = { viewModel.showAiAnalysisDialog(false) }
    )
  }

  if (showEmailDialog) {
    EmailVerificationDialog(
      currentEmail = userProfile?.email ?: "aanchalbb11@gmail.com",
      onVerify = { email -> viewModel.verifyEmail(email) },
      onDismiss = { viewModel.showEmailVerifyDialog(false) }
    )
  }

  if (showIdDialog) {
    GovtIdVerificationDialog(
      onVerify = { idType, idNum -> viewModel.verifyGovtId(idType, idNum) },
      onDismiss = { viewModel.showIdVerifyDialog(false) }
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
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // Header
    item {
      Text(
        text = "Settings & Preferences 🔒",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Strict consent-driven data ownership & medical guardrails",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // Profile Summary Card
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(54.dp)
              .clip(CircleShape)
              .background(JeevixCrimson.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "🩸", fontSize = 24.sp)
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = userProfile?.fullName ?: "Aanchal Singh",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              if (userProfile?.isIdVerified == true) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.Default.Verified, contentDescription = "Shield Verified", tint = Color(0xFF8B5CF6), modifier = Modifier.size(16.dp))
              }
            }
            Text(
              text = "Blood Group: ${userProfile?.bloodGroup ?: "O+"} • ID: ${userProfile?.jeevixId ?: "JVX-8942-O+"}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = userProfile?.email ?: "aanchalbb11@gmail.com",
              style = MaterialTheme.typography.bodySmall,
              color = JeevixCrimsonDark,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }

    // SECTION: LANGUAGE & REGIONALIZATION
    item {
      Text(
        text = "Language & Regionalization",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable { viewModel.showLanguageDialog(true) }
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Language, contentDescription = null, tint = JeevixTeal)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "App Language / भाषा / ભાષા",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (currentLang == "hi") "हिन्दी (Hindi)" else if (currentLang == "gu") "ગુજરાતી (Gujarati)" else "English",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Surface(shape = RoundedCornerShape(8.dp), color = JeevixTeal.copy(alpha = 0.12f)) {
            Text(
              text = "Change",
              color = JeevixTeal,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
            )
          }
        }
      }
    }

    // SECTION: WEARABLE & AI REPORT SHORTCUTS
    item {
      Text(
        text = "Wearable Health & AI Insights",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Wearable Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Watch, contentDescription = null, tint = Color(0xFF0284C7))
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "Smartwatch / Wearable Sync",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = if (userProfile?.connectedWearable != null) "Connected: ${userProfile?.connectedWearable} (Live Sync)" else "Connect Apple Watch / Fitbit",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            OutlinedButton(
              onClick = { viewModel.showWearableDialog(true) },
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Sync", style = MaterialTheme.typography.labelSmall)
            }
          }

          Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

          // AI Health Analysis Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = JeevixAiViolet)
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = "AI Health Passport Analysis",
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = "Clinical risk detection & trends report",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Button(
              onClick = { viewModel.showAiAnalysisDialog(true) },
              colors = ButtonDefaults.buttonColors(containerColor = JeevixAiViolet),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text("Analyze", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // SECTION: GRANULAR CONSENT AND PRIVACY
    item {
      Text(
        text = "Granular Data Sharing Controls",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Toggle 1
          PrivacyToggleRow(
            title = "Share Blood Group with Requesters",
            subtitle = "Allows nearby hospitals and blood seekers to find your blood compatibility.",
            checked = shareBloodGroup,
            onCheckedChange = {
              shareBloodGroup = it
              viewModel.updatePrivacySettings(privacySettings.copy(shareBloodGroupWithRequesters = it))
            }
          )

          Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

          // Toggle 2
          PrivacyToggleRow(
            title = "Hide Full Medical History",
            subtitle = "Requesters never see your allergies, surgeries, or medications. Only blood info is shared.",
            checked = hideMedicalHistory,
            onCheckedChange = {
              hideMedicalHistory = it
              viewModel.updatePrivacySettings(privacySettings.copy(hideMedicalHistoryFromPublic = it))
            }
          )

          Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

          // Toggle 3
          PrivacyToggleRow(
            title = "GPS Active During SOS Only",
            subtitle = "Your real-time GPS coordinates are only broadcasted when you trigger SOS.",
            checked = sosGpsOnly,
            onCheckedChange = {
              sosGpsOnly = it
              viewModel.updatePrivacySettings(privacySettings.copy(allowGpsDuringSosOnly = it))
            }
          )

          Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

          // Toggle 4
          PrivacyToggleRow(
            title = "Grounded Jeevix AI Analysis",
            subtitle = "Enable Jeevix AI to answer clinical questions using your local Health Passport data.",
            checked = allowAiPassport,
            onCheckedChange = {
              allowAiPassport = it
              viewModel.updatePrivacySettings(privacySettings.copy(allowAiToReadPassport = it))
            }
          )
        }
      }
    }

    // SECTION: EMERGENCY CARD EXPORT
    item {
      Text(
        text = "Offline Health Passport",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.QrCode2, contentDescription = null, tint = JeevixCrimson)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(text = "Download Offline Emergency Card", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = "Save digital ID to lockscreen / wallet", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            Button(
              onClick = {
                viewModel.showEmergencyQr(true)
                Toast.makeText(context, "Emergency Card Ready for Offline Use", Toast.LENGTH_SHORT).show()
              },
              colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("View ID", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // SECTION: APP INFO
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(text = "Jeevix • Digital Health Passport & Lifeline Network", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
          Text(text = "Version 1.0.0 (Production Release)", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(text = "Encrypted local SQLite storage • Strict user consent compliance", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun PrivacyToggleRow(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 2.dp)
      )
    }

    Spacer(modifier = Modifier.width(12.dp))

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = JeevixCrimson,
        checkedTrackColor = JeevixCrimson.copy(alpha = 0.4f)
      )
    )
  }
}
