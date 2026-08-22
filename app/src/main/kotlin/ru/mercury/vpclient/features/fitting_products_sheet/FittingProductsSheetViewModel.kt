package ru.mercury.vpclient.features.fitting_products_sheet

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.fitting_products_sheet.event.FittingProductsEvent
import ru.mercury.vpclient.features.fitting_products_sheet.event.FittingProductsEventManager
import ru.mercury.vpclient.features.fitting_products_sheet.intent.FittingProductsIntent
import ru.mercury.vpclient.features.fitting_products_sheet.model.FittingProductsModel
import ru.mercury.vpclient.shared.domain.usecase.FittingProductsFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class FittingProductsSheetViewModel @Inject constructor(
    private val fittingProductsFlowUseCase: FittingProductsFlowUseCase
): ClientViewModel<FittingProductsIntent, FittingProductsModel, FittingProductsEvent>(FittingProductsModel()) {

    init {
        dispatch(FittingProductsIntent.CollectProducts)
    }

    override fun dispatch(intent: FittingProductsIntent) {
        when (intent) {
            is FittingProductsIntent.CollectProducts -> {
                launch {
                    fittingProductsFlowUseCase(Unit).collectLatest { entities ->
                        reduce { it.copy(cartProductEntities = entities) }
                    }
                }
            }
            is FittingProductsIntent.ConfirmClick -> {
                launch {
                    val productIds = stateFlow.value.selectedProductIds.toList()
                    FittingProductsEventManager.send(FittingProductsEvent.ConfirmClick(productIds))
                }
            }
            is FittingProductsIntent.DismissClick -> {
                launch { FittingProductsEventManager.send(FittingProductsEvent.DismissRequest) }
            }
            is FittingProductsIntent.ProductCheckedChange -> {
                reduce {
                    it.copy(
                        selectedProductIds = when {
                            intent.checked -> it.selectedProductIds + intent.productId
                            else -> it.selectedProductIds - intent.productId
                        }
                    )
                }
            }
        }
    }
}
