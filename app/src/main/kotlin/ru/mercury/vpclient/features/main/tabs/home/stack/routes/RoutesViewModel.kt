package ru.mercury.vpclient.features.main.tabs.home.stack.routes

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.event.ClipboardEvent
import ru.mercury.vpclient.core.event.SnackbarBottomBarErrorEvent
import ru.mercury.vpclient.core.exception.VPClientException
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.VPClientViewModel
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.intent.RoutesIntent
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.model.RoutesModel
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class RoutesViewModel @Inject constructor(
    private val interactor: Interactor
): VPClientViewModel<RoutesIntent, RoutesModel>(RoutesModel()) {

    init {
        dispatch(RoutesIntent.CollectRouteData)
        dispatch(RoutesIntent.RequestRoutes)
    }

    override fun dispatch(intent: RoutesIntent) {
        when (intent) {
            is RoutesIntent.CollectRouteData -> {}
            is RoutesIntent.RequestRoutes -> {}
            is RoutesIntent.BoutiqueClick -> {}
            is RoutesIntent.DeliveryClick -> {}
            is RoutesIntent.ReturnClick -> Unit
            is RoutesIntent.CopyDeliveryClick -> { launch { MainEventManager.send(ClipboardEvent(intent.deliveryId)) } }
            is RoutesIntent.CallTel -> launch {}
            is RoutesIntent.CallWhatsApp -> launch {}
            is RoutesIntent.WriteWhatsApp -> launch {}
            is RoutesIntent.PullToRefresh -> {
                reduce { it.copy(isRefreshing = true) }
                dispatch(RoutesIntent.RequestRoutes)
            }
            is RoutesIntent.LoadDetailsProducts -> {}
            is RoutesIntent.CloseTripButtonClick -> {}
            is RoutesIntent.SectionBoutiquesClick -> {}
            is RoutesIntent.SectionDeliveriesClick -> {}
            is RoutesIntent.SectionReturnsClick -> {}
            is RoutesIntent.SetPhone -> { reduce { it.copy(selectedPhone = intent.phone) } }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is VPClientException -> launch { MainEventManager.send(SnackbarBottomBarErrorEvent(throwable.message)) }
            is RoomException, is RoomSQLiteException -> launch { MainEventManager.send(SnackbarBottomBarErrorEvent(throwable.message.orEmpty())) }
            else -> super.catch(throwable)
        }
    }
}
