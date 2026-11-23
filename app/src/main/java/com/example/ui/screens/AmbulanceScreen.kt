package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmergencyAlertHelper
import com.example.ui.AppLang
import com.example.ui.AppLanguage
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.tr

@Composable
fun AmbulanceScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currentLocation by viewModel.currentLocation.collectAsState()

    val emergencyTypes = listOf(
        Triple("Severe Injury / Accident", "गंभीर चोट / दुर्घटना", "গুরুতর আঘাত / দুর্ঘটনা"),
        Triple("Loss of Consciousness", "बेहोशी", "অজ্ঞান হয়ে যাওয়া"),
        Triple("Difficulty Breathing", "सांस लेने में तकलीफ", "শ্বাসকষ্ট"),
        Triple("Severe Pain / Bleeding", "तेज़ दर्द / रक्तस्राव", "তীব্র ব্যথা / রক্তপাত"),
        Triple("Maternal / Labor Onset", "प्रसव / मातृत्व आपातकाल", "প্রসবকালীন / মাতৃত্বকালীন জরুরি অবস্থা"),
        Triple("Assault / Trauma", "हमला / ट्रॉमा", "হামলা / ট্রমা")
    )

    var selectedIndex by remember { mutableIntStateOf(0) }
    var additionalNotes by remember { mutableStateOf("") }
    var alertSentFeedback by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr(
                    "Medical & Ambulance",
                    "मेडिकल और एम्बुलेंस",
                    "মেডিকেল ও অ্যাম্বুলেন্স"
                ),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("ambulance_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Direct Dials Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("ambulance_dial_112_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tr(
                                "DIAL 112\n(UNIFIED)",
                                "112 डायल करें\n(आपातकाल)",
                                "112 ডায়াল করুন\n(জরুরি)"
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "108") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("ambulance_dial_108_button")
                    ) {
                        Icon(Icons.Default.MedicalServices, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tr(
                                "DIAL 108\n(AMBULANCE)",
                                "108 डायल करें\n(एम्बुलेंस)",
                                "108 ডায়াল করুন\n(অ্যাম্বুলেন্স)"
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Maternal Transport 102
            item {
                OutlinedButton(
                    onClick = { EmergencyAlertHelper.dialNumber(context, "102") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ambulance_dial_102_button")
                ) {
                    Text(
                        text = tr(
                            "Dial 102 (Maternal & Infant Ambulance Transport)",
                            "102 डायल करें (मातृ एवं शिशु एम्बुलेंस सेवा)",
                            "102 ডায়াল করুন (মাতৃ ও শিশু অ্যাম্বুলেন্স পরিষেবা)"
                        ),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Realism Disclosure
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = tr(
                                "Important: Tapping the call buttons dials the verified government ambulance helpline. An ambulance is only dispatched once you connect with the official dispatcher and confirm your condition and location.",
                                "महत्वपूर्ण: कॉल बटन दबाने पर सत्यापित सरकारी एम्बुलेंस हेल्पलाइन डायल होती है। एम्बुलेंस तभी भेजी जाती है जब आप ऑपरेटर से जुड़कर अपनी स्थिति और लोकेशन की पुष्टि करते हैं।",
                                "গুরুত্বপূর্ণ: কল বাটনে ট্যাপ করলে যাচাইকৃত সরকারি অ্যাম্বুলেন্স হেল্পলাইনে কল যায়। অপারেটরের সাথে যুক্ত হয়ে আপনার অবস্থা ও অবস্থান নিশ্চিত করার পরেই অ্যাম্বুলেন্স পাঠানো হয়।"
                            ),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Nearby Hospitals on Maps
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalHospital,
                                contentDescription = null,
                                tint = Color(0xFFE11D48),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr(
                                    "Locate Nearest Hospitals & Trauma Centers",
                                    "नज़दीकी अस्पताल और ट्रॉमा सेंटर खोजें",
                                    "নিকটতম হাসপাতাল ও ট্রমা সেন্টার খুঁজুন"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = tr(
                                "Open Google Maps navigation to verified hospitals and emergency rooms nearest to your location.",
                                "अपनी लोकेशन के पास सत्यापित अस्पतालों और इमरजेंसी कक्षों के लिए Google Maps खोलें।",
                                "আপনার অবস্থানের কাছাকাছি যাচাইকৃত হাসপাতাল ও জরুরি বিভাগের জন্য Google Maps খুলুন।"
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                EmergencyAlertHelper.openMapSearch(context, "hospital emergency", currentLocation)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("find_hospitals_button")
                        ) {
                            Icon(Icons.Default.LocalHospital, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                tr(
                                    "Open Nearby Hospitals on Google Maps",
                                    "Google Maps पर नज़दीकी अस्पताल देखें",
                                    "Google Maps-এ নিকটতম হাসপাতাল দেখুন"
                                )
                            )
                        }
                    }
                }
            }

            // Emergency Medical Info Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = tr(
                                "Medical Emergency Details Form",
                                "मेडिकल इमरजेंसी विवरण फॉर्म",
                                "মেডিকেল জরুরি বিবরণ ফর্ম"
                            ),
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tr(
                                "Specify symptoms to quickly broadcast to your emergency contacts alongside your GPS pin:",
                                "अपने आपातकालीन संपर्कों को GPS लोकेशन के साथ तुरंत भेजने के लिए लक्षण चुनें:",
                                "আপনার GPS অবস্থানের সাথে জরুরি যোগাযোগগুলোতে দ্রুত পাঠাতে লক্ষণ নির্বাচন করুন:"
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Chips
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            emergencyTypes.indices.chunked(2).forEach { rowIndices ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowIndices.forEach { idx ->
                                        val (en, hi, bn) = emergencyTypes[idx]
                                        val labelText = when (AppLanguage.current) {
                                            AppLang.HINDI -> hi
                                            AppLang.BENGALI -> bn
                                            AppLang.ENGLISH -> en
                                        }
                                        FilterChip(
                                            selected = selectedIndex == idx,
                                            onClick = { selectedIndex = idx },
                                            label = { Text(labelText, fontSize = 11.sp) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (rowIndices.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = additionalNotes,
                            onValueChange = { additionalNotes = it },
                            label = {
                                Text(
                                    tr(
                                        "Any Allergies, Blood Group, or Specific Landmark",
                                        "एलर्जी, ब्लड ग्रुप या नज़दीकी पहचान चिह्न",
                                        "অ্যালার্জি, রক্তের গ্রুপ বা নিকটবর্তী ল্যান্ডমার্ক"
                                    )
                                )
                            },
                            placeholder = {
                                Text(
                                    tr(
                                        "e.g. Diabetic, Apartment 3B, Severe bleeding",
                                        "जैसे: शुगर, अपार्टमेंट 3B, अधिक रक्तस्राव",
                                        "যেমন: ডায়াবেটিস, ফ্ল্যাট 3B, অতিরিক্ত রক্তপাত"
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("medical_notes_input"),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (alertSentFeedback != null) {
                            Text(
                                text = alertSentFeedback ?: "",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Button(
                            onClick = {
                                val selectedEn = emergencyTypes[selectedIndex].first
                                val text = "MEDICAL EMERGENCY ALERT: I require an ambulance. Condition: $selectedEn. Notes: ${additionalNotes.ifBlank { "None" }}."
                                viewModel.triggerSosAlertImmediate(text)
                                alertSentFeedback = tr(
                                    "Medical alert shared with contacts.",
                                    "मेडिकल अलर्ट संपर्कों के साथ साझा किया गया।",
                                    "মেডিকেল অ্যালার্ট যোগাযোগগুলোর সাথে শেয়ার করা হয়েছে।"
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("share_medical_alert_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                tr(
                                    "Share Medical Details & GPS with Contacts",
                                    "मेडिकल विवरण और GPS संपर्कों को भेजें",
                                    "মেডিকেল বিবরণ ও GPS যোগাযোগগুলোতে পাঠান"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}