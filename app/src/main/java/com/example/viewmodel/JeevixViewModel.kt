package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.JeevixDatabase
import com.example.data.model.BloodDonation
import com.example.data.model.BloodRequest
import com.example.data.model.ChatMessage
import com.example.data.model.EmergencyContact
import com.example.data.model.MatchedDonor
import com.example.data.model.MedicalRecord
import com.example.data.model.NearbyFacility
import com.example.data.model.NotificationItem
import com.example.data.model.PrivacySettings
import com.example.data.model.UserProfile
import com.example.data.repository.JeevixRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileWizardState(
  val step: Int = 1, // 1: Basic Profile, 2: Blood Profile, 3: Medical History, 4: Emergency Contacts, 5: Location Permission
  val fullName: String = "Aanchal Sharma",
  val email: String = "aanchalbb11@gmail.com",
  val phone: String = "+91 98765 43210",
  val dob: String = "14 Aug 2000",
  val gender: String = "Female",
  val heightCm: String = "165 cm",
  val weightKg: String = "58 kg",
  val city: String = "Bengaluru, Karnataka",
  val bloodGroup: String = "O+",
  val rhFactor: String = "Positive",
  val lastDonationDate: String = "12 May 2026",
  val previousDonationsCount: Int = 4,
  val isDonor: Boolean = true,
  val allergies: List<String> = listOf("Penicillin"),
  val medications: List<String> = listOf("Vitamin D3 60k IU"),
  val conditions: List<String> = listOf("Mild Asthma"),
  val surgeries: List<String> = listOf("Appendectomy (2024)"),
  val generalNotes: String = "No known drug allergies other than Penicillin.",
  val primaryContactName: String = "Mom",
  val primaryContactRelation: String = "Mother",
  val primaryContactPhone: String = "+91 98450 11223",
  val secondaryContactName: String = "Dad",
  val secondaryContactRelation: String = "Father",
  val secondaryContactPhone: String = "+91 98450 44556"
)

class JeevixViewModel(application: Application) : AndroidViewModel(application) {
  private val database = JeevixDatabase.getDatabase(application)
  private val repository = JeevixRepository(database)

  val userProfile: StateFlow<UserProfile?> = repository.userProfileFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val medicalRecords: StateFlow<List<MedicalRecord>> = repository.medicalRecordsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val emergencyContacts: StateFlow<List<EmergencyContact>> = repository.emergencyContactsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val bloodDonations: StateFlow<List<BloodDonation>> = repository.bloodDonationsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val bloodRequests: StateFlow<List<BloodRequest>> = repository.bloodRequestsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val notifications: StateFlow<List<NotificationItem>> = repository.notificationsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val privacySettings: StateFlow<PrivacySettings> = repository.privacySettingsFlow
  val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessagesFlow

  // Nearby facility search & filter state
  private val _nearbySearchQuery = MutableStateFlow("")
  val nearbySearchQuery = _nearbySearchQuery.asStateFlow()

  private val _nearbyFilterTab = MutableStateFlow("All") // All, Hospitals, Blood Banks
  val nearbyFilterTab = _nearbyFilterTab.asStateFlow()

  // Blood Request filter state
  private val _bloodRequestsFilter = MutableStateFlow("Nearby") // Nearby, Compatible, Emergency
  val bloodRequestsFilter = _bloodRequestsFilter.asStateFlow()

  // Notifications tab filter
  private val _notificationTab = MutableStateFlow("All") // All, Health, Blood, Emergency
  val notificationTab = _notificationTab.asStateFlow()

  // SOS Emergency Mode state
  private val _isEmergencyModeActive = MutableStateFlow(false)
  val isEmergencyModeActive = _isEmergencyModeActive.asStateFlow()

  private val _showSosConfirmDialog = MutableStateFlow(false)
  val showSosConfirmDialog = _showSosConfirmDialog.asStateFlow()

  private val _showEmergencyQrDialog = MutableStateFlow(false)
  val showEmergencyQrDialog = _showEmergencyQrDialog.asStateFlow()

  // Profile Wizard Draft state
  private val _wizardState = MutableStateFlow(ProfileWizardState())
  val wizardState = _wizardState.asStateFlow()

