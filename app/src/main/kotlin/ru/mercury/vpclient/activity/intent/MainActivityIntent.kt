package ru.mercury.vpclient.activity.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface MainActivityIntent: Intent {
    data object ResolveNavigation: MainActivityIntent
    data class CenterLoading(val enabled: Boolean): MainActivityIntent
}
