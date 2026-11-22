package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Directions
import androidx.compose.material.icons.outlined.DirectionsSubway
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocalPharmacy
import androidx.compose.material.icons.outlined.LocalPolice
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmergencyAlertHelper
import com.example.ui.AppLanguage
import com.example.ui.RakshaViewModel
import com.example.ui.theme.GuideViolet
import com.example.ui.tr
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private data class SafeHub(
    val name: String,
    val nameHi: String,
    val category: String,
    val categoryHi: String,
    val filterKey: String,
    val address: String,
    val addressHi: String,
    val phone: String,
    val query: String,
    val icon: ImageVector
)

private val safeHubs = listOf(
    SafeHub(
        "Nearest Police Station", "नज़दीकी पुलिस स्टेशन",
        "Police", "पुलिस",
        "Police",
        "24x7 Emergency Police Desk", "24x7 आपातकालीन पुलिस डेस्क",
        "112", "police station", Icons.Outlined.LocalPolice
    ),
    SafeHub(
        "Nearest Women Help Desk", "नज़दीकी महिला हेल्प डेस्क",
        "Women Cells", "महिला प्रकोष्ठ",
        "Women Cells",
        "All-Women Police & Sakhi Cell", "महिला पुलिस और सखी सेल",
        "181", "women police station", Icons.Outlined.VerifiedUser
    ),
    SafeHub(
        "Nearest Metro Station", "नज़दीकी मेट्रो स्टेशन",
        "Metro / Transit", "मेट्रो / परिवहन",
        "Metro",
        "CCTV Monitored Public Transit & Security", "CCTV निगरानी वाला सार्वजनिक परिवहन और सुरक्षा",
        "112", "metro station", Icons.Outlined.DirectionsSubway
    ),
    SafeHub(
        "Nearest Emergency Hospital", "नज़दीकी इमरजेंसी अस्पताल",
        "Hospital / ER", "अस्पताल / इमरजेंसी",
        "Hospital",
        "24x7 Trauma & Emergency Care", "24x7 ट्रॉमा और इमरजेंसी सेवा",
        "108", "hospital emergency", Icons.Outlined.LocalHospital
    ),
    SafeHub(
        "Nearest 24/7 Pharmacy", "नज़दीकी 24/7 मेडिकल स्टोर",
        "Pharmacy", "मेडिकल स्टोर",
        "Hospital",
        "Round-the-clock Medical Store", "24 घंटे खुला मेडिकल स्टोर",
        "108", "24 hour pharmacy", Icons.Outlined.LocalPharmacy
    )
)

