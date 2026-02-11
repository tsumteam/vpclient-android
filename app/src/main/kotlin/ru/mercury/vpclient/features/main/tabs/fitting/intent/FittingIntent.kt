package ru.mercury.vpclient.features.main.tabs.fitting.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface FittingIntent: Intent {
    data object CollectClientEntity: FittingIntent
}
