package ru.mercury.vpclient.features.profile_gift

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_gift.intent.ProfileGiftIntent
import ru.mercury.vpclient.features.profile_gift.model.ProfileGiftModel
import ru.mercury.vpclient.features.profile_root.event.ProfileRootEventManager
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileGiftViewModel @Inject constructor(): ClientViewModel<ProfileGiftIntent, ProfileGiftModel, Event>(ProfileGiftModel()) {

    override fun dispatch(intent: ProfileGiftIntent) {
        when (intent) {
            is ProfileGiftIntent.BackClick -> launch { ProfileRootEventManager.send(BackRoute) }
        }
    }
}
