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
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.tr

@Composable
fun BeingFollowedScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLiveLocation: () -> Unit,
    onNavigateToRecord: () -> Unit
) {
    val context = LocalContext.current
    val currentLocation by viewModel.currentLocation.collectAsState()
    val isLiveTracking by viewModel.isLiveLocationTracking.collectAsState()
    val contacts by viewModel.contacts.collectAsState()

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("I Am Being Followed", "कोई मेरा पीछा कर रहा है"),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("being_followed_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // High Priority 112 Call
            item {
                Button(
                    onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("followed_call_112_button")
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = tr(
                            "ONE-TAP CALL 112 (EMERGENCY POLICE)",
                            "1-टैप कॉल 112 (आपातकालीन पुलिस)"
                        ),
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
            }

            // CRITICAL SAFETY RULES CARD (Strictly: Do Not Confront)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DirectionsRun,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = tr("IMMEDIATE SAFETY ACTIONS", "तत्काल सुरक्षा कदम"),
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = Color(0xFF78350F)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        SafetyStepItem(
                            number = "1",
                            text = tr(
                                "MOVE TO A BUSY, WELL-LIT PUBLIC AREA immediately — enter an open store, pharmacy, metro station, restaurant, or bank ATM with a guard.",
                                "तुरंत किसी भीड़भाड़ वाले, रोशनी वाले सार्वजनिक स्थान पर जाएँ — किसी खुली दुकान, मेडिकल स्टोर, मेट्रो स्टेशन, रेस्तरां या गार्ड वाले बैंक ATM में प्रवेश करें।"
                            )
                        )
                        SafetyStepItem(
                            number = "2",
                            text = tr(
                                "DO NOT CONFRONT the suspected follower under any circumstance. Keep moving steadily.",
                                "किसी भी स्थिति में पीछा करने वाले व्यक्ति से बहस या सामना न करें। लगातार चलते रहें।"
                            )
                        )
                        SafetyStepItem(
                            number = "3",
                            text = tr(
                                "Approach security personnel, store managers, or trusted members of the public and state clearly: 'Someone is following me, please stay with me.'",
                                "सुरक्षा गार्ड, दुकान के मैनेजर या आसपास के लोगों के पास जाएँ और स्पष्ट कहें: 'कोई मेरा पीछा कर रहा है, कृपया मेरे साथ रहें।'"
                            )
                        )
                        SafetyStepItem(
                            number = "4",
                            text = tr(
                                "Avoid heading directly to an empty or isolated home. Remain in public view until safe.",
                                "सीधे सुनसान या खाली घर न जाएँ। पूरी तरह सुरक्षित होने तक सार्वजनिक स्थान पर ही रहें।"
                            )
                        )
                    }
                }
            }

            // Nearby Police Station Maps Finder
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalPolice,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr("Nearest Police Stations", "नज़दीकी पुलिस स्टेशन"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = tr(
                                "Find verified local police stations near your current GPS position via Google Maps navigation.",
                                "Google Maps के माध्यम से अपनी वर्तमान GPS लोकेशन के पास के पुलिस स्टेशन खोजें।"
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                EmergencyAlertHelper.openMapSearch(context, "police station", currentLocation)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("find_police_station_button")
                        ) {
                            Icon(Icons.Default.LocalPolice, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                tr("Open Nearby Police Stations in Maps", "Maps में नज़दीकी पुलिस स्टेशन देखें")
                            )
                        }
                    }
                }
            }

            // Live Location Sharing with Contacts
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr("Live Location Sharing", "लाइव लोकेशन साझा करना"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isLiveTracking) {
                                tr("Live GPS stream is broadcasting.", "लाइव GPS लोकेशन साझा हो रही है।")
                            } else {
                                tr(
                                    "Broadcast your exact movement coordinates to your emergency contacts.",
                                    "अपने आपातकालीन संपर्कों को अपनी सटीक लोकेशन भेजें।"
                                )
                            },
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    viewModel.toggleLiveLocationTracking(!isLiveTracking)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isLiveTracking) Color(0xFFEF4444) else Color(0xFF0284C7)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("toggle_live_tracking_button")
                            ) {
                                Text(
                                    if (isLiveTracking) {
                                        tr("Stop Live Stream", "लाइव रोकें")
                                    } else {
                                        tr("Start Live GPS", "लाइव GPS शुरू करें")
                                    }
                                )
                            }
                            OutlinedButton(
                                onClick = onNavigateToLiveLocation,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("view_live_map_button")
                            ) {
                                Text(tr("View Map Details", "मैप विवरण देखें"))
                            }
                        }
                    }
                }
            }

            // Discreet SOS & Evidence
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tr(
                                    "Discreet SOS Alert & Documentation",
                                    "गुप्त SOS अलर्ट और सबूत रिकॉर्डिंग"
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tr(
                                "Silently send an urgent alert text to your ${contacts.size} contacts without sounding alarms.",
                                "बिना किसी अलार्म के अपने ${contacts.size} संपर्कों को चुपचाप आपातकालीन संदेश भेजें।"
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    viewModel.triggerSosAlertImmediate("Discreet Alert: I suspect I am being followed. Monitoring my surroundings.")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("discreet_sos_button")
                            ) {
                                Text(tr("Discreet SOS", "गुप्त SOS"), fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = onNavigateToRecord,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("record_followed_evidence_button")
                            ) {
                                Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(tr("Audio Evidence", "ऑडियो सबूत"), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SafetyStepItem(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFB45309)),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.size(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = number,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color(0xFF78350F),
            lineHeight = 17.sp,
            fontWeight = FontWeight.Medium
        )
    }
}