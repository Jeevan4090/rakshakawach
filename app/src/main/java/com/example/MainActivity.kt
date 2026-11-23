package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppLanguage
import com.example.ui.RakshaViewModel
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.AmbulanceScreen
import com.example.ui.screens.AssaultSupportScreen
import com.example.ui.screens.BeingFollowedScreen
import com.example.ui.screens.BetaPadhaoScreen
import com.example.ui.screens.ChildSafetyScreen
import com.example.ui.screens.ContactsScreen
import com.example.ui.screens.CybercrimeScreen
import com.example.ui.screens.EvidenceRecordingScreen
import com.example.ui.screens.HarassmentSupportScreen
import com.example.ui.screens.HelplineDirectoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IncidentReportsListScreen
import com.example.ui.screens.LiveLocationScreen
import com.example.ui.screens.UrgentHelpScreen
import com.example.ui.screens.WorkplaceHarassmentScreen
import com.example.ui.theme.GuideViolet
import com.example.ui.theme.RakshaKavachTheme
import com.example.ui.tr

enum class Screen {
    HOME,
    URGENT_HELP,
    BEING_FOLLOWED,
    AMBULANCE,
    HELPLINES,
    HARASSMENT,
    ASSAULT,
    WORKPLACE,
    CYBERCRIME,
    CHILD_SAFETY,
    CONTACTS,
    LIVE_LOCATION,
    EVIDENCE_RECORDING,
    AI_ASSISTANT,
    REPORTS,
    BETA_PADHAO
}

private data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val labelHi: String,
    val labelBn: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.HOME, "Home", "होम", "হোম", Icons.Outlined.Home, Icons.Filled.Home),
    BottomNavItem(Screen.LIVE_LOCATION, "Map", "मैप", "ম্যাপ", Icons.Outlined.Map, Icons.Filled.Map),
    BottomNavItem(Screen.CONTACTS, "Contacts", "संपर्क", "যোগাযোগ", Icons.Outlined.People, Icons.Filled.People),
    BottomNavItem(Screen.AI_ASSISTANT, "Rakshak", "रक्षक", "রক্ষক", Icons.Outlined.AutoAwesome, Icons.Filled.AutoAwesome),
    BottomNavItem(Screen.REPORTS, "Vault", "वॉल्ट", "ভল্ট", Icons.Outlined.Shield, Icons.Filled.Shield)
)

class MainActivity : ComponentActivity() {

    private var sosTriggerCount by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppLanguage.init(this)
        checkSosIntent(intent)

