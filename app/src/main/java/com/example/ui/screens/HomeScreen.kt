package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AiAssistantCard
import com.example.ui.components.HealthPassportDigitalCard
import com.example.ui.components.ProminentSosBanner
import com.example.ui.components.VitalStatTile
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
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

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background),
    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // Header Greeting & Notification Bell
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Sleek Avatar with Rose border
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(Color(0xFFFFE4E6)) // Rose 100
              .shadow(2.dp, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = userProfile?.fullName?.take(1)?.uppercase() ?: "A",
              color = Color(0xFFE11D48), // Rose 600
              fontWeight = FontWeight.Bold,
              fontSize = 18.sp
            )
          }

          Column {
            Text(
              text = "GOOD MORNING",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF64748B), // Slate 500
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.2.sp,
              fontSize = 11.sp
            )
            Text(
              text = userProfile?.fullName ?: "Aanchal Singh",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF0F172A), // Slate 900
              fontSize = 18.sp
            )
          }
        }

        // Notification Icon with sleek rounded-2xl container
        Box {
          Surface(
            onClick = onNavigateToNotifications,
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
            shadowElevation = 2.dp,
            modifier = Modifier.size(44.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color(0xFF475569), // Slate 600
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
          title = "Find Blood",
          icon = Icons.Default.Bloodtype,
          bgColor = Color(0xFFEFF6FF), // Blue 50
          iconColor = Color(0xFF2563EB), // Blue 600
          onClick = onNavigateToDonate,
          modifier = Modifier.weight(1f)
        )
        QuickActionTile(
          title = "Hospital",
          icon = Icons.Default.LocalHospital,
          bgColor = Color(0xFFECFDF5), // Emerald 50
          iconColor = Color(0xFF059669), // Emerald 600
          onClick = { onNavigateToNearby("Hospitals") },
          modifier = Modifier.weight(1f)
        )
        QuickActionTile(
          title = "Bank",
          icon = Icons.Default.WaterDrop,
          bgColor = Color(0xFFFFF7ED), // Orange 50
          iconColor = Color(0xFFEA580C), // Orange 600
          onClick = { onNavigateToNearby("Blood Banks") },
          modifier = Modifier.weight(1f)
        )
        QuickActionTile(
          title = "Find Donor",
          icon = Icons.Default.Groups,
          bgColor = Color(0xFFFAF5FF), // Purple 50
          iconColor = Color(0xFF9333EA), // Purple 600
          onClick = onNavigateToDonate,
          modifier = Modifier.weight(1f)
        )
      }
    }

    // CARD 3: HEALTH SNAPSHOT
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
                text = "Health Snapshot",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFF8FAFC)
            ) {
              Text(
                text = "TODAY",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            VitalStatTile(
              title = "Heart Rate",
              value = "${userProfile?.heartRate ?: 72}",
              unit = "BPM",
              icon = Icons.Default.Favorite,
              accentColor = Color(0xFFF43F5E),
              modifier = Modifier.weight(1f)
            )
            VitalStatTile(
              title = "Pressure",
              value = "${userProfile?.systolicBp ?: 118}/${userProfile?.diastolicBp ?: 76}",
              unit = "",
              icon = Icons.Default.Speed,
              accentColor = Color(0xFF059669),
              modifier = Modifier.weight(1.1f)
            )
            VitalStatTile(
              title = "SpO₂",
              value = "${userProfile?.spo2 ?: 98}",
              unit = "%",
              icon = Icons.Default.WaterDrop,
              accentColor = Color(0xFF9333EA),
              modifier = Modifier.weight(1f)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Recorded on device",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF94A3B8),
              fontSize = 10.sp
            )
            TextButton(
              onClick = { viewModel.showVitalsUpdateDialog(true) },
              contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 0.dp)
            ) {
              Text(
                text = "Edit Vitals ✎",
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
      shape = RoundedCornerShape(18.dp),
      color = bgColor,
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
      modifier = Modifier
        .fillMaxWidth()
        .size(56.dp)
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = iconColor,
          modifier = Modifier.size(24.dp)
        )
      }
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = title.uppercase(),
      style = MaterialTheme.typography.labelSmall,
      color = Color(0xFF64748B), // Slate 500
      fontWeight = FontWeight.Bold,
      fontSize = 10.sp,
      letterSpacing = 0.5.sp,
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
