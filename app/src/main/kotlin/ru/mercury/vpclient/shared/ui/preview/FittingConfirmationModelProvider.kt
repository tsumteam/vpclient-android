package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationModel
import ru.mercury.vpclient.features.fitting_confirmation.model.FittingConfirmationPlaceType
import ru.mercury.vpclient.features.fitting_confirmation.navigation.FittingConfirmationRoute
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval

class FittingConfirmationModelProvider: PreviewParameterProvider<FittingConfirmationModel> {
    private val products = CartProductProvider().values.take(3).toList()
    private val firstInterval = FittingConfirmationDeliveryInterval(
        id = "2026-05-13T10:00_2026-05-13T12:00",
        dayId = "2026-05-13",
        dayTitle = "13 мая",
        timeTitle = "10:00-12:00",
        summary = "13 мая с 10:00 до 12:00"
    )
    private val secondInterval = FittingConfirmationDeliveryInterval(
        id = "2026-05-13T12:00_2026-05-13T14:00",
        dayId = "2026-05-13",
        dayTitle = "13 мая",
        timeTitle = "12:00-14:00",
        summary = "13 мая с 12:00 до 14:00"
    )
    private val thirdInterval = FittingConfirmationDeliveryInterval(
        id = "2026-06-13T10:00_2026-06-13T12:00",
        dayId = "2026-06-13",
        dayTitle = "13 июня",
        timeTitle = "10:00-12:00",
        summary = "13 июня с 10:00 до 12:00"
    )
    private val firstDelivery = FittingConfirmationDeliveryGroup(
        id = "delivery_0",
        products = products.take(1),
        intervals = listOf(firstInterval, secondInterval)
    )
    private val secondDelivery = FittingConfirmationDeliveryGroup(
        id = "delivery_1",
        products = products.drop(1),
        intervals = listOf(thirdInterval)
    )

    override val values: Sequence<FittingConfirmationModel> = sequenceOf(
        base(products).copy(
            clientAddress = null,
            selectedPlaceType = FittingConfirmationPlaceType.Home
        ),
        base(products).copy(
            deliveryMode = FittingConfirmationDeliveryMode.Single,
            deliveryGroups = emptyList()
        ),
        base(products),
        base(products).copy(
            expandedDeliveryId = firstDelivery.id
        ),
        base(products).copy(
            isIntervalsLoading = true,
            singleIntervals = emptyList(),
            deliveryGroups = emptyList()
        )
    )

    private fun base(products: List<CartProduct>): FittingConfirmationModel {
        return FittingConfirmationModel(
            route = FittingConfirmationRoute(productIds = products.map { it.id }),
            products = products,
            boutiqueAddress = "Барвиха Luxury Village",
            clientAddress = "Москва, Петровка, 2",
            selectedPlaceType = FittingConfirmationPlaceType.Boutique,
            deliveryMode = FittingConfirmationDeliveryMode.Multiple,
            singleIntervals = listOf(firstInterval, secondInterval, thirdInterval),
            deliveryGroups = listOf(firstDelivery, secondDelivery),
            selectedSingleDayId = firstInterval.dayId,
            selectedSingleIntervalId = firstInterval.id,
            selectedDeliveryDayIds = mapOf(
                firstDelivery.id to firstInterval.dayId,
                secondDelivery.id to thirdInterval.dayId
            ),
            selectedDeliveryIntervalIds = mapOf(
                firstDelivery.id to firstInterval.id,
                secondDelivery.id to thirdInterval.id
            )
        )
    }
}
