@file:OptIn(ExperimentalSerializationApi::class)

package ru.mercury.vpclient.core.network.entity

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import ru.mercury.vpclient.core.network.request.FilterValuesRequest
import ru.mercury.vpclient.core.network.request.FilteredProductsQuantityRequest
import ru.mercury.vpclient.core.network.request.FilteredProductsRequest
import ru.mercury.vpclient.core.network.request.FiltersRequest
import ru.mercury.vpclient.core.network.response.CatalogCategoriesBasicResponse
import ru.mercury.vpclient.core.network.response.CatalogProductDetailCardV2Response
import ru.mercury.vpclient.core.network.response.CurrentUserResponse
import ru.mercury.vpclient.core.network.response.FilterValuesResponse
import ru.mercury.vpclient.core.network.response.FilteredProductsQuantityResponse
import ru.mercury.vpclient.core.network.response.FilteredProductsResponse
import ru.mercury.vpclient.core.network.response.FiltersResponse

// fixme

@Serializable
data class ActionInProductSearchDto(
    val name: String? = null,
    val isCashDesk: Boolean? = null
)

@Serializable
data class ActionItemDto(
    val dateFrom: String? = null,
    val dateTo: String? = null,
    val fashionImageId: Int? = null,
    val fashionImageItemsQty: Int? = null,
    val id: Int? = null,
    val imageUrl: String? = null,
    val orderBy: Int? = null,
    val title: String? = null,
    val url: String? = null,
    val category: CatalogItemTypeEnum? = null,
    val type: ActionItemTypeEnumDto? = null
)

@Serializable
data class ActionItemDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ActionItemDto? = null
)

@Serializable
enum class ActionItemTypeEnumDto {
    @SerialName("newCollection")
    NEW_COLLECTION,
    @SerialName("httpLinkAction")
    HTTP_LINK_ACTION,
    @SerialName("newCollectionVM")
    NEW_COLLECTION_VM,
    @SerialName("specialOfferAction")
    SPECIAL_OFFER_ACTION
}

@Serializable
data class ActionPushRequestDto(
    val pushMessage: String? = null,
    val pushTitle: String? = null,
    val clientIds: List<String>? = null,
    val employeeIds: List<String>? = null
)

@Serializable
data class ActionsResponseDto(
    val items: List<ActionItemDto>? = null
)

@Serializable
data class ActionsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ActionsResponseDto? = null
)

@Serializable
data class ActiveClientResponseDto(
    val clientEmail: String? = null,
    val clientId: String? = null,
    val clientMiddleName: String? = null,
    val clientName: String? = null,
    val clientPhone: String? = null,
    val clientSurname: String? = null,
    val isAvailableFittingHome: Boolean? = null
)

@Serializable
data class ActiveClientResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ActiveClientResponseDto? = null
)

@Serializable
data class ActiveEmployeeResponseDto(
    val employeeEmail: String? = null,
    val employeeId: String? = null,
    val employeeMiddleName: String? = null,
    val employeeName: String? = null,
    val employeePhone: String? = null,
    val employeeSurname: String? = null,
    val photoUrl: String? = null,
    val previewPhotoUrl: String? = null,
    val employeeBotiqueAddress: String? = null,
    val employeeBotiqueAddressShort: String? = null,
    val employeeBrand: String? = null
)

@Serializable
data class ActiveEmployeeResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ActiveEmployeeResponseDto? = null
)

@Serializable
enum class ActivityCounterTypeRequestEnum {
    @SerialName("basket")
    BASKET,
    @SerialName("messenger")
    MESSENGER,
    @SerialName("order")
    ORDER,
    @SerialName("fitting")
    FITTING,
    @SerialName("compilation")
    COMPILATION,
    @SerialName("axaptaNotification")
    AXAPTA_NOTIFICATION,
    @SerialName("clientNotification")
    CLIENT_NOTIFICATION,
    @SerialName("messages")
    MESSAGES
}

@Serializable
data class AddProductToActionRequestDto(
    val actionId: Int? = null,
    val productId: Int? = null
)

@Serializable
data class AddProductToFashionImageRequestDto(
    val fashionImageNo: Int? = null,
    val fashionImageCollectionId: Int? = null,
    val productId: String? = null,
    val orderBy: Int? = null
)

@Serializable
data class AddProductsInFoldersRequestDto(
    val folderIds: List<Int>? = null,
    val items: List<PaletteProductRequestItemDto>? = null
)

@Serializable
data class AddressSuggestionDto(
    val title: String? = null,
    val coordinate: CoordinateDto? = null
)

@Serializable
data class AddressSuggestionDtoItemsDto(
    val items: List<AddressSuggestionDto>? = null
)

@Serializable
data class AddressSuggestionDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AddressSuggestionDtoItemsDto? = null
)

@Serializable
data class AggregatedActivityCounterResponseDto(
    val items: List<AggregatedActivityCounterResponseItemDto>? = null
)

@Serializable
data class AggregatedActivityCounterResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AggregatedActivityCounterResponseDto? = null
)

@Serializable
data class AggregatedActivityCounterResponseItemDto(
    val type: ActivityCounterTypeRequestEnum? = null,
    val value: Int? = null
)

@Serializable
enum class AlternativesPaletteStatusEnum {
    @SerialName("open")
    OPEN,
    @SerialName("hidden")
    HIDDEN
}

@Serializable
data class ApproveFittingDeliveryManualPickupRequestDto(
    val deliveryId: String? = null,
    val actionType: String? = null,
    val items: List<ApproveFittingDeliveryManualPickupRequestItemDto>? = null
)

@Serializable
data class ApproveFittingDeliveryManualPickupRequestItemDto(
    val lineId: String? = null
)

@Serializable
data class ApproveFittingDeliveryManualPickupResponse(
    val result: String? = null
)

@Serializable
data class ApproveFittingDeliveryManualPickupResponseResponseDto(
    val error: ErrorDto? = null,
    val data: ApproveFittingDeliveryManualPickupResponse? = null
)

@Serializable
data class ApproveFittingDeliveryRequestDto(
    val deliveryId: String? = null,
    val actionType: String? = null,
    val comment: String? = null,
    val items: List<ApproveFittingDeliveryRequestItemDto>? = null
)

@Serializable
data class ApproveFittingDeliveryRequestItemDto(
    val lineId: String? = null
)

@Serializable
data class ApproveFittingDeliveryResponse(
    val result: String? = null
)

@Serializable
data class ApproveFittingDeliveryResponseResponseDto(
    val error: ErrorDto? = null,
    val data: ApproveFittingDeliveryResponse? = null
)

@Serializable
data class ApproveFittingExpirationDateExtensionRequestDto(
    val deliveryId: String? = null,
    val actionType: String? = null,
    val qtyDaysExtension: Int? = null,
    val items: List<ApproveFittingExpirationDateExtensionRequestItemDto>? = null
)

@Serializable
data class ApproveFittingExpirationDateExtensionRequestItemDto(
    val lineId: String? = null
)

@Serializable
data class ApproveFittingExpirationDateExtensionResponse(
    val result: String? = null
)

@Serializable
data class ApproveFittingExpirationDateExtensionResponseResponseDto(
    val error: ErrorDto? = null,
    val data: ApproveFittingExpirationDateExtensionResponse? = null
)

@Serializable
data class AvailableColorsRequestDto(
    val itemId: String? = null,
    val sizeId: String? = null
)

@Serializable
data class AvailableColorsResponseDto(
    val items: List<AvailableColorsResponseItemDto>? = null
)

@Serializable
data class AvailableColorsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AvailableColorsResponseDto? = null
)

@Serializable
data class AvailableColorsResponseItemDto(
    val colorFullName: String? = null,
    val colorHex: String? = null,
    val colorId: String? = null,
    val isOnlyInVipSite: Boolean? = null,
    val isOnlyInTransit: Boolean? = null
)

@Serializable
data class AvailableLocationsForProductResponseDto(
    val barcode: String? = null,
    val items: List<AvailableLocationsForProductResponseItemDto>? = null
)

@Serializable
data class AvailableLocationsForProductResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AvailableLocationsForProductResponseDto? = null
)

@Serializable
data class AvailableLocationsForProductResponseItemDto(
    val locationId: String? = null,
    val locationName: String? = null
)

@Serializable
data class AvailableLocationsRequestDto(
    val colorId: String? = null,
    val sizeId: String? = null,
    val itemId: String? = null
)

@Serializable
data class AvailableLocationsResponseDto(
    val items: List<AvailableLocationsResponseItemDto>? = null
)

@Serializable
data class AvailableLocationsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AvailableLocationsResponseDto? = null
)

@Serializable
data class AvailableLocationsResponseItemDto(
    val locationId: String? = null,
    val locationName: String? = null,
    val isOnlyInVipSite: Boolean? = null,
    val isOnlyInTransit: Boolean? = null
)

@Serializable
data class AvailableSizesRequestDto(
    val colorId: String? = null,
    val itemId: String? = null
)

@Serializable
data class AvailableSizesResponseDto(
    val items: List<AvailableSizesResponseItemDto>? = null,
    val sizeTableTitle: String? = null,
    val sizeTableUrl: String? = null
)

@Serializable
data class AvailableSizesResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AvailableSizesResponseDto? = null
)

@Serializable
data class AvailableSizesResponseItemDto(
    val sizeFullName: String? = null,
    val sizeId: String? = null,
    val inOrder: Boolean? = null,
    val inStock: Boolean? = null,
    val inStockShops: List<String>? = null,
    val isOnlyInVipSite: Boolean? = null,
    val isOnlyInTransit: Boolean? = null
)

@Serializable
data class AxLoyaltyCardAuthRequestDto(
    val basketId: String? = null,
    val loyaltyCardNumber: String? = null,
    val docType: Int? = null
)

@Serializable
data class AxLoyaltyCardAuthResponseDto(
    val token: String? = null,
    val identityIsRequired: Boolean? = null,
    val showPinField: Boolean? = null
)

@Serializable
data class AxLoyaltyCardAuthResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AxLoyaltyCardAuthResponseDto? = null
)

@Serializable
data class AxLoyaltyCardCalcAndReserveBonusPayRequestDto(
    val basketId: String? = null,
    val loyaltyCardNumber: String? = null,
    val token: String? = null,
    val bonusAmount: Double? = null,
    val docType: Int? = null
)

@Serializable
data class AxLoyaltyCardCalcAndReserveBonusPayResponseDto(
    val confirmedAmount: Double? = null,
    val paymentAmount: Double? = null
)

@Serializable
data class AxLoyaltyCardCalcAndReserveBonusPayResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AxLoyaltyCardCalcAndReserveBonusPayResponseDto? = null
)

@Serializable
data class AxLoyaltyCardCalcBonusAmountRequestDto(
    val basketId: String? = null,
    val loyaltyCardNumber: String? = null,
    val token: String? = null,
    val bonusAmount: Double? = null,
    val docType: Int? = null
)

@Serializable
data class AxLoyaltyCardCalcBonusAmountResponseDto(
    val confirmedAmount: Double? = null,
    val paymentAmount: Double? = null
)

@Serializable
data class AxLoyaltyCardCalcBonusAmountResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AxLoyaltyCardCalcBonusAmountResponseDto? = null
)

@Serializable
data class AxLoyaltyCardCheckPinRequestDto(
    val basketId: String? = null,
    val loyaltyCardNumber: String? = null,
    val token: String? = null,
    val smsCode: String? = null,
    val docType: Int? = null
)

@Serializable
data class AxLoyaltyCardCheckPinResponseDto(
    val correctPin: Boolean? = null
)

@Serializable
data class AxLoyaltyCardCheckPinResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: AxLoyaltyCardCheckPinResponseDto? = null
)

@Serializable
data class AxLoyaltyCardRollBackBonusPayRequestDto(
    val basketId: String? = null,
    val loyaltyCardNumber: String? = null,
    val token: String? = null,
    val docType: Int? = null
)

@Serializable
data class BarcodeScanRequestDto(
    val barcode: String? = null
)

@Serializable
data class BasketAddLineOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val colorId: String? = null,
    val itemId: String? = null,
    val lineId: String? = null,
    val sizeId: String? = null,
    val compilationLookProductId: Int? = null
)

@Serializable
data class BasketAddProductByBarcodeAndLocationIdRequestDto(
    val clientId: String? = null,
    val barcode: String? = null,
    val locationId: String? = null
)

@Serializable
data class BasketAddProductByBarcodeRequestDto(
    val clientId: String? = null,
    val barcode: String? = null
)

@Serializable
data class BasketAddProductFromDetailedStocksRequestDto(
    val clientId: String? = null,
    val itemId: String? = null,
    val colorId: String? = null,
    val sizeId: String? = null,
    val locationId: String? = null
)

@Serializable
data class BasketAddSameProductWithDifferentSizeToLineOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val productId: String? = null,
    val sizeId: String? = null
)

@Serializable
data class BasketAlternativeResponseDto(
    val product: CatalogProductSearchCardDto? = null,
    val alternativeId: String? = null,
    val alternativeType: BasketAlternativeType? = null
)

@Serializable
enum class BasketAlternativeType {
    @SerialName("manual")
    MANUAL,
    @SerialName("original")
    ORIGINAL,
    @SerialName("automatic")
    AUTOMATIC
}

@Serializable
enum class BasketAlternativesPaletteState {
    @SerialName("open")
    OPEN,
    @SerialName("hidden")
    HIDDEN
}

@Serializable
data class BasketChangeAlternativePaletteStateOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val paletteState: BasketAlternativesPaletteState? = null
)

@Serializable
data class BasketChangeLineColorOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val colorId: String? = null,
    val lineId: String? = null
)

@Serializable
data class BasketChangeLineLocationOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val locationId: String? = null
)

@Serializable
data class BasketChangeLineLookOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val lookId: String? = null
)

@Serializable
data class BasketChangeLineOrderOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val order: Int? = null
)

@Serializable
data class BasketChangeLinePaySwitchOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val paySwitch: Boolean? = null
)

@Serializable
data class BasketChangeLineQuantityOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val quantity: Int? = null
)

