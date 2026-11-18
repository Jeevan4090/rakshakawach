package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.EmergencyAlertHelper
import com.example.ui.RakshaViewModel
import com.example.ui.components.CategoryCard
import com.example.ui.components.LocationStatusBar
import com.example.ui.components.SosEmergencyButton
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyMedium
import com.example.ui.theme.SafeGreen

@Composable
fun HomeScreen(
    viewModel: RakshaViewModel,
    onNavigateToCategory: (Int) -> Unit,
    onNavigateToContacts: () -> Unit,
    onNavigateToLiveLocation: () -> Unit,
    onNavigateToEvidenceRecording: () -> Unit,
    onNavigateToAiAssistant: () -> Unit,
    onNavigateToReports: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sosState by viewModel.sosState.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val isLiveTracking by viewModel.isLiveLocationTracking.collectAsState()
    val contacts by viewModel.contacts.collectAsState()

    var hasLocPermission by remember { mutableStateOf(viewModel.locationHelper.hasLocationPermission()) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocPermission = granted
        if (granted) {
            viewModel.refreshLocation()
        }
    }

    LaunchedEffect(Unit) {
        hasLocPermission = viewModel.locationHelper.hasLocationPermission()
        if (hasLocPermission) {
            viewModel.refreshLocation()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Header: Logo, Name & Tagline
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(NavyDark, NavyMedium)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_raksha_logo),
                            contentDescription = "Raksha Kavach Emblem",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, GoldPrimary, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "RAKSHA KAVACH",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = "WOMEN'S SAFETY APPLICATION",
                                color = GoldPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mandatory Tagline
                    Text(
                        text = "\"Women's Security Comes First — Jai Hind.\"",
                        color = Color(0xFFF1F5F9),
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Location Status Bar
                    LocationStatusBar(
                        hasPermission = hasLocPermission,
                        currentLocation = currentLocation,
                        isLiveTracking = isLiveTracking,
                        onRefreshLocation = { viewModel.refreshLocation() },
                        onRequestPermission = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        onClickBar = { onNavigateToLiveLocation() }
                    )
                }
            }
        }

        // Visible Reassuring Safety Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = CrimsonPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Your safety matters. Help is one tap away.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Prominent SOS Emergency Button Section
        item {
            SosEmergencyButton(
                sosState = sosState,
                onSosClick = {
                    viewModel.startSosCountdown {
                        onNavigateToCategory(1) // I Need Help Urgently
                    }
                },
                onCancelCountdown = { viewModel.cancelSosCountdown() },
                onImmediateTrigger = {
                    viewModel.triggerSosAlertImmediate()
                    onNavigateToCategory(1)
                },
                onDirectCall112 = {
                    EmergencyAlertHelper.dialNumber(context, "112")
                }
            )
        }

        // Quick-Access Action Grid
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "QUICK SAFETY ACCESS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionTile(
                        title = "Contacts (${contacts.size})",
                        icon = Icons.Default.People,
                        color = Color(0xFF2563EB),
                        onClick = onNavigateToContacts,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_access_contacts"
                    )
                    QuickActionTile(
                        title = "Live GPS",
                        icon = Icons.Default.MyLocation,
                        color = Color(0xFF0284C7),
                        onClick = onNavigateToLiveLocation,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_access_live_gps"
                    )
                    QuickActionTile(
                        title = "Audio/Video",
                        icon = Icons.Default.Mic,
                        color = CrimsonPrimary,
                        onClick = onNavigateToEvidenceRecording,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_access_record"
                    )
                    QuickActionTile(
                        title = "AI Guide",
                        icon = Icons.Default.Psychology,
                        color = Color(0xFF7C3AED),
                        onClick = onNavigateToAiAssistant,
                        modifier = Modifier.weight(1f),
                        testTag = "quick_access_ai"
                    )
                }
            }
        }

        // 9 Emergency Categories Section
        item {
            Text(
                text = "EMERGENCY ASSISTANCE CATEGORIES",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }

        val categories = listOf(
            Triple(1, "I Need Help Urgently", "Immediate 112 emergency call, GPS blast alert to contacts, panic beacon"),
            Triple(2, "I Am Being Followed", "Real-time location sharing, discreet SOS, safe route tips, nearest police"),
            Triple(3, "I Need an Ambulance", "Dial 112 & 108 ambulance, share medical emergency details, nearby hospitals"),
            Triple(4, "I Need Assistance / Helplines", "Verified national & regional helpline directory (112, 181, 1091, 1930, 1098)"),
            Triple(5, "I Am Being Harassed", "Document incident, alert loved ones, legal guidance, secure offline notes"),
            Triple(6, "Case of Assault", "Urgent physical safety & medical aid first, 112, supportive legal rights"),
            Triple(7, "Workplace Sexual Harassment", "POSH Act rights, Internal Committee (IC), SHe-Box portal reporting link"),
            Triple(8, "Cybercrime Help", "Call 1930 for financial cyber fraud, cybercrime.gov.in, evidence checklist"),
            Triple(9, "Child Safety / POCSO Help", "Call 1098 Childline, 112, POCSO legal guidelines, child privacy protection")
        )

        items(categories.size) { index ->
            val (num, title, subtitle) = categories[index]
            val icon = when (num) {
                1 -> Icons.Default.Emergency
                2 -> Icons.Default.DirectionsRun
                3 -> Icons.Default.LocalHospital
                4 -> Icons.Default.PhoneInTalk
                5 -> Icons.Default.Campaign
                6 -> Icons.Default.Gavel
                7 -> Icons.Default.Work
                8 -> Icons.Default.Security
                else -> Icons.Default.ChildCare
            }
            val accentColor = when (num) {
                1 -> CrimsonPrimary
                2 -> Color(0xFFEA580C)
                3 -> Color(0xFFE11D48)
                4 -> Color(0xFF0284C7)
                5 -> Color(0xFF9333EA)
                6 -> CrimsonPrimary
                7 -> Color(0xFF4F46E5)
                8 -> Color(0xFF0D9488)
                else -> Color(0xFFD97706)
            }

            CategoryCard(
                number = num,
                title = title,
                subtitle = subtitle,
                icon = icon,
                accentColor = accentColor,
                onClick = { onNavigateToCategory(num) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
            )
        }

        // Saved Reports Access Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .clickable { onNavigateToReports() }
                    .testTag("nav_saved_reports_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderShared,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Confidential Incident Vault",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Review encrypted offline incident notes & evidence",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionTile(
    title: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = color,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
