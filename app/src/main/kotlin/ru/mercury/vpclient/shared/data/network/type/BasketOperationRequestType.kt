package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BasketOperationRequestType {
    @SerialName("addLine") ADD_LINE,
    @SerialName("changeLineQuantity") CHANGE_LINE_QUANTITY,
    @SerialName("changeLineOrder") CHANGE_LINE_ORDER,
    @SerialName("changeLinePaySwitch") CHANGE_LINE_PAY_SWITCH,
    @SerialName("changeLineLook") CHANGE_LINE_LOOK,
    @SerialName("changeLineColor") CHANGE_LINE_COLOR,
    @SerialName("changeLineSize") CHANGE_LINE_SIZE,
    @SerialName("removeLine") REMOVE_LINE,
    @SerialName("createLook") CREATE_LOOK,
    @SerialName("removeLook") REMOVE_LOOK,
    @SerialName("changeLookImage") CHANGE_LOOK_IMAGE,
    @SerialName("addSameProductWithDifferentSizeToLine") ADD_SAME_PRODUCT_WITH_DIFFERENT_SIZE_TO_LINE,
    @SerialName("removeProductFromLine") REMOVE_PRODUCT_FROM_LINE,
    @SerialName("clear") CLEAR,
    @SerialName("addAlternative") ADD_ALTERNATIVE,
    @SerialName("removeAlternative") REMOVE_ALTERNATIVE,
    @SerialName("switchProductWithAlternative") SWITCH_PRODUCT_WITH_ALTERNATIVE,
    @SerialName("fillAutomaticAlternatives") FILL_AUTOMATIC_ALTERNATIVES,
    @SerialName("changeAlternativePaletteState") CHANGE_ALTERNATIVE_PALETTE_STATE,
    @SerialName("switchAlternativeBackToOriginal") SWITCH_ALTERNATIVE_BACK_TO_ORIGINAL,
    @SerialName("switchProductWithCatalogProduct") SWITCH_PRODUCT_WITH_CATALOG_PRODUCT,
    @SerialName("changeLineLocation") CHANGE_LINE_LOCATION
}
