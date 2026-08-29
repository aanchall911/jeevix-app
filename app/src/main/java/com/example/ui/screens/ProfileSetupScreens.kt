package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.viewmodel.JeevixViewModel

@Composable
fun ProfileSetupFlow(
  viewModel: JeevixViewModel,
  onSetupCompleted: () -> Unit,
  modifier: Modifier = Modifier
) {
  var currentStep by remember { mutableIntStateOf(1) }
  val wizardState by viewModel.wizardState.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .navigationBarsPadding()
      .imePadding()
  ) {
    if (currentStep in 1..4) {
      // Progress Header
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 24.dp, vertical = 16.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "HEALTH PASSPORT SETUP",
            style = MaterialTheme.typography.labelSmall,
            color = JeevixCrimsonDark,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
          )
          Text(
            text = "$currentStep / 4",
            style = MaterialTheme.typography.titleMedium,
            color = JeevixCrimsonDark,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
          progress = { currentStep / 4f },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = JeevixCrimson,
          trackColor = JeevixCrimson.copy(alpha = 0.15f)
        )
      }
    }

    when (currentStep) {
      1 -> Step1BasicProfile(
        viewModel = viewModel,
        onNext = { currentStep = 2 }
      )
      2 -> Step2BloodProfile(
        viewModel = viewModel,
        onNext = { currentStep = 3 },
        onBack = { currentStep = 1 }
      )
      3 -> Step3MedicalProfile(
        viewModel = viewModel,
        onNext = { currentStep = 4 },
        onBack = { currentStep = 2 }
      )
      4 -> Step4EmergencyContact(
        viewModel = viewModel,
        onNext = { currentStep = 5 },
        onBack = { currentStep = 3 }
      )
      5 -> LocationPermissionScreen(
        onAllowed = {
          viewModel.finishWizardAndSaveProfile()
          onSetupCompleted()
        },
        onNotNow = {
          viewModel.finishWizardAndSaveProfile()
          onSetupCompleted()
        }
      )
    }
  }
}

