package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BloodDonation
import com.example.data.model.DonorRewardBadge
import com.example.data.model.DonorRewards
import com.example.data.model.EmergencyContact
import com.example.data.model.MedicalRecord
import com.example.data.model.UserProfile
import com.example.ui.components.CanvasEmergencyQrCode
import com.example.ui.components.DonorBadgeDetailDialog
import com.example.ui.components.DonorMilestoneProgressCard
import com.example.ui.components.DonorRewardBadgeChip
import com.example.ui.components.HealthPassportDigitalCard
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.viewmodel.JeevixViewModel

@Composable
fun HealthPassportScreen(
  viewModel: JeevixViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val userProfile by viewModel.userProfile.collectAsState()
  val medicalRecords by viewModel.medicalRecords.collectAsState()
  val emergencyContacts by viewModel.emergencyContacts.collectAsState()
  val donations by viewModel.bloodDonations.collectAsState()
  val showQrDialog by viewModel.showEmergencyQrDialog.collectAsState()

  val showAddRecordDialog by viewModel.showAddRecordDialog.collectAsState()
  val showAddDonationDialog by viewModel.showAddDonationDialog.collectAsState()
  val showAddContactDialog by viewModel.showAddContactDialog.collectAsState()

  var selectedTab by remember { mutableIntStateOf(0) }
  val tabs = listOf("Blood Profile", "Medical History", "Donations", "Emergency Contacts")

  // Emergency QR Code Dialog
  if (showQrDialog) {
    EmergencyPassportQrDialog(
      profile = userProfile,
      records = medicalRecords,
      contacts = emergencyContacts,
      onDismiss = { viewModel.showEmergencyQr(false) }
    )
  }

  // Add Medical Record Dialog
  if (showAddRecordDialog) {
    AddMedicalRecordDialog(
      onDismiss = { viewModel.showAddRecordDialog(false) },
      onSave = { type, title, desc, year, severity, notes ->
        viewModel.addMedicalRecord(type, title, desc, year, severity, notes)
        viewModel.showAddRecordDialog(false)
      }
    )
  }

  // Add Blood Donation Dialog
  if (showAddDonationDialog) {
    AddDonationDialog(
      onDismiss = { viewModel.showAddDonationDialog(false) },
      onSave = { date, facility, units, notes ->
        viewModel.addDonation(date, facility, units, notes)
        viewModel.showAddDonationDialog(false)
      }
    )
  }

  // Add Emergency Contact Dialog
  if (showAddContactDialog) {
    AddContactDialog(
      onDismiss = { viewModel.showAddContactDialog(false) },
      onSave = { name, relation, phone, isPrimary ->
        viewModel.addEmergencyContact(name, relation, phone, isPrimary)
        viewModel.showAddContactDialog(false)
      }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Top Bar Actions
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Health Passport",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Digital ID • Verified Lifeline Network",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // QR Trigger
        IconButton(
          onClick = { viewModel.showEmergencyQr(true) },
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(JeevixCrimson.copy(alpha = 0.12f))
        ) {
          Icon(Icons.Default.QrCode2, contentDescription = "QR Code", tint = JeevixCrimson)
        }

        // Share Action
        IconButton(
          onClick = {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
              type = "text/plain"
              putExtra(
                Intent.EXTRA_TEXT,
                "Jeevix Digital Health Passport\nName: ${userProfile?.fullName}\nBlood Group: ${userProfile?.bloodGroup}\nJeevix ID: ${userProfile?.jeevixId}\nEmergency Contact: ${emergencyContacts.firstOrNull()?.name} (${emergencyContacts.firstOrNull()?.phoneNumber})"
              )
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share Health Passport"))
          },
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.onSurface)
        }
      }
    }

    // Scrollable Tab Row
    ScrollableTabRow(
      selectedTabIndex = selectedTab,
      edgePadding = 20.dp,
      containerColor = MaterialTheme.colorScheme.background,
      contentColor = JeevixCrimson,
      indicator = { tabPositions ->
        TabRowDefaults.Indicator(
          Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
          color = JeevixCrimson,
          height = 3.dp
        )
      },
      divider = {}
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
              color = if (selectedTab == index) JeevixCrimson else MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        )
      }
    }

    // Tab Content Body
    when (selectedTab) {
      0 -> BloodProfileTab(
        profile = userProfile,
        onUpdateStatus = { isDonor, status -> viewModel.updateDonorStatus(isDonor, status) },
        onOpenQr = { viewModel.showEmergencyQr(true) }
      )
      1 -> MedicalHistoryTab(
        records = medicalRecords,
        onAddClick = { viewModel.showAddRecordDialog(true) },
        onDeleteRecord = { viewModel.deleteMedicalRecord(it) }
      )
      2 -> DonationHistoryTab(
        donations = donations,
        rewards = viewModel.getDonorRewards(),
        onAddClick = { viewModel.showAddDonationDialog(true) }
      )
      3 -> EmergencyContactsTab(
        contacts = emergencyContacts,
        onAddClick = { viewModel.showAddContactDialog(true) },
        onDeleteContact = { viewModel.deleteEmergencyContact(it) }
      )
    }
  }
}

