package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.model.BloodRequest
import com.example.data.model.MatchedDonor
import com.example.data.model.UserProfile
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.viewmodel.JeevixViewModel

@Composable
fun DonateAndBloodRequestsScreen(
  viewModel: JeevixViewModel,
  modifier: Modifier = Modifier
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val bloodRequests by viewModel.bloodRequests.collectAsState()
  val donations by viewModel.bloodDonations.collectAsState()
  val smartMatchRequest by viewModel.smartMatchRequest.collectAsState()
  val showCreateDialog by viewModel.showCreateRequestDialog.collectAsState()
  val showAddDonationDialog by viewModel.showAddDonationDialog.collectAsState()

  var selectedTab by remember { mutableIntStateOf(1) } // 0: Donor Profile, 1: Blood Requests, 2: Donation History
  val tabs = listOf("Donor Profile", "Blood Requests", "Donation History")

  // Smart Matching Dialog
  if (smartMatchRequest != null) {
    SmartMatchingDonorsDialog(
      request = smartMatchRequest!!,
      donors = viewModel.getMatchedDonorsForRequest(smartMatchRequest!!.bloodGroup),
      onDismiss = { viewModel.openSmartMatching(null) }
    )
  }

  // Create Blood Request Dialog
  if (showCreateDialog) {
    CreateBloodRequestDialog(
      onDismiss = { viewModel.showCreateRequestDialog(false) },
      onCreate = { patient, bg, units, hosp, loc, urgency, date, contact, phone, notes ->
        viewModel.createBloodRequest(patient, bg, units, hosp, loc, urgency, date, contact, phone, notes)
        viewModel.showCreateRequestDialog(false)
      }
    )
  }

  // Add Donation Dialog
  if (showAddDonationDialog) {
    AddDonationDialog(
      onDismiss = { viewModel.showAddDonationDialog(false) },
      onSave = { date, facility, units, notes ->
        viewModel.addDonation(date, facility, units, notes)
        viewModel.showAddDonationDialog(false)
      }
    )
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Blood Network & Requests",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Real-time donor matching & urgent requirements",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = { viewModel.showCreateRequestDialog(true) },
          colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Request Blood", fontWeight = FontWeight.Bold)
        }
      }

      // Scrollable Tabs
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

      when (selectedTab) {
        0 -> DonorProfileView(
          profile = userProfile,
          onUpdateStatus = { isDonor, status -> viewModel.updateDonorStatus(isDonor, status) }
        )
        1 -> BloodRequestsView(
          requests = bloodRequests,
          onSmartMatch = { req -> viewModel.openSmartMatching(req) },
          onFulfill = { id -> viewModel.fulfillBloodRequest(id) }
        )
        2 -> DonationHistoryTab(
          donations = donations,
          onAddClick = { viewModel.showAddDonationDialog(true) }
        )
      }
    }
  }
}

