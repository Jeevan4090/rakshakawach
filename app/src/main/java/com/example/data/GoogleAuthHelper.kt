package com.example.data

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

data class GoogleAuthResult(
    val idToken: String,
    val email: String,
    val displayName: String?
)

object GoogleAuthHelper {
    // Must be the WEB Client ID (same as GOOGLE_CLIENT_ID on Render)
    private const val WEB_CLIENT_ID = "608539782535-lrsfm91k8fvaev1rjih4ak6vkbngeqnh.apps.googleusercontent.com"

    suspend fun signIn(activityContext: Context): GoogleAuthResult? {
        return try {
            val credentialManager = CredentialManager.create(activityContext)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(WEB_CLIENT_ID)
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = activityContext
            )

            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCred = GoogleIdTokenCredential.createFrom(credential.data)
                GoogleAuthResult(
                    idToken = googleCred.idToken,
                    email = googleCred.id,
                    displayName = googleCred.displayName
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("GoogleAuthHelper", "Google Sign-In failed", e)
            null
        }
    }
}