package com.example.ui.screens

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.AddToHomeScreen
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
import androidx.compose.material.icons.outlined.School
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
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.AppLanguage
import com.example.ui.RakshaViewModel
import com.example.ui.components.SosEmergencyButton
import com.example.ui.theme.GuideViolet
import com.example.ui.theme.KavachTeal
import com.example.ui.theme.SageDeep
import com.example.ui.theme.SageTint
import com.example.ui.theme.SaffronDeep
import com.example.ui.theme.SaffronTint
import com.example.ui.tr
import com.example.widget.SosWidgetProvider

private data class HelpCategory(
    val num: Int,
    val title: String,
    val titleHi: String,
    val subtitle: String,
    val subtitleHi: String,
    val icon: ImageVector,
    val badge: String?,
    val accent: Color
)

private val quickEmergencyCategories = listOf(
    HelpCategory(
        1, "I Need Help Urgently", "मुझे तुरंत मदद चाहिए",
        "Immediate SOS alert & 112", "तुरंत SOS अलर्ट और 112",
        Icons.Outlined.Emergency, "112", Color(0xFFD93848)
    ),
    HelpCategory(
        2, "Someone Is Following Me", "कोई मेरा पीछा कर रहा है",
        "Discreet alerts & nearest police", "गुप्त अलर्ट और नज़दीकी पुलिस",
        Icons.AutoMirrored.Outlined.DirectionsRun, "112", Color(0xFFE05D38)
    ),
    HelpCategory(
        3, "Medical Emergency", "मेडिकल इमरजेंसी",
        "Call ambulance (108 / 112)", "एम्बुलेंस बुलाएँ (108 / 112)",
        Icons.Outlined.LocalHospital, "108", Color(0xFF0E8A72)
    ),
    HelpCategory(
        4, "Need Assistance", "सहायता चाहिए",
        "Verified helplines & support", "सत्यापित हेल्पलाइन और मदद",
        Icons.Outlined.PhoneInTalk, "181", Color(0xFF6A48D7)
    )
)

