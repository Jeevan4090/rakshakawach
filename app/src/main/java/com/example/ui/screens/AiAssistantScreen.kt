package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.EmergencyAlertHelper
import com.example.data.GoogleAuthHelper
import com.example.data.MessageSender
import com.example.ui.AppLanguage
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.tr
import kotlinx.coroutines.launch

@Composable
fun AiAssistantScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val messages by viewModel.chatMessages.collectAsState()
    val isLoading by viewModel.isAiLoading.collectAsState()
    val signedInEmail by viewModel.signedInEmail.collectAsState()
    var inputText by remember { mutableStateOf("") }
    var isSigningIn by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (signedInEmail == null) {
            val saved = GoogleAuthHelper.getSavedUser(context)
            if (saved != null) {
                viewModel.setGoogleAuth(saved.idToken, saved.email)
            }
        }
    }

    val quickPrompts = if (AppLanguage.isHindi) {
        listOf(
            "कोई मेरा पीछा कर रहा है।",
            "मुझे धमकी भरे संदेश मिल रहे हैं।",
            "कार्यस्थल पर उत्पीड़न की शिकायत कैसे करें?",
            "साइबर स्टॉकिंग की रिपोर्ट कैसे करें?",
            "मुझे नज़दीकी पुलिस स्टेशन खोजने में मदद चाहिए।"
        )
    } else {
        listOf(
            "Someone is following me.",
            "I am receiving threatening messages.",
            "How do I report workplace harassment?",
            "How do I report cyberstalking?",
            "I need help finding a nearby police station."
        )
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("Chat with Rakshak Assistant", "रक्षक असिस्टेंट से चैट करें"),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("ai_assistant_screen")
        ) {
            // Mandatory AI Tool Disclaimer Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr(
                            "Notice: Raksha Assistant is an AI informational tool. It is NOT a replacement for emergency responders, police, legal advocates, or medical doctors. If in immediate danger, dial 112 below.",
                            "सूचना: रक्षा असिस्टेंट एक AI सूचनात्मक टूल है। यह पुलिस, आपातकालीन सेवाओं, वकील या डॉक्टर का विकल्प नहीं है। तुरंत खतरे में हों तो 112 डायल करें।"
                        ),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }

            // Google Sign-In Status Bar (for Live Backend AI)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (signedInEmail == null) {
                    Text(
                        text = tr("Offline mode (sign in for Live AI)", "ऑफ़लाइन मोड (लाइव AI के लिए साइन इन करें)"),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = {
                            if (!isSigningIn) {
                                isSigningIn = true
                                scope.launch {
                                    val auth = GoogleAuthHelper.signIn(context)
                                    isSigningIn = false
                                    if (auth != null) {
                                        viewModel.setGoogleAuth(auth.idToken, auth.email)
                                        Toast.makeText(
                                            context,
                                            tr("Signed in as ${auth.email}", "${auth.email} से साइन इन हुआ"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            tr("Google Sign-In cancelled or failed", "Google साइन-इन रद्द या विफल हुआ"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        enabled = !isSigningIn,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isSigningIn) {
                                tr("Signing in...", "साइन इन हो रहा है...")
                            } else {
                                tr("Sign in with Google", "Google से साइन इन करें")
                            },
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Text(
                        text = tr("Live AI active ($signedInEmail)", "लाइव AI सक्रिय ($signedInEmail)"),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    TextButton(
                        onClick = {
                            GoogleAuthHelper.signOut(context)
                            viewModel.signOut()
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(tr("Sign out", "साइन आउट"), fontSize = 11.sp)
                    }
                }
            }

            // Quick Prompt Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                items(quickPrompts) { prompt ->
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.sendChatMessage(prompt) },
                        label = { Text(prompt, fontSize = 11.sp) },
                        modifier = Modifier.testTag("quick_prompt_${prompt.take(10)}")
                    )
                }
            }

            // Message Stream
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(
                        message = msg,
                        onCallHelpline = { number ->
                            EmergencyAlertHelper.dialNumber(context, number)
                        }
                    )
                }

                if (isLoading) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr(
                                    "Raksha Assistant is evaluating safety guidance...",
                                    "रक्षा असिस्टेंट सुरक्षा मार्गदर्शन तैयार कर रहा है..."
                                ),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Bottom Input Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(tr("Ask Raksha Assistant for safety advice...", "सुरक्षा सलाह के लिए पूछें..."))
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        maxLines = 3,
                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendChatMessage(inputText)
                                inputText = ""
                            }
                        },
                        enabled = inputText.isNotBlank(),
                        modifier = Modifier.testTag("send_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = tr("Send", "भेजें"),
                            tint = if (inputText.isNotBlank()) CrimsonPrimary else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    onCallHelpline: (String) -> Unit
) {
    val isUser = message.sender == MessageSender.USER
    val bubbleBg = if (isUser) Color(0xFFF0D4F7) else Color(0xFFFFFFFF)
    val bubbleText = Color(0xFF241729)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 2.dp,
                bottomEnd = if (isUser) 2.dp else 14.dp
            ),
            colors = CardDefaults.cardColors(containerColor = bubbleBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (message.isUrgentEmergency) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CrimsonPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tr("POTENTIAL DANGER DETECTED", "संभावित खतरा पाया गया"),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = bubbleText,
                    lineHeight = 18.sp
                )

                if (message.recommendedHelpline != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onCallHelpline(message.recommendedHelpline) },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("ai_dial_helpline_${message.recommendedHelpline}")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tr("CALL ${message.recommendedHelpline} NOW", "अभी ${message.recommendedHelpline} पर कॉल करें"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}