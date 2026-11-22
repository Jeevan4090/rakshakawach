package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GuideViolet
import com.example.ui.tr

@Composable
fun BetaPadhaoScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(1) } // 0 = Beta Padhao, 1 = Beti Bachao
    var pledged by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = tr("Back", "वापस"),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column {
                    Text(
                        text = tr("Beta Padhao • Beti Bachao", "बेटा पढ़ाओ • बेटी बचाओ"),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = tr(
                            "Empowerment & Gender Sensitization",
                            "सशक्तिकरण और लैंगिक संवेदनशीलता"
                        ),
                        fontSize = 12.sp,
                        color = GuideViolet
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Gradient Hero Banner
            item {
                val gradient = Brush.linearGradient(
                    colors = listOf(Color(0xFF1A1446), Color(0xFF5B21B6), Color(0xFF1E1B4B))
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(gradient)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.18f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tr("National Movement for Equality", "समानता का राष्ट्रीय अभियान"),
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp
                    )
                }
            }

            // 2-Tab Switcher
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TabButton(
                            title = tr("Beta Padhao\n(Sensitization)", "बेटा पढ़ाओ\n(जागरूकता)"),
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            title = tr("Beti Bachao (Schemes)", "बेटी बचाओ (योजनाएँ)"),
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (selectedTab == 1) {
                // Beti Bachao (Schemes)
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFD93848).copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.School,
                                        contentDescription = null,
                                        tint = Color(0xFFD93848),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = tr(
                                            "Beti Bachao, Beti Padhao (BBBP)",
                                            "बेटी बचाओ, बेटी पढ़ाओ (BBBP)"
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = tr(
                                            "Protect, nurture, and educate the girl child",
                                            "बेटियों की सुरक्षा, पोषण और शिक्षा"
                                        ),
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = tr(
                                    "Launched by the Government of India, the Beti Bachao Beti Padhao flagship initiative tackles skewed sex ratios, ensures female survival, guarantees child protection, and enables equal higher education for every girl in India.",
                                    "भारत सरकार द्वारा शुरू की गई 'बेटी बचाओ, बेटी पढ़ाओ' पहल गिरते लिंगानुपात को सुधारने, बालिकाओं की सुरक्षा सुनिश्चित करने और हर बेटी को समान उच्च शिक्षा के अवसर दिलाने के लिए कार्य करती है।"
                                ),
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = tr("Official Welfare Schemes & Benefits", "सरकारी कल्याणकारी योजनाएँ और लाभ"),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    SchemeCard(
                        title = tr("Sukanya Samriddhi Yojana (SSY)", "सुकन्या समृद्धि योजना (SSY)"),
                        subtitle = tr(
                            "High-interest sovereign savings account for daughters",
                            "बेटियों के लिए उच्च ब्याज दर वाला सरकारी बचत खाता"
                        ),
                        bullets = listOf(
                            tr(
                                "Guaranteed government interest rate (up to 8.2% p.a.).",
                                "सरकार द्वारा गारंटीड ब्याज दर (8.2% प्रति वर्ष तक)।"
                            ),
                            tr(
                                "Full income tax deduction under Section 80C.",
                                "आयकर अधिनियम की धारा 80C के तहत पूर्ण कर छूट।"
                            ),
                            tr(
                                "Account can be opened for girls below 10 years of age.",
                                "10 वर्ष से कम उम्र की बेटियों के लिए खाता खोला जा सकता है।"
                            )
                        )
                    )
                }

                item {
                    SchemeCard(
                        title = tr("One Stop Centre (Sakhi)", "वन स्टॉप सेंटर (सखी)"),
                        subtitle = tr(
                            "Integrated support for women affected by violence",
                            "हिंसा से प्रभावित महिलाओं के लिए एकीकृत सहायता"
                        ),
                        bullets = listOf(
                            tr(
                                "Free emergency medical, legal, and police assistance under one roof.",
                                "एक ही छत के नीचे निःशुल्क चिकित्सा, कानूनी और पुलिस सहायता।"
                            ),
                            tr(
                                "Temporary shelter and psycho-social counselling.",
                                "अस्थायी आश्रय और मनो-सामाजिक परामर्श।"
                            ),
                            tr(
                                "Accessible 24x7 via Women Helpline 181.",
                                "महिला हेल्पलाइन 181 के माध्यम से 24x7 उपलब्ध।"
                            )
                        )
                    )
                }

                item {
                    SchemeCard(
                        title = tr("CBSE Udaan & Pragati Scholarships", "CBSE उड़ान और प्रगति छात्रवृत्ति"),
                        subtitle = tr(
                            "STEM & higher education support for girls",
                            "बेटियों के लिए STEM और उच्च शिक्षा सहायता"
                        ),
                        bullets = listOf(
                            tr(
                                "Free mentoring and resources for engineering entrance exams.",
                                "इंजीनियरिंग प्रवेश परीक्षाओं के लिए निःशुल्क मार्गदर्शन और संसाधन।"
                            ),
                            tr(
                                "AICTE Pragati Scholarship offers ₹50,000/year for technical degrees.",
                                "AICTE प्रगति छात्रवृत्ति तकनीकी डिग्री के लिए ₹50,000/वर्ष प्रदान करती है।"
                            )
                        )
                    )
                }
            } else {
                // Beta Padhao (Sensitization)
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                    tint = GuideViolet
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = tr("Raising Respectful Sons", "संस्कारित और सम्मानजनक बेटों की परवरिश"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = tr(
                                    "• Teach consent, boundaries, and empathy from childhood.\n" +
                                    "• Challenge casual sexism and disrespectful jokes at home and school.\n" +
                                    "• Share household responsibilities equally regardless of gender.\n" +
                                    "• Speak up as an active bystander when witnessing harassment.",
                                    "• बचपन से ही सहमति, सीमाओं और सहानुभूति का महत्व सिखाएँ।\n" +
                                    "• घर और स्कूल में लैंगिक भेदभाव और अपमानजनक मज़ाक का विरोध करें।\n" +
                                    "• घर की जिम्मेदारियाँ बिना किसी भेदभाव के बराबर बाँटें।\n" +
                                    "• कहीं भी उत्पीड़न होते देखें तो चुप न रहें, आवाज़ उठाएँ।"
                                ),
                                fontSize = 13.sp,
                                lineHeight = 21.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = tr("Take the Equality Pledge", "समानता की शपथ लें"),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = GuideViolet
                            )
                            Text(
                                text = tr(
                                    "I commit to treating every woman and girl with dignity, educating those around me on respect, and helping build safer public and digital spaces.",
                                    "मैं हर महिला और बालिका का सम्मान करने, अपने आसपास के लोगों को सम्मान के प्रति जागरूक करने और सुरक्षित सार्वजनिक व डिजिटल वातावरण बनाने की शपथ लेता/लेती हूँ।"
                                ),
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Button(
                                onClick = {
                                    pledged = true
                                    Toast.makeText(
                                        context,
                                        tr("Thank you for taking the pledge!", "शपथ लेने के लिए धन्यवाद!"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GuideViolet),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Outlined.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    if (pledged) {
                                        tr("Pledge Taken", "शपथ ली गई")
                                    } else {
                                        tr("I Take This Pledge", "मैं यह शपथ लेता/लेती हूँ")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(top = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = GuideViolet,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(if (selected) GuideViolet else Color.Transparent)
        )
    }
}

@Composable
private fun SchemeCard(
    title: String,
    subtitle: String,
    bullets: List<String>
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GuideViolet
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            bullets.forEach { point ->
                Text(
                    text = "•  $point",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}