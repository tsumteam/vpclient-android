package ru.mercury.vpclient.features.profile_brands.event

import ru.mercury.vpclient.shared.mvi.Event

sealed interface ProfileBrandsEvent: Event {
    data class SnackbarErrorMessage(val message: String): ProfileBrandsEvent
}
