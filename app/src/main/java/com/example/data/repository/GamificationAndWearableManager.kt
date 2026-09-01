package com.example.data.repository

import com.example.data.model.DonorBadge
import com.example.data.model.DonorMilestone
import com.example.data.model.DonorRewardBadge
import com.example.data.model.DonorRewards
import com.example.data.model.LeaderboardDonor
import com.example.data.model.UserProfile
import com.example.data.model.WearableDevice

object GamificationAndWearableManager {

  fun calculateDonorRewards(userProfile: UserProfile?, totalDonationsOverride: Int? = null): DonorRewards {
    val donationCount = totalDonationsOverride ?: userProfile?.totalDonations ?: 4
    val points = userProfile?.gamificationPoints ?: (1000 + donationCount * 250)
    val tier = userProfile?.donorTier ?: when {
      points >= 3500 -> "Diamond Legend"
      points >= 2500 -> "Platinum Guardian"
      points >= 1400 -> "Gold Lifesaver"
      points >= 800 -> "Silver Protector"
      else -> "Bronze Donor"
    }

    val milestones = listOf(
      DonorMilestone(
        level = 1,
        title = "First Drop of Hope",
        requiredDonations = 1,
        badgeName = "First Time Donor",
        badgeEmoji = "🩸",
        rewardDescription = "First Time Donor Badge & Digital Certificate",
        pointsReward = 200,
        isAchieved = donationCount >= 1
      ),
      DonorMilestone(
        level = 2,
        title = "Triple Impact",
        requiredDonations = 3,
        badgeName = "Life Saver",
        badgeEmoji = "❤️‍🩹",
        rewardDescription = "Life Saver Badge & Priority Matching Tier",
        pointsReward = 350,
        isAchieved = donationCount >= 3
      ),
      DonorMilestone(
        level = 3,
        title = "Guardian of Life",
        requiredDonations = 5,
        badgeName = "Five-Star Life Saver",
        badgeEmoji = "🌟",
        rewardDescription = "Gold Lifesaver Status & Exclusive Hero Pin",
        pointsReward = 500,
        isAchieved = donationCount >= 5
      ),
      DonorMilestone(
        level = 4,
        title = "Century Lifeline Hero",
        requiredDonations = 10,
        badgeName = "Century Hero",
        badgeEmoji = "🏆",
        rewardDescription = "Platinum Guardian Status & Regional Hall of Fame",
        pointsReward = 1000,
        isAchieved = donationCount >= 10
      ),
      DonorMilestone(
        level = 5,
        title = "Diamond Benefactor",
        requiredDonations = 20,
        badgeName = "Diamond Legend",
        badgeEmoji = "💎",
        rewardDescription = "Diamond Legend Medal & State Donor Recognition",
        pointsReward = 2500,
        isAchieved = donationCount >= 20
      )
    )

    val currentMilestone = milestones.lastOrNull { it.isAchieved } ?: DonorMilestone(
      level = 0,
      title = "New Donor Candidate",
      requiredDonations = 0,
      badgeName = "Initiate",
      badgeEmoji = "🌱",
      rewardDescription = "Complete your first blood donation to start the journey",
      pointsReward = 50,
      isAchieved = true
    )

    val nextMilestone = milestones.firstOrNull { !it.isAchieved }

    val (progress, needed) = if (nextMilestone != null) {
      val prevThreshold = currentMilestone.requiredDonations
      val nextThreshold = nextMilestone.requiredDonations
      val span = (nextThreshold - prevThreshold).coerceAtLeast(1)
      val currentProgress = ((donationCount - prevThreshold).toFloat() / span.toFloat()).coerceIn(0f, 1f)
      val donationsNeeded = (nextThreshold - donationCount).coerceAtLeast(0)
      Pair(currentProgress, donationsNeeded)
    } else {
      Pair(1.0f, 0)
    }

    val badges = listOf(
      DonorRewardBadge(
        id = "FIRST_TIME_DONOR",
        name = "First Time Donor",
        emoji = "🩸",
        description = "Logged your first successful voluntary blood donation",
        category = "Donation",
        isUnlocked = donationCount >= 1,
        unlockedDate = if (donationCount >= 1) "10 Mar 2025" else null,
        points = 200,
        tierColorHex = 0xFFE11D48
      ),
      DonorRewardBadge(
        id = "LIFE_SAVER",
        name = "Life Saver",
        emoji = "❤️‍🩹",
        description = "Donated 3+ times to save critical accident and surgery patients",
        category = "Donation",
        isUnlocked = donationCount >= 3,
        unlockedDate = if (donationCount >= 3) "18 Aug 2025" else null,
        points = 350,
        tierColorHex = 0xFFF59E0B
      ),
      DonorRewardBadge(
        id = "FIVE_STAR_GUARDIAN",
        name = "Five-Star Life Saver",
        emoji = "🌟",
        description = "Completed 5 lifetime blood donations milestone",
        category = "Donation",
        isUnlocked = donationCount >= 5,
        unlockedDate = if (donationCount >= 5) "12 May 2026" else null,
        points = 500,
        tierColorHex = 0xFF8B5CF6
      ),
      DonorRewardBadge(
        id = "CENTURY_HERO",
        name = "Century Hero",
        emoji = "🏆",
        description = "Achieve 10+ total lifetime donations with Jeevix",
        category = "Donation",
        isUnlocked = donationCount >= 10,
        unlockedDate = if (donationCount >= 10) "Active" else null,
        points = 1000,
        tierColorHex = 0xFF10B981
      ),
      DonorRewardBadge(
        id = "DIAMOND_BENEFACTOR",
        name = "Diamond Benefactor",
        emoji = "💎",
        description = "Elite 20+ blood donations milestone",
        category = "Donation",
        isUnlocked = donationCount >= 20,
        unlockedDate = null,
        points = 2500,
        tierColorHex = 0xFF06B6D4
      ),
      DonorRewardBadge(
        id = "EMERGENCY_LIFELINE",
        name = "Emergency Lifeline",
        emoji = "🚨",
        description = "Responded to an SOS or critical trauma blood broadcast",
        category = "Emergency",
        isUnlocked = true,
        unlockedDate = "04 Jan 2026",
        points = 350,
        tierColorHex = 0xFFBE123C
      ),
      DonorRewardBadge(
        id = "GOLDEN_VERIFIED",
        name = "Golden Shield",
        emoji = "🛡️",
        description = "Govt ID (Aadhaar) & Medical Verification Completed",
        category = "Verification",
        isUnlocked = userProfile?.isIdVerified == true,
        unlockedDate = "Verified",
        points = 200,
        tierColorHex = 0xFF6366F1
      ),
      DonorRewardBadge(
        id = "HEALTH_PULSE_MASTER",
        name = "Health Pulse Master",
        emoji = "⌚",
        description = "Synced smartwatch & verified optimal donation vitals",
        category = "Health",
        isUnlocked = userProfile?.connectedWearable != null,
        unlockedDate = "Active",
        points = 150,
        tierColorHex = 0xFF0D9488
      ),
      DonorRewardBadge(
        id = "UNIVERSAL_CHAMPION",
        name = "Universal Champion",
        emoji = "👑",
        description = "Active emergency donor of universal or rare blood group",
        category = "Donation",
        isUnlocked = true,
        unlockedDate = "Active",
        points = 400,
        tierColorHex = 0xFFD97706
      )
    )

    return DonorRewards(
      donationCount = donationCount,
      livesSavedEstimated = donationCount * 3,
      currentMilestone = currentMilestone,
      nextMilestone = nextMilestone,
      progressToNextMilestone = progress,
      donationsNeededForNextMilestone = needed,
      earnedBadges = badges,
      allMilestones = milestones,
      totalPoints = points,
      currentTier = tier
    )
  }

