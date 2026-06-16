package ru.mercury.vpclient.features.profile_loyalty_qr

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.profile_loyalty_qr.intent.ProfileLoyaltyQrIntent
import ru.mercury.vpclient.features.profile_loyalty_qr.model.ProfileLoyaltyQrModel
import ru.mercury.vpclient.features.profile_loyalty_qr.navigation.ProfileLoyaltyQrRoute
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = ProfileLoyaltyQrViewModel.Factory::class)
class ProfileLoyaltyQrViewModel @AssistedInject constructor(
    @Assisted private val route: ProfileLoyaltyQrRoute
): ClientViewModel<ProfileLoyaltyQrIntent, ProfileLoyaltyQrModel, Event>(ProfileLoyaltyQrModel()) {

    init {
        dispatch(ProfileLoyaltyQrIntent.LoadQrCode)
    }

    override fun dispatch(intent: ProfileLoyaltyQrIntent) {
        when (intent) {
            is ProfileLoyaltyQrIntent.LoadQrCode -> reduce { it.copy(qrCode = route.qrCode) }
            is ProfileLoyaltyQrIntent.CloseClick -> launch { MainEventManager.send(BackRoute) }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: ProfileLoyaltyQrRoute): ProfileLoyaltyQrViewModel
    }
}