  // Selected Smart Matching Request
  private val _smartMatchRequest = MutableStateFlow<BloodRequest?>(null)
  val smartMatchRequest = _smartMatchRequest.asStateFlow()

  // Selected Facility Detail
  private val _selectedFacility = MutableStateFlow<NearbyFacility?>(null)
  val selectedFacility = _selectedFacility.asStateFlow()

  // UI Dialog States
  private val _showAddRecordDialog = MutableStateFlow(false)
  val showAddRecordDialog = _showAddRecordDialog.asStateFlow()

  private val _showAddDonationDialog = MutableStateFlow(false)
  val showAddDonationDialog = _showAddDonationDialog.asStateFlow()

  private val _showAddContactDialog = MutableStateFlow(false)
  val showAddContactDialog = _showAddContactDialog.asStateFlow()

  private val _showCreateRequestDialog = MutableStateFlow(false)
  val showCreateRequestDialog = _showCreateRequestDialog.asStateFlow()

  private val _showVitalsUpdateDialog = MutableStateFlow(false)
  val showVitalsUpdateDialog = _showVitalsUpdateDialog.asStateFlow()

  // New Feature Dialog States
  private val _showAiAnalysisDialog = MutableStateFlow(false)
  val showAiAnalysisDialog = _showAiAnalysisDialog.asStateFlow()

  private val _showWearableDialog = MutableStateFlow(false)
  val showWearableDialog = _showWearableDialog.asStateFlow()

  private val _showEmailVerifyDialog = MutableStateFlow(false)
  val showEmailVerifyDialog = _showEmailVerifyDialog.asStateFlow()

  private val _showIdVerifyDialog = MutableStateFlow(false)
  val showIdVerifyDialog = _showIdVerifyDialog.asStateFlow()

  private val _showLanguageDialog = MutableStateFlow(false)
  val showLanguageDialog = _showLanguageDialog.asStateFlow()

  private val _leaderboardCity = MutableStateFlow("Bengaluru")
  val leaderboardCity = _leaderboardCity.asStateFlow()

  init {
    viewModelScope.launch {
      repository.initializeSeedData()
    }
  }

  fun setLeaderboardCity(city: String) {
    _leaderboardCity.value = city
  }

  fun showAiAnalysisDialog(show: Boolean) {
    _showAiAnalysisDialog.value = show
  }

  fun showWearableDialog(show: Boolean) {
    _showWearableDialog.value = show
  }

  fun showEmailVerifyDialog(show: Boolean) {
    _showEmailVerifyDialog.value = show
  }

  fun showIdVerifyDialog(show: Boolean) {
    _showIdVerifyDialog.value = show
  }

  fun showLanguageDialog(show: Boolean) {
    _showLanguageDialog.value = show
  }

  fun setLanguage(langCode: String) {
    viewModelScope.launch {
      repository.setLanguage(langCode)
    }
  }

  fun syncWearableDevice(deviceName: String = "Fitbit Charge 6") {
    viewModelScope.launch {
      repository.syncWearableDevice(deviceName)
    }
  }

  fun verifyEmail(email: String) {
    viewModelScope.launch {
      repository.verifyEmail(email)
      _showEmailVerifyDialog.value = false
    }
  }

  fun verifyGovtId(idType: String, idNumber: String) {
    viewModelScope.launch {
      repository.verifyGovtId(idType, idNumber)
      _showIdVerifyDialog.value = false
    }
  }

  fun getAiHealthReport(): com.example.data.model.AiHealthInsightReport {
    val profile = userProfile.value
    val records = medicalRecords.value
    val donations = bloodDonations.value
    val lang = profile?.selectedLanguage ?: "en"
    return com.example.data.remote.AiHealthAnalyzer.analyzeHealthPassport(
      profile = profile,
      medicalRecords = records,
      donations = donations,
      languageCode = lang
    )
  }

  fun getDonorRewards(): com.example.data.model.DonorRewards {
    val count = bloodDonations.value.size.takeIf { it > 0 } ?: (userProfile.value?.totalDonations ?: 4)
    return com.example.data.repository.GamificationAndWearableManager.calculateDonorRewards(
      userProfile = userProfile.value,
      totalDonationsOverride = count
    )
  }

