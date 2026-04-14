package ru.mercury.vpclient.shared.persistence.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

sealed class PreferenceKey<T>(
    val preferenceKey: Preferences.Key<T>
) {
    data object ApplicationType: PreferenceKey<String>(stringPreferencesKey("applicationType"))
    data object DeviceId: PreferenceKey<String>(stringPreferencesKey("deviceId"))
    data object UserId: PreferenceKey<String>(stringPreferencesKey("userId"))
    data object PairedUser: PreferenceKey<String>(stringPreferencesKey("pairedUser"))
    data object UserToken: PreferenceKey<String>(stringPreferencesKey("userToken"))
    data object RequestDelay: PreferenceKey<Long>(longPreferencesKey("requestDelayMIlls"))
    data object EnvironmentProd: PreferenceKey<String>(stringPreferencesKey("environmentProd"))
    data object EnvironmentUat: PreferenceKey<String>(stringPreferencesKey("environmentUat"))
    data object EnvironmentDev: PreferenceKey<String>(stringPreferencesKey("environmentDev"))
    data object Autofill: PreferenceKey<Boolean>(booleanPreferencesKey("autofill"))
}
