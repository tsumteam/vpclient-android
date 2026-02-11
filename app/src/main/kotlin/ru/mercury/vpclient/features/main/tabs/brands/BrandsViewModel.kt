package ru.mercury.vpclient.features.main.tabs.brands

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.event.SnackbarBottomBarErrorEvent
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.features.main.tabs.brands.intent.BrandsIntent
import ru.mercury.vpclient.features.main.tabs.brands.model.BrandsModel
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class BrandsViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<BrandsIntent, BrandsModel, Event>(BrandsModel()) {

    init {
        dispatch(BrandsIntent.CollectClientEntity)
    }

    override fun dispatch(intent: BrandsIntent) {
        when (intent) {
            is BrandsIntent.CollectClientEntity -> {
                launch {
                    interactor.clientEntityFlow.collectLatest { entity ->
                        reduce { it.copy(clientEntity = entity) }
                    }
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
