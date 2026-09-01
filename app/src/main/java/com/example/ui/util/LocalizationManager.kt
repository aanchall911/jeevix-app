package com.example.ui.util

import com.example.data.model.AppLanguage

object LocalizationManager {

  fun get(key: String, languageCode: String = "en"): String {
    val lang = when (languageCode.lowercase()) {
      "hi" -> AppLanguage.HINDI
      "gu" -> AppLanguage.GUJARATI
      else -> AppLanguage.ENGLISH
    }

    val dictionary = when (lang) {
      AppLanguage.HINDI -> hindiStrings
      AppLanguage.GUJARATI -> gujaratiStrings
      AppLanguage.ENGLISH -> englishStrings
    }

    return dictionary[key] ?: englishStrings[key] ?: key
  }

  private val englishStrings = mapOf(
    // App & Navigation
    "app_name" to "Jeevix",
    "home" to "Home",
    "passport" to "Passport",
    "sos" to "SOS",
    "donate" to "Donate",
    "network" to "Network",
    "ai_assistant" to "AI Assistant",
    "leaderboard" to "Leaderboard",
    "settings" to "Settings",
    "notifications" to "Notifications",

    // Greeting & Header
    "good_morning" to "GOOD MORNING",
    "digital_identity" to "Digital Identity",
    "health_passport" to "JEEVIX HEALTH PASSPORT",
    "verified_donor" to "Verified Donor",
    "non_donor" to "Non-Donor",
    "open_full_passport" to "Open Full Health Passport",

    // Quick Actions
    "find_blood" to "Find Blood",
    "hospital" to "Hospital",
    "blood_bank" to "Blood Bank",
    "find_donor" to "Find Donor",

    // Health Snapshot & Vitals
    "health_snapshot" to "Health Snapshot",
    "today" to "TODAY",
    "heart_rate" to "Heart Rate",
    "blood_pressure" to "Pressure",
    "spo2" to "SpO₂",
    "oxygen" to "Oxygen",
    "bpm" to "BPM",
    "normal_range" to "Normal range",
    "edit_vitals" to "Edit Vitals ✎",
    "wearable_synced" to "⌚ Wearable Synced",
    "sync_device" to "Sync Wearable",
    "ai_health_insights" to "AI Health Insights",
    "view_ai_analysis" to "View AI Health Analysis & Trends",

    // AI Assistant
    "ask_jeevix_ai" to "Ask Jeevix AI",
    "ai_assistant_title" to "AI ASSISTANT",
    "chat_now" to "CHAT NOW",
    "find_matching_donors" to "Find Matching Donors",
    "ai_analyzing" to "Analyzing health passport...",

    // Gamification
    "lifesaver_points" to "Lifesaver Points",
    "donor_tier" to "Donor Tier",
    "donor_rank" to "Regional Rank",
    "milestone_badges" to "Milestone Badges",
    "all_badges" to "All Badges",
    "top_donors" to "Top Donors Leaderboard",
    "points_for_donation" to "+250 Points per donation",
    "next_tier_progress" to "Progress to Platinum Tier",

    // Verification
    "identity_verification" to "Identity Verification",
    "phone_verified" to "Phone Verified",
    "email_verified" to "Email Verified",
    "govt_id_verified" to "Govt ID Verified",
    "verify_email" to "Verify Email",
    "verify_id" to "Verify Govt ID",
    "security_shield" to "Jeevix Security & Trust",

    // Wearables
    "wearable_health_sync" to "Wearable Health Devices",
    "connected_device" to "Connected Device",
    "auto_sync_active" to "Auto-Sync Active",
    "last_synced" to "Last synced",
    "connect_watch" to "Connect Smartwatch",

    // Language
    "change_language" to "Change Language",
    "select_language" to "Select Your Preferred Language"
  )

