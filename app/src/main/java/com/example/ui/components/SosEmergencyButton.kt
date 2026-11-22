package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SosUiState
import com.example.ui.theme.CrimsonDark
import com.example.ui.theme.CrimsonLight
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.GoldPrimary
import com.example.ui.tr

@Composable
fun SosEmergencyButton(
    sosState: SosUiState,
    onSosClick: () -> Unit,
    onCancelCountdown: () -> Unit,
    onImmediateTrigger: () -> Unit,
    onDirectCall112: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (sosState.isCountingDown) 1.15f else 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (sosState.isCountingDown) 500 else 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (sosState.isCountingDown) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = CrimsonDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = tr(
                            "🚨 SENDING SOS IN ${sosState.countdownRemaining}s",
                            "🚨 ${sosState.countdownRemaining} सेकंड में SOS भेजा जा रहा है"
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tr(
                            "Contacts will receive your GPS coordinates & map link.",
                            "संपर्कों को आपकी GPS लोकेशन और मैप लिंक मिल जाएगा।"
                        ),
                        color = Color(0xFFFFDAD6),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = onImmediateTrigger,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier.testTag("sos_send_now_button")
                        ) {
                            Text(
                                text = tr("Send Now", "अभी भेजें"),
                                color = CrimsonPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        OutlinedButton(
                            onClick = onCancelCountdown,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            modifier = Modifier.testTag("sos_cancel_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(tr("Cancel", "रद्द करें"), color = Color.White)
                        }
                    }
                }
            }
        }

        // Circular Pulse SOS Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(vertical = 14.dp)
                .size(180.dp)
        ) {
            // Outer radiant aura
            Box(
                modifier = Modifier
                    .size(175.dp)
                    .scale(pulseScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                CrimsonLight.copy(alpha = 0.45f),
                                CrimsonPrimary.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Middle border ring
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(3.dp, GoldPrimary.copy(alpha = 0.8f), CircleShape)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(CrimsonLight, CrimsonDark)
                        )
                    )
                    .clickable { onSosClick() }
                    .testTag("sos_main_button"),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = tr("Emergency Alert", "आपातकालीन अलर्ट"),
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (sosState.isCountingDown) "${sosState.countdownRemaining}" else "SOS",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = if (sosState.isCountingDown) 38.sp else 32.sp,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = tr("PRESS FOR HELP", "मदद के लिए दबाएँ"),
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // 112 Direct Dial Option (Never delayed)
        Button(
            onClick = onDirectCall112,
            colors = ButtonDefaults.buttonColors(
                containerColor = CrimsonPrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(48.dp)
                .testTag("direct_call_112_button")
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = tr("CALL 112 DIRECT EMERGENCY", "112 पर सीधी आपातकालीन कॉल"),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        }
    }
}