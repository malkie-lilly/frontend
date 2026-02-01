package com.example.lilian.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

data class UserSession(
    val userId: Int,
    val username: String,
    val email: String,
    val role: String,
    val sessionId: String,
    val isLoggedIn: Boolean
)

class SessionManager(private val context: Context) {
    
    companion object {
        private val USER_ID = intPreferencesKey("user_id")
        private val USERNAME = stringPreferencesKey("username")
        private val EMAIL = stringPreferencesKey("email")
        private val ROLE = stringPreferencesKey("role")
        private val SESSION_ID = stringPreferencesKey("session_id")
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveUserSession(
        userId: Int,
        username: String,
        email: String,
        role: String,
        sessionId: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
            preferences[USERNAME] = username
            preferences[EMAIL] = email
            preferences[ROLE] = role
            preferences[SESSION_ID] = sessionId
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    val userSession: Flow<UserSession?> = context.dataStore.data.map { preferences ->
        if (preferences[IS_LOGGED_IN] == true) {
            UserSession(
                userId = preferences[USER_ID] ?: 0,
                username = preferences[USERNAME] ?: "",
                email = preferences[EMAIL] ?: "",
                role = preferences[ROLE] ?: "",
                sessionId = preferences[SESSION_ID] ?: "",
                isLoggedIn = true
            )
        } else {
            null
        }
    }
}
