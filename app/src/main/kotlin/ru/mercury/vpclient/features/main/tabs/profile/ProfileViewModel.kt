package ru.mercury.vpclient.features.main.tabs.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.features.main.tabs.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.main.tabs.profile.model.ProfileModel
import ru.mercury.vpclient.features.welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.activity.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<ProfileIntent, ProfileModel, Event>(ProfileModel()) {

    override fun dispatch(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.Logout -> {
                val job = launch {
                    interactor.logout()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(logoutJob = null) } }
                }
                reduce { it.copy(logoutJob = job) }
            }
        }
    }
}
