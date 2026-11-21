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

    private const val PREFS_NAME = "raksha_auth_prefs"
    private const val KEY_TOKEN = "id_token"
    private const val KEY_EMAIL = "email"
    private const val KEY_NAME = "display_name"

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
                val authResult = GoogleAuthResult(
                    idToken = googleCred.idToken,
                    email = googleCred.id,
                    displayName = googleCred.displayName
                )
                saveUser(activityContext, authResult)
                authResult
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("GoogleAuthHelper", "Google Sign-In failed", e)
            null
        }
    }

    fun getSavedUser(context: Context): GoogleAuthResult? {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val token = prefs.getString(KEY_TOKEN, null) ?: return null
        val email = prefs.getString(KEY_EMAIL, null) ?: return null
        val name = prefs.getString(KEY_NAME, null)
        return GoogleAuthResult(idToken = token, email = email, displayName = name)
    }

    fun signOut(context: Context) {
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    private fun saveUser(context: Context, user: GoogleAuthResult) {
        context.applicationContext
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TOKEN, user.idToken)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_NAME, user.displayName)
            .apply()
    }
}