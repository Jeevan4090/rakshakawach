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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmergencyAlertHelper
import com.example.data.entity.IncidentReport
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.SafeGreen
import com.example.ui.tr
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun IncidentReportsListScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val reports by viewModel.incidentReports.collectAsState()

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr(
                    "Confidential Incident Vault",
                    "गोपनीय घटना वॉल्ट",
                    "গোপনীয় ঘটনার ভল্ট"
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
                .testTag("incident_reports_list_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = SafeGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = tr(
                                    "Encrypted Local Storage Only",
                                    "केवल एन्क्रिप्टेड लोकल स्टोरेज",
                                    "শুধুমাত্র এনক্রিপ্টেড লোকাল স্টোরেজ"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = tr(
                                    "All reports are stored strictly in your app's private sandbox database. You retain complete ownership and control.",
                                    "सभी रिपोर्ट केवल आपके ऐप के निजी डेटाबेस में सुरक्षित रहती हैं। इन पर पूरा अधिकार और नियंत्रण केवल आपका है।",
                                    "সমস্ত রিপোর্ট শুধুমাত্র আপনার অ্যাপের ব্যক্তিগত ডাটাবেসে সুরক্ষিত থাকে। এগুলোর সম্পূর্ণ মালিকানা ও নিয়ন্ত্রণ আপনার কাছেই থাকে।"
                                ),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            if (reports.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderShared,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = tr(
                                    "Vault is Currently Empty",
                                    "वॉल्ट अभी खाली है",
                                    "ভল্ট বর্তমানে খালি"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tr(
                                    "When you document incidents under Harassment, Assault, Workplace, Cybercrime, or Child Safety, they will appear here.",
                                    "जब आप उत्पीड़न, हमला, कार्यस्थल, साइबर क्राइम या बाल सुरक्षा के तहत कोई घटना दर्ज करेंगी, तो वह यहाँ दिखाई देगी।",
                                    "আপনি যখন হয়রানি, হামলা, কর্মক্ষেত্র, সাইবার ক্রাইম বা শিশু সুরক্ষার অধীনে কোনো ঘটনা নথিভুক্ত করবেন, তখন তা এখানে দেখাবে।"
                                ),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(reports, key = { it.id }) { report ->
                    ReportCardItem(
                        report = report,
                        onDelete = { viewModel.deleteIncidentReport(report) },
                        onShare = {
                            val timeStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(report.timestamp))
                            val text = """
📋 RAKSHA KAVACH INCIDENT RECORD
Category: ${report.category}
Date & Time: $timeStr
Location: ${report.locationDescription}
Description: ${report.description}
Perpetrator Info: ${report.perpetratorDetails.ifBlank { "N/A" }}
Evidence Notes: ${report.evidenceNotes.ifBlank { "N/A" }}
                            """.trimIndent()
                            EmergencyAlertHelper.shareSosMessage(context, text)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ReportCardItem(
    report: IncidentReport,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(report.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("report_item_${report.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = report.category.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = dateStr,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = report.description,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (report.locationDescription.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tr("📍 Location: ", "📍 स्थान: ", "📍 অবস্থান: ") + report.locationDescription,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (report.perpetratorDetails.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tr("👤 Perpetrator: ", "👤 आरोपी: ", "👤 অভিযুক্ত: ") + report.perpetratorDetails,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (report.evidenceNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tr("📁 Evidence: ", "📁 सबूत: ", "📁 প্রমাণ: ") + report.evidenceNotes,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onShare,
                    modifier = Modifier.testTag("share_report_${report.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = tr("Export / Share", "निर्यात / साझा करें", "এক্সপোর্ট / শেয়ার করুন"),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("delete_report_${report.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = tr("Delete", "हटाएँ", "মুছে ফেলুন"),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}