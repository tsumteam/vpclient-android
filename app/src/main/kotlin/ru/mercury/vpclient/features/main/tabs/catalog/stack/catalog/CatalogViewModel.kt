package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.exception.ClientException
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.features.main.tabs.catalog.event.CatalogStackEventManager
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.event.CatalogEvent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.intent.CatalogIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.model.CatalogModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.subcategory.navigation.SubcategoryRoute
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<CatalogIntent, CatalogModel, CatalogEvent>(CatalogModel()) {

    init {
        dispatch(CatalogIntent.CollectCatalogScreenData)
        dispatch(CatalogIntent.LoadCatalogCategoriesTop)
    }

    override fun dispatch(intent: CatalogIntent) {
        when (intent) {
            is CatalogIntent.CollectCatalogScreenData -> {
                launch {
                    interactor.catalogScreenDataFlow.collectLatest { data ->
                        reduce { it.copy(catalogScreenData = data) }
                    }
                }
            }
            is CatalogIntent.LoadCatalogCategoriesTop -> launch { interactor.loadCatalogCategoriesTop() }
            is CatalogIntent.CategoryClick -> launch { CatalogStackEventManager.send(SubcategoryRoute(categoryId = intent.categoryId)) }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> launch { send(CatalogEvent.SnackbarMessage(throwable.message.orEmpty())) }
            is ClientException -> launch { send(CatalogEvent.SnackbarMessage(throwable.message)) }
            else -> super.catch(throwable)
        }
    }
}
