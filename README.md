# Rakshakavach

An Android personal safety application built with Kotlin and Jetpack Compose, designed for rapid emergency response, discreet evidence collection, and verified legal/helpline guidance across India.

## Features

- **Emergency SOS:** One-tap SOS with a cancellable countdown or immediate trigger, direct 112 dialing, and location-tagged alerts to trusted contacts.
- **3-Tap Home Screen SOS Widget:** A 1×1 home screen widget (`SosWidgetProvider`) that detects 3 rapid taps within 3 seconds, provides haptic feedback, and automatically triggers the emergency alert flow. Can be pinned directly from the Home screen.
- **Live Location & Trusted Contacts:** Manage emergency contacts and share real-time GPS location.
- **Evidence Recording & Incident Vault:** Record audio/video evidence and store incident notes locally on the device.
- **AI Safety Guide:** Context-aware safety and legal guidance powered by `@google/genai` via a Render backend, secured with Google Sign-In (Credential Manager).
- **Situation Guides & Verified Helplines:** Dedicated workflows for urgent help, being followed, ambulance (112/108), harassment, assault, workplace harassment (POSH Act), cybercrime (1930), child safety (1098 / POCSO), and national helplines (112, 181, 1091).
- **Beta Padhao • Beti Bachao:** Quick access to women's welfare schemes (One Stop Centre / Sakhi, 181, BBBP) and an awareness pledge.

## Tech Stack

- **Android:** Kotlin, Jetpack Compose, Material 3, AndroidX Credential Manager, AppWidgetProvider
- **Backend:** Node.js, Express, `@google/genai`, Google Auth Library (hosted on Render)

## Prerequisites

- Android Studio (or JDK 17+ and Android SDK CLI tools)
- A Google Cloud project with OAuth 2.0 credentials configured:
  - **Android Client ID** linked to your `applicationId` and debug/release SHA-1 fingerprint (stays in Google Cloud Console only).
  - **Web Application Client ID** used for both Android Credential Manager and backend token verification.

## Configuration

1. **Android (`GoogleAuthHelper.kt`):**
   Set `WEB_CLIENT_ID` to your Google Cloud **Web application** Client ID.
2. **Backend (Render Environment Variables):**
   - `GOOGLE_CLIENT_ID` — The same **Web application** Client ID.
   - `GEMINI_API_KEY` — Your Google AI Studio API key.

## Building & Running

Build a debug APK:
```bash
./gradlew assembleDebug
