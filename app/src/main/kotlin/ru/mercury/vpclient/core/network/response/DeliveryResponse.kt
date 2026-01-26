@file:Suppress("unused")

package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeliveryResponse(
    @SerialName("deliveryId") val deliveryId: String?,
    @SerialName("deliveryStatus") val deliveryStatus: String?,
    @SerialName("detailedStatus") val detailedStatus: String?,
    @SerialName("codeGoal") val codeGoal: String?,
    @SerialName("codeTask") val codeTask: String?,
    @SerialName("isPaid") val isPaid: Boolean?,
    @SerialName("numberOfItems") val numberOfItems: Int?,
    @SerialName("sourceDocId") val sourceDocId: String?,
    @SerialName("sourceDocType") val sourceDocType: String?,
    @SerialName("startTime") val startTime: String?,
    @SerialName("arrivalFrom") val arrivalFrom: String?,
    @SerialName("arrivalTo") val arrivalTo: String?,
    @SerialName("sellerName") val sellerName: String?,
    @SerialName("sellerPhone") val sellerPhone: String?,
    @SerialName("clientName") val clientName: String?,
    @SerialName("clientPhone") val clientPhone: String?,
    @SerialName("clientAddress") val clientAddress: String?,
    @SerialName("clientCanCall") val clientCanCall: Boolean?,
    @SerialName("comment") val comment: String?,
    @SerialName("withSeller") val withSeller: Boolean?,
    @SerialName("inventLocationId") val inventLocationId: String?,
    @SerialName("inventLocationName") val inventLocationName: String?,
    @SerialName("isFitting") val isFitting: Boolean?,
    @SerialName("cargoPackages") val cargoPackages: List<CargoPackageResponse>?,
    @SerialName("clientClass") val clientClass: String?,
    @SerialName("extOrderId") val extOrderId: String?,

    @SerialName("syncStatus") val syncStatus: String?,
    @SerialName("items") val items: List<ProductResponse>?
) {
    companion object{
        const val DELIVERY_STATUS_PLANNED = "Planned"      // Заявка запланирована
        const val DELIVERY_STATUS_IN_PROCESS = "InProcess" // Заявка выполняется
        const val DELIVERY_STATUS_DONE = "Done"            // Заявка выполнена
        const val DELIVERY_STATUS_CLOSED = "Closed"        // Заявка закрыта

        const val DETAILED_STATUS_NONE = "None"                             // Назначен
        const val DETAILED_STATUS_PICKED_UP_GOODS = "picked_up_goods"       // Забрал товары
        const val DETAILED_STATUS_ON_ROUTE_TO_CLIENT = "on_route_to_client" // Выехал к клиенту
        const val DETAILED_STATUS_ARRIVED_AT_CLIENT = "arrived_at_client"   // Прибыл к клиенту
        const val DETAILED_STATUS_HANDED_OVER_GOODS = "handed_over_goods"   // Передал товары
        const val DETAILED_STATUS_FITTING_COMPLETED = "fitting_completed"   // Завершил примерку
        const val DETAILED_STATUS_PAYMENT_COMPLETED = "payment_completed"   // Оплата выполнена
        const val DETAILED_STATUS_LEFT_CLIENT = "left_client"               // Уехал от клиента
        const val DETAILED_STATUS_COMPLETED = "completed"                   // Заявка завершена

        const val SOURCE_DOC_TYPE_NONE = "None"                                                    // [0] Без документа
        const val SOURCE_DOC_TYPE_INVENT_TRANSFER = "InventTransfer"                               // [5] Заказ на перемещение
        const val SOURCE_DOC_TYPE_CREDIT_CLIENT = "CreditClient"                                   // [10] Долги клиентов
        const val SOURCE_DOC_TYPE_ESHOP_SALES = "EShopSales"                                       // [15] Интернет-заказ
        const val SOURCE_DOC_TYPE_BASKET_SALES = "BasketSales"                                     // [20] Продажа по корзине
        const val SOURCE_DOC_TYPE_RETAIL_SALES = "RetailSales"                                     // [25] Продажа (чек)
        const val SOURCE_DOC_TYPE_PROD = "Prod"                                                    // [30] Квитанция ателье
        const val SOURCE_DOC_TYPE_INVENT_TRANSFERT_REGISTER_TABLE = "InventTransfertRegisterTable" // [31] Журнал регистрации трансфертов
        const val SOURCE_DOC_TYPE_SALES = "Sales"                                                  // [35] Продажа по VIP Platinum
        const val SOURCE_DOC_TYPE_SALE_BY_REQUEST = "SaleByRequest"                                // [40] Оптовая продажа

        const val CLIENT_CLASS_VIP1 = "VIP1"
        const val CLIENT_CLASS_VIP2 = "VIP2"
        const val CLIENT_CLASS_VIP3 = "VIP3"
        const val CLIENT_CLASS_VIP4 = "VIP4"
        const val CLIENT_CLASS_VIP35 = "VIP35"
        const val CLIENT_CLASS_VIPIN = "VipIn"
        const val CLIENT_CLASS_PHOTO = "photo"
        const val CLIENT_CLASS_NEW = "New"
    }
}
