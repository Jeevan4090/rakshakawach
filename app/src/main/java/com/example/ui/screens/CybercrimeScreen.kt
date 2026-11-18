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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = "Cybercrime & Online Safety",
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Direct Dials Header: 1930 & Portal Link
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "1930") },
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("cyber_dial_1930_button")
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("DIAL 1930\n(CYBER FRAUD)", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }

                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("cyber_dial_112_button")
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("DIAL 112\n(POLICE)", fontSize = 11.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            // National Cyber Crime Reporting Portal Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F766E)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "NATIONAL CYBER CRIME REPORTING PORTAL",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Official Ministry of Home Affairs portal (cybercrime.gov.in) to report cyberstalking, online harassment, obscene content, morphed photos, and cyber financial fraud anonymously or formally.",
                            color = Color(0xFFCCFBF1),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                EmergencyAlertHelper.openWebUrl(context, "https://cybercrime.gov.in/")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("open_cybercrime_portal_button")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, tint = Color(0xFF0F766E))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Open cybercrime.gov.in Portal", color = Color(0xFF0F766E), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Bank Urgent Warning Alert
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Financial Theft / Unauthorized Transaction?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF78350F)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "1. Dial 1930 within the 'Golden Hour' (first 2-3 hours) to place a freeze on beneficiary bank accounts.\n2. Call your bank immediately to block ATM cards and net banking.\n3. Raksha Kavach NEVER asks for OTPs, PINs, or banking passwords.",
                                fontSize = 11.sp,
                                color = Color(0xFF78350F),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Evidence Preservation Checklist
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🛡️ Evidence & Account Security Checklist",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Check off evidence items as you secure them for legal reporting:",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        CyberChecklistItem(
                            checked = checklistState["screenshots"] == true,
                            onCheckedChange = { checklistState["screenshots"] = it },
                            title = "Full Screenshots with Dates & Timestamps",
                            subtitle = "Capture abusive posts, comments, emails, or chat threads clearly."
                        )

                        CyberChecklistItem(
                            checked = checklistState["url_profile"] == true,
                            onCheckedChange = { checklistState["url_profile"] = it },
                            title = "Profile Links & Web URLs",
                            subtitle = "Copy direct URLs of offending accounts, posts, or groups before they are deleted."
                        )

                        CyberChecklistItem(
                            checked = checklistState["txn_details"] == true,
                            onCheckedChange = { checklistState["txn_details"] = it },
                            title = "Transaction UTR / Reference Numbers",
                            subtitle = "Save SMS debit alerts, UPI Reference numbers, and bank statements."
                        )

                        CyberChecklistItem(
                            checked = checklistState["bank_contacted"] == true,
                            onCheckedChange = { checklistState["bank_contacted"] = it },
                            title = "Bank Fraud Hotline Alerted",
                            subtitle = "Notified bank and requested immediate reversal/block of compromised cards."
                        )

                        CyberChecklistItem(
                            checked = checklistState["two_factor"] == true,
                            onCheckedChange = { checklistState["two_factor"] = it },
                            title = "Enable Two-Factor Authentication (2FA)",
                            subtitle = "Secured email, Instagram, WhatsApp, and social profiles with app-based 2FA."
                        )

                        CyberChecklistItem(
                            checked = checklistState["account_blocked"] == true,
                            onCheckedChange = { checklistState["account_blocked"] = it },
                            title = "Reported & Blocked on Platform",
                            subtitle = "Used in-app reporting tools on WhatsApp/Instagram/Facebook after preserving screenshots."
                        )
                    }
                }
            }

            // Confidential Incident Note
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
                                text = "Confidential Cyber Incident Vault Note",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = platformName,
                            onValueChange = { platformName = it },
                            label = { Text("Platform (e.g., WhatsApp, Instagram, Telegram, UPI)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = suspectHandle,
                            onValueChange = { suspectHandle = it },
                            label = { Text("Suspect Handle / Mobile / URL") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = incidentText,
                            onValueChange = { incidentText = it },
                            label = { Text("Description of Harassment / Cyber Fraud") },
                            placeholder = { Text("Describe the timeline, nature of threat, or loss...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("cyber_incident_description_input"),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (isSaved) {
                            Text(
                                text = "Cybercrime incident note saved securely to device.",
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
                                        category = "Cybercrime",
                                        description = incidentText,
                                        locationText = "Platform: $platformName",
                                        perpetratorDetails = suspectHandle,
                                        evidenceNotes = "Checklist completed: ${checklistState.values.count { it }}/6 items"
                                    )
                                    isSaved = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_cyber_report_button")
                        ) {
                            Text("Save Incident to Device Vault")
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
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}
