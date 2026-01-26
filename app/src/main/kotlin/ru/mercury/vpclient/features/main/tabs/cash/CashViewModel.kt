package ru.mercury.vpclient.features.main.tabs.cash

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.event.SnackbarBottomBarErrorEvent
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.VPClientViewModel
import ru.mercury.vpclient.features.main.event.MainTabsEventManager
import ru.mercury.vpclient.features.main.tabs.cash.intent.CashIntent
import ru.mercury.vpclient.features.main.tabs.cash.model.CashModel
import ru.mercury.vpclient.features.main.tabs.home.navigation.HomeRoute
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class CashViewModel @Inject constructor(
    private val interactor: Interactor
): VPClientViewModel<CashIntent, CashModel>(CashModel()) {

    override fun dispatch(intent: CashIntent) {
        when (intent) {
            is CashIntent.CollectDriverEntity -> {}
            is CashIntent.ReturnHomeClick -> launch { MainTabsEventManager.send(HomeRoute) }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> launch { MainEventManager.send(SnackbarBottomBarErrorEvent(throwable.message.orEmpty())) }
            else -> super.catch(throwable)
        }
    }
}
