package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.FORMAT_RUB
import ru.mercury.vpclient.core.network.request.OrderLineBarcodeRequest
import ru.mercury.vpclient.core.network.request.OrderLineRequest
import ru.mercury.vpclient.core.network.response.OrderLineStatusResponse
import ru.mercury.vpclient.core.network.response.ProductResponse
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity
import java.util.Locale

val ProductEntity.isNotEmpty: Boolean
    get() = this != ProductEntity.Empty

val ProductEntity?.orEmpty: ProductEntity
    get() = this ?: ProductEntity.Empty

val ProductEntity.fittingOrderLineRequest: OrderLineRequest
    get() = OrderLineRequest(
        lineId = lineId,
        actionType = OrderLineStatusResponse.ACTION_TYPE_STAY
    )

val ProductEntity.returnOrderLineRequest: OrderLineRequest
    get() = OrderLineRequest(
        lineId = lineId,
        actionType = OrderLineStatusResponse.ACTION_TYPE_RETURN
    )

val ProductEntity.orderLineBarcodeRequest: OrderLineBarcodeRequest
    get() = OrderLineBarcodeRequest(lineId, barcode)

val ProductEntity.isStatusNone: Boolean
    get() = status == ""

val ProductEntity.isStatusReady: Boolean
    get() = status == ProductResponse.STATUS_READY

val ProductEntity.isStatusShipped: Boolean
    get() = status == ProductResponse.STATUS_SHIPPED

val ProductEntity.isStatusFitting: Boolean
    get() = status == ProductResponse.STATUS_DELIVERED

val ProductEntity.isStatusReturn: Boolean
    get() = status == ProductResponse.STATUS_RETURN

val ProductEntity.isStatusSold: Boolean
    get() = status == ProductResponse.STATUS_SOLD

val ProductEntity.isStatePayment: Boolean
    get() = !isStatusSold && isToPay

val ProductEntity.isStateFitting: Boolean
    get() = isStatusFitting && !isToPay

val ProductEntity.isStateReturn: Boolean
    get() = isStatusReturn && !isToPay

val ProductEntity.isStateShipped: Boolean
    get() = isStatusShipped && !isToPay

val ProductEntity.priceText: String
    get() = String.format(Locale.getDefault(), FORMAT_RUB, price.rub)

val ProductEntity.priceWithDiscText: String
    get() = String.format(Locale.getDefault(), FORMAT_RUB, priceWithDisc.rub)

val List<ProductEntity>.priceText: String
    get() = String.format(Locale.getDefault(), FORMAT_RUB, sumOf(ProductEntity::price).rub)

val List<ProductEntity>.priceWithDiscText: String
    get() = String.format(Locale.getDefault(), FORMAT_RUB, sumOf(ProductEntity::priceWithDisc).rub)

val List<ProductEntity>.isStateFitting: Boolean
    get() = isNotEmpty() && all(ProductEntity::isStateFitting)
