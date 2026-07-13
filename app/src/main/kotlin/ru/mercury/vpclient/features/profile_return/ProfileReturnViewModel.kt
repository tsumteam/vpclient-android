package ru.mercury.vpclient.features.profile_return

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_return.intent.ProfileReturnIntent
import ru.mercury.vpclient.features.profile_return.model.ProfileReturnModel
import ru.mercury.vpclient.features.profile_root.event.ProfileRootEventManager
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileReturnViewModel @Inject constructor(): ClientViewModel<ProfileReturnIntent, ProfileReturnModel, Event>(ProfileReturnModel()) {

    override fun dispatch(intent: ProfileReturnIntent) {
        when (intent) {
            is ProfileReturnIntent.BackClick -> launch { ProfileRootEventManager.send(BackRoute) }
        }
    }
}
