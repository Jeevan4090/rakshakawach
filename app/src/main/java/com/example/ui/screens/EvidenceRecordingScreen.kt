package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.SafeGreen
import com.example.ui.tr
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EvidenceRecordingScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val audioState by viewModel.mediaHelper.audioState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Audio SOS, 1: Video SOS

    var audioFiles by remember { mutableStateOf(emptyList<File>()) }
    var videoFiles by remember { mutableStateOf(emptyList<File>()) }

    fun refreshFiles() {
        audioFiles = viewModel.mediaHelper.listRecordedAudioFiles()
        videoFiles = viewModel.mediaHelper.listRecordedVideoFiles()
    }

    LaunchedEffect(audioState.isRecording) {
        refreshFiles()
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.mediaHelper.startAudioRecording()
        }
    }

    val videoPermissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle video permission
    }

    val pulseTransition = rememberInfiniteTransition(label = "pulse_record")
    val recordScale by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rec_scale"
    )

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("Evidence Recording (Audio/Video)", "सबूत रिकॉर्डिंग (ऑडियो/वीडियो)"),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("evidence_recording_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Legal Transparency & Platform Rule Compliance Banner
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
                            text = tr(
                                "Legal Compliance: Evidence recordings are activated only upon your explicit tap. No secret recording occurs. Files are stored securely on this device's private sandbox and are never uploaded to police or servers without your direction.",
                                "कानूनी पारदर्शिता: सबूत रिकॉर्डिंग केवल आपके स्पष्ट टैप करने पर ही शुरू होती है। कोई गुप्त रिकॉर्डिंग नहीं होती। फ़ाइलें आपके फोन के सुरक्षित स्टोरेज में रहती हैं और आपकी अनुमति के बिना कहीं अपलोड नहीं की जातीं।"
                            ),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Tab Selector: Audio SOS vs Video SOS
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(tr("Audio SOS Evidence", "ऑडियो सबूत"))
                            }
                        },
                        modifier = Modifier.testTag("tab_audio_sos")
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(tr("Video SOS Documentation", "वीडियो सबूत"))
                            }
                        },
                        modifier = Modifier.testTag("tab_video_sos")
                    )
                }
            }

            if (selectedTab == 0) {
                // AUDIO SOS RECORDER CARD
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (audioState.isRecording) {
                                    tr("RECORDING AUDIO EVIDENCE", "ऑडियो सबूत रिकॉर्ड हो रहा है")
                                } else {
                                    tr("Audio SOS Recorder", "ऑडियो SOS रिकॉर्डर")
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = if (audioState.isRecording) CrimsonPrimary else MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Recording Visual Indicator & Duration
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(120.dp)
                                    .padding(8.dp)
                            ) {
                                if (audioState.isRecording) {
                                    Box(
                                        modifier = Modifier
                                            .size(110.dp)
                                            .scale(recordScale)
                                            .clip(CircleShape)
                                            .background(CrimsonPrimary.copy(alpha = 0.2f))
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .background(if (audioState.isRecording) CrimsonPrimary else MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = null,
                                        tint = if (audioState.isRecording) Color.White else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }

                            // Timer Display
                            val minutes = audioState.durationSeconds / 60
                            val seconds = audioState.durationSeconds % 60
                            val timeStr = String.format("%02d:%02d", minutes, seconds)

                            Text(
                                text = timeStr,
                                fontWeight = FontWeight.Black,
                                fontSize = 28.sp,
                                color = if (audioState.isRecording) CrimsonPrimary else MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (!audioState.isRecording) {
                                    Button(
                                        onClick = {
                                            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("start_audio_rec_button")
                                    ) {
                                        Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = tr("START AUDIO RECORDING", "ऑडियो रिकॉर्डिंग शुरू करें"),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            viewModel.mediaHelper.stopAudioRecording()
                                            refreshFiles()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("stop_audio_rec_button")
                                    ) {
                                        Icon(Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = tr("STOP & SECURE AUDIO RECORDING", "रिकॉर्डिंग रोकें और सहेजें"),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Saved Audio Evidence List
                item {
                    Text(
                        text = tr(
                            "SAVED AUDIO EVIDENCE FILES (${audioFiles.size})",
                            "सहेजी गई ऑडियो फ़ाइलें (${audioFiles.size})"
                        ),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                if (audioFiles.isEmpty()) {
                    item {
                        Text(
                            text = tr("No audio evidence recorded yet.", "अभी तक कोई ऑडियो सबूत रिकॉर्ड नहीं किया गया है।"),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                } else {
                    items(audioFiles) { file ->
                        MediaFileCard(
                            file = file,
                            isVideo = false,
                            isPlaying = audioState.isPlaying,
                            onPlayToggle = { viewModel.mediaHelper.toggleAudioPlayback(file) },
                            onShare = { viewModel.shareRecordedFile(file) }
                        )
                    }
                }
            } else {
                // VIDEO SOS CARD
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = tr("Video SOS Documentation", "वीडियो SOS रिकॉर्डिंग"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = tr(
                                    "Record video documentation of approaching individuals, vehicle registration numbers, or surroundings with verified timestamps.",
                                    "पास आने वाले व्यक्तियों, वाहन नंबर प्लेट या आसपास के माहौल का समय के साथ वीडियो सबूत रिकॉर्ड करें।"
                                ),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF3B82F6).copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = Color(0xFF3B82F6),
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    videoPermissionsLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.RECORD_AUDIO
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("launch_video_capture_button")
                            ) {
                                Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(tr("ACTIVATE VIDEO EVIDENCE RECORDING", "वीडियो रिकॉर्डिंग शुरू करें"))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MediaFileCard(
    file: File,
    isVideo: Boolean,
    isPlaying: Boolean,
    onPlayToggle: () -> Unit,
    onShare: () -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(file.lastModified()))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("media_file_${file.name}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPlayToggle) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = tr("Play/Stop", "चलाएँ/रोकें"),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = file.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1
                )
                Text(
                    text = "$dateStr • ${(file.length() / 1024)} KB",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onShare,
                modifier = Modifier.testTag("share_media_${file.name}")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = tr("Share", "साझा करें"),
                    tint = SafeGreen
                )
            }
        }
    }
}