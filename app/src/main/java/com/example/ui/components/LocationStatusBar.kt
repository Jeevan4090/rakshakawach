package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserLocation
import com.example.ui.theme.NavyCard
import com.example.ui.theme.SafeGreen

@Composable
fun LocationStatusBar(
    hasPermission: Boolean,
    currentLocation: UserLocation?,
    isLiveTracking: Boolean,
    onRefreshLocation: () -> Unit,
    onRequestPermission: () -> Unit,
    onClickBar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickBar() }
            .testTag("location_status_bar"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isLiveTracking -> Color(0xFF38BDF8) // Sky blue
                                hasPermission && currentLocation != null -> SafeGreen
                                hasPermission -> Color(0xFFFBBF24) // Yellow
                                else -> Color(0xFFEF4444) // Red
                            }
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = when {
                            isLiveTracking -> "Live Tracking Active"
                            hasPermission && currentLocation != null -> "GPS Location Secured"
                            hasPermission -> "Acquiring GPS Signal..."
                            else -> "Location Permission Needed"
                        },
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                    Text(
                        text = if (currentLocation != null) {
                            "${String.format("%.4f", currentLocation.latitude)}, ${String.format("%.4f", currentLocation.longitude)} (±${currentLocation.accuracyMeters.toInt()}m)"
                        } else if (!hasPermission) {
                            "Tap to enable GPS for instant SOS sharing"
                        } else {
                            "Tap to view live coordinates"
                        },
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            IconButton(
                onClick = {
                    if (!hasPermission) onRequestPermission() else onRefreshLocation()
                },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (!hasPermission) Icons.Default.Warning else Icons.Default.Refresh,
                    contentDescription = "Refresh GPS",
                    tint = if (!hasPermission) Color(0xFFEF4444) else Color(0xFF38BDF8),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
