package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.RemoteViews
import android.widget.Toast
import com.example.R

class SosWidgetProvider : AppWidgetProvider() {

    companion object {
        const val ACTION_SOS_TAP = "com.aistudio.rakshakavach.ACTION_SOS_TAP"
        private const val PREFS_NAME = "sos_widget_prefs"
        private const val KEY_TAP_COUNT = "tap_count"
        private const val KEY_LAST_TAP_TIME = "last_tap_time"
        private const val TAP_WINDOW_MS = 3000L // 3 seconds to complete 3 taps
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            val intent = Intent(context, SosWidgetProvider::class.java).apply {
                action = ACTION_SOS_TAP
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val views = RemoteViews(context.packageName, R.layout.widget_sos_shortcut).apply {
                setOnClickPendingIntent(R.id.sos_widget_root, pendingIntent)
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_SOS_TAP) {
            handleTap(context)
        }
    }

    private fun handleTap(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()
        val lastTap = prefs.getLong(KEY_LAST_TAP_TIME, 0L)
        val currentCount = if (now - lastTap <= TAP_WINDOW_MS) {
            prefs.getInt(KEY_TAP_COUNT, 0) + 1
        } else {
            1
        }

        if (currentCount >= 3) {
            // Reset counter and trigger SOS
            prefs.edit().putInt(KEY_TAP_COUNT, 0).putLong(KEY_LAST_TAP_TIME, 0L).apply()
            vibrate(context, 400L)
            triggerEmergency(context)
        } else {
            prefs.edit().putInt(KEY_TAP_COUNT, currentCount).putLong(KEY_LAST_TAP_TIME, now).apply()
            vibrate(context, 50L)
            val remaining = 3 - currentCount
            Toast.makeText(context, "Tap $remaining more time(s) for SOS", Toast.LENGTH_SHORT).show()
        }
    }

    private fun triggerEmergency(context: Context) {
        // Launches MainActivity with an SOS flag so your app immediately fires the SOS flow
        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("TRIGGER_SOS_IMMEDIATELY", true)
        }
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        }
    }

    private fun vibrate(context: Context, durationMs: Long) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(durationMs)
        }
    }
}