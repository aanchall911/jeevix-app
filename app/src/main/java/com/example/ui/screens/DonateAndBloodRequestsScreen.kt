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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
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
import com.example.data.model.DonorBadge
import com.example.data.model.DonorRewardBadge
import com.example.data.model.DonorRewards
import com.example.data.model.LeaderboardDonor
import com.example.data.model.MatchedDonor
import com.example.data.model.UserProfile
import com.example.ui.components.DonorBadgeDetailDialog
import com.example.ui.components.DonorMilestoneProgressCard
import com.example.ui.components.DonorRewardBadgeChip
import com.example.ui.components.EmailVerificationDialog
import com.example.ui.components.GovtIdVerificationDialog
import com.example.ui.theme.JeevixAiPink
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixEmergencyRed
import com.example.ui.theme.JeevixSuccess
import com.example.ui.theme.JeevixTeal
import com.example.ui.util.LocalizationManager
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
  val showEmailDialog by viewModel.showEmailVerifyDialog.collectAsState()
  val showIdDialog by viewModel.showIdVerifyDialog.collectAsState()
  val leaderboardCity by viewModel.leaderboardCity.collectAsState()

  val currentLang = userProfile?.selectedLanguage ?: "en"

  var selectedTab by remember { mutableIntStateOf(0) } // 0: Donor Profile & Badges, 1: Leaderboard, 2: Blood Requests, 3: Donation History
  val tabs = listOf(
    LocalizationManager.get("donor_profile", currentLang),
    LocalizationManager.get("leaderboard", currentLang),
    LocalizationManager.get("blood_requests", currentLang),
    LocalizationManager.get("donation_history", currentLang)
  )

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

  // Email Verify Dialog
  if (showEmailDialog) {
    EmailVerificationDialog(
      currentEmail = userProfile?.email ?: "aanchalbb11@gmail.com",
      onVerify = { email -> viewModel.verifyEmail(email) },
      onDismiss = { viewModel.showEmailVerifyDialog(false) }
    )
  }

  // Govt ID Verify Dialog
  if (showIdDialog) {
    GovtIdVerificationDialog(
      onVerify = { idType, idNum -> viewModel.verifyGovtId(idType, idNum) },
      onDismiss = { viewModel.showIdVerifyDialog(false) }
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
            text = "Blood Network & Lifesavers",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "Donor rewards, milestones & live requests",
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
          Text(LocalizationManager.get("request_blood", currentLang), fontWeight = FontWeight.Bold)
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
        0 -> DonorProfileAndGamificationView(
          profile = userProfile,
          rewards = viewModel.getDonorRewards(),
          badges = viewModel.getAllBadges(),
          onUpdateStatus = { isDonor, status -> viewModel.updateDonorStatus(isDonor, status) },
          onVerifyEmailClick = { viewModel.showEmailVerifyDialog(true) },
          onVerifyIdClick = { viewModel.showIdVerifyDialog(true) },
          languageCode = currentLang
        )
        1 -> RegionalLeaderboardView(
          leaderboard = viewModel.getRegionalLeaderboard(),
          selectedCity = leaderboardCity,
          onCitySelect = { city -> viewModel.setLeaderboardCity(city) },
          userProfile = userProfile,
          languageCode = currentLang
        )
        2 -> BloodRequestsView(
          requests = bloodRequests,
          onSmartMatch = { req -> viewModel.openSmartMatching(req) },
          onFulfill = { id -> viewModel.fulfillBloodRequest(id) },
          languageCode = currentLang
        )
        3 -> DonationHistoryTab(
          donations = donations,
          onAddClick = { viewModel.showAddDonationDialog(true) },
          languageCode = currentLang
        )
      }
    }
  }
}

