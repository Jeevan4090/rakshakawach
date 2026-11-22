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
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun ChildSafetyScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val context = LocalContext.current

    var situationNotes by remember { mutableStateOf("") }
    var locationArea by remember { mutableStateOf("") }
    var actionTakenNotes by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("Child Safety & POCSO Help", "बाल सुरक्षा और POCSO सहायता"),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("child_safety_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Priority Dials: 1098 & 112
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "1098") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("child_dial_1098_button")
                    ) {
                        Icon(Icons.Default.ChildCare, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tr("DIAL 1098\n(CHILDLINE)", "1098 डायल करें\n(चाइल्डलाइन)"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("child_dial_112_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tr("DIAL 112\n(POLICE ERSS)", "112 डायल करें\n(पुलिस ERSS)"),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // Mandatory Child Privacy & Non-Disclosure Notice
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.PrivacyTip,
                            contentDescription = null,
                            tint = Color(0xFF1D4ED8),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = tr(
                                    "Strict Child Privacy & Legal Confidentiality",
                                    "बच्चों की गोपनीयता और कानूनी सुरक्षा"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF1E3A8A)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tr(
                                    "Under Section 23 of the POCSO Act, publishing or broadcasting the identity, school, photograph, or address of a child victim/survivor is strictly prohibited by law. Prioritize the child's physical safety, warmth, and immediate protection.",
                                    "POCSO अधिनियम की धारा 23 के तहत, पीड़ित बच्चे की पहचान, स्कूल, फोटो या पता प्रकाशित या प्रसारित करना कानूनन सख्त मना है। बच्चे की शारीरिक सुरक्षा, स्नेह और तत्काल संरक्षण को प्राथमिकता दें।"
                                ),
                                fontSize = 11.sp,
                                color = Color(0xFF1E40AF),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // POCSO Reporting Guidelines
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr(
                                    "What to do if a child is in danger",
                                    "यदि कोई बच्चा खतरे में हो तो क्या करें"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tr(
                                "1. REMOVE FROM IMMEDIATE DANGER: Ensure the child is in a safe room with trusted adults.\n2. CALL 1098: Childline provides 24x7 emergency outreach, medical care, counseling, and Child Welfare Committee (CWC) escalation.\n3. CALL 112: If an offender is present or active violence is occurring, request immediate police arrival.\n4. ONLINE EXPLOITATION: Report child sexual abuse material (CSAM) immediately at cybercrime.gov.in (National Cyber Crime Portal).",
                                "1. तत्काल खतरे से दूर करें: सुनिश्चित करें कि बच्चा विश्वसनीय बड़ों के साथ एक सुरक्षित कमरे में हो।\n2. 1098 पर कॉल करें: चाइल्डलाइन 24x7 आपातकालीन सहायता, चिकित्सा देखभाल, परामर्श और बाल कल्याण समिति (CWC) की मदद प्रदान करती है।\n3. 112 पर कॉल करें: यदि अपराधी आसपास हो या हिंसा हो रही हो, तो तुरंत पुलिस बुलाएँ।\n4. ऑनलाइन शोषण: बाल यौन शोषण सामग्री (CSAM) की तुरंत cybercrime.gov.in (राष्ट्रीय साइबर क्राइम पोर्टल) पर रिपोर्ट करें।"
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                EmergencyAlertHelper.openWebUrl(context, "https://cybercrime.gov.in/")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                tr(
                                    "Report CSAM on cybercrime.gov.in",
                                    "cybercrime.gov.in पर CSAM की रिपोर्ट करें"
                                )
                            )
                        }
                    }
                }
            }

            // Secure Confidential Incident Note
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
                                text = tr(
                                    "Secure Child Concern Note (Offline)",
                                    "सुरक्षित बाल सुरक्षा नोट (ऑफ़लाइन)"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tr(
                                "Kept strictly offline on device. Do not record unnecessary graphic descriptions.",
                                "केवल आपके फोन में ऑफ़लाइन सुरक्षित। अनावश्यक संवेदनशील विवरण न लिखें।"
                            ),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = situationNotes,
                            onValueChange = { situationNotes = it },
                            label = {
                                Text(tr("Nature of Concern / Observed Situation", "चिंता का विषय / देखी गई स्थिति"))
                            },
                            placeholder = {
                                Text(tr("e.g. Distress observed, neglect, harassment...", "जैसे: तनाव, उपेक्षा, उत्पीड़न..."))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("child_safety_notes_input"),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = locationArea,
                            onValueChange = { locationArea = it },
                            label = { Text(tr("Locality / Area Landmark", "क्षेत्र / पहचान चिह्न")) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = actionTakenNotes,
                            onValueChange = { actionTakenNotes = it },
                            label = {
                                Text(
                                    tr(
                                        "Actions Taken (e.g., Contacted Childline 1098, CWC)",
                                        "की गई कार्रवाई (जैसे: चाइल्डलाइन 1098 या CWC से संपर्क)"
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isSaved) {
                            Text(
                                text = tr(
                                    "Note saved securely to your offline vault.",
                                    "नोट आपके ऑफ़लाइन वॉल्ट में सुरक्षित रूप से सहेजा गया।"
                                ),
                                color = SafeGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Button(
                            onClick = {
                                if (situationNotes.isNotBlank()) {
                                    viewModel.saveIncidentReport(
                                        category = "Child Safety",
                                        description = situationNotes,
                                        locationText = locationArea,
                                        perpetratorDetails = "Confidential",
                                        evidenceNotes = actionTakenNotes
                                    )
                                    isSaved = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_child_report_button")
                        ) {
                            Text(tr("Save Confidential Child Safety Note", "गोपनीय बाल सुरक्षा नोट सहेजें"))
                        }
                    }
                }
            }
        }
    }
}