@Serializable
data class BasketChangeLineSizeOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val sizeId: String? = null
)

@Serializable
data class BasketChangeLookImageOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val imageUrl: String? = null,
    val lookId: String? = null
)

@Serializable
data class BasketCheckoutOrderItemDto(
    val product: CatalogProductSearchCardDto? = null,
    val productId: String? = null
)

@Serializable
data class BasketCheckoutOrderResponseDto(
    val deliveryTimes: List<DeliveryTimeDto>? = null,
    val items: List<BasketCheckoutOrderItemDto>? = null
)

@Serializable
data class BasketCheckoutOrderResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: BasketCheckoutOrderResponseDto? = null
)

@Serializable
data class BasketClearOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null
)

@Serializable
data class BasketCreateLookOperationLineRequestItemDto(
    val lineId: String? = null
)

@Serializable
data class BasketCreateLookOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val imageUrl: String? = null,
    val items: List<BasketCreateLookOperationLineRequestItemDto>? = null,
    val lookId: String? = null,
    val name: String? = null
)

@Serializable
data class BasketFillAutomaticAlternativesOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null
)

@Serializable
data class BasketForCheckoutResponseDto(
    val basketResponseDto: BasketResponseDto? = null,
    val availableBonusSum: Double? = null,
    val loyaltyCardNumber: String? = null,
    val totalAvailableBonuses: Double? = null
)

@Serializable
data class BasketForCheckoutResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: BasketForCheckoutResponseDto? = null
)

@Serializable
data class BasketGetDeliveryTimesForFittingRequestDto(
    val pairedUserId: String? = null,
    val basketLineIds: List<String>? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val kittingType: KittingTypeDto? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val isClientInHouse: Boolean? = null,
    val vipShopperManualPickup: Boolean? = null
)

@Serializable
data class BasketGetDeliveryTimesForOrderRequestDto(
    val pairedUserId: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val address: String? = null,
    val addressComment: String? = null
)

@Serializable
data class BasketLineControlsResponseDto(
    val isChooseAlternativeFromCatalogAvailable: Boolean? = null,
    val alternativesPalette: AlternativesPaletteStatusEnum? = null,
    val isAlternativePaletteControlsAvailable: Boolean? = null,
    val isManualAddAlternativeAvailable: Boolean? = null,
    val isSwitchAlternativeBackToOriginalAvailable: Boolean? = null,
    val isChangingLocationAvailable: Boolean? = null
)

@Serializable
data class BasketLineResponseDto(
    val lineId: String? = null,
    val lookId: String? = null,
    val order: Int? = null,
    val paySwitch: Boolean? = null,
    val products: List<BasketProductResponseDto>? = null,
    val quantity: Int? = null,
    val barcode: String? = null,
    val locationId: String? = null,
    val locationAsString: String? = null,
    val controls: BasketLineControlsResponseDto? = null,
    val alternatives: List<BasketAlternativeResponseDto>? = null
)

@Serializable
data class BasketLookResponseDto(
    val imageUrl: String? = null,
    val lookId: String? = null,
    val name: String? = null
)

@Serializable
enum class BasketOperationInitiatorDtoEnum {
    @SerialName("client")
    CLIENT,
    @SerialName("employee")
    EMPLOYEE,
    @SerialName("system")
    SYSTEM,
    @SerialName("axapta")
    AXAPTA
}

@Serializable
data class BasketOperationRequestDto(
    val pairedUserId: String? = null,
    val items: List<JsonElement> = emptyList()
)

@Serializable
data class BasketOperationRequestItemBaseDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null
)

@Serializable
enum class BasketOperationRequestTypeEnum {
    @SerialName("addLine")
    ADD_LINE,
    @SerialName("changeLineQuantity")
    CHANGE_LINE_QUANTITY,
    @SerialName("changeLineOrder")
    CHANGE_LINE_ORDER,
    @SerialName("changeLinePaySwitch")
    CHANGE_LINE_PAY_SWITCH,
    @SerialName("changeLineLook")
    CHANGE_LINE_LOOK,
    @SerialName("changeLineColor")
    CHANGE_LINE_COLOR,
    @SerialName("changeLineSize")
    CHANGE_LINE_SIZE,
    @SerialName("removeLine")
    REMOVE_LINE,
    @SerialName("createLook")
    CREATE_LOOK,
    @SerialName("removeLook")
    REMOVE_LOOK,
    @SerialName("changeLookImage")
    CHANGE_LOOK_IMAGE,
    @SerialName("addSameProductWithDifferentSizeToLine")
    ADD_SAME_PRODUCT_WITH_DIFFERENT_SIZE_TO_LINE,
    @SerialName("removeProductFromLine")
    REMOVE_PRODUCT_FROM_LINE,
    @SerialName("clear")
    CLEAR,
    @SerialName("addAlternative")
    ADD_ALTERNATIVE,
    @SerialName("removeAlternative")
    REMOVE_ALTERNATIVE,
    @SerialName("switchProductWithAlternative")
    SWITCH_PRODUCT_WITH_ALTERNATIVE,
    @SerialName("fillAutomaticAlternatives")
    FILL_AUTOMATIC_ALTERNATIVES,
    @SerialName("changeAlternativePaletteState")
    CHANGE_ALTERNATIVE_PALETTE_STATE,
    @SerialName("switchAlternativeBackToOriginal")
    SWITCH_ALTERNATIVE_BACK_TO_ORIGINAL,
    @SerialName("switchProductWithCatalogProduct")
    SWITCH_PRODUCT_WITH_CATALOG_PRODUCT,
    @SerialName("changeLineLocation")
    CHANGE_LINE_LOCATION
}

@Serializable
data class BasketProductResponseDto(
    val product: CatalogProductSearchCardDto? = null,
    val productId: String? = null
)

@Serializable
data class BasketRemoveAlternativeOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val alternativeId: String? = null
)

@Serializable
data class BasketRemoveLineOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null
)

@Serializable
data class BasketRemoveLookOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lookId: String? = null
)

@Serializable
data class BasketRemoveProductFromLineOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val productId: String? = null
)

@Serializable
data class BasketResponseDto(
    val editor: BasketOperationInitiatorDtoEnum? = null,
    val id: String? = null,
    val lines: List<BasketLineResponseDto>? = null,
    val looks: List<BasketLookResponseDto>? = null,
    val catalogActionDisclaimer: String? = null,
    val timestamp: String? = null,
    val version: Int? = null
)

@Serializable
data class BasketResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: BasketResponseDto? = null
)

@Serializable
data class BasketSwitchAlternativeBackToOriginalOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null
)

@Serializable
data class BasketSwitchProductWithAlternativeOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val alternativeId: String? = null
)

@Serializable
data class BasketSwitchProductWithCatalogProductOperationRequestItemDto(
    val operationType: BasketOperationRequestTypeEnum? = null,
    val operationOrder: Int? = null,
    val colorId: String? = null,
    val itemId: String? = null,
    val sizeId: String? = null,
    val lineId: String? = null
)

@Serializable
data class BasketSyncRequestDto(
    val allBaskets: Boolean? = null
)

@Serializable
data class BlvLinkContentDto(
    val orderNumber: String? = null,
    val products: List<CatalogProductSearchCardVNDto>? = null,
    val type: BlvLinkContentTypeEnum? = null
)

@Serializable
data class BlvLinkContentDtoResponseDto(
    val error: ErrorDto? = null,
    val data: BlvLinkContentDto? = null
)

@Serializable
enum class BlvLinkContentTypeEnum {
    @SerialName("basket")
    BASKET,
    @SerialName("fitting")
    FITTING,
    @SerialName("order")
    ORDER
}

@Serializable
data class BooleanResponseDto(
    val error: ErrorDto? = null,
    val data: Boolean? = null
)

@Serializable
data class BoutiqueAddressDto(
    val boutiqueId: String? = null,
    val address: String? = null,
    val shortAddress: String? = null,
    val brandName: String? = null
)

@Serializable
data class BoutiqueDto(
    val brand: String? = null,
    val brandPhone: String? = null
)

@Serializable
data class CardInfoResponseDto(
    val loyaltyCardNumber: String? = null,
    val bonusAmount: Double? = null,
    val clientName: String? = null,
    val maxPercent: Double? = null,
    val actionID: String? = null,
    val typeCard: String? = null,
    val dateEndCard: String? = null,
    val qrCode: String? = null
)

@Serializable
data class CardInfoResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CardInfoResponseDto? = null
)

@Serializable
data class CardTypeDto(
    val id: Long? = null,
    val type: String? = null,
    val cardName: String? = null,
    val bonusRules: String? = null,
    val termsForObtaining: String? = null,
    val validity: String? = null,
    val renewalTerms: String? = null,
    val termsOfUse: String? = null
)

@Serializable
data class CardTypeDtoItemsDto(
    val items: List<CardTypeDto>? = null
)

@Serializable
data class CardTypeDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CardTypeDtoItemsDto? = null
)

@Serializable
data class CatalogBrandDto(
    val id: Int? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val isTopBrand: Boolean? = null
)

@Serializable
data class CatalogBrandsCategoryDto(
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val brands: List<CatalogBrandDto>? = null
)

@Serializable
data class CatalogBrandsResponseDto(
    val items: List<CatalogBrandsCategoryDto>? = null
)

@Serializable
data class CatalogBrandsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CatalogBrandsResponseDto? = null
)

@Serializable
data class CatalogCategoriesResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CatalogCategoriesBasicResponse? = null
)

@Serializable
data class CatalogCategoryDto(
    val id: Int? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val categoryType: CatalogCategoryTypeEnumDto? = null,
    val children: List<CatalogCategoryDto>? = null
)

@Serializable
enum class CatalogCategoryTypeEnumDto {
    @SerialName("catalog")
    CATALOG,
    @SerialName("giftCard")
    GIFT_CARD,
    @SerialName("action")
    ACTION
}

@Serializable
data class CatalogFashionImageCardDto(
    val id: String? = null,
    val imageUrl: String? = null,
    val items: List<CatalogProductSearchCardDto>? = null
)

@Serializable
data class CatalogFashionImageCardDtoItemsDto(
    val items: List<CatalogFashionImageCardDto>? = null
)

@Serializable
data class CatalogFashionImageCardDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CatalogFashionImageCardDtoItemsDto? = null
)

@Serializable
enum class CatalogItemTypeEnum {
    @SerialName("none")
    NONE,
    @SerialName("man")
    MAN,
    @SerialName("woman")
    WOMAN,
    @SerialName("child")
    CHILD,
    @SerialName("all")
    ALL
}

@Serializable
data class CatalogProductActionFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdValueDto> = emptyList()
)

@Serializable
data class CatalogProductAttributeFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val filterSubtype: String? = null,
    val values: List<FilterIdValueDto> = emptyList()
)

@Serializable
data class CatalogProductAvailableToClientFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterBoolValueDto> = emptyList()
)

@Serializable
data class CatalogProductBrandFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdValueDto> = emptyList()
)

@Serializable
data class CatalogProductCarryOverFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterBoolValueDto> = emptyList()
)

@Serializable
data class CatalogProductCategoryFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdTreeValueDto> = emptyList()
)

@Serializable
data class CatalogProductColorFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdValueDto> = emptyList()
)

@Serializable
data class CatalogProductDetailCardV2DtoResponseDto(
    val error: ErrorDto? = null,
    val data: CatalogProductDetailCardV2Response? = null
)

@Serializable
data class CatalogProductEkttFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdTreeValueDto> = emptyList()
)

@Serializable
data class CatalogProductFilterDtoBase(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null
)

@Serializable
data class CatalogProductFilterSizeValueDto(
    val valueType: FilterValueTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val value: Int? = null,
    val labelInternational: String? = null,
    val labelItalian: String? = null,
    val labelFrench: String? = null,
    val labelInches: String? = null
)

@Serializable
data class CatalogProductInStockFilterRequest(
    val filterType: String?,
    val label: String?,
    val labelPhotoUrl: String?,
    val hint: String?,
    val isMultiSelect: Boolean?,
    val order: Int?,
    val ribbonSettings: FilterRibbonSettingsDto?,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto?,
    val valueType: FilterValueTypeEnumDto?,
    val values: List<FilterBoolValueDto>
) {
    companion object {
        const val ACTION = "action"
        const val AVAILABLE_TO_CLIENT = "availableToClient"
        const val BRAND = "brand"
        const val CATEGORY = "category"
        const val COLOR = "color"
        const val EKTT = "ektt"
        const val KTT = "ktt"
        const val IN_STOCK = "inStock"
        const val NO_PHOTO = "noPhoto"
        const val NO_RUS_SIZE = "noRusSize"
        const val PRICE = "price"
        const val SEASON = "season"
        const val SIZE_CODE = "sizeCode"
        const val SIZE = "size"
        const val STOCK_ONLY_IN_TRANSIT = "stockOnlyInTransit"
        const val LOCATION = "location"
        const val SITE = "site"
        const val ATTRIBUTE = "attribute"
        const val NEW_ARRIVALS = "newArrivals"
        const val CARRY_OVER = "carryOver"
    }
}

@Serializable
data class CatalogProductKttFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdTreeValueDto> = emptyList()
)

@Serializable
data class CatalogProductLocationFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdValueDto> = emptyList()
)

@Serializable
data class CatalogProductNewArrivalsFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterBoolValueDto> = emptyList()
)

@Serializable
data class CatalogProductNoPhotoFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterBoolValueDto> = emptyList()
)

@Serializable
data class CatalogProductNoRusSizeFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterBoolValueDto> = emptyList()
)

@Serializable
data class CatalogProductPriceFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterDecimalRangeValueDto> = emptyList()
)

@Serializable
data class CatalogProductScanHistoryCard(
    val brand: String? = null,
    val colorId: String? = null,
    val colorName: String? = null,
    val eKttId: String? = null,
    val imageUrl: String? = null,
    val imageUrls: List<String>? = null,
    val itemId: String? = null,
    val name: String? = null,
    val order: Int? = null,
    val price: Double? = null,
    val sizeId: String? = null,
    val sizeName: String? = null
)

