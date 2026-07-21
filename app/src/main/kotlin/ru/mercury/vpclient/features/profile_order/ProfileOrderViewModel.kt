package ru.mercury.vpclient.features.profile_order

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.features.profile_order.intent.ProfileOrderIntent
import ru.mercury.vpclient.features.profile_order.model.ProfileOrderModel
import ru.mercury.vpclient.features.profile_order.navigation.ProfileOrderRoute
import ru.mercury.vpclient.features.profile_root.event.ProfileRootEventManager
import ru.mercury.vpclient.shared.data.event.ProfileOrderDeliveryGroupState
import ru.mercury.vpclient.shared.domain.usecase.ProfileOrderUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderProductItemState

@HiltViewModel(assistedFactory = ProfileOrderViewModel.Factory::class)
class ProfileOrderViewModel @AssistedInject constructor(
    @Assisted private val route: ProfileOrderRoute,
    private val profileOrderUseCase: ProfileOrderUseCase
): ClientViewModel<ProfileOrderIntent, ProfileOrderModel, Event>(ProfileOrderModel()) {

    init {
        dispatch(ProfileOrderIntent.CollectRoute)
        dispatch(ProfileOrderIntent.LoadData)
    }

    override fun dispatch(intent: ProfileOrderIntent) {
        when (intent) {
            is ProfileOrderIntent.CollectRoute -> {
                reduce { it.copy(orderNumber = route.orderNumber, amount = route.amount) }
            }
            is ProfileOrderIntent.LoadData -> {
                launch {
                    val details = profileOrderUseCase(route.orderNumber).getOrThrow()
                    reduce {
                        when (details) {
                            null -> it.copy(isLoading = false)
                            else -> ProfileOrderModel(
                                orderNumber = details.orderNumber.ifBlank { route.orderNumber },
                                amount = details.amount.ifBlank { route.amount },
                                creationDate = details.creationDate,
                                isLoading = false,
                                isPaymentAlertVisible = details.isPaymentAlertVisible,
                                paymentAlertRemainingMinutes = details.paymentAlertRemainingMinutes,
                                deliveryGroups = details.deliveries.map { delivery ->
                                    ProfileOrderDeliveryGroupState(
                                        id = delivery.id,
                                        date = delivery.date,
                                        address = delivery.address,
                                        products = delivery.products.map { product ->
                                            ProfileOrderProductItemState(
                                                productId = product.productId,
                                                imageUrl = product.imageUrl,
                                                brand = product.brand,
                                                urlBrandLogo = product.urlBrandLogo,
                                                name = product.name,
                                                color = product.color,
                                                article = product.article,
                                                price = product.price,
                                                size = product.size,
                                                status = product.status,
                                                quantity = product.quantity,
                                                isGiftCard = product.isGiftCard
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
            is ProfileOrderIntent.BackClick -> launch { ProfileRootEventManager.send(BackRoute) }
            is ProfileOrderIntent.DeliveryClick -> {
                launch {
                    MainEventManager.send(
                        FittingConfirmationRoute(
                            productIds = intent.productIds,
                            deliveryId = intent.deliveryId.ifBlank { null }
                        )
                    )
                }
            }
            is ProfileOrderIntent.ProductClick -> {
                if (intent.productId.isBlank()) return
                launch { MainEventManager.send(DetailsRoute(intent.productId, openedFromCart = true)) }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: ProfileOrderRoute): ProfileOrderViewModel
    }
}
