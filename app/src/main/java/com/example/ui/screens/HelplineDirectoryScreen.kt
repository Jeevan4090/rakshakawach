package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmergencyAlertHelper
import com.example.data.entity.HelplineItem
import com.example.ui.AppLang
import com.example.ui.AppLanguage
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.CrimsonPrimary
import com.example.ui.theme.SafeGreen
import com.example.ui.tr

@Composable
fun HelplineDirectoryScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val helplines by viewModel.helplines.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedHelplineCategory.collectAsState()

    val categories = listOf(
        Triple("All", "सभी", "সব"),
        Triple("Emergency", "आपातकाल", "জরুরি"),
        Triple("Women Safety", "महिला सुरक्षा", "নারী সুরক্ষা"),
        Triple("Medical", "चिकित्सा", "চিকিৎসা"),
        Triple("Cybercrime", "साइबर क्राइम", "সাইবার ক্রাইম"),
        Triple("Child Protection", "बाल सुरक्षा", "শিশু সুরক্ষা"),
        Triple("Regional (WB/Kolkata)", "क्षेत्रीय (WB/कोलकाता)", "আঞ্চলিক (পশ্চিমবঙ্গ/কলকাতা)")
    )

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr(
                    "Verified Helpline Directory",
                    "सत्यापित हेल्पलाइन निर्देशिका",
                    "যাচাইকৃত হেল্পলাইন ডিরেক্টরি"
                ),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("helpline_directory_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setHelplineSearch(it) },
                    label = {
                        Text(
                            tr(
                                "Search helpline name, number, or keyword...",
                                "हेल्पलाइन नाम, नंबर या कीवर्ड खोजें...",
                                "হেল্পলাইনের নাম, নম্বর বা কীওয়ার্ড খুঁজুন..."
                            )
                        )
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("helpline_search_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Category Filter Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(categories) { (en, hi, bn) ->
                        val labelText = when (AppLanguage.current) {
                            AppLang.HINDI -> hi
                            AppLang.BENGALI -> bn
                            AppLang.ENGLISH -> en
                        }
                        FilterChip(
                            selected = selectedCategory == en,
                            onClick = { viewModel.setHelplineCategory(en) },
                            label = { Text(labelText, fontSize = 12.sp) },
                            modifier = Modifier.testTag("filter_chip_$en")
                        )
                    }
                }
            }

            // Accuracy & Regional Disclaimer
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tr(
                                "All helplines are verified against official government directories. In case a regional line is congested, 112 functions as the single national fallback.",
                                "सभी हेल्पलाइन आधिकारिक सरकारी निर्देशिकाओं से सत्यापित हैं। यदि कोई क्षेत्रीय लाइन व्यस्त हो, तो 112 राष्ट्रीय आपातकालीन नंबर के रूप में काम करता है।",
                                "সমস্ত হেল্পলাইন সরকারি ডিরেক্টরি থেকে যাচাইকৃত। কোনো আঞ্চলিক লাইন ব্যস্ত থাকলে 112 জাতীয় জরুরি নম্বর হিসেবে কাজ করে।"
                            ),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Helpline items list
            items(helplines) { helpline ->
                HelplineCard(
                    helpline = helpline,
                    onCall = { EmergencyAlertHelper.dialNumber(context, helpline.number) },
                    onCallAlt = helpline.alternativeNumber?.let { alt ->
                        { EmergencyAlertHelper.dialNumber(context, alt) }
                    }
                )
            }
        }
    }
}

@Composable
fun HelplineCard(
    helpline: HelplineItem,
    onCall: () -> Unit,
    onCallAlt: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("helpline_item_${helpline.number}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = helpline.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = helpline.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Prominent Phone Number Badge
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = helpline.number,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = helpline.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Availability & verified badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SafeGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = tr(
                        "Availability: ${helpline.availability}",
                        "उपलब्धता: ${helpline.availability}",
                        "উপলব্ধতা: ${helpline.availability}"
                    ),
                    fontSize = 11.sp,
                    color = SafeGreen,
                    fontWeight = FontWeight.Medium
                )
            }

            if (helpline.verifiedNote != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "• ${helpline.verifiedNote}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onCall,
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("dial_button_${helpline.number}")
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tr(
                            "TAP TO CALL ${helpline.number}",
                            "${helpline.number} पर कॉल करें",
                            "${helpline.number}-এ কল করুন"
                        ),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (onCallAlt != null && helpline.alternativeNumber != null) {
                    OutlinedButton(
                        onClick = onCallAlt,
                        modifier = Modifier.testTag("alt_dial_button_${helpline.number}")
                    ) {
                        Text(
                            text = tr(
                                "Alt (${helpline.alternativeNumber})",
                                "अन्य (${helpline.alternativeNumber})",
                                "বিকল্প (${helpline.alternativeNumber})"
                            ),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}