@Serializable
data class CatalogProductSearchCardDto(
    val oneSize: Boolean? = null,
    val article: String? = null,
    val brand: String? = null,
    val urlBrandLogo: String? = null,
    val colorId: String? = null,
    val colorName: String? = null,
    val eKttId: String? = null,
    val id: String? = null,
    val imageUrl: String? = null,
    val imageUrls: List<String>? = null,
    val isCharity: Boolean? = null,
    val isSeasonDisplay: Boolean? = null,
    val itemId: String? = null,
    val lookId: String? = null,
    val name: String? = null,
    val order: Int? = null,
    val paySwitch: Boolean? = null,
    val price: Double? = null,
    val priceWithoutDiscount: Double? = null,
    val currentRetailPrice: Double? = null,
    val quantity: Int? = null,
    val season: String? = null,
    val sizes: List<SizeInProductSearchDto>? = null,
    val actions: List<ActionInProductSearchDto>? = null,
    val onlyInTransit: Boolean? = null,
    val onlyInVipSite: Boolean? = null,
    val breadcrumbs: List<String>? = null,
    val compilationLookProductId: Int? = null,
    val isGiftCard: Boolean? = null
)

@Serializable
data class CatalogProductSearchCardDtoItemsResponse(
    val items: List<CatalogProductSearchCardDto>? = null
)

@Serializable
data class CatalogProductSearchCardDtoItemsResponseResponseDto(
    val error: ErrorDto? = null,
    val data: CatalogProductSearchCardDtoItemsResponse? = null
)

@Serializable
data class CatalogProductSearchCardVNDto(
    val oneSize: Boolean? = null,
    val article: String? = null,
    val brand: String? = null,
    val colorId: String? = null,
    val colorName: String? = null,
    val eKttId: String? = null,
    val id: String? = null,
    val imageUrl: String? = null,
    val imageUrls: List<String>? = null,
    val isSeasonDisplay: Boolean? = null,
    val itemId: String? = null,
    val lookId: String? = null,
    val name: String? = null,
    val order: Int? = null,
    val paySwitch: Boolean? = null,
    val price: Double? = null,
    val priceWithoutDiscount: Double? = null,
    val quantity: Int? = null,
    val season: String? = null,
    val sizes: List<SizeInProductSearchVNDto>? = null
)

@Serializable
data class CatalogProductSeasonFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdValueDto> = emptyList()
)

@Serializable
data class CatalogProductSiteFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdValueDto> = emptyList()
)

@Serializable
data class CatalogProductSizeCodeFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterIdValueDto> = emptyList()
)

@Serializable
data class CatalogProductSizeFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<CatalogProductFilterSizeValueDto> = emptyList()
)

@Serializable
data class CatalogProductStockOnlyInTransitFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val values: List<FilterBoolValueDto> = emptyList()
)

@Serializable
data class CatalogScanHistoryDto(
    val items: List<CatalogProductScanHistoryCard>? = null
)

@Serializable
data class CatalogScanHistoryDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CatalogScanHistoryDto? = null
)

@Serializable
data class ChangeActionRequestDto(
    val actionId: Int? = null,
    val orderBy: Int? = null,
    val type: Int? = null,
    val imageUrl: String? = null,
    val isHidden: Boolean? = null,
    val title: String? = null
)

@Serializable
data class CheckOutAddressesControlDto(
    val isDeliveryToClientAvailable: Boolean? = null
)

@Serializable
data class CheckOutAddressesDto(
    val clientAddress: ClientAddressWithCoordinateDto? = null,
    val boutiqueAddress: BoutiqueAddressDto? = null,
    val controls: CheckOutAddressesControlDto? = null
)

@Serializable
data class CheckOutAddressesDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CheckOutAddressesDto? = null
)

@Serializable
data class CheckOutDeliverySettingsDto(
    val deliveryType: DeliveryTypeDto? = null,
    val leaveItemsToClient: Boolean? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val vipShopperManualPickup: Boolean? = null,
    val deliveryComment: String? = null
)

@Serializable
data class CheckOutKittingSettingsDto(
    val kittingType: KittingTypeDto? = null
)

@Serializable
data class CheckUserResponseDto(
    val result: CheckUserResultEnum? = null
)

@Serializable
data class CheckUserResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CheckUserResponseDto? = null
)

@Serializable
enum class CheckUserResultEnum {
    @SerialName("exists")
    EXISTS,
    @SerialName("notExists")
    NOT_EXISTS,
    @SerialName("invalidPhone")
    INVALID_PHONE
}

@Serializable
data class ClientActivityInfoDto(
    val clientId: String? = null,
    val activationDate: String? = null,
    val lastActivity: String? = null
)

@Serializable
data class ClientActivityInfoDtoItemsDto(
    val items: List<ClientActivityInfoDto>? = null
)

@Serializable
data class ClientActivityInfoDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ClientActivityInfoDtoItemsDto? = null
)

@Serializable
data class ClientAddressBaseDto(
    val address: String? = null,
    val flat: String? = null,
    val entrance: String? = null,
    val intercom: String? = null,
    val floor: String? = null,
    val comment: String? = null
)

@Serializable
data class ClientAddressDto(
    val addressId: Int? = null,
    val address: String? = null,
    val flat: String? = null,
    val entrance: String? = null,
    val intercom: String? = null,
    val floor: String? = null,
    val comment: String? = null
)

@Serializable
data class ClientAddressDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ClientAddressDto? = null
)

@Serializable
data class ClientAddressWithCoordinateDto(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val addressId: Int? = null,
    val address: String? = null,
    val flat: String? = null,
    val entrance: String? = null,
    val intercom: String? = null,
    val floor: String? = null,
    val comment: String? = null
)

@Serializable
data class ClientAddressWithCoordinateDtoItemsDto(
    val items: List<ClientAddressWithCoordinateDto>? = null
)

@Serializable
data class ClientAddressWithCoordinateDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ClientAddressWithCoordinateDtoItemsDto? = null
)

@Serializable
enum class ClientClassEnumDto {
    @SerialName("vip3.5")
    VIP_3_5,
    @SerialName("vip4")
    VIP_4,
    @SerialName("vip3")
    VIP_3,
    @SerialName("vip2")
    VIP_2,
    @SerialName("vip1")
    VIP_1,
    @SerialName("photo")
    PHOTO
}

@Serializable
data class ClientCompilationPayloadDto(
    val clientCompilations: List<ClientCompilationPayloadItemDto>? = null,
    val text: String? = null,
    val title: String? = null,
    val citation: String? = null,
    val citatedMessageId: Int? = null,
    val type: PayloadType? = null
)

@Serializable
data class ClientCompilationPayloadItemDto(
    val compilationId: Int? = null,
    val compilationName: String? = null,
    val compilationDescription: String? = null,
    val imageUrl: String? = null
)

@Serializable
data class ClientSyncRequestDto(
    val allClients: Boolean? = null
)

@Serializable
data class CommonSalesDto(
    val weekCaption: String? = null,
    val weekPeriodCaption: String? = null,
    val btqWeekTotalSalesByPlan: Double? = null,
    val btqWeekPlan: Double? = null,
    val commonSalesPayrollPercent: Double? = null,
    val btqWeekTotalSalesAmount: Double? = null,
    val btqWeekLeftForPlan: Double? = null,
    val daysForPlan: Int? = null,
    val daysWorkedWithForecast: Int? = null,
    val commonSalesPayrollPartAmount: Double? = null
)

@Serializable
data class CompilationCopyRequestDto(
    val id: Int? = null,
    val name: String? = null,
    val employeeId: String? = null
)

@Serializable
data class CompilationCreateRequestDto(
    val name: String? = null,
    val description: String? = null
)

@Serializable
data class CompilationDto(
    val badge: Int? = null,
    val id: Int? = null,
    val collageUrl: String? = null,
    val photoUrl: String? = null,
    val name: String? = null,
    val description: String? = null,
    val createDate: String? = null,
    val looksQty: Int? = null,
    val lookProductsQty: Int? = null,
    val isStatsAvailable: Boolean? = null
)

@Serializable
data class CompilationDtoItemsDto(
    val items: List<CompilationDto>? = null
)

@Serializable
data class CompilationDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CompilationDtoItemsDto? = null
)

@Serializable
data class CompilationLookPayloadCompilationItemDto(
    val id: Int? = null,
    val name: String? = null
)

@Serializable
data class CompilationLookPayloadDto(
    val compilationLooks: List<CompilationLookPayloadItemDto> = emptyList(),
    val text: String? = null,
    val title: String? = null,
    val citation: String? = null,
    val citatedMessageId: Int? = null,
    val type: PayloadType? = null
)

@Serializable
data class CompilationLookPayloadItemDto(
    val id: Int? = null,
    val name: String? = null,
    val imageUrl: String? = null,
    val compilation: CompilationLookPayloadCompilationItemDto? = null
)

@Serializable
data class CompilationLookSaveProductDto(
    val colorId: String? = null,
    val itemId: String? = null
)

@Serializable
data class CompilationLookSaveRequestDto(
    val compilationId: Int? = null,
    val meta: JsonElement? = null,
    val name: String? = null,
    val products: List<CompilationLookSaveProductDto>? = null
)

typealias CompilationLookSourceDto = Int

@Serializable
data class CompilationShareRequestDto(
    val clientIds: List<String>? = null
)

@Serializable
enum class CompilationStatus {
    @SerialName("none")
    NONE,
    @SerialName("active")
    ACTIVE,
    @SerialName("opened")
    OPENED,
    @SerialName("sent")
    SENT,
    @SerialName("archived")
    ARCHIVED,
    @SerialName("notSent")
    NOT_SENT
}

@Serializable
data class CompilationsResponseItemDto(
    val id: Int? = null,
    val collageUrl: String? = null,
    val photoUrl: String? = null,
    val name: String? = null,
    val description: String? = null,
    val createDate: String? = null,
    val looksQty: Int? = null,
    val lookProductsQty: Int? = null,
    val isStatsAvailable: Boolean? = null
)

@Serializable
data class CompilationsWithStatsByClientResponseDto(
    val items: List<CompilationsWithStatsResponseItemDto>? = null
)

@Serializable
data class CompilationsWithStatsByClientResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CompilationsWithStatsByClientResponseDto? = null
)

@Serializable
data class CompilationsWithStatsByClientsResponseItemDto(
    val statistics: StatsByClientsResponseDto? = null,
    val id: Int? = null,
    val collageUrl: String? = null,
    val photoUrl: String? = null,
    val name: String? = null,
    val description: String? = null,
    val createDate: String? = null,
    val looksQty: Int? = null,
    val lookProductsQty: Int? = null,
    val isStatsAvailable: Boolean? = null
)

@Serializable
data class CompilationsWithStatsResponseDto(
    val items: List<CompilationsWithStatsByClientsResponseItemDto>? = null
)

@Serializable
data class CompilationsWithStatsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CompilationsWithStatsResponseDto? = null
)

@Serializable
data class CompilationsWithStatsResponseItemDto(
    val statistics: StatisticsResponseDto? = null,
    val id: Int? = null,
    val collageUrl: String? = null,
    val photoUrl: String? = null,
    val name: String? = null,
    val description: String? = null,
    val createDate: String? = null,
    val looksQty: Int? = null,
    val lookProductsQty: Int? = null,
    val isStatsAvailable: Boolean? = null
)

@Serializable
data class ContinueLoginRequestDto(
    val phone: String? = null,
    val code: String? = null
)

@Serializable
data class CoordinateDto(
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
data class CreateClientAddressRequestDto(
    val clientId: String? = null,
    val address: ClientAddressBaseDto? = null,
    val coordinate: CoordinateDto? = null
)

@Serializable
data class CreateCompilationResultDto(
    val compilationId: Int? = null
)

@Serializable
data class CreateCompilationResultDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CreateCompilationResultDto? = null
)

@Serializable
data class CreateLookFromFashionImageRequestDto(
    val items: List<CreateLookFromFashionImageRequestItemDto>? = null
)

@Serializable
data class CreateLookFromFashionImageRequestItemDto(
    val fashionImageId: Int? = null,
    val fashionImageCollectionId: Int? = null,
    val compilationId: Int? = null,
    val name: String? = null
)

@Serializable
data class CreateOrderWithGiftCardRequestDto(
    val pairedUserId: String? = null,
    val templateId: Int? = null,
    val itemId: String? = null,
    val amount: Double? = null,
    val type: GiftCardTypeDto? = null,
    val email: String? = null,
    val phone: String? = null,
    val dateFrom: String? = null,
    val dateTo: String? = null,
    val paymentType: PaymentType? = null
)

@Serializable
data class CreatePaletteFolderRequestDto(
    val name: String? = null,
    val items: List<PaletteProductRequestItemDto>? = null
)

@Serializable
enum class GenderEnum {
    @SerialName("none") NONE,
    @SerialName("masculine") MASCULINE,
    @SerialName("feminine") FEMININE
}

@Serializable
data class CurrentUserDtoResponseDto(
    val error: ErrorDto? = null,
    val data: CurrentUserResponse? = null
)

@Serializable
enum class DateReceiptExpiredStatusDto {
    @SerialName("ok")
    OK,
    @SerialName("changed")
    CHANGED,
    @SerialName("overdue")
    OVERDUE
}

@Serializable
data class DeleteClientAddressRequestDto(
    val clientId: String? = null,
    val addressId: Int? = null
)

@Serializable
data class DeleteFashionImageRequestDto(
    val fashionImageCollectionId: Int? = null,
    val fashionImageNo: Int? = null
)

@Serializable
data class DeleteLinkedCardRequestDto(
    val loyaltyCardNumber: String? = null
)

@Serializable
data class DeleteMessageRequestDto(
    val pairedUserId: String? = null,
    val messageIds: List<Long>? = null
)

@Serializable
data class DeleteProductFromFashionImageRequestDto(
    val fashionImageNo: Int? = null,
    val fashionImageCollectionId: Int? = null,
    val productId: String? = null
)

@Serializable
data class DeleteProductsFromFolderRequestDto(
    val items: List<PaletteProductRequestItemDto>? = null
)

@Serializable
data class DeliveryTimeDto(
    @SerialName("from")
    val fromValue: String? = null,
    val to: String? = null
)