  fun getAllBadges(): List<com.example.data.model.DonorBadge> {
    val unlocked = userProfile.value?.unlockedBadges ?: "FIRST_DONOR,TRI_HERO,LIFESAVER_PRO"
    return com.example.data.repository.GamificationAndWearableManager.getAllBadges(unlocked)
  }

  fun getRegionalLeaderboard(): List<com.example.data.model.LeaderboardDonor> {
    return com.example.data.repository.GamificationAndWearableManager.getRegionalLeaderboard(
      userProfile = userProfile.value,
      selectedCity = _leaderboardCity.value
    )
  }

  fun getSupportedWearables(): List<com.example.data.model.WearableDevice> {
    return com.example.data.repository.GamificationAndWearableManager.getSupportedWearables(
      connectedDeviceName = userProfile.value?.connectedWearable
    )
  }

  fun setNearbySearchQuery(query: String) {
    _nearbySearchQuery.value = query
  }

  fun setNearbyFilterTab(tab: String) {
    _nearbyFilterTab.value = tab
  }

  fun setBloodRequestsFilter(filter: String) {
    _bloodRequestsFilter.value = filter
  }

  fun setNotificationTab(tab: String) {
    _notificationTab.value = tab
  }

  fun triggerSosConfirmation(show: Boolean) {
    _showSosConfirmDialog.value = show
  }

  fun activateEmergencyMode(active: Boolean) {
    _isEmergencyModeActive.value = active
    _showSosConfirmDialog.value = false
  }

  fun showEmergencyQr(show: Boolean) {
    _showEmergencyQrDialog.value = show
  }

  fun openSmartMatching(request: BloodRequest?) {
    _smartMatchRequest.value = request
  }

  fun setSelectedFacility(facility: NearbyFacility?) {
    _selectedFacility.value = facility
  }

  fun showAddRecordDialog(show: Boolean) {
    _showAddRecordDialog.value = show
  }

  fun showAddDonationDialog(show: Boolean) {
    _showAddDonationDialog.value = show
  }

  fun showAddContactDialog(show: Boolean) {
    _showAddContactDialog.value = show
  }

  fun showCreateRequestDialog(show: Boolean) {
    _showCreateRequestDialog.value = show
  }

  fun showVitalsUpdateDialog(show: Boolean) {
    _showVitalsUpdateDialog.value = show
  }

  fun getFilteredNearbyFacilities(): List<NearbyFacility> {
    val all = repository.getNearbyFacilities()
    val query = _nearbySearchQuery.value.trim().lowercase()
    val tab = _nearbyFilterTab.value

    return all.filter { facility ->
      val matchesTab = when (tab) {
        "Hospitals" -> facility.type == "Hospital"
        "Blood Banks" -> facility.type == "BloodBank"
        else -> true
      }
      val matchesQuery = query.isEmpty() ||
        facility.name.lowercase().contains(query) ||
        facility.address.lowercase().contains(query) ||
        facility.availableBloodGroups.any { it.lowercase().contains(query) }
      matchesTab && matchesQuery
    }
  }

  fun getMatchedDonorsForRequest(bloodGroup: String): List<MatchedDonor> {
    return repository.getMatchedDonors(bloodGroup)
  }

  // Wizard actions
  fun updateWizard(update: (ProfileWizardState) -> ProfileWizardState) {
    _wizardState.value = update(_wizardState.value)
  }

