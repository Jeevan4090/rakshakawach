package com.example.data.entity

data class HelplineItem(
    val id: String,
    val name: String,
    val number: String,
    val category: String, // "Emergency", "Women Safety", "Medical", "Cybercrime", "Child Protection", "Regional (WB/Kolkata)"
    val description: String,
    val availability: String, // e.g., "24x7 Nationwide", "24x7 State-wide"
    val alternativeNumber: String? = null,
    val verifiedNote: String? = null
)

object VerifiedHelplines {
    val list: List<HelplineItem> = listOf(
        HelplineItem(
            id = "112",
            name = "National Emergency Response (ERSS)",
            number = "112",
            category = "Emergency",
            description = "Single unified emergency number across India for immediate Police, Fire, and Medical assistance.",
            availability = "24x7 Nationwide",
            alternativeNumber = "100",
            verifiedNote = "Official Ministry of Home Affairs ERSS system."
        ),
        HelplineItem(
            id = "181",
            name = "Women Helpline (WHL)",
            number = "181",
            category = "Women Safety",
            description = "Toll-free 24-hour helpline providing support to women affected by violence, harassment, or in distress.",
            availability = "24x7 (Subject to state rollout)",
            alternativeNumber = "112",
            verifiedNote = "Ministry of Women & Child Development scheme."
        ),
        HelplineItem(
            id = "1091",
            name = "Women Police Helpline",
            number = "1091",
            category = "Women Safety",
            description = "Dedicated women police helpline for reporting stalking, molestation, distress, or physical danger.",
            availability = "24x7 (Active in major cities/states)",
            alternativeNumber = "112",
            verifiedNote = "State police women protection units."
        ),
        HelplineItem(
            id = "1930",
            name = "Cyber Financial Fraud Helpline",
            number = "1930",
            category = "Cybercrime",
            description = "Immediate reporting of financial cyber fraud, online scams, unauthorized UPI/banking debits, and identity theft.",
            availability = "24x7 Operational",
            alternativeNumber = null,
            verifiedNote = "Operated by Indian Cyber Crime Coordination Centre (I4C)."
        ),
        HelplineItem(
            id = "1098",
            name = "Childline / POCSO Child Helpline",
            number = "1098",
            category = "Child Protection",
            description = "Emergency phone outreach service for children in need of care and protection, POCSO incidents, and abuse reporting.",
            availability = "24x7 Nationwide",
            alternativeNumber = "112",
            verifiedNote = "National Child Protection System under MWCD."
        ),
        HelplineItem(
            id = "108",
            name = "Emergency Medical Ambulance Service",
            number = "108",
            category = "Medical",
            description = "Free emergency ambulance dispatch service for accidents, urgent medical conditions, and critical care transport.",
            availability = "24x7 in operational states",
            alternativeNumber = "112",
            verifiedNote = "National Health Mission emergency medical service."
        ),
        HelplineItem(
            id = "102",
            name = "Maternal & Child Ambulance Transport",
            number = "102",
            category = "Medical",
            description = "Free transport service focused on pregnant mothers, infants, and non-critical medical transport.",
            availability = "24x7 State-wide",
            alternativeNumber = "108",
            verifiedNote = "Janani Shishu Suraksha Karyakram transport network."
        ),
        HelplineItem(
            id = "14490",
            name = "National Commission for Women (NCW)",
            number = "14490",
            category = "Women Safety",
            description = "Dedicated NCW helpline providing round-the-clock complaints registration, legal aid, and counseling for women.",
            availability = "24x7 Verified",
            alternativeNumber = "011-26942369",
            verifiedNote = "National Commission for Women official 24x7 helpline."
        ),
        HelplineItem(
            id = "100",
            name = "Police Control Room (Traditional)",
            number = "100",
            category = "Emergency",
            description = "Traditional police emergency dial; in many states routed seamlessly into the 112 ERSS infrastructure.",
            availability = "24x7 Nationwide",
            alternativeNumber = "112",
            verifiedNote = "Direct police dial."
        ),
        HelplineItem(
            id = "1090_wb",
            name = "Kolkata Police Women Helpline",
            number = "1090",
            category = "Regional (WB/Kolkata)",
            description = "Dedicated women distress helpline for Kolkata city and metropolitan police jurisdiction.",
            availability = "24x7 City-wide",
            alternativeNumber = "112",
            verifiedNote = "Kolkata Police Headquarters Lalbazar."
        ),
        HelplineItem(
            id = "lalbazar_wb",
            name = "Kolkata Police Central Control Room",
            number = "03322145000",
            category = "Regional (WB/Kolkata)",
            description = "Kolkata Police HQ Lalbazar Central Emergency Control Desk for urgent intervention and verification.",
            availability = "24x7 Landline Desk",
            alternativeNumber = "100",
            verifiedNote = "Verified Kolkata Police Official Desk."
        )
    )
}
