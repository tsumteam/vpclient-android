package ru.mercury.vpclient.features.banner.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface BannerEvent: Event {
    data class SnackbarErrorMessage(val message: String): BannerEvent
}