@Composable
fun BloodProfileTab(
  profile: UserProfile?,
  onUpdateStatus: (Boolean, String) -> Unit,
  onOpenQr: () -> Unit
) {
  val bloodGroup = profile?.bloodGroup ?: "O+"
  val isDonor = profile?.isDonor ?: true
  val donorStatus = profile?.donorStatus ?: "Available"

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      HealthPassportDigitalCard(profile = profile, onViewPassport = onOpenQr)
    }

    item {
      Text(
        text = "Blood Compatibility Overview",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
    }

    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Can Donate Blood To",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = when (bloodGroup) {
                  "O-" -> "All Blood Types (Universal Red Cell Donor 🌟)"
                  "O+" -> "O+, A+, B+, AB+"
                  "A+" -> "A+, AB+"
                  "A-" -> "A+, A-, AB+, AB-"
                  "B+" -> "B+, AB+"
                  "B-" -> "B+, B-, AB+, AB-"
                  "AB+" -> "AB+ Only"
                  "AB-" -> "AB+, AB-"
                  else -> "Compatible matched recipients"
                },
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = JeevixCrimsonDark,
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Column {
            Text(
              text = "Can Receive Blood From",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
              text = when (bloodGroup) {
                "O-" -> "O- Only"
                "O+" -> "O+, O-"
                "A+" -> "A+, A-, O+, O-"
                "A-" -> "A-, O-"
                "B+" -> "B+, B-, O+, O-"
                "B-" -> "B-, O-"
                "AB+" -> "All Blood Types (Universal Recipient 🌟)"
                "AB-" -> "AB-, A-, B-, O-"
                else -> "Compatible screened blood"
              },
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = JeevixTeal,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }
      }
    }

    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Donor Availability Status",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            listOf("Available", "Busy", "Inactive").forEach { status ->
              val isSelected = isDonor && donorStatus == status
              Surface(
                onClick = { onUpdateStatus(status != "Inactive", status) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  text = status,
                  style = MaterialTheme.typography.labelMedium,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  textAlign = TextAlign.Center,
                  modifier = Modifier.padding(vertical = 10.dp)
                )
              }
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun MedicalHistoryTab(
  records: List<MedicalRecord>,
  onAddClick: () -> Unit,
  onDeleteRecord: (Long) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Medical Profile & Records",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "${records.size} active records logged",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = onAddClick,
          colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Record", fontWeight = FontWeight.Bold)
        }
      }
    }

    if (records.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(text = "🛡️", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "No medical records logged yet",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Tap + Add Record to add allergies, chronic conditions, surgeries, or medications.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }
      }
    } else {
      items(records, key = { it.id }) { record ->
        MedicalRecordItemCard(record = record, onDelete = { onDeleteRecord(record.id) })
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun MedicalRecordItemCard(
  record: MedicalRecord,
  onDelete: () -> Unit
) {
  val typeColor = when (record.type) {
    "Allergy" -> JeevixEmergencyRed
    "Medication" -> JeevixTeal
    "Condition" -> JeevixAiViolet
    "Surgery" -> Color(0xFFD97706)
    else -> JeevixCrimson
  }

  Card(
    shape = RoundedCornerShape(16.dp),
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
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = typeColor.copy(alpha = 0.12f),
          border = androidx.compose.foundation.BorderStroke(1.dp, typeColor.copy(alpha = 0.3f))
        ) {
          Text(
            text = record.type.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = typeColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = record.yearOrDate,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.width(6.dp))
          IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(16.dp))
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = record.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )

      if (record.description.isNotBlank()) {
        Text(
          text = record.description,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(top = 2.dp)
        )
      }

      if (record.severityOrDosage.isNotBlank()) {
        Text(
          text = "Dosage / Severity: ${record.severityOrDosage}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.padding(top = 4.dp)
        )
      }
    }
  }
}

