package ru.mercury.vpclient.core.persistence.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    fun <T> getValueFlow(key: PreferenceKey<T>): Flow<T?> {
        return dataStore.data.map { preferences -> preferences[key.preferenceKey] }
    }

    fun <T> getValueBlocking(key: PreferenceKey<T>): T? {
        return runBlocking { dataStore.data.first()[key.preferenceKey] }
    }

    suspend fun <T> getValue(key: PreferenceKey<T>): T? {
        return dataStore.data.first()[key.preferenceKey]
    }

    suspend fun <T> setValue(key: PreferenceKey<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key.preferenceKey] = value
        }
    }

    suspend fun <T> removeValue(key: PreferenceKey<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key.preferenceKey)
        }
    }

    suspend fun <T> removeValues(vararg keys: PreferenceKey<T>) {
        dataStore.edit { preferences ->
            keys.forEach { key -> preferences.remove(key.preferenceKey) }
        }
    }
}