  fun getAllBadges(unlockedIdsString: String): List<DonorBadge> {
    val unlockedSet = unlockedIdsString.split(",").map { it.trim() }.toSet()

    return listOf(
      DonorBadge(
        id = "FIRST_DONOR",
        title = "First Drop of Life",
        description = "Completed your first successful voluntary blood donation",
        emoji = "🩸",
        pointsReward = 150,
        isUnlocked = unlockedSet.contains("FIRST_DONOR") || unlockedSet.contains("FIRST_TIME"),
        unlockedDate = "10 Mar 2025",
        category = "Donations"
      ),
      DonorBadge(
        id = "TRI_HERO",
        title = "Tri-Hero Lifesaver",
        description = "Donated blood 3 times to save critical patients",
        emoji = "🥉",
        pointsReward = 300,
        isUnlocked = unlockedSet.contains("TRI_HERO") || unlockedSet.contains("THREE_PEAT"),
        unlockedDate = "18 Aug 2025",
        category = "Donations"
      ),
      DonorBadge(
        id = "LIFESAVER_PRO",
        title = "Five-Star Guardian",
        description = "Completed 5 blood donations milestone",
        emoji = "🌟",
        pointsReward = 500,
        isUnlocked = unlockedSet.contains("LIFESAVER_PRO") || unlockedSet.contains("FIVE_STAR"),
        unlockedDate = "12 May 2026",
        category = "Donations"
      ),
      DonorBadge(
        id = "EMERGENCY_CHAMPION",
        title = "Emergency First Responder",
        description = "Responded to an SOS or emergency trauma blood broadcast",
        emoji = "🚨",
        pointsReward = 350,
        isUnlocked = unlockedSet.contains("EMERGENCY_CHAMPION"),
        unlockedDate = "04 Jan 2026",
        category = "Emergency"
      ),
      DonorBadge(
        id = "GOLDEN_VERIFIED",
        title = "Golden Verified Shield",
        description = "Completed Govt ID (Aadhaar/Passport) & Email verification",
        emoji = "🛡️",
        pointsReward = 200,
        isUnlocked = unlockedSet.contains("GOLDEN_VERIFIED") || true,
        unlockedDate = "Verified",
        category = "Verification"
      ),
      DonorBadge(
        id = "VITALS_GUARDIAN",
        title = "Wearable Health Sync Master",
        description = "Connected a smartwatch & synced vital metrics regularly",
        emoji = "⌚",
        pointsReward = 150,
        isUnlocked = unlockedSet.contains("VITALS_GUARDIAN"),
        unlockedDate = "Active",
        category = "Health"
      ),
      DonorBadge(
        id = "RARE_BLOOD_DEFENDER",
        title = "Universal Lifesaver",
        description = "Registered as an active O- or O+ emergency blood donor",
        emoji = "👑",
        pointsReward = 400,
        isUnlocked = true,
        unlockedDate = "Active",
        category = "Donations"
      ),
      DonorBadge(
        id = "CENTURY_HERO",
        title = "Century Club Champion",
        description = "Achieve 10+ total lifetime donations with Jeevix",
        emoji = "🏆",
        pointsReward = 1000,
        isUnlocked = false,
        unlockedDate = null,
        category = "Donations"
      )
    )
  }

