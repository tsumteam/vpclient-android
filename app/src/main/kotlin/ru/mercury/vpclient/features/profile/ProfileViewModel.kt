package ru.mercury.vpclient.features.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.auth_welcome.navigation.WelcomeRoute
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.profile.event.ProfileEvent
import ru.mercury.vpclient.features.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.profile.model.ProfileModel
import ru.mercury.vpclient.features.notifications.navigation.NotificationsRoute
import ru.mercury.vpclient.features.profile_brands.navigation.ProfileBrandsRoute
import ru.mercury.vpclient.features.profile_info.navigation.ProfileInfoRoute
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardMode
import ru.mercury.vpclient.features.profile_loyalty_info.navigation.ProfileLoyaltyInfoRoute
import ru.mercury.vpclient.features.profile_loyalty_qr.navigation.ProfileLoyaltyQrRoute
import ru.mercury.vpclient.features.profile_my_data.navigation.ProfileMyDataRoute
import ru.mercury.vpclient.features.profile_orders.navigation.ProfileOrdersRoute
import ru.mercury.vpclient.features.profile_qr.navigation.ProfileQrRoute
import ru.mercury.vpclient.features.profile_root.event.ProfileRootEventManager
import ru.mercury.vpclient.features.profile_view_history.navigation.ProfileViewHistoryRoute
import ru.mercury.vpclient.shared.data.CODE_RESEND_TIMER_DELAY
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.codeResendSecondsLeft
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.domain.mapper.orEmpty
import ru.mercury.vpclient.shared.domain.usecase.ActivityCounterFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ActivityCountersUseCase
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.Companion.CODE_LENGTH
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogViewHistoryUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogViewHistoryUseCase.CatalogViewHistoryException
import ru.mercury.vpclient.shared.domain.usecase.ClientEntityFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CurrentUserUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.LinkLoyaltyCardByPhoneUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadFittingUseCase
import ru.mercury.vpclient.shared.domain.usecase.LogoutUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyCardInfoFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyCardInfoFlowUseCase.Companion.ALPHA_BANK_DISCLAIMER_HIDE_DURATION_MILLIS
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyCardInfoUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyCardInfoUseCase.LoyaltyCardInfoException
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyLinkUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyLinkUseCase.LoyaltyLinkException
import ru.mercury.vpclient.shared.domain.usecase.VerifyLoyaltyCardByPhoneUseCase
import ru.mercury.vpclient.shared.domain.usecase.VerifyLoyaltyCardUseCase
import ru.mercury.vpclient.shared.domain.usecase.ViewHistoryProductsFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val currentUserUseCase: CurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val clientEntityFlowUseCase: ClientEntityFlowUseCase,
    private val loadBasketUseCase: LoadBasketUseCase,
    private val loadFittingUseCase: LoadFittingUseCase,
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val activityCounterFlowUseCase: ActivityCounterFlowUseCase,
    private val activityCountersUseCase: ActivityCountersUseCase,
    private val catalogViewHistoryUseCase: CatalogViewHistoryUseCase,
    private val loyaltyCardInfoUseCase: LoyaltyCardInfoUseCase,
    private val loyaltyCardInfoFlowUseCase: LoyaltyCardInfoFlowUseCase,
    private val loyaltyLinkUseCase: LoyaltyLinkUseCase,
    private val linkLoyaltyCardByPhoneUseCase: LinkLoyaltyCardByPhoneUseCase,
    private val verifyLoyaltyCardUseCase: VerifyLoyaltyCardUseCase,
    private val verifyLoyaltyCardByPhoneUseCase: VerifyLoyaltyCardByPhoneUseCase,
    private val viewHistoryProductsFlowUseCase: ViewHistoryProductsFlowUseCase,
    private val settingsDataStore: SettingsDataStore
): ClientViewModel<ProfileIntent, ProfileModel, ProfileEvent>(ProfileModel()) {

    init {
        dispatch(ProfileIntent.CollectCartCount)
        dispatch(ProfileIntent.CollectFittingCount)
        dispatch(ProfileIntent.CollectClientEntity)
        dispatch(ProfileIntent.CollectActiveEmployee)
        dispatch(ProfileIntent.CollectNotificationCount)
        dispatch(ProfileIntent.CollectOrderCount)
        dispatch(ProfileIntent.CollectLoyaltyCardInfoEntity)
        dispatch(ProfileIntent.CollectViewHistoryProducts)
        dispatch(ProfileIntent.LoadActivityCounters)
        dispatch(ProfileIntent.LoadCatalogViewHistory)
        dispatch(ProfileIntent.LoadLoyaltyCardInfo)
    }

    override fun dispatch(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is ProfileIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(fittingCount = count) } }
                }
            }
            is ProfileIntent.CollectClientEntity -> {
                launch {
                    clientEntityFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { clientEntity ->
                            reduce { state ->
                                state.copy(
                                    clientEntity = clientEntity,
                                    loyaltyAddCardPhone = when {
                                        state.isLoyaltyAddCardSheetVisible &&
                                            state.loyaltyAddCardPhone.isBlank() -> {
                                            formatPhoneForDisplay(normalizePhoneInput(clientEntity.phone, maxDigits = 11))
                                        }
                                        else -> state.loyaltyAddCardPhone
                                    }
                                )
                            }
                        }
                }
            }
            is ProfileIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee -> reduce { it.copy(activeEmployee = employee) } }
                }
            }
            is ProfileIntent.CollectNotificationCount -> {
                launch {
                    activityCounterFlowUseCase(ActivityCounterType.CLIENT_NOTIFICATION)
                        .distinctUntilChanged()
                        .collectLatest { counter -> reduce { it.copy(notificationCount = counter.value) } }
                }
            }
            is ProfileIntent.CollectOrderCount -> {
                launch {
                    activityCounterFlowUseCase(ActivityCounterType.ORDER)
                        .distinctUntilChanged()
                        .collectLatest { counter -> reduce { it.copy(orderCount = counter.value) } }
                }
            }
            is ProfileIntent.CollectViewHistoryProducts -> {
                launch {
                    viewHistoryProductsFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { products -> reduce { it.copy(viewHistoryProducts = products) } }
                }
            }
            is ProfileIntent.CollectLoyaltyCardInfoEntity -> {
                launch {
                    loyaltyCardInfoFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { loyaltyCardInfo ->
                            val cardType = loyaltyCardInfo.typeCard.takeIf {
                                loyaltyCardInfo.loyaltyCardNumber.isNotBlank()
                            }
                            val availableCardType = when (cardType) {
                                LoyaltyCardType.Black,
                                LoyaltyCardType.Gold -> cardType
                                LoyaltyCardType.Silver,
                                null -> null
                            }
                            val closeTimestamp = settingsDataStore
                                .getValue(PreferenceKey.DisclaimerCloseTimestamp).orEmpty
                            val isAlphaBankBannerVisible = availableCardType != null &&
                                System.currentTimeMillis() - closeTimestamp >= ALPHA_BANK_DISCLAIMER_HIDE_DURATION_MILLIS

                            reduce {
                                it.copy(
                                    loyaltyCardInfoEntity = loyaltyCardInfo,
                                    alphaBankBannerCardType = availableCardType.takeIf { isAlphaBankBannerVisible },
                                    isProfilePrivilegesSheetVisible = it.isProfilePrivilegesSheetVisible && isAlphaBankBannerVisible
                                )
                            }
                        }
                }
            }
            is ProfileIntent.LoadActivityCounters -> launch { activityCountersUseCase(Unit) }
            is ProfileIntent.LoadCatalogViewHistory -> {
                val job = launch {
                    catalogViewHistoryUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(viewHistoryJob = null) } }
                }
                reduce { it.copy(viewHistoryJob = job) }
            }
            is ProfileIntent.LoadLoyaltyCardInfo -> {
                val job = launch {
                    loyaltyCardInfoUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(loyaltyCardInfoJob = null) } }
                }
                reduce { it.copy(loyaltyCardInfoJob = job) }
            }
            is ProfileIntent.NotificationClick -> launch { MainEventManager.send(NotificationsRoute) }
            is ProfileIntent.AddLoyaltyCardClick -> {
                reduce {
                    it.copy(
                        isLoyaltyAddCardSheetVisible = true,
                        loyaltyAddCardMode = ProfileLoyaltyAddCardMode.Phone,
                        loyaltyAddCardPhone = formatPhoneForDisplay(
                            normalizePhoneInput(it.clientEntity.phone, maxDigits = 11)
                        ),
                        loyaltyAddCardCardNumber = "",
                        isLoyaltyAddCardPhoneErrorVisible = false
                    )
                }
            }
            is ProfileIntent.LoyaltyCardQrClick -> {
                val qrCode = stateFlow.value.loyaltyCardInfoEntity.qrCode
                launch { MainEventManager.send(ProfileLoyaltyQrRoute(qrCode)) }
            }
            is ProfileIntent.LoyaltyCardMoreClick -> {
                launch { MainEventManager.send(ProfileLoyaltyInfoRoute) }
            }
            is ProfileIntent.AlphaBankBannerCloseClick -> {
                launch {
                    settingsDataStore.setValue(
                        key = PreferenceKey.DisclaimerCloseTimestamp,
                        value = System.currentTimeMillis()
                    )
                    reduce {
                        it.copy(
                            alphaBankBannerCardType = null,
                            isProfilePrivilegesSheetVisible = false
                        )
                    }
                }
            }
            is ProfileIntent.AlphaBankBannerMoreClick -> {
                reduce { it.copy(isProfilePrivilegesSheetVisible = it.alphaBankBannerCardType != null) }
            }
            is ProfileIntent.DismissAlphaBankPrivilegesSheet -> {
                reduce { it.copy(isProfilePrivilegesSheetVisible = false) }
            }
            is ProfileIntent.MyDataClick -> launch { ProfileRootEventManager.send(ProfileMyDataRoute) }
            is ProfileIntent.PurchasesClick -> launch { ProfileRootEventManager.send(ProfileOrdersRoute) }
            is ProfileIntent.InformationClick -> launch { ProfileRootEventManager.send(ProfileInfoRoute) }
            is ProfileIntent.QrCodeClick -> launch { MainEventManager.send(ProfileQrRoute) }
            is ProfileIntent.FavoriteBrandsClick -> launch { MainEventManager.send(ProfileBrandsRoute) }
            is ProfileIntent.ViewHistoryViewMoreClick -> {
                launch { ProfileRootEventManager.send(ProfileViewHistoryRoute) }
            }
            is ProfileIntent.ViewHistoryProductClick -> {
                launch { MainEventManager.send(DetailsRoute(intent.productId, openedFromCart = true)) }
            }
            is ProfileIntent.ShowLogoutDialog -> reduce { it.copy(isLogoutDialogVisible = true) }
            is ProfileIntent.DismissLogoutDialog -> reduce { it.copy(isLogoutDialogVisible = false) }
            is ProfileIntent.Logout -> {
                val job = launch {
                    reduce { it.copy(isLogoutDialogVisible = false) }
                    logoutUseCase(Unit).getOrThrow()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(logoutJob = null) } }
                }
                reduce { it.copy(logoutJob = job) }
            }
            is ProfileIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is ProfileIntent.MessengerClick -> return
            is ProfileIntent.DismissLoyaltyAddCardSheet -> {
                reduce { it.copy(isLoyaltyAddCardSheetVisible = false) }
            }
            is ProfileIntent.LoyaltyAddCardModeClick -> {
                reduce { state ->
                    state.copy(
                        loyaltyAddCardMode = intent.mode,
                        isLoyaltyAddCardPhoneErrorVisible = false
                    )
                }
            }
            is ProfileIntent.LoyaltyAddCardPhoneChange -> {
                reduce { state ->
                    state.copy(
                        loyaltyAddCardPhone = formatPhoneForDisplay(normalizePhoneInput(intent.phone, maxDigits = 11)),
                        isLoyaltyAddCardPhoneErrorVisible = false
                    )
                }
            }
            is ProfileIntent.LoyaltyAddCardCardNumberChange -> {
                reduce { state -> state.copy(loyaltyAddCardCardNumber = intent.cardNumber.filter(Char::isDigit)) }
            }
            is ProfileIntent.LoyaltyAddCardPhoneConfirmClick -> {
                val sheet = stateFlow.value
                if (sheet.isLoyaltyAddCardSheetVisible) {
                    val phone = normalizePhoneInput(sheet.loyaltyAddCardPhone, maxDigits = 11)
                    if (phone.length != 11) {
                        reduce { state -> state.copy(isLoyaltyAddCardPhoneErrorVisible = true) }
                    } else {
                        val job = launch {
                            runCatching { linkLoyaltyCardByPhoneUseCase(phone).getOrThrow() }
                                .onSuccess { isNeedVerification ->
                                    when {
                                        isNeedVerification -> {
                                            val startedAt = System.currentTimeMillis()
                                            stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                                            reduce {
                                                it.copy(
                                                    isLoyaltyAddCardSheetVisible = false,
                                                    isLoyaltyCodeSheetVisible = true,
                                                    loyaltyCodeMode = ProfileLoyaltyAddCardMode.Phone,
                                                    loyaltyCodePhone = phone,
                                                    loyaltyCodeCardNumber = "",
                                                    loyaltyCode = "",
                                                    isLoyaltyCodeLoading = false,
                                                    isLoyaltyCodeResendLoading = false,
                                                    isLoyaltyCodeErrorVisible = false,
                                                    loyaltyCodeResendTimerStartedAt = startedAt,
                                                    loyaltyCodeResendSecondsLeft = codeResendSecondsLeft(startedAt)
                                                )
                                            }
                                            dispatch(ProfileIntent.StartLoyaltyCodeResendTimerTicker)
                                        }
                                        else -> {
                                            currentUserUseCase(Unit).getOrThrow()
                                            stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                                            reduce { it.copy(isLoyaltyAddCardSheetVisible = false, isLoyaltyCodeSheetVisible = false) }
                                            dispatch(ProfileIntent.LoadLoyaltyCardInfo)
                                        }
                                    }
                                }
                                .onFailure { throwable ->
                                    reduce { state -> state.copy(isLoyaltyAddCardPhoneErrorVisible = false) }
                                    throwable.message
                                        ?.takeIf(String::isNotBlank)
                                        ?.let { message -> launch { send(ProfileEvent.SnackbarErrorMessage(message)) } }
                                }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion { reduce { it.copy(loyaltyAddCardJob = null) } }
                        }
                        reduce { it.copy(loyaltyAddCardJob = job) }
                    }
                }
            }
            is ProfileIntent.LoyaltyAddCardCardNumberConfirmClick -> {
                val sheet = stateFlow.value
                if (sheet.isLoyaltyAddCardSheetVisible) {
                    val cardNumber = sheet.loyaltyAddCardCardNumber.trim().takeIf(String::isNotEmpty) ?: return
                    val job = launch {
                        loyaltyLinkUseCase(cardNumber).getOrThrow()
                        val startedAt = System.currentTimeMillis()
                        stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                        reduce {
                            it.copy(
                                isLoyaltyAddCardSheetVisible = false,
                                isLoyaltyCodeSheetVisible = true,
                                loyaltyCodeMode = ProfileLoyaltyAddCardMode.CardNumber,
                                loyaltyCodePhone = "",
                                loyaltyCodeCardNumber = cardNumber,
                                loyaltyCode = "",
                                isLoyaltyCodeLoading = false,
                                isLoyaltyCodeResendLoading = false,
                                isLoyaltyCodeErrorVisible = false,
                                loyaltyCodeResendTimerStartedAt = startedAt,
                                loyaltyCodeResendSecondsLeft = codeResendSecondsLeft(startedAt)
                            )
                        }
                        dispatch(ProfileIntent.StartLoyaltyCodeResendTimerTicker)
                    }.also { launchedJob ->
                        launchedJob.invokeOnCompletion { reduce { it.copy(loyaltyAddCardJob = null) } }
                    }
                    reduce { it.copy(loyaltyAddCardJob = job) }
                }
            }
            is ProfileIntent.DismissLoyaltyCodeSheet -> {
                stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                reduce { it.copy(isLoyaltyCodeSheetVisible = false, loyaltyCodeResendTimerJob = null) }
            }
            is ProfileIntent.StartLoyaltyCodeResendTimerTicker -> {
                stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                val job = launch {
                    while (true) {
                        delay(CODE_RESEND_TIMER_DELAY.milliseconds)
                        val sheet = stateFlow.value
                        if (!sheet.isLoyaltyCodeSheetVisible) break
                        val resendSecondsLeft = codeResendSecondsLeft(sheet.loyaltyCodeResendTimerStartedAt)
                        if (resendSecondsLeft != sheet.loyaltyCodeResendSecondsLeft) {
                            reduce { state -> state.copy(loyaltyCodeResendSecondsLeft = resendSecondsLeft) }
                        }
                    }
                }
                reduce { state -> state.copy(loyaltyCodeResendTimerJob = job) }
            }
            is ProfileIntent.LoyaltyCodeChange -> {
                reduce { state ->
                    state.copy(
                        loyaltyCode = intent.code.filter(Char::isDigit).take(CODE_LENGTH),
                        isLoyaltyCodeErrorVisible = false
                    )
                }
            }
            is ProfileIntent.LoyaltyCodeConfirmClick -> {
                val sheet = stateFlow.value
                if (sheet.isLoyaltyCodeSheetVisible && sheet.loyaltyCode.length == CODE_LENGTH) {
                    launch {
                        reduce { state ->
                            state.copy(
                                isLoyaltyCodeLoading = true,
                                isLoyaltyCodeErrorVisible = false
                            )
                        }
                        runCatching {
                            when (sheet.loyaltyCodeMode) {
                                ProfileLoyaltyAddCardMode.Phone -> {
                                    verifyLoyaltyCardByPhoneUseCase(
                                        VerifyLoyaltyCardByPhoneUseCase.Params(
                                            phone = sheet.loyaltyCodePhone,
                                            code = sheet.loyaltyCode
                                        )
                                    ).getOrThrow()
                                }
                                ProfileLoyaltyAddCardMode.CardNumber -> {
                                    verifyLoyaltyCardUseCase(
                                        VerifyLoyaltyCardUseCase.Params(
                                            cardNumber = sheet.loyaltyCodeCardNumber,
                                            code = sheet.loyaltyCode
                                        )
                                    ).getOrThrow()
                                }
                            }
                            currentUserUseCase(Unit).getOrThrow()
                        }.onSuccess {
                            stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                            reduce { it.copy(isLoyaltyCodeSheetVisible = false, isLoyaltyCodeLoading = false, loyaltyCodeResendTimerJob = null) }
                            dispatch(ProfileIntent.LoadLoyaltyCardInfo)
                        }.onFailure {
                            reduce { state ->
                                state.copy(
                                    isLoyaltyCodeLoading = false,
                                    isLoyaltyCodeErrorVisible = true
                                )
                            }
                        }
                    }
                }
            }
            is ProfileIntent.LoyaltyCodeResendCodeClick -> {
                val sheet = stateFlow.value
                if (sheet.isLoyaltyCodeSheetVisible && sheet.loyaltyCodeResendSecondsLeft <= 0 && !sheet.isLoyaltyCodeResendLoading) {
                    launch {
                        reduce { state -> state.copy(isLoyaltyCodeResendLoading = true) }
                        runCatching {
                            when (sheet.loyaltyCodeMode) {
                                ProfileLoyaltyAddCardMode.Phone -> linkLoyaltyCardByPhoneUseCase(sheet.loyaltyCodePhone).getOrThrow()
                                ProfileLoyaltyAddCardMode.CardNumber -> {
                                    loyaltyLinkUseCase(sheet.loyaltyCodeCardNumber).getOrThrow()
                                    true
                                }
                            }
                        }.onSuccess { isNeedVerification ->
                            when {
                                isNeedVerification -> {
                                    val startedAt = System.currentTimeMillis()
                                    reduce { state ->
                                        state.copy(
                                            isLoyaltyCodeResendLoading = false,
                                            loyaltyCodeResendTimerStartedAt = startedAt,
                                            loyaltyCodeResendSecondsLeft = codeResendSecondsLeft(startedAt)
                                        )
                                    }
                                    dispatch(ProfileIntent.StartLoyaltyCodeResendTimerTicker)
                                }
                                else -> {
                                    currentUserUseCase(Unit).getOrThrow()
                                    stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                                    reduce { it.copy(isLoyaltyCodeSheetVisible = false, isLoyaltyCodeResendLoading = false, loyaltyCodeResendTimerJob = null) }
                                    dispatch(ProfileIntent.LoadLoyaltyCardInfo)
                                }
                            }
                        }.onFailure { throwable ->
                            if (sheet.loyaltyCodeMode == ProfileLoyaltyAddCardMode.Phone) {
                                throwable.message
                                    ?.takeIf(String::isNotBlank)
                                    ?.let { message -> launch { send(ProfileEvent.SnackbarErrorMessage(message)) } }
                            }
                            reduce { state ->
                                state.copy(
                                    isLoyaltyCodeResendLoading = false,
                                    isLoyaltyCodeErrorVisible = sheet.loyaltyCodeMode != ProfileLoyaltyAddCardMode.Phone
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CatalogViewHistoryException -> reduce { it.copy(viewHistoryJob = null) }
            is LoyaltyCardInfoException -> reduce { it.copy(loyaltyCardInfoJob = null) }
            is LoyaltyLinkException -> {
                reduce { it.copy(loyaltyAddCardJob = null) }
                launch { send(ProfileEvent.SnackbarTopErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(ProfileEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
