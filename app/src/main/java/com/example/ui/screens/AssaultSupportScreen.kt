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
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
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
fun AssaultSupportScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val context = LocalContext.current
    val currentLocation by viewModel.currentLocation.collectAsState()

    var incidentText by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }
    var perpetratorDetails by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("Case of Assault — Immediate Aid", "हमला / हिंसा — तत्काल सहायता"),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("assault_support_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Absolute #1 Priority: Immediate Physical Safety & Medical Attention
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CrimsonPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HealthAndSafety,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = tr(
                                    "FIRST PRIORITY: PHYSICAL SAFETY & MEDICAL CARE",
                                    "पहली प्राथमिकता: शारीरिक सुरक्षा और चिकित्सा सहायता"
                                ),
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tr(
                                "You are NOT required to document evidence or fill any forms right now. Your immediate health and protection come first.",
                                "अभी सबूत लिखने या कोई फॉर्म भरने की आवश्यकता नहीं है। आपका स्वास्थ्य और सुरक्षा सबसे पहले है।"
                            ),
                            color = Color(0xFFFFDAD6),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("assault_call_112_button")
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, tint = CrimsonPrimary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = tr("DIAL 112", "112 डायल करें"),
                                    color = CrimsonPrimary,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Button(
                                onClick = { EmergencyAlertHelper.dialNumber(context, "108") },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2)),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("assault_call_108_button")
                            ) {
                                Icon(Icons.Default.LocalHospital, contentDescription = null, tint = CrimsonPrimary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = tr("DIAL 108", "108 डायल करें"),
                                    color = CrimsonPrimary,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // Legal Rights & Zero FIR Information
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr(
                                    "Your Legal Rights & Medical Rights in India",
                                    "भारत में आपके कानूनी और चिकित्सा अधिकार"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tr(
                                "• ZERO FIR: You can register an FIR at ANY police station, regardless of where the incident took place.\n• FREE MEDICAL EXAMINATION & TREATMENT: Under Section 357C of CrPC / BNSS, all government and private hospitals are legally bound to provide immediate, free first aid and medical care.\n• ONE STOP CENTERS (SAKHI): Integrated medical, legal, psychological, and temporary shelter support under one roof.\n• NALSA FREE LEGAL AID: Dial 15100 for toll-free legal advice by government-appointed legal advocates.",
                                "• ज़ीरो FIR: घटना कहीं भी हुई हो, आप किसी भी पुलिस स्टेशन में FIR दर्ज करा सकती हैं।\n• निःशुल्क चिकित्सा जांच और इलाज: CrPC की धारा 357C / BNSS के तहत सभी सरकारी और निजी अस्पताल तुरंत और मुफ्त प्राथमिक उपचार देने के लिए कानूनी रूप से बाध्य हैं।\n• वन स्टॉप सेंटर (सखी): एक ही छत के नीचे चिकित्सा, कानूनी, मनोवैज्ञानिक और अस्थायी आश्रय सहायता।\n• NALSA मुफ्त कानूनी सहायता: सरकारी वकीलों से निःशुल्क कानूनी सलाह के लिए 15100 डायल करें।"
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = { EmergencyAlertHelper.dialNumber(context, "15100") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                tr(
                                    "Call 15100 (National Legal Services Authority - NALSA)",
                                    "15100 पर कॉल करें (राष्ट्रीय विधिक सेवा प्राधिकरण - NALSA)"
                                )
                            )
                        }
                    }
                }
            }

            // Medical Evidence Preservation Advice
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = tr(
                                "⚠️ Medical Evidence Preservation (If Safe)",
                                "⚠️ चिकित्सा सबूत सुरक्षित रखना (यदि सुरक्षित हो)"
                            ),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF78350F)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tr(
                                "• If planning a medical exam, try not to wash, bathe, or change clothes beforehand as forensic evidence can be gathered.\n• If clothes are changed, store unwashed clothes in a clean paper bag.\n• Again: Your physical health and immediate emotional safety come first before any evidence.",
                                "• यदि मेडिकल जांच करवानी है, तो पहले नहाने, धोने या कपड़े बदलने से बचें ताकि फॉरेंसिक सबूत सुरक्षित रह सकें।\n• यदि कपड़े बदलें, तो बिना धोए कपड़े एक साफ कागज़ के बैग में रखें।\n• याद रखें: किसी भी सबूत से पहले आपका शारीरिक स्वास्थ्य और मानसिक सुरक्षा सबसे महत्वपूर्ण है।"
                            ),
                            fontSize = 11.sp,
                            color = Color(0xFF78350F),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Optional Private Note Filing (Zero pressure)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = SafeGreen, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr("Optional Private Incident Note", "वैकल्पिक निजी घटना नोट"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tr(
                                "Only if you feel calm and ready. Stored strictly on device.",
                                "केवल तभी लिखें जब आप शांत और तैयार महसूस करें। यह केवल आपके फोन में सुरक्षित रहेगा।"
                            ),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = incidentText,
                            onValueChange = { incidentText = it },
                            label = {
                                Text(tr("Private notes / Account of event", "निजी नोट्स / घटना का विवरण"))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("assault_notes_input"),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = locationInput,
                            onValueChange = { locationInput = it },
                            label = { Text(tr("Location Landmark", "स्थान / पहचान चिह्न")) },
                            placeholder = {
                                Text(currentLocation?.toFormattedCoordinates() ?: tr("Area name", "क्षेत्र का नाम"))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (isSaved) {
                            Text(
                                text = tr(
                                    "Note saved locally to your device vault.",
                                    "नोट आपके फोन के सुरक्षित वॉल्ट में सहेजा गया।"
                                ),
                                color = SafeGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (incidentText.isNotBlank()) {
                                    viewModel.saveIncidentReport(
                                        category = "Assault",
                                        description = incidentText,
                                        locationText = locationInput,
                                        perpetratorDetails = perpetratorDetails,
                                        evidenceNotes = "Assault incident report filed offline"
                                    )
                                    isSaved = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_assault_report_button")
                        ) {
                            Text(
                                tr(
                                    "Save Locally for Personal / Legal Reference",
                                    "निजी / कानूनी संदर्भ के लिए फोन में सहेजें"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}