@Composable
fun DonorProfileAndGamificationView(
  profile: UserProfile?,
  rewards: DonorRewards,
  badges: List<DonorBadge>,
  onUpdateStatus: (Boolean, String) -> Unit,
  onVerifyEmailClick: () -> Unit,
  onVerifyIdClick: () -> Unit,
  languageCode: String
) {
  val bloodGroup = profile?.bloodGroup ?: "O+"
  val isDonor = profile?.isDonor ?: true
  val donorStatus = profile?.donorStatus ?: "Available"
  var selectedBadgeCategory by remember { mutableStateOf("All") }
  var selectedBadgeForDialog by remember { mutableStateOf<DonorRewardBadge?>(null) }

  // Dialog for detailed badge inspection
  if (selectedBadgeForDialog != null) {
    DonorBadgeDetailDialog(
      badge = selectedBadgeForDialog!!,
      onDismiss = { selectedBadgeForDialog = null }
    )
  }

  val badgeCategories = listOf("All", "Donation", "Emergency", "Verification", "Health")
  val filteredBadges = remember(selectedBadgeCategory, rewards.earnedBadges) {
    if (selectedBadgeCategory == "All") {
      rewards.earnedBadges
    } else {
      rewards.earnedBadges.filter { it.category.equals(selectedBadgeCategory, ignoreCase = true) }
    }
  }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. DONOR REWARDS & NEXT MILESTONE PROGRESS CARD
    item {
      DonorMilestoneProgressCard(
        rewards = rewards,
        onBadgeClick = { selectedBadgeForDialog = it }
      )
    }

    // 2. DONOR PROFILE PASSPORT CARD
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
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = profile?.fullName ?: "Aanchal Singh",
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                if (profile?.isIdVerified == true) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Icon(Icons.Default.Verified, contentDescription = "Verified", tint = Color(0xFF8B5CF6), modifier = Modifier.size(18.dp))
                }
              }
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
              Text(text = "${rewards.donationCount} Times", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Column {
              Text(text = "Last Donated", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = profile?.lastDonationDate ?: "12 May 2026", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Column {
              Text(text = "Lives Saved", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "${rewards.livesSavedEstimated}+ Lives ❤️", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = JeevixCrimson)
            }
          }
        }
      }
    }

    // 3. DYNAMIC REWARDS BADGES SECTION WITH CHIPS
    item {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Donor Rewards & Badges",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold
            )
            Text(
              text = "${rewards.earnedBadges.count { it.isUnlocked }} of ${rewards.earnedBadges.size} Badges Unlocked • Tap for details",
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFFBBF24).copy(alpha = 0.15f)
          ) {
            Text(
              text = "Gamified Tier",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFFD97706),
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          items(badgeCategories) { category ->
            val isSelected = selectedBadgeCategory == category
            Surface(
              onClick = { selectedBadgeCategory = category },
              shape = RoundedCornerShape(10.dp),
              color = if (isSelected) Color(0xFF0F172A) else MaterialTheme.colorScheme.surfaceVariant,
              border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
              Text(
                text = category,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Dynamic Chips Row for Badges (e.g. 'First Time Donor', 'Life Saver')
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(filteredBadges, key = { it.id }) { badge ->
            DonorRewardBadgeChip(
              badge = badge,
              onClick = { selectedBadgeForDialog = badge }
            )
          }
        }
      }
    }

    // 4. IDENTITY VERIFICATION STATUS
    item {
      Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Donor Verification & Trust Badges",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(10.dp))

          // Email Verification Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Verified, contentDescription = null, tint = if (profile?.isEmailVerified == true) JeevixSuccess else Color(0xFF94A3B8))
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text("Email Address Verification", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(profile?.email ?: "aanchalbb11@gmail.com", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            if (profile?.isEmailVerified == true) {
              Surface(shape = CircleShape, color = JeevixSuccess.copy(alpha = 0.15f)) {
                Text("Verified ✓", color = JeevixSuccess, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
              }
            } else {
              OutlinedButton(onClick = onVerifyEmailClick, shape = RoundedCornerShape(8.dp)) {
                Text("Verify (+100 XP)", style = MaterialTheme.typography.labelSmall)
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Govt ID Verification Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("🛡️", fontSize = 18.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text("Government ID Shield", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(if (profile?.isIdVerified == true) "${profile.idType} (${profile.idNumberMasked})" else "Aadhaar / Passport / Voter ID", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }

            if (profile?.isIdVerified == true) {
              Surface(shape = CircleShape, color = Color(0xFF8B5CF6).copy(alpha = 0.15f)) {
                Text("Shielded ✓", color = Color(0xFF8B5CF6), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
              }
            } else {
              Button(
                onClick = onVerifyIdClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
              ) {
                Text("Verify (+200 XP)", style = MaterialTheme.typography.labelSmall)
              }
            }
          }
        }
      }
    }

    // 5. DONOR AVAILABILITY STATUS
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

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun RegionalLeaderboardView(
  leaderboard: List<LeaderboardDonor>,
  selectedCity: String,
  onCitySelect: (String) -> Unit,
  userProfile: UserProfile?,
  languageCode: String
) {
  val cities = listOf("Bengaluru", "Mumbai", "Delhi", "Ahmedabad", "Hyderabad")

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(20.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "🏆 Regional Blood Lifesaver Leaderboard",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "Top blood heroes recognized across city zones",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(12.dp))

      // City Filter Selector Chips
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(cities) { city ->
          val isSelected = selectedCity.equals(city, ignoreCase = true)
          Surface(
            onClick = { onCitySelect(city) },
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) JeevixCrimson else MaterialTheme.colorScheme.surfaceVariant,
            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
          ) {
            Text(
              text = city,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
              style = MaterialTheme.typography.labelMedium,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }
    }

    // Top 3 Podium Cards
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
      ) {
        // #2 Silver
        if (leaderboard.size > 1) {
          PodiumCard(donor = leaderboard[1], rank = 2, medal = "🥈", color = Color(0xFF94A3B8), modifier = Modifier.weight(1f))
        }
        // #1 Gold
        if (leaderboard.isNotEmpty()) {
          PodiumCard(donor = leaderboard[0], rank = 1, medal = "🥇", color = Color(0xFFF59E0B), modifier = Modifier.weight(1.1f))
        }
        // #3 Bronze
        if (leaderboard.size > 2) {
          PodiumCard(donor = leaderboard[2], rank = 3, medal = "🥉", color = Color(0xFFD97706), modifier = Modifier.weight(1f))
        }
      }
    }

    // Ranked List items
    items(leaderboard.drop(3), key = { it.rank }) { donor ->
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (donor.isCurrentUser) JeevixCrimson.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = if (donor.isCurrentUser) androidx.compose.foundation.BorderStroke(1.5.dp, JeevixCrimson.copy(alpha = 0.5f)) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = CircleShape,
              color = if (donor.isCurrentUser) JeevixCrimson else MaterialTheme.colorScheme.surfaceVariant,
              modifier = Modifier.size(32.dp)
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = "#${donor.rank}",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = if (donor.isCurrentUser) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = donor.name,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold
                )
                if (donor.isCurrentUser) {
                  Spacer(modifier = Modifier.width(6.dp))
                  Surface(shape = CircleShape, color = JeevixCrimson.copy(alpha = 0.15f)) {
                    Text("YOU", color = JeevixCrimson, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                  }
                }
              }
              Text(
                text = "${donor.bloodGroup} • ${donor.totalDonations} Donations • ${donor.tier}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Text(
            text = "${donor.points} XP",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFF59E0B)
          )
        }
      }
    }

    item { Spacer(modifier = Modifier.height(60.dp)) }
  }
}