  private val hindiStrings = mapOf(
    // App & Navigation
    "app_name" to "जीविक्स",
    "home" to "होम",
    "passport" to "पासपोर्ट",
    "sos" to "एसओएस",
    "donate" to "रक्तदान",
    "network" to "नेटवर्क",
    "ai_assistant" to "एआई सहायक",
    "leaderboard" to "लीडरबोर्ड",
    "settings" to "सेटिंग्स",
    "notifications" to "सूचनाएं",

    // Greeting & Header
    "good_morning" to "शुभ प्रभात",
    "digital_identity" to "डिजिटल पहचान",
    "health_passport" to "जीविक्स हेल्थ पासपोर्ट",
    "verified_donor" to "सत्यापित रक्तदाता",
    "non_donor" to "गैर-दाता",
    "open_full_passport" to "हेल्थ पासपोर्ट खोलें",

    // Quick Actions
    "find_blood" to "रक्त खोजें",
    "hospital" to "अस्पताल",
    "blood_bank" to "ब्लड बैंक",
    "find_donor" to "दाता खोजें",

    // Health Snapshot & Vitals
    "health_snapshot" to "स्वास्थ्य स्नैपशॉट",
    "today" to "आज",
    "heart_rate" to "हृदय गति",
    "blood_pressure" to "रक्तचाप",
    "spo2" to "SpO₂",
    "oxygen" to "ऑक्सीजन",
    "bpm" to "BPM",
    "normal_range" to "सामान्य सीमा",
    "edit_vitals" to "वाइटल्स बदलें ✎",
    "wearable_synced" to "⌚ स्मार्टवॉच सिंक हुआ",
    "sync_device" to "डिवाइस सिंक करें",
    "ai_health_insights" to "एआई स्वास्थ्य विश्लेषण",
    "view_ai_analysis" to "एआई स्वास्थ्य ट्रेंड्स देखें",

    // AI Assistant
    "ask_jeevix_ai" to "जीविक्स एआई से पूछें",
    "ai_assistant_title" to "एआई सहायक",
    "chat_now" to "बातचीत करें",
    "find_matching_donors" to "रक्तदाता खोजें",
    "ai_analyzing" to "हेल्थ पासपोर्ट का विश्लेषण हो रहा है...",

    // Gamification
    "lifesaver_points" to "जीवनरक्षक अंक",
    "donor_tier" to "दाता स्तर",
    "donor_rank" to "क्षेत्रीय रैंक",
    "milestone_badges" to "उपलब्धि बैज",
    "all_badges" to "सभी बैज",
    "top_donors" to "शीर्ष रक्तदाता लीडरबोर्ड",
    "points_for_donation" to "प्रति रक्तदान +250 अंक",
    "next_tier_progress" to "प्लैटिनम स्तर की प्रगति",

    // Verification
    "identity_verification" to "पहचान सत्यापन",
    "phone_verified" to "फोन सत्यापित",
    "email_verified" to "ईमेल सत्यापित",
    "govt_id_verified" to "सरकारी आईडी सत्यापित",
    "verify_email" to "ईमेल सत्यापित करें",
    "verify_id" to "आईडी सत्यापित करें",
    "security_shield" to "जीविक्स सुरक्षा और विश्वास",

    // Wearables
    "wearable_health_sync" to "स्मार्टवॉच स्वास्थ्य सिंक",
    "connected_device" to "जुड़ा हुआ उपकरण",
    "auto_sync_active" to "ऑटो-सिंक सक्रिय",
    "last_synced" to "अंतिम सिंक",
    "connect_watch" to "स्मार्टवॉच कनेक्ट करें",

    // Language
    "change_language" to "भाषा बदलें",
    "select_language" to "अपनी पसंदीदा भाषा चुनें"
  )