@Serializable
data class DeliveryTimesForSingleProductRequestDto(
    val pairedUserId: String? = null,
    val itemId: String? = null,
    val colorId: String? = null,
    val sizeId: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val kittingType: KittingTypeDto? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val isClientInHouse: Boolean? = null,
    val vipShopperManualPickup: Boolean? = null
)

@Serializable
enum class DeliveryTypeDto {
    @SerialName("logistic")
    LOGISTIC,
    @SerialName("employee")
    EMPLOYEE
}

@Serializable
data class DetailedStocksRequestDto(
    val itemId: String? = null
)

@Serializable
data class DetailedStocksResponseDto(
    val availableRemains: Int? = null,
    val groups: List<DetailedStocksResponseGroupByColorAndSeasonDto>? = null
)

@Serializable
data class DetailedStocksResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: DetailedStocksResponseDto? = null
)

@Serializable
data class DetailedStocksResponseGroupByColorAndSeasonDto(
    val colorId: String? = null,
    val baseColor: String? = null,
    val colorHex: String? = null,
    val season: String? = null,
    val rows: List<DetailedStocksResponseRowDto>? = null
)

@Serializable
data class DetailedStocksResponseRowControlsDto(
    val isGetReserveButtonAvailable: Boolean? = null,
    val isGetTransferButtonAvailable: Boolean? = null,
    val isAddToBasketButtonAvailable: Boolean? = null,
    val isSubscribeButtonAvailable: Boolean? = null
)

@Serializable
data class DetailedStocksResponseRowDto(
    val itemId: String? = null,
    val locationId: String? = null,
    val locationString: String? = null,
    val siteId: String? = null,
    val site: String? = null,
    val sizeId: String? = null,
    val totalRemains: Int? = null,
    val availableRemains: Int? = null,
    val serialId: String? = null,
    val moratoryEndDate: String? = null,
    val system: SystemEnumDto? = null,
    val controls: DetailedStocksResponseRowControlsDto? = null,
    val price: Double? = null
)

@Serializable
data class DetermineGenderByClientIdRequestDto(
    val clientId: String? = null
)

@Serializable
data class DigineticaFilterValuesRequestDto(
    val searchText: String? = null,
    val request: FilterValuesRequest? = null
)

@Serializable
data class DigineticaFilteredProductsQuantityRequestDto(
    val searchText: String? = null,
    val request: FilteredProductsQuantityRequest? = null
)

@Serializable
data class DigineticaFilteredProductsRequestDto(
    val searchText: String? = null,
    val filteredProductsRequest: FilteredProductsRequest? = null
)

@Serializable
data class DigineticaFilteredProductsResponseDto(
    val products: FilteredProductsResponse? = null,
    val correction: String? = null
)

@Serializable
data class DigineticaFilteredProductsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: DigineticaFilteredProductsResponseDto? = null
)

@Serializable
data class DigineticaFiltersRequestDto(
    val searchText: String? = null,
    val filtersRequest: FiltersRequest? = null
)

@Serializable
data class EditFashionImageRequestDto(
    val fashionImageNo: Int? = null,
    val fashionImageCollectionId: Int? = null,
    val imageUrl: String? = null
)

@Serializable
data class EditMessageRequestDto(
    val pairedUserId: String? = null,
    val messageId: Long? = null,
    val text: String? = null
)

@Serializable
data class EditPaletteFolderRequestDto(
    val name: String? = null
)

@Serializable
data class EmployeeCompilationProductsReportRequestDto(
    val employeeId: String? = null
)

@Serializable
data class EmployeeEventsReportRequestDto(
    val dateFrom: String? = null,
    val dateTo: String? = null
)

@Serializable
data class EmployeeFittingBrandLimitDto(
    val brand: String? = null,
    val limitPerDay: Int? = null,
    val currentValue: Int? = null
)

@Serializable
data class EmployeeFittingLimitsDto(
    val isLimited: Boolean? = null,
    val isLimitExceeded: Boolean? = null,
    val limitPerDay: Int? = null,
    val currentValue: Int? = null,
    val brandLimits: List<EmployeeFittingBrandLimitDto>? = null
)

@Serializable
data class EmployeeFittingLimitsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: EmployeeFittingLimitsDto? = null
)

@Serializable
data class EmployeeFittingOrderAccessFlagsResponseDto(
    val fittingHome: Boolean? = null,
    val fittingBtq: Boolean? = null,
    val orderHome: Boolean? = null,
    val orderBtq: Boolean? = null
)

@Serializable
data class EmployeeFittingOrderAccessFlagsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: EmployeeFittingOrderAccessFlagsResponseDto? = null
)

@Serializable
enum class EmployeeNotificationTypeEnumDto {
    @SerialName("fitting")
    FITTING,
    @SerialName("order")
    ORDER,
    @SerialName("stockAvailability")
    STOCK_AVAILABILITY,
    @SerialName("sync")
    SYNC,
    @SerialName("newArrivals")
    NEW_ARRIVALS,
    @SerialName("newAction")
    NEW_ACTION,
    @SerialName("newCatalog")
    NEW_CATALOG
}

@Serializable
data class EmployeeNotificationsClientFilterDto(
    val filterType: EmployeeNotificationsFilterTypeEnum? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterStringValueDto> = emptyList()
)

@Serializable
data class EmployeeNotificationsFilterDtoBase(
    val filterType: EmployeeNotificationsFilterTypeEnum? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null
)

@Serializable
enum class EmployeeNotificationsFilterTypeEnum {
    @SerialName("client")
    CLIENT,
    @SerialName("timestamp")
    TIMESTAMP,
    @SerialName("notificationType")
    NOTIFICATION_TYPE
}

@Serializable
data class EmployeeNotificationsFiltersRequestDto(
    val filters: List<JsonElement> = emptyList()
)

@Serializable
data class EmployeeNotificationsFiltersResponseDto(
    val quantity: Int? = null,
    val filters: List<JsonElement>? = null
)

@Serializable
data class EmployeeNotificationsFiltersResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: EmployeeNotificationsFiltersResponseDto? = null
)

@Serializable
data class EmployeeNotificationsNotificationTypeFilterDto(
    val filterType: EmployeeNotificationsFilterTypeEnum? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterStringValueDto> = emptyList()
)

@Serializable
data class EmployeeNotificationsRequestDto(
    val filters: List<JsonElement> = emptyList()
)

@Serializable
data class EmployeeNotificationsResponseDto(
    val items: List<EmployeeNotificationsResponseItemDto>? = null
)

@Serializable
data class EmployeeNotificationsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: EmployeeNotificationsResponseDto? = null
)

@Serializable
data class EmployeeNotificationsResponseItemDto(
    val type: EmployeeNotificationTypeEnumDto? = null,
    val message: String? = null,
    val deepLinkUrl: String? = null,
    val timestamp: String? = null,
    val clientName: String? = null,
    val hasBadge: Boolean? = null
)

@Serializable
data class EmployeeNotificationsTimestampFilterDto(
    val filterType: EmployeeNotificationsFilterTypeEnum? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterDateTimeRangeValueDto> = emptyList()
)

typealias ErrorCode = Int

@Serializable
data class ErrorDto(
    val code: ErrorCode? = null,
    val display: String? = null,
    val msg: String? = null,
    val reason: JsonElement? = null
)

@Serializable
data class ExportCompilationStatisticsRequestDto(
    val dateFrom: String? = null,
    val dateTo: String? = null
)

@Serializable
data class ExportCompilationStatisticsResponseDto(
    val items: List<ExportCompilationStatisticsResponseItemDto>? = null
)

@Serializable
data class ExportCompilationStatisticsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ExportCompilationStatisticsResponseDto? = null
)

@Serializable
data class ExportCompilationStatisticsResponseItemDto(
    val employeeId: String? = null,
    val employeeName: String? = null,
    val areaId: String? = null,
    val brandId: String? = null,
    val clientId: String? = null,
    val crmId: String? = null,
    val clientName: String? = null,
    val clientPhone: String? = null,
    val clientRegistrationDate: String? = null,
    val compilationName: String? = null,
    val compilationId: Int? = null,
    val compilationCreateDate: String? = null,
    val compilationDescription: String? = null,
    val isOpenCompilation: Boolean? = null,
    val isSentCompilation: Boolean? = null,
    val isActiveCompilation: Boolean? = null,
    val isArchivedCompilation: Boolean? = null,
    val clientCompilationCreateDate: String? = null,
    val clientCompilationOpeningDate: String? = null,
    val lookName: String? = null,
    val lookId: Int? = null,
    val lookPhotoUrl: String? = null,
    val lookCreateDate: String? = null,
    val lookSource: CompilationLookSourceDto? = null,
    val itemBrand: String? = null,
    val itemKttId: String? = null,
    val itemId: String? = null,
    val itemColorId: String? = null,
    val itemIsViewed: Boolean? = null,
    val itemIsAddedToBasket: Boolean? = null,
    val itemIsAddedToFitting: Boolean? = null,
    val price: Double? = null
)

@Serializable
data class ExportEmployeeEventsRequestDto(
    val dateFrom: String? = null,
    val dateTo: String? = null
)

@Serializable
data class ExportEmployeeEventsResponseDto(
    val items: List<ExportEmployeeEventsResponseItemDto>? = null
)

@Serializable
data class ExportEmployeeEventsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ExportEmployeeEventsResponseDto? = null
)

@Serializable
data class ExportEmployeeEventsResponseItemDto(
    val areaId: String? = null,
    val brandId: String? = null,
    val employeeName: String? = null,
    val employeeId: String? = null,
    val employeeHrId: Int? = null,
    val clientId: String? = null,
    val crmId: String? = null,
    val eventId: Int? = null,
    val reasonIds: String? = null,
    val reasonsAsString: String? = null,
    val eventCommunicationType: Int? = null,
    val eventCommunicationTypeString: String? = null,
    val eventComment: String? = null,
    val eventTime: String? = null,
    val taskIdSource: Int? = null,
    val taskCreatedDateSource: String? = null,
    val taskCompletionDateSource: String? = null,
    val taskExpectedAmountSource: Double? = null,
    val taskResult: Int? = null,
    val taskResultAsString: String? = null,
    val guestsNumber: Int? = null
)

@Serializable
data class ExportTasksRequestDto(
    val dateFrom: String? = null,
    val dateTo: String? = null
)

@Serializable
data class ExportTasksResponseDto(
    val items: List<ExportTasksResponseItemDto>? = null
)

@Serializable
data class ExportTasksResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ExportTasksResponseDto? = null
)

@Serializable
data class ExportTasksResponseItemDto(
    val areaId: String? = null,
    val brandId: String? = null,
    val employeeName: String? = null,
    val employeeId: String? = null,
    val employeeHrId: Int? = null,
    val clientId: String? = null,
    val crmId: String? = null,
    val taskId: Int? = null,
    val reasons: String? = null,
    val reasonsAsString: String? = null,
    val taskCommunicationType: Int? = null,
    val taskCommunicationTypeString: String? = null,
    val taskComment: String? = null,
    val taskCreatedDate: String? = null,
    val taskCompletionDate: String? = null,
    val taskRemindTimeSpan: String? = null,
    val taskIsOverdue: Boolean? = null,
    val eventIdSource: Int? = null,
    val taskExpectedAmount: Double? = null,
    val taskResult: Int? = null,
    val taskResultAsString: String? = null,
    val guestsNumber: Int? = null
)

@Serializable
enum class ExternalPushReceiverEnumDto {
    @SerialName("client")
    CLIENT,
    @SerialName("employee")
    EMPLOYEE
}

typealias ExternalPushStatusEnumDto = Int

@Serializable
data class FashionImageStatisticsReportRequestDto(
    val fashionImageIds: List<Int>? = null
)

@Serializable
data class FillAlternativesInBasketsRequestDto(
    val allBaskets: Boolean? = null
)

@Serializable
data class FilterBoolValueDto(
    val valueType: FilterValueTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val value: Boolean? = null
)

@Serializable
data class FilterDateTimeRangeValueDto(
    val valueType: FilterValueTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    @SerialName("from")
    val fromValue: String? = null,
    val to: String? = null
)

@Serializable
data class FilterDecimalRangeValueDto(
    val valueType: FilterValueTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    @SerialName("from")
    val fromValue: Double? = null,
    val to: Double? = null
)

@Serializable
data class FilterIdTreeValueDto(
    val valueType: FilterValueTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val level: Int? = null,
    val parent: Int? = null,
    val children: List<FilterIdTreeValueDto>? = null,
    val isLeafNode: Boolean? = null,
    val value: Int? = null
)

@Serializable
data class FilterIdValueDto(
    val valueType: FilterValueTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val value: Int? = null
)

@Serializable
data class FilterRibbonSettingsDto(
    val isVisible: Boolean? = null,
    val row: Int? = null,
    val order: Int? = null
)

@Serializable
data class FilterStringValueDto(
    val valueType: FilterValueTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val value: String? = null
)

@Serializable
data class FilterValueDtoBase(
    val valueType: FilterValueTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null
)

@Serializable
data class FilterValuePickerViewSettingsDto(
    val showSearchBar: Boolean? = null,
    val showSidePanelWithLetters: Boolean? = null,
    val showPhotos: Boolean? = null
)

@Serializable
enum class FilterValueTypeEnumDto {
    @SerialName("string")
    STRING,
    @SerialName("dateTimeRange")
    DATE_TIME_RANGE,
    @SerialName("decimalRange")
    DECIMAL_RANGE,
    @SerialName("id")
    ID,
    @SerialName("idTree")
    ID_TREE,
    @SerialName("bool")
    BOOL,
    @SerialName("catalogProductSize")
    CATALOG_PRODUCT_SIZE
}

@Serializable
data class FilterValuesRequestFilterDto(
    val filterType: String? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val hint: String? = null,
    val isMultiSelect: Boolean? = null,
    val order: Int? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val valuePickerViewSettings: FilterValuePickerViewSettingsDto? = null,
    val valueType: FilterValueTypeEnumDto? = null,
    val filterSubtype: String? = null,
    val values: List<JsonElement> = emptyList()
)

