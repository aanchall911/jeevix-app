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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NearbyFacility
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.viewmodel.JeevixViewModel

@Composable
fun NearbyHealthNetworkScreen(
  viewModel: JeevixViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val searchQuery by viewModel.nearbySearchQuery.collectAsState()
  val filterTab by viewModel.nearbyFilterTab.collectAsState()
  val selectedFacility by viewModel.selectedFacility.collectAsState()

  val facilities = viewModel.getFilteredNearbyFacilities()
  val filterTabs = listOf("All", "Hospitals", "Blood Banks")

  if (selectedFacility != null) {
    FacilityDetailDialog(
      facility = selectedFacility!!,
      onDismiss = { viewModel.setSelectedFacility(null) }
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Header & Search
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      Text(
        text = "Nearby Health Network 🏥",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Verified hospitals, blood banks & 24/7 trauma centers",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Search field
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.setNearbySearchQuery(it) },
        placeholder = { Text("Search by name, locality, or blood group...") },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = JeevixCrimson) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { viewModel.setNearbySearchQuery("") }) {
              Icon(Icons.Default.Clear, contentDescription = "Clear")
            }
          }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = JeevixCrimson
        ),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Filter chips
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        filterTabs.forEach { tab ->
          val isSelected = filterTab == tab
          Surface(
            onClick = { viewModel.setNearbyFilterTab(tab) },
            shape = RoundedCornerShape(10.dp),
            color = if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = tab,
              style = MaterialTheme.typography.labelMedium,
              color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              modifier = Modifier.padding(vertical = 8.dp),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }
    }

    // Facility List
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(facilities, key = { it.id }) { facility ->
        NearbyFacilityCard(
          facility = facility,
          onCardClick = { viewModel.setSelectedFacility(facility) },
          onCallClick = {
            val callIntent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${facility.phone}") }
            context.startActivity(callIntent)
          },
          onDirectionsClick = {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(facility.name + " " + facility.address)}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            context.startActivity(mapIntent)
          }
        )
      }

      item { Spacer(modifier = Modifier.height(60.dp)) }
    }
  }
}

@Composable
fun NearbyFacilityCard(
  facility: NearbyFacility,
  onCardClick: () -> Unit,
  onCallClick: () -> Unit,
  onDirectionsClick: () -> Unit
) {
  val isHospital = facility.type == "Hospital"
  val accentColor = if (isHospital) JeevixTeal else JeevixCrimson

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onCardClick() }
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
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
              .size(44.dp)
              .clip(CircleShape)
              .background(accentColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isHospital) Icons.Default.LocalHospital else Icons.Default.WaterDrop,
              contentDescription = null,
              tint = accentColor,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = facility.name,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${facility.distanceKm} km away",
                style = MaterialTheme.typography.bodySmall,
                color = JeevixCrimsonDark,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = " • 24/7 Emergency",
                style = MaterialTheme.typography.bodySmall,
                color = JeevixSuccess,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = facility.address,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      if (facility.availableBloodGroups.isNotEmpty()) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "Blood Stocks Available:",
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          facility.availableBloodGroups.take(6).forEach { bg ->
            val stock = facility.bloodStockLevels[bg] ?: "Available"
            val isLow = stock.contains("Low", ignoreCase = true)
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = if (isLow) JeevixEmergencyRed.copy(alpha = 0.12f) else JeevixCrimson.copy(alpha = 0.08f),
              border = androidx.compose.foundation.BorderStroke(1.dp, if (isLow) JeevixEmergencyRed.copy(alpha = 0.3f) else JeevixCrimson.copy(alpha = 0.2f))
            ) {
              Text(
                text = bg,
                style = MaterialTheme.typography.labelSmall,
                color = if (isLow) JeevixEmergencyRed else JeevixCrimsonDark,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontSize = 11.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Bottom buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Button(
          onClick = onCallClick,
          colors = ButtonDefaults.buttonColors(containerColor = JeevixSuccess),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Call, null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Call Now", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = onDirectionsClick,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.weight(1f)
        ) {
          Icon(Icons.Default.Directions, null, modifier = Modifier.size(16.dp), tint = JeevixCrimson)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Directions", color = JeevixCrimson, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun FacilityDetailDialog(
  facility: NearbyFacility,
  onDismiss: () -> Unit
) {
  val context = LocalContext.current

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = facility.name)
      }
    },
    text = {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "📍 ${facility.address}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "📞 Hotline: ${facility.phone}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)

        if (facility.emergencyServices.isNotEmpty()) {
          Text(text = "Emergency Services:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
          facility.emergencyServices.forEach { service ->
            Text(text = "• $service", style = MaterialTheme.typography.bodySmall)
          }
        }

        if (facility.bloodStockLevels.isNotEmpty()) {
          Text(text = "Blood Inventory Status:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
          facility.bloodStockLevels.forEach { (bg, level) ->
            Text(text = "• $bg: $level", style = MaterialTheme.typography.bodySmall)
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val callIntent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:${facility.phone}") }
          context.startActivity(callIntent)
        },
        colors = ButtonDefaults.buttonColors(containerColor = JeevixSuccess)
      ) {
        Text("Call Facility")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text("Close") }
    }
  )
}