@Composable
fun PodiumCard(
  donor: LeaderboardDonor,
  rank: Int,
  medal: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.5.dp, color.copy(alpha = 0.5f)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(medal, fontSize = 24.sp)
      Text("#$rank", fontWeight = FontWeight.ExtraBold, color = color, fontSize = 14.sp)
      Spacer(modifier = Modifier.height(4.dp))
      Text(donor.name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, maxLines = 1)
      Text("${donor.bloodGroup} • ${donor.totalDonations}x", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
      Spacer(modifier = Modifier.height(4.dp))
      Surface(shape = CircleShape, color = color.copy(alpha = 0.15f)) {
        Text("${donor.points} XP", color = color, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
      }
    }
  }
}

@Composable
fun BloodRequestsView(
  requests: List<BloodRequest>,
  onSmartMatch: (BloodRequest) -> Unit,
  onFulfill: (Long) -> Unit,
  languageCode: String
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
fun DonationHistoryTab(
  donations: List<BloodDonation>,
  onAddClick: () -> Unit,
  languageCode: String
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
        Text(
          text = "Blood Donation Logbook",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Button(
          onClick = onAddClick,
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = JeevixCrimson)
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Log Donation (+250 XP)", fontWeight = FontWeight.Bold)
        }
      }
    }

    items(donations, key = { it.id }) { donation ->
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFE4E6)),
              contentAlignment = Alignment.Center
            ) {
              Text("🩸", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(donation.facilityName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
              Text("Date: ${donation.date} • ${donation.unitsGiven} Unit(s)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              if (donation.notes.isNotBlank()) {
                Text(donation.notes, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }

          Surface(
            shape = CircleShape,
            color = Color(0xFFFEF3C7)
          ) {
            Text(
              text = "+250 XP",
              color = Color(0xFFD97706),
              fontWeight = FontWeight.ExtraBold,
              style = MaterialTheme.typography.labelSmall,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
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
