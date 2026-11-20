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
                title = "Workplace harassment",
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
                    CardHeader(icon = Icons.Outlined.Work, title = "SHe-Box portal")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The Government of India's single-window portal for women employees, in government and private workplaces, to file complaints under the POSH Act 2013 directly with the concerned authority.",
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
                        Text("Open SHe-Box")
                    }
                }
            }

            item {
                SurfaceCard {
                    CardHeader(icon = Icons.Outlined.Gavel, title = "Your protections under the POSH Act")
                    FactRow(
                        "Internal Committee",
                        "Employers with 10 or more employees must set up an IC led by a senior woman employee, with at least half its members women."
                    )
                    FactRow(
                        "Local Committee",
                        "If your workplace has fewer than 10 employees, or the complaint is against the employer, file with the District Local Committee."
                    )
                    FactRow(
                        "Three-month window",
                        "File within 3 months of the incident. The committee can extend this by another 3 months."
                    )
                    FactRow(
                        "Confidentiality",
                        "Section 16 bars anyone from disclosing your identity or the inquiry proceedings."
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
                        Text("Call 181 for legal guidance")
                    }
                }
            }

            item {
                SurfaceCard(container = MaterialTheme.colorScheme.surfaceVariant) {
                    CardHeader(
                        icon = Icons.Outlined.Folder,
                        title = "Keep your evidence",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    FactRow("1", "Forward abusive emails or work chats to your personal email.")
                    FactRow("2", "Screenshot Slack, Teams, or WhatsApp messages with the date and time visible.")
                    FactRow("3", "Keep a dated diary of verbal incidents, inappropriate requests, or comments.")
                    FactRow("4", "Note the colleagues or witnesses who were present.")
                }
            }

            item {
                SurfaceCard {
                    CardHeader(icon = Icons.Outlined.Lock, title = "Private complaint draft", tint = SageDeep)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Write your account here first. It stays on this phone until you submit to the IC or SHe-Box.",
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
                        label = "Organization, department, or branch"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = perpetratorInfo,
                        onValueChange = {
                            perpetratorInfo = it
                            isSaved = false
                        },
                        label = "Respondent's name and designation"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = incidentText,
                        onValueChange = {
                            incidentText = it
                            incidentError = false
                            isSaved = false
                        },
                        label = "What happened, in order",
                        singleLine = false,
                        minLines = 4,
                        error = if (incidentError) "Describe what happened to save the draft" else null,
                        modifier = Modifier.testTag("workplace_complaint_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = evidenceItems,
                        onValueChange = {
                            evidenceItems = it
                            isSaved = false
                        },
                        label = "Evidence you have",
                        placeholder = "Emails, witnesses, chat logs"
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
                        Text("Save draft to vault")
                    }

                    if (isSaved) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Draft saved to your vault",
                                color = SageDeep,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            TextButton(onClick = onNavigateToReports) {
                                Text("View in vault")
                            }
                        }
                    }
                }
            }
        }
    }
}