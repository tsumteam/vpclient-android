package ru.mercury.vpclient.shared.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.json.JsonElement
import ru.mercury.vpclient.shared.data.network.request.ActionPushRequest
import ru.mercury.vpclient.shared.data.network.request.AddProductToActionRequest
import ru.mercury.vpclient.shared.data.network.request.AddProductToFashionImageRequest
import ru.mercury.vpclient.shared.data.network.request.AddProductsInFoldersRequest
import ru.mercury.vpclient.shared.data.network.request.ApproveFittingDeliveryManualPickupRequest
import ru.mercury.vpclient.shared.data.network.request.ApproveFittingDeliveryRequest
import ru.mercury.vpclient.shared.data.network.request.ApproveFittingExpirationDateExtensionRequest
import ru.mercury.vpclient.shared.data.network.request.AuthenticationContinueLoginRequest
import ru.mercury.vpclient.shared.data.network.request.AuthenticationLoginRequest
import ru.mercury.vpclient.shared.data.network.request.AuthenticationRegisterRequest
import ru.mercury.vpclient.shared.data.network.request.AvailableColorsRequest
import ru.mercury.vpclient.shared.data.network.request.AvailableLocationsRequest
import ru.mercury.vpclient.shared.data.network.request.AvailableSizesRequest
import ru.mercury.vpclient.shared.data.network.request.AxLoyaltyCardAuthRequest
import ru.mercury.vpclient.shared.data.network.request.AxLoyaltyCardCalcAndReserveBonusPayRequest
import ru.mercury.vpclient.shared.data.network.request.AxLoyaltyCardCalcBonusAmountRequest
import ru.mercury.vpclient.shared.data.network.request.AxLoyaltyCardCheckPinRequest
import ru.mercury.vpclient.shared.data.network.request.AxLoyaltyCardRollBackBonusPayRequest
import ru.mercury.vpclient.shared.data.network.request.BarcodeScanRequest
import ru.mercury.vpclient.shared.data.network.request.BasketAddProductByBarcodeAndLocationIdRequest
import ru.mercury.vpclient.shared.data.network.request.BasketAddProductByBarcodeRequest
import ru.mercury.vpclient.shared.data.network.request.BasketAddProductFromCatalogWithSelectedRussianSizeRequest
import ru.mercury.vpclient.shared.data.network.request.BasketAddProductFromDetailedStocksRequest
import ru.mercury.vpclient.shared.data.network.request.BasketGetDeliveryTimesForFittingRequest
import ru.mercury.vpclient.shared.data.network.request.BasketGetDeliveryTimesForOrderRequest
import ru.mercury.vpclient.shared.data.network.request.BasketOperationRequest
import ru.mercury.vpclient.shared.data.network.request.BasketSyncRequest
import ru.mercury.vpclient.shared.data.network.request.BottomCategoriesRequest
import ru.mercury.vpclient.shared.data.network.request.CatalogBrandFavoriteRequest
import ru.mercury.vpclient.shared.data.network.request.ChangeActionRequest
import ru.mercury.vpclient.shared.data.network.request.ClientSyncRequest
import ru.mercury.vpclient.shared.data.network.request.CompilationCopyRequest
import ru.mercury.vpclient.shared.data.network.request.CompilationCreateRequest
import ru.mercury.vpclient.shared.data.network.request.CompilationLookSaveRequest
import ru.mercury.vpclient.shared.data.network.request.CompilationShareRequest
import ru.mercury.vpclient.shared.data.network.request.CreateClientAddressRequest
import ru.mercury.vpclient.shared.data.network.request.CreateLookFromFashionImageRequest
import ru.mercury.vpclient.shared.data.network.request.CreateOrderWithGiftCardRequest
import ru.mercury.vpclient.shared.data.network.request.CreatePaletteFolderRequest
import ru.mercury.vpclient.shared.data.network.request.DeleteClientAddressRequest
import ru.mercury.vpclient.shared.data.network.request.DeleteFashionImageRequest
import ru.mercury.vpclient.shared.data.network.request.DeleteLinkedCardRequest
import ru.mercury.vpclient.shared.data.network.request.DeleteMessageRequest
import ru.mercury.vpclient.shared.data.network.request.DeleteProductFromFashionImageRequest
import ru.mercury.vpclient.shared.data.network.request.DeleteProductsFromFolderRequest
import ru.mercury.vpclient.shared.data.network.request.DeliveryTimesForSingleProductRequest
import ru.mercury.vpclient.shared.data.network.request.DetailCardRequest
import ru.mercury.vpclient.shared.data.network.request.DetailedStocksRequest
import ru.mercury.vpclient.shared.data.network.request.DetermineGenderByClientIdRequest
import ru.mercury.vpclient.shared.data.network.request.DigineticaFilterValuesRequest
import ru.mercury.vpclient.shared.data.network.request.DigineticaFilteredProductsQuantityRequest
import ru.mercury.vpclient.shared.data.network.request.DigineticaFilteredProductsRequest
import ru.mercury.vpclient.shared.data.network.request.DigineticaFiltersRequest
import ru.mercury.vpclient.shared.data.network.request.EditFashionImageRequest
import ru.mercury.vpclient.shared.data.network.request.EditMessageRequest
import ru.mercury.vpclient.shared.data.network.request.EditPaletteFolderRequest
import ru.mercury.vpclient.shared.data.network.request.EmployeeCompilationProductsReportRequest
import ru.mercury.vpclient.shared.data.network.request.EmployeeEventsReportRequest
import ru.mercury.vpclient.shared.data.network.request.EmployeeNotificationsFiltersRequest
import ru.mercury.vpclient.shared.data.network.request.EmployeeNotificationsRequest
import ru.mercury.vpclient.shared.data.network.request.ExportCompilationStatisticsRequest
import ru.mercury.vpclient.shared.data.network.request.ExportEmployeeEventsRequest
import ru.mercury.vpclient.shared.data.network.request.ExportTasksRequest
import ru.mercury.vpclient.shared.data.network.request.FashionImageStatisticsReportRequest
import ru.mercury.vpclient.shared.data.network.request.FillAlternativesInBasketsRequest
import ru.mercury.vpclient.shared.data.network.request.FilterValuesRequest
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsQuantityRequest
import ru.mercury.vpclient.shared.data.network.request.FilteredProductsRequest
import ru.mercury.vpclient.shared.data.network.request.FiltersRequest
import ru.mercury.vpclient.shared.data.network.request.FittingCheckoutTagsRequest
import ru.mercury.vpclient.shared.data.network.request.FittingExternalPushRequest
import ru.mercury.vpclient.shared.data.network.request.FittingHistoryFiltersRequest
import ru.mercury.vpclient.shared.data.network.request.FittingHistoryRequest
import ru.mercury.vpclient.shared.data.network.request.FittingOperationRequest
import ru.mercury.vpclient.shared.data.network.request.FittingSyncRequest
import ru.mercury.vpclient.shared.data.network.request.GetCheckOutFlagsForExistingFittingRequest
import ru.mercury.vpclient.shared.data.network.request.GetCheckOutFlagsForSingleProductRequest
import ru.mercury.vpclient.shared.data.network.request.GetCheckOutFlagsRequest
import ru.mercury.vpclient.shared.data.network.request.GetDeliveryIntervalsForExistingFittingRequest
import ru.mercury.vpclient.shared.data.network.request.GetDeliveryIntervalsForExistingOrderRequest
import ru.mercury.vpclient.shared.data.network.request.GetEmployeeFittingLimitsForSingleProductRequest
import ru.mercury.vpclient.shared.data.network.request.GetEmployeeFittingLimitsRequest
import ru.mercury.vpclient.shared.data.network.request.GetListClientAddressForCheckoutRequest
import ru.mercury.vpclient.shared.data.network.request.GetListClientAddressRequest
import ru.mercury.vpclient.shared.data.network.request.ImageSearchRequest
import ru.mercury.vpclient.shared.data.network.request.LinkCardRequest
import ru.mercury.vpclient.shared.data.network.request.LoyaltyLinkByPhoneRequest
import ru.mercury.vpclient.shared.data.network.request.LoyaltyVerifyByPhoneRequest
import ru.mercury.vpclient.shared.data.network.request.MainScreenAnalyticsReportRequest
import ru.mercury.vpclient.shared.data.network.request.MessageGetRequest
import ru.mercury.vpclient.shared.data.network.request.MessengerActivityReportRequest
import ru.mercury.vpclient.shared.data.network.request.MotivationRequest
import ru.mercury.vpclient.shared.data.network.request.MyClientsRequest
import ru.mercury.vpclient.shared.data.network.request.NewArrivalsPushRequest
import ru.mercury.vpclient.shared.data.network.request.OrderBonusReservationConfirmationRequest
import ru.mercury.vpclient.shared.data.network.request.OrderBonusReservationRequest
import ru.mercury.vpclient.shared.data.network.request.OrderConfirmPaymentViaCloudPaymentRequest
import ru.mercury.vpclient.shared.data.network.request.OrderCreationFromFittingRequest
import ru.mercury.vpclient.shared.data.network.request.OrderCreationRequest
import ru.mercury.vpclient.shared.data.network.request.OrderDeliveryChangeRequest
import ru.mercury.vpclient.shared.data.network.request.OrderExternalPushRequest
import ru.mercury.vpclient.shared.data.network.request.OrderStartPaymentViaCloudPaymentRequest
import ru.mercury.vpclient.shared.data.network.request.PaymentAcknowledgeRequest
import ru.mercury.vpclient.shared.data.network.request.PaymentReconciliationRequest
import ru.mercury.vpclient.shared.data.network.request.ProfileOrdersSalesRequest
import ru.mercury.vpclient.shared.data.network.request.RemoveBackgroundRequest
import ru.mercury.vpclient.shared.data.network.request.ReserveRequest
import ru.mercury.vpclient.shared.data.network.request.ResetActivityCountersRequest
import ru.mercury.vpclient.shared.data.network.request.SaveClientCompilationMessageRequest
import ru.mercury.vpclient.shared.data.network.request.SaveCompilationLookMessageRequest
import ru.mercury.vpclient.shared.data.network.request.SaveMessageWithImagesRequest
import ru.mercury.vpclient.shared.data.network.request.SaveProductMessageRequest
import ru.mercury.vpclient.shared.data.network.request.TransferBasketToFittingRequest
import ru.mercury.vpclient.shared.data.network.request.TransferRequest
import ru.mercury.vpclient.shared.data.network.request.UpdateClientAddressRequest
import ru.mercury.vpclient.shared.data.network.request.UpdateMainScreenRequest
import ru.mercury.vpclient.shared.data.network.request.UploadCollageRequest
import ru.mercury.vpclient.shared.data.network.request.VerifyLinkCardRequest
import ru.mercury.vpclient.shared.data.network.response.ActionItemDtoResponse
import ru.mercury.vpclient.shared.data.network.response.ActionsResponse
import ru.mercury.vpclient.shared.data.network.response.ActiveClientResponse
import ru.mercury.vpclient.shared.data.network.response.AddressSuggestionItemsResponse
import ru.mercury.vpclient.shared.data.network.response.AggregatedActivityCounterResponse
import ru.mercury.vpclient.shared.data.network.response.ApproveFittingDeliveryManualPickupResponse
import ru.mercury.vpclient.shared.data.network.response.ApproveFittingDeliveryResponse
import ru.mercury.vpclient.shared.data.network.response.ApproveFittingExpirationDateExtensionResponse
import ru.mercury.vpclient.shared.data.network.response.AvailableColorsResponse
import ru.mercury.vpclient.shared.data.network.response.AvailableLocationsForProductResponse
import ru.mercury.vpclient.shared.data.network.response.AvailableLocationsResponse
import ru.mercury.vpclient.shared.data.network.response.AvailableSizesResponse
import ru.mercury.vpclient.shared.data.network.response.AxLoyaltyCardAuthResponse
import ru.mercury.vpclient.shared.data.network.response.AxLoyaltyCardCalcAndReserveBonusPayResponse
import ru.mercury.vpclient.shared.data.network.response.AxLoyaltyCardCalcBonusAmountResponse
import ru.mercury.vpclient.shared.data.network.response.AxLoyaltyCardCheckPinResponse
import ru.mercury.vpclient.shared.data.network.response.BaseResponse
import ru.mercury.vpclient.shared.data.network.response.BasketCheckoutOrderResponse
import ru.mercury.vpclient.shared.data.network.response.BasketForCheckoutResponse
import ru.mercury.vpclient.shared.data.network.response.BasketResponse
import ru.mercury.vpclient.shared.data.network.response.BlvLinkContentDtoResponse
import ru.mercury.vpclient.shared.data.network.response.CardInfoResponse
import ru.mercury.vpclient.shared.data.network.response.CardTypeItemsResponse
import ru.mercury.vpclient.shared.data.network.response.CatalogBrandsResponse
import ru.mercury.vpclient.shared.data.network.response.CatalogCategoriesBasicResponse
import ru.mercury.vpclient.shared.data.network.response.CatalogFashionImageCardItemsResponse
import ru.mercury.vpclient.shared.data.network.response.CatalogProductDetailCardV2Response
import ru.mercury.vpclient.shared.data.network.response.CatalogProductSearchCardDtoItemsResponse
import ru.mercury.vpclient.shared.data.network.response.CatalogScanHistoryDtoResponse
import ru.mercury.vpclient.shared.data.network.response.CheckOutAddressesDtoResponse
import ru.mercury.vpclient.shared.data.network.response.CheckUserResponse
import ru.mercury.vpclient.shared.data.network.response.ClientActivityInfoItemsResponse
import ru.mercury.vpclient.shared.data.network.response.ClientAddressDtoResponse
import ru.mercury.vpclient.shared.data.network.response.ClientAddressWithCoordinateItemsResponse
import ru.mercury.vpclient.shared.data.network.response.CompilationItemsResponse
import ru.mercury.vpclient.shared.data.network.response.CompilationsEmployeeLookUploadCollagePostResponse
import ru.mercury.vpclient.shared.data.network.response.CompilationsEmployeeLookUploadPhotoPostResponse
import ru.mercury.vpclient.shared.data.network.response.CompilationsEmployeeUploadPhotoPostResponse
import ru.mercury.vpclient.shared.data.network.response.CompilationsWithStatsByClientResponse
import ru.mercury.vpclient.shared.data.network.response.CompilationsWithStatsResponse
import ru.mercury.vpclient.shared.data.network.response.CreateCompilationResultDtoResponse
import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse
import ru.mercury.vpclient.shared.data.network.response.DetailedStocksResponse
import ru.mercury.vpclient.shared.data.network.response.DigineticaFilteredProductsResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeBadgesResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeFittingLimitsDtoResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeFittingOrderAccessFlagsResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeNotificationsFiltersResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeNotificationsResponse
import ru.mercury.vpclient.shared.data.network.response.EmployeeResponse
import ru.mercury.vpclient.shared.data.network.response.ExportCompilationStatisticsResponse
import ru.mercury.vpclient.shared.data.network.response.ExportEmployeeEventsResponse
import ru.mercury.vpclient.shared.data.network.response.ExportTasksResponse
import ru.mercury.vpclient.shared.data.network.response.FilterValuesResponse
import ru.mercury.vpclient.shared.data.network.response.FilteredProductsQuantityResponse
import ru.mercury.vpclient.shared.data.network.response.FilteredProductsResponse
import ru.mercury.vpclient.shared.data.network.response.FiltersResponse
import ru.mercury.vpclient.shared.data.network.response.FittingCheckoutTagsResponse
import ru.mercury.vpclient.shared.data.network.response.FittingDeliveryTimeResponse
import ru.mercury.vpclient.shared.data.network.response.FittingExternalPushResponseItemItemsResponse
import ru.mercury.vpclient.shared.data.network.response.FittingForCheckoutResponse
import ru.mercury.vpclient.shared.data.network.response.FittingHistoryFiltersResponse
import ru.mercury.vpclient.shared.data.network.response.FittingHistoryResponse
import ru.mercury.vpclient.shared.data.network.response.FittingResponse
import ru.mercury.vpclient.shared.data.network.response.GenderDtoResponse
import ru.mercury.vpclient.shared.data.network.response.GetCheckOutFlagsResponse
import ru.mercury.vpclient.shared.data.network.response.GetDeliveryIntervalsForExistingFittingResponse
import ru.mercury.vpclient.shared.data.network.response.GetDeliveryIntervalsForExistingOrderResponse
import ru.mercury.vpclient.shared.data.network.response.GiftCardResponse
import ru.mercury.vpclient.shared.data.network.response.ImageSearchResponse
import ru.mercury.vpclient.shared.data.network.response.ImagesUploadPostResponse
import ru.mercury.vpclient.shared.data.network.response.ImagesUploadResponse
import ru.mercury.vpclient.shared.data.network.response.ImportResultResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportAppendCollectionPostResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportImportCollectionPostResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportImportCrmTasksPostResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportImportLooksPostResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportImportMannequinsPostResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportImportNewArrivalsPostResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportImportSortSettingsPostResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportImportVpTasksPostResponse
import ru.mercury.vpclient.shared.data.network.response.InternalExcelImportUpdateNoPhotoPostResponse
import ru.mercury.vpclient.shared.data.network.response.LookProductsResponse
import ru.mercury.vpclient.shared.data.network.response.LookProductsWithStatsByClientResponse
import ru.mercury.vpclient.shared.data.network.response.LookProductsWithStatsResponse
import ru.mercury.vpclient.shared.data.network.response.LooksResponse
import ru.mercury.vpclient.shared.data.network.response.LooksResponseItemDtoResponse
import ru.mercury.vpclient.shared.data.network.response.LooksResponseItemItemsResponse
import ru.mercury.vpclient.shared.data.network.response.LooksWithStatsResponse
import ru.mercury.vpclient.shared.data.network.response.LoyaltyLinkByPhoneResponse
import ru.mercury.vpclient.shared.data.network.response.LoyaltyOperationResponse
import ru.mercury.vpclient.shared.data.network.response.MainDeliveryResponse
import ru.mercury.vpclient.shared.data.network.response.MessageReadGetResponse
import ru.mercury.vpclient.shared.data.network.response.MessageReadResponse
import ru.mercury.vpclient.shared.data.network.response.MessageResponse
import ru.mercury.vpclient.shared.data.network.response.MessageResponseItemsResponse
import ru.mercury.vpclient.shared.data.network.response.MessengerActivityReportResponse
import ru.mercury.vpclient.shared.data.network.response.MotivationResponse
import ru.mercury.vpclient.shared.data.network.response.MyClientsClientBadgesResponse
import ru.mercury.vpclient.shared.data.network.response.MyClientsFiltersResponse
import ru.mercury.vpclient.shared.data.network.response.MyClientsResponse
import ru.mercury.vpclient.shared.data.network.response.MyEmployeesResponse
import ru.mercury.vpclient.shared.data.network.response.NewLooksResponse
import ru.mercury.vpclient.shared.data.network.response.OrderExternalPushResponseItemItemsResponse
import ru.mercury.vpclient.shared.data.network.response.OrderPaymentLinkResponse
import ru.mercury.vpclient.shared.data.network.response.OrderResponse
import ru.mercury.vpclient.shared.data.network.response.OrderResponseWithBadgeDtoResponse
import ru.mercury.vpclient.shared.data.network.response.OrderResponseWithBadgeItemsResponse
import ru.mercury.vpclient.shared.data.network.response.OrderStartPaymentViaSbpResponse
import ru.mercury.vpclient.shared.data.network.response.PaletteFoldersResponse
import ru.mercury.vpclient.shared.data.network.response.PaletteProductsResponse
import ru.mercury.vpclient.shared.data.network.response.PaymentReconciliationResponse
import ru.mercury.vpclient.shared.data.network.response.ProfileOrdersSalesResponse
import ru.mercury.vpclient.shared.data.network.response.PushTokenResponse
import ru.mercury.vpclient.shared.data.network.response.RemoveBackgroundResponse
import ru.mercury.vpclient.shared.data.network.response.ReserveResponse
import ru.mercury.vpclient.shared.data.network.response.StringItemsResponse
import ru.mercury.vpclient.shared.data.network.response.SuggestionResponse
import ru.mercury.vpclient.shared.data.network.response.SuggestsSearchResponse
import ru.mercury.vpclient.shared.data.network.response.TasksImportResultResponse
import ru.mercury.vpclient.shared.data.network.response.TokenResponse
import ru.mercury.vpclient.shared.data.network.response.TransferResponse
import ru.mercury.vpclient.shared.data.network.response.UpdateMessageResponse
import ru.mercury.vpclient.shared.data.network.response.UpdateNoPhotoResultResponse
import ru.mercury.vpclient.shared.data.network.response.UserProfileUploadPhotoPostResponse
import ru.mercury.vpclient.shared.data.network.response.ViewHistoryDtoResponse
import ru.mercury.vpclient.shared.data.network.type.CatalogItemType
import ru.mercury.vpclient.shared.data.network.type.CompilationStatus
import ru.mercury.vpclient.shared.data.network.type.StatisticsType
import ru.mercury.vpclient.shared.domain.mapper.appendFilePart
import ru.mercury.vpclient.shared.domain.mapper.appendFileParts
import ru.mercury.vpclient.shared.domain.mapper.appendFormPart
import ru.mercury.vpclient.shared.domain.mapper.appendQueryParameter
import ru.mercury.vpclient.shared.domain.mapper.bodyAsBaseResponse
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

    suspend fun activityCountersByPairedUserId(
        pairedUserId: String
    ): BaseResponse<AggregatedActivityCounterResponse> {
        return ktorHttpClient.get("activity/counters/$pairedUserId").body()
    }

    suspend fun catalogViewHistory(
        limit: Int? = null,
        paginationToken: String? = null
    ): BaseResponse<CatalogProductSearchCardDtoItemsResponse> {
        return ktorHttpClient.post("catalog/view-history") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("paginationToken", paginationToken)
            setBody(emptyMap<String, String>())
        }.body()
    }

    suspend fun userProfileDelete(): BaseResponse<JsonElement> {
        return ktorHttpClient.post("user/profile/delete").body()
    }

    suspend fun actionsPost(
        request: ActionItemDtoResponse
    ): BaseResponse<ActionItemDtoResponse> {
        return ktorHttpClient.post("actions") {
            setBody(request)
        }.body()
    }

    suspend fun actionsGet(
        category: CatalogItemType? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ActionsResponse> {
        return ktorHttpClient.get("actions") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun actionsByActionIdNotifyAll(
        actionId: Int,
        request: ActionPushRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("actions/$actionId/notify-all") {
            setBody(request)
        }.body()
    }

    suspend fun actionsNewArrivalsNotifyAll(
        request: NewArrivalsPushRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("actions/new-arrivals/notify-all") {
            setBody(request)
        }.body()
    }

    suspend fun actionsActions(
        category: CatalogItemType? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ActionsResponse> {
        return ktorHttpClient.get("actions/actions") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun actionsNewCollections(
        category: CatalogItemType? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ActionsResponse> {
        return ktorHttpClient.get("actions/newCollections") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun actionsNewCollectionsVM(
        category: CatalogItemType? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ActionsResponse> {
        return ktorHttpClient.get("actions/newCollectionsVM") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun actionsByActionId(
        actionId: Int
    ): BaseResponse<ActionItemDtoResponse> {
        return ktorHttpClient.get("actions/$actionId").body()
    }

    suspend fun actionsNewLooks(
        category: CatalogItemType? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<NewLooksResponse> {
        return ktorHttpClient.get("actions/newLooks") {
            appendQueryParameter("category", category)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun activityByClientIdViewHistory(
        clientId: String,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ViewHistoryDtoResponse> {
        return ktorHttpClient.get("activity/$clientId/view-history") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun activityCountersByPairedUserIdReset(
        pairedUserId: String,
        request: ResetActivityCountersRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("activity/counters/$pairedUserId/reset") {
            setBody(request)
        }.body()
    }

    suspend fun addressSuggestion(
        limit: Int? = null,
        searchText: String? = null
    ): BaseResponse<AddressSuggestionItemsResponse> {
        return ktorHttpClient.get("address-suggestion") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("searchText", searchText)
        }.body()
    }

    suspend fun internalAnalyticsTeamUpdateMainScreen(
        request: UpdateMainScreenRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/analytics-team/update-main-screen") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaPushFitting(
        request: FittingExternalPushRequest
    ): BaseResponse<FittingExternalPushResponseItemItemsResponse> {
        return ktorHttpClient.post("/axapta/push/fitting") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaPushOrder(
        request: OrderExternalPushRequest
    ): BaseResponse<OrderExternalPushResponseItemItemsResponse> {
        return ktorHttpClient.post("/axapta/push/order") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaPaymentsReconciliation(
        request: PaymentReconciliationRequest
    ): BaseResponse<PaymentReconciliationResponse> {
        return ktorHttpClient.post("/axapta/payments-reconciliation") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardAuthClient(
        request: AxLoyaltyCardAuthRequest
    ): BaseResponse<AxLoyaltyCardAuthResponse> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardAuthClient") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardCheckPin(
        request: AxLoyaltyCardCheckPinRequest
    ): BaseResponse<AxLoyaltyCardCheckPinResponse> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardCheckPin") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardCalcBonusAmount(
        request: AxLoyaltyCardCalcBonusAmountRequest
    ): BaseResponse<AxLoyaltyCardCalcBonusAmountResponse> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardCalcBonusAmount") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardCalcAndReserveBonusPay(
        request: AxLoyaltyCardCalcAndReserveBonusPayRequest
    ): BaseResponse<AxLoyaltyCardCalcAndReserveBonusPayResponse> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardCalcAndReserveBonusPay") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyLoyaltyCardRollBackBonusPay(
        request: AxLoyaltyCardRollBackBonusPayRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/axapta/loyalty/loyaltyCardRollBackBonusPay") {
            setBody(request)
        }.body()
    }

    suspend fun axaptaLoyaltyBasketPaymAcknowledge(
        request: PaymentAcknowledgeRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/axapta/loyalty/basketPaymAcknowledge") {
            setBody(request)
        }.body()
    }

    suspend fun basket(
        request: BasketOperationRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket") {
            setBody(request)
        }.body()
    }

    suspend fun basketResponse(
        request: BasketOperationRequest
    ): HttpResponse {
        return ktorHttpClient.post("basket") {
            setBody(request)
        }
    }

    suspend fun basketAddProductByBarcode(
        request: BasketAddProductByBarcodeRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket/add-product-by-barcode") {
            setBody(request)
        }.body()
    }

    suspend fun basketAddProductByBarcodeAndLocationid(
        request: BasketAddProductByBarcodeAndLocationIdRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket/add-product-by-barcode-and-locationid") {
            setBody(request)
        }.body()
    }

    suspend fun basketAddProductFromDetailedStocks(
        request: BasketAddProductFromDetailedStocksRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket/add-product-from-detailed-stocks") {
            setBody(request)
        }.bodyAsBaseResponse(JsonElement.serializer())
    }

    suspend fun basketAddProductsFromCatalogWithSelectedRussianSize(
        request: BasketAddProductFromCatalogWithSelectedRussianSizeRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("basket/add-products-from-catalog-with-selected-russian-size") {
            setBody(request)
        }.bodyAsBaseResponse(JsonElement.serializer())
    }

    suspend fun basketByPairedUserId(
        pairedUserId: String
    ): BaseResponse<BasketResponse> {
        return ktorHttpClient.get("basket/$pairedUserId").body()
    }

    suspend fun basketChatGet(
        request: MessageGetRequest
    ): BaseResponse<MessageResponseItemsResponse> {
        return ktorHttpClient.post("basket/chat/get") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatStatus(
        request: MessageReadGetResponse
    ): BaseResponse<MessageReadResponse> {
        return ktorHttpClient.post("basket/chat/status") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatSend(
        request: SaveProductMessageRequest
    ): BaseResponse<MessageResponse> {
        return ktorHttpClient.post("basket/chat/send") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatSendCompilationLook(
        request: SaveCompilationLookMessageRequest
    ): BaseResponse<MessageResponse> {
        return ktorHttpClient.post("basket/chat/send/compilationLook") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatSendClientCompilation(
        request: SaveClientCompilationMessageRequest
    ): BaseResponse<MessageResponse> {
        return ktorHttpClient.post("basket/chat/send/clientCompilation") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatSendImages(
        request: SaveMessageWithImagesRequest
    ): BaseResponse<MessageResponse> {
        return ktorHttpClient.post("basket/chat/send/images") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatUpdate(
        request: UpdateMessageResponse
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("basket/chat/update") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatEdit(
        request: EditMessageRequest
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("basket/chat/edit") {
            setBody(request)
        }.body()
    }

    suspend fun basketChatDelete(
        request: DeleteMessageRequest
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("basket/chat/delete") {
            setBody(request)
        }.body()
    }

    suspend fun basketByPairedUserIdForCheckout(
        pairedUserId: String
    ): BaseResponse<BasketForCheckoutResponse> {
        return ktorHttpClient.get("basket/$pairedUserId/for-checkout").body()
    }

    suspend fun blvLinkIdByLinkId(
        linkId: String
    ): BaseResponse<BlvLinkContentDtoResponse> {
        return ktorHttpClient.get("blv-link/id/$linkId").body()
    }

    suspend fun blvLinkApproveDelivery(
        request: ApproveFittingDeliveryRequest
    ): BaseResponse<ApproveFittingDeliveryResponse> {
        return ktorHttpClient.post("blv-link/approve-delivery") {
            setBody(request)
        }.body()
    }

    suspend fun blvLinkApproveFittingExtension(
        request: ApproveFittingExpirationDateExtensionRequest
    ): BaseResponse<ApproveFittingExpirationDateExtensionResponse> {
        return ktorHttpClient.post("blv-link/approve-fitting-extension") {
            setBody(request)
        }.body()
    }

    suspend fun blvLinkApproveFittingManualPickup(
        request: ApproveFittingDeliveryManualPickupRequest
    ): BaseResponse<ApproveFittingDeliveryManualPickupResponse> {
        return ktorHttpClient.post("blv-link/approve-fitting-manual-pickup") {
            setBody(request)
        }.body()
    }

    suspend fun catalogActionIdSpecialOffer(): BaseResponse<Int> {
        return ktorHttpClient.post("catalog/action-id/special-offer").body()
    }

    suspend fun catalogBrands(): BaseResponse<CatalogBrandsResponse> {
        return ktorHttpClient.post("catalog/brands").body()
    }

    suspend fun catalogBrandsLike(
        request: CatalogBrandFavoriteRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("catalog/brands/like") {
            setBody(request)
        }.body()
    }

    suspend fun catalogBrandsUnlike(
        request: CatalogBrandFavoriteRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("catalog/brands/unlike") {
            setBody(request)
        }.body()
    }

    suspend fun catalogBrandsIsFavorite(
        brandId: Int,
        categoryId: Int
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("catalog/brands/is-favorite") {
            setBody(CatalogBrandFavoriteRequest(brandId = brandId, categoryId = categoryId))
        }.body()
    }

    suspend fun catalogAvailableColors(
        request: AvailableColorsRequest
    ): BaseResponse<AvailableColorsResponse> {
        return ktorHttpClient.post("catalog/available/colors") {
            setBody(request)
        }.body()
    }

    suspend fun catalogAvailableSizes(
        request: AvailableSizesRequest
    ): BaseResponse<AvailableSizesResponse> {
        return ktorHttpClient.post("catalog/available/sizes") {
            setBody(request)
        }.body()
    }

    suspend fun catalogAvailableLocations(
        request: AvailableLocationsRequest
    ): BaseResponse<AvailableLocationsResponse> {
        return ktorHttpClient.post("catalog/available/locations") {
            setBody(request)
        }.body()
    }

    suspend fun catalogNewArrivals(
        category: CatalogItemType? = null,
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
    ): BaseResponse<CatalogFashionImageCardItemsResponse> {
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
        request: BarcodeScanRequest
    ): BaseResponse<CatalogProductDetailCardV2Response> {
        return ktorHttpClient.post("catalog/scan") {
            setBody(request)
        }.body()
    }

    suspend fun catalogScanLocations(
        request: BarcodeScanRequest
    ): BaseResponse<AvailableLocationsForProductResponse> {
        return ktorHttpClient.post("catalog/scan/locations") {
            setBody(request)
        }.body()
    }

    suspend fun catalogScanHistory(
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<CatalogScanHistoryDtoResponse> {
        return ktorHttpClient.get("catalog/scan/history") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun catalogDetailedStocks(
        request: DetailedStocksRequest
    ): BaseResponse<DetailedStocksResponse> {
        return ktorHttpClient.post("catalog/detailed-stocks") {
            setBody(request)
        }.bodyAsBaseResponse(DetailedStocksResponse.serializer())
    }

    suspend fun catalogReserveBySerialid(
        request: ReserveRequest
    ): BaseResponse<ReserveResponse> {
        return ktorHttpClient.post("catalog/reserve-by-serialid") {
            setBody(request)
        }.body()
    }

    suspend fun catalogTransferBySerialid(
        request: TransferRequest
    ): BaseResponse<TransferResponse> {
        return ktorHttpClient.post("catalog/transfer-by-serialid") {
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextSuggestsDiginetica(
        limit: Int? = null,
        request: SuggestsSearchResponse
    ): BaseResponse<SuggestionResponse> {
        return ktorHttpClient.post("catalog/by-text/suggests/diginetica") {
            appendQueryParameter("limit", limit)
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextProductsDiginetica(
        limit: Int? = null,
        offset: Int? = null,
        request: DigineticaFilteredProductsRequest
    ): BaseResponse<DigineticaFilteredProductsResponse> {
        return ktorHttpClient.post("catalog/by-text/products/diginetica") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextFiltersDiginetica(
        request: DigineticaFiltersRequest
    ): BaseResponse<FiltersResponse> {
        return ktorHttpClient.post("catalog/by-text/filters/diginetica") {
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextFilterValues(
        request: DigineticaFilterValuesRequest
    ): BaseResponse<FilterValuesResponse> {
        return ktorHttpClient.post("catalog/by-text/filter-values") {
            setBody(request)
        }.body()
    }

    suspend fun catalogByTextFilterProductsQuantity(
        request: DigineticaFilteredProductsQuantityRequest
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
        request: GetListClientAddressRequest
    ): BaseResponse<ClientAddressWithCoordinateItemsResponse> {
        return ktorHttpClient.post("client/address/list") {
            setBody(request)
        }.body()
    }

    suspend fun clientAddressCheckout(
        request: GetListClientAddressForCheckoutRequest
    ): BaseResponse<CheckOutAddressesDtoResponse> {
        return ktorHttpClient.post("client/address/checkout") {
            setBody(request)
        }.body()
    }

    suspend fun clientAddressCreate(
        request: CreateClientAddressRequest
    ): BaseResponse<ClientAddressDtoResponse> {
        return ktorHttpClient.post("client/address/create") {
            setBody(request)
        }.body()
    }

    suspend fun clientAddressUpdate(
        request: UpdateClientAddressRequest
    ): BaseResponse<ClientAddressDtoResponse> {
        return ktorHttpClient.post("client/address/update") {
            setBody(request)
        }.body()
    }

    suspend fun clientAddressDelete(
        request: DeleteClientAddressRequest
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("client/address/delete") {
            setBody(request)
        }.body()
    }

    suspend fun codeGender(
        request: DetermineGenderByClientIdRequest
    ): BaseResponse<GenderDtoResponse> {
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
    ): BaseResponse<CompilationItemsResponse> {
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
    ): BaseResponse<LooksResponse> {
        return ktorHttpClient.get("compilations/client/$compilationId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsClientLookByLookId(
        lookId: Int
    ): BaseResponse<LookProductsResponse> {
        return ktorHttpClient.get("compilations/client/look/$lookId").body()
    }

    suspend fun compilationsClientLookByLookIdToBasket(
        lookId: Int
    ): BaseResponse<Boolean> {
        return ktorHttpClient.post("compilations/client/look/$lookId/to-basket").body()
    }

    suspend fun compilationsEmployeeCreate(
        request: CompilationCreateRequest
    ): BaseResponse<CreateCompilationResultDtoResponse> {
        return ktorHttpClient.post("compilations/employee/create") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeByCompilationIdEdit(
        compilationId: Int,
        request: CompilationCreateRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/edit") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeCopy(
        request: CompilationCopyRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/copy") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployee(
        limit: Int? = null,
        offset: Int? = null,
        pairedUserId: String? = null
    ): BaseResponse<CompilationItemsResponse> {
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
    ): BaseResponse<LooksResponse> {
        return ktorHttpClient.get("compilations/employee/$compilationId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookId(
        lookId: Int
    ): BaseResponse<LookProductsResponse> {
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
        request: CompilationShareRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/share") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeByCompilationIdShared(
        compilationId: Int
    ): BaseResponse<StringItemsResponse> {
        return ktorHttpClient.get("compilations/employee/$compilationId/shared").body()
    }

    suspend fun compilationsEmployeeLookByLookIdDelete(
        lookId: Int
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/delete").body()
    }

    suspend fun compilationsEmployeeLookCreate(
        request: CompilationLookSaveRequest
    ): BaseResponse<LooksResponseItemDtoResponse> {
        return ktorHttpClient.post("compilations/employee/look/create") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeLookCreateFromFashionImage(
        request: CreateLookFromFashionImageRequest
    ): BaseResponse<LooksResponseItemItemsResponse> {
        return ktorHttpClient.post("compilations/employee/look/create-from-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookIdEdit(
        lookId: Int,
        request: CompilationLookSaveRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/edit") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookIdUploadCollage(
        lookId: Int,
        request: CompilationsEmployeeLookUploadCollagePostResponse
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/look/$lookId/upload-collage") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("file", request.file)
            }))
        }.body()
    }

    suspend fun compilationsEmployeeLookByLookIdUploadPhoto(
        lookId: Int,
        request: CompilationsEmployeeLookUploadPhotoPostResponse
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
        request: UploadCollageRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/employee/$compilationId/upload-collage") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsEmployeeByCompilationIdUploadPhoto(
        compilationId: Int,
        request: CompilationsEmployeeUploadPhotoPostResponse
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
        request: CreatePaletteFolderRequest
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
        request: EditPaletteFolderRequest
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
    ): BaseResponse<PaletteProductsResponse> {
        return ktorHttpClient.get("compilations/palette/folders/$folderId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsPaletteFoldersAddItems(
        request: AddProductsInFoldersRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/palette/folders/add-items") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsPaletteFoldersByFolderIdDeleteItems(
        folderId: Int,
        request: DeleteProductsFromFolderRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("compilations/palette/folders/$folderId/delete-items") {
            setBody(request)
        }.body()
    }

    suspend fun compilationsPaletteFolders(): BaseResponse<PaletteFoldersResponse> {
        return ktorHttpClient.get("compilations/palette/folders").body()
    }

    suspend fun compilationsStatsClientByClientId(
        clientId: String,
        limit: Int? = null,
        offset: Int? = null,
        status: CompilationStatus? = null
    ): BaseResponse<CompilationsWithStatsByClientResponse> {
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
    ): BaseResponse<LooksWithStatsResponse> {
        return ktorHttpClient.get("compilations/stats/$compilationId/client/$clientId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsStatsLookByLookIdClientByClientId(
        clientId: String,
        lookId: Int
    ): BaseResponse<LookProductsWithStatsByClientResponse> {
        return ktorHttpClient.get("compilations/stats/look/$lookId/client/$clientId").body()
    }

    suspend fun compilationsStats(
        limit: Int? = null,
        offset: Int? = null,
        status: CompilationStatus? = null
    ): BaseResponse<CompilationsWithStatsResponse> {
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
    ): BaseResponse<LooksWithStatsResponse> {
        return ktorHttpClient.get("compilations/stats/$compilationId") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun compilationsStatsLookByLookId(
        lookId: Int,
        type: StatisticsType? = null
    ): BaseResponse<LookProductsWithStatsResponse> {
        return ktorHttpClient.get("compilations/stats/look/$lookId") {
            appendQueryParameter("type", type)
        }.body()
    }

    suspend fun internalContentTeamDeleteFashionImage(
        request: DeleteFashionImageRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/delete-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun internalContentTeamChangeAction(
        request: ChangeActionRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/change-action") {
            setBody(request)
        }.body()
    }

    suspend fun internalContentTeamAddProductToFashionImage(
        request: AddProductToFashionImageRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/add-product-to-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun internalContentTeamDeleteProductFromFashionImage(
        request: DeleteProductFromFashionImageRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/delete-product-from-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun internalContentTeamEditFashionImage(
        request: EditFashionImageRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("/internal/content-team/edit-fashion-image") {
            setBody(request)
        }.body()
    }

    suspend fun delivery(): BaseResponse<MainDeliveryResponse> {
        return ktorHttpClient.get("delivery").body()
    }

    suspend fun employeeByEmployeeIdMyClientsFilters(
        employeeId: String
    ): BaseResponse<MyClientsFiltersResponse> {
        return ktorHttpClient.get("employee/$employeeId/my-clients/filters").body()
    }

    suspend fun employeeByEmployeeIdMyClients(
        employeeId: String,
        request: MyClientsRequest
    ): BaseResponse<MyClientsResponse> {
        return ktorHttpClient.post("employee/$employeeId/my-clients") {
            setBody(request)
        }.body()
    }

    suspend fun employeeByEmployeeIdMyClientsByClientIdBadges(
        clientId: String,
        employeeId: String
    ): BaseResponse<MyClientsClientBadgesResponse> {
        return ktorHttpClient.get("employee/$employeeId/my-clients/$clientId/badges").body()
    }

    suspend fun employeeByEmployeeIdActiveClient(
        employeeId: String
    ): BaseResponse<ActiveClientResponse> {
        return ktorHttpClient.get("employee/$employeeId/active-client").body()
    }

    suspend fun employeeByEmployeeIdFittingOrderAccessFlags(
        employeeId: String
    ): BaseResponse<EmployeeFittingOrderAccessFlagsResponse> {
        return ktorHttpClient.get("employee/$employeeId/fitting-order-access-flags").body()
    }

    suspend fun employeeByEmployeeIdFittingLimits(
        employeeId: String
    ): BaseResponse<EmployeeFittingLimitsDtoResponse> {
        return ktorHttpClient.get("employee/$employeeId/fitting-limits").body()
    }

    suspend fun internalExcelImportImportCollection(
        request: InternalExcelImportImportCollectionPostResponse
    ): List<ImportResultResponse> {
        return ktorHttpClient.post("/internal/excelImport/importCollection") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportAppendCollection(
        fashionImageId: Int? = null,
        request: InternalExcelImportAppendCollectionPostResponse
    ): List<ImportResultResponse> {
        return ktorHttpClient.post("/internal/excelImport/appendCollection") {
            appendQueryParameter("fashionImageId", fashionImageId)
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportNewArrivals(
        deleteExisting: Boolean? = null,
        request: InternalExcelImportImportNewArrivalsPostResponse
    ): List<ImportResultResponse> {
        return ktorHttpClient.post("/internal/excelImport/importNewArrivals") {
            appendQueryParameter("deleteExisting", deleteExisting)
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportLooks(
        request: InternalExcelImportImportLooksPostResponse
    ): List<ImportResultResponse> {
        return ktorHttpClient.post("/internal/excelImport/importLooks") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportMannequins(
        request: InternalExcelImportImportMannequinsPostResponse
    ): List<ImportResultResponse> {
        return ktorHttpClient.post("/internal/excelImport/importMannequins") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportSortSettings(
        request: InternalExcelImportImportSortSettingsPostResponse
    ): List<ImportResultResponse> {
        return ktorHttpClient.post("/internal/excelImport/importSortSettings") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportVpTasks(
        request: InternalExcelImportImportVpTasksPostResponse
    ): TasksImportResultResponse {
        return ktorHttpClient.post("/internal/excelImport/importVpTasks") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportImportCrmTasks(
        request: InternalExcelImportImportCrmTasksPostResponse
    ): TasksImportResultResponse {
        return ktorHttpClient.post("/internal/excelImport/importCrmTasks") {
            setBody(MultiPartFormDataContent(formData {
                appendFilePart("formFile", request.formFile)
            }))
        }.body()
    }

    suspend fun internalExcelImportUpdateNoPhoto(
        request: InternalExcelImportUpdateNoPhotoPostResponse
    ): UpdateNoPhotoResultResponse {
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
        request: MessengerActivityReportRequest
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
        request: EmployeeEventsReportRequest
    ): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/employee-events") {
            setBody(request)
        }
    }

    suspend fun internalExportCsvFashionImageStats(
        request: FashionImageStatisticsReportRequest
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
        request: EmployeeCompilationProductsReportRequest
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
        request: MainScreenAnalyticsReportRequest
    ): HttpResponse {
        return ktorHttpClient.post("/internal/export/csv/main-screen-analytics") {
            setBody(request)
        }
    }

    suspend fun internalExportJson1CClientsActivity(): BaseResponse<ClientActivityInfoItemsResponse> {
        return ktorHttpClient.get("/internal/export/json/1C/clients-activity").body()
    }

    suspend fun internalExportJsonCompilationsStats(
        request: ExportCompilationStatisticsRequest
    ): BaseResponse<ExportCompilationStatisticsResponse> {
        return ktorHttpClient.post("/internal/export/json/compilations/stats") {
            setBody(request)
        }.body()
    }

    suspend fun internalExportJsonMessenger(
        request: MessengerActivityReportRequest
    ): BaseResponse<MessengerActivityReportResponse> {
        return ktorHttpClient.post("/internal/export/json/messenger") {
            setBody(request)
        }.body()
    }

    suspend fun internalExportJsonTasks(
        request: ExportTasksRequest
    ): BaseResponse<ExportTasksResponse> {
        return ktorHttpClient.post("/internal/export/json/tasks") {
            setBody(request)
        }.body()
    }

    suspend fun internalExportJsonEmployeeEvents(
        request: ExportEmployeeEventsRequest
    ): BaseResponse<ExportEmployeeEventsResponse> {
        return ktorHttpClient.post("/internal/export/json/employee-events") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsAddOperations(
        request: FittingOperationRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("fittings/add-operations") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsByPairedUserId(
        pairedUserId: String
    ): BaseResponse<FittingResponse> {
        return ktorHttpClient.get("fittings/$pairedUserId").body()
    }

    suspend fun fittingsTransferFromBasket(
        request: TransferBasketToFittingRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("fittings/transfer-from-basket") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsCheckoutFlags(
        request: GetCheckOutFlagsRequest
    ): BaseResponse<GetCheckOutFlagsResponse> {
        return ktorHttpClient.post("fittings/checkout-flags") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsCheckoutFlagsForExisingFitting(
        request: GetCheckOutFlagsForExistingFittingRequest
    ): BaseResponse<GetCheckOutFlagsResponse> {
        return ktorHttpClient.post("fittings/checkout-flags-for-exising-fitting") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsCheckoutFlagsForSingleProduct(
        request: GetCheckOutFlagsForSingleProductRequest
    ): BaseResponse<GetCheckOutFlagsResponse> {
        return ktorHttpClient.post("fittings/checkout-flags-for-single-product") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsDeliveryTimes(
        request: BasketGetDeliveryTimesForFittingRequest
    ): BaseResponse<FittingDeliveryTimeResponse> {
        return ktorHttpClient.post("fittings/delivery-times") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsDeliveryTimesForExisingDelivery(
        request: GetDeliveryIntervalsForExistingFittingRequest
    ): BaseResponse<GetDeliveryIntervalsForExistingFittingResponse> {
        return ktorHttpClient.post("fittings/delivery-times-for-exising-delivery") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsDeliveryTimesForSingleProduct(
        request: DeliveryTimesForSingleProductRequest
    ): BaseResponse<FittingDeliveryTimeResponse> {
        return ktorHttpClient.post("fittings/delivery-times-for-single-product") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsEmployeeLimits(
        request: GetEmployeeFittingLimitsRequest
    ): BaseResponse<EmployeeFittingLimitsDtoResponse> {
        return ktorHttpClient.post("fittings/employee-limits") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsEmployeeLimitsForSingleProduct(
        request: GetEmployeeFittingLimitsForSingleProductRequest
    ): BaseResponse<EmployeeFittingLimitsDtoResponse> {
        return ktorHttpClient.post("fittings/employee-limits-for-single-product") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsHistoryFilters(
        request: FittingHistoryFiltersRequest
    ): BaseResponse<FittingHistoryFiltersResponse> {
        return ktorHttpClient.post("fittings/history/filters") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsHistory(
        cursor: String? = null,
        limit: Int? = null,
        request: FittingHistoryRequest
    ): BaseResponse<FittingHistoryResponse> {
        return ktorHttpClient.post("fittings/history") {
            appendQueryParameter("cursor", cursor)
            appendQueryParameter("limit", limit)
            setBody(request)
        }.body()
    }

    suspend fun fittingsCheckoutTags(
        request: FittingCheckoutTagsRequest
    ): BaseResponse<FittingCheckoutTagsResponse> {
        return ktorHttpClient.post("fittings/checkout-tags") {
            setBody(request)
        }.body()
    }

    suspend fun fittingsByPairedUserIdForCheckout(
        pairedUserId: String
    ): BaseResponse<FittingForCheckoutResponse> {
        return ktorHttpClient.get("fittings/$pairedUserId/for-checkout").body()
    }

    suspend fun giftCards(): BaseResponse<GiftCardResponse> {
        return ktorHttpClient.get("gift-cards").body()
    }

    suspend fun giftCardsUpdate(): BaseResponse<JsonElement> {
        return ktorHttpClient.post("gift-cards/update").body()
    }

    suspend fun imageSearchRemoveBackground(
        request: RemoveBackgroundRequest
    ): BaseResponse<RemoveBackgroundResponse> {
        return ktorHttpClient.post("image-search/remove-background") {
            setBody(request)
        }.body()
    }

    suspend fun imageSearch(
        request: ImageSearchRequest
    ): BaseResponse<ImageSearchResponse> {
        return ktorHttpClient.post("image-search") {
            setBody(request)
        }.body()
    }

    suspend fun imagesUpload(
        request: ImagesUploadPostResponse
    ): BaseResponse<ImagesUploadResponse> {
        return ktorHttpClient.post("images/upload") {
            setBody(MultiPartFormDataContent(formData {
                appendFileParts("Files", request.files)
                appendFormPart("Category", request.category)
                appendFormPart("GeneratePreview", request.generatePreview)
            }))
        }.body()
    }

    suspend fun loyaltyLink(
        request: LinkCardRequest
    ): BaseResponse<LoyaltyOperationResponse> {
        return ktorHttpClient.post("loyalty/link") {
            setBody(request)
        }.body()
    }

    suspend fun loyaltyVerifyLink(
        request: VerifyLinkCardRequest
    ): BaseResponse<LoyaltyOperationResponse> {
        return ktorHttpClient.post("loyalty/verify-link") {
            setBody(request)
        }.body()
    }

    suspend fun loyaltyLinkByPhone(
        request: LoyaltyLinkByPhoneRequest
    ): BaseResponse<LoyaltyLinkByPhoneResponse> {
        return ktorHttpClient.post("loyalty/link-by-phone") {
            setBody(request)
        }.body()
    }

    suspend fun loyaltyVerifyByPhone(
        request: LoyaltyVerifyByPhoneRequest
    ): BaseResponse<LoyaltyOperationResponse> {
        return ktorHttpClient.post("loyalty/link-by-phone-continue") {
            setBody(request)
        }.body()
    }

    suspend fun loyaltyDelete(
        request: DeleteLinkedCardRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("loyalty/delete") {
            setBody(request)
        }.body()
    }

    suspend fun loyaltyCardInfo(): BaseResponse<CardInfoResponse> {
        return ktorHttpClient.get("loyalty/card-info").body()
    }

    suspend fun loyaltyCardTypes(): BaseResponse<CardTypeItemsResponse> {
        return ktorHttpClient.get("loyalty/card-types").body()
    }

    suspend fun motivation(
        request: MotivationRequest
    ): BaseResponse<MotivationResponse> {
        return ktorHttpClient.post("motivation") {
            setBody(request)
        }.body()
    }

    suspend fun notificationsFilters(
        request: EmployeeNotificationsFiltersRequest
    ): BaseResponse<EmployeeNotificationsFiltersResponse> {
        return ktorHttpClient.post("notifications/filters") {
            setBody(request)
        }.body()
    }

    suspend fun notifications(
        cursor: String? = null,
        limit: Int? = null,
        request: EmployeeNotificationsRequest
    ): BaseResponse<EmployeeNotificationsResponse> {
        return ktorHttpClient.post("notifications") {
            appendQueryParameter("cursor", cursor)
            appendQueryParameter("limit", limit)
            setBody(request)
        }.body()
    }

    suspend fun ordersCreateFromBasket(
        request: OrderCreationRequest
    ): BaseResponse<OrderResponse> {
        return ktorHttpClient.post("orders/create-from-basket") {
            setBody(request)
        }.body()
    }

    suspend fun ordersCreateFromFitting(
        request: OrderCreationFromFittingRequest
    ): BaseResponse<OrderResponse> {
        return ktorHttpClient.post("orders/create-from-fitting") {
            setBody(request)
        }.body()
    }

    suspend fun ordersCreateWithGiftCard(
        request: CreateOrderWithGiftCardRequest
    ): BaseResponse<OrderResponse> {
        return ktorHttpClient.post("orders/create-with-gift-card") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdChangeDelivery(
        orderId: String,
        request: OrderDeliveryChangeRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/change-delivery") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderId(
        orderId: String
    ): BaseResponse<OrderResponseWithBadgeDtoResponse> {
        return ktorHttpClient.get("orders/$orderId").body()
    }

    suspend fun orders(
        clientId: String? = null,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<OrderResponseWithBadgeItemsResponse> {
        return ktorHttpClient.get("orders") {
            appendQueryParameter("clientId", clientId)
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
        }.body()
    }

    suspend fun sales(
        request: ProfileOrdersSalesRequest,
        limit: Int? = null,
        offset: Int? = null
    ): BaseResponse<ProfileOrdersSalesResponse> {
        return ktorHttpClient.post("sales") {
            appendQueryParameter("limit", limit)
            appendQueryParameter("offset", offset)
            setBody(request)
        }.body()
    }

    suspend fun ordersDeliveryTimes(
        request: BasketGetDeliveryTimesForOrderRequest
    ): BaseResponse<BasketCheckoutOrderResponse> {
        return ktorHttpClient.post("orders/delivery-times") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdDeliveryTimes(
        orderId: String,
        request: GetDeliveryIntervalsForExistingOrderRequest
    ): BaseResponse<GetDeliveryIntervalsForExistingOrderResponse> {
        return ktorHttpClient.post("orders/$orderId/delivery-times") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdPaymentLink(
        orderId: String
    ): BaseResponse<OrderPaymentLinkResponse> {
        return ktorHttpClient.get("orders/$orderId/payment-link").body()
    }

    suspend fun ordersByOrderIdReserveBonuses(
        orderId: String,
        request: OrderBonusReservationRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/reserve-bonuses") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdConfirmBonuses(
        orderId: String,
        request: OrderBonusReservationConfirmationRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/confirm-bonuses") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdPaymentCloudPayment(
        orderId: String,
        request: OrderStartPaymentViaCloudPaymentRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/payment/cloud-payment") {
            setBody(request)
        }.body()
    }

    suspend fun ordersByOrderIdPaymentSbp(
        orderId: String
    ): BaseResponse<OrderStartPaymentViaSbpResponse> {
        return ktorHttpClient.post("orders/$orderId/payment/sbp").body()
    }

    suspend fun ordersByOrderIdPaymentCloudPaymentConfirm(
        orderId: String,
        request: OrderConfirmPaymentViaCloudPaymentRequest
    ): BaseResponse<JsonElement> {
        return ktorHttpClient.post("orders/$orderId/payment/cloud-payment/confirm") {
            setBody(request)
        }.body()
    }

    suspend fun push(
        request: PushTokenResponse
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
        request: FillAlternativesInBasketsRequest
    ): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/fillAlternativesInBaskets") {
            setBody(request)
        }.body()
    }

    suspend fun internalSchedulerRemoveOutofStockAlternativesFromBaskets(): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/removeOutofStockAlternativesFromBaskets").body()
    }

    suspend fun internalSchedulerFittingSync(
        request: FittingSyncRequest
    ): BaseResponse<String> {
        return ktorHttpClient.post("/internal/scheduler/fittingSync") {
            setBody(request)
        }.body()
    }

    suspend fun internalSchedulerBasketSync(
        request: BasketSyncRequest
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
        request: ClientSyncRequest
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
        request: AddProductToActionRequest
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
    ): BaseResponse<CheckUserResponse> {
        return ktorHttpClient.get("user/check-by-phone/$phone").body()
    }

    suspend fun userProfileUploadPhoto(
        request: UserProfileUploadPhotoPostResponse
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

}
