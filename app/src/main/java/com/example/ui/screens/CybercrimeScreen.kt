package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EmergencyAlertHelper
import com.example.ui.RakshaViewModel
import com.example.ui.components.RakshaTopAppBar
import com.example.ui.theme.SageDeep
import com.example.ui.theme.SaffronDeep
import com.example.ui.theme.SaffronTint
import com.example.ui.tr

@Composable
fun CybercrimeScreen(
    viewModel: RakshaViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToReports: () -> Unit
) {
    val context = LocalContext.current

    val checklistState = remember {
        mutableStateMapOf(
            "screenshots" to false,
            "url_profile" to false,
            "txn_details" to false,
            "bank_contacted" to false,
            "two_factor" to false,
            "account_blocked" to false
        )
    }

    var incidentText by remember { mutableStateOf("") }
    var suspectHandle by remember { mutableStateOf("") }
    var platformName by remember { mutableStateOf("") }
    var incidentError by remember { mutableStateOf(false) }
    var isSaved by remember { mutableStateOf(false) }

    val doneCount = checklistState.values.count { it }

    Scaffold(
        topBar = {
            RakshaTopAppBar(
                title = tr("Cybercrime help", "साइबर क्राइम सहायता", "সাইবার ক্রাইম সহায়তা"),
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("cybercrime_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "1930") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("cyber_dial_1930_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                tr("Call 1930", "1930 पर कॉल करें", "1930-এ কল করুন"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(tr("Cyber fraud", "साइबर धोखाधड़ी", "সাইবার জালিয়াতি"), fontSize = 11.sp)
                        }
                    }

                    OutlinedButton(
                        onClick = { EmergencyAlertHelper.dialNumber(context, "112") },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("cyber_dial_112_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Security,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                tr("Call 112", "112 पर कॉल करें", "112-এ কল করুন"),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(tr("Police", "पुलिस", "পুলিশ"), fontSize = 11.sp)
                        }
                    }
                }
            }

            item {
                NoticeCard(
                    icon = Icons.Outlined.AccountBalance,
                    title = tr("Lost money to fraud?", "धोखाधड़ी में पैसे चले गए?", "প্রতারণায় টাকা হারিয়েছেন?"),
                    text = tr(
                        "Call 1930 as soon as you can. The first few hours matter most for freezing the receiving account. Then call your bank to block cards and net banking. Rakshakavach never asks for OTPs, PINs, or banking passwords.",
                        "जितनी जल्दी हो सके 1930 पर कॉल करें। धोखेबाज़ का खाता फ्रीज़ कराने के लिए शुरुआती कुछ घंटे सबसे अहम होते हैं। इसके बाद अपने बैंक को कॉल करके कार्ड और नेट बैंकिंग ब्लॉक कराएँ। रक्षाकवच कभी भी OTP, PIN या बैंकिंग पासवर्ड नहीं माँगता।",
                        "যত দ্রুত সম্ভব 1930-এ কল করুন। প্রতারকের অ্যাকাউন্ট ফ্রিজ করার জন্য প্রথম কয়েক ঘণ্টা সবচেয়ে গুরুত্বপূর্ণ। এরপর আপনার ব্যাংকে কল করে কার্ড ও নেট ব্যাংকিং ব্লক করুন। রক্ষাকবচ কখনোই OTP, PIN বা ব্যাংকিং পাসওয়ার্ড চায় না।"
                    ),
                    container = SaffronTint,
                    content = SaffronDeep
                )
            }

            item {
                SurfaceCard {
                    CardHeader(
                        icon = Icons.Outlined.Security,
                        title = tr(
                            "National Cyber Crime Reporting Portal",
                            "राष्ट्रीय साइबर क्राइम रिपोर्टिंग पोर्टल",
                            "জাতীয় সাইবার ক্রাইম রিপোর্টিং পোর্টাল"
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tr(
                            "The Ministry of Home Affairs portal to report cyberstalking, online harassment, obscene content, morphed photos, and financial fraud, formally or anonymously.",
                            "गृह मंत्रालय का आधिकारिक पोर्टल, जहाँ आप साइबर स्टॉकिंग, ऑनलाइन उत्पीड़न, आपत्तिजनक सामग्री, मॉर्फ्ड फोटो और वित्तीय धोखाधड़ी की औपचारिक या गुमनाम शिकायत दर्ज कर सकती हैं।",
                            "স্বরাষ্ট্র মন্ত্রকের পোর্টাল, যেখানে আপনি সাইবার স্টকিং, অনলাইন হয়রানি, অশ্লীল বিষয়বস্তু, মর্ফ করা ছবি এবং আর্থিক প্রতারণার আনুষ্ঠানিক বা বেনামী অভিযোগ দায়ের করতে পারবেন।"
                        ),
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            EmergencyAlertHelper.openWebUrl(context, "https://cybercrime.gov.in/")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("open_cybercrime_portal_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tr("Open cybercrime.gov.in", "cybercrime.gov.in खोलें", "cybercrime.gov.in খুলুন"))
                    }
                }
            }

            item {
                SurfaceCard {
                    CardHeader(
                        icon = Icons.Outlined.Shield,
                        title = tr(
                            "Evidence and account checklist",
                            "सबूत और अकाउंट सुरक्षा चेकलिस्ट",
                            "প্রমাণ ও অ্যাকাউন্ট চেকলিস্ট"
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
    text = tr(
        "$doneCount of ${checklistState.size} done",
        "${checklistState.size} में से $doneCount पूर्ण",
        "${checklistState.size}-এর মধ্যে ${doneCount}টি সম্পন্ন"
    ),
    fontSize = 12.sp,
    color = MaterialTheme.colorScheme.onSurfaceVariant
)
                    Spacer(modifier = Modifier.height(8.dp))

                    CyberChecklistItem(
                        checked = checklistState["screenshots"] == true,
                        onCheckedChange = { checklistState["screenshots"] = it },
                        title = tr(
                            "Screenshots with dates and times",
                            "तारीख और समय के साथ स्क्रीनशॉट",
                            "তারিখ ও সময়সহ স্ক্রিনশট"
                        ),
                        subtitle = tr(
                            "Capture abusive posts, comments, emails, or chats clearly.",
                            "अपमानजनक पोस्ट, कमेंट, ईमेल या चैट का स्पष्ट स्क्रीनशॉट लें।",
                            "অবমাননাকর পোস্ট, মন্তব্য, ইমেল বা চ্যাটের স্পষ্ট স্ক্রিনশট নিন।"
                        )
                    )
                    CyberChecklistItem(
                        checked = checklistState["url_profile"] == true,
                        onCheckedChange = { checklistState["url_profile"] = it },
                        title = tr("Profile links and URLs", "प्रोफ़ाइल लिंक और URL", "প্রোফাইল লিংক ও URL"),
                        subtitle = tr(
                            "Copy links to offending accounts, posts, or groups before they're deleted.",
                            "हटाए जाने से पहले संबंधित अकाउंट, पोस्ट या ग्रुप के लिंक कॉपी कर लें।",
                            "মুছে ফেলার আগেই অভিযুক্ত অ্যাকাউন্ট, পোস্ট বা গ্রুপের লিংক কপি করে রাখুন।"
                        )
                    )
                    CyberChecklistItem(
                        checked = checklistState["txn_details"] == true,
                        onCheckedChange = { checklistState["txn_details"] = it },
                        title = tr(
                            "Transaction and UPI reference numbers",
                            "लेन-देन और UPI रेफरेंस नंबर",
                            "লেনদেন ও UPI রেফারেন্স নম্বর"
                        ),
                        subtitle = tr(
                            "Save debit SMS alerts, UPI references, and bank statements.",
                            "पैसे कटने का SMS, UPI रेफरेंस नंबर और बैंक स्टेटमेंट सुरक्षित रखें।",
                            "টাকা কাটার SMS, UPI রেফারেন্স এবং ব্যাংক স্টেটমেন্ট সংরক্ষণ করুন।"
                        )
                    )
                    CyberChecklistItem(
                        checked = checklistState["bank_contacted"] == true,
                        onCheckedChange = { checklistState["bank_contacted"] = it },
                        title = tr("Bank alerted", "बैंक को सूचित किया", "ব্যাংককে জানানো হয়েছে"),
                        subtitle = tr(
                            "Asked your bank to block or reverse compromised cards.",
                            "बैंक से प्रभावित कार्ड ब्लॉक करने या लेन-देन रोकने के लिए कहा।",
                            "ঝুঁকিপূর্ণ কার্ড ব্লক বা লেনদেন বন্ধ করতে ব্যাংককে অনুরোধ করা হয়েছে।"
                        )
                    )
                    CyberChecklistItem(
                        checked = checklistState["two_factor"] == true,
                        onCheckedChange = { checklistState["two_factor"] = it },
                        title = tr(
                            "Two-factor authentication on",
                            "टू-फैक्टर ऑथेंटिकेशन चालू करें",
                            "টু-ফ্যাক্টর অথেনটিকেশন চালু করুন"
                        ),
                        subtitle = tr(
                            "Secure your email, Instagram, WhatsApp, and other accounts.",
                            "अपना ईमेल, Instagram, WhatsApp और अन्य खाते सुरक्षित करें।",
                            "আপনার ইমেল, Instagram, WhatsApp এবং অন্যান্য অ্যাকাউন্ট সুরক্ষিত করুন।"
                        )
                    )
                    CyberChecklistItem(
                        checked = checklistState["account_blocked"] == true,
                        onCheckedChange = { checklistState["account_blocked"] = it },
                        title = tr(
                            "Reported and blocked on the platform",
                            "प्लेटफ़ॉर्म पर रिपोर्ट और ब्लॉक किया",
                            "প্ল্যাটফর্মে রিপোর্ট ও ব্লক করা হয়েছে"
                        ),
                        subtitle = tr(
                            "Use the app's reporting tools after saving your screenshots.",
                            "स्क्रीनशॉट सहेजने के बाद ऐप के रिपोर्टिंग टूल से अकाउंट ब्लॉक करें।",
                            "স্ক্রিনশট সংরক্ষণের পর অ্যাপের রিপোর্টিং টুল দিয়ে অ্যাকাউন্ট ব্লক করুন।"
                        )
                    )
                }
            }

            item {
                SurfaceCard {
                    CardHeader(
                        icon = Icons.Outlined.Lock,
                        title = tr("Private incident note", "निजी घटना नोट", "ব্যক্তিগত ঘটনার নোট"),
                        tint = SageDeep
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    FormField(
                        value = platformName,
                        onValueChange = {
                            platformName = it
                            isSaved = false
                        },
                        label = tr("Platform", "प्लेटफ़ॉर्म", "প্ল্যাটফর্ম"),
                        placeholder = "WhatsApp, Instagram, Telegram, UPI"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = suspectHandle,
                        onValueChange = {
                            suspectHandle = it
                            isSaved = false
                        },
                        label = tr(
                            "Suspect's handle, number, or URL",
                            "संदिग्ध का हैंडल, नंबर या URL",
                            "সন্দেহভাজনের হ্যান্ডেল, নম্বর বা URL"
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FormField(
                        value = incidentText,
                        onValueChange = {
                            incidentText = it
                            incidentError = false
                            isSaved = false
                        },
                        label = tr("What happened", "क्या हुआ", "কী ঘটেছে"),
                        placeholder = tr(
                            "The timeline, the threat, or the loss",
                            "घटना का समय, धमकी या नुकसान का विवरण",
                            "ঘটনার সময়ক্রম, হুমকি বা ক্ষতির বিবরণ"
                        ),
                        singleLine = false,
                        minLines = 4,
                        error = if (incidentError) {
                            tr(
                                "Describe what happened to save the note",
                                "नोट सहेजने के लिए घटना का विवरण लिखें",
                                "নোট সংরক্ষণ করতে কী ঘটেছে তা লিখুন"
                            )
                        } else null,
                        modifier = Modifier.testTag("cyber_incident_description_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (incidentText.isBlank()) {
                                incidentError = true
                            } else {
                                viewModel.saveIncidentReport(
                                    category = "Cybercrime",
                                    description = incidentText,
                                    locationText = "Platform: $platformName",
                                    perpetratorDetails = suspectHandle,
                                    evidenceNotes = "Checklist completed: $doneCount/${checklistState.size} items"
                                )
                                isSaved = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_cyber_report_button")
                    ) {
                        Text(tr("Save note to vault", "वॉल्ट में नोट सहेजें", "ভল্টে নোট সংরক্ষণ করুন"))
                    }

                    if (isSaved) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tr(
                                    "Note saved to your vault",
                                    "नोट आपके वॉल्ट में सहेजा गया",
                                    "নোট আপনার ভল্টে সংরক্ষিত হয়েছে"
                                ),
                                color = SageDeep,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            TextButton(onClick = onNavigateToReports) {
                                Text(tr("View in vault", "वॉल्ट में देखें", "ভল্টে দেখুন"))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CyberChecklistItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Checkbox) { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.padding(top = 4.dp, end = 4.dp, start = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}