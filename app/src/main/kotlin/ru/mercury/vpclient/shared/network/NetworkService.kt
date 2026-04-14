package ru.mercury.vpclient.shared.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement
import ru.mercury.vpclient.shared.network.entity.ActionItemDto
import ru.mercury.vpclient.shared.network.entity.ActionPushRequestDto
import ru.mercury.vpclient.shared.network.entity.ActionsResponseDto
import ru.mercury.vpclient.shared.network.entity.ActiveClientResponseDto
import ru.mercury.vpclient.shared.network.entity.AddProductToActionRequestDto
import ru.mercury.vpclient.shared.network.entity.AddProductToFashionImageRequestDto
import ru.mercury.vpclient.shared.network.entity.AddProductsInFoldersRequestDto
import ru.mercury.vpclient.shared.network.entity.AddressSuggestionDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.AggregatedActivityCounterResponseDto
import ru.mercury.vpclient.shared.network.entity.ApproveFittingDeliveryManualPickupRequestDto
import ru.mercury.vpclient.shared.network.entity.ApproveFittingDeliveryManualPickupResponse
import ru.mercury.vpclient.shared.network.entity.ApproveFittingDeliveryRequestDto
import ru.mercury.vpclient.shared.network.entity.ApproveFittingDeliveryResponse
import ru.mercury.vpclient.shared.network.entity.ApproveFittingExpirationDateExtensionRequestDto
import ru.mercury.vpclient.shared.network.entity.ApproveFittingExpirationDateExtensionResponse
import ru.mercury.vpclient.shared.network.entity.AvailableColorsRequestDto
import ru.mercury.vpclient.shared.network.entity.AvailableColorsResponseDto
import ru.mercury.vpclient.shared.network.entity.AvailableLocationsForProductResponseDto
import ru.mercury.vpclient.shared.network.entity.AvailableLocationsRequestDto
import ru.mercury.vpclient.shared.network.entity.AvailableLocationsResponseDto
import ru.mercury.vpclient.shared.network.entity.AvailableSizesRequestDto
import ru.mercury.vpclient.shared.network.entity.AvailableSizesResponseDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardAuthRequestDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardAuthResponseDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardCalcAndReserveBonusPayRequestDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardCalcAndReserveBonusPayResponseDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardCalcBonusAmountRequestDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardCalcBonusAmountResponseDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardCheckPinRequestDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardCheckPinResponseDto
import ru.mercury.vpclient.shared.network.entity.AxLoyaltyCardRollBackBonusPayRequestDto
import ru.mercury.vpclient.shared.network.entity.BarcodeScanRequestDto
import ru.mercury.vpclient.shared.network.entity.BasketAddProductByBarcodeAndLocationIdRequestDto
import ru.mercury.vpclient.shared.network.entity.BasketAddProductByBarcodeRequestDto
import ru.mercury.vpclient.shared.network.entity.BasketAddProductFromDetailedStocksRequestDto
import ru.mercury.vpclient.shared.network.entity.BasketCheckoutOrderResponseDto
import ru.mercury.vpclient.shared.network.entity.BasketForCheckoutResponseDto
import ru.mercury.vpclient.shared.network.entity.BasketGetDeliveryTimesForFittingRequestDto
import ru.mercury.vpclient.shared.network.entity.BasketGetDeliveryTimesForOrderRequestDto
import ru.mercury.vpclient.shared.network.entity.BasketOperationRequestDto
import ru.mercury.vpclient.shared.network.entity.BasketResponseDto
import ru.mercury.vpclient.shared.network.entity.BasketSyncRequestDto
import ru.mercury.vpclient.shared.network.entity.BlvLinkContentDto
import ru.mercury.vpclient.shared.network.entity.CardInfoResponseDto
import ru.mercury.vpclient.shared.network.entity.CardTypeDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.CatalogBrandsResponseDto
import ru.mercury.vpclient.shared.network.entity.CatalogFashionImageCardDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.CatalogItemTypeEnum
import ru.mercury.vpclient.shared.network.entity.CatalogProductSearchCardDtoItemsResponse
import ru.mercury.vpclient.shared.network.entity.CatalogScanHistoryDto
import ru.mercury.vpclient.shared.network.entity.ChangeActionRequestDto
import ru.mercury.vpclient.shared.network.entity.CheckOutAddressesDto
import ru.mercury.vpclient.shared.network.entity.CheckUserResponseDto
import ru.mercury.vpclient.shared.network.entity.ClientActivityInfoDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.ClientAddressDto
import ru.mercury.vpclient.shared.network.entity.ClientAddressWithCoordinateDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.ClientSyncRequestDto
import ru.mercury.vpclient.shared.network.entity.CompilationCopyRequestDto
import ru.mercury.vpclient.shared.network.entity.CompilationCreateRequestDto
import ru.mercury.vpclient.shared.network.entity.CompilationDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.CompilationLookSaveRequestDto
import ru.mercury.vpclient.shared.network.entity.CompilationShareRequestDto
import ru.mercury.vpclient.shared.network.entity.CompilationStatus
import ru.mercury.vpclient.shared.network.entity.CompilationsEmployeeLookUploadCollagePostRequest
import ru.mercury.vpclient.shared.network.entity.CompilationsEmployeeLookUploadPhotoPostRequest
import ru.mercury.vpclient.shared.network.entity.CompilationsEmployeeUploadPhotoPostRequest
import ru.mercury.vpclient.shared.network.entity.CompilationsWithStatsByClientResponseDto
import ru.mercury.vpclient.shared.network.entity.CompilationsWithStatsResponseDto
import ru.mercury.vpclient.shared.network.entity.CreateClientAddressRequestDto
import ru.mercury.vpclient.shared.network.entity.CreateCompilationResultDto
import ru.mercury.vpclient.shared.network.entity.CreateLookFromFashionImageRequestDto
import ru.mercury.vpclient.shared.network.entity.CreateOrderWithGiftCardRequestDto
import ru.mercury.vpclient.shared.network.entity.CreatePaletteFolderRequestDto
import ru.mercury.vpclient.shared.network.entity.DeleteClientAddressRequestDto
import ru.mercury.vpclient.shared.network.entity.DeleteFashionImageRequestDto
import ru.mercury.vpclient.shared.network.entity.DeleteLinkedCardRequestDto
import ru.mercury.vpclient.shared.network.entity.DeleteMessageRequestDto
import ru.mercury.vpclient.shared.network.entity.DeleteProductFromFashionImageRequestDto
import ru.mercury.vpclient.shared.network.entity.DeleteProductsFromFolderRequestDto
import ru.mercury.vpclient.shared.network.entity.DeliveryTimesForSingleProductRequestDto
import ru.mercury.vpclient.shared.network.entity.DetailedStocksRequestDto
import ru.mercury.vpclient.shared.network.entity.DetailedStocksResponseDto
import ru.mercury.vpclient.shared.network.entity.DetermineGenderByClientIdRequestDto
import ru.mercury.vpclient.shared.network.entity.DigineticaFilterValuesRequestDto
import ru.mercury.vpclient.shared.network.entity.DigineticaFilteredProductsQuantityRequestDto
import ru.mercury.vpclient.shared.network.entity.DigineticaFilteredProductsRequestDto
import ru.mercury.vpclient.shared.network.entity.DigineticaFilteredProductsResponseDto
import ru.mercury.vpclient.shared.network.entity.DigineticaFiltersRequestDto
import ru.mercury.vpclient.shared.network.entity.EditFashionImageRequestDto
import ru.mercury.vpclient.shared.network.entity.EditMessageRequestDto
import ru.mercury.vpclient.shared.network.entity.EditPaletteFolderRequestDto
import ru.mercury.vpclient.shared.network.entity.EmployeeCompilationProductsReportRequestDto
import ru.mercury.vpclient.shared.network.entity.EmployeeEventsReportRequestDto
import ru.mercury.vpclient.shared.network.entity.EmployeeFittingLimitsDto
import ru.mercury.vpclient.shared.network.entity.EmployeeFittingOrderAccessFlagsResponseDto
import ru.mercury.vpclient.shared.network.entity.EmployeeNotificationsFiltersRequestDto
import ru.mercury.vpclient.shared.network.entity.EmployeeNotificationsFiltersResponseDto
import ru.mercury.vpclient.shared.network.entity.EmployeeNotificationsRequestDto
import ru.mercury.vpclient.shared.network.entity.EmployeeNotificationsResponseDto
import ru.mercury.vpclient.shared.network.entity.ExportCompilationStatisticsRequestDto
import ru.mercury.vpclient.shared.network.entity.ExportCompilationStatisticsResponseDto
import ru.mercury.vpclient.shared.network.entity.ExportEmployeeEventsRequestDto
import ru.mercury.vpclient.shared.network.entity.ExportEmployeeEventsResponseDto
import ru.mercury.vpclient.shared.network.entity.ExportTasksRequestDto
import ru.mercury.vpclient.shared.network.entity.ExportTasksResponseDto
import ru.mercury.vpclient.shared.network.entity.FashionImageStatisticsReportRequestDto
import ru.mercury.vpclient.shared.network.entity.FillAlternativesInBasketsRequestDto
import ru.mercury.vpclient.shared.network.entity.FittingCheckoutTagsRequestDto
import ru.mercury.vpclient.shared.network.entity.FittingCheckoutTagsResponseDto
import ru.mercury.vpclient.shared.network.entity.FittingDeliveryTimeResponseDto
import ru.mercury.vpclient.shared.network.entity.FittingExternalPushRequestDto
import ru.mercury.vpclient.shared.network.entity.FittingExternalPushResponseItemDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.FittingForCheckoutResponseDto
import ru.mercury.vpclient.shared.network.entity.FittingHistoryFiltersRequestDto
import ru.mercury.vpclient.shared.network.entity.FittingHistoryFiltersResponseDto
import ru.mercury.vpclient.shared.network.entity.FittingHistoryRequestDto
import ru.mercury.vpclient.shared.network.entity.FittingHistoryResponseDto
import ru.mercury.vpclient.shared.network.entity.FittingOperationRequestDto
import ru.mercury.vpclient.shared.network.entity.FittingResponseDto
import ru.mercury.vpclient.shared.network.entity.FittingSyncRequestDto
import ru.mercury.vpclient.shared.network.entity.GenderDto
import ru.mercury.vpclient.shared.network.entity.GetCheckOutFlagsForExistingFittingRequestDto
import ru.mercury.vpclient.shared.network.entity.GetCheckOutFlagsForSingleProductRequestDto
import ru.mercury.vpclient.shared.network.entity.GetCheckOutFlagsRequestDto
import ru.mercury.vpclient.shared.network.entity.GetCheckOutFlagsResponseDto
import ru.mercury.vpclient.shared.network.entity.GetDeliveryIntervalsForExistingFittingRequestDto
import ru.mercury.vpclient.shared.network.entity.GetDeliveryIntervalsForExistingFittingResponseDto
import ru.mercury.vpclient.shared.network.entity.GetDeliveryIntervalsForExistingOrderRequestDto
import ru.mercury.vpclient.shared.network.entity.GetDeliveryIntervalsForExistingOrderResponseDto
import ru.mercury.vpclient.shared.network.entity.GetEmployeeFittingLimitsForSingleProductRequestDto
import ru.mercury.vpclient.shared.network.entity.GetEmployeeFittingLimitsRequestDto
import ru.mercury.vpclient.shared.network.entity.GetListClientAddressForCheckoutRequestDto
import ru.mercury.vpclient.shared.network.entity.GetListClientAddressRequestDto
import ru.mercury.vpclient.shared.network.entity.GiftCardResponseDto
import ru.mercury.vpclient.shared.network.entity.ImageSearchRequestDto
import ru.mercury.vpclient.shared.network.entity.ImageSearchResponseDto
import ru.mercury.vpclient.shared.network.entity.ImagesUploadPostRequest
import ru.mercury.vpclient.shared.network.entity.ImagesUploadResponseDto
import ru.mercury.vpclient.shared.network.entity.ImportResultDto
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportAppendCollectionPostRequest
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportImportCollectionPostRequest
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportImportCrmTasksPostRequest
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportImportLooksPostRequest
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportImportMannequinsPostRequest
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportImportNewArrivalsPostRequest
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportImportSortSettingsPostRequest
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportImportVpTasksPostRequest
import ru.mercury.vpclient.shared.network.entity.InternalExcelImportUpdateNoPhotoPostRequest
import ru.mercury.vpclient.shared.network.entity.LinkCardRequestDto
import ru.mercury.vpclient.shared.network.entity.LookProductsResponseDto
import ru.mercury.vpclient.shared.network.entity.LookProductsWithStatsByClientResponseDto
import ru.mercury.vpclient.shared.network.entity.LookProductsWithStatsResponseDto
import ru.mercury.vpclient.shared.network.entity.LooksResponseDto
import ru.mercury.vpclient.shared.network.entity.LooksResponseItemDto
import ru.mercury.vpclient.shared.network.entity.LooksResponseItemDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.LooksWithStatsResponseDto
import ru.mercury.vpclient.shared.network.entity.MainDeliveryResponseDto
import ru.mercury.vpclient.shared.network.entity.MainScreenAnalyticsReportRequestDto
import ru.mercury.vpclient.shared.network.entity.MessageGetRequestDto
import ru.mercury.vpclient.shared.network.entity.MessageReadGetDto
import ru.mercury.vpclient.shared.network.entity.MessageReadResponseDto
import ru.mercury.vpclient.shared.network.entity.MessageResponseDto
import ru.mercury.vpclient.shared.network.entity.MessageResponseDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.MessengerActivityReportRequestDto
import ru.mercury.vpclient.shared.network.entity.MessengerActivityReportResponseDto
import ru.mercury.vpclient.shared.network.entity.MotivationRequestDto
import ru.mercury.vpclient.shared.network.entity.MotivationResponseDto
import ru.mercury.vpclient.shared.network.entity.MyClientsClientBadgesResponseDto
import ru.mercury.vpclient.shared.network.entity.MyClientsFiltersResponseDto
import ru.mercury.vpclient.shared.network.entity.MyClientsRequestDto
import ru.mercury.vpclient.shared.network.entity.MyClientsResponseDto
import ru.mercury.vpclient.shared.network.entity.NewArrivalsPushRequestDto
import ru.mercury.vpclient.shared.network.entity.NewLooksResponseDto
import ru.mercury.vpclient.shared.network.entity.OrderBonusReservationConfirmationRequestDto
import ru.mercury.vpclient.shared.network.entity.OrderBonusReservationRequestDto
import ru.mercury.vpclient.shared.network.entity.OrderConfirmPaymentViaCloudPaymentRequestDto
import ru.mercury.vpclient.shared.network.entity.OrderCreationFromFittingRequestDto
import ru.mercury.vpclient.shared.network.entity.OrderCreationRequestDto
import ru.mercury.vpclient.shared.network.entity.OrderDeliveryChangeRequestDto
import ru.mercury.vpclient.shared.network.entity.OrderExternalPushRequestDto
import ru.mercury.vpclient.shared.network.entity.OrderExternalPushResponseItemDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.OrderPaymentLinkResponseDto
import ru.mercury.vpclient.shared.network.entity.OrderResponseDto
import ru.mercury.vpclient.shared.network.entity.OrderResponseWithBadgeDto
import ru.mercury.vpclient.shared.network.entity.OrderResponseWithBadgeDtoItemsDto
import ru.mercury.vpclient.shared.network.entity.OrderStartPaymentViaCloudPaymentRequestDto
import ru.mercury.vpclient.shared.network.entity.OrderStartPaymentViaSbpResponseDto
import ru.mercury.vpclient.shared.network.entity.PaletteFoldersResponseDto
import ru.mercury.vpclient.shared.network.entity.PaletteProductsResponseDto
import ru.mercury.vpclient.shared.network.entity.PaymentAcknowledgeRequestDto
import ru.mercury.vpclient.shared.network.entity.PaymentReconciliationRequestDto
import ru.mercury.vpclient.shared.network.entity.PaymentReconciliationResponseDto
import ru.mercury.vpclient.shared.network.entity.PushTokenDto
import ru.mercury.vpclient.shared.network.entity.RemoveBackgroundRequestDto
import ru.mercury.vpclient.shared.network.entity.RemoveBackgroundResponseDto
import ru.mercury.vpclient.shared.network.entity.ReserveRequestDto
import ru.mercury.vpclient.shared.network.entity.ReserveResponseDto
import ru.mercury.vpclient.shared.network.entity.ResetActivityCountersRequestDto
import ru.mercury.vpclient.shared.network.entity.SaveClientCompilationMessageRequestDto
import ru.mercury.vpclient.shared.network.entity.SaveCompilationLookMessageRequestDto
import ru.mercury.vpclient.shared.network.entity.SaveMessageWithImagesRequestDto
import ru.mercury.vpclient.shared.network.entity.SaveProductMessageRequestDto
import ru.mercury.vpclient.shared.network.entity.StatisticsType
import ru.mercury.vpclient.shared.network.entity.StringItemsDto
import ru.mercury.vpclient.shared.network.entity.SuggestionResponseDto
import ru.mercury.vpclient.shared.network.entity.SuggestsSearchRequest
import ru.mercury.vpclient.shared.network.entity.TasksImportResultDto
import ru.mercury.vpclient.shared.network.entity.TransferBasketToFittingRequestDto
import ru.mercury.vpclient.shared.network.entity.TransferRequestDto
import ru.mercury.vpclient.shared.network.entity.TransferResponseDto
import ru.mercury.vpclient.shared.network.entity.UpdateClientAddressRequestDto
import ru.mercury.vpclient.shared.network.entity.UpdateMainScreenRequestDto
import ru.mercury.vpclient.shared.network.entity.UpdateMessageDto
import ru.mercury.vpclient.shared.network.entity.UpdateNoPhotoResultDto
import ru.mercury.vpclient.shared.network.entity.UploadCollageRequestDto
import ru.mercury.vpclient.shared.network.entity.UserProfileUploadPhotoPostRequest
import ru.mercury.vpclient.shared.network.entity.VerifyLinkCardRequestDto
import ru.mercury.vpclient.shared.network.entity.ViewHistoryDto
import ru.mercury.vpclient.shared.network.request.AuthenticationContinueLoginRequest
import ru.mercury.vpclient.shared.network.request.AuthenticationLoginRequest
import ru.mercury.vpclient.shared.network.request.AuthenticationRegisterRequest
import ru.mercury.vpclient.shared.network.request.BottomCategoriesRequest
import ru.mercury.vpclient.shared.network.request.DetailCardRequest
import ru.mercury.vpclient.shared.network.request.FilterValuesRequest
import ru.mercury.vpclient.shared.network.request.FilteredProductsQuantityRequest
import ru.mercury.vpclient.shared.network.request.FilteredProductsRequest
import ru.mercury.vpclient.shared.network.request.FiltersRequest
import ru.mercury.vpclient.shared.network.response.BaseResponse
import ru.mercury.vpclient.shared.network.response.CatalogCategoriesBasicResponse
import ru.mercury.vpclient.shared.network.response.CatalogProductDetailCardV2Response
import ru.mercury.vpclient.shared.network.response.CurrentUserResponse
import ru.mercury.vpclient.shared.network.response.EmployeeBadgesResponse
import ru.mercury.vpclient.shared.network.response.EmployeeResponse
import ru.mercury.vpclient.shared.network.response.FilterValuesResponse
import ru.mercury.vpclient.shared.network.response.FilteredProductsQuantityResponse
import ru.mercury.vpclient.shared.network.response.FilteredProductsResponse
import ru.mercury.vpclient.shared.network.response.FiltersResponse
import ru.mercury.vpclient.shared.network.response.MyEmployeesResponse
import ru.mercury.vpclient.shared.network.response.TokenResponse
import javax.inject.Inject

