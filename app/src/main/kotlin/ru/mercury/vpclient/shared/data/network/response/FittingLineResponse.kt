package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.DateReceiptExpiredStatus
import ru.mercury.vpclient.shared.data.network.type.FittingLogisticStatus

@Serializable
data class FittingLineResponse(
    @SerialName("lineId") val lineId: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("paySwitch") val paySwitch: Boolean? = null,
    @SerialName("dateOfExpiration") val dateOfExpiration: String? = null,
    @SerialName("dateOfExpirationBadgeColorHex") val dateOfExpirationBadgeColorHex: String? = null,
    @SerialName("logisticStatus") val logisticStatus: FittingLogisticStatus? = null,
    @SerialName("logisticStatusAsStringClient") val logisticStatusAsStringClient: String? = null,
    @SerialName("logisticStatusAsStringEmployee") val logisticStatusAsStringEmployee: String? = null,
    @SerialName("logisticStatusRejectReason") val logisticStatusRejectReason: String? = null,
    @SerialName("dateReceiptAsString") val dateReceiptAsString: String? = null,
    @SerialName("dateReceiptExpiredStatus") val dateReceiptExpiredStatus: DateReceiptExpiredStatus? = null,
    @SerialName("barcode") val barcode: String? = null,
    @SerialName("locationId") val locationId: String? = null,
    @SerialName("locationAsString") val locationAsString: String? = null,
    @SerialName("tag") val tag: String? = null,
    @SerialName("product") val product: CatalogProductSearchCardResponse? = null,
    @SerialName("controls") val controls: FittingLineControlsResponse? = null
)
