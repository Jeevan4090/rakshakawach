package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmergencyAlertHelper
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.SageDeep
import com.example.ui.tr

@Composable
fun WorkplaceHarassmentScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val context = LocalContext.current

    var incidentText by remember { mutableStateOf("") }
    var companyDetails by remember { mutableStateOf("") }
    var perpetratorInfo by remember { mutableStateOf("") }
    var evidenceItems by remember { mutableStateOf("") }
    var incidentError by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("Workplace harassment", "कार्यस्थल पर उत्पीड़न"),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("workplace_harassment_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SurfaceCard {
                    CardHeader(icon = Icons.Outlined.Work, title = tr("SHe-Box portal", "SHe-Box पोर्टल"))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tr(
                            "The Government of India's single-window portal for women employees, in government and private workplaces, to file complaints under the POSH Act 2013 directly with the concerned authority.",
                            "सरकारी और निजी कार्यस्थलों में महिला कर्मचारियों के लिए भारत सरकार का सिंगल-विंडो पोर्टल, जहाँ POSH अधिनियम 2013 के तहत सीधे संबंधित प्राधिकरण को शिकायत दर्ज की जा सकती है।"
                        ),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            EmergencyAlertHelper.openWebUrl(context, "https://shebox.wcd.gov.in/")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("open_shebox_portal_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tr("Open SHe-Box", "SHe-Box खोलें"))
                    }
                }
            }

            item {
                SurfaceCard {
                    CardHeader(
                        icon = Icons.Outlined.Gavel,
                        title = tr("Your protections under the POSH Act", "POSH अधिनियम के तहत आपके अधिकार")
                    )
                    FactRow(
                        tr("Internal Committee", "आंतरिक समिति (IC)"),
                        tr(
                            "Employers with 10 or more employees must set up an IC led by a senior woman employee, with at least half its members women.",
                            "10 या अधिक कर्मचारियों वाले नियोक्ताओं को एक वरिष्ठ महिला कर्मचारी की अध्यक्षता में आंतरिक समिति (IC) बनाना अनिवार्य है, जिसमें कम से कम आधे सदस्य महिलाएँ हों।"
                        )
                    )
                    FactRow(
                        tr("Local Committee", "स्थानीय समिति (LC)"),
                        tr(
                            "If your workplace has fewer than 10 employees, or the complaint is against the employer, file with the District Local Committee.",
                            "यदि आपके कार्यस्थल में 10 से कम कर्मचारी हैं, या शिकायत नियोक्ता के विरुद्ध है, तो जिला स्थानीय समिति (LC) में शिकायत दर्ज करें।"
                        )
                    )
                    FactRow(
                        tr("Three-month window", "तीन महीने की समय-सीमा"),
                        tr(
                            "File within 3 months of the incident. The committee can extend this by another 3 months.",
                            "घटना के 3 महीने के भीतर शिकायत दर्ज करें। समिति इसे अगले 3 महीने तक बढ़ा सकती है।"
                        )
                    )
                    FactRow(
                        tr("Confidentiality", "गोपनीयता"),
                        tr(
                            "Section 16 bars anyone from disclosing your identity or the inquiry proceedings.",
                            "धारा 16 किसी को भी आपकी पहचान या जांच की कार्यवाही उजागर करने से रोकती है।"
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "181") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tr("Call 181 for legal guidance", "कानूनी मार्गदर्शन के लिए 181 पर कॉल करें"))
                    }
                }
            }

            item {
                SurfaceCard(container = MaterialTheme.colorScheme.surfaceVariant) {
                    CardHeader(
                        icon = Icons.Outlined.Folder,
                        title = tr("Keep your evidence", "अपने सबूत सुरक्षित रखें"),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    FactRow(
                        "1",
                        tr(
                            "Forward abusive emails or work chats to your personal email.",
                            "अपमानजनक ईमेल या वर्क चैट को अपने निजी ईमेल पर फॉरवर्ड करें।"
                        )
                    )
                    FactRow(
                        "2",
                        tr(
                            "Screenshot Slack, Teams, or WhatsApp messages with the date and time visible.",
                            "Slack, Teams या WhatsApp संदेशों के तारीख और समय के साथ स्क्रीनशॉट लें।"
                        )
                    )
                    FactRow(
                        "3",
                        tr(
                            "Keep a dated diary of verbal incidents, inappropriate requests, or comments.",
                            "मौखिक घटनाओं, अनुचित मांगों या टिप्पणियों की तारीख के साथ डायरी लिखें।"
                        )
                    )
                    FactRow(
                        "4",
                        tr(
                            "Note the colleagues or witnesses who were present.",
                            "वहाँ मौजूद सहकर्मियों या गवाहों के नाम नोट करें।"
                        )
                    )
                }
            }

            item {
                SurfaceCard {
                    CardHeader(
                        icon = Icons.Outlined.Lock,
                        title = tr("Private complaint draft", "निजी शिकायत ड्राफ्ट"),
                        tint = SageDeep
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tr(
                            "Write your account here first. It stays on this phone until you submit to the IC or SHe-Box.",
                            "अपना विवरण पहले यहाँ लिख लें। जब तक आप IC या SHe-Box में जमा न करें, यह केवल आपके फोन में सुरक्षित रहेगा।"
                        ),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    FormField(
                        value = companyDetails,
                        onValueChange = {
                            companyDetails = it
                            isSaved = false
                        },
                        label = tr("Organization, department, or branch", "संस्थान, विभाग या शाखा")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = perpetratorInfo,
                        onValueChange = {
                            perpetratorInfo = it
                            isSaved = false
                        },
                        label = tr("Respondent's name and designation", "आरोपी का नाम और पद")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = incidentText,
                        onValueChange = {
                            incidentText = it
                            incidentError = false
                            isSaved = false
                        },
                        label = tr("What happened, in order", "क्या हुआ, क्रम से लिखें"),
                        singleLine = false,
                        minLines = 4,
                        error = if (incidentError) {
                            tr("Describe what happened to save the draft", "ड्राफ्ट सहेजने के लिए घटना का विवरण लिखें")
                        } else null,
                        modifier = Modifier.testTag("workplace_complaint_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = evidenceItems,
                        onValueChange = {
                            evidenceItems = it
                            isSaved = false
                        },
                        label = tr("Evidence you have", "आपके पास मौजूद सबूत"),
                        placeholder = tr("Emails, witnesses, chat logs", "ईमेल, गवाह, चैट लॉग")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (incidentText.isBlank()) {
                                incidentError = true
                            } else {
                                viewModel.saveIncidentReport(
                                    category = "Workplace",
                                    description = incidentText,
                                    locationText = companyDetails,
                                    perpetratorDetails = perpetratorInfo,
                                    evidenceNotes = evidenceItems
                                )
                                isSaved = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_workplace_report_button")
                    ) {
                        Text(tr("Save draft to vault", "ड्राफ्ट वॉल्ट में सहेजें"))
                    }

                    if (isSaved) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tr("Draft saved to your vault", "ड्राफ्ट आपके वॉल्ट में सहेजा गया"),
                                color = SageDeep,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            TextButton(onClick = onNavigateToReports) {
                                Text(tr("View in vault", "वॉल्ट में देखें"))
                            }
                        }
                    }
                }
            }
        }
    }
}