@Serializable
data class FilterValuesResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FilterValuesResponse? = null
)

@Serializable
data class FilteredProductsQuantityResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FilteredProductsQuantityResponse? = null
)

@Serializable
data class FilteredProductsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FilteredProductsResponse? = null
)

@Serializable
data class FiltersResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FiltersResponse? = null
)

@Serializable
data class FittingAddDayToExpirationDateOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null
)

@Serializable
data class FittingAddLineOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val colorId: String? = null,
    val itemId: String? = null,
    val sizeId: String? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val leaveItemsToClient: Boolean? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null,
    val isClientInHouse: Boolean? = null,
    val tag: String? = null,
    val compilationLookProductId: Int? = null
)

@Serializable
data class FittingChangeDeliveryOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val deliveryId: String? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val leaveItemsToClient: Boolean? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null,
    val isClientInHouse: Boolean? = null,
    val vipShopperManualPickup: Boolean? = null
)

@Serializable
data class FittingChangeLineColorOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val colorId: String? = null,
    val lineId: String? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val leaveItemsToClient: Boolean? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null,
    val isClientInHouse: Boolean? = null
)

@Serializable
data class FittingChangeLineDeliveryOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val deliveryId: String? = null,
    val lineId: String? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val leaveItemsToClient: Boolean? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null,
    val isClientInHouse: Boolean? = null,
    val vipShopperManualPickup: Boolean? = null
)

@Serializable
data class FittingChangeLinePaySwitchOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val paySwitch: Boolean? = null
)

@Serializable
data class FittingChangeLineSizeOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val sizeId: String? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val leaveItemsToClient: Boolean? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null,
    val isClientInHouse: Boolean? = null
)

@Serializable
data class FittingChangeLineTagOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null,
    val tag: String? = null
)

@Serializable
data class FittingCheckoutTagsRequestDto(
    val clientId: String? = null,
    val employeeId: String? = null
)

@Serializable
data class FittingCheckoutTagsResponseDto(
    val tags: List<String>? = null
)

@Serializable
data class FittingCheckoutTagsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FittingCheckoutTagsResponseDto? = null
)

@Serializable
data class FittingConfirmDeliveryOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val deliveryId: String? = null
)

@Serializable
data class FittingDeliveryControlsResponseDto(
    val isEditDeliveryAvailable: Boolean? = null,
    val isPaySwitchAvailable: Boolean? = null,
    val isExpirationDateExtensionAvailable: Boolean? = null,
    val isTransferFromStoreToHomeAvailable: Boolean? = null,
    val isConfirmDeliveryAvailable: Boolean? = null,
    val isRequiredToConfirmDeliverySignActive: Boolean? = null
)

@Serializable
data class FittingDeliveryResponseDto(
    val deliveryId: String? = null,
    val lines: List<FittingLineResponseDto>? = null,
    val order: Int? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val controls: FittingDeliveryControlsResponseDto? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val leaveItemsToClient: Boolean? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null,
    val deliveryStatusAsString: String? = null,
    val deliveryDateAsString: String? = null,
    val vipShopperManualPickup: Boolean? = null
)

@Serializable
data class FittingDeliveryTimeDeliveryDto(
    val products: List<FittingDeliveryTimeProductDto>? = null,
    val deliveryTimes: List<DeliveryTimeDto>? = null
)

@Serializable
data class FittingDeliveryTimeProductDto(
    val product: CatalogProductSearchCardDto? = null,
    val productId: String? = null
)

@Serializable
data class FittingDeliveryTimeResponseDto(
    val deliveryTimes: List<DeliveryTimeDto>? = null,
    val deliveries: List<FittingDeliveryTimeDeliveryDto>? = null
)

@Serializable
data class FittingDeliveryTimeResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FittingDeliveryTimeResponseDto? = null
)

@Serializable
data class FittingExternalPushRequestDto(
    val items: List<FittingExternalPushRequestItemDto>? = null
)

@Serializable
data class FittingExternalPushRequestItemDto(
    val fittingId: String? = null,
    val phone: String? = null,
    val pushMessage: String? = null,
    val pushTitle: String? = null,
    val messageId: String? = null,
    val clientPhone: String? = null,
    val pushReceiver: ExternalPushReceiverEnumDto? = null
)

@Serializable
data class FittingExternalPushResponseItemDto(
    val messageId: String? = null,
    val status: ExternalPushStatusEnumDto? = null,
    val errorMessage: String? = null
)

@Serializable
data class FittingExternalPushResponseItemDtoItemsDto(
    val items: List<FittingExternalPushResponseItemDto>? = null
)

@Serializable
data class FittingExternalPushResponseItemDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FittingExternalPushResponseItemDtoItemsDto? = null
)

@Serializable
data class FittingForCheckoutResponseDto(
    val fittingResponseDto: FittingResponseDto? = null,
    val availableBonusSum: Double? = null,
    val loyaltyCardNumber: String? = null,
    val totalAvailableBonuses: Double? = null
)

@Serializable
data class FittingForCheckoutResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FittingForCheckoutResponseDto? = null
)

@Serializable
data class FittingHistoryFilterDtoBase(
    val filterType: FittingHistoryFilterTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null
)

@Serializable
enum class FittingHistoryFilterTypeEnumDto {
    @SerialName("operationType")
    OPERATION_TYPE,
    @SerialName("timestamp")
    TIMESTAMP
}

@Serializable
data class FittingHistoryFiltersRequestDto(
    val pairedUserId: String? = null,
    val filters: List<JsonElement> = emptyList()
)

@Serializable
data class FittingHistoryFiltersResponseDto(
    val quantity: Int? = null,
    val filters: List<JsonElement>? = null
)

@Serializable
data class FittingHistoryFiltersResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FittingHistoryFiltersResponseDto? = null
)

@Serializable
data class FittingHistoryOperationInfoDto(
    val operationType: String? = null,
    val operationTypeColorHex: String? = null,
    val timestamp: String? = null
)

@Serializable
data class FittingHistoryOperationTypeFilterDto(
    val filterType: FittingHistoryFilterTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterStringValueDto> = emptyList()
)

@Serializable
data class FittingHistoryRequestDto(
    val pairedUserId: String? = null,
    val filters: List<JsonElement> = emptyList()
)

@Serializable
data class FittingHistoryResponseDto(
    val items: List<FittingHistoryResponseItemDto>? = null
)

@Serializable
data class FittingHistoryResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FittingHistoryResponseDto? = null
)

@Serializable
data class FittingHistoryResponseItemDto(
    val operationInfo: FittingHistoryOperationInfoDto? = null,
    val product: CatalogProductSearchCardDto? = null
)

@Serializable
data class FittingHistoryTimestampFilterDto(
    val filterType: FittingHistoryFilterTypeEnumDto? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterDateTimeRangeValueDto> = emptyList()
)

@Serializable
data class FittingLineControlsResponseDto(
    val isChangeTagAvailable: Boolean? = null
)

@Serializable
data class FittingLineResponseDto(
    val lineId: String? = null,
    val order: Int? = null,
    val paySwitch: Boolean? = null,
    val dateOfExpiration: String? = null,
    val dateOfExpirationBadgeColorHex: String? = null,
    val logisticStatus: FittingLogisticStatusDto? = null,
    val logisticStatusAsStringClient: String? = null,
    val logisticStatusAsStringEmployee: String? = null,
    val logisticStatusRejectReason: String? = null,
    val dateReceiptAsString: String? = null,
    val dateReceiptExpiredStatus: DateReceiptExpiredStatusDto? = null,
    val barcode: String? = null,
    val locationId: String? = null,
    val locationAsString: String? = null,
    val tag: String? = null,
    val product: CatalogProductSearchCardDto? = null,
    val controls: FittingLineControlsResponseDto? = null
)

@Serializable
enum class FittingLogisticStatusDto {
    @SerialName("created")
    CREATED,
    @SerialName("approved")
    APPROVED,
    @SerialName("kitting")
    KITTING,
    @SerialName("readyToShipToCustomer")
    READY_TO_SHIP_TO_CUSTOMER,
    @SerialName("shippingToCustomer")
    SHIPPING_TO_CUSTOMER,
    @SerialName("atCustomer")
    AT_CUSTOMER,
    @SerialName("returningFromCustomer")
    RETURNING_FROM_CUSTOMER,
    @SerialName("inTheStore")
    IN_THE_STORE,
    @SerialName("notAvailable")
    NOT_AVAILABLE,
    @SerialName("refused")
    REFUSED
}

@Serializable
data class FittingMergeLineIntoDeliveryOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val deliveryId: String? = null,
    val lineId: String? = null
)

@Serializable
enum class FittingOperationInitiatorDtoEnum {
    @SerialName("client")
    CLIENT,
    @SerialName("employee")
    EMPLOYEE,
    @SerialName("system")
    SYSTEM,
    @SerialName("axapta")
    AXAPTA
}

@Serializable
data class FittingOperationRequestDto(
    val pairedUserId: String? = null,
    val items: List<JsonElement> = emptyList()
)

@Serializable
data class FittingOperationRequestItemDtoBase(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null
)

@Serializable
enum class FittingOperationRequestTypeDto {
    @SerialName("addLine")
    ADD_LINE,
    @SerialName("changeLinePaySwitch")
    CHANGE_LINE_PAY_SWITCH,
    @SerialName("returnProduct")
    RETURN_PRODUCT,
    @SerialName("changeLineSize")
    CHANGE_LINE_SIZE,
    @SerialName("changeLineColor")
    CHANGE_LINE_COLOR,
    @SerialName("addDayToExpirationDate")
    ADD_DAY_TO_EXPIRATION_DATE,
    @SerialName("changeDelivery")
    CHANGE_DELIVERY,
    @SerialName("changeLineDelivery")
    CHANGE_LINE_DELIVERY,
    @SerialName("confirmDelivery")
    CONFIRM_DELIVERY,
    @SerialName("mergeLineIntoDelivery")
    MERGE_LINE_INTO_DELIVERY,
    @SerialName("returnProductToFitting")
    RETURN_PRODUCT_TO_FITTING,
    @SerialName("changeLineTag")
    CHANGE_LINE_TAG
}

@Serializable
data class FittingResponseDto(
    val fittingNumber: String? = null,
    val id: String? = null,
    val axaptaId: String? = null,
    val initiator: FittingOperationInitiatorDtoEnum? = null,
    val deliveries: List<FittingDeliveryResponseDto>? = null,
    val returningProducts: List<FittingReturningProductResponseDto>? = null,
    val catalogActionDisclaimer: String? = null,
    val timestamp: String? = null,
    val version: Int? = null
)

@Serializable
data class FittingResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: FittingResponseDto? = null
)

@Serializable
data class FittingReturnProductOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val lineId: String? = null
)

@Serializable
data class FittingReturnProductToFittingOperationRequestItemDto(
    val operationType: FittingOperationRequestTypeDto? = null,
    val operationOrder: Int? = null,
    val productId: String? = null
)

@Serializable
data class FittingReturningProductResponseDto(
    val order: Int? = null,
    val product: CatalogProductSearchCardDto? = null,
    val productId: String? = null,
    val quantity: Int? = null
)

@Serializable
data class FittingSyncRequestDto(
    val allFittings: Boolean? = null
)

@Serializable
enum class FittingType {
    @SerialName("none")
    NONE,
    @SerialName("inTheStore")
    IN_THE_STORE,
    @SerialName("atHome")
    AT_HOME
}

@Serializable
enum class FittingTypeDtoEnum {
    @SerialName("inTheStore")
    IN_THE_STORE,
    @SerialName("atHome")
    AT_HOME
}

@Serializable
data class GenderDto(
    val gender: GenderEnum? = null
)

@Serializable
data class GenderDtoResponseDto(
    val error: ErrorDto? = null,
    val data: GenderDto? = null
)

@Serializable
data class GetCheckOutFlagsControlResponseDto(
    val leaveItemsToClientCaption: String? = null,
    val isDeliverySettingsAvailable: Boolean? = null,
    val isKittingSettingsAvailable: Boolean? = null,
    val isKittingByEmployeeAvailable: Boolean? = null,
    val isDeliveryByEmployeeAvailable: Boolean? = null,
    val isLeaveItemsToClientAvailable: Boolean? = null,
    val isClientInHouseSwitchAvailable: Boolean? = null,
    val isVipShopperManualPickupAvailable: Boolean? = null
)

@Serializable
data class GetCheckOutFlagsForExistingFittingRequestDto(
    val pairedUserId: String? = null,
    val deliveryId: String? = null,
    val address: String? = null,
    val comment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null
)

@Serializable
data class GetCheckOutFlagsForSingleProductRequestDto(
    val pairedUserId: String? = null,
    val address: String? = null,
    val comment: String? = null,
    val itemId: String? = null,
    val colorId: String? = null,
    val sizeId: String? = null,
    val quantity: Int? = null,
    val fittingType: FittingTypeDtoEnum? = null
)

@Serializable
data class GetCheckOutFlagsRequestDto(
    val pairedUserId: String? = null,
    val basketLineIds: List<String>? = null,
    val address: String? = null,
    val comment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null
)

@Serializable
data class GetCheckOutFlagsResponseDto(
    val kittingSettings: CheckOutKittingSettingsDto? = null,
    val deliverySettings: CheckOutDeliverySettingsDto? = null,
    val controls: GetCheckOutFlagsControlResponseDto? = null
)

@Serializable
data class GetCheckOutFlagsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: GetCheckOutFlagsResponseDto? = null
)

@Serializable
data class GetDeliveryIntervalsForExistingFittingRequestDto(
    val pairedUserId: String? = null,
    val deliveryId: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val kittingType: KittingTypeDto? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val address: String? = null,
    val addressComment: String? = null
)

@Serializable
data class GetDeliveryIntervalsForExistingFittingResponseDto(
    val deliveryTimes: List<DeliveryTimeDto>? = null
)

@Serializable
data class GetDeliveryIntervalsForExistingFittingResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: GetDeliveryIntervalsForExistingFittingResponseDto? = null
)

