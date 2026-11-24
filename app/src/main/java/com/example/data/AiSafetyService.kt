package com.example.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isUrgentEmergency: Boolean = false,
    val recommendedHelpline: String? = null
)

enum class MessageSender {
    USER,
    ASSISTANT
}

class AiSafetyService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val backendBaseUrl = "https://rakshakavach-backend-k47b.onrender.com"

    suspend fun getSafetyGuidance(
        userQuery: String,
        history: List<ChatMessage> = emptyList(),
        idToken: String? = null,
        lang: String = "en"
    ): ChatMessage = withContext(Dispatchers.IO) {
        val isUrgent = checkUrgentQuery(userQuery)
        val helpline = detectRecommendedHelpline(userQuery)

        if (!idToken.isNullOrBlank()) {
            try {
                val apiResponse = callBackendChat(userQuery, history, idToken, lang)
                if (apiResponse.isNotBlank()) {
                    return@withContext ChatMessage(
                        sender = MessageSender.ASSISTANT,
                        text = apiResponse,
                        isUrgentEmergency = isUrgent,
                        recommendedHelpline = helpline
                    )
                }
            } catch (e: Exception) {
                Log.e("AiSafetyService", "Backend call failed", e)
            }
        } else {
            Log.w("AiSafetyService", "No Google ID token available; using offline safety guidance")
        }

        val responseText = generateRuleBasedSafetyResponse(userQuery, isUrgent, lang)
        ChatMessage(
            sender = MessageSender.ASSISTANT,
            text = responseText,
            isUrgentEmergency = isUrgent,
            recommendedHelpline = helpline
        )
    }

    private fun callBackendChat(
        userQuery: String,
        history: List<ChatMessage>,
        idToken: String,
        lang: String
    ): String {
        val url = "$backendBaseUrl/api/v1/chat"

        val messagesArray = JSONArray().apply {
            history.takeLast(5).forEach { msg ->
                put(JSONObject().apply {
                    put("role", if (msg.sender == MessageSender.USER) "user" else "assistant")
                    put("content", msg.text)
                })
            }
            put(JSONObject().apply {
                put("role", "user")
                put("content", userQuery)
            })
        }

        val jsonBody = JSONObject().apply {
            put("messages", messagesArray)
            put("language", lang)
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $idToken")
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            val bodyString = response.body?.string() ?: ""
            if (!response.isSuccessful) {
                Log.e("AiSafetyService", "HTTP ${response.code}: $bodyString")
                return ""
            }
            return JSONObject(bodyString).optString("reply", "")
        }
    }

    private fun checkUrgentQuery(query: String): Boolean {
        val q = query.lowercase()
        return q.contains("follow") || q.contains("attack") || q.contains("emergency") ||
                q.contains("help me") || q.contains("in danger") || q.contains("stalking me right now") ||
                q.contains("trapped") || q.contains("save me") ||
                q.contains("पीछा") || q.contains("बचाओ") || q.contains("खतरा") ||
                q.contains("অনুসরণ") || q.contains("বাঁচাও") || q.contains("বিপদ")
    }

    private fun detectRecommendedHelpline(query: String): String? {
        val q = query.lowercase()
        return when {
            q.contains("cyber") || q.contains("fraud") || q.contains("hacked") || q.contains("account") -> "1930"
            q.contains("child") || q.contains("minor") || q.contains("pocso") || q.contains("kid") -> "1098"
            q.contains("workplace") || q.contains("office") || q.contains("boss") || q.contains("colleague") -> "181"
            q.contains("ambulance") || q.contains("doctor") || q.contains("hospital") || q.contains("bleeding") -> "108"
            else -> "112"
        }
    }

    private fun pick(lang: String, en: String, hi: String, bn: String): String {
        return when (lang) {
            "hi" -> hi
            "bn" -> bn
            else -> en
        }
    }

    private fun generateRuleBasedSafetyResponse(query: String, isUrgent: Boolean, lang: String): String {
        val q = query.lowercase()
        return when {
            isUrgent || q.contains("following") -> pick(
                lang,
                """
🚨 IMMEDIATE SAFETY GUIDANCE:
1. DIAL 112: If you believe you are in immediate danger, please tap the Emergency 112 button above or dial 112 immediately.
2. DO NOT CONFRONT: Do not stop or attempt to confront the suspected person.
3. MOVE TO PUBLIC LIGHT: Head toward a well-lit, crowded area — an open shop, pharmacy, metro station, or restaurant.
4. SHARE LIVE LOCATION: Use the Live Location button in Raksha Kavach to alert your primary trusted contact.
5. SEEK HELP: Approach a security guard, shopkeeper, or police personnel and state calmly that you are being followed.
                """.trimIndent(),
                """
🚨 तत्काल सुरक्षा सलाह:
1. 112 डायल करें: यदि आपको तुरंत खतरा लगे तो ऊपर दिया गया 112 बटन दबाएँ या 112 डायल करें।
2. सामना न करें: संदिग्ध व्यक्ति को रोकने या उससे भिड़ने की कोशिश न करें।
3. रोशनी और भीड़ की ओर जाएँ: खुली दुकान, फार्मेसी, मेट्रो स्टेशन या रेस्टोरेंट जैसी रोशन और भीड़ वाली जगह पर जाएँ।
4. लाइव लोकेशन भेजें: Raksha Kavach में Live Location बटन से अपने मुख्य विश्वसनीय संपर्क को सूचित करें।
5. मदद माँगें: सुरक्षा गार्ड, दुकानदार या पुलिसकर्मी के पास जाकर शांति से बताएँ कि कोई आपका पीछा कर रहा है।
                """.trimIndent(),
                """
🚨 তাৎক্ষণিক নিরাপত্তা নির্দেশনা:
1. ১১২ ডায়াল করুন: আপনি যদি তাৎক্ষণিক বিপদে থাকেন, উপরের ১১২ বোতাম চাপুন বা ১১২ ডায়াল করুন।
2. মুখোমুখি হবেন না: সন্দেহভাজন ব্যক্তিকে থামানো বা তার মুখোমুখি হওয়ার চেষ্টা করবেন না।
3. আলো ও ভিড়ের দিকে যান: খোলা দোকান, ফার্মেসি, মেট্রো স্টেশন বা রেস্তোরাঁর মতো আলোকিত ও ভিড়ের জায়গায় যান।
4. লাইভ লোকেশন পাঠান: Raksha Kavach-এর Live Location বোতাম ব্যবহার করে আপনার প্রধান বিশ্বস্ত যোগাযোগকে জানান।
5. সাহায্য চান: নিরাপত্তারক্ষী, দোকানদার বা পুলিশকর্মীর কাছে গিয়ে শান্তভাবে জানান যে কেউ আপনাকে অনুসরণ করছে।
                """.trimIndent()
            )

            q.contains("workplace") || q.contains("posh") || q.contains("boss") || q.contains("office") -> pick(
                lang,
                """
💼 WORKPLACE SEXUAL HARASSMENT (POSH ACT 2013):
1. INTERNAL COMMITTEE (IC): Any organisation with 10+ employees is legally mandated to have an IC headed by a senior woman.
2. SUBMITTING A COMPLAINT: You have the right to file a written complaint within 3 months of the incident.
3. LOCAL COMMITTEE (LC): If your workplace has fewer than 10 employees or the complaint is against the employer, file with the District Local Committee.
4. SHe-Box PORTAL: You can register a formal complaint on the Government of India portal: shebox.wcd.gov.in.
5. PRESERVE EVIDENCE: Keep all emails, SMS, WhatsApp chats, calendar invites, and notes of witnesses with dates and times.
                """.trimIndent(),
                """
💼 कार्यस्थल पर यौन उत्पीड़न (POSH अधिनियम 2013):
1. आंतरिक समिति (IC): 10 या अधिक कर्मचारियों वाले हर संगठन में एक वरिष्ठ महिला की अध्यक्षता वाली IC होना कानूनन अनिवार्य है।
2. शिकायत दर्ज करना: आपको घटना के 3 महीने के भीतर लिखित शिकायत दर्ज करने का अधिकार है।
3. स्थानीय समिति (LC): यदि आपके कार्यस्थल पर 10 से कम कर्मचारी हैं या शिकायत नियोक्ता के विरुद्ध है, तो जिला स्थानीय समिति में शिकायत करें।
4. SHe-Box पोर्टल: आप भारत सरकार के पोर्टल shebox.wcd.gov.in पर औपचारिक शिकायत दर्ज कर सकती हैं।
5. सबूत सुरक्षित रखें: सभी ईमेल, SMS, WhatsApp चैट, कैलेंडर इनवाइट और गवाहों के नाम तारीख और समय के साथ सहेजकर रखें।
                """.trimIndent(),
                """
💼 কর্মক্ষেত্রে যৌন হয়রানি (POSH আইন ২০১৩):
1. অভ্যন্তরীণ কমিটি (IC): ১০ বা তার বেশি কর্মীর যেকোনো প্রতিষ্ঠানে একজন সিনিয়র মহিলার নেতৃত্বে IC থাকা আইনত বাধ্যতামূলক।
2. অভিযোগ দায়ের: ঘটনার ৩ মাসের মধ্যে লিখিত অভিযোগ দায়ের করার অধিকার আপনার আছে।
3. স্থানীয় কমিটি (LC): আপনার কর্মক্ষেত্রে ১০ জনের কম কর্মী থাকলে বা অভিযোগ নিয়োগকর্তার বিরুদ্ধে হলে জেলা স্থানীয় কমিটিতে অভিযোগ করুন।
4. SHe-Box পোর্টাল: ভারত সরকারের পোর্টাল shebox.wcd.gov.in-এ আপনি আনুষ্ঠানিক অভিযোগ নথিভুক্ত করতে পারেন।
5. প্রমাণ সংরক্ষণ করুন: সব ইমেল, SMS, WhatsApp চ্যাট, ক্যালেন্ডার আমন্ত্রণ এবং সাক্ষীদের নাম তারিখ ও সময়সহ সংরক্ষণ করুন।
                """.trimIndent()
            )

            q.contains("cyber") || q.contains("stalk") || q.contains("threat") || q.contains("instagram") || q.contains("whatsapp") -> pick(
                lang,
                """
🛡️ CYBER HARASSMENT & ONLINE THREATS:
1. FINANCIAL FRAUD: If money was stolen or transferred fraudulently, call 1930 immediately to freeze transactions.
2. PRESERVE SCREENSHOTS: Capture screenshots of profiles, messages, URLs, phone numbers, and transaction IDs with exact timestamps.
3. DO NOT DELETE: Keep the chat history intact as legal evidence under the Information Technology Act.
4. REPORT OFFICIALLY: File a formal complaint at cybercrime.gov.in (National Cyber Crime Reporting Portal).
5. SECURE ACCOUNTS: Change passwords, enable Two-Factor Authentication (2FA), and block the offending accounts.
                """.trimIndent(),
                """
🛡️ साइबर उत्पीड़न और ऑनलाइन धमकियाँ:
1. वित्तीय धोखाधड़ी: यदि पैसे चोरी हुए हैं या धोखे से ट्रांसफर हुए हैं, तो लेन-देन रुकवाने के लिए तुरंत 1930 पर कॉल करें।
2. स्क्रीनशॉट सुरक्षित रखें: प्रोफ़ाइल, संदेश, URL, फ़ोन नंबर और ट्रांज़ैक्शन ID के सही समय के साथ स्क्रीनशॉट लें।
3. डिलीट न करें: सूचना प्रौद्योगिकी अधिनियम के तहत कानूनी सबूत के रूप में चैट हिस्ट्री को वैसे ही रखें।
4. आधिकारिक रिपोर्ट करें: cybercrime.gov.in (राष्ट्रीय साइबर अपराध रिपोर्टिंग पोर्टल) पर औपचारिक शिकायत दर्ज करें।
5. अकाउंट सुरक्षित करें: पासवर्ड बदलें, टू-फैक्टर ऑथेंटिकेशन (2FA) चालू करें और आपत्तिजनक अकाउंट को ब्लॉक करें।
                """.trimIndent(),
                """
🛡️ সাইবার হয়রানি ও অনলাইন হুমকি:
1. আর্থিক প্রতারণা: টাকা চুরি বা প্রতারণার মাধ্যমে স্থানান্তর হলে লেনদেন আটকাতে এখনই ১৯৩০ নম্বরে কল করুন।
2. স্ক্রিনশট সংরক্ষণ করুন: প্রোফাইল, বার্তা, URL, ফোন নম্বর ও লেনদেনের ID-র সঠিক সময়সহ স্ক্রিনশট নিন।
3. মুছবেন না: তথ্যপ্রযুক্তি আইনের অধীনে আইনি প্রমাণ হিসেবে চ্যাট ইতিহাস অক্ষত রাখুন।
4. সরকারিভাবে রিপোর্ট করুন: cybercrime.gov.in (জাতীয় সাইবার অপরাধ রিপোর্টিং পোর্টাল)-এ আনুষ্ঠানিক অভিযোগ করুন।
5. অ্যাকাউন্ট সুরক্ষিত করুন: পাসওয়ার্ড বদলান, টু-ফ্যাক্টর অথেন্টিকেশন (2FA) চালু করুন এবং আপত্তিকর অ্যাকাউন্ট ব্লক করুন।
                """.trimIndent()
            )

            q.contains("police station") || q.contains("nearest") -> pick(
                lang,
                """
📍 FINDING LOCAL POLICE SUPPORT:
1. Tap the "Find Nearby Police Stations" button in Raksha Kavach to open verified police stations on Google Maps.
2. You can also dial 112 or 1091 (Women Police Helpline) for immediate police patrol dispatch to your location.
3. Under Indian Law (Zero FIR), any police station must register a cognizable offense report regardless of jurisdiction.
                """.trimIndent(),
                """
📍 नज़दीकी पुलिस सहायता:
1. Raksha Kavach में "Find Nearby Police Stations" बटन दबाएँ, Google Maps पर सत्यापित पुलिस स्टेशन खुल जाएँगे।
2. आप तुरंत पुलिस गश्ती दल बुलाने के लिए 112 या 1091 (महिला पुलिस हेल्पलाइन) भी डायल कर सकती हैं।
3. भारतीय कानून (ज़ीरो FIR) के तहत कोई भी पुलिस स्टेशन क्षेत्राधिकार से परे संज्ञेय अपराध की रिपोर्ट दर्ज करने के लिए बाध्य है।
                """.trimIndent(),
                """
📍 স্থানীয় পুলিশ সহায়তা:
1. Raksha Kavach-এ "Find Nearby Police Stations" বোতাম চাপুন, Google Maps-এ যাচাইকৃত থানা খুলবে।
2. আপনার অবস্থানে দ্রুত পুলিশ টহল পাঠাতে ১১২ বা ১০৯১ (মহিলা পুলিশ হেল্পলাইন) ডায়াল করতে পারেন।
3. ভারতীয় আইন (জিরো FIR) অনুযায়ী এখতিয়ার নির্বিশেষে যেকোনো থানা আমলযোগ্য অপরাধের অভিযোগ নথিভুক্ত করতে বাধ্য।
                """.trimIndent()
            )

            q.contains("child") || q.contains("pocso") || q.contains("minor") -> pick(
                lang,
                """
👶 CHILD PROTECTION & POCSO ADVICE:
1. CALL 1098: Childline provides 24x7 emergency protection and counseling for minors under 18 years.
2. ZERO TOLERANCE: Any sexual offense against a child is governed by the stringent Protection of Children from Sexual Offences (POCSO) Act.
3. REPORTING OBLIGATION: Any adult aware of abuse can report without fear; the child's identity must remain confidential by law.
4. IN IMMEDIATE DANGER: Dial 112 for rapid police response.
                """.trimIndent(),
                """
👶 बाल सुरक्षा और POCSO सलाह:
1. 1098 पर कॉल करें: चाइल्डलाइन 18 वर्ष से कम उम्र के बच्चों को 24x7 आपातकालीन सुरक्षा और परामर्श देती है।
2. शून्य सहिष्णुता: बच्चे के विरुद्ध किसी भी यौन अपराध पर कठोर POCSO (यौन अपराधों से बच्चों का संरक्षण) अधिनियम लागू होता है।
3. रिपोर्ट करने का दायित्व: दुर्व्यवहार की जानकारी रखने वाला कोई भी वयस्क बिना डरे रिपोर्ट कर सकता है; कानूनन बच्चे की पहचान गोपनीय रहती है।
4. तत्काल खतरे में: त्वरित पुलिस प्रतिक्रिया के लिए 112 डायल करें।
                """.trimIndent(),
                """
👶 শিশু সুরক্ষা ও POCSO পরামর্শ:
1. ১০৯৮ নম্বরে কল করুন: চাইল্ডলাইন ১৮ বছরের কম বয়সী শিশুদের ২৪x৭ জরুরি সুরক্ষা ও কাউন্সেলিং দেয়।
2. শূন্য সহনশীলতা: শিশুর বিরুদ্ধে যেকোনো যৌন অপরাধ কঠোর POCSO (শিশুদের যৌন অপরাধ থেকে সুরক্ষা) আইনের আওতায় পড়ে।
3. রিপোর্ট করার দায়িত্ব: নির্যাতনের কথা জানা যেকোনো প্রাপ্তবয়স্ক নির্ভয়ে রিপোর্ট করতে পারেন; আইনত শিশুর পরিচয় গোপন থাকে।
4. তাৎক্ষণিক বিপদে: দ্রুত পুলিশি সাড়ার জন্য ১১২ ডায়াল করুন।
                """.trimIndent()
            )

            else -> pick(
                lang,
                """
🛡️ RAKSHAK SAFETY ADVICE:
- In any emergency, your immediate physical safety is paramount. Dial 112 for police, fire, or ambulance.
- Women Helpline 181 is available 24x7 for counseling, domestic distress, and legal guidance.
- Use Raksha Kavach's "Incident Report" section to privately document timestamps, details, and evidence for legal reference.
- Note: I am Rakshak, an AI safety assistant. For active danger, always connect with emergency services (112) or medical professionals immediately.
                """.trimIndent(),
                """
🛡️ रक्षक की सुरक्षा सलाह:
- किसी भी आपातकाल में आपकी तत्काल शारीरिक सुरक्षा सबसे ज़रूरी है। पुलिस, फायर या एम्बुलेंस के लिए 112 डायल करें।
- महिला हेल्पलाइन 181 परामर्श, घरेलू संकट और कानूनी मार्गदर्शन के लिए 24x7 उपलब्ध है।
- कानूनी संदर्भ के लिए समय, विवरण और सबूत निजी रूप से दर्ज करने हेतु Raksha Kavach का "Incident Report" सेक्शन इस्तेमाल करें।
- ध्यान दें: मैं रक्षक हूँ, एक AI सुरक्षा सहायक। वास्तविक खतरे में हमेशा तुरंत आपातकालीन सेवाओं (112) या चिकित्सा पेशेवरों से संपर्क करें।
                """.trimIndent(),
                """
🛡️ রক্ষকের নিরাপত্তা পরামর্শ:
- যেকোনো জরুরি অবস্থায় আপনার তাৎক্ষণিক শারীরিক নিরাপত্তাই সবচেয়ে গুরুত্বপূর্ণ। পুলিশ, দমকল বা অ্যাম্বুলেন্সের জন্য ১১২ ডায়াল করুন।
- মহিলা হেল্পলাইন ১৮১ কাউন্সেলিং, পারিবারিক সংকট ও আইনি নির্দেশনার জন্য ২৪x৭ উপলব্ধ।
- আইনি রেফারেন্সের জন্য সময়, বিবরণ ও প্রমাণ গোপনে নথিভুক্ত করতে Raksha Kavach-এর "Incident Report" অংশ ব্যবহার করুন।
- মনে রাখবেন: আমি রক্ষক, একটি AI নিরাপত্তা সহকারী। সক্রিয় বিপদে সবসময় সঙ্গে সঙ্গে জরুরি পরিষেবা (১১২) বা চিকিৎসকদের সাথে যোগাযোগ করুন।
                """.trimIndent()
            )
        }
    }
}