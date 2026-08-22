package ru.mercury.vpclient.features.profile_brand_sheet.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ProfileBrandEvent: Event {
    data object Dismiss: ProfileBrandEvent
    data class SnackbarErrorMessage(val message: String): ProfileBrandEvent
}