@Serializable
data class GetDeliveryIntervalsForExistingOrderRequestDto(
    val pairedUserId: String? = null,
    val deliveryId: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val address: String? = null,
    val addressComment: String? = null
)

@Serializable
data class GetDeliveryIntervalsForExistingOrderResponseDto(
    val deliveryTimes: List<DeliveryTimeDto>? = null
)

@Serializable
data class GetDeliveryIntervalsForExistingOrderResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: GetDeliveryIntervalsForExistingOrderResponseDto? = null
)

@Serializable
data class GetEmployeeFittingLimitsForSingleProductRequestDto(
    val itemId: String? = null,
    val colorId: String? = null,
    val sizeId: String? = null,
    val kittingType: KittingTypeDto? = null
)

@Serializable
data class GetEmployeeFittingLimitsRequestDto(
    val clientId: String? = null,
    val basketLineIds: List<String>? = null,
    val kittingType: KittingTypeDto? = null
)

@Serializable
data class GetListClientAddressForCheckoutRequestDto(
    val pairedUserId: String? = null
)

@Serializable
data class GetListClientAddressRequestDto(
    val clientId: String? = null
)

@Serializable
data class GiftCardResponseDto(
    val items: List<GiftCardResponseItemDto>? = null
)

@Serializable
data class GiftCardResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: GiftCardResponseDto? = null
)

@Serializable
data class GiftCardResponseItemDto(
    val id: Int? = null,
    val itemId: String? = null,
    val type: GiftCardTypeDto? = null,
    val maxAmount: Double? = null,
    val minAmount: Double? = null,
    val defaultAmount: Double? = null,
    val presetAmounts: List<Double>? = null,
    val templates: List<GiftCardTemplateResponseItemDto>? = null
)

@Serializable
data class GiftCardTemplateResponseItemDto(
    val id: Int? = null,
    val templateId: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val photoUrl: String? = null,
    val termOfUse: String? = null,
    val orderView: Int? = null
)

@Serializable
enum class GiftCardTypeDto {
    @SerialName("none")
    NONE,
    @SerialName("virtual")
    VIRTUAL,
    @SerialName("physical")
    PHYSICAL
}

@Serializable
data class GradeDto(
    val gradeCurrent: Double? = null,
    val grade2GoalAmount: Double? = null,
    val grade3GoalAmount: Double? = null,
    val grade4GoalAmount: Double? = null,
    val grade5GoalAmount: Double? = null
)

@Serializable
enum class ImageCategoryEnum {
    @SerialName("looks")
    LOOKS,
    @SerialName("imagesearch")
    IMAGESEARCH,
    @SerialName("messenger")
    MESSENGER,
    @SerialName("compilations")
    COMPILATIONS
}

@Serializable
data class ImageSearchRequestDto(
    val imageUrl: String? = null
)

@Serializable
data class ImageSearchResponseDto(
    val items: List<CatalogProductSearchCardDto>? = null
)

@Serializable
data class ImageSearchResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ImageSearchResponseDto? = null
)

@Serializable
data class ImageUploadResponseDto(
    val imageUrl: String? = null,
    val previewUrl: String? = null
)

@Serializable
data class ImagesPayloadDto(
    val images: List<ImagesPayloadItemDto> = emptyList(),
    val text: String? = null,
    val title: String? = null,
    val citation: String? = null,
    val citatedMessageId: Int? = null,
    val type: PayloadType? = null
)

@Serializable
data class ImagesPayloadItemDto(
    val imageUrl: String? = null,
    val previewUrl: String? = null
)

@Serializable
data class ImagesUploadResponseDto(
    val items: List<ImageUploadResponseDto>? = null
)

@Serializable
data class ImagesUploadResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ImagesUploadResponseDto? = null
)

@Serializable
data class ImportResultDto(
    val productId: String? = null,
    val plu: String? = null,
    val colorId: String? = null,
    val availCatalog: Boolean? = null,
    val availQuantity: Boolean? = null,
    val fashionImageId: Int? = null
)

@Serializable
data class Int32NullableResponseDto(
    val error: ErrorDto? = null,
    val data: Int? = null
)

@Serializable
enum class KittingTypeDto {
    @SerialName("logistic")
    LOGISTIC,
    @SerialName("employee")
    EMPLOYEE
}

@Serializable
data class LinkCardRequestDto(
    val loyaltyCardNumber: String? = null
)

@Serializable
data class LoginRequestDto(
    val phone: String? = null
)

@Serializable
data class LookByClientsResponseItemDto(
    val id: String? = null,
    val name: String? = null,
    val surName: String? = null,
    val phone: String? = null,
    val lastActivityDate: String? = null,
    val statistics: StatisticsResponseBaseDto? = null
)

@Serializable
data class LookByProductsResponseItemDto(
    val searchCard: CatalogProductSearchCardDto? = null,
    val statistics: StatisticsResponseBaseDto? = null
)

@Serializable
data class LookItemDto(
    val id: Int? = null,
    val imageUrl: String? = null,
    val orderBy: Int? = null,
    val title: String? = null,
    val category: CatalogItemTypeEnum? = null
)

@Serializable
data class LookProductsResponseDto(
    val lookInfo: LooksResponseItemDto? = null,
    val products: List<CatalogProductSearchCardDto>? = null
)

@Serializable
data class LookProductsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: LookProductsResponseDto? = null
)

@Serializable
data class LookProductsResponseItemDto(
    val searchCard: CatalogProductSearchCardDto? = null,
    val lastStatus: ProductAction? = null
)

@Serializable
data class LookProductsWithStatsByClientResponseDto(
    val compilationName: String? = null,
    val lookInfo: LooksWithStatsResponseItemDto? = null,
    val products: List<LookProductsResponseItemDto>? = null
)

@Serializable
data class LookProductsWithStatsByClientResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: LookProductsWithStatsByClientResponseDto? = null
)

@Serializable
data class LookProductsWithStatsResponseDto(
    val lookInfo: LooksWithStatsResponseItemDto? = null,
    val products: List<LookByProductsResponseItemDto>? = null,
    val clients: List<LookByClientsResponseItemDto>? = null
)

@Serializable
data class LookProductsWithStatsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: LookProductsWithStatsResponseDto? = null
)

@Serializable
data class LooksResponseDto(
    val compilationInfo: CompilationsResponseItemDto? = null,
    val looks: List<LooksResponseItemDto>? = null
)

@Serializable
data class LooksResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: LooksResponseDto? = null
)

@Serializable
data class LooksResponseItemDto(
    val id: Int? = null,
    val collageUrl: String? = null,
    val photoUrl: String? = null,
    val meta: JsonElement? = null,
    val name: String? = null,
    val createDate: String? = null,
    val lookProductsQty: Int? = null,
    val isStatsAvailable: Boolean? = null
)

@Serializable
data class LooksResponseItemDtoItemsDto(
    val items: List<LooksResponseItemDto>? = null
)

@Serializable
data class LooksResponseItemDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: LooksResponseItemDtoItemsDto? = null
)

@Serializable
data class LooksResponseItemDtoResponseDto(
    val error: ErrorDto? = null,
    val data: LooksResponseItemDto? = null
)

@Serializable
data class LooksWithStatsResponseDto(
    val compilationInfo: CompilationsWithStatsResponseItemDto? = null,
    val looks: List<LooksWithStatsResponseItemDto>? = null
)

@Serializable
data class LooksWithStatsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: LooksWithStatsResponseDto? = null
)

@Serializable
data class LooksWithStatsResponseItemDto(
    val statistics: StatisticsResponseBaseDto? = null,
    val id: Int? = null,
    val collageUrl: String? = null,
    val photoUrl: String? = null,
    val meta: JsonElement? = null,
    val name: String? = null,
    val createDate: String? = null,
    val lookProductsQty: Int? = null,
    val isStatsAvailable: Boolean? = null
)

@Serializable
data class MainDeliveryResponseDto(
    val boutiques: List<BoutiqueDto>? = null,
    val buttonTitle: String? = null,
    val callCenterEmail: String? = null,
    val callCenterPhone: String? = null,
    val catalogueText: String? = null,
    val header: String? = null,
    val subheader: String? = null,
    val urlRegulations: String? = null,
    val vipPhone: String? = null
)

@Serializable
data class MainDeliveryResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MainDeliveryResponseDto? = null
)

@Serializable
data class MainScreenAnalyticsReportRequestDto(
    val dateFrom: String? = null,
    val dateTo: String? = null,
    val isEmployee: Boolean? = null
)

@Serializable
data class MessageGetRequestDto(
    val pairedUserId: String? = null,
    val fromMessageId: Long? = null,
    val limit: Int? = null,
    val toBackward: Boolean? = null
)

@Serializable
data class MessageReadDto(
    val isRead: Boolean? = null,
    val isReceived: Boolean? = null,
    val isDeleted: Boolean? = null,
    val isEdited: Boolean? = null,
    val text: String? = null,
    val messageId: Long? = null
)

@Serializable
data class MessageReadGetDto(
    val pairedUserId: String? = null,
    val revision: String? = null
)

@Serializable
data class MessageReadResponseDto(
    val items: List<MessageReadDto>? = null,
    val revision: String? = null
)

@Serializable
data class MessageReadResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MessageReadResponseDto? = null
)

@Serializable
enum class MessageReadType {
    @SerialName("none")
    NONE,
    @SerialName("received")
    RECEIVED,
    @SerialName("read")
    READ
}

@Serializable
data class MessageResponseDto(
    val clientId: String? = null,
    val createTime: String? = null,
    val employeeId: String? = null,
    val localCreateTime: String? = null,
    val localMessageId: String? = null,
    val payload: PayloadDtoBase? = null,
    val showBadge: Boolean? = null,
    val showChecks: Boolean? = null,
    val type: MessageTypeEnum? = null,
    val isRead: Boolean? = null,
    val isReceived: Boolean? = null,
    val isDeleted: Boolean? = null,
    val isEdited: Boolean? = null,
    val text: String? = null,
    val messageId: Long? = null
)

@Serializable
data class MessageResponseDtoItemsDto(
    val items: List<MessageResponseDto>? = null
)

@Serializable
data class MessageResponseDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MessageResponseDtoItemsDto? = null
)

@Serializable
data class MessageResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MessageResponseDto? = null
)

typealias MessageType = Int

@Serializable
enum class MessageTypeEnum {
    @SerialName("none")
    NONE,
    @SerialName("client")
    CLIENT,
    @SerialName("employee")
    EMPLOYEE,
    @SerialName("system")
    SYSTEM
}

@Serializable
data class MessengerActivityReportRequestDto(
    val dateFrom: String? = null,
    val dateTo: String? = null
)

@Serializable
data class MessengerActivityReportResponseDto(
    val items: List<MessengerActivityReportResponseItemDto>? = null
)

@Serializable
data class MessengerActivityReportResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MessengerActivityReportResponseDto? = null
)

@Serializable
data class MessengerActivityReportResponseItemDto(
    val creationDate: String? = null,
    val clientId: String? = null,
    val employeeId: String? = null,
    val message: String? = null,
    val sender: MessageType? = null,
    val isRead: Boolean? = null,
    val messageId: Long? = null,
    val readTime: String? = null
)

@Serializable
data class MotivationRequestDto(
    val hrId: Int? = null,
    val date: String? = null
)

@Serializable
data class MotivationResponseDto(
    val personal: PersonalSalesDto? = null,
    val common: CommonSalesDto? = null,
    val byBrand: SalesByBrandDto? = null,
    val perMonth: SalesPerMonthDto? = null,
    val grade: GradeDto? = null
)

@Serializable
data class MotivationResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MotivationResponseDto? = null
)

@Serializable
data class MyClientsBasketFilterDto(
    val filterType: MyClientsFilterType? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterStringValueDto> = emptyList()
)

@Serializable
data class MyClientsChatFilterDto(
    val filterType: MyClientsFilterType? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterStringValueDto> = emptyList()
)

@Serializable
data class MyClientsClientBadgesResponseDto(
    val clientId: String? = null,
    val basketIcon: MyClientsIconWithBadgeResponse? = null,
    val fittingIcon: MyClientsIconWithBadgeResponse? = null,
    val messengerIcon: MyClientsIconWithBadgeResponse? = null,
    val orderIcon: MyClientsIconWithBadgeResponse? = null,
    val compilationIcon: MyClientsIconWithBadgeResponse? = null
)

@Serializable
data class MyClientsClientBadgesResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MyClientsClientBadgesResponseDto? = null
)

@Serializable
data class MyClientsClientLastActivityResponse(
    val colorHex: String? = null,
    val date: String? = null
)

@Serializable
data class MyClientsClientResponseDto(
    val clientEmail: String? = null,
    val clientId: String? = null,
    val clientMiddleName: String? = null,
    val clientName: String? = null,
    val clientPhone: String? = null,
    val clientSurname: String? = null,
    val clientClass: ClientClassEnumDto? = null,
    val isRegistered: Boolean? = null,
    val lastActivity: MyClientsClientLastActivityResponse? = null,
    val isAvailableFittingHome: Boolean? = null,
    val isReadyToConnect: Boolean? = null
)

@Serializable
data class MyClientsCompilationFilterDto(
    val filterType: MyClientsFilterType? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterStringValueDto> = emptyList()
)

@Serializable
data class MyClientsFilterDtoBase(
    val filterType: MyClientsFilterType? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null
)

@Serializable
enum class MyClientsFilterType {
    @SerialName("fitting")
    FITTING,
    @SerialName("basket")
    BASKET,
    @SerialName("order")
    ORDER,
    @SerialName("chat")
    CHAT,
    @SerialName("compilation")
    COMPILATION
}

@Serializable
data class MyClientsFiltersResponseDto(
    val filters: List<JsonElement>? = null
)

@Serializable
data class MyClientsFiltersResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MyClientsFiltersResponseDto? = null
)

@Serializable
data class MyClientsFittingFilterDto(
    val filterType: MyClientsFilterType? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterStringValueDto> = emptyList()
)

@Serializable
data class MyClientsIconResponse(
    val colorHex: String? = null,
    val isActive: Boolean? = null,
    val number: Int? = null,
    val secondNumber: Int? = null,
    val type: MyClientsIconTypeResponse? = null
)