private val supportCategories = listOf(
    HelpCategory(
        5, "Harassment Support", "उत्पीड़न सहायता",
        "Document & know your options", "सबूत सहेजें और विकल्प जानें",
        Icons.Outlined.Campaign, "1091", Color(0xFFD97706)
    ),
    HelpCategory(
        6, "Assault Support", "हमला / हिंसा सहायता",
        "Medical help & legal rights", "चिकित्सा मदद और कानूनी अधिकार",
        Icons.Outlined.Gavel, "181", Color(0xFFBE185D)
    ),
    HelpCategory(
        7, "Workplace POSH", "कार्यस्थल सुरक्षा (POSH)",
        "POSH Act & IC filing guide", "POSH कानून और शिकायत गाइड",
        Icons.Outlined.Work, "POSH", Color(0xFF2563EB)
    ),
    HelpCategory(
        8, "Cybercrime", "साइबर क्राइम",
        "Report fraud & keep evidence", "धोखाधड़ी रिपोर्ट और सबूत सहेजें",
        Icons.Outlined.Security, "1930", Color(0xFF0284C7)
    ),
    HelpCategory(
        9, "Child Safety", "बाल सुरक्षा",
        "Childline & POCSO guidance", "चाइल्डलाइन और POCSO मार्गदर्शन",
        Icons.Outlined.ChildCare, "1098", Color(0xFF16A34A)
    )
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
    onNavigateToBetaPadhao: () -> Unit,
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
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
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
                            text = tr("Rakshakavach", "रक्षाकवच"),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = tr(
                                "Women's security comes first — Jai Hind",
                                "महिला सुरक्षा सर्वोपरि — जय हिंद"
                            ),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Instant EN / हिंदी Language Toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                        .clickable { AppLanguage.toggle(context) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (AppLanguage.isHindi) "EN" else "हिंदी",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        item {
            val locationRow: LocationRow = when {
                !hasLocPermission -> LocationRow(
                    Icons.Outlined.LocationOff,
                    tr("Location is off", "लोकेशन बंद है"),
                    tr("Turn on", "चालू करें"),
                    SaffronTint, SaffronDeep
                )
                isLiveTracking -> LocationRow(
                    Icons.Outlined.MyLocation,
                    tr("Sharing your live location", "लाइव लोकेशन साझा हो रही है"),
                    null,
                    SageTint, SageDeep
                )
                currentLocation != null -> LocationRow(
                    Icons.Outlined.LocationOn,
                    tr("Location ready", "लोकेशन तैयार है"),
                    null,
                    MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurfaceVariant
                )
                else -> LocationRow(
                    Icons.Outlined.LocationOn,
                    tr("Finding your location…", "आपकी लोकेशन खोजी जा रही है…"),
                    null,
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
                    text = tr(
                        "Add a trusted contact so SOS has someone to reach",
                        "SOS के लिए एक विश्वसनीय संपर्क जोड़ें"
                    ),
                    container = SaffronTint,
                    content = SaffronDeep,
                    actionLabel = tr("Add", "जोड़ें"),
                    onClick = onNavigateToContacts
                )
            } else {
                StatusRow(
                    icon = Icons.Outlined.Shield,
                    text = tr(
                        "${contacts.size} trusted ${if (contacts.size == 1) "contact" else "contacts"} ready",
                        "${contacts.size} विश्वसनीय संपर्क तैयार हैं"
                    ),
                    container = SageTint,
                    content = SageDeep,
                    actionLabel = null,
                    onClick = onNavigateToContacts
                )
            }
        }

        item {
            Box(modifier = Modifier.padding(vertical = 6.dp)) {
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
                    title = tr("Contacts", "संपर्क"),
                    icon = Icons.Outlined.People,
                    color = Color(0xFF0E8A72),
                    onClick = onNavigateToContacts,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_access_contacts"
                )
                QuickActionTile(
                    title = tr("Location", "लोकेशन"),
                    icon = Icons.Outlined.MyLocation,
                    color = Color(0xFF0284C7),
                    onClick = onNavigateToLiveLocation,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_access_live_gps"
                )
                QuickActionTile(
                    title = tr("Record", "रिकॉर्ड"),
                    icon = Icons.Outlined.Mic,
                    color = Color(0xFFD93848),
                    onClick = onNavigateToEvidenceRecording,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_access_record"
                )
                QuickActionTile(
                    title = tr("Ask guide", "सहायक"),
                    icon = Icons.Outlined.Psychology,
                    color = GuideViolet,
                    onClick = onNavigateToAiAssistant,
                    modifier = Modifier.weight(1f),
                    testTag = "quick_access_ai"
                )
            }
        }

        item {
            BetaPadhaoBannerCard(
                onClick = onNavigateToBetaPadhao
            )
        }

        item {
            StatusRow(
                icon = Icons.Outlined.AddToHomeScreen,
                text = tr(
                    "Add 3-tap SOS shortcut to home screen",
                    "होम स्क्रीन पर 3-टैप SOS शॉर्टकट जोड़ें"
                ),
                container = MaterialTheme.colorScheme.surface,
                content = KavachTeal,
                actionLabel = tr("Pin", "पिन"),
                onClick = { pinSosWidget(context) }
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tr("Quick Emergency Categories", "त्वरित आपातकालीन श्रेणियाँ"),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = tr("Instant 1-tap support", "1-टैप तुरंत मदद"),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            CategoryGrid(
                categories = quickEmergencyCategories,
                onSelect = onNavigateToCategory
            )
        }

        item {
            Text(
                text = tr("More Support & Rights", "अन्य सहायता और अधिकार"),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 6.dp)
            )
        }

        item {
            CategoryGrid(
                categories = supportCategories,
                onSelect = onNavigateToCategory
            )
        }

        item {
            val shape = RoundedCornerShape(14.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), shape)
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
                        text = tr("Incident vault", "सुरक्षित वॉल्ट"),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = tr(
                            "Encrypted notes and evidence, stored on this phone",
                            "एन्क्रिप्टेड नोट्स और सबूत, केवल आपके फोन में सुरक्षित"
                        ),
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

@Composable
private fun CategoryGrid(
    categories: List<HelpCategory>,
    onSelect: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        categories.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    CategoryGridCard(
                        item = item,
                        onClick = { onSelect(item.num) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategoryGridCard(
    item: HelpCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = modifier
            .height(152.dp)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, item.accent.copy(alpha = 0.28f), shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = item.accent,
                    modifier = Modifier.size(22.dp)
                )
            }

            if (item.badge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(item.accent.copy(alpha = 0.14f))
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = item.badge,
                        color = item.accent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = if (AppLanguage.isHindi) item.titleHi else item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
            Text(
                text = if (AppLanguage.isHindi) item.subtitleHi else item.subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp
            )
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
            .background(tint.copy(alpha = 0.14f)),
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
private fun BetaPadhaoBannerCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1A1446),
            Color(0xFF4C2396),
            Color(0xFF1F6F78)
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(gradient)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(18.dp)
            .testTag("beta_padhao_banner"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.White.copy(alpha = 0.18f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.School,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = tr("Beta Padhao • Beti Bachao", "बेटा पढ़ाओ • बेटी बचाओ"),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Text(
            text = tr(
                "Educate Sons with Respect • Empower Daughters with Strength",
                "बेटों को सम्मान सिखाएँ • बेटियों को सशक्त बनाएँ"
            ),
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 22.sp
        )

        Text(
            text = tr(
                "Government Schemes, Sensitization & Pledge →",
                "सरकारी योजनाएँ, जागरूकता और शपथ →"
            ),
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
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
            .border(0.8.dp, color.copy(alpha = 0.22f), shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 4.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconBadge(icon = icon, tint = color, size = 40.dp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

private fun pinSosWidget(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val provider = ComponentName(context, SosWidgetProvider::class.java)
        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            appWidgetManager.requestPinAppWidget(provider, null, null)
            return
        }
    }
    Toast.makeText(
        context,
        tr(
            "Long-press your home screen → Widgets → Rakshakavach to add SOS",
            "होम स्क्रीन पर देर तक दबाएँ → विजेट्स → रक्षाकवच चुनें"
        ),
        Toast.LENGTH_LONG
    ).show()
}