  fun getRegionalLeaderboard(userProfile: UserProfile?, selectedCity: String = "Bengaluru"): List<LeaderboardDonor> {
    val currentPoints = userProfile?.gamificationPoints ?: 1450
    val currentDonations = userProfile?.totalDonations ?: 4
    val userName = userProfile?.fullName ?: "Aanchal Singh"

    return listOf(
      LeaderboardDonor(
        rank = 1,
        jeevixId = "JVX-1092",
        name = "Dr. Vikram Kulkarni",
        bloodGroup = "O+",
        city = "Bengaluru",
        totalDonations = 18,
        points = 4850,
        tier = "Diamond Legend",
        badgeCount = 8
      ),
      LeaderboardDonor(
        rank = 2,
        jeevixId = "JVX-3421",
        name = "Rajeshwari Hegde",
        bloodGroup = "B+",
        city = "Bengaluru",
        totalDonations = 14,
        points = 3720,
        tier = "Platinum Guardian",
        badgeCount = 7
      ),
      LeaderboardDonor(
        rank = 3,
        jeevixId = "JVX-5512",
        name = "Mohammad Farooq",
        bloodGroup = "A+",
        city = "Bengaluru",
        totalDonations = 11,
        points = 2950,
        tier = "Platinum Guardian",
        badgeCount = 6
      ),
      LeaderboardDonor(
        rank = 4,
        jeevixId = userProfile?.jeevixId ?: "JVX-8821",
        name = "$userName (You)",
        bloodGroup = userProfile?.bloodGroup ?: "O+",
        city = "Bengaluru",
        totalDonations = currentDonations,
        points = currentPoints,
        tier = userProfile?.donorTier ?: "Gold Lifesaver",
        badgeCount = 6,
        isCurrentUser = true
      ),
      LeaderboardDonor(
        rank = 5,
        jeevixId = "JVX-7741",
        name = "Karthik Venkatesh",
        bloodGroup = "AB+",
        city = "Bengaluru",
        totalDonations = 4,
        points = 1380,
        tier = "Gold Lifesaver",
        badgeCount = 5
      ),
      LeaderboardDonor(
        rank = 6,
        jeevixId = "JVX-9014",
        name = "Sneha Chawla",
        bloodGroup = "O-",
        city = "Bengaluru",
        totalDonations = 3,
        points = 1150,
        tier = "Silver Protector",
        badgeCount = 4
      ),
      LeaderboardDonor(
        rank = 7,
        jeevixId = "JVX-6239",
        name = "Arjun Patel",
        bloodGroup = "B-",
        city = "Bengaluru",
        totalDonations = 3,
        points = 980,
        tier = "Silver Protector",
        badgeCount = 4
      ),
      LeaderboardDonor(
        rank = 8,
        jeevixId = "JVX-4188",
        name = "Divya Mehra",
        bloodGroup = "A-",
        city = "Bengaluru",
        totalDonations = 2,
        points = 720,
        tier = "Bronze Donor",
        badgeCount = 3
      )
    )
  }

