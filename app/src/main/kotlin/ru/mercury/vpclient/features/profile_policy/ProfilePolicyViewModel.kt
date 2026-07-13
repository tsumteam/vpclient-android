package ru.mercury.vpclient.features.profile_policy

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_policy.intent.ProfilePolicyIntent
import ru.mercury.vpclient.features.profile_policy.model.ProfilePolicyModel
import ru.mercury.vpclient.features.profile_root.event.ProfileRootEventManager
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfilePolicyViewModel @Inject constructor(): ClientViewModel<ProfilePolicyIntent, ProfilePolicyModel, Event>(ProfilePolicyModel()) {

    override fun dispatch(intent: ProfilePolicyIntent) {
        when (intent) {
            is ProfilePolicyIntent.BackClick -> launch { ProfileRootEventManager.send(BackRoute) }
        }
    }
}