class NetworkService @Inject constructor(
    private val ktorHttpClient: HttpClient
) {

    suspend fun authenticationRegister(
        request: AuthenticationRegisterRequest
    ): BaseResponse<String> {
        return ktorHttpClient.post("authentication/register") {
            setBody(request)
        }.body()
    }

    suspend fun authenticationLogin(
        request: AuthenticationLoginRequest
    ): BaseResponse<String> {
        return ktorHttpClient.post("authentication/login") {
            setBody(request)
        }.body()
    }

    suspend fun authenticationContinueLogin(
        request: AuthenticationContinueLoginRequest
    ): BaseResponse<TokenResponse> {
        return ktorHttpClient.post("authentication/continue-login") {
            setBody(request)
        }.body()
    }

    suspend fun authenticationLogout(): BaseResponse<String> {
        return ktorHttpClient.post("authentication/logout").body()
    }

    suspend fun catalogCategoriesBasic(): BaseResponse<CatalogCategoriesBasicResponse> {
        return ktorHttpClient.post("catalog/categories/basic").body()
    }

    suspend fun catalogCategoriesTop(): BaseResponse<CatalogCategoriesBasicResponse> {
        return ktorHttpClient.post("catalog/categories/top").body()
    }

    suspend fun catalogCategoriesBottom(
        request: BottomCategoriesRequest
    ): BaseResponse<CatalogCategoriesBasicResponse> {
        return ktorHttpClient.post("catalog/categories/bottom") {
            setBody(request)
        }.body()
    }

    suspend fun catalogFilters(
        request: FiltersRequest
    ): BaseResponse<FiltersResponse> {
        return ktorHttpClient.post("catalog/filters") {
            setBody(request)
        }.body()
    }

    suspend fun catalogFilterValues(
        request: FilterValuesRequest
    ): BaseResponse<FilterValuesResponse> {
        return ktorHttpClient.post("catalog/filter-values") {
            setBody(request)
        }.body()
    }

    suspend fun catalogFilterProductsQuantity(
        request: FilteredProductsQuantityRequest
    ): BaseResponse<FilteredProductsQuantityResponse> {
        return ktorHttpClient.post("catalog/filter-products-quantity") {
            setBody(request)
        }.body()
    }

    suspend fun catalogProducts(
        limit: Int,
        offset: Int,
        request: FilteredProductsRequest
    ): BaseResponse<FilteredProductsResponse> {
        return ktorHttpClient.post("catalog/products") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
            setBody(request)
        }.body()
    }

    suspend fun catalogDetailedProduct(
        request: DetailCardRequest
    ): BaseResponse<CatalogProductDetailCardV2Response> {
        return ktorHttpClient.post("catalog/detailed-product") {
            setBody(request)
        }.body()
    }

    suspend fun clientMyEmployees(): BaseResponse<MyEmployeesResponse> {
        return ktorHttpClient.get("client/my-employees").body()
    }

    suspend fun clientEmployeeBadges(
        employeeId: String
    ): BaseResponse<EmployeeBadgesResponse> {
        return ktorHttpClient.get("client/my-employees/$employeeId/badges").body()
    }

    suspend fun clientActiveEmployee(): BaseResponse<EmployeeResponse> {
        return ktorHttpClient.get("client/active-employee").body()
    }

    suspend fun clientEmployee(
        employeeId: String
    ): BaseResponse<EmployeeResponse> {
        return ktorHttpClient.get("client/my-employee/$employeeId").body()
    }

    suspend fun userCurrentUser(): BaseResponse<CurrentUserResponse> {
        return ktorHttpClient.get("user/current-user").body()
    }


    // fixme

    suspend fun actionsPost(
        request: ActionItemDto
    ): BaseResponse<ActionItemDto> {
        return ktorHttpClient.post("actions") {
            setBody(request)
        }.body()
    }

    suspend fun actionsGet(
        category: CatalogItemTypeEnum? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ActionsResponseDto> {
        return ktorHttpClient.get("actions") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun actionsByActionIdNotifyAll(
        actionId: Int,
        request: ActionPushRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("actions/$actionId/notify-all") {
            setBody(request)
        }.body()
    }

    suspend fun actionsNewArrivalsNotifyAll(
        request: NewArrivalsPushRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("actions/new-arrivals/notify-all") {
            setBody(request)
        }.body()
    }

    suspend fun actionsActions(
        category: CatalogItemTypeEnum? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ActionsResponseDto> {
        return ktorHttpClient.get("actions/actions") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun actionsNewCollections(
        category: CatalogItemTypeEnum? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ActionsResponseDto> {
        return ktorHttpClient.get("actions/newCollections") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun actionsNewCollectionsVM(
        category: CatalogItemTypeEnum? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ActionsResponseDto> {
        return ktorHttpClient.get("actions/newCollectionsVM") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun actionsByActionId(
        actionId: Int
    ): BaseResponse<ActionItemDto> {
        return ktorHttpClient.get("actions/$actionId").body()
    }

    suspend fun actionsNewLooks(
        category: CatalogItemTypeEnum? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<NewLooksResponseDto> {
        return ktorHttpClient.get("actions/newLooks") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun activityCountersByPairedUserId(
        pairedUserId: String
    ): BaseResponse<AggregatedActivityCounterResponseDto> {
        return ktorHttpClient.get("activity/counters/$pairedUserId").body()
    }

    suspend fun activityByClientIdViewHistory(
        clientId: String,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ViewHistoryDto> {
        return ktorHttpClient.get("activity/$clientId/view-history") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun activityCountersByPairedUserIdReset(
        pairedUserId: String,
        request: ResetActivityCountersRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("activity/counters/$pairedUserId/reset") {
            setBody(request)
        }.body()
    }

    suspend fun addressSuggestion(
        limit: Int? = null,
        searchText: String? = null
    ): BaseResponse<AddressSuggestionDtoItemsDto> {
        return ktorHttpClient.get("address-suggestion") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("searchText", searchText)
        }.body()
    }

    suspend fun internalAnalyticsTeamUpdateMainScreen(
        request: UpdateMainScreenRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/analytics-team/update-main-screen") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaPushFitting(
        request: FittingExternalPushRequestDto
    ): BaseResponse<FittingExternalPushResponseItemDtoItemsDto> {
        return ktorHttpClient.post("/axapta/push/fitting") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaPushOrder(
        request: OrderExternalPushRequestDto
    ): BaseResponse<OrderExternalPushResponseItemDtoItemsDto> {
        return ktorHttpClient.post("/axapta/push/order") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaPaymentsReconciliation(
        request: PaymentReconciliationRequestDto
    ): BaseResponse<PaymentReconciliationResponseDto> {
        return ktorHttpClient.post("/axapta/payments-reconciliation") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardAuthClient(
        request: AxLoyaltyCardAuthRequestDto
    ): BaseResponse<AxLoyaltyCardAuthResponseDto> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardAuthClient") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardCheckPin(
        request: AxLoyaltyCardCheckPinRequestDto
    ): BaseResponse<AxLoyaltyCardCheckPinResponseDto> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardCheckPin") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardCalcBonusAmount(
        request: AxLoyaltyCardCalcBonusAmountRequestDto
    ): BaseResponse<AxLoyaltyCardCalcBonusAmountResponseDto> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardCalcBonusAmount") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardCalcAndReserveBonusPay(
        request: AxLoyaltyCardCalcAndReserveBonusPayRequestDto
    ): BaseResponse<AxLoyaltyCardCalcAndReserveBonusPayResponseDto> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardCalcAndReserveBonusPay") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardRollBackBonusPay(
        request: AxLoyaltyCardRollBackBonusPayRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardRollBackBonusPay") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyBasketPaymAcknowledge(
        request: PaymentAcknowledgeRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/axapta/loyalty/basketPaymAcknowledge") {
            setBody(request)
        }.body()
    }

    suspend fun basket(
        request: BasketOperationRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket") {
            setBody(request)
        }.body()
    }

    suspend fun basketAddProductByBarcode(
        request: BasketAddProductByBarcodeRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket/add-product-by-barcode") {
            setBody(request)
        }.body()
    }

    suspend fun basketAddProductByBarcodeAndLocationid(
        request: BasketAddProductByBarcodeAndLocationIdRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket/add-product-by-barcode-and-locationid") {
            setBody(request)
        }.body()
    }

    suspend fun basketAddProductFromDetailedStocks(
        request: BasketAddProductFromDetailedStocksRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket/add-product-from-detailed-stocks") {
            setBody(request)
        }.body()
    }

    suspend fun basketByPairedUserId(
        pairedUserId: String
    ): BaseResponse<BasketResponseDto> {
        return ktorHttpClient.get("basket/$pairedUserId").body()
    }

    suspend fun basketChatGet(
        request: MessageGetRequestDto
    ): BaseResponse<MessageResponseDtoItemsDto> {
        return ktorHttpClient.post("basket/chat/get") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatStatus(
        request: MessageReadGetDto
    ): BaseResponse<MessageReadResponseDto> {
        return ktorHttpClient.post("basket/chat/status") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatSend(
        request: SaveProductMessageRequestDto
    ): BaseResponse<MessageResponseDto> {
        return ktorHttpClient.post("basket/chat/send") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatSendCompilationLook(
        request: SaveCompilationLookMessageRequestDto
    ): BaseResponse<MessageResponseDto> {
        return ktorHttpClient.post("basket/chat/send/compilationLook") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatSendClientCompilation(
        request: SaveClientCompilationMessageRequestDto
    ): BaseResponse<MessageResponseDto> {
        return ktorHttpClient.post("basket/chat/send/clientCompilation") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatSendImages(
        request: SaveMessageWithImagesRequestDto
    ): BaseResponse<MessageResponseDto> {
        return ktorHttpClient.post("basket/chat/send/images") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatUpdate(
        request: UpdateMessageDto
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("basket/chat/update") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatEdit(
        request: EditMessageRequestDto
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("basket/chat/edit") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatDelete(
        request: DeleteMessageRequestDto
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("basket/chat/delete") {
            setBody(request)
        }.body()
    }

    suspend fun basketByPairedUserIdForCheckout(
        pairedUserId: String
    ): BaseResponse<BasketForCheckoutResponseDto> {
        return ktorHttpClient.get("basket/$pairedUserId/for-checkout").body()
    }

    suspend fun blvLinkIdByLinkId(
        linkId: String
    ): BaseResponse<BlvLinkContentDto> {
        return ktorHttpClient.get("blv-link/id/$linkId").body()
    }

    suspend fun blvLinkApproveDelivery(
        request: ApproveFittingDeliveryRequestDto
    ): BaseResponse<ApproveFittingDeliveryResponse> {
        return ktorHttpClient.post("blv-link/approve-delivery") {
            setBody(request)
        }.body()
    }

    suspend fun blvLinkApproveFittingExtension(
        request: ApproveFittingExpirationDateExtensionRequestDto
    ): BaseResponse<ApproveFittingExpirationDateExtensionResponse> {
        return ktorHttpClient.post("blv-link/approve-fitting-extension") {
            setBody(request)
        }.body()
    }

    suspend fun blvLinkApproveFittingManualPickup(
        request: ApproveFittingDeliveryManualPickupRequestDto
    ): BaseResponse<ApproveFittingDeliveryManualPickupResponse> {
        return ktorHttpClient.post("blv-link/approve-fitting-manual-pickup") {
            setBody(request)
        }.body()
    }

    suspend fun catalogActionIdSpecialOffer(): BaseResponse<Int> {
        return ktorHttpClient.post("catalog/action-id/special-offer").body()
    }

    suspend fun catalogBrands(): BaseResponse<CatalogBrandsResponseDto> {
        return ktorHttpClient.post("catalog/brands").body()
    }

    suspend fun catalogAvailableColors(
        request: AvailableColorsRequestDto
    ): BaseResponse<AvailableColorsResponseDto> {
        return ktorHttpClient.post("catalog/available/colors") {
            setBody(request)
        }.body()
    }

    suspend fun catalogAvailableSizes(
        request: AvailableSizesRequestDto
    ): BaseResponse<AvailableSizesResponseDto> {
        return ktorHttpClient.post("catalog/available/sizes") {
            setBody(request)
        }.body()
    }

    suspend fun catalogAvailableLocations(
        request: AvailableLocationsRequestDto
    ): BaseResponse<AvailableLocationsResponseDto> {
        return ktorHttpClient.post("catalog/available/locations") {
            setBody(request)
        }.body()
    }

    suspend fun catalogNewArrivals(
        category: CatalogItemTypeEnum? = null,
        cover: Boolean? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<CatalogProductSearchCardDtoItemsResponse> {
        return ktorHttpClient.get("catalog/newArrivals") {
            appendQueryParameter("category", category)
            appendQueryParameter("cover", cover)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun catalogNewLookProducts(
        limit: Int? = null,
        lookId: Int? = null,
        offset: Int? = null
    ): BaseResponse<CatalogProductSearchCardDtoItemsResponse> {
        return ktorHttpClient.get("catalog/newLookProducts") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("lookId", lookId)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun catalogFashionImageById(
        id: Int
    ): BaseResponse<CatalogFashionImageCardDtoItemsDto> {
        return ktorHttpClient.get("catalog/fashion-image/$id").body()
    }

    suspend fun catalogFashionImageDeleteAction(
        actionId: Int? = null
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("catalog/fashion-image/delete/action") {
            appendQueryParameter("actionId", actionId)
        }.body()
    }

    suspend fun catalogScan(
        request: BarcodeScanRequestDto
    ): BaseResponse<CatalogProductDetailCardV2Response> {
        return ktorHttpClient.post("catalog/scan") {
            setBody(request)
        }.body()
    }

    suspend fun catalogScanLocations(
        request: BarcodeScanRequestDto
    ): BaseResponse<AvailableLocationsForProductResponseDto> {
        return ktorHttpClient.post("catalog/scan/locations") {
            setBody(request)
        }.body()
    }

    suspend fun catalogScanHistory(
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<CatalogScanHistoryDto> {
        return ktorHttpClient.get("catalog/scan/history") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun catalogDetailedStocks(
        request: DetailedStocksRequestDto
    ): BaseResponse<DetailedStocksResponseDto> {
        return ktorHttpClient.post("catalog/detailed-stocks") {
            setBody(request)
        }.body()
    }

    suspend fun catalogReserveBySerialid(
        request: ReserveRequestDto
    ): BaseResponse<ReserveResponseDto> {
        return ktorHttpClient.post("catalog/reserve-by-serialid") {
            setBody(request)
        }.body()
    }

    suspend fun catalogTransferBySerialid(
        request: TransferRequestDto
    ): BaseResponse<TransferResponseDto> {
        return ktorHttpClient.post("catalog/transfer-by-serialid") {
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextSuggestsDiginetica(
        limit: Int? = null,
        request: SuggestsSearchRequest
    ): BaseResponse<SuggestionResponseDto> {
        return ktorHttpClient.post("catalog/by-text/suggests/diginetica") {
            appendQueryParameter("limit", limit)
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextProductsDiginetica(
        limit: Int? = null,
        offset: Int? = null,
        request: DigineticaFilteredProductsRequestDto
    ): BaseResponse<DigineticaFilteredProductsResponseDto> {
        return ktorHttpClient.post("catalog/by-text/products/diginetica") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextFiltersDiginetica(
        request: DigineticaFiltersRequestDto
    ): BaseResponse<FiltersResponse> {
        return ktorHttpClient.post("catalog/by-text/filters/diginetica") {
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextFilterValues(
        request: DigineticaFilterValuesRequestDto
    ): BaseResponse<FilterValuesResponse> {
        return ktorHttpClient.post("catalog/by-text/filter-values") {
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextFilterProductsQuantity(
        request: DigineticaFilteredProductsQuantityRequestDto
    ): BaseResponse<FilteredProductsQuantityResponse> {
        return ktorHttpClient.post("catalog/by-text/filter-products-quantity") {
            setBody(request)
        }.body()
    }

    suspend fun chatByClientIdSendPushByMessageId(
        clientId: String,
        messageId: Int
    ): BaseResponse<String> {
        return ktorHttpClient.post("chat/$clientId/send-push/$messageId").body()
    }

    suspend fun chatByClientIdSendSmsByMessageId(
        clientId: String,
        messageId: Int
    ): BaseResponse<String> {
        return ktorHttpClient.post("chat/$clientId/send-sms/$messageId").body()
    }

    suspend fun clientAddressList(
        request: GetListClientAddressRequestDto
    ): BaseResponse<ClientAddressWithCoordinateDtoItemsDto> {
        return ktorHttpClient.post("client/address/list") {
            setBody(request)
        }.body()
    }

    suspend fun clientAddressCheckout(
        request: GetListClientAddressForCheckoutRequestDto
    ): BaseResponse<CheckOutAddressesDto> {
        return ktorHttpClient.post("client/address/checkout") {
            setBody(request)
        }.body()
    }

    suspend fun clientAddressCreate(
        request: CreateClientAddressRequestDto
    ): BaseResponse<ClientAddressDto> {
        return ktorHttpClient.post("client/address/create") {
            setBody(request)
        }.body()
    }

    suspend fun clientAddressUpdate(
        request: UpdateClientAddressRequestDto
    ): BaseResponse<ClientAddressDto> {
        return ktorHttpClient.post("client/address/update") {
            setBody(request)
        }.body()
    }

    suspend fun clientAddressDelete(
        request: DeleteClientAddressRequestDto
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("client/address/delete") {
            setBody(request)
        }.body()
    }

    suspend fun codeGender(
        request: DetermineGenderByClientIdRequestDto
    ): BaseResponse<GenderDto> {
        return ktorHttpClient.post("code/gender") {
            setBody(request)
        }.body()
    }

    suspend fun codeSessionActivate(): BaseResponse<Boolean> {
        return ktorHttpClient.post("code/session-activate").body()
    }

    suspend fun compilationsClient(
        limit: Int? = null,
        offset: Int? = null,
        pairedUserId: String? = null
    ): BaseResponse<CompilationDtoItemsDto> {
        return ktorHttpClient.get("compilations/client") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
            appendQueryParameter("pairedUserId", pairedUserId)
        }.body()
    }

    suspend fun compilationsClientByCompilationId(
        compilationId: Int,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<LooksResponseDto> {
        return ktorHttpClient.get("compilations/client/$compilationId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsClientLookByLookId(
        lookId: Int
    ): BaseResponse<LookProductsResponseDto> {
        return ktorHttpClient.get("compilations/client/look/$lookId").body()
    }

    suspend fun compilationsClientLookByLookIdToBasket(
        lookId: Int
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("compilations/client/look/$lookId/to-basket").body()
    }

    suspend fun compilationsEmployeeCreate(
        request: CompilationCreateRequestDto
    ): BaseResponse<CreateCompilationResultDto> {
        return ktorHttpClient.post("compilations/employee/create") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeByCompilationIdEdit(
        compilationId: Int,
        request: CompilationCreateRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/edit") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeCopy(
        request: CompilationCopyRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/copy") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployee(
        limit: Int? = null,
        offset: Int? = null,
        pairedUserId: String? = null
    ): BaseResponse<CompilationDtoItemsDto> {
        return ktorHttpClient.get("compilations/employee") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
            appendQueryParameter("pairedUserId", pairedUserId)
        }.body()
    }

    suspend fun compilationsEmployeeByCompilationId(
        compilationId: Int,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<LooksResponseDto> {
        return ktorHttpClient.get("compilations/employee/$compilationId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookId(
        lookId: Int
    ): BaseResponse<LookProductsResponseDto> {
        return ktorHttpClient.get("compilations/employee/look/$lookId").body()
    }

    suspend fun compilationsEmployeeLookByLookIdToBasketClientByClientId(
        clientId: String,
        lookId: Int
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/to-basket/client/$clientId").body()
    }

    suspend fun compilationsEmployeeByCompilationIdDelete(
        compilationId: Int
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/delete").body()
    }

    suspend fun compilationsEmployeeByCompilationIdShare(
        compilationId: Int,
        request: CompilationShareRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/share") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeByCompilationIdShared(
        compilationId: Int
    ): BaseResponse<StringItemsDto> {
        return ktorHttpClient.get("compilations/employee/$compilationId/shared").body()
    }

    suspend fun compilationsEmployeeLookByLookIdDelete(
        lookId: Int
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/delete").body()
    }

    suspend fun compilationsEmployeeLookCreate(
        request: CompilationLookSaveRequestDto
    ): BaseResponse<LooksResponseItemDto> {
        return ktorHttpClient.post("compilations/employee/look/create") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeLookCreateFromFashionImage(
        request: CreateLookFromFashionImageRequestDto
    ): BaseResponse<LooksResponseItemDtoItemsDto> {
        return ktorHttpClient.post("compilations/employee/look/create-from-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookIdEdit(
        lookId: Int,
        request: CompilationLookSaveRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/edit") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookIdUploadCollage(
        lookId: Int,
        request: CompilationsEmployeeLookUploadCollagePostRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/upload-collage") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("file", request.file)
            }))
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookIdUploadPhoto(
        lookId: Int,
        request: CompilationsEmployeeLookUploadPhotoPostRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/upload-photo") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("file", request.file)
            }))
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookIdRemovePhoto(
        lookId: Int
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/remove-photo").body()
    }

    suspend fun compilationsEmployeeByCompilationIdUploadCollage(
        compilationId: Int,
        request: UploadCollageRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/upload-collage") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeByCompilationIdUploadPhoto(
        compilationId: Int,
        request: CompilationsEmployeeUploadPhotoPostRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/upload-photo") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("file", request.file)
            }))
        }.body()
    }

    suspend fun compilationsEmployeeByCompilationIdRemovePhoto(
        compilationId: Int
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/remove-photo").body()
    }

    suspend fun compilationsPaletteFoldersCreate(
        request: CreatePaletteFolderRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/palette/folders/create") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsPaletteFoldersByFolderIdDelete(
        folderId: Int
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/palette/folders/$folderId/delete").body()
    }

    suspend fun compilationsPaletteFoldersByFolderIdEdit(
        folderId: Int,
        request: EditPaletteFolderRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/palette/folders/$folderId/edit") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsPaletteFoldersByFolderIdDeleteAll(
        folderId: Int
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/palette/folders/$folderId/delete-all").body()
    }

    suspend fun compilationsPaletteFoldersByFolderId(
        folderId: Int,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<PaletteProductsResponseDto> {
        return ktorHttpClient.get("compilations/palette/folders/$folderId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsPaletteFoldersAddItems(
        request: AddProductsInFoldersRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/palette/folders/add-items") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsPaletteFoldersByFolderIdDeleteItems(
        folderId: Int,
        request: DeleteProductsFromFolderRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/palette/folders/$folderId/delete-items") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsPaletteFolders(): BaseResponse<PaletteFoldersResponseDto> {
        return ktorHttpClient.get("compilations/palette/folders").body()
    }

    suspend fun compilationsStatsClientByClientId(
        clientId: String,
        limit: Int? = null,
        offset: Int? = null,
        status: CompilationStatus? = null
    ): BaseResponse<CompilationsWithStatsByClientResponseDto> {
        return ktorHttpClient.get("compilations/stats/client/$clientId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
            appendQueryParameter("status", status)
        }.body()
    }

    suspend fun compilationsStatsByCompilationIdClientByClientId(
        clientId: String,
        compilationId: Int,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<LooksWithStatsResponseDto> {
        return ktorHttpClient.get("compilations/stats/$compilationId/client/$clientId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsStatsLookByLookIdClientByClientId(
        clientId: String,
        lookId: Int
    ): BaseResponse<LookProductsWithStatsByClientResponseDto> {
        return ktorHttpClient.get("compilations/stats/look/$lookId/client/$clientId").body()
    }

    suspend fun compilationsStats(
        limit: Int? = null,
        offset: Int? = null,
        status: CompilationStatus? = null
    ): BaseResponse<CompilationsWithStatsResponseDto> {
        return ktorHttpClient.get("compilations/stats") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
            appendQueryParameter("status", status)
        }.body()
    }

    suspend fun compilationsStatsByCompilationId(
        compilationId: Int,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<LooksWithStatsResponseDto> {
        return ktorHttpClient.get("compilations/stats/$compilationId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsStatsLookByLookId(
        lookId: Int,
        type: StatisticsType? = null
    ): BaseResponse<LookProductsWithStatsResponseDto> {
        return ktorHttpClient.get("compilations/stats/look/$lookId") {
            appendQueryParameter("type", type)
        }.body()
    }

    suspend fun internalContentTeamDeleteFashionImage(
        request: DeleteFashionImageRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/delete-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun internalContentTeamChangeAction(
        request: ChangeActionRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/change-action") {
            setBody(request)
        }.body()
    }

    suspend fun internalContentTeamAddProductToFashionImage(
        request: AddProductToFashionImageRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/add-product-to-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun internalContentTeamDeleteProductFromFashionImage(
        request: DeleteProductFromFashionImageRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/delete-product-from-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun internalContentTeamEditFashionImage(
        request: EditFashionImageRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/edit-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun delivery(): BaseResponse<MainDeliveryResponseDto> {
        return ktorHttpClient.get("delivery").body()
    }

    suspend fun employeeByEmployeeIdMyClientsFilters(
        employeeId: String
    ): BaseResponse<MyClientsFiltersResponseDto> {
        return ktorHttpClient.get("employee/$employeeId/my-clients/filters").body()
    }

    suspend fun employeeByEmployeeIdMyClients(
        employeeId: String,
        request: MyClientsRequestDto
    ): BaseResponse<MyClientsResponseDto> {
        return ktorHttpClient.post("employee/$employeeId/my-clients") {
            setBody(request)
        }.body()
    }

    suspend fun employeeByEmployeeIdMyClientsByClientIdBadges(
        clientId: String,
        employeeId: String
    ): BaseResponse<MyClientsClientBadgesResponseDto> {
        return ktorHttpClient.get("employee/$employeeId/my-clients/$clientId/badges").body()
    }

    suspend fun employeeByEmployeeIdActiveClient(
        employeeId: String
    ): BaseResponse<ActiveClientResponseDto> {
        return ktorHttpClient.get("employee/$employeeId/active-client").body()
    }

    suspend fun employeeByEmployeeIdFittingOrderAccessFlags(
        employeeId: String
    ): BaseResponse<EmployeeFittingOrderAccessFlagsResponseDto> {
        return ktorHttpClient.get("employee/$employeeId/fitting-order-access-flags").body()
    }

    suspend fun employeeByEmployeeIdFittingLimits(
        employeeId: String
    ): BaseResponse<EmployeeFittingLimitsDto> {
        return ktorHttpClient.get("employee/$employeeId/fitting-limits").body()
    }

    suspend fun internalExcelImportImportCollection(
        request: InternalExcelImportImportCollectionPostRequest
    ): List<ImportResultDto> {
        return ktorHttpClient.post("/internal/excelImport/importCollection") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportAppendCollection(
        fashionImageId: Int? = null,
        request: InternalExcelImportAppendCollectionPostRequest
    ): List<ImportResultDto> {
        return ktorHttpClient.post("/internal/excelImport/appendCollection") {
            appendQueryParameter("fashionImageId", fashionImageId)
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportNewArrivals(
        deleteExisting: Boolean? = null,
        request: InternalExcelImportImportNewArrivalsPostRequest
    ): List<ImportResultDto> {
        return ktorHttpClient.post("/internal/excelImport/importNewArrivals") {
            appendQueryParameter("deleteExisting", deleteExisting)
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportLooks(
        request: InternalExcelImportImportLooksPostRequest
    ): List<ImportResultDto> {
        return ktorHttpClient.post("/internal/excelImport/importLooks") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportMannequins(
        request: InternalExcelImportImportMannequinsPostRequest
    ): List<ImportResultDto> {
        return ktorHttpClient.post("/internal/excelImport/importMannequins") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportSortSettings(
        request: InternalExcelImportImportSortSettingsPostRequest
    ): List<ImportResultDto> {
        return ktorHttpClient.post("/internal/excelImport/importSortSettings") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportVpTasks(
        request: InternalExcelImportImportVpTasksPostRequest
    ): TasksImportResultDto {
        return ktorHttpClient.post("/internal/excelImport/importVpTasks") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportCrmTasks(
        request: InternalExcelImportImportCrmTasksPostRequest
    ): TasksImportResultDto {
        return ktorHttpClient.post("/internal/excelImport/importCrmTasks") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportUpdateNoPhoto(
        request: InternalExcelImportUpdateNoPhotoPostRequest
    ): UpdateNoPhotoResultDto {
        return ktorHttpClient.post("/internal/excelImport/update-no-photo") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExportCsvClientActivity(): HttpResponse {
        return ktorHttpClient.get("/internal/export/csv/client-activity") {
        }
    }

    suspend fun internalExportCsvBasketAdditions(): HttpResponse {
        return ktorHttpClient.get("/internal/export/csv/basket-additions") {
        }
    }

    suspend fun internalExportCsvCompilations(
        dateFrom: String? = null
    ): HttpResponse {
        return ktorHttpClient.get("/internal/export/csv/compilations") {
            appendQueryParameter("dateFrom", dateFrom)
        }
    }

    suspend fun internalExportCsvMessenger(
        request: MessengerActivityReportRequestDto
    ): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/messenger") {
            setBody(request)
        }
    }

    suspend fun internalExportCsvTasks(): HttpResponse {
        return ktorHttpClient.get("/internal/export/csv/tasks") {
        }
    }

    suspend fun internalExportCsvEmployeeEvents(
        request: EmployeeEventsReportRequestDto
    ): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/employee-events") {
            setBody(request)
        }
    }

    suspend fun internalExportCsvFashionImageStats(
        request: FashionImageStatisticsReportRequestDto
    ): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/fashion-image-stats") {
            setBody(request)
        }
    }

    suspend fun internalExportCsvEmployeePhotos(): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/employee-photos") {
        }
    }

    suspend fun internalExportCsvEmployeeCompilationProducts(
        request: EmployeeCompilationProductsReportRequestDto
    ): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/employee-compilation-products") {
            setBody(request)
        }
    }

    suspend fun internalExportCsvClientAvailableBrands(): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/client-available-brands") {
        }
    }

    suspend fun internalExportCsvMainScreenAnalytics(
        request: MainScreenAnalyticsReportRequestDto
    ): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/main-screen-analytics") {
            setBody(request)
        }
    }

    suspend fun internalExportJson1CClientsActivity(): BaseResponse<ClientActivityInfoDtoItemsDto> {
        return ktorHttpClient.get("/internal/export/json/1C/clients-activity").body()
    }

    suspend fun internalExportJsonCompilationsStats(
        request: ExportCompilationStatisticsRequestDto
    ): BaseResponse<ExportCompilationStatisticsResponseDto> {
        return ktorHttpClient.post("/internal/export/json/compilations/stats") {
            setBody(request)
        }.body()
    }

    suspend fun internalExportJsonMessenger(
        request: MessengerActivityReportRequestDto
    ): BaseResponse<MessengerActivityReportResponseDto> {
        return ktorHttpClient.post("/internal/export/json/messenger") {
            setBody(request)
        }.body()
    }

    suspend fun internalExportJsonTasks(
        request: ExportTasksRequestDto
    ): BaseResponse<ExportTasksResponseDto> {
        return ktorHttpClient.post("/internal/export/json/tasks") {
            setBody(request)
        }.body()
    }

    suspend fun internalExportJsonEmployeeEvents(
        request: ExportEmployeeEventsRequestDto
    ): BaseResponse<ExportEmployeeEventsResponseDto> {
        return ktorHttpClient.post("/internal/export/json/employee-events") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsAddOperations(
        request: FittingOperationRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("fittings/add-operations") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsByPairedUserId(
        pairedUserId: String
    ): BaseResponse<FittingResponseDto> {
        return ktorHttpClient.get("fittings/$pairedUserId").body()
    }

    suspend fun fittingsTransferFromBasket(
        request: TransferBasketToFittingRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("fittings/transfer-from-basket") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsCheckoutFlags(
        request: GetCheckOutFlagsRequestDto
    ): BaseResponse<GetCheckOutFlagsResponseDto> {
        return ktorHttpClient.post("fittings/checkout-flags") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsCheckoutFlagsForExisingFitting(
        request: GetCheckOutFlagsForExistingFittingRequestDto
    ): BaseResponse<GetCheckOutFlagsResponseDto> {
        return ktorHttpClient.post("fittings/checkout-flags-for-exising-fitting") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsCheckoutFlagsForSingleProduct(
        request: GetCheckOutFlagsForSingleProductRequestDto
    ): BaseResponse<GetCheckOutFlagsResponseDto> {
        return ktorHttpClient.post("fittings/checkout-flags-for-single-product") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsDeliveryTimes(
        request: BasketGetDeliveryTimesForFittingRequestDto
    ): BaseResponse<FittingDeliveryTimeResponseDto> {
        return ktorHttpClient.post("fittings/delivery-times") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsDeliveryTimesForExisingDelivery(
        request: GetDeliveryIntervalsForExistingFittingRequestDto
    ): BaseResponse<GetDeliveryIntervalsForExistingFittingResponseDto> {
        return ktorHttpClient.post("fittings/delivery-times-for-exising-delivery") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsDeliveryTimesForSingleProduct(
        request: DeliveryTimesForSingleProductRequestDto
    ): BaseResponse<FittingDeliveryTimeResponseDto> {
        return ktorHttpClient.post("fittings/delivery-times-for-single-product") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsEmployeeLimits(
        request: GetEmployeeFittingLimitsRequestDto
    ): BaseResponse<EmployeeFittingLimitsDto> {
        return ktorHttpClient.post("fittings/employee-limits") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsEmployeeLimitsForSingleProduct(
        request: GetEmployeeFittingLimitsForSingleProductRequestDto
    ): BaseResponse<EmployeeFittingLimitsDto> {
        return ktorHttpClient.post("fittings/employee-limits-for-single-product") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsHistoryFilters(
        request: FittingHistoryFiltersRequestDto
    ): BaseResponse<FittingHistoryFiltersResponseDto> {
        return ktorHttpClient.post("fittings/history/filters") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsHistory(
        cursor: String? = null,
        limit: Int? = null,
        request: FittingHistoryRequestDto
    ): BaseResponse<FittingHistoryResponseDto> {
        return ktorHttpClient.post("fittings/history") {
            appendQueryParameter("cursor", cursor)
            appendQueryParameter("limit", limit)
            setBody(request)
        }.body()
    }

    suspend fun fittingsCheckoutTags(
        request: FittingCheckoutTagsRequestDto
    ): BaseResponse<FittingCheckoutTagsResponseDto> {
        return ktorHttpClient.post("fittings/checkout-tags") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsByPairedUserIdForCheckout(
        pairedUserId: String
    ): BaseResponse<FittingForCheckoutResponseDto> {
        return ktorHttpClient.get("fittings/$pairedUserId/for-checkout").body()
    }

    suspend fun giftCards(): BaseResponse<GiftCardResponseDto> {
        return ktorHttpClient.get("gift-cards").body()
    }

    suspend fun giftCardsUpdate(): BaseResponse<JsonElement> {
        return ktorHttpClient.post("gift-cards/update").body()
    }

    suspend fun imageSearchRemoveBackground(
        request: RemoveBackgroundRequestDto
    ): BaseResponse<RemoveBackgroundResponseDto> {
        return ktorHttpClient.post("image-search/remove-background") {
            setBody(request)
        }.body()
    }

    suspend fun imageSearch(
        request: ImageSearchRequestDto
    ): BaseResponse<ImageSearchResponseDto> {
        return ktorHttpClient.post("image-search") {
            setBody(request)
        }.body()
    }

    suspend fun imagesUpload(
        request: ImagesUploadPostRequest
    ): BaseResponse<ImagesUploadResponseDto> {
        return ktorHttpClient.post("images/upload") {
            setBody(MultiPartFormDataContent(formData {
                appendFileParts("Files", request.files)
                appendFormPart("Category", request.category)
                appendFormPart("GeneratePreview", request.generatePreview)
            }))
        }.body()
    }

    suspend fun loyaltyLink(
        request: LinkCardRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("loyalty/link") {
            setBody(request)
        }.body()
    }

    suspend fun loyaltyVerifyLink(
        request: VerifyLinkCardRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("loyalty/verify-link") {
            setBody(request)
        }.body()
    }

    suspend fun loyaltyDelete(
        request: DeleteLinkedCardRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("loyalty/delete") {
            setBody(request)
        }.body()
    }

    suspend fun loyaltyCardInfo(): BaseResponse<CardInfoResponseDto> {
        return ktorHttpClient.get("loyalty/card-info").body()
    }

    suspend fun loyaltyCardTypes(): BaseResponse<CardTypeDtoItemsDto> {
        return ktorHttpClient.get("loyalty/card-types").body()
    }

    suspend fun motivation(
        request: MotivationRequestDto
    ): BaseResponse<MotivationResponseDto> {
        return ktorHttpClient.post("motivation") {
            setBody(request)
        }.body()
    }

    suspend fun notificationsFilters(
        request: EmployeeNotificationsFiltersRequestDto
    ): BaseResponse<EmployeeNotificationsFiltersResponseDto> {
        return ktorHttpClient.post("notifications/filters") {
            setBody(request)
        }.body()
    }

    suspend fun notifications(
        cursor: String? = null,
        limit: Int? = null,
        request: EmployeeNotificationsRequestDto
    ): BaseResponse<EmployeeNotificationsResponseDto> {
        return ktorHttpClient.post("notifications") {
            appendQueryParameter("cursor", cursor)
            appendQueryParameter("limit", limit)
            setBody(request)
        }.body()
    }

    suspend fun ordersCreateFromBasket(
        request: OrderCreationRequestDto
    ): BaseResponse<OrderResponseDto> {
        return ktorHttpClient.post("orders/create-from-basket") {
            setBody(request)
        }.body()
    }

    suspend fun ordersCreateFromFitting(
        request: OrderCreationFromFittingRequestDto
    ): BaseResponse<OrderResponseDto> {
        return ktorHttpClient.post("orders/create-from-fitting") {
            setBody(request)
        }.body()
    }

    suspend fun ordersCreateWithGiftCard(
        request: CreateOrderWithGiftCardRequestDto
    ): BaseResponse<OrderResponseDto> {
        return ktorHttpClient.post("orders/create-with-gift-card") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdChangeDelivery(
        orderId: String,
        request: OrderDeliveryChangeRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/change-delivery") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderId(
        orderId: String
    ): BaseResponse<OrderResponseWithBadgeDto> {
        return ktorHttpClient.get("orders/$orderId").body()
    }

    suspend fun orders(
        clientId: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<OrderResponseWithBadgeDtoItemsDto> {
        return ktorHttpClient.get("orders") {
            appendQueryParameter("clientId", clientId)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun ordersDeliveryTimes(
        request: BasketGetDeliveryTimesForOrderRequestDto
    ): BaseResponse<BasketCheckoutOrderResponseDto> {
        return ktorHttpClient.post("orders/delivery-times") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdDeliveryTimes(
        orderId: String,
        request: GetDeliveryIntervalsForExistingOrderRequestDto
    ): BaseResponse<GetDeliveryIntervalsForExistingOrderResponseDto> {
        return ktorHttpClient.post("orders/$orderId/delivery-times") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdPaymentLink(
        orderId: String
    ): BaseResponse<OrderPaymentLinkResponseDto> {
        return ktorHttpClient.get("orders/$orderId/payment-link").body()
    }

    suspend fun ordersByOrderIdReserveBonuses(
        orderId: String,
        request: OrderBonusReservationRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/reserve-bonuses") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdConfirmBonuses(
        orderId: String,
        request: OrderBonusReservationConfirmationRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/confirm-bonuses") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdPaymentCloudPayment(
        orderId: String,
        request: OrderStartPaymentViaCloudPaymentRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/payment/cloud-payment") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdPaymentSbp(
        orderId: String
    ): BaseResponse<OrderStartPaymentViaSbpResponseDto> {
        return ktorHttpClient.post("orders/$orderId/payment/sbp").body()
    }

    suspend fun ordersByOrderIdPaymentCloudPaymentConfirm(
        orderId: String,
        request: OrderConfirmPaymentViaCloudPaymentRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/payment/cloud-payment/confirm") {
            setBody(request)
        }.body()
    }

    suspend fun push(
        request: PushTokenDto
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("push") {
            setBody(request)
        }.body()
    }

    suspend fun internalSchedulerSendSms(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/sendSms").body()
    }

    suspend fun internalSchedulerSendEmail(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/sendEmail").body()
    }

    suspend fun internalSchedulerSendPush(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/sendPush").body()
    }

    suspend fun internalSchedulerSendAppIconBadge(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/sendAppIconBadge").body()
    }

    suspend fun internalSchedulerSendSignalrMessages(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/sendSignalrMessages").body()
    }

    suspend fun internalSchedulerSync1C(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/sync1c").body()
    }

    suspend fun internalSchedulerFillAlternativesInBaskets(
        request: FillAlternativesInBasketsRequestDto
    ): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/fillAlternativesInBaskets") {
            setBody(request)
        }.body()
    }

    suspend fun internalSchedulerRemoveOutofStockAlternativesFromBaskets(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/removeOutofStockAlternativesFromBaskets").body()
    }

    suspend fun internalSchedulerFittingSync(
        request: FittingSyncRequestDto
    ): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/fittingSync") {
            setBody(request)
        }.body()
    }

    suspend fun internalSchedulerBasketSync(
        request: BasketSyncRequestDto
    ): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/basketSync") {
            setBody(request)
        }.body()
    }

    suspend fun internalSchedulerSendProductBecameAvailableNotifications(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/send-product-became-available-notifications").body()
    }

    suspend fun internalSchedulerDigineticaFeed(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/diginetica/feed").body()
    }

    suspend fun internalSchedulerOrdersCheckSbpPaymentStatus(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/ordersCheckSbpPaymentStatus").body()
    }

    suspend fun internalSchedulerOrdersForbidPayment(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/ordersForbidPayment").body()
    }

    suspend fun internalSchedulerOrdersDeleteForbidden(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/ordersDeleteForbidden").body()
    }

    suspend fun internalSchedulerGetOrdersUpdatesFromAx(
        request: ClientSyncRequestDto
    ): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/getOrdersUpdatesFromAx") {
            setBody(request)
        }.body()
    }

    suspend fun internalSchedulerNotifyEmployeeAboutUnreadMessages(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/notifyEmployeeAboutUnreadMessages").body()
    }

    suspend fun internalSchedulerArchiveCompilations(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/archiveCompilations").body()
    }

    suspend fun internalSchedulerUpdateGiftCardTemplates(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/updateGiftCardTemplates").body()
    }

    suspend fun internalSchedulerRelevanceScores(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/relevanceScores").body()
    }

    suspend fun internalSchedulerMyClients(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/myClients").body()
    }

    suspend fun internalSchedulerPartialMyClients(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/partialMyClients").body()
    }

    suspend fun internalSchedulerCrm(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/crm").body()
    }

    suspend fun internalSchedulerActivityTracks(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/activity-tracks").body()
    }

    suspend fun internalSchedulerSale(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/sale").body()
    }

    suspend fun internalSchedulerOverdueTasks(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/overdueTasks").body()
    }

    suspend fun internalSchedulerRemindTasksCompletion(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/remindTasksCompletion").body()
    }

    suspend fun internalSchedulerImportCrmSalesAndOneCEvents(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/importCrmSalesAndOneCEvents").body()
    }

    suspend fun internalSchedulerSendStockNotifications(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/sendStockNotifications").body()
    }

    suspend fun internalSchedulerMigrationClientPreferredCategory(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/migration/clientPreferredCategory").body()
    }

    suspend fun internalSchedulerCrutchEmployeeLocation(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/crutch/employee-location").body()
    }

    suspend fun internalSchedulerMigrationMessagesFrontendGroupId(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/migration/messagesFrontendGroupId").body()
    }

    suspend fun internalSchedulerMigrationVpclientsBirthdays(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/migration/vpclientsBirthdays").body()
    }

    suspend fun internalSchedulerProcessEventMessages(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/processEventMessages").body()
    }

    suspend fun internalSchedulerClearExpiredDeepLink(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/clear-expired-deep-link").body()
    }

    suspend fun internalSchedulerResetEmployeeFilterSettings(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/reset-employee-filter-settings").body()
    }

    suspend fun internalSchedulerConfirmTaskLists(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/confirmTaskLists").body()
    }

    suspend fun internalSchedulerImportTaskLists(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/importTaskLists").body()
    }

    suspend fun internalSupportTeamDeleteChatByClientIdByEmployeeId(
        clientId: String,
        employeeId: String
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/support-team/deleteChat/$clientId/$employeeId").body()
    }

    suspend fun internalSupportTeamAddProductToAction(
        request: AddProductToActionRequestDto
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/support-team/add-product-to-action") {
            setBody(request)
        }.body()
    }

    suspend fun internalSupportTeamChangePhoneNumberByClientId(
        clientId: String
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/support-team/change-phone-number/$clientId").body()
    }

    suspend fun userCheckByPhoneByPhone(
        phone: String
    ): BaseResponse<CheckUserResponseDto> {
        return ktorHttpClient.get("user/check-by-phone/$phone").body()
    }

    suspend fun userProfileDelete(): BaseResponse<JsonElement> {
        return ktorHttpClient.post("user/profile/delete").body()
    }

    suspend fun userProfileUploadPhoto(
        request: UserProfileUploadPhotoPostRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("user/profile/upload-photo") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("file", request.file)
            }))
        }.body()
    }

    suspend fun userProfileDeletePhoto(): BaseResponse<JsonElement> {
        return ktorHttpClient.post("user/profile/delete-photo").body()
    }

    private fun HttpRequestBuilder.appendQueryParameter(
        name: String,
        value: Any?
    ) {
        when (value) {
            null -> Unit
            is Iterable<*> -> value.filterNotNull().forEach { item ->
                parameter(name, item.asParameterValue())
            }
            else -> parameter(name, value.asParameterValue())
        }
    }

    private fun FormBuilder.appendFormPart(
        name: String,
        value: Any?
    ) {
        when (value) {
            null -> Unit
            is Iterable<*> -> value.filterNotNull().forEach { item ->
                append(name, item.asParameterValue())
            }
            else -> append(name, value.asParameterValue())
        }
    }

    private fun FormBuilder.appendFilePart(
        name: String,
        value: ByteArray?
    ) {
        if (value == null) return
        append(
            name,
            value,
            Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=\"$name\"")
            }
        )
    }

    private fun FormBuilder.appendFileParts(
        name: String,
        values: List<ByteArray>?
    ) {
        values.orEmpty().forEachIndexed { index, value ->
            append(
                name,
                value,
                Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=\"${name}-${index}\"")
                }
            )
        }
    }

    private fun Any.asParameterValue(): String {
        return when (this) {
            is Enum<*> -> serialNameValue()
            else -> toString()
        }
    }

    private fun Enum<*>.serialNameValue(): String {
        return javaClass.getField(name).getAnnotation(SerialName::class.java)?.value ?: name
    }
}