  fun getSupportedWearables(connectedDeviceName: String?): List<WearableDevice> {
    return listOf(
      WearableDevice(
        id = "fitbit_charge6",
        name = "Fitbit Charge 6",
        brand = "Fitbit",
        iconEmoji = "⌚",
        isConnected = connectedDeviceName?.contains("Fitbit", ignoreCase = true) == true,
        lastSynced = "5 mins ago",
        batteryLevel = 88,
        syncedMetrics = listOf("Heart Rate", "SpO2", "Blood Pressure", "Daily Steps", "Sleep Score")
      ),
      WearableDevice(
        id = "apple_watch",
        name = "Apple Watch Series 9 / Ultra",
        brand = "Apple Health",
        iconEmoji = "🍏",
        isConnected = connectedDeviceName?.contains("Apple", ignoreCase = true) == true,
        lastSynced = "1 hour ago",
        batteryLevel = 92,
        syncedMetrics = listOf("ECG", "Heart Rate", "Blood Oxygen", "Active Calories", "Sleep")
      ),
      WearableDevice(
        id = "google_pixel",
        name = "Google Pixel Watch / Health Connect",
        brand = "Google Health Connect",
        iconEmoji = "🔵",
        isConnected = connectedDeviceName?.contains("Google", ignoreCase = true) == true || connectedDeviceName?.contains("Pixel", ignoreCase = true) == true,
        lastSynced = "Ready to connect",
        batteryLevel = 100,
        syncedMetrics = listOf("Resting HR", "SpO2", "Skin Temperature", "Steps")
      ),
      WearableDevice(
        id = "garmin_venu",
        name = "Garmin Venu 3 / Forerunner",
        brand = "Garmin Health",
        iconEmoji = "🧭",
        isConnected = connectedDeviceName?.contains("Garmin", ignoreCase = true) == true,
        lastSynced = "Ready to connect",
        batteryLevel = 95,
        syncedMetrics = listOf("Pulse Ox (SpO2)", "Heart Rate Variability", "Body Battery", "Steps")
      ),
      WearableDevice(
        id = "samsung_galaxy",
        name = "Samsung Galaxy Watch 6",
        brand = "Samsung Health",
        iconEmoji = "⌚",
        isConnected = connectedDeviceName?.contains("Samsung", ignoreCase = true) == true,
        lastSynced = "Ready to connect",
        batteryLevel = 84,
        syncedMetrics = listOf("Blood Pressure (BIA)", "HR", "SpO2", "Sleep Staging")
      )
    )
  }
}
