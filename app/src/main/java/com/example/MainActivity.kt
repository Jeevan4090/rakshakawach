package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.RakshaViewModel
import com.example.ui.screens.AiAssistantScreen
import com.example.ui.screens.AmbulanceScreen
import com.example.ui.screens.AssaultSupportScreen
import com.example.ui.screens.BeingFollowedScreen
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
import com.example.ui.theme.RakshaKavachTheme

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
    REPORTS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RakshaKavachTheme {
                RakshaKavachApp()
            }
        }
    }
}

@Composable
fun RakshaKavachApp(
    viewModel: RakshaViewModel = viewModel()
) {
    val navStack = remember { mutableStateListOf(Screen.HOME) }
    val currentScreen = navStack.lastOrNull() ?: Screen.HOME

    fun navigateTo(screen: Screen) {
        navStack.add(screen)
    }

    fun navigateBack() {
        if (navStack.size > 1) {
            navStack.removeAt(navStack.size - 1)
        }
    }

    if (currentScreen != Screen.HOME) {
        BackHandler {
            navigateBack()
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                    onNavigateToContacts = { navigateTo(Screen.CONTACTS) },
                    onNavigateToLiveLocation = { navigateTo(Screen.LIVE_LOCATION) },
                    onNavigateToEvidenceRecording = { navigateTo(Screen.EVIDENCE_RECORDING) },
                    onNavigateToAiAssistant = { navigateTo(Screen.AI_ASSISTANT) },
                    onNavigateToReports = { navigateTo(Screen.REPORTS) }
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
            }
        }
    }
}