@Composable
fun DonationHistoryTab(
  donations: List<BloodDonation>,
  rewards: DonorRewards,
  onAddClick: () -> Unit
) {
  var selectedBadgeForDialog by remember { mutableStateOf<DonorRewardBadge?>(null) }

  if (selectedBadgeForDialog != null) {
    DonorBadgeDetailDialog(
      badge = selectedBadgeForDialog!!,
      onDismiss = { selectedBadgeForDialog = null }
    )
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // 1. MILESTONE PROGRESS BAR CARD
    item {
      DonorMilestoneProgressCard(
        rewards = rewards,
        onBadgeClick = { selectedBadgeForDialog = it }
      )
    }

    // 2. DYNAMIC BADGE CHIPS
    item {
      Column {
        Text(
          text = "Active Lifesaver Badges",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(rewards.earnedBadges, key = { it.id }) { badge ->
            DonorRewardBadgeChip(
              badge = badge,
              onClick = { selectedBadgeForDialog = badge }
            )
          }
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Verified Blood Donations",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "${donations.sumOf { it.unitsGiven }} total units donated",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = onAddClick,
          colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Log Donation", fontWeight = FontWeight.Bold)
        }
      }
    }

    items(donations, key = { it.id }) { donation ->
      Card(
        shape = RoundedCornerShape(16.dp),
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
              .size(46.dp)
              .clip(CircleShape)
              .background(JeevixCrimson.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = "🩸", fontSize = 22.sp)
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = donation.facilityName,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = "Date: ${donation.date} • ${donation.unitsGiven} Unit (${donation.bloodGroup})",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (donation.certificateNumber.isNotBlank()) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = JeevixSuccess.copy(alpha = 0.1f),
                modifier = Modifier.padding(top = 4.dp)
              ) {
                Text(
                  text = "✓ ${donation.certificateNumber}",
                  style = MaterialTheme.typography.labelSmall,
                  color = JeevixSuccess,
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                  fontSize = 10.sp
                )
              }
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun EmergencyContactsTab(
  contacts: List<EmergencyContact>,
  onAddClick: () -> Unit,
  onDeleteContact: (Long) -> Unit
) {
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Emergency Contacts",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Notified instantly in SOS triggers",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = onAddClick,
          colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Add Contact", fontWeight = FontWeight.Bold)
        }
      }
    }

    items(contacts, key = { it.id }) { contact ->
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (contact.isPrimary) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (contact.isPrimary) JeevixEmergencyRed.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Color(contact.avatarColorHex).copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = if (contact.name.isNotBlank()) contact.name.take(1) else "C",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(contact.avatarColorHex)
              )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = contact.name,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                if (contact.isPrimary) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = JeevixEmergencyRed
                  ) {
                    Text(
                      text = "PRIMARY",
                      style = MaterialTheme.typography.labelSmall,
                      color = Color.White,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                      fontSize = 9.sp
                    )
                  }
                }
              }

              Text(
                text = "${contact.relationship} • ${contact.phoneNumber}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
              onClick = {
                val callIntent = Intent(Intent.ACTION_DIAL).apply {
                  data = Uri.parse("tel:${contact.phoneNumber}")
                }
                context.startActivity(callIntent)
              },
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(JeevixSuccess.copy(alpha = 0.15f))
            ) {
              Icon(Icons.Default.Call, contentDescription = "Call", tint = JeevixSuccess, modifier = Modifier.size(20.dp))
            }

            if (!contact.isPrimary) {
              IconButton(onClick = { onDeleteContact(contact.id) }, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(18.dp))
              }
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun EmergencyPassportQrDialog(
  profile: UserProfile?,
  records: List<MedicalRecord>,
  contacts: List<EmergencyContact>,
  onDismiss: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "🚨 Emergency Health QR Code")
      }
    },
    text = {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "Scan with any camera or medical reader to access vital info & emergency contacts securely.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        CanvasEmergencyQrCode(
          dataString = "JEEVIX-PASSPORT:${profile?.jeevixId}:${profile?.bloodGroup}:${profile?.fullName}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "• Blood Group: ${profile?.bloodGroup ?: "O+"}",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "• Primary Contact: ${contacts.firstOrNull()?.name ?: "Mom"} (${contacts.firstOrNull()?.phoneNumber ?: ""})",
              style = MaterialTheme.typography.labelMedium
            )
            Text(
              text = "• Allergies: ${records.filter { it.type == "Allergy" }.joinToString { it.title }.ifBlank { "None reported" }}",
              style = MaterialTheme.typography.labelMedium
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)
      ) {
        Text("Done")
      }
    }
  )
}