        setContent {
            RakshaKavachTheme {
                RakshaKavachApp(sosTriggerCount = sosTriggerCount)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        checkSosIntent(intent)
    }

    private fun checkSosIntent(intent: Intent?) {
        if (intent?.getBooleanExtra("TRIGGER_SOS_IMMEDIATELY", false) == true) {
            intent.removeExtra("TRIGGER_SOS_IMMEDIATELY")
            sosTriggerCount++
        }
    }
}

@Composable
fun RakshaKavachApp(
    sosTriggerCount: Int = 0,
    viewModel: RakshaViewModel = viewModel()
) {
    val navStack = remember { mutableStateListOf(Screen.HOME) }
    val currentScreen = navStack.lastOrNull() ?: Screen.HOME
    val topLevelScreens = remember { bottomNavItems.map { it.screen }.toSet() }

    fun navigateTo(screen: Screen) {
        if (currentScreen != screen) {
            navStack.add(screen)
        }
    }

    fun switchTab(screen: Screen) {
        if (currentScreen == screen) return
        navStack.clear()
        navStack.add(Screen.HOME)
        if (screen != Screen.HOME) {
            navStack.add(screen)
        }
    }

    fun navigateBack() {
        if (navStack.size > 1) {
            navStack.removeAt(navStack.size - 1)
        }
    }

    LaunchedEffect(sosTriggerCount) {
        if (sosTriggerCount > 0) {
            viewModel.triggerSosAlertImmediate()
            navStack.clear()
            navStack.add(Screen.HOME)
            navStack.add(Screen.URGENT_HELP)
        }
    }

    if (currentScreen != Screen.HOME) {
        BackHandler {
            navigateBack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentScreen in topLevelScreens) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentScreen == item.screen
                        val displayLabel = tr(item.label, item.labelHi, item.labelBn)
                        NavigationBarItem(
                            selected = selected,
                            onClick = { switchTab(item.screen) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.icon,
                                    contentDescription = displayLabel
                                )
                            },
                            label = {
                                Text(
                                    text = displayLabel,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = GuideViolet,
                                selectedTextColor = GuideViolet,
                                indicatorColor = GuideViolet.copy(alpha = 0.14f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (currentScreen) {
                Screen.HOME -> HomeScreen(
                    viewModel = viewModel,
                    onNavigateToCategory = { num ->
                        when (num) {
                            1 -> navigateTo(Screen.URGENT_HELP)
                            2 -> navigateTo(Screen.BEING_FOLLOWED)
                            3 -> navigateTo(Screen.AMBULANCE)
                            4 -> navigateTo(Screen.HELPLINES)
                            5 -> navigateTo(Screen.HARASSMENT)
                            6 -> navigateTo(Screen.ASSAULT)
                            7 -> navigateTo(Screen.WORKPLACE)
                            8 -> navigateTo(Screen.CYBERCRIME)
                            9 -> navigateTo(Screen.CHILD_SAFETY)
                        }
                    },
                    onNavigateToContacts = { switchTab(Screen.CONTACTS) },
                    onNavigateToLiveLocation = { switchTab(Screen.LIVE_LOCATION) },
                    onNavigateToEvidenceRecording = { navigateTo(Screen.EVIDENCE_RECORDING) },
                    onNavigateToAiAssistant = { switchTab(Screen.AI_ASSISTANT) },
                    onNavigateToReports = { switchTab(Screen.REPORTS) },
                    onNavigateToBetaPadhao = { navigateTo(Screen.BETA_PADHAO) }
                )

                Screen.URGENT_HELP -> UrgentHelpScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() },
                    onNavigateToContacts = { navigateTo(Screen.CONTACTS) },
                    onNavigateToRecord = { navigateTo(Screen.EVIDENCE_RECORDING) }
                )

                Screen.BEING_FOLLOWED -> BeingFollowedScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() },
                    onNavigateToLiveLocation = { navigateTo(Screen.LIVE_LOCATION) },
                    onNavigateToRecord = { navigateTo(Screen.EVIDENCE_RECORDING) }
                )

                Screen.AMBULANCE -> AmbulanceScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() }
                )

                Screen.HELPLINES -> HelplineDirectoryScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() }
                )

                Screen.HARASSMENT -> HarassmentSupportScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() },
                    onNavigateToReports = { navigateTo(Screen.REPORTS) }
                )

                Screen.ASSAULT -> AssaultSupportScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() },
                    onNavigateToReports = { navigateTo(Screen.REPORTS) }
                )

                Screen.WORKPLACE -> WorkplaceHarassmentScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() },
                    onNavigateToReports = { navigateTo(Screen.REPORTS) }
                )

                Screen.CYBERCRIME -> CybercrimeScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() },
                    onNavigateToReports = { navigateTo(Screen.REPORTS) }
                )

                Screen.CHILD_SAFETY -> ChildSafetyScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() },
                    onNavigateToReports = { navigateTo(Screen.REPORTS) }
                )

                Screen.CONTACTS -> ContactsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() }
                )

                Screen.LIVE_LOCATION -> LiveLocationScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() }
                )

                Screen.EVIDENCE_RECORDING -> EvidenceRecordingScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() }
                )

                Screen.AI_ASSISTANT -> AiAssistantScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() }
                )

                Screen.REPORTS -> IncidentReportsListScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navigateBack() }
                )

                Screen.BETA_PADHAO -> BetaPadhaoScreen(
                    onNavigateBack = { navigateBack() }
                )
            }
        }
    }
}