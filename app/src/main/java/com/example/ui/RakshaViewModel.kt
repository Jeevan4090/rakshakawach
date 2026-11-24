package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AiSafetyService
import com.example.data.ChatMessage
import com.example.data.EmergencyAlertHelper
import com.example.data.LocationHelper
import com.example.data.MediaRecordingHelper
import com.example.data.MessageSender
import com.example.data.RakshaDatabase
import com.example.data.UserLocation
import com.example.data.entity.HelplineItem
import com.example.data.entity.IncidentReport
import com.example.data.entity.TrustedContact
import com.example.data.entity.VerifiedHelplines
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

data class SosUiState(
    val isCountingDown: Boolean = false,
    val countdownRemaining: Int = 3,
    val alertDispatched: Boolean = false,
    val statusMessage: String? = null,
    val customEmergencyMessage: String = "I need help urgently! Please contact me immediately. My location is attached."
)

class RakshaViewModel(application: Application) : AndroidViewModel(application) {
    private val db = RakshaDatabase.getDatabase(application)
    private val contactDao = db.contactDao()
    private val incidentReportDao = db.incidentReportDao()

    val locationHelper = LocationHelper(application)
    val mediaHelper = MediaRecordingHelper(application)
    private val aiService = AiSafetyService()

    private val _googleIdToken = MutableStateFlow<String?>(null)
    val googleIdToken: StateFlow<String?> = _googleIdToken.asStateFlow()

    private val _signedInEmail = MutableStateFlow<String?>(null)
    val signedInEmail: StateFlow<String?> = _signedInEmail.asStateFlow()

    val contacts: StateFlow<List<TrustedContact>> = contactDao.getAllContacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val incidentReports: StateFlow<List<IncidentReport>> = incidentReportDao.getAllReports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentLocation = MutableStateFlow<UserLocation?>(null)
    val currentLocation: StateFlow<UserLocation?> = _currentLocation.asStateFlow()

    private val _isLiveLocationTracking = MutableStateFlow(false)
    val isLiveLocationTracking: StateFlow<Boolean> = _isLiveLocationTracking.asStateFlow()

    private var liveLocationJob: Job? = null

    private val _sosState = MutableStateFlow(SosUiState())
    val sosState: StateFlow<SosUiState> = _sosState.asStateFlow()

    private var sosCountdownJob: Job? = null