@Serializable
enum class MyClientsIconTypeResponse {
    @SerialName("basket")
    BASKET,
    @SerialName("order")
    ORDER,
    @SerialName("fitting")
    FITTING,
    @SerialName("messenger")
    MESSENGER,
    @SerialName("compilation")
    COMPILATION
}

@Serializable
data class MyClientsIconWithBadgeResponse(
    val badge: Int? = null,
    val icon: MyClientsIconResponse? = null
)

@Serializable
data class MyClientsOrderFilterDto(
    val filterType: MyClientsFilterType? = null,
    val label: String? = null,
    val labelPhotoUrl: String? = null,
    val isMultiSelect: Boolean? = null,
    val ribbonSettings: FilterRibbonSettingsDto? = null,
    val values: List<FilterStringValueDto> = emptyList()
)

@Serializable
data class MyClientsRequestDto(
    val filters: List<JsonElement> = emptyList()
)

@Serializable
data class MyClientsResponseDto(
    val items: List<MyClientsClientResponseDto>? = null
)

@Serializable
data class MyClientsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MyClientsResponseDto? = null
)

@Serializable
data class MyEmployeesEmployeeBadgesResponseDto(
    val employeeId: String? = null,
    val basketIcon: MyEmployeesIconWithBadgeResponse? = null,
    val fittingIcon: MyEmployeesIconWithBadgeResponse? = null,
    val messengerIcon: MyEmployeesIconWithBadgeResponse? = null,
    val orderIcon: MyEmployeesIconWithBadgeResponse? = null,
    val compilationIcon: MyEmployeesIconWithBadgeResponse? = null
)

@Serializable
data class MyEmployeesEmployeeBadgesResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MyEmployeesEmployeeBadgesResponseDto? = null
)

@Serializable
data class MyEmployeesEmployeeLastActivityResponse(
    val colorHex: String? = null,
    val date: String? = null
)

@Serializable
data class MyEmployeesEmployeeResponseDto(
    val employeeEmail: String? = null,
    val employeeId: String? = null,
    val employeeMiddleName: String? = null,
    val employeeName: String? = null,
    val employeePhone: String? = null,
    val employeeSurname: String? = null,
    val photoUrl: String? = null,
    val previewPhotoUrl: String? = null,
    val lastActivity: MyEmployeesEmployeeLastActivityResponse? = null,
    val employeeBotiqueAddress: String? = null,
    val employeeBotiqueAddressShort: String? = null,
    val employeeBrand: String? = null
)

@Serializable
data class MyEmployeesEmployeeResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MyEmployeesEmployeeResponseDto? = null
)

@Serializable
data class MyEmployeesIconResponse(
    val colorHex: String? = null,
    val isActive: Boolean? = null,
    val number: Int? = null,
    val type: MyEmployeesIconTypeResponse? = null
)

@Serializable
enum class MyEmployeesIconTypeResponse {
    @SerialName("basket")
    BASKET,
    @SerialName("order")
    ORDER,
    @SerialName("fitting")
    FITTING,
    @SerialName("messenger")
    MESSENGER,
    @SerialName("compilation")
    COMPILATION
}

@Serializable
data class MyEmployeesIconWithBadgeResponse(
    val badge: Int? = null,
    val icon: MyEmployeesIconResponse? = null
)

@Serializable
data class MyEmployeesResponseDto(
    val items: List<MyEmployeesEmployeeResponseDto>? = null
)

@Serializable
data class MyEmployeesResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: MyEmployeesResponseDto? = null
)

@Serializable
data class NewArrivalsPushRequestDto(
    val pushMessage: String? = null,
    val pushTitle: String? = null,
    val clientIds: List<String>? = null,
    val employeeIds: List<String>? = null,
    val category: CatalogItemTypeEnum? = null
)

@Serializable
data class NewLooksResponseDto(
    val items: List<LookItemDto>? = null
)

@Serializable
data class NewLooksResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: NewLooksResponseDto? = null
)

@Serializable
data class OrderBonusReservationConfirmationRequestDto(
    val smsCode: String? = null
)

@Serializable
data class OrderBonusReservationRequestDto(
    val bonusAmount: Double? = null
)

@Serializable
data class OrderConfirmPaymentViaCloudPaymentRequestDto(
    val transactionId: String? = null
)

@Serializable
data class OrderCreationFromFittingRequestDto(
    val pairedUserId: String? = null,
    val deliveryIds: List<String> = emptyList(),
    val paymentType: PaymentType? = null
)

@Serializable
data class OrderCreationRequestDto(
    val pairedUserId: String? = null,
    val paymentType: PaymentType? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null
)

@Serializable
data class OrderDeliveryChangeRequestDto(
    val pairedUserId: String? = null,
    val deliveryId: String? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null
)

@Serializable
data class OrderDeliveryResponseControlsDto(
    val isEditDeliveryAvailable: Boolean? = null
)

@Serializable
data class OrderDeliveryResponseDto(
    val deliveryId: String? = null,
    val deliveryTime: DeliveryTimeDto? = null,
    val address: OrderResponseAddressDto? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null,
    val products: List<OrderProductResponseDto>? = null,
    val controls: OrderDeliveryResponseControlsDto? = null
)

@Serializable
data class OrderExternalPushRequestDto(
    val items: List<OrderExternalPushRequestItemDto>? = null
)

@Serializable
data class OrderExternalPushRequestItemDto(
    val orderId: String? = null,
    val phone: String? = null,
    val pushMessage: String? = null,
    val pushTitle: String? = null,
    val messageId: String? = null,
    val clientPhone: String? = null,
    val pushReceiver: ExternalPushReceiverEnumDto? = null
)

@Serializable
data class OrderExternalPushResponseItemDto(
    val messageId: String? = null,
    val status: ExternalPushStatusEnumDto? = null,
    val errorMessage: String? = null
)

@Serializable
data class OrderExternalPushResponseItemDtoItemsDto(
    val items: List<OrderExternalPushResponseItemDto>? = null
)

@Serializable
data class OrderExternalPushResponseItemDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: OrderExternalPushResponseItemDtoItemsDto? = null
)

@Serializable
enum class OrderLogisticStatusRequestEnum {
    @SerialName("created")
    CREATED,
    @SerialName("approved")
    APPROVED,
    @SerialName("kitting")
    KITTING,
    @SerialName("readyToShipToCustomer")
    READY_TO_SHIP_TO_CUSTOMER,
    @SerialName("shippingToCustomer")
    SHIPPING_TO_CUSTOMER,
    @SerialName("atCustomer")
    AT_CUSTOMER,
    @SerialName("sold")
    SOLD,
    @SerialName("notSold")
    NOT_SOLD,
    @SerialName("notAvailable")
    NOT_AVAILABLE,
    @SerialName("inTheStore")
    IN_THE_STORE
}

@Serializable
data class OrderPaymentLinkResponseDto(
    val urlPayment: String? = null
)

@Serializable
data class OrderPaymentLinkResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: OrderPaymentLinkResponseDto? = null
)

@Serializable
enum class OrderPaymentStatusEnumDto {
    @SerialName("notPaid")
    NOT_PAID,
    @SerialName("bonusReservationStarted")
    BONUS_RESERVATION_STARTED,
    @SerialName("bonusReservationFinished")
    BONUS_RESERVATION_FINISHED,
    @SerialName("paymentStarted")
    PAYMENT_STARTED,
    @SerialName("paymentFinished")
    PAYMENT_FINISHED,
    @SerialName("paymentForbidden")
    PAYMENT_FORBIDDEN
}

@Serializable
data class OrderProductResponseDto(
    val lineId: String? = null,
    val logisticStatus: OrderLogisticStatusRequestEnum? = null,
    val logisticStatusAsStringForClient: String? = null,
    val logisticStatusAsStringForEmployee: String? = null,
    val price: Double? = null,
    val product: CatalogProductSearchCardDto? = null,
    val dateReceiptAsString: String? = null,
    val dateReceiptExpiredStatus: DateReceiptExpiredStatusDto? = null,
    val giftCardTemplateId: Int? = null,
    val giftCardEmailReceiver: String? = null,
    val giftCardPhoneReceiver: String? = null,
    val giftCardSendDateTime: String? = null,
    val isOrderVirtualGiftCard: Boolean? = null
)

@Serializable
data class OrderResponseAddressDto(
    val address: String? = null,
    val comment: String? = null,
    val fittingType: FittingType? = null
)

@Serializable
data class OrderResponseControlsDto(
    val isOnlinePayAvailable: Boolean? = null
)

@Serializable
data class OrderResponseDto(
    val clientEmail: String? = null,
    val clientId: String? = null,
    val clientName: String? = null,
    val clientPhone: String? = null,
    val clientSurname: String? = null,
    val creationDate: String? = null,
    val id: String? = null,
    val isFinished: Boolean? = null,
    val isDelivered: Boolean? = null,
    val paymentStatus: OrderPaymentStatusEnumDto? = null,
    val paymentStatusAsString: String? = null,
    val reservedBonusAmount: Double? = null,
    val bonusAmount: Double? = null,
    val isFinishedAsString: String? = null,
    val order: Int? = null,
    val orderNumber: String? = null,
    val paymentType: PaymentTypeRequestEnum? = null,
    val deliveries: List<OrderDeliveryResponseDto>? = null,
    val totalPrice: Double? = null,
    val controls: OrderResponseControlsDto? = null,
    val isOrderWithGiftCard: Boolean? = null
)

@Serializable
data class OrderResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: OrderResponseDto? = null
)

@Serializable
data class OrderResponseWithBadgeDto(
    val badge: Int? = null,
    val order: OrderResponseDto? = null
)

@Serializable
data class OrderResponseWithBadgeDtoItemsDto(
    val items: List<OrderResponseWithBadgeDto>? = null
)

@Serializable
data class OrderResponseWithBadgeDtoItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: OrderResponseWithBadgeDtoItemsDto? = null
)

@Serializable
data class OrderResponseWithBadgeDtoResponseDto(
    val error: ErrorDto? = null,
    val data: OrderResponseWithBadgeDto? = null
)

@Serializable
data class OrderStartPaymentViaCloudPaymentRequestDto(
    val transactionId: String? = null
)

@Serializable
data class OrderStartPaymentViaSbpResponseDto(
    val qrCodeUrl: String? = null
)

@Serializable
data class OrderStartPaymentViaSbpResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: OrderStartPaymentViaSbpResponseDto? = null
)

@Serializable
data class PaletteFoldersResponseDto(
    val items: List<PaletteFoldersResponseItemDto>? = null
)

@Serializable
data class PaletteFoldersResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: PaletteFoldersResponseDto? = null
)

@Serializable
data class PaletteFoldersResponseItemDto(
    val id: Int? = null,
    val name: String? = null,
    val photoUrl: String? = null,
    val createDate: String? = null,
    val productsQty: Int? = null
)

@Serializable
data class PaletteProductRequestItemDto(
    val colorId: String? = null,
    val itemId: String? = null
)

@Serializable
data class PaletteProductsResponseDto(
    val items: List<CatalogProductSearchCardDto>? = null
)

@Serializable
data class PaletteProductsResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: PaletteProductsResponseDto? = null
)

@Serializable
data class PayloadDtoBase(
    val text: String? = null,
    val title: String? = null,
    val citation: String? = null,
    val citatedMessageId: Int? = null,
    val type: PayloadType? = null
)

@Serializable
enum class PayloadType {
    @SerialName("product")
    PRODUCT,
    @SerialName("compilationLook")
    COMPILATION_LOOK,
    @SerialName("clientCompilation")
    CLIENT_COMPILATION,
    @SerialName("basketLook")
    BASKET_LOOK,
    @SerialName("images")
    IMAGES,
    @SerialName("giftCard")
    GIFT_CARD
}

@Serializable
data class PaymentAcknowledgeRequestDto(
    val setId: String? = null,
    val paymentType: String? = null,
    val orderAcq: String? = null,
    val bonusAmount: Double? = null,
    val discountAmount: Double? = null,
    val cashAmount: Double? = null,
    val bonusType: String? = null,
    val rrn: String? = null,
    val paymentCardType: String? = null
)

@Serializable
data class PaymentReconciliationRequestDto(
    val dateFrom: String? = null,
    val dateTo: String? = null
)

@Serializable
data class PaymentReconciliationResponseDto(
    val items: List<PaymentReconciliationResponseItemDto>? = null
)

@Serializable
data class PaymentReconciliationResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: PaymentReconciliationResponseDto? = null
)

@Serializable
data class PaymentReconciliationResponseItemDto(
    val acquiringOrderCode: String? = null,
    val transactionDate: String? = null,
    val paymentType: String? = null,
    val paymentSource: PaymentTypeDto? = null,
    val orderNumber: String? = null,
    val orderAmount: Double? = null
)

@Serializable
enum class PaymentType {
    @SerialName("none")
    NONE,
    @SerialName("cashlessOnline")
    CASHLESS_ONLINE,
    @SerialName("cashlessCourier")
    CASHLESS_COURIER,
    @SerialName("cashCourier")
    CASH_COURIER,
    @SerialName("other")
    OTHER,
    @SerialName("cashlessOnlineUrl")
    CASHLESS_ONLINE_URL,
    @SerialName("onlinePaymentClient")
    ONLINE_PAYMENT_CLIENT
}

typealias PaymentTypeDto = Int

@Serializable
enum class PaymentTypeRequestEnum {
    @SerialName("cashlessOnline")
    CASHLESS_ONLINE,
    @SerialName("cashlessCourier")
    CASHLESS_COURIER,
    @SerialName("cashCourier")
    CASH_COURIER,
    @SerialName("other")
    OTHER,
    @SerialName("cashlessOnlineUrl")
    CASHLESS_ONLINE_URL,
    @SerialName("onlinePaymentClient")
    ONLINE_PAYMENT_CLIENT
}

