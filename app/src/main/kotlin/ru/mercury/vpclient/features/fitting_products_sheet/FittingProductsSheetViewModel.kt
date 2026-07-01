package ru.mercury.vpclient.features.fitting_products_sheet

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.fitting_products_sheet.event.FittingProductsSheetEvent
import ru.mercury.vpclient.features.fitting_products_sheet.event.FittingProductsSheetEventManager
import ru.mercury.vpclient.features.fitting_products_sheet.intent.FittingProductsSheetIntent
import ru.mercury.vpclient.features.fitting_products_sheet.model.FittingProductsSheetModel
import ru.mercury.vpclient.shared.domain.usecase.FittingProductsFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class FittingProductsSheetViewModel @Inject constructor(
    private val fittingProductsFlowUseCase: FittingProductsFlowUseCase
): ClientViewModel<FittingProductsSheetIntent, FittingProductsSheetModel, FittingProductsSheetEvent>(FittingProductsSheetModel()) {

    init {
        dispatch(FittingProductsSheetIntent.CollectProducts)
    }

    override fun dispatch(intent: FittingProductsSheetIntent) {
        when (intent) {
            is FittingProductsSheetIntent.CollectProducts -> {
                launch {
                    fittingProductsFlowUseCase(Unit).collectLatest { entities ->
                        reduce { it.copy(cartProductEntities = entities) }
                    }
                }
            }
            is FittingProductsSheetIntent.ConfirmClick -> {
                launch {
                    val productIds = stateFlow.value.selectedProductIds.toList()
                    FittingProductsSheetEventManager.send(FittingProductsSheetEvent.ConfirmClick(productIds))
                }
            }
            is FittingProductsSheetIntent.DismissRequest -> {
                launch { FittingProductsSheetEventManager.send(FittingProductsSheetEvent.DismissRequest) }
            }
            is FittingProductsSheetIntent.ProductCheckedChange -> {
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