    private val _helplines = MutableStateFlow(VerifiedHelplines.list)
    val helplines: StateFlow<List<HelplineItem>> = _helplines.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedHelplineCategory = MutableStateFlow("All")
    val selectedHelplineCategory: StateFlow<String> = _selectedHelplineCategory.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = "welcome",
                sender = MessageSender.ASSISTANT,
                text = welcomeText()
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        viewModelScope.launch {
            refreshLocation()
        }
    }

    private fun welcomeText(): String = tr(
        "Namaste. I am Rakshak. Your safety is my absolute priority.\n\nAsk me for safety guidance, legal reporting channels (POSH, POCSO, Cybercrime 1930), or evidence preservation tips.\n\n⚠️ In active danger, dial 112 immediately.",
        "नमस्ते। मैं रक्षक हूँ। आपकी सुरक्षा मेरी सर्वोच्च प्राथमिकता है।\n\nमुझसे सुरक्षा मार्गदर्शन, कानूनी शिकायत के रास्ते (POSH, POCSO, साइबर क्राइम 1930) या सबूत सुरक्षित रखने के सुझाव पूछें।\n\n⚠️ वास्तविक खतरे में तुरंत 112 डायल करें।",
        "নমস্কার। আমি রক্ষক। আপনার নিরাপত্তা আমার সর্বোচ্চ অগ্রাধিকার।\n\nআমাকে নিরাপত্তা নির্দেশনা, আইনি অভিযোগের পথ (POSH, POCSO, সাইবার ক্রাইম ১৯৩০) বা প্রমাণ সংরক্ষণের পরামর্শ জিজ্ঞাসা করুন।\n\n⚠️ সক্রিয় বিপদে তাৎক্ষণিক ১১২ ডায়াল করুন।"
    )

    fun refreshWelcomeMessage() {
        val list = _chatMessages.value
        if (list.isNotEmpty() && list.first().id == "welcome") {
            _chatMessages.value = listOf(list.first().copy(text = welcomeText())) + list.drop(1)
        }
    }

    fun setGoogleAuth(idToken: String, email: String) {
        _googleIdToken.value = idToken
        _signedInEmail.value = email
    }

    fun signOut() {
        _googleIdToken.value = null
        _signedInEmail.value = null
    }

    fun refreshLocation() {
        viewModelScope.launch {
            val loc = locationHelper.getCurrentLocation()
            _currentLocation.value = loc
        }
    }

    fun toggleLiveLocationTracking(enable: Boolean) {
        _isLiveLocationTracking.value = enable
        if (enable) {
            liveLocationJob?.cancel()
            liveLocationJob = viewModelScope.launch {
                locationHelper.getLocationUpdates(intervalMs = 4000L).collect { loc ->
                    _currentLocation.value = loc
                }
            }
        } else {
            liveLocationJob?.cancel()
            liveLocationJob = null
        }
    }

    fun startSosCountdown(onTrigger: () -> Unit) {
        if (_sosState.value.isCountingDown) return
        EmergencyAlertHelper.triggerSosVibration(getApplication())
        _sosState.value = _sosState.value.copy(
            isCountingDown = true,
            countdownRemaining = 3,
            alertDispatched = false,
            statusMessage = "Alerting in 3 seconds..."
        )

        sosCountdownJob?.cancel()
        sosCountdownJob = viewModelScope.launch {
            for (i in 3 downTo 1) {
                _sosState.value = _sosState.value.copy(
                    countdownRemaining = i,
                    statusMessage = "Sending alert in $i s..."
                )
                delay(1000L)
            }
            _sosState.value = _sosState.value.copy(isCountingDown = false)
            triggerSosAlertImmediate()
            onTrigger()
        }
    }

    fun cancelSosCountdown() {
        sosCountdownJob?.cancel()
        sosCountdownJob = null
        _sosState.value = _sosState.value.copy(
            isCountingDown = false,
            statusMessage = "SOS alert cancelled."
        )
    }

    fun triggerSosAlertImmediate(evidenceNote: String? = null) {
        sosCountdownJob?.cancel()
        sosCountdownJob = null

        val app = getApplication<Application>()
        EmergencyAlertHelper.triggerSosVibration(app)

        val message = EmergencyAlertHelper.buildEmergencyMessage(
            customPrefix = _sosState.value.customEmergencyMessage,
            location = _currentLocation.value,
            evidenceAttachedNote = evidenceNote
        )

        val contactList = contacts.value
        val dispatched = EmergencyAlertHelper.dispatchSosAlert(app, contactList, message)

        _sosState.value = _sosState.value.copy(
            isCountingDown = false,
            alertDispatched = dispatched,
            statusMessage = if (contactList.isNotEmpty()) {
                "Emergency SOS dispatched to ${contactList.size} trusted contacts."
            } else {
                "Emergency SOS ready to share. Please add trusted contacts for direct SMS."
            }
        )
    }

    fun updateCustomEmergencyMessage(message: String) {
        _sosState.value = _sosState.value.copy(customEmergencyMessage = message)
    }

    fun addContact(name: String, phone: String, relationship: String, isPrimary: Boolean) {
        viewModelScope.launch {
            val contact = TrustedContact(
                name = name.trim(),
                phone = phone.trim(),
                relationship = relationship.trim(),
                isPrimary = isPrimary
            )
            val newId = contactDao.insertContact(contact)
            if (isPrimary) {
                contactDao.clearOtherPrimaryFlags(newId)
            }
        }
    }

    fun updateContact(contact: TrustedContact) {
        viewModelScope.launch {
            contactDao.updateContact(contact)
            if (contact.isPrimary) {
                contactDao.clearOtherPrimaryFlags(contact.id)
            }
        }
    }

    fun deleteContact(contact: TrustedContact) {
        viewModelScope.launch {
            contactDao.deleteContact(contact)
        }
    }

    fun saveIncidentReport(
        category: String,
        description: String,
        locationText: String,
        perpetratorDetails: String,
        evidenceNotes: String,
        mediaFilePath: String? = null
    ) {
        viewModelScope.launch {
            val report = IncidentReport(
                category = category,
                description = description,
                locationDescription = locationText.ifBlank {
                    _currentLocation.value?.toFormattedCoordinates() ?: "Location not captured"
                },
                latitude = _currentLocation.value?.latitude,
                longitude = _currentLocation.value?.longitude,
                perpetratorDetails = perpetratorDetails,
                evidenceNotes = evidenceNotes,
                mediaFilePath = mediaFilePath
            )
            incidentReportDao.insertReport(report)
        }
    }

    fun deleteIncidentReport(report: IncidentReport) {
        viewModelScope.launch {
            incidentReportDao.deleteReport(report)
        }
    }

    fun setHelplineCategory(category: String) {
        _selectedHelplineCategory.value = category
        filterHelplines()
    }

    fun setHelplineSearch(query: String) {
        _searchQuery.value = query
        filterHelplines()
    }

    private fun filterHelplines() {
        val q = _searchQuery.value.trim().lowercase()
        val cat = _selectedHelplineCategory.value

        _helplines.value = VerifiedHelplines.list.filter { item ->
            val matchCat = cat == "All" || item.category == cat
            val matchQuery = q.isEmpty() ||
                    item.name.lowercase().contains(q) ||
                    item.number.contains(q) ||
                    item.description.lowercase().contains(q)
            matchCat && matchQuery
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val currentHistory = _chatMessages.value
        val userMsg = ChatMessage(sender = MessageSender.USER, text = text.trim())
        _chatMessages.value = currentHistory + userMsg

        _isAiLoading.value = true
        viewModelScope.launch {
            val assistantResponse = aiService.getSafetyGuidance(
                userQuery = userMsg.text,
                history = currentHistory,
                idToken = _googleIdToken.value,
                lang = AppLanguage.current.code
            )
            _chatMessages.value = _chatMessages.value + assistantResponse
            _isAiLoading.value = false
        }
    }

    fun shareRecordedFile(file: File) {
        mediaHelper.shareEvidenceFile(getApplication(), file)
    }
}