@Serializable
data class PersonalSalesDto(
    val weekCaption: String? = null,
    val weekPeriodCaption: String? = null,
    val connectedBrandSales: Double? = null,
    val otherBrandSales: Double? = null,
    val goalSalesAmount: Double? = null,
    val goalSalesAmountMinimum: Double? = null,
    val completedWeekPlansInARowQty: Int? = null,
    val connectedBrandSalesAmount: Double? = null,
    val connectedBrandSalesInWeekTotalSalesPercent: Double? = null,
    val otherBrandSalesAmount: Double? = null,
    val otherBrandSalesInWeekTotalSalesPercent: Double? = null,
    val totalSalesAmount: Double? = null,
    val leftForPlan: Double? = null,
    val personalSalesPayrollPartAmount: Double? = null,
    val quarterPayrollAmount: Double? = null,
    val personalSalesIncreasingCoefficient: Double? = null,
    val personalSalesGoalPercentCompletion: Double? = null,
    val personalSalesQuarter: Double? = null
)

@Serializable
enum class ProductAction {
    @SerialName("none")
    NONE,
    @SerialName("viewed")
    VIEWED,
    @SerialName("addedToBasket")
    ADDED_TO_BASKET,
    @SerialName("addedToFitting")
    ADDED_TO_FITTING,
    @SerialName("bought")
    BOUGHT
}

@Serializable
data class ProductPayloadDto(
    val orderNumber: String? = null,
    val products: List<ProductPayloadItemDto>? = null,
    val text: String? = null,
    val title: String? = null,
    val citation: String? = null,
    val citatedMessageId: Int? = null,
    val type: PayloadType? = null
)

@Serializable
data class ProductPayloadItemDto(
    val brand: String? = null,
    val colorId: String? = null,
    val colorName: String? = null,
    val id: String? = null,
    val imageUrl: String? = null,
    val itemId: String? = null,
    val name: String? = null,
    val price: Double? = null
)

@Serializable
data class PushTokenDto(
    val clientId: String? = null,
    val employeeId: String? = null,
    val token: String? = null
)

@Serializable
data class RegistrationRequestDto(
    val phone: String? = null,
    val name: String? = null
)

@Serializable
data class RemoveBackgroundRequestDto(
    val imageUrl: String? = null
)

@Serializable
data class RemoveBackgroundResponseDto(
    val status: RemoveBackgroundStatusEnum? = null,
    val previewImageUrl: String? = null,
    val resultImageUrl: String? = null
)

@Serializable
data class RemoveBackgroundResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: RemoveBackgroundResponseDto? = null
)

@Serializable
enum class RemoveBackgroundStatusEnum {
    @SerialName("alreadyRemoved")
    ALREADY_REMOVED,
    @SerialName("removalSuccessful")
    REMOVAL_SUCCESSFUL,
    @SerialName("removalRequiresUserConfirmation")
    REMOVAL_REQUIRES_USER_CONFIRMATION
}

@Serializable
data class ReserveRequestDto(
    val serialId: String? = null,
    val locationId: String? = null,
    val system: SystemEnumDto? = null
)

@Serializable
data class ReserveResponseDto(
    val reserveId: String? = null,
    val reserveReason: String? = null,
    val reserveStartDate: String? = null,
    val reserveEndDate: String? = null,
    val employee: String? = null,
    val employeePhone: String? = null,
    val fittingType: String? = null,
    val clientType: ClientClassEnumDto? = null
)

@Serializable
data class ReserveResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ReserveResponseDto? = null
)

@Serializable
data class ResetActivityCountersRequestDto(
    val type: ActivityCounterTypeRequestEnum? = null
)

@Serializable
data class ResponseDto(
    val error: ErrorDto? = null,
    val data: JsonElement? = null
)

@Serializable
data class SalesByBrandDto(
    val items: List<SalesByBrandItemDto>? = null
)

@Serializable
data class SalesByBrandItemDto(
    val brand: String? = null,
    val brandPercent: Double? = null,
    val amount: Double? = null,
    val personalSales: Double? = null
)

@Serializable
data class SalesPerMonthDto(
    val connectedBrandSalesAmount: Double? = null,
    val connectedBrandSalesInMonthTotalSalesPercent: Double? = null,
    val otherBrandSalesAmount: Double? = null,
    val otherBrandSalesInMonthTotalSalesPercent: Double? = null,
    val totalSalesAmount: Double? = null,
    val commonSalesPayrollPartAmount: Double? = null,
    val personalSalesPayrollPartAmount: Double? = null,
    val totalPayrollAmount: Double? = null
)

@Serializable
data class SaveClientCompilationMessageRequestDto(
    val pairedUserId: String? = null,
    val createTime: String? = null,
    val localCreateTime: String? = null,
    val localMessageId: String? = null,
    val payload: ClientCompilationPayloadDto? = null,
    val showBadge: Boolean? = null,
    val showChecks: Boolean? = null,
    val type: MessageTypeEnum? = null,
    val isRead: Boolean? = null,
    val isReceived: Boolean? = null,
    val isDeleted: Boolean? = null,
    val isEdited: Boolean? = null,
    val text: String? = null,
    val messageId: Long? = null
)

@Serializable
data class SaveCompilationLookMessageRequestDto(
    val pairedUserId: String? = null,
    val createTime: String? = null,
    val localCreateTime: String? = null,
    val localMessageId: String? = null,
    val payload: CompilationLookPayloadDto? = null,
    val showBadge: Boolean? = null,
    val showChecks: Boolean? = null,
    val type: MessageTypeEnum? = null,
    val isRead: Boolean? = null,
    val isReceived: Boolean? = null,
    val isDeleted: Boolean? = null,
    val isEdited: Boolean? = null,
    val text: String? = null,
    val messageId: Long? = null
)

@Serializable
data class SaveMessageWithImagesRequestDto(
    val pairedUserId: String? = null,
    val createTime: String? = null,
    val localCreateTime: String? = null,
    val localMessageId: String? = null,
    val payload: ImagesPayloadDto? = null,
    val showBadge: Boolean? = null,
    val showChecks: Boolean? = null,
    val type: MessageTypeEnum? = null,
    val isRead: Boolean? = null,
    val isReceived: Boolean? = null,
    val isDeleted: Boolean? = null,
    val isEdited: Boolean? = null,
    val text: String? = null,
    val messageId: Long? = null
)

@Serializable
data class SaveProductMessageRequestDto(
    val pairedUserId: String? = null,
    val createTime: String? = null,
    val localCreateTime: String? = null,
    val localMessageId: String? = null,
    val payload: ProductPayloadDto? = null,
    val showBadge: Boolean? = null,
    val showChecks: Boolean? = null,
    val type: MessageTypeEnum? = null,
    val isRead: Boolean? = null,
    val isReceived: Boolean? = null,
    val isDeleted: Boolean? = null,
    val isEdited: Boolean? = null,
    val text: String? = null,
    val messageId: Long? = null
)

@Serializable
data class SizeInProductSearchDto(
    val availableStockQuantity: Int? = null,
    val id: String? = null,
    val inOrder: Boolean? = null,
    val inStock: Boolean? = null,
    val inStockShops: List<String>? = null,
    val isFavorite: Boolean? = null,
    val isLastInStock: Boolean? = null,
    val name: String? = null,
    val sizeForFilter: String? = null,
    val onlyInVipSite: Boolean? = null,
    val onlyInTransit: Boolean? = null
)

@Serializable
data class SizeInProductSearchVNDto(
    val availableStockQuantity: Int? = null,
    val id: String? = null,
    val inOrder: Boolean? = null,
    val inStock: Boolean? = null,
    val inStockShops: List<String>? = null,
    val isFavorite: Boolean? = null,
    val isLastInStock: Boolean? = null,
    val name: String? = null
)

@Serializable
data class StatisticsResponseBaseDto(
    val viewsQty: Int? = null,
    val basketQty: Int? = null,
    val fittingQty: Int? = null,
    val boughtQty: Int? = null
)

@Serializable
data class StatisticsResponseDto(
    val openCompilation: Boolean? = null,
    val viewsQty: Int? = null,
    val basketQty: Int? = null,
    val fittingQty: Int? = null,
    val boughtQty: Int? = null
)

@Serializable
enum class StatisticsType {
    @SerialName("none")
    NONE,
    @SerialName("byProducts")
    BY_PRODUCTS,
    @SerialName("byClients")
    BY_CLIENTS
}

@Serializable
data class StatsByClientsResponseDto(
    val sentQty: Int? = null,
    val activeQty: Int? = null,
    val openedQty: Int? = null
)

@Serializable
data class StringItemsDto(
    val items: List<String>? = null
)

@Serializable
data class StringItemsDtoResponseDto(
    val error: ErrorDto? = null,
    val data: StringItemsDto? = null
)

@Serializable
data class StringResponseDto(
    val error: ErrorDto? = null,
    val data: String? = null
)

@Serializable
data class SuggestionResponseDto(
    val items: List<String>? = null,
    val correction: String? = null
)

@Serializable
data class SuggestionResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: SuggestionResponseDto? = null
)

@Serializable
data class SuggestsSearchRequest(
    val searchText: String? = null
)

@Serializable
enum class SystemEnumDto {
    @SerialName("tsum")
    TSUM,
    @SerialName("btq")
    BTQ
}

@Serializable
data class TasksImportResultDataDto(
    val employeeId: String? = null,
    val clientId: String? = null,
    val crmId: String? = null,
    val message: String? = null
)

@Serializable
data class TasksImportResultDto(
    val errors: List<TasksImportResultDataDto>? = null
)

@Serializable
data class TokenResponseDto(
    val token: String? = null
)

@Serializable
data class TokenResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: TokenResponseDto? = null
)

@Serializable
data class TransferBasketToFittingLineDto(
    val lineId: String? = null,
    val deliveryTime: DeliveryTimeDto? = null
)

@Serializable
data class TransferBasketToFittingRequestDto(
    val pairedUserId: String? = null,
    val lines: List<TransferBasketToFittingLineDto> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val address: String? = null,
    val addressComment: String? = null,
    val fittingType: FittingTypeDtoEnum? = null,
    val deliveryType: DeliveryTypeDto? = null,
    val kittingType: KittingTypeDto? = null,
    val leaveItemsToClient: Boolean? = null,
    val isGoWithCourier: Boolean? = null,
    val packOrderMyself: Boolean? = null,
    val manageClientCommunicationsMyself: Boolean? = null,
    val deliveryComment: String? = null,
    val isClientInHouse: Boolean? = null,
    val vipShopperManualPickup: Boolean? = null,
    val tag: String? = null
)

@Serializable
data class TransferRequestDto(
    val serialId: String? = null,
    val locationId: String? = null,
    val system: SystemEnumDto? = null
)

@Serializable
data class TransferResponseDto(
    val transferId: String? = null,
    val transferOrderStatus: String? = null,
    val transferType: String? = null,
    val employee: String? = null,
    val description: String? = null,
    val locationFrom: String? = null,
    val locationTo: String? = null,
    val statusHistory: List<TransferStatusHistoryItemDto>? = null
)

@Serializable
data class TransferResponseDtoResponseDto(
    val error: ErrorDto? = null,
    val data: TransferResponseDto? = null
)

@Serializable
data class TransferStatusHistoryItemDto(
    val time: String? = null,
    val status: String? = null,
    val courier: String? = null
)

@Serializable
data class UpdateClientAddressRequestDto(
    val clientId: String? = null,
    val address: ClientAddressDto? = null,
    val coordinate: CoordinateDto? = null
)

@Serializable
data class UpdateMainScreenRequestDto(
    val settingIds: List<Int>? = null
)

@Serializable
data class UpdateMessageDto(
    val pairedUserId: String? = null,
    val messageIds: List<Long>? = null,
    val readType: MessageReadType? = null
)

@Serializable
data class UpdateNoPhotoResultDto(
    val insertedProductsWithPhotos: Int? = null,
    val updatedDboProducts: Int? = null,
    val updatedCatalogProducts: Int? = null
)

@Serializable
data class UploadCollageRequestDto(
    val url: String? = null
)

@Serializable
data class VerifyLinkCardRequestDto(
    val loyaltyCardNumber: String? = null,
    val smsCode: String? = null
)

@Serializable
data class ViewHistoryDto(
    val items: List<ViewHistoryItemDto>? = null
)

@Serializable
data class ViewHistoryDtoResponseDto(
    val error: ErrorDto? = null,
    val data: ViewHistoryDto? = null
)

@Serializable
data class ViewHistoryItemDto(
    val product: CatalogProductSearchCardDto? = null,
    val timestamp: String? = null,
    val timestampAsAString: String? = null,
    val type: ViewHistoryItemTypeDtoEnum? = null
)

@Serializable
enum class ViewHistoryItemTypeDtoEnum {
    @SerialName("wasViewedViaDetailCard")
    WAS_VIEWED_VIA_DETAIL_CARD,
    @SerialName("wasInBasketBefore")
    WAS_IN_BASKET_BEFORE,
    @SerialName("isInBasketNow")
    IS_IN_BASKET_NOW
}

@Serializable
data class CompilationsEmployeeLookUploadCollagePostRequest(
    val file: ByteArray? = null
)

@Serializable
data class CompilationsEmployeeLookUploadPhotoPostRequest(
    val file: ByteArray? = null
)

@Serializable
data class CompilationsEmployeeUploadPhotoPostRequest(
    val file: ByteArray? = null
)

@Serializable
data class InternalExcelImportImportCollectionPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class InternalExcelImportAppendCollectionPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class InternalExcelImportImportNewArrivalsPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class InternalExcelImportImportLooksPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class InternalExcelImportImportMannequinsPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class InternalExcelImportImportSortSettingsPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class InternalExcelImportImportVpTasksPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class InternalExcelImportImportCrmTasksPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class InternalExcelImportUpdateNoPhotoPostRequest(
    val formFile: ByteArray? = null
)

@Serializable
data class ImagesUploadPostRequest(
    @SerialName("Files")
    val files: List<ByteArray>? = null,
    @SerialName("Category")
    val category: ImageCategoryEnum? = null,
    @SerialName("GeneratePreview")
    val generatePreview: Boolean? = null
)

@Serializable
data class UserProfileUploadPhotoPostRequest(
    val file: ByteArray? = null
)