@Composable
fun LiveLocationScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currentLocation by viewModel.currentLocation.collectAsState()
    val isLiveTracking by viewModel.isLiveLocationTracking.collectAsState()

    var hasPermission by remember { mutableStateOf(viewModel.locationHelper.hasLocationPermission()) }
    var selectedFilter by remember { mutableStateOf("All") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasPermission = granted
        if (granted) viewModel.refreshLocation()
    }

    val requestPermission = {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(Unit) {
        hasPermission = viewModel.locationHelper.hasLocationPermission()
        if (hasPermission) viewModel.refreshLocation()
    }

    val location = currentLocation
    val timestamp = remember(location) {
        SimpleDateFormat("dd MMM yyyy, hh:mm:ss a", Locale.getDefault()).format(Date())
    }

    fun shareLocation() {
        location?.let { loc ->
            EmergencyAlertHelper.shareSosMessage(
                context,
                "My live location: ${loc.toMapsUrl()} (${loc.latitude}, ${loc.longitude})"
            )
        }
    }

    val filteredHubs = remember(selectedFilter) {
        if (selectedFilter == "All") safeHubs else safeHubs.filter { it.filterKey == selectedFilter }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("live_location_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = tr("Safety Map & Safe Zones", "सुरक्षा मैप और सुरक्षित क्षेत्र"),
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = when {
                            isLiveTracking -> tr("Live GPS Tracking Active", "लाइव GPS ट्रैकिंग सक्रिय")
                            location != null -> tr("Live GPS Ready", "लाइव GPS तैयार")
                            !hasPermission -> tr("Location Permission Needed", "लोकेशन अनुमति आवश्यक है")
                            else -> tr("Fetching GPS signal...", "GPS सिग्नल खोजा जा रहा है...")
                        },
                        fontSize = 12.sp,
                        color = GuideViolet
                    )
                }
                Row {
                    IconButton(onClick = { viewModel.refreshLocation() }) {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = tr("Refresh", "रिफ्रेश"),
                            tint = GuideViolet
                        )
                    }
                    IconButton(
                        onClick = { shareLocation() },
                        enabled = location != null
                    ) {
                        Icon(
                            Icons.Outlined.Share,
                            contentDescription = tr("Share", "साझा करें"),
                            tint = GuideViolet
                        )
                    }
                }
            }
        }

        // Live GPS Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(if (location != null || isLiveTracking) Color(0xFF10B981) else Color(0xFFD97706))
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = when {
                                    isLiveTracking -> tr("Live GPS Active", "लाइव GPS सक्रिय")
                                    location != null -> tr("GPS Locked", "GPS लॉक है")
                                    !hasPermission -> tr("Location Off", "लोकेशन बंद है")
                                    else -> tr("Searching GPS...", "GPS खोजा जा रहा है...")
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0E8A72)
                            )
                        }
                        Text(
                            text = timestamp,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (location != null) {
                        Text(
                            text = tr("Current GPS Coordinates", "वर्तमान GPS निर्देशांक"),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = String.format(
                                Locale.US,
                                "Lat: %.5f, Lng: %.5f (±%dm)",
                                location.latitude,
                                location.longitude,
                                location.accuracyMeters.toInt()
                            ),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(location.toMapsUrl()))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14182E)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Outlined.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(tr("Open in Maps", "Maps में खोलें"), fontSize = 12.sp)
                            }

                            Button(
                                onClick = { shareLocation() },
                                colors = ButtonDefaults.buttonColors(containerColor = GuideViolet),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("share_location_link_button")
                            ) {
                                Icon(Icons.Outlined.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(tr("Share GPS", "GPS साझा करें"), fontSize = 12.sp)
                            }
                        }
                    } else if (!hasPermission) {
                        Text(
                            text = tr(
                                "Allow location access so Rakshakavach can find nearby safe zones and share your live map link.",
                                "लोकेशन की अनुमति दें ताकि रक्षाकवच नज़दीकी सुरक्षित स्थान खोज सके और आपका लाइव मैप लिंक साझा कर सके।"
                            ),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { requestPermission() },
                            colors = ButtonDefaults.buttonColors(containerColor = GuideViolet),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(tr("Allow precise location", "सटीक लोकेशन की अनुमति दें"))
                        }
                    } else {
                        Text(
                            text = tr(
                                "Waiting for GPS fix. Make sure location is switched on in your phone's settings.",
                                "GPS सिग्नल की प्रतीक्षा है। कृपया सुनिश्चित करें कि आपके फोन की सेटिंग्स में लोकेशन चालू है।"
                            ),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Live Map Quick Search Grid
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Search, contentDescription = null, tint = GuideViolet, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = tr(
                                "Live Map Quick Search (Direct Navigation)",
                                "लाइव मैप त्वरित खोज (सीधा नेविगेशन)"
                            ),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickSearchButton(
                            label = tr("Police Station", "पुलिस स्टेशन"),
                            icon = Icons.Outlined.LocalPolice,
                            tint = Color(0xFFD93848),
                            onClick = { EmergencyAlertHelper.openMapSearch(context, "police station", currentLocation) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("map_search_police_btn")
                        )
                        QuickSearchButton(
                            label = tr("Hospital / ER", "अस्पताल / ER"),
                            icon = Icons.Outlined.LocalHospital,
                            tint = Color(0xFF0E8A72),
                            onClick = { EmergencyAlertHelper.openMapSearch(context, "hospital emergency", currentLocation) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("map_search_hospital_btn")
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickSearchButton(
                            label = tr("24/7 Pharmacy", "24/7 मेडिकल"),
                            icon = Icons.Outlined.LocalPharmacy,
                            tint = Color(0xFF0284C7),
                            onClick = { EmergencyAlertHelper.openMapSearch(context, "24 hour pharmacy", currentLocation) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickSearchButton(
                            label = tr("Women Desk", "महिला डेस्क"),
                            icon = Icons.Outlined.VerifiedUser,
                            tint = GuideViolet,
                            onClick = { EmergencyAlertHelper.openMapSearch(context, "women police station", currentLocation) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    QuickSearchButton(
                        label = tr("Metro Station", "मेट्रो स्टेशन"),
                        icon = Icons.Outlined.DirectionsSubway,
                        tint = Color(0xFFD97706),
                        onClick = { EmergencyAlertHelper.openMapSearch(context, "metro station", currentLocation) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("map_search_metro_btn")
                    )
                }
            }
        }

        // Filter Chips
        item {
            val filters = listOf(
                "All" to tr("All Safe Hubs (${safeHubs.size})", "सभी सुरक्षित स्थान (${safeHubs.size})"),
                "Police" to tr("Police", "पुलिस"),
                "Women Cells" to tr("Women Cells", "महिला प्रकोष्ठ"),
                "Metro" to tr("Metro", "मेट्रो"),
                "Hospital" to tr("Medical", "चिकित्सा")
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { (key, label) ->
                    FilterChip(
                        selected = selectedFilter == key,
                        onClick = { selectedFilter = key },
                        label = { Text(label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = GuideViolet.copy(alpha = 0.16f),
                            selectedLabelColor = GuideViolet
                        )
                    )
                }
            }
        }

        // Verified Nearby Safe Locations Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tr("Verified Nearby Safe Locations", "नज़दीकी सत्यापित सुरक्षित स्थान"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = tr("Closest first", "सबसे नज़दीक पहले"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Safe Hub Cards
        items(filteredHubs) { hub ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(GuideViolet.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(hub.icon, contentDescription = null, tint = GuideViolet, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (AppLanguage.isHindi) hub.nameHi else hub.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (AppLanguage.isHindi) hub.categoryHi else hub.category,
                                fontSize = 12.sp,
                                color = GuideViolet
                            )
                        }
                    }

                    Text(
                        text = if (AppLanguage.isHindi) hub.addressHi else hub.address,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { EmergencyAlertHelper.openMapSearch(context, hub.query, currentLocation) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF14182E)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Outlined.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(tr("Directions", "दिशा-निर्देश"), fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { EmergencyAlertHelper.dialNumber(context, hub.phone) },
                            border = BorderStroke(1.dp, GuideViolet.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Outlined.Call, contentDescription = null, tint = GuideViolet, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tr("Call ${hub.phone}", "${hub.phone} पर कॉल"),
                                fontSize = 12.sp,
                                color = GuideViolet
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickSearchButton(
    label: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}