@Composable
fun Step1BasicProfile(
  viewModel: JeevixViewModel,
  onNext: () -> Unit
) {
  val wizard = viewModel.wizardState.value
  var name by remember { mutableStateOf(wizard.fullName) }
  var dob by remember { mutableStateOf(wizard.dob) }
  var gender by remember { mutableStateOf(wizard.gender) }
  var height by remember { mutableStateOf(wizard.heightCm) }
  var weight by remember { mutableStateOf(wizard.weightKg) }
  var city by remember { mutableStateOf(wizard.city) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Text(
        text = "Let's build your Health Passport",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Enter your primary personal details for your digital medical ID.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
      )

      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("Full Name") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = dob,
        onValueChange = { dob = it },
        label = { Text("Date of Birth") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Gender selector row
      Text(
        text = "Gender",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 6.dp)
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        listOf("Female", "Male", "Other").forEach { g ->
          val isSelected = gender.equals(g, ignoreCase = true)
          Surface(
            onClick = { gender = g },
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) JeevixCrimson.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, JeevixCrimson) else null,
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = g,
              style = MaterialTheme.typography.labelLarge,
              color = if (isSelected) JeevixCrimsonDark else MaterialTheme.colorScheme.onSurface,
              textAlign = TextAlign.Center,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              modifier = Modifier.padding(vertical = 12.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedTextField(
          value = height,
          onValueChange = { height = it },
          label = { Text("Height") },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(14.dp),
          singleLine = true
        )
        OutlinedTextField(
          value = weight,
          onValueChange = { weight = it },
          label = { Text("Weight") },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(14.dp),
          singleLine = true
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = city,
        onValueChange = { city = it },
        label = { Text("Current City / Location") },
        leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = JeevixCrimson) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )
    }

    Button(
      onClick = {
        viewModel.updateWizard {
          it.copy(
            fullName = name,
            dob = dob,
            gender = gender,
            heightCm = height,
            weightKg = weight,
            city = city
          )
        }
        onNext()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .height(52.dp),
      colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text(text = "Continue →", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
fun Step2BloodProfile(
  viewModel: JeevixViewModel,
  onNext: () -> Unit,
  onBack: () -> Unit
) {
  val wizard = viewModel.wizardState.value
  var selectedBloodGroup by remember { mutableStateOf(wizard.bloodGroup) }
  var lastDonationDate by remember { mutableStateOf(wizard.lastDonationDate) }
  var donationsCount by remember { mutableIntStateOf(wizard.previousDonationsCount) }
  var isDonorNetworkMember by remember { mutableStateOf(wizard.isDonor) }

  val bloodGroups = listOf("A+", "A−", "B+", "B−", "AB+", "AB−", "O+", "O−")

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Text(
        text = "Your Blood Profile 🩸",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Select your blood group and donation history.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
      )

      // Blood Group Card Selector
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Blood Group",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(12.dp))

          // 4x2 Grid of Blood Groups
          Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              bloodGroups.take(4).forEach { bg ->
                val isSelected = selectedBloodGroup == bg || (selectedBloodGroup == "A-" && bg == "A−") || (selectedBloodGroup == "B-" && bg == "B−") || (selectedBloodGroup == "AB-" && bg == "AB−") || (selectedBloodGroup == "O-" && bg == "O−")
                BloodGroupSelectChip(
                  bloodGroup = bg,
                  isSelected = isSelected,
                  onClick = { selectedBloodGroup = bg },
                  modifier = Modifier.weight(1f)
                )
              }
            }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              bloodGroups.drop(4).forEach { bg ->
                val isSelected = selectedBloodGroup == bg || (selectedBloodGroup == "O-" && bg == "O−") || (selectedBloodGroup == "AB-" && bg == "AB−")
                BloodGroupSelectChip(
                  bloodGroup = bg,
                  isSelected = isSelected,
                  onClick = { selectedBloodGroup = bg },
                  modifier = Modifier.weight(1f)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "Additional Information",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = lastDonationDate,
        onValueChange = { lastDonationDate = it },
        label = { Text("Last Blood Donation Date") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true
      )

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Number of previous donations",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = "Total verified units contributed",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          IconButton(
            onClick = { if (donationsCount > 0) donationsCount-- },
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.surfaceVariant)
          ) {
            Text(text = "−", fontSize = 20.sp, fontWeight = FontWeight.Bold)
          }
          Text(
            text = "$donationsCount",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 14.dp)
          )
          IconButton(
            onClick = { donationsCount++ },
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(JeevixCrimson.copy(alpha = 0.15f))
          ) {
            Text(text = "+", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = JeevixCrimson)
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Donor status opt in
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isDonorNetworkMember) JeevixCrimson.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = if (isDonorNetworkMember) androidx.compose.foundation.BorderStroke(1.dp, JeevixCrimson.copy(alpha = 0.3f)) else null
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Donor Status",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Would you like to be part of the Jeevix donor network to help emergency requests nearby?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Surface(
              onClick = { isDonorNetworkMember = true },
              shape = RoundedCornerShape(12.dp),
              color = if (isDonorNetworkMember) JeevixCrimson else MaterialTheme.colorScheme.surface,
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = "Yes, become a donor",
                style = MaterialTheme.typography.labelMedium,
                color = if (isDonorNetworkMember) Color.White else MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp)
              )
            }

            Surface(
              onClick = { isDonorNetworkMember = false },
              shape = RoundedCornerShape(12.dp),
              color = if (!isDonorNetworkMember) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
              modifier = Modifier.weight(1f)
            ) {
              Text(
                text = "Not now",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp)
              )
            }
          }
        }
      }
    }

    Button(
      onClick = {
        viewModel.updateWizard {
          it.copy(
            bloodGroup = selectedBloodGroup.replace("−", "-"),
            lastDonationDate = lastDonationDate,
            previousDonationsCount = donationsCount,
            isDonor = isDonorNetworkMember
          )
        }
        onNext()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .height(52.dp),
      colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text(text = "Save & Continue →", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
fun BloodGroupSelectChip(
  bloodGroup: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    onClick = onClick,
    shape = RoundedCornerShape(14.dp),
    color = if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.surface,
    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
    modifier = modifier.height(48.dp)
  ) {
    Box(contentAlignment = Alignment.Center) {
      Text(
        text = bloodGroup,
        style = MaterialTheme.typography.titleMedium,
        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.ExtraBold
      )
    }
  }
}

@Composable
fun Step3MedicalProfile(
  viewModel: JeevixViewModel,
  onNext: () -> Unit,
  onBack: () -> Unit
) {
  val wizard = viewModel.wizardState.value
  var allergies by remember { mutableStateOf(wizard.allergies) }
  var medications by remember { mutableStateOf(wizard.medications) }
  var conditions by remember { mutableStateOf(wizard.conditions) }
  var surgeries by remember { mutableStateOf(wizard.surgeries) }
  var generalNotes by remember { mutableStateOf(wizard.generalNotes) }

  var showAddDialogType by remember { mutableStateOf<String?>(null) }
  var newItemInput by remember { mutableStateOf("") }

  if (showAddDialogType != null) {
    AlertDialog(
      onDismissRequest = {
        showAddDialogType = null
        newItemInput = ""
      },
      title = { Text(text = "Add ${showAddDialogType}") },
      text = {
        OutlinedTextField(
          value = newItemInput,
          onValueChange = { newItemInput = it },
          label = { Text("Enter ${showAddDialogType}") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp)
        )
      },
      confirmButton = {
        Button(
          onClick = {
            if (newItemInput.isNotBlank()) {
              when (showAddDialogType) {
                "Allergy" -> allergies = allergies + newItemInput.trim()
                "Medication" -> medications = medications + newItemInput.trim()
                "Condition" -> conditions = conditions + newItemInput.trim()
                "Surgery" -> surgeries = surgeries + newItemInput.trim()
              }
            }
            showAddDialogType = null
            newItemInput = ""
          },
          colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)
        ) {
          Text("Add")
        }
      },
      dismissButton = {
        TextButton(onClick = { showAddDialogType = null; newItemInput = "" }) {
          Text("Cancel")
        }
      }
    )
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Text(
        text = "Your Medical Profile",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Add information that could be useful during an emergency.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 18.dp)
      )

      // Allergies Section
      MedicalCategoryBlock(
        title = "Allergies",
        items = allergies,
        onAddClick = { showAddDialogType = "Allergy" },
        onRemoveItem = { allergies = allergies - it }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Current Medications
      MedicalCategoryBlock(
        title = "Current Medications",
        items = medications,
        onAddClick = { showAddDialogType = "Medication" },
        onRemoveItem = { medications = medications - it }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Previous Medical Conditions
      MedicalCategoryBlock(
        title = "Previous Medical Conditions",
        items = conditions,
        onAddClick = { showAddDialogType = "Condition" },
        onRemoveItem = { conditions = conditions - it }
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Previous Surgeries
      MedicalCategoryBlock(
        title = "Previous Surgeries",
        items = surgeries,
        onAddClick = { showAddDialogType = "Surgery" },
        onRemoveItem = { surgeries = surgeries - it }
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Other Important Information",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(6.dp))
      OutlinedTextField(
        value = generalNotes,
        onValueChange = { generalNotes = it },
        placeholder = { Text("Anything important you'd like to record...") },
        modifier = Modifier
          .fillMaxWidth()
          .height(90.dp),
        shape = RoundedCornerShape(14.dp)
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Privacy reassurance banner
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = JeevixTeal.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, JeevixTeal.copy(alpha = 0.2f))
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Security,
            contentDescription = null,
            tint = JeevixTeal,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "You control what you share. You can edit or restrict this information anytime in Settings.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    Button(
      onClick = {
        viewModel.updateWizard {
          it.copy(
            allergies = allergies,
            medications = medications,
            conditions = conditions,
            surgeries = surgeries,
            generalNotes = generalNotes
          )
        }
        onNext()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .height(52.dp),
      colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text(text = "Continue →", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
fun MedicalCategoryBlock(
  title: String,
  items: List<String>,
  onAddClick: () -> Unit,
  onRemoveItem: (String) -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        TextButton(onClick = onAddClick) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp), tint = JeevixCrimson)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add", color = JeevixCrimson, fontWeight = FontWeight.Bold)
          }
        }
      }

      if (items.isEmpty()) {
        Text(
          text = "No records added yet",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      } else {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          items.forEach { item ->
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = JeevixCrimson.copy(alpha = 0.1f),
              border = androidx.compose.foundation.BorderStroke(1.dp, JeevixCrimson.copy(alpha = 0.25f))
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = item,
                  style = MaterialTheme.typography.labelSmall,
                  color = JeevixCrimsonDark,
                  fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.Default.Close,
                  contentDescription = "Remove",
                  tint = JeevixCrimsonDark,
                  modifier = Modifier
                    .size(14.dp)
                    .clickable { onRemoveItem(item) }
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun Step4EmergencyContact(
  viewModel: JeevixViewModel,
  onNext: () -> Unit,
  onBack: () -> Unit
) {
  val wizard = viewModel.wizardState.value
  var primaryName by remember { mutableStateOf(wizard.primaryContactName) }
  var primaryRelation by remember { mutableStateOf(wizard.primaryContactRelation) }
  var primaryPhone by remember { mutableStateOf(wizard.primaryContactPhone) }

  var secondaryName by remember { mutableStateOf(wizard.secondaryContactName) }
  var secondaryRelation by remember { mutableStateOf(wizard.secondaryContactRelation) }
  var secondaryPhone by remember { mutableStateOf(wizard.secondaryContactPhone) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 24.dp, vertical = 8.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Text(
        text = "Your Emergency Network 🚨",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
      )
      Text(
        text = "Add people who should be contacted automatically during an SOS trigger.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
      )

      // Primary Contact Card
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, JeevixEmergencyRed.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(JeevixEmergencyRed)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Primary Emergency Contact",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = JeevixEmergencyRed
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = primaryName,
            onValueChange = { primaryName = it },
            label = { Text("Name (e.g. Mom)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = primaryRelation,
            onValueChange = { primaryRelation = it },
            label = { Text("Relationship (e.g. Mother)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = primaryPhone,
            onValueChange = { primaryPhone = it },
            label = { Text("Phone Number") },
            leadingIcon = { Icon(Icons.Default.Phone, null, tint = JeevixEmergencyRed) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Secondary Contact
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Secondary Contact (Optional)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedTextField(
            value = secondaryName,
            onValueChange = { secondaryName = it },
            label = { Text("Name (e.g. Dad)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedTextField(
            value = secondaryPhone,
            onValueChange = { secondaryPhone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Highlight primary summary
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = JeevixEmergencyRed.copy(alpha = 0.08f),
        border = androidx.compose.foundation.BorderStroke(1.dp, JeevixEmergencyRed.copy(alpha = 0.2f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "❤️", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "$primaryName (${primaryRelation.ifBlank { "Family" }})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7F1D1D)
              )
              Text(
                text = "📞 $primaryPhone",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF991B1B)
              )
            }
          }
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = JeevixEmergencyRed
          ) {
            Text(
              text = "PRIMARY",
              style = MaterialTheme.typography.labelSmall,
              color = Color.White,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    Button(
      onClick = {
        viewModel.updateWizard {
          it.copy(
            primaryContactName = primaryName,
            primaryContactRelation = primaryRelation,
            primaryContactPhone = primaryPhone,
            secondaryContactName = secondaryName,
            secondaryContactRelation = secondaryRelation,
            secondaryContactPhone = secondaryPhone
          )
        }
        onNext()
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp)
        .height(52.dp),
      colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
      shape = RoundedCornerShape(14.dp)
    ) {
      Text(text = "Save & Finish", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
  }
}

@Composable
fun LocationPermissionScreen(
  onAllowed: () -> Unit,
  onNotNow: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(24.dp),
    verticalArrangement = Arrangement.SpaceBetween,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.Center
    ) {
      Box(
        modifier = Modifier
          .size(110.dp)
          .clip(CircleShape)
          .background(JeevixTeal.copy(alpha = 0.15f))
          .border(2.dp, JeevixTeal.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = JeevixTeal,
          modifier = Modifier.size(54.dp)
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "Stay connected to help nearby 📍",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "Allow Jeevix to use your location to show nearby hospitals, verified blood banks, and urgent matching blood requests.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        lineHeight = 22.sp,
        modifier = Modifier.padding(horizontal = 12.dp)
      )
    }

    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Button(
        onClick = onAllowed,
        modifier = Modifier
          .fillMaxWidth()
          .height(52.dp),
        colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson),
        shape = RoundedCornerShape(16.dp)
      ) {
        Text(text = "Allow Location", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
      }

      Spacer(modifier = Modifier.height(8.dp))

      TextButton(
        onClick = onNotNow,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text(
          text = "Not Now",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
