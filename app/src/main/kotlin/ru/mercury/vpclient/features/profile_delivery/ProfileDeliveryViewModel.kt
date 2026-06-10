package ru.mercury.vpclient.features.profile_delivery

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_delivery.intent.ProfileDeliveryIntent
import ru.mercury.vpclient.features.profile_delivery.model.ProfileDeliveryModel
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileDeliveryViewModel @Inject constructor(): ClientViewModel<ProfileDeliveryIntent, ProfileDeliveryModel, Event>(
    ProfileDeliveryModel()
) {

    override fun dispatch(intent: ProfileDeliveryIntent) {
        when (intent) {
            is ProfileDeliveryIntent.BackClick -> launch { ProfileStackEventManager.send(BackRoute) }
        }
    }
}