@Composable
fun AddMedicalRecordDialog(
  onDismiss: () -> Unit,
  onSave: (type: String, title: String, desc: String, year: String, severity: String, notes: String) -> Unit
) {
  var selectedType by remember { mutableStateOf("Allergy") }
  var title by remember { mutableStateOf("") }
  var desc by remember { mutableStateOf("") }
  var year by remember { mutableStateOf("2026") }
  var severity by remember { mutableStateOf("Mild") }
  var notes by remember { mutableStateOf("") }

  val types = listOf("Allergy", "Medication", "Condition", "Surgery")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add Medical Record") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Record Type", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          types.forEach { t ->
            val isSelected = selectedType == t
            Surface(
              onClick = { selectedType = t },
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = t,
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
              )
            }
          }
        }

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Name / Title (e.g. Penicillin)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
          value = desc,
          onValueChange = { desc = it },
          label = { Text("Description / Details") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Year / Date") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
          )
          OutlinedTextField(
            value = severity,
            onValueChange = { severity = it },
            label = { Text("Dosage / Severity") },
            singleLine = true,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
          )
        }

        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("Doctor's Notes (Optional)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { if (title.isNotBlank()) onSave(selectedType, title, desc, year, severity, notes) },
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)
      ) {
        Text("Save Record")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}

@Composable
fun AddDonationDialog(
  onDismiss: () -> Unit,
  onSave: (date: String, facility: String, units: Int, notes: String) -> Unit
) {
  var facility by remember { mutableStateOf("") }
  var date by remember { mutableStateOf("Today") }
  var units by remember { mutableIntStateOf(1) }
  var notes by remember { mutableStateOf("") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Log Blood Donation ❤️") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = facility,
          onValueChange = { facility = it },
          label = { Text("Facility / Hospital Name") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
        OutlinedTextField(
          value = date,
          onValueChange = { date = it },
          label = { Text("Date of Donation") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
        OutlinedTextField(
          value = "$units",
          onValueChange = { units = it.toIntOrNull() ?: 1 },
          label = { Text("Units Donated") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("Notes / Certificate Number") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { if (facility.isNotBlank()) onSave(date, facility, units, notes) },
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)
      ) {
        Text("Log Donation")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}

@Composable
fun AddContactDialog(
  onDismiss: () -> Unit,
  onSave: (name: String, relation: String, phone: String, isPrimary: Boolean) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var relation by remember { mutableStateOf("") }
  var phone by remember { mutableStateOf("+91 ") }
  var isPrimary by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Add Emergency Contact 🚨") },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Contact Name") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
        OutlinedTextField(
          value = relation,
          onValueChange = { relation = it },
          label = { Text("Relationship (e.g. Sister, Doctor)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("Phone Number") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(10.dp)
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { if (name.isNotBlank() && phone.isNotBlank()) onSave(name, relation, phone, isPrimary) },
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)
      ) {
        Text("Add Contact")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}
