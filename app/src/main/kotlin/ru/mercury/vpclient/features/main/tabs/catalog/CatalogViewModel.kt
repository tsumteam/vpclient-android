package ru.mercury.vpclient.features.main.tabs.catalog

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.event.SnackbarBottomBarErrorEvent
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.mvi.Event
import ru.mercury.vpclient.features.main.tabs.catalog.intent.CatalogIntent
import ru.mercury.vpclient.features.main.tabs.catalog.model.CatalogModel
import ru.mercury.vpclient.main.event.MainEventManager
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<CatalogIntent, CatalogModel, Event>(CatalogModel()) {

    init {
        dispatch(CatalogIntent.CollectClientEntity)
    }

    override fun dispatch(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.CollectClientEntity -> {
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
