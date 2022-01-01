package com.asainternational.ambsmobile.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.asainternational.ambsmobile.prefs.DataStorePreferenceStorage.PreferencesKeys.PREF_ACCESS_TOKEN
import com.asainternational.ambsmobile.prefs.DataStorePreferenceStorage.PreferencesKeys.PREF_OFFLINE_PASSWORD
import com.asainternational.ambsmobile.prefs.DataStorePreferenceStorage.PreferencesKeys.PREF_REFRESH_TOKEN
import com.asainternational.ambsmobile.prefs.DataStorePreferenceStorage.PreferencesKeys.PREF_USER_ID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Storage for app and user preferences.
 */
interface PreferenceStorage {
    suspend fun accessToken(token: String)
    val accessToken: String

    suspend fun refreshToken(token: String)
    val refreshToken: String

    suspend fun userId(id: String)
    val userId: String

    suspend fun offlinePassword(id: String)
    val offlinePassword: String

}

@Singleton
class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferenceStorage {
    companion object {
        const val PREFS_NAME = "ambs"
    }

    object PreferencesKeys {
        val PREF_ACCESS_TOKEN = stringPreferencesKey("pref_access_token")
        val PREF_REFRESH_TOKEN = stringPreferencesKey("pref_-refresh_access_token")
        val PREF_USER_ID = stringPreferencesKey("pref_user_id")
        val PREF_OFFLINE_PASSWORD = stringPreferencesKey("pref_offline_password")
    }

    override suspend fun accessToken(token: String) {
        dataStore.edit {
            it[PREF_ACCESS_TOKEN] = token
        }
    }

    override val accessToken: String
        get() =
            runBlocking {
                dataStore.data.map { it[PREF_ACCESS_TOKEN] ?: "" }.first()
            }

    override suspend fun refreshToken(token: String) {
        dataStore.edit {
            it[PREF_REFRESH_TOKEN] = token
        }
    }

    override val refreshToken : String
        get() =
        runBlocking { dataStore.data.map { it[PREF_REFRESH_TOKEN] ?: "" }.first() }

    override suspend fun userId(id: String) {
        dataStore.edit {
            it[PREF_USER_ID] = id
        }
    }

    override val userId: String
        get() =
            runBlocking {
                dataStore.data.map { it[PREF_USER_ID] ?: "" }.first()
            }


    override suspend fun offlinePassword(id: String) {
        dataStore.edit {
            it[PREF_OFFLINE_PASSWORD] = id
        }
    }

    override val offlinePassword: String
        get() =
            runBlocking {
                dataStore.data.map { it[PREF_OFFLINE_PASSWORD] ?: "" }.first()
            }

}
