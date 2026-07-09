package ru.mercury.vpclient.features.profile_brand_sheet.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ProfileBrandSheetEvent: Event {
    data object Dismiss: ProfileBrandSheetEvent
    data class SnackbarErrorMessage(val message: String): ProfileBrandSheetEvent
}
