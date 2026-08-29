package com.example.data.repository

import com.example.data.local.JeevixDatabase
import com.example.data.model.BloodDonation
import com.example.data.model.BloodRequest
import com.example.data.model.ChatMessage
import com.example.data.model.EmergencyContact
import com.example.data.model.MatchedDonor
import com.example.data.model.MedicalRecord
import com.example.data.model.MessageSender
import com.example.data.model.NearbyFacility
import com.example.data.model.NotificationItem
import com.example.data.model.PrivacySettings
import com.example.data.model.UserProfile
import com.example.data.remote.GeminiHealthAssistant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

class JeevixRepository(
  private val database: JeevixDatabase,
  private val geminiAssistant: GeminiHealthAssistant = GeminiHealthAssistant()
) {
  private val userProfileDao = database.userProfileDao()
  private val medicalRecordDao = database.medicalRecordDao()
  private val emergencyContactDao = database.emergencyContactDao()
  private val bloodDonationDao = database.bloodDonationDao()
  private val bloodRequestDao = database.bloodRequestDao()
  private val notificationDao = database.notificationDao()

  val userProfileFlow: Flow<UserProfile?> = userProfileDao.getUserProfile()
  val medicalRecordsFlow: Flow<List<MedicalRecord>> = medicalRecordDao.getAllRecords()
  val emergencyContactsFlow: Flow<List<EmergencyContact>> = emergencyContactDao.getAllContacts()
  val bloodDonationsFlow: Flow<List<BloodDonation>> = bloodDonationDao.getAllDonations()
  val bloodRequestsFlow: Flow<List<BloodRequest>> = bloodRequestDao.getAllRequests()
  val notificationsFlow: Flow<List<NotificationItem>> = notificationDao.getAllNotifications()

  private val _privacySettings = MutableStateFlow(PrivacySettings())
  val privacySettingsFlow = _privacySettings.asStateFlow()

  private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
    listOf(
      ChatMessage(
        sender = MessageSender.JEEVIX_AI,
        text = "👋 Hello! I am your Jeevix AI Assistant.\n\nI can help you review your Health Passport, track blood donation intervals, explain your medical records, or find emergency resources. Ask me anything!"
      )
    )
  )
  val chatMessagesFlow = _chatMessages.asStateFlow()

  // Seed default data if database is empty on first launch
  suspend fun initializeSeedData() {
    val existingProfile = userProfileDao.getUserProfileOnce()
    if (existingProfile == null) {
      val defaultProfile = UserProfile(
        id = 1,
        fullName = "Aanchal",
        email = "aanchalbb11@gmail.com",
        phoneNumber = "+91 98765 43210",
        dateOfBirth = "14 Aug 2000",
        gender = "Female",
        heightCm = "165 cm",
        weightKg = "58 kg",
        cityLocation = "Bengaluru, Karnataka",
        bloodGroup = "O+",
        rhFactor = "Positive",
        isDonor = true,
        donorStatus = "Available",
        lastDonationDate = "12 May 2026",
        totalDonations = 4,
        jeevixId = "JVX-8942-O+",
        heartRate = 72,
        systolicBp = 118,
        diastolicBp = 76,
        spo2 = 98,
        isOnboarded = true,
        isProfileComplete = true,
        isLocationAllowed = true
      )
      userProfileDao.insertOrUpdateProfile(defaultProfile)

      // Seed Medical Records
      medicalRecordDao.insertRecord(
        MedicalRecord(
          type = "Allergy",
          title = "Penicillin",
          description = "Mild skin rash and hives on ingestion",
          yearOrDate = "2026",
          severityOrDosage = "Moderate",
          notes = "Avoid all penicillin-family antibiotics"
        )
      )
      medicalRecordDao.insertRecord(
        MedicalRecord(
          type = "Medication",
          title = "Vitamin D3 60k IU",
          description = "Weekly supplement prescribed for bone health",
          yearOrDate = "2026",
          severityOrDosage = "1 cap/week",
          notes = "Take after morning meal"
        )
      )
      medicalRecordDao.insertRecord(
        MedicalRecord(
          type = "Condition",
          title = "Mild Asthma",
          description = "Seasonal exercise-induced wheezing",
          yearOrDate = "2025",
          severityOrDosage = "Mild",
          notes = "Inhaler available in emergency kit"
        )
      )
      medicalRecordDao.insertRecord(
        MedicalRecord(
          type = "Surgery",
          title = "Appendectomy",
          description = "Laparoscopic appendectomy at Apollo Hospital",
          yearOrDate = "2024",
          severityOrDosage = "Full recovery",
          notes = "No post-op complications"
        )
      )

      // Seed Emergency Contacts
      emergencyContactDao.insertContact(
        EmergencyContact(
          name = "Mom",
          relationship = "Mother",
          phoneNumber = "+91 98450 11223",
          isPrimary = true,
          avatarColorHex = 0xFFE11D48
        )
      )
      emergencyContactDao.insertContact(
        EmergencyContact(
          name = "Dad",
          relationship = "Father",
          phoneNumber = "+91 98450 44556",
          isPrimary = false,
          avatarColorHex = 0xFF0D9488
        )
      )

      // Seed Blood Donation History
      bloodDonationDao.insertDonation(
        BloodDonation(
          date = "12 May 2026",
          facilityName = "Manipal Hospital Blood Center",
          unitsGiven = 1,
          bloodGroup = "O+",
          certificateNumber = "CERT-JVX-2026-084",
          notes = "Whole blood donation, Hb level 13.8 g/dL"
        )
      )
      bloodDonationDao.insertDonation(
        BloodDonation(
          date = "04 Jan 2026",
          facilityName = "Red Cross Blood Bank Central",
          unitsGiven = 1,
          bloodGroup = "O+",
          certificateNumber = "CERT-JVX-2026-012",
          notes = "Routine community blood drive"
        )
      )
      bloodDonationDao.insertDonation(
        BloodDonation(
          date = "18 Aug 2025",
          facilityName = "Apollo Speciality Blood Center",
          unitsGiven = 1,
          bloodGroup = "O+",
          certificateNumber = "CERT-JVX-2025-241",
          notes = "Emergency response call donation"
        )
      )
      bloodDonationDao.insertDonation(
        BloodDonation(
          date = "10 Mar 2025",
          facilityName = "Rotary TTK Blood Center",
          unitsGiven = 1,
          bloodGroup = "O+",
          certificateNumber = "CERT-JVX-2025-067",
          notes = "World Blood Donor Day campaign"
        )
      )

      // Seed Blood Requests
      bloodRequestDao.insertRequest(
        BloodRequest(
          patientName = "Suresh Kumar",
          bloodGroup = "O+",
          unitsNeeded = 2,
          hospitalName = "XYZ Memorial Hospital",
          location = "Indiranagar, Bengaluru",
          distanceKm = 3.2,
          urgency = "Emergency",
          requiredDate = "Today",
          contactPerson = "Dr. Ramesh (Attending)",
          contactNumber = "+91 91234 56780",
          notes = "Emergency trauma surgery scheduled in ICU-3",
          isFulfilled = false,
          matchedDonorsCount = 4
        )
      )
      bloodRequestDao.insertRequest(
        BloodRequest(
          patientName = "Meera Nair",
          bloodGroup = "B+",
          unitsNeeded = 1,
          hospitalName = "Fortis Hospital Bannerghatta",
          location = "Bannerghatta Rd, Bengaluru",
          distanceKm = 5.8,
          urgency = "Urgent",
          requiredDate = "Tomorrow morning",
          contactPerson = "Anil Nair (Brother)",
          contactNumber = "+91 94567 89012",
          notes = "Platelet transfusion needed for oncology patient",
          isFulfilled = false,
          matchedDonorsCount = 3
        )
      )
      bloodRequestDao.insertRequest(
        BloodRequest(
          patientName = "David Joseph",
          bloodGroup = "O-",
          unitsNeeded = 3,
          hospitalName = "St. John's Medical College",
          location = "Koramangala, Bengaluru",
          distanceKm = 4.1,
          urgency = "Emergency",
          requiredDate = "Today (Immediate)",
          contactPerson = "Nurse Station 4B",
          contactNumber = "+91 98877 66554",
          notes = "Rare negative blood group required for cardiac bypass",
          isFulfilled = false,
          matchedDonorsCount = 2
        )
      )

      // Seed Notifications
      notificationDao.insertNotification(
        NotificationItem(
          type = "Emergency",
          title = "Emergency Blood Request Nearby",
          description = "O+ blood required 3.2 km away at XYZ Memorial Hospital (2 units).",
          timeAgo = "10 mins ago",
          urgencyColorHex = 0xFFDC2626
        )
      )
      notificationDao.insertNotification(
        NotificationItem(
          type = "Blood",
          title = "Donation Request Match",
          description = "A compatible blood request is available in your locality (Indiranagar).",
          timeAgo = "2 hours ago",
          urgencyColorHex = 0xFFE11D48
        )
      )
      notificationDao.insertNotification(
        NotificationItem(
          type = "Health",
          title = "Health Passport Updated",
          description = "Your medical history and allergy record (Penicillin) was synced securely.",
          timeAgo = "Yesterday",
          urgencyColorHex = 0xFF0D9488
        )
      )
      notificationDao.insertNotification(
        NotificationItem(
          type = "Security",
          title = "Privacy & Data Guard Active",
          description = "Granular donor data sharing controls applied. Medical history remains private.",
          timeAgo = "2 days ago",
          urgencyColorHex = 0xFF8B5CF6
        )
      )
    }
  }

  // Nearby Facilities Static Data
  fun getNearbyFacilities(): List<NearbyFacility> {
    return listOf(
      NearbyFacility(
        id = "hosp-1",
        name = "XYZ Memorial Super Speciality Hospital",
        type = "Hospital",
        address = "12th Main, HAL 2nd Stage, Indiranagar, Bengaluru",
        distanceKm = 1.8,
        isOpen = true,
        phone = "+91 80 2520 4400",
        availableBloodGroups = listOf("A+", "B+", "O+", "O-", "AB+"),
        bloodStockLevels = mapOf("A+" to "Available", "B+" to "Available", "O+" to "Available", "O-" to "Critical Low", "AB+" to "Available"),
        emergencyServices = listOf("24/7 Level 1 Trauma Care", "Critical Care ICU", "Cardiac Emergency", "Ambulance Hotline")
      ),
      NearbyFacility(
        id = "bb-1",
        name = "ABC Rotary Central Blood Bank",
        type = "BloodBank",
        address = "7th Cross, Koramangala 4th Block, Bengaluru",
        distanceKm = 2.4,
        isOpen = true,
        phone = "+91 80 2553 6677",
        availableBloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+"),
        bloodStockLevels = mapOf("A+" to "Available", "B+" to "Limited", "O+" to "Available", "O-" to "Contact", "AB+" to "Limited"),
        emergencyServices = listOf("Component Separation", "Platelet Apheresis", "24/7 Blood Issue", "Emergency Crossmatch")
      ),
      NearbyFacility(
        id = "hosp-2",
        name = "Apollo Speciality Hospital",
        type = "Hospital",
        address = "154/11, Opposite IIMB, Bannerghatta Road, Bengaluru",
        distanceKm = 3.5,
        isOpen = true,
        phone = "+91 80 2630 4050",
        availableBloodGroups = listOf("A+", "B+", "AB+", "O+"),
        bloodStockLevels = mapOf("A+" to "Available", "B+" to "Available", "O+" to "Available", "AB+" to "Available"),
        emergencyServices = listOf("Stroke Unit", "24/7 Cath Lab", "Pediatric Emergency", "Helipad Air Ambulance")
      ),
      NearbyFacility(
        id = "bb-2",
        name = "Red Cross State Blood Center",
        type = "BloodBank",
        address = "Race Course Road, High Grounds, Bengaluru",
        distanceKm = 4.2,
        isOpen = true,
        phone = "+91 80 2226 8456",
        availableBloodGroups = listOf("A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"),
        bloodStockLevels = mapOf("A+" to "Available", "B+" to "Available", "O+" to "Available", "O-" to "Limited", "AB-" to "Contact"),
        emergencyServices = listOf("State Blood Registry", "Rare Blood Group Bank", "Mobile Donation Van")
      ),
      NearbyFacility(
        id = "ems-1",
        name = "National Emergency Ambulance & Response Service (108)",
        type = "EmergencyService",
        address = "State Central Dispatch, Bengaluru",
        distanceKm = 1.0,
        isOpen = true,
        phone = "108",
        availableBloodGroups = emptyList(),
        bloodStockLevels = emptyMap(),
        emergencyServices = listOf("Advanced Life Support (ALS)", "GPS Real-time Dispatch", "Paramedic Onboard")
      )
    )
  }

  // Smart Matching Donors
  fun getMatchedDonors(bloodGroup: String): List<MatchedDonor> {
    return listOf(
      MatchedDonor(
        id = "donor-1",
        name = "Rahul Sharma",
        bloodGroup = bloodGroup,
        distanceKm = 2.1,
        status = "Available",
        phone = "+91 98451 99001",
        totalDonations = 6
      ),
      MatchedDonor(
        id = "donor-2",
        name = "Priya Venkatesh",
        bloodGroup = bloodGroup,
        distanceKm = 4.2,
        status = "Available",
        phone = "+91 97402 33445",
        totalDonations = 3
      ),
      MatchedDonor(
        id = "donor-3",
        name = "Vikram Aditya",
        bloodGroup = if (bloodGroup == "O+") "O-" else bloodGroup,
        distanceKm = 5.6,
        status = "Available",
        phone = "+91 99011 44556",
        totalDonations = 8
      ),
      MatchedDonor(
        id = "donor-4",
        name = "Ananya Roy",
        bloodGroup = bloodGroup,
        distanceKm = 6.8,
        status = "Available",
        phone = "+91 98860 12345",
        totalDonations = 5
      )
    )
  }

  // Profile operations
  suspend fun updateProfile(profile: UserProfile) {
    userProfileDao.insertOrUpdateProfile(profile)
  }

  suspend fun updateVitals(hr: Int, systolic: Int, diastolic: Int, spo2: Int) {
    val current = userProfileDao.getUserProfileOnce() ?: return
    userProfileDao.updateProfile(
      current.copy(
        heartRate = hr,
        systolicBp = systolic,
        diastolicBp = diastolic,
        spo2 = spo2
      )
    )
  }

  suspend fun updateDonorStatus(isDonor: Boolean, status: String) {
    val current = userProfileDao.getUserProfileOnce() ?: return
    userProfileDao.updateProfile(current.copy(isDonor = isDonor, donorStatus = status))
  }

  // Medical Record operations
  suspend fun addMedicalRecord(record: MedicalRecord): Long {
    val id = medicalRecordDao.insertRecord(record)
    notificationDao.insertNotification(
      NotificationItem(
        type = "Health",
        title = "New Medical Record Added",
        description = "${record.type}: ${record.title} added to your Health Passport.",
        timeAgo = "Just now",
        urgencyColorHex = 0xFF0D9488
      )
    )
    return id
  }

  suspend fun deleteMedicalRecord(id: Long) {
    medicalRecordDao.deleteRecordById(id)
  }

  // Emergency Contact operations
  suspend fun addEmergencyContact(contact: EmergencyContact): Long {
    return emergencyContactDao.insertContact(contact)
  }

  suspend fun deleteEmergencyContact(id: Long) {
    emergencyContactDao.deleteContactById(id)
  }

  // Blood Donation operations
  suspend fun addBloodDonation(donation: BloodDonation): Long {
    val id = bloodDonationDao.insertDonation(donation)
    val current = userProfileDao.getUserProfileOnce()
    if (current != null) {
      userProfileDao.updateProfile(
        current.copy(
          totalDonations = current.totalDonations + 1,
          lastDonationDate = donation.date
        )
      )
    }
    notificationDao.insertNotification(
      NotificationItem(
        type = "Blood",
        title = "Donation Recorded ❤️",
        description = "Your blood donation at ${donation.facilityName} has been logged.",
        timeAgo = "Just now",
        urgencyColorHex = 0xFFE11D48
      )
    )
    return id
  }

  // Blood Request operations
  suspend fun createBloodRequest(request: BloodRequest): Long {
    val id = bloodRequestDao.insertRequest(request)
    notificationDao.insertNotification(
      NotificationItem(
        type = "Emergency",
        title = "Blood Request Broadcasted 🩸",
        description = "${request.unitsNeeded} units of ${request.bloodGroup} requested at ${request.hospitalName}.",
        timeAgo = "Just now",
        urgencyColorHex = if (request.urgency == "Emergency") 0xFFDC2626 else 0xFFF59E0B
      )
    )
    return id
  }

  suspend fun fulfillBloodRequest(id: Long) {
    bloodRequestDao.updateFulfilled(id, true)
  }

  // Notification operations
  suspend fun markNotificationAsRead(id: Long) {
    notificationDao.markAsRead(id)
  }

  suspend fun clearAllNotifications() {
    notificationDao.clearAll()
  }

  // Privacy settings
  fun updatePrivacySettings(newSettings: PrivacySettings) {
    _privacySettings.value = newSettings
  }

  // AI Chat
  suspend fun sendChatMessage(userText: String) {
    val userMsg = ChatMessage(sender = MessageSender.USER, text = userText)
    _chatMessages.value = _chatMessages.value + userMsg

    val currentProfile = userProfileDao.getUserProfileOnce()
    val records = medicalRecordDao.getAllRecords().firstOrNull() ?: emptyList()
    val contacts = emergencyContactDao.getAllContacts().firstOrNull() ?: emptyList()
    val donations = bloodDonationDao.getAllDonations().firstOrNull() ?: emptyList()

    val thinkingMsg = ChatMessage(
      sender = MessageSender.JEEVIX_AI,
      text = "Analyzing health passport...",
      isThinking = true
    )
    _chatMessages.value = _chatMessages.value + thinkingMsg

    val aiResponseText = geminiAssistant.askJeevixAi(
      userPrompt = userText,
      profile = currentProfile,
      medicalRecords = records,
      emergencyContacts = contacts,
      donations = donations
    )

    _chatMessages.value = _chatMessages.value.filterNot { it.isThinking } + ChatMessage(
      sender = MessageSender.JEEVIX_AI,
      text = aiResponseText
    )
  }
}
