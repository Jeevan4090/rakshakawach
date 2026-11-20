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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PhoneInTalk
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.EmergencyAlertHelper
import com.example.ui.RakshaViewModel
import com.example.ui.components.SosEmergencyButton
import com.example.ui.theme.GuideViolet
import com.example.ui.theme.KavachTeal
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.SageDeep
import com.example.ui.theme.SageTint
import com.example.ui.theme.SaffronDeep
import com.example.ui.theme.SaffronTint
import com.example.ui.theme.SosRed

private data class HelpCategory(
    val num: Int,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

private val urgentCategories = listOf(
    HelpCategory(1, "I need help urgently", "Calls 112 and alerts your contacts with your location", Icons.Outlined.Emergency),
    HelpCategory(2, "I'm being followed", "Shares your location quietly and finds the nearest police", Icons.AutoMirrored.Outlined.DirectionsRun),
    HelpCategory(3, "I need an ambulance", "Calls 112 and 108, shows hospitals near you", Icons.Outlined.LocalHospital)
)

private val supportCategories = listOf(
    HelpCategory(4, "Helplines and assistance", "Verified numbers: 112, 181, 1091, 1930, 1098", Icons.Outlined.PhoneInTalk),
    HelpCategory(5, "I'm being harassed", "Document what happened and know your options", Icons.Outlined.Campaign),
    HelpCategory(6, "Assault", "Medical help first, then your legal rights", Icons.Outlined.Gavel),
    HelpCategory(7, "Workplace harassment", "POSH Act rights and filing with the Internal Committee", Icons.Outlined.Work),
    HelpCategory(8, "Cybercrime", "Report fraud on 1930 and keep your evidence", Icons.Outlined.Security),
    HelpCategory(9, "Child safety", "Childline 1098 and POCSO guidance", Icons.Outlined.ChildCare)
)

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
        if (granted) viewModel.refreshLocation()
    }

    LaunchedEffect(Unit) {
        hasLocPermission = viewModel.locationHelper.hasLocationPermission()
        if (hasLocPermission) viewModel.refreshLocation()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen"),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_raksha_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Rakshakavach",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Women's security comes first — Jai Hind",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            val locationRow: LocationRow = when {
                !hasLocPermission -> LocationRow(
                    Icons.Outlined.LocationOff, "Location is off", "Turn on",
                    SaffronTint, SaffronDeep
                )
                isLiveTracking -> LocationRow(
                    Icons.Outlined.MyLocation, "Sharing your live location", null,
                    SageTint, SageDeep
                )
                currentLocation != null -> LocationRow(
                    Icons.Outlined.LocationOn, "Location ready", null,
                    MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurfaceVariant
                )
                else -> LocationRow(
                    Icons.Outlined.LocationOn, "Finding your location…", null,
                    MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            StatusRow(
                icon = locationRow.icon,
                text = locationRow.text,
                container = locationRow.container,
                content = locationRow.content,
                actionLabel = locationRow.action,
                onClick = {
                    if (!hasLocPermission) {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    } else {
                        onNavigateToLiveLocation()
                    }
                }
            )
        }

        item {
            if (contacts.isEmpty()) {
                StatusRow(
                    icon = Icons.Outlined.PersonAdd,
                    text = "Add a trusted contact so SOS has someone to reach",
                    container = SaffronTint,
                    content = SaffronDeep,
                    actionLabel = "Add",
                    onClick = onNavigateToContacts
                )
            } else {
                StatusRow(
                    icon = Icons.Outlined.Shield,
                    text = "${contacts.size} trusted ${if (contacts.size == 1) "contact" else "contacts"} ready",
                    container = SageTint,
                    content = SageDeep,
                    actionLabel = null,
                    onClick = onNavigateToContacts
                )
            }
        }

        item {
            Box(modifier = Modifier.padding(vertical = 8.dp)) {
                SosEmergencyButton(
                    sosState = sosState,
                    onSosClick = {
                        viewModel.startSosCountdown {
                            onNavigateToCategory(1)
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
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionTile(
                    title = "Contacts",
                    icon = Icons.Outlined.People,
                    color = KavachTeal,
                    onClick = onNavigateToContacts,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_access_contacts"
                )
                QuickActionTile(
                    title = "Location",
                    icon = Icons.Outlined.MyLocation,
                    color = KavachTeal,
                    onClick = onNavigateToLiveLocation,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_access_live_gps"
                )
                QuickActionTile(
                    title = "Record",
                    icon = Icons.Outlined.Mic,
                    color = KavachTeal,
                    onClick = onNavigateToEvidenceRecording,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_access_record"
                )
                QuickActionTile(
                    title = "Ask guide",
                    icon = Icons.Outlined.Psychology,
                    color = GuideViolet,
                    onClick = onNavigateToAiAssistant,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_access_ai"
                )
            }
        }

        item {
            Text(
                text = "Get help now",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(urgentCategories, key = { it.num }) { item ->
            UrgentCard(item = item, onClick = { onNavigateToCategory(item.num) })
        }

        item {
            Text(
                text = "More support",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            val shape = RoundedCornerShape(14.dp)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(0.5.dp, MaterialTheme.colorScheme.outline, shape)
            ) {
                supportCategories.forEachIndexed { index, category ->
                    SupportRow(category = category, onClick = { onNavigateToCategory(category.num) })
                    if (index < supportCategories.lastIndex) {
                        Box(
                            modifier = Modifier
                                .padding(start = 68.dp)
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(MaterialTheme.colorScheme.outline)
                        )
                    }
                }
            }
        }

        item {
            val shape = RoundedCornerShape(14.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(role = Role.Button, onClick = onNavigateToReports)
                    .padding(16.dp)
                    .testTag("nav_saved_reports_card"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Incident vault",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Encrypted notes and evidence, stored on this phone",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private data class LocationRow(
    val icon: ImageVector,
    val text: String,
    val action: String?,
    val container: Color,
    val content: Color
)

@Composable
private fun StatusRow(
    icon: ImageVector,
    text: String,
    container: Color,
    content: Color,
    actionLabel: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(container)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            color = content,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        if (actionLabel != null) {
            Text(
                text = actionLabel,
                color = content,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun IconBadge(icon: ImageVector, tint: Color, size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(tint.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

@Composable
private fun UrgentCard(item: HelpCategory, onClick: () -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    val accent = if (item.num == 1) SosRed else KavachTeal
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBadge(icon = item.icon, tint = accent, size = 44.dp)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = item.subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SupportRow(category: HelpCategory, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBadge(icon = category.icon, tint = KavachTeal, size = 40.dp)
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = category.subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
    val shape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(0.5.dp, MaterialTheme.colorScheme.outline, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconBadge(icon = icon, tint = color, size = 40.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}