  private val gujaratiStrings = mapOf(
    // App & Navigation
    "app_name" to "જીવિક્સ",
    "home" to "હોમ",
    "passport" to "પાસપોર્ટ",
    "sos" to "એસઓએસ",
    "donate" to "રક્તદાન",
    "network" to "નેટવર્ક",
    "ai_assistant" to "એઆઈ સહાયક",
    "leaderboard" to "લીડરબોર્ડ",
    "settings" to "સેટિંગ્સ",
    "notifications" to "સૂચનાઓ",

    // Greeting & Header
    "good_morning" to "શુભ પ્રભાત",
    "digital_identity" to "ડિજિટલ ઓળખ",
    "health_passport" to "જીવિક્સ હેલ્થ પાસપોર્ટ",
    "verified_donor" to "પ્રમાણિત રક્તદાતા",
    "non_donor" to "બિન-દાતા",
    "open_full_passport" to "હેલ્થ પાસપોર્ટ જુઓ",

    // Quick Actions
    "find_blood" to "રક્ત શોધો",
    "hospital" to "હોસ્પિટલ",
    "blood_bank" to "બ્લડ બેંક",
    "find_donor" to "દાતા શોધો",

    // Health Snapshot & Vitals
    "health_snapshot" to "સ્વાસ્થ્ય સ્નેપશોટ",
    "today" to "આજે",
    "heart_rate" to "હૃદયના ધબકારા",
    "blood_pressure" to "બ્લડ પ્રેશર",
    "spo2" to "SpO₂",
    "oxygen" to "ઓક્સિજન",
    "bpm" to "BPM",
    "normal_range" to "સામાન્ય સ્તર",
    "edit_vitals" to "વાઇટલ્સ બદલો ✎",
    "wearable_synced" to "⌚ સ્માર્ટવોચ સિંક થયું",
    "sync_device" to "ડિવાઇસ સિંક કરો",
    "ai_health_insights" to "એઆઈ સ્વાસ્થ્ય વિશ્લેષણ",
    "view_ai_analysis" to "એઆઈ સ્વાસ્થ્ય ટ્રેન્ડ્સ જુઓ",

    // AI Assistant
    "ask_jeevix_ai" to "જીવિક્સ એઆઈ ને પૂછો",
    "ai_assistant_title" to "એઆઈ સહાયક",
    "chat_now" to "વાત કરો",
    "find_matching_donors" to "દાતા શોધો",
    "ai_analyzing" to "હેલ્થ પાસપોર્ટ તપાસી રહ્યા છીએ...",

    // Gamification
    "lifesaver_points" to "જીવનરક્ષક પોઇન્ટ્સ",
    "donor_tier" to "દાતા સ્તર",
    "donor_rank" to "પ્રાદેશિક રેન્ક",
    "milestone_badges" to "સિદ્ધિ બેજ",
    "all_badges" to "બધા બેજ",
    "top_donors" to "ટોચના રક્તદાતા લીડરબોર્ડ",
    "points_for_donation" to "રક્તદાન દીઠ +250 પોઇન્ટ્સ",
    "next_tier_progress" to "પ્લેટિનમ સ્તરની પ્રગતિ",

    // Verification
    "identity_verification" to "ઓળખ ચકાસણી",
    "phone_verified" to "ફોન ચકાસાયેલ છે",
    "email_verified" to "ઈમેલ ચકાસાયેલ છે",
    "govt_id_verified" to "સરકારી આઈડી ચકાસાયેલ છે",
    "verify_email" to "ઈમેલ ચકાસો",
    "verify_id" to "સરકારી આઈડી ચકાસો",
    "security_shield" to "જીવિક્સ સુરક્ષા અને વિશ્વાસ",

    // Wearables
    "wearable_health_sync" to "સ્માર્ટવોચ હેલ્થ સિંક",
    "connected_device" to "જોડાયેલ ઉપકરણ",
    "auto_sync_active" to "ઓટો-સિંક સક્રિય",
    "last_synced" to "છેલ્લું સિંક",
    "connect_watch" to "સ્માર્ટવોચ જોડો",

    // Language
    "change_language" to "ભાષા બદલો",
    "select_language" to "તમારી પસંદગીની ભાષા પસંદ કરો"
  )
}
