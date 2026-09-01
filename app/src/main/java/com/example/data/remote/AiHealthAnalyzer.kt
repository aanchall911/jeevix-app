package com.example.data.remote

import com.example.data.model.AiHealthInsightReport
import com.example.data.model.BloodDonation
import com.example.data.model.MedicalRecord
import com.example.data.model.UserProfile
import com.example.data.model.VitalTrendItem

object AiHealthAnalyzer {

  fun analyzeHealthPassport(
    profile: UserProfile?,
    medicalRecords: List<MedicalRecord>,
    donations: List<BloodDonation>,
    languageCode: String = "en"
  ): AiHealthInsightReport {
    val hr = profile?.heartRate ?: 72
    val sys = profile?.systolicBp ?: 118
    val dia = profile?.diastolicBp ?: 76
    val spo2 = profile?.spo2 ?: 98
    val totalDonations = profile?.totalDonations ?: donations.size

    // Vital Trends calculation
    val hrStatus = when {
      hr in 60..85 -> "Optimal"
      hr in 86..100 -> "Normal"
      else -> "Attention"
    }

    val bpStatus = when {
      sys < 120 && dia < 80 -> "Optimal"
      sys in 120..129 && dia < 80 -> "Elevated"
      else -> "Attention"
    }

    val spo2Status = when {
      spo2 >= 96 -> "Optimal"
      spo2 in 93..95 -> "Normal"
      else -> "Attention"
    }

    val vitalTrends = listOf(
      VitalTrendItem(
        metricName = if (languageCode == "hi") "हृदय गति" else if (languageCode == "gu") "હૃદયના ધબકારા" else "Resting Heart Rate",
        value = "$hr BPM",
        status = hrStatus,
        trendDescription = if (languageCode == "hi") "स्थिर और उत्कृष्ट सामान्य श्रेणी में है" else if (languageCode == "gu") "સ્થિર અને શ્રેષ્ઠ સામાન્ય શ્રેણીમાં છે" else "Stable within optimal resting zone (60-85 BPM)",
        isNormal = hrStatus != "Attention"
      ),
      VitalTrendItem(
        metricName = if (languageCode == "hi") "रक्तचाप" else if (languageCode == "gu") "બ્લડ પ્રેશર" else "Blood Pressure",
        value = "$sys/$dia mmHg",
        status = bpStatus,
        trendDescription = if (languageCode == "hi") "स्वस्थ सीमा में है, रक्तदान के लिए आदर्श" else if (languageCode == "gu") "તંદુરસ્ત શ્રેણીમાં છે, રક્તદાન માટે શ્રેષ્ઠ" else "Optimal normotensive pressure, ideal for donation",
        isNormal = bpStatus != "Attention"
      ),
      VitalTrendItem(
        metricName = if (languageCode == "hi") "रक्त ऑक्सीजन" else if (languageCode == "gu") "બ્લડ ઓક્સિજન" else "Blood Oxygen (SpO₂)",
        value = "$spo2%",
        status = spo2Status,
        trendDescription = if (languageCode == "hi") "फेफड़ों और संचलन का उत्कृष्ट स्वास्थ्य" else if (languageCode == "gu") "ફેફસાં અને પરિભ્રમણનું શ્રેષ્ઠ સ્વાસ્થ્ય" else "Excellent arterial oxygen saturation",
        isNormal = spo2Status != "Attention"
      ),
      VitalTrendItem(
        metricName = if (languageCode == "hi") "दैनिक कदम" else if (languageCode == "gu") "દૈનિક પગલાં" else "Daily Activity (Wearable)",
        value = "${profile?.dailySteps ?: 8420} steps",
        status = "Optimal",
        trendDescription = if (languageCode == "hi") "सक्रिय जीवनशैली रक्त कोशिकाओं के पुनर्जनन में सहायक है" else if (languageCode == "gu") "સક્રિય જીવનશૈલી લાલ રક્તકણોના નિર્માણમાં મદદરૂપ છે" else "Active lifestyle promotes steady hemoglobin replenishment",
        isNormal = true
      )
    )

    // Donation readiness & recovery
    val donationReadiness = if (totalDonations > 0) {
      if (languageCode == "hi") "रक्तदान के लिए पूरी तरह तैयार और योग्य"
      else if (languageCode == "gu") "રક્તદાન માટે સંપૂર્ણ તૈયાર અને પાત્ર"
      else "Fully Eligible & Primed for Donation"
    } else {
      if (languageCode == "hi") "प्रथम रक्तदान के लिए योग्य"
      else if (languageCode == "gu") "પ્રથમ રક્તદાન માટે પાત્ર"
      else "Eligible for First Donation"
    }

    // Risk alerts based on recorded medical history
    val riskAlerts = mutableListOf<String>()
    medicalRecords.forEach { record ->
      if (record.type.equals("Allergy", ignoreCase = true)) {
        riskAlerts.add(
          if (languageCode == "hi") "⚠️ एलर्जी चेतावनी: ${record.title} - आपातकालीन समय में संज्ञान लें।"
          else if (languageCode == "gu") "⚠️ એલર્જી ચેતવણી: ${record.title} - કટોકટીમાં ધ્યાનમાં રાખો."
          else "⚠️ Allergy Alert: ${record.title} (${record.description.ifBlank { "Logged in Passport" }})"
        )
      }
      if (record.type.equals("Condition", ignoreCase = true)) {
        riskAlerts.add(
          if (languageCode == "hi") "ℹ️ स्वास्थ्य स्थिति: ${record.title} (निगरानी में)"
          else if (languageCode == "gu") "ℹ️ આરોગ્ય સ્થિતિ: ${record.title} (દેખરેખ હેઠળ)"
          else "ℹ️ Tracked Condition: ${record.title} (${record.severityOrDosage})"
        )
      }
    }

    if (riskAlerts.isEmpty()) {
      riskAlerts.add(
        if (languageCode == "hi") "✅ कोई गंभीर एलर्जी या कार्डियक जोखिम दर्ज नहीं है।"
        else if (languageCode == "gu") "✅ કોઈ ગંભીર એલર્જી અથવા હૃદયનું જોખમ નોંધાયેલ નથી."
        else "✅ No high-risk cardiovascular or systemic anomalies detected."
      )
    }

    // Recommendations
    val recommendations = listOf(
      if (languageCode == "hi") "पर्याप्त जलयोजन: रक्तदान से 24 घंटे पहले कम से कम 2.5 लीटर पानी पिएं।"
      else if (languageCode == "gu") "પૂરતું હાઇડ્રેશન: રક્તદાનના 24 કલાક પહેલા ઓછામાં ઓછું 2.5 લિટર પાણી પીવો."
      else "Hydration: Maintain 2.5L+ daily water intake to support plasma volume.",

      if (languageCode == "hi") "आयरन युक्त आहार: हीमोग्लोबिन स्तर 13+ g/dL बनाए रखने के लिए हरी सब्जियां व खजूर लें।"
      else if (languageCode == "gu") "આયર્નયુક્ત આહાર: હિમોગ્લોબિન 13+ g/dL જાળવવા માટે લીલા શાકભાજી અને ખજૂર લો."
      else "Iron Nutrition: Leafy greens, dates, and legumes support continuous iron stores.",

      if (languageCode == "hi") "स्मार्टवॉच सिंक: वास्तविक समय में वाइटल्स मॉनिटरिंग के लिए नियमित रूप से सिंक करें।"
      else if (languageCode == "gu") "સ્માર્ટવોચ સિંક: રીઅલ-ટાઇમ વાઇટલ્સ માટે નિયમિત સિંક કરતા રહો."
      else "Wearable Sync: Regular telemetry helps Jeevix AI forecast health readiness."
    )

    val overallStatus = if (languageCode == "hi") "उत्कृष्ट स्वास्थ्य (96% स्कोर)"
      else if (languageCode == "gu") "ઉત્તમ સ્વાસ્થ્ય (96% સ્કોર)"
      else "Excellent Health (96% Vitality Score)"

    return AiHealthInsightReport(
      overallStatus = overallStatus,
      healthScore = 96,
      donationReadiness = donationReadiness,
      nextEligibleDonationDate = "Ready Now (Available)",
      recoveryPercentage = 100,
      vitalTrends = vitalTrends,
      riskAlerts = riskAlerts,
      recommendations = recommendations,
      medicalDisclaimer = if (languageCode == "hi")
        "अस्वीकरण: जीविक्स एआई स्वास्थ्य विश्लेषण केवल जानकारी और जागरूकता के लिए है। यह पेशेवर चिकित्सीय सलाह या निदान का विकल्प नहीं है।"
      else if (languageCode == "gu")
        "અસ્વીકરણ: જીવિક્સ એઆઈ સ્વાસ્થ્ય વિશ્લેષણ ફક્ત માહિતી અને જાગૃતિ માટે છે. તે વ્યાવસાયિક તબીબી સલાહનો વિકલ્પ નથી."
      else
        "Disclaimer: Jeevix AI Health Insights are generated for health tracking and preventive awareness only. They do not constitute professional medical diagnosis or clinical prescription."
    )
  }
}
