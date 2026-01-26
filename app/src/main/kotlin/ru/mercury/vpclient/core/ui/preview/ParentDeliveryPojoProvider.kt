package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.DeliveryResponse
import ru.mercury.vpclient.core.network.response.ProductResponse
import ru.mercury.vpclient.core.network.response.RouteResponse
import ru.mercury.vpclient.core.persistence.database.entity.CargoEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.core.persistence.database.pojo.DeliveryPojo

class ParentDeliveryPojoProvider: PreviewParameterProvider<DeliveryPojo> {
    override val values: Sequence<DeliveryPojo> = sequenceOf(
        DeliveryPojo(
            deliveryEntity = DeliveryEntity.Empty.copy(
                routeId = "КР-0137320",
                routeStatus = RouteResponse.ROUTE_STATUS_ASSIGNED,
                deliveryId = "ЗД02316904",
                deliveryStatus = DeliveryResponse.DELIVERY_STATUS_PLANNED,
                detailedStatus = DeliveryResponse.DETAILED_STATUS_NONE,
                codeGoal = "Примерка",
                codeTask = "Примерка с продавцом",
                isPaid = true,
                numberOfItems = 1,
                sourceDocId = "VPH079227",
                sourceDocType = DeliveryResponse.SOURCE_DOC_TYPE_ESHOP_SALES,
                startTime = "2025-02-24T14:00:00",
                arrivalFrom = "2025-02-14T14:00:00",
                arrivalTo = "2025-02-14T16:00:00",
                sellerName = "Киореску Виктория",
                sellerPhone = "+7 777 777 77 77",
                clientName = "Пиотровски Рената",
                clientPhone = "+7 777 777 77 77",
                clientAddress = "Красногорский район, к/п никольская слобода, ул. Соколиной охоты 29",
                comment = "Связь с помощником Игорем Нужен пропуск Охрана охрана!!! Ааааааааааа",
                clientCanCall = true,
                boutiqueId = "0766",
                boutiqueName = "Multibrand/1 Multibrand детское",
                isFitting = false,
                clientClass = DeliveryResponse.CLIENT_CLASS_VIP2,
                extOrderId = "VP152390",
                orderId = "VP152390",
                cargoPackagesLineIds = emptyList(),
                isParent = true,
                isOffline = true
            ),
            childDeliveryEntities = listOf(
                DeliveryEntity.Empty.copy(
                    routeId = "КР-0137320",
                    routeStatus = RouteResponse.ROUTE_STATUS_ASSIGNED,
                    deliveryId = "ЗД02316904",
                    deliveryStatus = DeliveryResponse.DELIVERY_STATUS_PLANNED,
                    detailedStatus = DeliveryResponse.DETAILED_STATUS_PICKED_UP_GOODS,
                    codeGoal = "Примерка",
                    codeTask = "Примерка с продавцом",
                    isPaid = false,
                    numberOfItems = 1,
                    sourceDocId = "VPH079227",
                    sourceDocType = "Sales",
                    startTime = "2025-02-24T14:00:00",
                    arrivalFrom = "2025-02-14T14:00:00",
                    arrivalTo = "2025-02-14T16:00:00",
                    sellerName = "Киореску Виктория",
                    sellerPhone = "+7 777 777 77 77",
                    clientName = "Пиотровски Рената",
                    clientPhone = "+7 777 777 77 77",
                    clientAddress = "Красногорский район, к/п никольская слобода, ул. Соколиной охоты 29",
                    comment = "Связь с помощником Игорем Нужен пропуск Охрана охрана!!! Ааааааааааа",
                    clientCanCall = true,
                    boutiqueId = "0766",
                    boutiqueName = "Multibrand/1 Multibrand детское",
                    isFitting = true,
                    clientClass = DeliveryResponse.CLIENT_CLASS_VIP2,
                    extOrderId = "VP152390",
                    orderId = "VP152390",
                    cargoPackagesLineIds = emptyList(),
                    parentDeliveryId = "ЗД02316904"
                )
            ),
            productEntities = listOf(
                ProductEntity.Empty.copy(
                    routeId = "КР-0137320",
                    boutiqueId = "0046",
                    deliveryId = "ЗД02316904",
                    cis = "46807180544945DC0001822653",
                    barcode = "0104680718054494215DC0001822653\u001d91EE10\u001d92E+b9KG/7jLEEeikRXmJEWY2DxhcHpM6TbJ98TI7MtfE=",
                    itemColorId = "Серый_0940",
                    itemId = "6931977",
                    itemName = "Брюки",
                    itemSizeId = "40",
                    lineId = "c76df7gf-3453-43j5-f837-8435h3857483h",
                    price = 643500.0,
                    priceWithDisc = 643500.0,
                    quantity = 1,
                    status = ProductResponse.STATUS_READY,
                    imageUrl = "https://st-cdn.tsum.com/sig/285ef6bfa14fbd81865f5dc189e4054f/width/247/i/d9/7e/aa/6f/38408711-0d2b-4d19-a900-80bc80b98940.jpg",
                    nameAlias = "EXBCK-Y089-3N/YLN006",
                    submarkName = "MVST"
                ),
                ProductEntity.Empty.copy(
                    routeId = "КР-0137320",
                    boutiqueId = "0046",
                    deliveryId = "ЗД02316904",
                    cis = "46807180544945DC0001822653",
                    barcode = "0104680718054494215DC0001822653\u001d91EE10\u001d92E+b9KG/7jLEEeikRXmJEWY2DxhcHpM6TbJ98TI7MtfE=",
                    itemColorId = "Серый_0940",
                    itemId = "6931977",
                    itemName = "Брюки",
                    itemSizeId = "40",
                    lineId = "c76df7gf-3453-43j5-f837-8435h3857483h",
                    price = 643500.0,
                    priceWithDisc = 643500.0,
                    quantity = 1,
                    status = ProductResponse.STATUS_READY,
                    imageUrl = "https://st-cdn.tsum.com/sig/285ef6bfa14fbd81865f5dc189e4054f/width/247/i/d9/7e/aa/6f/38408711-0d2b-4d19-a900-80bc80b98940.jpg",
                    nameAlias = "EXBCK-Y089-3N/YLN006",
                    submarkName = "MVST"
                ),
                ProductEntity.Empty.copy(
                    routeId = "КР-0137320",
                    boutiqueId = "0046",
                    deliveryId = "ЗД02316904",
                    cis = "46807180544945DC0001822653",
                    barcode = "0104680718054494215DC0001822653\u001d91EE10\u001d92E+b9KG/7jLEEeikRXmJEWY2DxhcHpM6TbJ98TI7MtfE=",
                    itemColorId = "Серый_0940",
                    itemId = "6931977",
                    itemName = "Брюки",
                    itemSizeId = "40",
                    lineId = "c76df7gf-3453-43j5-f837-8435h3857483h",
                    price = 643500.0,
                    priceWithDisc = 643500.0,
                    quantity = 1,
                    status = ProductResponse.STATUS_READY,
                    imageUrl = "https://st-cdn.tsum.com/sig/285ef6bfa14fbd81865f5dc189e4054f/width/247/i/d9/7e/aa/6f/38408711-0d2b-4d19-a900-80bc80b98940.jpg",
                    nameAlias = "EXBCK-Y089-3N/YLN006",
                    submarkName = "MVST"
                ),
                ProductEntity.Empty.copy(
                    routeId = "КР-0137320",
                    boutiqueId = "0046",
                    deliveryId = "ЗД02316904",
                    cis = "46807180544945DC0001822653",
                    barcode = "0104680718054494215DC0001822653\u001d91EE10\u001d92E+b9KG/7jLEEeikRXmJEWY2DxhcHpM6TbJ98TI7MtfE=",
                    itemColorId = "Серый_0940",
                    itemId = "6931977",
                    itemName = "Брюки",
                    itemSizeId = "40",
                    lineId = "c76df7gf-3453-43j5-f837-8435h3857483h",
                    price = 643500.0,
                    priceWithDisc = 643500.0,
                    quantity = 1,
                    status = ProductResponse.STATUS_READY,
                    imageUrl = "https://st-cdn.tsum.com/sig/285ef6bfa14fbd81865f5dc189e4054f/width/247/i/d9/7e/aa/6f/38408711-0d2b-4d19-a900-80bc80b98940.jpg",
                    nameAlias = "EXBCK-Y089-3N/YLN006",
                    submarkName = "MVST"
                ),
                ProductEntity.Empty.copy(
                    routeId = "КР-0137320",
                    boutiqueId = "0046",
                    deliveryId = "ЗД02316904",
                    cis = "46807180544945DC0001822653",
                    barcode = "0104680718054494215DC0001822653\u001d91EE10\u001d92E+b9KG/7jLEEeikRXmJEWY2DxhcHpM6TbJ98TI7MtfE=",
                    itemColorId = "Серый_0940",
                    itemId = "6931977",
                    itemName = "Брюки",
                    itemSizeId = "40",
                    lineId = "c76df7gf-3453-43j5-f837-8435h3857483h",
                    price = 643500.0,
                    priceWithDisc = 643500.0,
                    quantity = 1,
                    status = ProductResponse.STATUS_READY,
                    imageUrl = "https://st-cdn.tsum.com/sig/285ef6bfa14fbd81865f5dc189e4054f/width/247/i/d9/7e/aa/6f/38408711-0d2b-4d19-a900-80bc80b98940.jpg",
                    nameAlias = "EXBCK-Y089-3N/YLN006",
                    submarkName = "MVST"
                )
            ),
            cargoEntities = listOf(
                CargoEntity(
                    routeId = "КР-0137320",
                    boutiqueId = "0046",
                    deliveryId = "ЗД02316904",
                    barcode = "5911493792",
                    lineIds = listOf("ce3bcd99-b8d3-44aa-b078-0fe2e6382274"),
                    scannedInBoutique = false,
                    scannedAtDelivery = false
                )
            )
        )
    )
}
