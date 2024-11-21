package com.example.coursefinderapp.data.remote.prefs

import android.content.Context
import com.example.coursefinderapp.domain.entity.UserProfile
import javax.inject.Inject

class PrefsDataSourceImpl @Inject constructor(private val context: Context) : PrefsDataSource {

    override fun saveToken(token: String) {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        with(prefs.edit()) {
            putString(tokenKey, token)
            apply()
        }
    }

    override fun fetchToken(): String? {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        return prefs.getString(tokenKey, "")
    }

    override fun saveStepikToken(stepikToken: String) {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        with(prefs.edit()) {
            putString(stepikTokenKey, stepikToken)
            apply()
        }
    }

    override fun fetchStepikToken(): String? {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        return prefs.getString(stepikTokenKey, "")
    }

    override fun saveUserProfile(userProfile: UserProfile) {
        val prefs = context.getSharedPreferences(
            sessionPrefs, Context.MODE_PRIVATE
        )

        with(prefs.edit()) {
            putString(usernameKey, userProfile.username)
            putString(userIdKey, userProfile.userId)
            apply()
        }
    }

    companion object {
        const val stepikTokenKey = "stepik_token_key"
        const val sessionPrefs = "session_prefs"
        const val tokenKey = "token_key"
        const val userIdKey = "current_user_id"
        const val usernameKey = "current_username"
    }
}

interface PrefsDataSource {
    fun saveToken(token: String)
    fun fetchToken(): String?
    fun saveStepikToken(stepikToken: String)
    fun fetchStepikToken(): String?
    fun saveUserProfile(userProfile: UserProfile)
}

