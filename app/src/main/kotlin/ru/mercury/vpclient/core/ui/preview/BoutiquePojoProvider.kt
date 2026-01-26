package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.DeliveryResponse
import ru.mercury.vpclient.core.network.response.RouteResponse
import ru.mercury.vpclient.core.persistence.database.entity.BoutiqueEntity
import ru.mercury.vpclient.core.persistence.database.entity.CargoEntity
import ru.mercury.vpclient.core.persistence.database.entity.DeliveryEntity
import ru.mercury.vpclient.core.persistence.database.pojo.BoutiquePojo

class BoutiquePojoProvider: PreviewParameterProvider<BoutiquePojo> {
    override val values: Sequence<BoutiquePojo> = sequenceOf(
        BoutiquePojo(
            boutiqueEntity = BoutiqueEntity(
                routeId = "КР-0137320",
                routeStatus = RouteResponse.ROUTE_STATUS_RUNNING,
                boutiqueId = "0766",
                boutiqueName = "Multibrand/1 Multibrand детское",
                position = 1
            ),
            deliveryEntities = listOf(
                DeliveryEntity.Empty.copy(
                    routeId = "КР-0137320",
                    routeStatus = RouteResponse.ROUTE_STATUS_ASSIGNED,
                    deliveryId = "ЗД02316904",
                    deliveryStatus = DeliveryResponse.DELIVERY_STATUS_PLANNED,
                    detailedStatus = DeliveryResponse.DETAILED_STATUS_PICKED_UP_GOODS,
                    codeGoal = "Примерка",
                    codeTask = "Примерка с продавцом",
                    isPaid = true,
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
                    isFitting = false,
                    clientClass = DeliveryResponse.CLIENT_CLASS_VIP2,
                    extOrderId = "VP152390",
                    orderId = "VP152390"
                )
            ),
            cargoEntities = listOf(
                CargoEntity(
                    routeId = "КР-0137320",
                    boutiqueId = "0046",
                    deliveryId = "ЗД02316904",
                    barcode = "5911493792",
                    lineIds = listOf("ce3bcd99-b8d3-44aa-b078-0fe2e6382274"),
                    scannedInBoutique = true,
                    scannedAtDelivery = true
                )
            )
        )
    )
}
