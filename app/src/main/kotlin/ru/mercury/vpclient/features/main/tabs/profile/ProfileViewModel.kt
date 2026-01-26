package ru.mercury.vpclient.features.main.tabs.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.event.SnackbarBottomBarErrorEvent
import ru.mercury.vpclient.core.event.SnackbarBottomBarEvent
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.VPClientViewModel
import ru.mercury.vpclient.core.ui.theme.VPClientStrings
import ru.mercury.vpclient.features.authentication.navigation.AuthenticationRoute
import ru.mercury.vpclient.features.main.tabs.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.main.tabs.profile.model.ProfileModel
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: Interactor
): VPClientViewModel<ProfileIntent, ProfileModel>(ProfileModel()) {

    init {
        dispatch(ProfileIntent.CollectDriverEntity)
    }

    override fun dispatch(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.CollectDriverEntity -> {}
            is ProfileIntent.ClearDeliveryCacheClick -> {
                launch {
                    MainEventManager.send(SnackbarBottomBarEvent(VPClientStrings.AppName))
                }
            }
            is ProfileIntent.ConfirmLogout -> {
                launch {
                    interactor.logout()
                    MainEventManager.send(AuthenticationRoute)
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
