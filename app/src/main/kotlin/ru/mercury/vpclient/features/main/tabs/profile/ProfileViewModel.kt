package ru.mercury.vpclient.features.main.tabs.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.event.SnackbarBottomBarErrorEvent
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.features.main.tabs.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.main.tabs.profile.model.ProfileModel
import ru.mercury.vpclient.features.welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<ProfileIntent, ProfileModel, Event>(ProfileModel()) {

    init {
        dispatch(ProfileIntent.CollectClientEntity)
    }

    override fun dispatch(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.CollectClientEntity -> {
                launch {
                    interactor.clientEntityFlow.collectLatest { entity ->
                        reduce { it.copy(clientEntity = entity) }
                    }
                }
            }
            is ProfileIntent.ConfirmLogout -> {
                launch {
                    interactor.logout()
                    MainEventManager.send(WelcomeRoute)
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> launch { MainEventManager.send(SnackbarBottomBarErrorEvent(throwable.message.orEmpty())) }
            else -> super.catch(throwable)
        }
    }
}
