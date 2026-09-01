package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
  @PrimaryKey val id: Int = 1,
  val fullName: String = "Aanchal Singh",
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
  val jeevixId: String = "JVX-8821",
  val heartRate: Int = 72,
  val systolicBp: Int = 118,
  val diastolicBp: Int = 76,
  val spo2: Int = 98,
  val isOnboarded: Boolean = true,
  val isProfileComplete: Boolean = true,
  val isLocationAllowed: Boolean = true,
  // Gamification fields
  val gamificationPoints: Int = 1450,
  val donorTier: String = "Gold Lifesaver", // Bronze, Silver, Gold, Platinum, Diamond
  val unlockedBadges: String = "FIRST_DONOR,TRI_HERO,LIFESAVER_PRO,EMERGENCY_CHAMPION,VITALS_GUARDIAN",
  // Verification beyond OTP
  val isEmailVerified: Boolean = true,
  val isIdVerified: Boolean = true,
  val idType: String = "Aadhaar Card",
  val idNumberMasked: String = "XXXX-XXXX-8921",
  // Wearable health device sync
  val connectedWearable: String? = "Fitbit Charge 6",
  val wearableSyncStatus: String = "Synced 5m ago",
  val dailySteps: Int = 8420,
  val sleepHours: Double = 7.5,
  // Multilingual preference
  val selectedLanguage: String = "en" // "en", "hi", "gu"
)

data class DonorMilestone(
  val level: Int,
  val title: String,
  val requiredDonations: Int,
  val badgeName: String,
  val badgeEmoji: String,
  val rewardDescription: String,
  val pointsReward: Int,
  val isAchieved: Boolean
)

data class DonorRewardBadge(
  val id: String,
  val name: String,
  val emoji: String,
  val description: String,
  val category: String = "Donations", // Donations, Emergency, Verification, Health
  val isUnlocked: Boolean,
  val unlockedDate: String? = null,
  val points: Int = 150,
  val tierColorHex: Long = 0xFFE11D48
)

data class DonorRewards(
  val donationCount: Int,
  val livesSavedEstimated: Int,
  val currentMilestone: DonorMilestone,
  val nextMilestone: DonorMilestone?,
  val progressToNextMilestone: Float,
  val donationsNeededForNextMilestone: Int,
  val earnedBadges: List<DonorRewardBadge>,
  val allMilestones: List<DonorMilestone>,
  val totalPoints: Int,
  val currentTier: String
)

data class DonorBadge(
  val id: String,
  val title: String,
  val description: String,
  val emoji: String,
  val pointsReward: Int,
  val isUnlocked: Boolean,
  val unlockedDate: String? = null,
  val category: String = "Donations" // Donations, Emergency, Verification, Health
)

data class LeaderboardDonor(
  val rank: Int,
  val jeevixId: String,
  val name: String,
  val bloodGroup: String,
  val city: String,
  val totalDonations: Int,
  val points: Int,
  val tier: String,
  val badgeCount: Int,
  val isCurrentUser: Boolean = false
)

data class WearableDevice(
  val id: String,
  val name: String,
  val brand: String, // Fitbit, Apple, Garmin, Google, Samsung
  val iconEmoji: String,
  val isConnected: Boolean,
  val lastSynced: String,
  val batteryLevel: Int,
  val syncedMetrics: List<String>
)

data class AiHealthInsightReport(
  val overallStatus: String,
  val healthScore: Int,
  val donationReadiness: String,
  val nextEligibleDonationDate: String,
  val recoveryPercentage: Int,
  val vitalTrends: List<VitalTrendItem>,
  val riskAlerts: List<String>,
  val recommendations: List<String>,
  val medicalDisclaimer: String = "Disclaimer: AI Health Insights are generated for health tracking and preventive awareness only. They do not constitute professional medical diagnosis or clinical prescription."
)

data class VitalTrendItem(
  val metricName: String,
  val value: String,
  val status: String, // "Optimal", "Normal", "Attention"
  val trendDescription: String,
  val isNormal: Boolean = true
)

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String, val flag: String) {
  ENGLISH("en", "English", "English", "🇬🇧"),
  HINDI("hi", "Hindi", "हिंदी", "🇮🇳"),
  GUJARATI("gu", "Gujarati", "ગુજરાતી", "🇮🇳")
}

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
