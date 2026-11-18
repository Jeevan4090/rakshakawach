package com.example.data

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AudioRecordingState(
    val isRecording: Boolean = false,
    val durationSeconds: Int = 0,
    val currentFilePath: String? = null,
    val isPlaying: Boolean = false,
    val lastRecordedFile: File? = null,
    val errorMessage: String? = null
)

class MediaRecordingHelper(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _audioState = MutableStateFlow(AudioRecordingState())
    val audioState: StateFlow<AudioRecordingState> = _audioState.asStateFlow()

    private fun getEvidenceFolder(subfolder: String): File {
        val folder = File(context.filesDir, "raksha_evidence/$subfolder")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return folder
    }

    fun startAudioRecording(): Boolean {
        if (_audioState.value.isRecording) return false

        try {
            stopAudioPlayback()

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val audioFile = File(getEvidenceFolder("audio"), "AUD_SOS_$timestamp.m4a")

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(audioFile.absolutePath)
                prepare()
                start()
            }

            mediaRecorder = recorder
            _audioState.value = AudioRecordingState(
                isRecording = true,
                durationSeconds = 0,
                currentFilePath = audioFile.absolutePath,
                lastRecordedFile = audioFile
            )

            timerJob?.cancel()
            timerJob = scope.launch {
                var seconds = 0
                while (isActive && _audioState.value.isRecording) {
                    delay(1000L)
                    seconds++
                    _audioState.value = _audioState.value.copy(durationSeconds = seconds)
                }
            }
            return true
        } catch (e: Exception) {
            _audioState.value = _audioState.value.copy(
                isRecording = false,
                errorMessage = "Recording error: ${e.message}"
            )
            mediaRecorder?.release()
            mediaRecorder = null
            return false
        }
    }

    fun stopAudioRecording(): File? {
        timerJob?.cancel()
        timerJob = null

        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            val currentFile = _audioState.value.lastRecordedFile
            _audioState.value = _audioState.value.copy(
                isRecording = false,
                lastRecordedFile = currentFile
            )
            currentFile
        } catch (e: Exception) {
            mediaRecorder?.release()
            mediaRecorder = null
            _audioState.value = _audioState.value.copy(isRecording = false, errorMessage = e.message)
            null
        }
    }

    fun toggleAudioPlayback(file: File) {
        if (_audioState.value.isPlaying) {
            stopAudioPlayback()
        } else {
            startAudioPlayback(file)
        }
    }

    private fun startAudioPlayback(file: File) {
        try {
            stopAudioPlayback()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(file.absolutePath)
                prepare()
                start()
                setOnCompletionListener {
                    _audioState.value = _audioState.value.copy(isPlaying = false)
                }
            }
            _audioState.value = _audioState.value.copy(isPlaying = true)
        } catch (e: Exception) {
            _audioState.value = _audioState.value.copy(errorMessage = "Playback failed: ${e.message}")
        }
    }

    fun stopAudioPlayback() {
        try {
            mediaPlayer?.apply {
                if (isPlaying) stop()
                release()
            }
        } catch (ignored: Exception) {
        } finally {
            mediaPlayer = null
            _audioState.value = _audioState.value.copy(isPlaying = false)
        }
    }

    fun listRecordedAudioFiles(): List<File> {
        val folder = getEvidenceFolder("audio")
        return folder.listFiles()?.filter { it.extension == "m4a" }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }

    fun listRecordedVideoFiles(): List<File> {
        val folder = getEvidenceFolder("video")
        return folder.listFiles()?.filter { it.extension == "mp4" }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }

    fun shareEvidenceFile(context: Context, file: File) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = if (file.extension == "mp4") "video/*" else "audio/*"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val chooser = Intent.createChooser(intent, "Share Evidence Securely").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            // Direct share fallback
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "*/*"
                putExtra(Intent.EXTRA_TEXT, "Raksha Kavach Evidence File: ${file.name}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(Intent.createChooser(intent, "Share Evidence"))
        }
    }

    fun createVideoOutputFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File(getEvidenceFolder("video"), "VID_SOS_$timestamp.mp4")
    }
}
