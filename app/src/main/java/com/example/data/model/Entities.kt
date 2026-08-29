package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
  @PrimaryKey val id: Int = 1,
  val fullName: String = "Aanchal Sharma",
  val email: String = "aanchalbb11@gmail.com",
  val phoneNumber: String = "+91 98765 43210",
  val dateOfBirth: String = "14 Aug 2000",
  val gender: String = "Female",
  val heightCm: String = "165 cm",
  val weightKg: String = "58 kg",
  val cityLocation: String = "Bengaluru, Karnataka",
  val bloodGroup: String = "O+",
  val rhFactor: String = "Positive",
  val isDonor: Boolean = true,
  val donorStatus: String = "Available", // Available, Cooldown, Inactive
  val lastDonationDate: String = "12 May 2026",
  val totalDonations: Int = 4,
  val jeevixId: String = "JVX-8942-O+",
  val heartRate: Int = 72,
  val systolicBp: Int = 118,
  val diastolicBp: Int = 76,
  val spo2: Int = 98,
  val isOnboarded: Boolean = true,
  val isProfileComplete: Boolean = true,
  val isLocationAllowed: Boolean = true
)

@Entity(tableName = "medical_records")
data class MedicalRecord(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val type: String, // Allergy, Medication, Condition, Surgery, Note
  val title: String,
  val description: String = "",
  val yearOrDate: String = "2026",
  val severityOrDosage: String = "",
  val notes: String = "",
  val isEmergencyRelevant: Boolean = true
)

@Entity(tableName = "emergency_contacts")
data class EmergencyContact(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val name: String,
  val relationship: String,
  val phoneNumber: String,
  val isPrimary: Boolean = false,
  val avatarColorHex: Long = 0xFFE11D48
)

@Entity(tableName = "blood_donations")
data class BloodDonation(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val date: String,
  val facilityName: String,
  val unitsGiven: Int = 1,
  val bloodGroup: String = "O+",
  val certificateNumber: String = "CERT-JVX-2026-084",
  val notes: String = "Voluntary blood donation camp"
)

@Entity(tableName = "blood_requests")
data class BloodRequest(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val patientName: String,
  val bloodGroup: String,
  val unitsNeeded: Int,
  val hospitalName: String,
  val location: String,
  val distanceKm: Double,
  val urgency: String, // Emergency, Urgent, Normal
  val requiredDate: String,
  val contactPerson: String,
  val contactNumber: String,
  val notes: String = "",
  val isFulfilled: Boolean = false,
  val isUserCreated: Boolean = false,
  val matchedDonorsCount: Int = 3
)

@Entity(tableName = "notifications")
data class NotificationItem(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val type: String, // Emergency, Blood, Health, Security
  val title: String,
  val description: String,
  val timeAgo: String,
  val isRead: Boolean = false,
  val urgencyColorHex: Long = 0xFFBE123C
)

data class NearbyFacility(
  val id: String,
  val name: String,
  val type: String, // Hospital, BloodBank, EmergencyService
  val address: String,
  val distanceKm: Double,
  val isOpen: Boolean,
  val phone: String,
  val availableBloodGroups: List<String>,
  val bloodStockLevels: Map<String, String>, // "A+" -> "Available", "B+" -> "Limited"
  val emergencyServices: List<String>
)

data class MatchedDonor(
  val id: String,
  val name: String,
  val bloodGroup: String,
  val distanceKm: Double,
  val status: String,
  val phone: String,
  val totalDonations: Int
)

data class PrivacySettings(
  val shareBloodGroup: Boolean = true,
  val shareName: Boolean = true,
  val shareApproxLocation: Boolean = true,
  val sharePhoneWithConsentOnly: Boolean = true,
  val shareMedicalHistory: Boolean = false,
  val shareMedications: Boolean = false,
  val shareEmergencyContacts: Boolean = false,
  val biometricLockEnabled: Boolean = false,
  val emergencyQrEnabled: Boolean = true,
  val shareBloodGroupWithRequesters: Boolean = true,
  val hideMedicalHistoryFromPublic: Boolean = true,
  val allowGpsDuringSosOnly: Boolean = true,
  val allowAiToReadPassport: Boolean = true
)

data class ChatMessage(
  val id: String = java.util.UUID.randomUUID().toString(),
  val sender: MessageSender,
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isThinking: Boolean = false
)

enum class MessageSender {
  USER, JEEVIX_AI
}
