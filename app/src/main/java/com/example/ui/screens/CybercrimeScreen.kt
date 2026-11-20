package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmergencyAlertHelper
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.SageDeep
import com.example.ui.theme.SaffronDeep
import com.example.ui.theme.SaffronTint

@Composable
fun CybercrimeScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val context = LocalContext.current

    val checklistState = remember {
        mutableStateMapOf(
            "screenshots" to false,
            "url_profile" to false,
            "txn_details" to false,
            "bank_contacted" to false,
            "two_factor" to false,
            "account_blocked" to false
        )
    }

    var incidentText by remember { mutableStateOf("") }
    var suspectHandle by remember { mutableStateOf("") }
    var platformName by remember { mutableStateOf("") }
    var incidentError by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    val doneCount = checklistState.values.count { it }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = "Cybercrime help",
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("cybercrime_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "1930") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("cyber_dial_1930_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Call 1930", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text("Cyber fraud", fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("cyber_dial_112_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Security,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Call 112", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            Text("Police", fontSize = 11.sp)
                        }
                    }
                }
            }

            item {
                NoticeCard(
                    icon = Icons.Outlined.AccountBalance,
                    title = "Lost money to fraud?",
                    text = "Call 1930 as soon as you can. The first few hours matter most for freezing the receiving account. Then call your bank to block cards and net banking. Rakshakavach never asks for OTPs, PINs, or banking passwords.",
                    container = SaffronTint,
                    content = SaffronDeep
                )
            }

            item {
                SurfaceCard {
                    CardHeader(icon = Icons.Outlined.Security, title = "National Cyber Crime Reporting Portal")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The Ministry of Home Affairs portal to report cyberstalking, online harassment, obscene content, morphed photos, and financial fraud, formally or anonymously.",
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            EmergencyAlertHelper.openWebUrl(context, "https://cybercrime.gov.in/")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("open_cybercrime_portal_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open cybercrime.gov.in")
                    }
                }
            }

            item {
                SurfaceCard {
                    CardHeader(icon = Icons.Outlined.Shield, title = "Evidence and account checklist")
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$doneCount of ${checklistState.size} done",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    CyberChecklistItem(
                        checked = checklistState["screenshots"] == true,
                        onCheckedChange = { checklistState["screenshots"] = it },
                        title = "Screenshots with dates and times",
                        subtitle = "Capture abusive posts, comments, emails, or chats clearly."
                    )
                    CyberChecklistItem(
                        checked = checklistState["url_profile"] == true,
                        onCheckedChange = { checklistState["url_profile"] = it },
                        title = "Profile links and URLs",
                        subtitle = "Copy links to offending accounts, posts, or groups before they're deleted."
                    )
                    CyberChecklistItem(
                        checked = checklistState["txn_details"] == true,
                        onCheckedChange = { checklistState["txn_details"] = it },
                        title = "Transaction and UPI reference numbers",
                        subtitle = "Save debit SMS alerts, UPI references, and bank statements."
                    )
                    CyberChecklistItem(
                        checked = checklistState["bank_contacted"] == true,
                        onCheckedChange = { checklistState["bank_contacted"] = it },
                        title = "Bank alerted",
                        subtitle = "Asked your bank to block or reverse compromised cards."
                    )
                    CyberChecklistItem(
                        checked = checklistState["two_factor"] == true,
                        onCheckedChange = { checklistState["two_factor"] = it },
                        title = "Two-factor authentication on",
                        subtitle = "Secure your email, Instagram, WhatsApp, and other accounts."
                    )
                    CyberChecklistItem(
                        checked = checklistState["account_blocked"] == true,
                        onCheckedChange = { checklistState["account_blocked"] = it },
                        title = "Reported and blocked on the platform",
                        subtitle = "Use the app's reporting tools after saving your screenshots."
                    )
                }
            }

            item {
                SurfaceCard {
                    CardHeader(icon = Icons.Outlined.Lock, title = "Private incident note", tint = SageDeep)
                    Spacer(modifier = Modifier.height(14.dp))

                    FormField(
                        value = platformName,
                        onValueChange = {
                            platformName = it
                            isSaved = false
                        },
                        label = "Platform",
                        placeholder = "WhatsApp, Instagram, Telegram, UPI"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = suspectHandle,
                        onValueChange = {
                            suspectHandle = it
                            isSaved = false
                        },
                        label = "Suspect's handle, number, or URL"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = incidentText,
                        onValueChange = {
                            incidentText = it
                            incidentError = false
                            isSaved = false
                        },
                        label = "What happened",
                        placeholder = "The timeline, the threat, or the loss",
                        singleLine = false,
                        minLines = 4,
                        error = if (incidentError) "Describe what happened to save the note" else null,
                        modifier = Modifier.testTag("cyber_incident_description_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (incidentText.isBlank()) {
                                incidentError = true
                            } else {
                                viewModel.saveIncidentReport(
                                    category = "Cybercrime",
                                    description = incidentText,
                                    locationText = "Platform: $platformName",
                                    perpetratorDetails = suspectHandle,
                                    evidenceNotes = "Checklist completed: $doneCount/${checklistState.size} items"
                                )
                                isSaved = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_cyber_report_button")
                    ) {
                        Text("Save note to vault")
                    }

                    if (isSaved) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Note saved to your vault",
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

@Composable
fun CyberChecklistItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Checkbox) { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.padding(top = 4.dp, end = 4.dp, start = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}