@Composable
fun BloodRequestsView(
  requests: List<BloodRequest>,
  onSmartMatch: (BloodRequest) -> Unit,
  onFulfill: (Long) -> Unit
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
        Text(
          text = "Active Verified Requests Nearby",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onBackground
        )

        Surface(
          shape = RoundedCornerShape(8.dp),
          color = JeevixEmergencyRed.copy(alpha = 0.12f)
        ) {
          Text(
            text = "${requests.count { !it.isFulfilled }} Needed",
            style = MaterialTheme.typography.labelSmall,
            color = JeevixEmergencyRed,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }
    }

    items(requests, key = { it.id }) { req ->
      val isEmergency = req.urgency == "Emergency"
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (req.isFulfilled) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (isEmergency && !req.isFulfilled) JeevixEmergencyRed.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Top row: Blood group badge & Urgency tag
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .background(if (isEmergency) JeevixEmergencyRed else JeevixCrimson),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = req.bloodGroup,
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column {
                Text(
                  text = "${req.unitsNeeded} Unit(s) Required",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Patient: ${req.patientName}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (req.isFulfilled) JeevixSuccess.copy(alpha = 0.15f) else if (isEmergency) JeevixEmergencyRed.copy(alpha = 0.15f) else Color(0xFFFEF3C7)
            ) {
              Text(
                text = if (req.isFulfilled) "FULFILLED ✓" else req.urgency.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = if (req.isFulfilled) JeevixSuccess else if (isEmergency) JeevixEmergencyRed else Color(0xFFD97706),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontSize = 10.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Hospital & Location
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp), tint = JeevixCrimson)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${req.hospitalName} • ${req.location} (${req.distanceKm} km)",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          if (req.notes.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Note: ${req.notes}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.padding(start = 22.dp)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Action buttons: Smart Match & Direct Contact
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Button(
              onClick = { onSmartMatch(req) },
              colors = ButtonDefaults.buttonColors(
                containerColor = JeevixAiViolet.copy(alpha = 0.12f),
                contentColor = JeevixAiViolet
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.weight(1f),
              border = androidx.compose.foundation.BorderStroke(1.dp, JeevixAiViolet.copy(alpha = 0.3f))
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "✨", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Smart Match Donors", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
              }
            }

            IconButton(
              onClick = {
                val callIntent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${req.contactNumber}") }
                context.startActivity(callIntent)
              },
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(JeevixSuccess.copy(alpha = 0.15f))
            ) {
              Icon(Icons.Default.Call, contentDescription = "Call", tint = JeevixSuccess, modifier = Modifier.size(20.dp))
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun DonorProfileView(
  profile: UserProfile?,
  onUpdateStatus: (Boolean, String) -> Unit
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
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = profile?.fullName ?: "Aanchal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Verified Jeevix Donor ID: ${profile?.jeevixId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Box(
              modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(JeevixCrimson.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = bloodGroup,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = JeevixCrimson
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Total Donations", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "${profile?.totalDonations ?: 4} Times", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Column {
              Text(text = "Last Donated", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = profile?.lastDonationDate ?: "12 May 2026", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Column {
              Text(text = "Lives Saved", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "12+ Lives ❤️", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = JeevixCrimson)
            }
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
          Text(
            text = "Control whether nearby hospitals or patients can request emergency blood from you.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

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

    item {
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = JeevixTeal.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, JeevixTeal.copy(alpha = 0.2f))
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Security, null, tint = JeevixTeal, modifier = Modifier.size(22.dp))
          Spacer(modifier = Modifier.width(12.dp))
          Text(
            text = "🔒 Privacy Protection: Requesters will only see your first name, blood group, and approximate locality. Your medical passport and address remain completely private.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun SmartMatchingDonorsDialog(
  request: BloodRequest,
  donors: List<MatchedDonor>,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "✨ Matched Donors for ${request.bloodGroup}")
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "Jeevix AI matched verified donors within 10 km compatible with ${request.bloodGroup}.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        donors.forEach { donor ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(JeevixCrimson.copy(alpha = 0.15f)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = donor.bloodGroup, fontWeight = FontWeight.Bold, color = JeevixCrimson, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                  Text(text = donor.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                  Text(
                    text = "${donor.distanceKm} km away • ${donor.totalDonations} donations",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              IconButton(
                onClick = {
                  val callIntent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${donor.phone}") }
                  context.startActivity(callIntent)
                },
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(JeevixSuccess.copy(alpha = 0.15f))
              ) {
                Icon(Icons.Default.Call, contentDescription = "Call", tint = JeevixSuccess, modifier = Modifier.size(18.dp))
              }
            }
          }
        }
      }
    },
    confirmButton = {
      Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)) {
        Text("Done")
      }
    }
  )
}

@Composable
fun CreateBloodRequestDialog(
  onDismiss: () -> Unit,
  onCreate: (patient: String, bg: String, units: Int, hosp: String, loc: String, urgency: String, date: String, contact: String, phone: String, notes: String) -> Unit
) {
  var patient by remember { mutableStateOf("") }
  var bloodGroup by remember { mutableStateOf("O+") }
  var units by remember { mutableIntStateOf(2) }
  var hospital by remember { mutableStateOf("") }
  var location by remember { mutableStateOf("Bengaluru") }
  var urgency by remember { mutableStateOf("Emergency") }
  var date by remember { mutableStateOf("Today") }
  var contact by remember { mutableStateOf("Attending Doctor") }
  var phone by remember { mutableStateOf("+91 ") }
  var notes by remember { mutableStateOf("") }

  val bgs = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
  val urgencies = listOf("Emergency", "Urgent", "Standard")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("Create Emergency Blood Request 🩸") },
    text = {
      LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        item {
          OutlinedTextField(
            value = patient,
            onValueChange = { patient = it },
            label = { Text("Patient Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )
        }

        item {
          Text("Required Blood Group", style = MaterialTheme.typography.labelSmall)
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            bgs.take(4).forEach { bg ->
              val isSelected = bloodGroup == bg
              Surface(
                onClick = { bloodGroup = bg },
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  text = bg,
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                  textAlign = TextAlign.Center,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(vertical = 6.dp)
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            bgs.drop(4).forEach { bg ->
              val isSelected = bloodGroup == bg
              Surface(
                onClick = { bloodGroup = bg },
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  text = bg,
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                  textAlign = TextAlign.Center,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(vertical = 6.dp)
                )
              }
            }
          }
        }

        item {
          OutlinedTextField(
            value = hospital,
            onValueChange = { hospital = it },
            label = { Text("Hospital Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )
        }

        item {
          OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("City / Area") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )
        }

        item {
          Text("Urgency Level", style = MaterialTheme.typography.labelSmall)
          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            urgencies.forEach { u ->
              val isSelected = urgency == u
              Surface(
                onClick = { urgency = u },
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) JeevixEmergencyRed else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  text = u,
                  style = MaterialTheme.typography.labelSmall,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                  textAlign = TextAlign.Center,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(vertical = 6.dp)
                )
              }
            }
          }
        }

        item {
          OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Contact Phone") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )
        }

        item {
          OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Medical condition / ICU notes") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (patient.isNotBlank() && hospital.isNotBlank()) {
            onCreate(patient, bloodGroup, units, hospital, location, urgency, date, contact, phone, notes)
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)
      ) {
        Text("Broadcast Request")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Cancel") }
    }
  )
}
