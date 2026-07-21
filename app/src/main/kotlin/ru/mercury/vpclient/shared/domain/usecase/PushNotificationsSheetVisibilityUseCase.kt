package ru.mercury.vpclient.shared.domain.usecase

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.ui.ktx.isPostNotificationsPermissionGranted

class PushNotificationsSheetVisibilityUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Boolean>(dispatchers.io) {

    override suspend fun execute(params: Unit): Boolean {
        if (context.isPostNotificationsPermissionGranted) return false

        val lastPromptTimestamp = settingsDataStore.getValue(PreferenceKey.PushNotificationsPromptTimestamp)
            ?: return true
        val wasRejected = settingsDataStore.getValue(PreferenceKey.PushNotificationsPromptRejected) == true
        val timeoutMillis = when {
            wasRejected -> REJECTED_PROMPT_TIMEOUT_DAYS * MILLIS_IN_DAY
            else -> PROMPT_TIMEOUT_DAYS * MILLIS_IN_DAY
        }
        return System.currentTimeMillis() - lastPromptTimestamp >= timeoutMillis
    }

    private companion object {
        private const val PROMPT_TIMEOUT_DAYS = 30L
        private const val REJECTED_PROMPT_TIMEOUT_DAYS = 60L
        private const val MILLIS_IN_DAY = 24L * 60L * 60L * 1000L
    }
}
