package ru.mercury.vpclient.core.persistence.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

sealed class PreferenceKey<T>(
    val preferenceKey: Preferences.Key<T>
) {
    data object ApplicationType: PreferenceKey<String>(stringPreferencesKey("applicationType"))
    data object DeviceId: PreferenceKey<String>(stringPreferencesKey("deviceId"))
    data object PairedUser: PreferenceKey<String>(stringPreferencesKey("pairedUser"))
    data object UserToken: PreferenceKey<String>(stringPreferencesKey("userToken"))
    data object RequestDelay: PreferenceKey<Float>(floatPreferencesKey("requestDelay"))
    data object EnvironmentProd: PreferenceKey<String>(stringPreferencesKey("environmentProd"))
    data object EnvironmentUat: PreferenceKey<String>(stringPreferencesKey("environmentUat"))
    data object EnvironmentDev: PreferenceKey<String>(stringPreferencesKey("environmentDev"))
    data object Autofill: PreferenceKey<Boolean>(booleanPreferencesKey("autofill"))
}
