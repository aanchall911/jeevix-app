package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.ui.components.LanguageSelectionDialog
import com.example.ui.theme.JeevixAiIndigo
import com.example.ui.theme.JeevixAiPink
import com.example.ui.theme.JeevixAiViolet
import com.example.ui.theme.JeevixCrimson
import com.example.ui.theme.JeevixCrimsonDark
import com.example.ui.theme.JeevixTeal
import com.example.ui.util.LocalizationManager
import com.example.viewmodel.JeevixViewModel

@Composable
fun JeevixAiAssistantScreen(
  viewModel: JeevixViewModel,
  initialPrompt: String? = null,
  modifier: Modifier = Modifier
) {
  val chatMessages by viewModel.chatMessages.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()
  val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()

  val currentLang = userProfile?.selectedLanguage ?: "en"

  var inputQuery by remember { mutableStateOf(initialPrompt ?: "") }
  val listState = rememberLazyListState()

  LaunchedEffect(chatMessages.size) {
    if (chatMessages.isNotEmpty()) {
      listState.animateScrollToItem(chatMessages.size - 1)
    }
  }

  LaunchedEffect(initialPrompt) {
    if (!initialPrompt.isNullOrBlank()) {
      viewModel.sendChatMessage(initialPrompt)
      inputQuery = ""
    }
  }

  if (showLanguageDialog) {
    LanguageSelectionDialog(
      currentLanguage = currentLang,
      onSelectLanguage = { lang -> viewModel.setLanguage(lang) },
      onDismiss = { viewModel.showLanguageDialog(false) }
    )
  }

  val suggestedPrompts = when (currentLang) {
    "hi" -> listOf(
      "मेरे स्वास्थ्य पासपोर्ट और ट्रेंड्स का विश्लेषण करें",
      "क्या मैं आज रक्तदान करने के लिए योग्य हूँ?",
      "मेरे वियरेबल वाइटल्स और एलर्जी की जांच करें",
      "रक्तदान की तैयारी के लिए टिप्स बताएं"
    )
    "gu" -> listOf(
      "મારા હેલ્થ પાસપોર્ટ અને ટ્રેન્ડ્સ તપાસો",
      "શું હું આજે રક્તદાન માટે યોગ્ય છું?",
      "મારા વિયરેબલ વાઇટલ્સ અને એલર્જી તપાસો",
      "ઇમરજન્સીમાં શું કરવું?"
    )
    else -> listOf(
      "Analyze my health passport trends & risks",
      "Check my blood donation readiness",
      "Summarize wearable vitals & allergies",
      "Tips to prepare for blood donation",
      "What to do in a medical emergency?"
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .navigationBarsPadding()
      .imePadding()
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(JeevixAiViolet, JeevixAiPink))),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "✨", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = LocalizationManager.get("jeevix_ai_assistant", currentLang),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = JeevixAiViolet.copy(alpha = 0.15f)
            ) {
              Text(
                text = "Passport-Grounded",
                style = MaterialTheme.typography.labelSmall,
                color = JeevixAiViolet,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
          Text(
            text = "Clinical intelligence grounded in your Health Passport",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Language Switcher
      Surface(
        onClick = { viewModel.showLanguageDialog(true) },
        shape = RoundedCornerShape(12.dp),
        color = JeevixTeal.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, JeevixTeal.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Language, contentDescription = null, tint = JeevixTeal, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (currentLang == "hi") "हिंदी" else if (currentLang == "gu") "ગુજરાતી" else "EN",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = JeevixTeal
          )
        }
      }
    }

    // Chat Message Stream
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth(),
      contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(chatMessages) { message ->
        ChatMessageBubble(message = message)
      }
    }

    // Preset Prompt Chips
    LazyRow(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(suggestedPrompts) { prompt ->
        Surface(
          onClick = {
            viewModel.sendChatMessage(prompt)
          },
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          border = androidx.compose.foundation.BorderStroke(1.dp, JeevixAiViolet.copy(alpha = 0.3f))
        ) {
          Text(
            text = prompt,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
          )
        }
      }
    }

    // Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = inputQuery,
        onValueChange = { inputQuery = it },
        placeholder = { Text(LocalizationManager.get("ask_jeevix_ai", currentLang)) },
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = JeevixAiViolet
        ),
        maxLines = 3
      )

      Spacer(modifier = Modifier.width(8.dp))

      IconButton(
        onClick = {
          if (inputQuery.isNotBlank()) {
            viewModel.sendChatMessage(inputQuery.trim())
            inputQuery = ""
          }
        },
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(JeevixCrimson)
      ) {
        Icon(
          imageVector = Icons.Default.Send,
          contentDescription = "Send",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }
    }

    // Medical Disclaimer Footer
    Surface(
      shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(12.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = LocalizationManager.get("ai_disclaimer", currentLang),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 9.sp,
          maxLines = 1
        )
      }
    }
  }
}

@Composable
fun ChatMessageBubble(message: ChatMessage) {
  val isUser = message.sender == MessageSender.USER

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
  ) {
    if (!isUser) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(CircleShape)
          .background(Brush.linearGradient(listOf(JeevixAiViolet, JeevixAiPink))),
        contentAlignment = Alignment.Center
      ) {
        Text(text = "🤖", fontSize = 14.sp)
      }
      Spacer(modifier = Modifier.width(8.dp))
    }

    Card(
      shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = if (isUser) 16.dp else 4.dp,
        bottomEnd = if (isUser) 4.dp else 16.dp
      ),
      colors = CardDefaults.cardColors(
        containerColor = if (isUser) JeevixCrimson else MaterialTheme.colorScheme.surfaceVariant
      ),
      modifier = Modifier.widthIn(max = 300.dp)
    ) {
      Box(modifier = Modifier.padding(14.dp)) {
        if (message.isThinking) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(
              modifier = Modifier.size(16.dp),
              color = JeevixAiViolet,
              strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = message.text,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        } else {
          Text(
            text = message.text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface,
            lineHeight = 20.sp
          )
        }
      }
    }
  }
}
