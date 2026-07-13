package ru.mercury.vpclient.features.profile_payment

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_payment.intent.ProfilePaymentIntent
import ru.mercury.vpclient.features.profile_payment.model.ProfilePaymentModel
import ru.mercury.vpclient.features.profile_root.event.ProfileRootEventManager
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfilePaymentViewModel @Inject constructor(): ClientViewModel<ProfilePaymentIntent, ProfilePaymentModel, Event>(ProfilePaymentModel()) {

    override fun dispatch(intent: ProfilePaymentIntent) {
        when (intent) {
            is ProfilePaymentIntent.BackClick -> launch { ProfileRootEventManager.send(BackRoute) }
        }
    }
}
