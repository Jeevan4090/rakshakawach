package com.example.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import com.example.data.entity.TrustedContact
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EmergencyAlertHelper {

    fun dialNumber(context: Context, number: String) {
        try {
            val cleanNumber = number.replace(Regex("[^0-9+]"), "")
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$cleanNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to dial: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openMapSearch(context: Context, query: String, location: UserLocation? = null) {
        try {
            val uri = if (location != null) {
                Uri.parse("geo:${location.latitude},${location.longitude}?q=${Uri.encode(query)}")
            } else {
                Uri.parse("geo:0,0?q=${Uri.encode(query)}")
            }
            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            // Web fallback
            val webUri = if (location != null) {
                Uri.parse("https://www.google.com/maps/search/${Uri.encode(query)}/@${location.latitude},${location.longitude},15z")
            } else {
                Uri.parse("https://www.google.com/maps/search/${Uri.encode(query)}")
            }
            openWebUrl(context, webUri.toString())
        }
    }

    fun openWebUrl(context: Context, url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open browser: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun buildEmergencyMessage(
        customPrefix: String,
        location: UserLocation?,
        evidenceAttachedNote: String? = null
    ): String {
        val timeFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val timeStr = timeFormat.format(Date())

        val builder = StringBuilder()
        builder.append("🚨 EMERGENCY SOS — RAKSHA KAVACH 🚨\n\n")
        builder.append(customPrefix.ifBlank { "I need help. This is an emergency. Please contact me immediately and notify emergency services if needed." })
        builder.append("\n\n⏱️ Timestamp: $timeStr\n")

        if (location != null) {
            builder.append("📍 My Live Location:\n")
            builder.append("Coordinates: ${location.latitude}, ${location.longitude}\n")
            builder.append("Google Maps: ${location.toMapsUrl()}\n")
            builder.append("Accuracy: ±${location.accuracyMeters.toInt()}m\n")
        } else {
            builder.append("📍 Location: GPS coordinates unavailable or acquiring.\n")
        }

        if (!evidenceAttachedNote.isNullOrBlank()) {
            builder.append("\n📁 Evidence recorded on device: $evidenceAttachedNote\n")
        }

        builder.append("\n— Sent via Raksha Kavach Women's Safety App")
        return builder.toString()
    }

    fun dispatchSosAlert(
        context: Context,
        contacts: List<TrustedContact>,
        messageText: String
    ): Boolean {
        return try {
            val phoneNumbers = contacts.map { it.phone }.filter { it.isNotBlank() }
            if (phoneNumbers.isNotEmpty()) {
                val smsUri = Uri.parse("smsto:${phoneNumbers.joinToString(";")}")
                val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri).apply {
                    putExtra("sms_body", messageText)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(smsIntent)
                true
            } else {
                // Fallback to general Share intent
                shareSosMessage(context, messageText)
                true
            }
        } catch (e: Exception) {
            shareSosMessage(context, messageText)
            true
        }
    }

    fun shareSosMessage(context: Context, messageText: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "EMERGENCY SOS ALERT")
            putExtra(Intent.EXTRA_TEXT, messageText)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(shareIntent, "Send Emergency SOS Alert Via").apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(chooser)
    }

    fun triggerSosVibration(context: Context) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                // S-O-S in Morse Code: ... --- ...
                val timings = longArrayOf(0, 150, 100, 150, 100, 150, 250, 400, 100, 400, 100, 400, 250, 150, 100, 150, 100, 150)
                val amplitudes = intArrayOf(0, 255, 0, 255, 0, 255, 0, 255, 0, 255, 0, 255, 0, 255, 0, 255, 0, 255)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createWaveform(timings, amplitudes, -1)
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(timings, -1)
                }
            }
        } catch (ignored: Exception) {
        }
    }
}
