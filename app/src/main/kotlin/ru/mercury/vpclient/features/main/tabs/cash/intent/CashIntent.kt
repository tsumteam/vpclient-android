package ru.mercury.vpclient.features.main.tabs.cash.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface CashIntent: Intent {
    data object CollectDriverEntity: CashIntent
    data object ReturnHomeClick: CashIntent
}
