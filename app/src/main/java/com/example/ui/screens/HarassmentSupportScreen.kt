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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.SafeGreen
import com.example.ui.tr

@Composable
fun HarassmentSupportScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val context = LocalContext.current
    val currentLocation by viewModel.currentLocation.collectAsState()

    var incidentText by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }
    var perpetratorDetails by remember { mutableStateOf("") }
    var evidenceNotes by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("Harassment Support", "उत्पीड़न सहायता"),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("harassment_support_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Immediate 112 Dial
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("harassment_dial_112_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tr("DIAL 112 POLICE", "112 पुलिस डायल करें"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "181") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9333EA)),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("harassment_dial_181_button")
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tr("181 WOMEN HELPLINE", "181 महिला हेल्पलाइन"),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Supportive, Non-judgmental Message
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E8FF)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = tr(
                                "💜 You Are Not Alone. Your Safety is Valid.",
                                "💜 आप अकेली नहीं हैं। आपकी सुरक्षा सर्वोपरि है।"
                            ),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF581C87)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tr(
                                "Harassment is never your fault. Whether street stalking, verbal intimidation, or persistent harassment, you have full legal rights under Indian Law (Bharatiya Nyaya Sanhita). Take care of your immediate safety first.",
                                "उत्पीड़न कभी भी आपकी गलती नहीं है। चाहे सड़क पर पीछा करना हो, मौखिक धमकी हो या लगातार उत्पीड़न, भारतीय कानून (भारतीय न्याय संहिता) के तहत आपके पास पूरे कानूनी अधिकार हैं। सबसे पहले अपनी तत्काल सुरक्षा का ध्यान रखें।"
                            ),
                            fontSize = 12.sp,
                            color = Color(0xFF6B21A8),
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Evidence Preservation Guidance Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr("How to Preserve Evidence Safely", "सबूत सुरक्षित रूप से कैसे सहेजें"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tr(
                                "• Keep original message chains, dates, times, and phone numbers.\n• Do not alter, edit, or delete screenshots or chat logs.\n• Note physical appearance, clothing, vehicle license plate numbers, or directions.\n• Record witness details or names of nearby shopkeepers if safe to do so.",
                                "• मूल संदेश, तारीख, समय और फोन नंबर सुरक्षित रखें।\n• स्क्रीनशॉट या चैट लॉग में कोई बदलाव न करें और न ही उन्हें हटाएँ।\n• व्यक्ति का हुलिया, कपड़े, वाहन का नंबर या दिशा नोट कर लें।\n• यदि सुरक्षित हो, तो गवाहों या आसपास के दुकानदारों के नाम लिख लें।"
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Confidential Local Incident Report Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = SafeGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr(
                                    "Confidential On-Device Incident Record",
                                    "गोपनीय घटना रिकॉर्ड (केवल फोन में)"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tr(
                                "Saved privately only on your device storage. Never automatically uploaded without your consent.",
                                "यह केवल आपके फोन में निजी रूप से सहेजा जाता है। आपकी अनुमति के बिना कहीं अपलोड नहीं होता।"
                            ),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = incidentText,
                            onValueChange = { incidentText = it },
                            label = { Text(tr("What happened? (Description)", "क्या हुआ? (विवरण)")) },
                            placeholder = {
                                Text(
                                    tr(
                                        "Describe the harassment event, time, what was said or done...",
                                        "उत्पीड़न की घटना, समय और क्या कहा या किया गया, लिखें..."
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("harassment_description_input"),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = locationInput,
                            onValueChange = { locationInput = it },
                            label = { Text(tr("Location (Street, Landmark, Metro)", "स्थान (सड़क, पहचान चिह्न, मेट्रो)")) },
                            placeholder = {
                                Text(
                                    currentLocation?.toFormattedCoordinates() ?: tr(
                                        "e.g., Near City Center Gate 2",
                                        "जैसे: सिटी सेंटर गेट 2 के पास"
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = perpetratorDetails,
                            onValueChange = { perpetratorDetails = it },
                            label = {
                                Text(
                                    tr(
                                        "Perpetrator details (If known / observable)",
                                        "आरोपी का विवरण (यदि ज्ञात या देखा गया हो)"
                                    )
                                )
                            },
                            placeholder = {
                                Text(
                                    tr(
                                        "e.g. Red bike, black helmet, approximate age",
                                        "जैसे: लाल बाइक, काला हेलमेट, अनुमानित उम्र"
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = evidenceNotes,
                            onValueChange = { evidenceNotes = it },
                            label = {
                                Text(
                                    tr(
                                        "Evidence notes (e.g. CCTV nearby, witness)",
                                        "सबूत नोट्स (जैसे: पास में CCTV, गवाह)"
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isSaved) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SafeGreen)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = tr(
                                        "Incident report saved securely to your private vault.",
                                        "घटना रिपोर्ट आपके निजी वॉल्ट में सुरक्षित रूप से सहेजी गई।"
                                    ),
                                    color = SafeGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (incidentText.isNotBlank()) {
                                        viewModel.saveIncidentReport(
                                            category = "Harassment",
                                            description = incidentText,
                                            locationText = locationInput,
                                            perpetratorDetails = perpetratorDetails,
                                            evidenceNotes = evidenceNotes
                                        )
                                        isSaved = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("save_harassment_report_button")
                            ) {
                                Text(tr("Save to Private Vault", "निजी वॉल्ट में सहेजें"))
                            }

                            OutlinedButton(
                                onClick = onNavigateToReports,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(tr("View All Reports", "सभी रिपोर्ट देखें"))
                            }
                        }
                    }
                }
            }
        }
    }
}