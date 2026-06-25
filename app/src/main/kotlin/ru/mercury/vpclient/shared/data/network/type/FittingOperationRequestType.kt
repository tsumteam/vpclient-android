package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class FittingOperationRequestType {
    @SerialName("addLine") ADD_LINE,
    @SerialName("changeLinePaySwitch") CHANGE_LINE_PAY_SWITCH,
    @SerialName("returnProduct") RETURN_PRODUCT,
    @SerialName("changeLineSize") CHANGE_LINE_SIZE,
    @SerialName("changeLineColor") CHANGE_LINE_COLOR,
    @SerialName("addDayToExpirationDate") ADD_DAY_TO_EXPIRATION_DATE,
    @SerialName("changeDelivery") CHANGE_DELIVERY,
    @SerialName("changeLineDelivery") CHANGE_LINE_DELIVERY,
    @SerialName("confirmDelivery") CONFIRM_DELIVERY,
    @SerialName("mergeLineIntoDelivery") MERGE_LINE_INTO_DELIVERY,
    @SerialName("returnProductToFitting") RETURN_PRODUCT_TO_FITTING,
    @SerialName("changeLineTag") CHANGE_LINE_TAG
}
