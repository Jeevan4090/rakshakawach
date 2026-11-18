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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
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

@Composable
fun UrgentHelpScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToRecord: () -> Unit
) {
    val context = LocalContext.current
    val sosState by viewModel.sosState.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val contacts by viewModel.contacts.collectAsState()

    var customMsg by remember { mutableStateOf(sosState.customEmergencyMessage) }
    var attachEvidenceNote by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = "Emergency SOS",
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("urgent_help_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Immediate 112 Call Banner (Top Priority)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = CrimsonPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🚨 UNIFIED EMERGENCY CALL",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Direct connection to India's Emergency Response Support System (112)",
                            color = Color(0xFFFFDAD6),
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("call_112_button")
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = CrimsonPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DIAL 112 NOW (POLICE / MEDICAL / FIRE)",
                                color = CrimsonPrimary,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Realism & Legal Transparency Banner
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
                            text = "Notice: Tapping 112 opens the official emergency helpline dialer. This app does not claim police dispatch or automatic tracking until confirmed by the responding official authorities.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // SOS Broadcast Status & Action
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (sosState.alertDispatched) Icons.Default.CheckCircle else Icons.Default.Send,
                                contentDescription = null,
                                tint = if (sosState.alertDispatched) SafeGreen else CrimsonPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Broadcast SOS Alert to Trusted Contacts",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        if (sosState.statusMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = if (sosState.alertDispatched) SafeGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = sosState.statusMessage ?: "",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = if (sosState.alertDispatched) SafeGreen else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Custom emergency message field
                        OutlinedTextField(
                            value = customMsg,
                            onValueChange = {
                                customMsg = it
                                viewModel.updateCustomEmergencyMessage(it)
                            },
                            label = { Text("Emergency Message") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("urgent_sos_message_input"),
                            minLines = 2
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Attached evidence note
                        OutlinedTextField(
                            value = attachEvidenceNote,
                            onValueChange = { attachEvidenceNote = it },
                            label = { Text("Optional Evidence / Situation Details") },
                            placeholder = { Text("e.g., Near Bus Stop #4, White sedan") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("urgent_sos_evidence_input"),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Location attachment status
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (currentLocation != null) SafeGreen else Color(0xFFEAB308),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLocation != null) {
                                    "GPS Attached: ${currentLocation?.toFormattedCoordinates()}"
                                } else {
                                    "Acquiring GPS location for map link..."
                                },
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.triggerSosAlertImmediate(attachEvidenceNote)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("dispatch_sos_button")
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SEND SOS TO ALL CONTACTS NOW", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Trusted Contacts Quick Dial / Manage
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Emergency Contacts (${contacts.size})",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            OutlinedButton(
                                onClick = onNavigateToContacts,
                                modifier = Modifier.testTag("manage_contacts_from_sos")
                            ) {
                                Text("Manage", fontSize = 12.sp)
                            }
                        }

                        if (contacts.isEmpty()) {
                            Text(
                                text = "No trusted contacts added yet. Tap Manage to add parents, guardians, or friends.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            contacts.forEach { contact ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = contact.name + if (contact.isPrimary) " (Primary)" else "",
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "${contact.relationship} • ${contact.phone}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Button(
                                        onClick = { EmergencyAlertHelper.dialNumber(context, contact.phone) },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        modifier = Modifier.testTag("call_contact_${contact.id}")
                                    ) {
                                        Icon(Icons.Default.Call, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Call", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Quick Audio SOS recording
            item {
                OutlinedButton(
                    onClick = onNavigateToRecord,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("goto_audio_record_button")
                ) {
                    Icon(Icons.Default.Mic, contentDescription = null, tint = CrimsonPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Record Audio / Video SOS Evidence", color = CrimsonPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
