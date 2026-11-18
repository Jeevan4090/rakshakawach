package com.example.data

import com.example.BuildConfig
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
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val systemPrompt = """
        You are "Raksha Assistant", an empathetic, calm, and knowledgeable women's safety AI guide within the Raksha Kavach Android application in India.
        
        Guidelines:
        1. Always maintain a calm, supportive, non-judgmental tone. Never question or blame the user.
        2. If the user indicates IMMEDIATE DANGER (e.g., being followed, actively attacked, trapped, or in immediate physical harm), instruct them FIRST to dial 112 (India's Emergency Response Support System), move toward a well-lit crowded public area, and do not confront the danger.
        3. Explain verified Indian legal and reporting channels:
           - 112: Unified Pan-India emergency (Police, Fire, Ambulance).
           - 181: Women Helpline for domestic distress and counseling.
           - 1091: Women Police Helpline.
           - 1930 & cybercrime.gov.in: Financial cyber fraud, non-consensual image sharing, cyberstalking.
           - SHe-Box (shebox.wcd.gov.in) & Internal Committee (IC) under POSH Act 2013 for workplace sexual harassment.
           - 1098: Childline / POCSO Act reporting for minors under 18.
        4. Explain practical evidence preservation (screenshots, exact timestamps, preserving chat logs, avoiding deletion, not confronting suspects).
        5. Clearly remind users that you are an AI assistant and not a replacement for emergency responders, police, legal advocates, or doctors.
        Keep responses concise, clear, and easy to read under stress.
    """.trimIndent()

    suspend fun getSafetyGuidance(userQuery: String): ChatMessage = withContext(Dispatchers.IO) {
        val isUrgent = checkUrgentQuery(userQuery)
        val helpline = detectRecommendedHelpline(userQuery)

        // Try Gemini API if key is available and configured
        val apiKey = BuildConfig.GEMINI_API_KEY
        val hasValidKey = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        if (hasValidKey) {
            try {
                val apiResponse = callGeminiRest(apiKey, userQuery)
                if (apiResponse.isNotBlank()) {
                    return@withContext ChatMessage(
                        sender = MessageSender.ASSISTANT,
                        text = apiResponse,
                        isUrgentEmergency = isUrgent,
                        recommendedHelpline = helpline
                    )
                }
            } catch (ignored: Exception) {
                // Fallback to safety rule engine
            }
        }

        // Offline / Rule-Engine Fallback: instant, robust, accurate safety advice
        val responseText = generateRuleBasedSafetyResponse(userQuery, isUrgent)
        ChatMessage(
            sender = MessageSender.ASSISTANT,
            text = responseText,
            isUrgentEmergency = isUrgent,
            recommendedHelpline = helpline
        )
    }

    private fun checkUrgentQuery(query: String): Boolean {
        val q = query.lowercase()
        return q.contains("follow") || q.contains("attack") || q.contains("emergency") ||
                q.contains("help me") || q.contains("in danger") || q.contains("stalking me right now") ||
                q.contains("trapped") || q.contains("save me")
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

    private fun callGeminiRest(apiKey: String, userQuery: String): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val jsonBody = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", userQuery))
                    })
                })
            })
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().put("text", systemPrompt))
                })
            })
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return ""
            val bodyString = response.body?.string() ?: return ""
            val json = JSONObject(bodyString)
            val candidates = json.optJSONArray("candidates") ?: return ""
            if (candidates.length() > 0) {
                val first = candidates.getJSONObject(0)
                val content = first.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return parts.getJSONObject(0).optString("text", "")
                }
            }
        }
        return ""
    }

    private fun generateRuleBasedSafetyResponse(query: String, isUrgent: Boolean): String {
        val q = query.lowercase()
        return when {
            isUrgent || q.contains("following") -> """
🚨 IMMEDIATE SAFETY GUIDANCE:
1. DIAL 112: If you believe you are in immediate danger, please tap the Emergency 112 button above or dial 112 immediately.
2. DO NOT CONFRONT: Do not stop or attempt to confront the suspected person.
3. MOVE TO PUBLIC LIGHT: Head toward a well-lit, crowded area — an open shop, pharmacy, metro station, or restaurant.
4. SHARE LIVE LOCATION: Use the Live Location button in Raksha Kavach to alert your primary trusted contact.
5. SEEK HELP: Approach a security guard, shopkeeper, or police personnel and state calmly that you are being followed.
            """.trimIndent()

            q.contains("workplace") || q.contains("posh") || q.contains("boss") || q.contains("office") -> """
💼 WORKPLACE SEXUAL HARASSMENT (POSH ACT 2013):
1. INTERNAL COMMITTEE (IC): Any organisation with 10+ employees is legally mandated to have an IC headed by a senior woman.
2. SUBMITTING A COMPLAINT: You have the right to file a written complaint within 3 months of the incident.
3. LOCAL COMMITTEE (LC): If your workplace has fewer than 10 employees or the complaint is against the employer, file with the District Local Committee.
4. SHe-Box PORTAL: You can register a formal complaint on the Government of India portal: shebox.wcd.gov.in.
5. PRESERVE EVIDENCE: Keep all emails, SMS, WhatsApp chats, calendar invites, and notes of witnesses with dates and times.
            """.trimIndent()

            q.contains("cyber") || q.contains("stalk") || q.contains("threat") || q.contains("instagram") || q.contains("whatsapp") -> """
🛡️ CYBER HARASSMENT & ONLINE THREATS:
1. FINANCIAL FRAUD: If money was stolen or transferred fraudulently, call 1930 immediately to freeze transactions.
2. PRESERVE SCREENSHOTS: Capture screenshots of profiles, messages, URLs, phone numbers, and transaction IDs with exact timestamps.
3. DO NOT DELETE: Keep the chat history intact as legal evidence under the Information Technology Act.
4. REPORT OFFICIALLY: File a formal complaint at cybercrime.gov.in (National Cyber Crime Reporting Portal).
5. SECURE ACCOUNTS: Change passwords, enable Two-Factor Authentication (2FA), and block the offending accounts.
            """.trimIndent()

            q.contains("police station") || q.contains("nearest") -> """
📍 FINDING LOCAL POLICE SUPPORT:
1. Tap the "Find Nearby Police Stations" button in Raksha Kavach to open verified police stations on Google Maps.
2. You can also dial 112 or 1091 (Women Police Helpline) for immediate police patrol dispatch to your location.
3. Under Indian Law (Zero FIR), any police station must register a cognizable offense report regardless of jurisdiction.
            """.trimIndent()

            q.contains("child") || q.contains("pocso") || q.contains("minor") -> """
👶 CHILD PROTECTION & POCSO ADVICE:
1. CALL 1098: Childline provides 24x7 emergency protection and counseling for minors under 18 years.
2. ZERO TOLERANCE: Any sexual offense against a child is governed by the stringent Protection of Children from Sexual Offences (POCSO) Act.
3. REPORTING OBLIGATION: Any adult aware of abuse can report without fear; the child's identity must remain confidential by law.
4. IN IMMEDIATE DANGER: Dial 112 for rapid police response.
            """.trimIndent()

            else -> """
🛡️ RAKSHA KAVACH SAFETY ADVICE:
• In any emergency, your immediate physical safety is paramount. Dial 112 for police, fire, or ambulance.
• Women Helpline 181 is available 24x7 for counseling, domestic distress, and legal guidance.
• Use Raksha Kavach's "Incident Report" section to privately document timestamps, details, and evidence for legal reference.
• Note: I am an AI safety assistant. For active danger, always connect with emergency services (112) or medical professionals immediately.
            """.trimIndent()
        }
    }
}