  fun finishWizardAndSaveProfile() {
    viewModelScope.launch {
      val w = _wizardState.value
      val newProfile = UserProfile(
        id = 1,
        fullName = w.fullName,
        email = w.email,
        phoneNumber = w.phone,
        dateOfBirth = w.dob,
        gender = w.gender,
        heightCm = w.heightCm,
        weightKg = w.weightKg,
        cityLocation = w.city,
        bloodGroup = w.bloodGroup,
        rhFactor = w.rhFactor,
        isDonor = w.isDonor,
        donorStatus = if (w.isDonor) "Available" else "Inactive",
        lastDonationDate = w.lastDonationDate,
        totalDonations = w.previousDonationsCount,
        jeevixId = "JVX-${(1000..9999).random()}-${w.bloodGroup}",
        isOnboarded = true,
        isProfileComplete = true,
        isLocationAllowed = true
      )
      repository.updateProfile(newProfile)

      // Add emergency contacts from wizard
      if (w.primaryContactName.isNotBlank()) {
        repository.addEmergencyContact(
          EmergencyContact(
            name = w.primaryContactName,
            relationship = w.primaryContactRelation,
            phoneNumber = w.primaryContactPhone,
            isPrimary = true,
            avatarColorHex = 0xFFE11D48
          )
        )
      }
      if (w.secondaryContactName.isNotBlank()) {
        repository.addEmergencyContact(
          EmergencyContact(
            name = w.secondaryContactName,
            relationship = w.secondaryContactRelation,
            phoneNumber = w.secondaryContactPhone,
            isPrimary = false,
            avatarColorHex = 0xFF0D9488
          )
        )
      }
    }
  }

  // Record CRUD
  fun addMedicalRecord(type: String, title: String, description: String, year: String, severity: String, notes: String) {
    viewModelScope.launch {
      repository.addMedicalRecord(
        MedicalRecord(
          type = type,
          title = title,
          description = description,
          yearOrDate = year,
          severityOrDosage = severity,
          notes = notes
        )
      )
    }
  }

  fun deleteMedicalRecord(id: Long) {
    viewModelScope.launch {
      repository.deleteMedicalRecord(id)
    }
  }

  // Emergency Contact CRUD
  fun addEmergencyContact(name: String, relation: String, phone: String, isPrimary: Boolean) {
    viewModelScope.launch {
      repository.addEmergencyContact(
        EmergencyContact(
          name = name,
          relationship = relation,
          phoneNumber = phone,
          isPrimary = isPrimary
        )
      )
    }
  }

  fun deleteEmergencyContact(id: Long) {
    viewModelScope.launch {
      repository.deleteEmergencyContact(id)
    }
  }

  // Donation CRUD
  fun addDonation(date: String, facility: String, units: Int, notes: String) {
    viewModelScope.launch {
      repository.addBloodDonation(
        BloodDonation(
          date = date,
          facilityName = facility,
          unitsGiven = units,
          notes = notes
        )
      )
    }
  }

  // Request CRUD
  fun createBloodRequest(
    patient: String,
    bloodGroup: String,
    units: Int,
    hospital: String,
    location: String,
    urgency: String,
    date: String,
    contactPerson: String,
    contactPhone: String,
    notes: String
  ) {
    viewModelScope.launch {
      repository.createBloodRequest(
        BloodRequest(
          patientName = patient,
          bloodGroup = bloodGroup,
          unitsNeeded = units,
          hospitalName = hospital,
          location = location,
          distanceKm = 2.5,
          urgency = urgency,
          requiredDate = date,
          contactPerson = contactPerson,
          contactNumber = contactPhone,
          notes = notes,
          isUserCreated = true
        )
      )
    }
  }

  fun fulfillBloodRequest(id: Long) {
    viewModelScope.launch {
      repository.fulfillBloodRequest(id)
    }
  }

  // Vitals Update
  fun updateVitals(hr: Int, systolic: Int, diastolic: Int, spo2: Int) {
    viewModelScope.launch {
      repository.updateVitals(hr, systolic, diastolic, spo2)
    }
  }

  // Donor status toggle
  fun updateDonorStatus(isDonor: Boolean, status: String) {
    viewModelScope.launch {
      repository.updateDonorStatus(isDonor, status)
    }
  }

  // Notification actions
  fun dismissNotification(id: Long) {
    viewModelScope.launch {
      repository.markNotificationAsRead(id)
    }
  }

  fun markNotificationRead(id: Long) {
    viewModelScope.launch {
      repository.markNotificationAsRead(id)
    }
  }

  fun clearAllNotifications() {
    viewModelScope.launch {
      repository.clearAllNotifications()
    }
  }

  // Privacy Settings update
  fun updatePrivacySettings(settings: PrivacySettings) {
    repository.updatePrivacySettings(settings)
  }

  // AI Chat
  fun sendChatMessage(text: String) {
    viewModelScope.launch {
      repository.sendChatMessage(text)
    }
  }
}
