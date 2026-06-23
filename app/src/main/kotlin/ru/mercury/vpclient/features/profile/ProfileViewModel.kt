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
import ru.mercury.vpclient.features.profile_info.navigation.ProfileInfoRoute
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.intent.ProfileLoyaltyAddCardIntent
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardMode
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardModel
import ru.mercury.vpclient.features.profile_loyalty_code_sheet.intent.ProfileLoyaltyCodeIntent
import ru.mercury.vpclient.features.profile_loyalty_code_sheet.model.ProfileLoyaltyCodeModel
import ru.mercury.vpclient.features.profile_loyalty_info.navigation.ProfileLoyaltyInfoRoute
import ru.mercury.vpclient.features.profile_loyalty_qr.navigation.ProfileLoyaltyQrRoute
import ru.mercury.vpclient.features.profile_my_data.navigation.ProfileMyDataRoute
import ru.mercury.vpclient.features.profile_orders.navigation.ProfileOrdersRoute
import ru.mercury.vpclient.features.profile_privileges_sheet.intent.ProfilePrivilegeIntent
import ru.mercury.vpclient.features.profile_privileges_sheet.model.ProfilePrivilegesModel
import ru.mercury.vpclient.features.profile_qr.navigation.ProfileQrRoute
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.shared.data.CODE_RESEND_TIMER_DELAY
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardInfo
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.domain.interactor.CartInteractor
import ru.mercury.vpclient.shared.domain.interactor.LoyaltyInteractor
import ru.mercury.vpclient.shared.domain.interactor.ProductInteractor
import ru.mercury.vpclient.shared.domain.mapper.codeResendSecondsLeft
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.Companion.CODE_LENGTH
import ru.mercury.vpclient.shared.domain.usecase.ClientEntityFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authenticationInteractor: AuthenticationInteractor,
    private val clientEntityFlowUseCase: ClientEntityFlowUseCase,
    private val cartInteractor: CartInteractor,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val loyaltyInteractor: LoyaltyInteractor,
    private val productInteractor: ProductInteractor,
    private val settingsDataStore: SettingsDataStore
): ClientViewModel<ProfileIntent, ProfileModel, ProfileEvent>(ProfileModel()) {

    init {
        dispatch(ProfileIntent.CollectCartSize)
        dispatch(ProfileIntent.CollectClientEntity)
        dispatch(ProfileIntent.CollectActiveEmployee)
        dispatch(ProfileIntent.CollectViewHistoryProducts)
        dispatch(ProfileIntent.LoadCartData)
        dispatch(ProfileIntent.LoadViewHistoryProducts)
        dispatch(ProfileIntent.LoadLoyaltyCardInfo)
    }

    override fun dispatch(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.CollectCartSize -> {
                launch {
                    cartInteractor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is ProfileIntent.CollectClientEntity -> {
                launch {
                    clientEntityFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { clientEntity ->
                            reduce { it.copy(clientEntity = clientEntity) }
                        }
                }
            }
            is ProfileIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                            if (employee.isNotEmpty) {
                                dispatch(ProfileIntent.LoadCartData)
                            }
                        }
                }
            }
            is ProfileIntent.CollectViewHistoryProducts -> {
                launch {
                    productInteractor.viewHistoryProductsFlow()
                        .distinctUntilChanged()
                        .collectLatest { products ->
                            reduce { it.copy(viewHistoryProducts = products) }
                        }
                }
            }
            is ProfileIntent.LoadCartData -> {
                launch {
                    runCatching { cartInteractor.loadBasket() }

                    val badge = runCatching { cartInteractor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileIntent.LoadViewHistoryProducts -> {
                launch {
                    reduce { it.copy(isViewHistoryLoading = true) }
                    runCatching {
                        productInteractor.loadViewHistoryProducts(11)
                    }
                    reduce { it.copy(isViewHistoryLoading = false) }
                }
            }
            is ProfileIntent.LoadLoyaltyCardInfo -> {
                launch {
                    reduce { it.copy(isLoyaltyCardLoading = true) }
                    runCatching { loyaltyInteractor.loyaltyCardInfo() }
                        .onSuccess { loyaltyCardInfo ->
                            val visibleLoyaltyCardInfo = loyaltyCardInfo.takeIf { info ->
                                info.loyaltyCardNumber.isNotBlank()
                            }
                            reduce {
                                it.copy(
                                    loyaltyCardInfo = visibleLoyaltyCardInfo,
                                    isLoyaltyCardLoading = false
                                )
                            }
                            updateAlphaBankBanner(visibleLoyaltyCardInfo)
                        }
                        .onFailure {
                            reduce {
                                it.copy(
                                    loyaltyCardInfo = null,
                                    alphaBankBannerCardType = null,
                                    profilePrivilegesSheet = null,
                                    isLoyaltyCardLoading = false
                                )
                            }
                        }
                }
            }
            is ProfileIntent.NotificationClick -> return
            is ProfileIntent.AddLoyaltyCardClick -> {
                reduce {
                    it.copy(
                        loyaltyAddCardSheet = ProfileLoyaltyAddCardModel(
                            phone = formatPhoneForDisplay(it.clientEntity.phone)
                        )
                    )
                }
            }
            is ProfileIntent.LoyaltyCardQrClick -> {
                val qrCode = stateFlow.value.loyaltyCardInfo?.qrCode.orEmpty()
                if (qrCode.isNotBlank()) {
                    launch { MainEventManager.send(ProfileLoyaltyQrRoute(qrCode = qrCode)) }
                }
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
                            profilePrivilegesSheet = null
                        )
                    }
                }
            }
            is ProfileIntent.AlphaBankBannerMoreClick -> {
                reduce {
                    it.copy(
                        profilePrivilegesSheet = it.alphaBankBannerCardType?.let { cardType ->
                            ProfilePrivilegesModel(cardType = cardType)
                        }
                    )
                }
            }
            is ProfileIntent.DismissAlphaBankPrivilegesSheet -> {
                reduce { it.copy(profilePrivilegesSheet = null) }
            }
            is ProfileIntent.ProfilePrivilegesSheetIntent -> when (intent.intent) {
                is ProfilePrivilegeIntent.DismissRequest -> {
                    reduce { it.copy(profilePrivilegesSheet = null) }
                }
            }
            is ProfileIntent.MyDataClick -> launch { ProfileStackEventManager.send(ProfileMyDataRoute) }
            is ProfileIntent.PurchasesClick -> launch { ProfileStackEventManager.send(ProfileOrdersRoute) }
            is ProfileIntent.InformationClick -> launch { ProfileStackEventManager.send(ProfileInfoRoute) }
            is ProfileIntent.QrCodeClick -> launch { MainEventManager.send(ProfileQrRoute) }
            is ProfileIntent.ViewHistoryViewMoreClick -> return
            is ProfileIntent.ViewHistoryProductClick -> {
                launch { MainEventManager.send(DetailsRoute(intent.productId, openedFromCart = true)) }
            }
            is ProfileIntent.ShowLogoutDialog -> reduce { it.copy(isLogoutDialogVisible = true) }
            is ProfileIntent.DismissLogoutDialog -> reduce { it.copy(isLogoutDialogVisible = false) }
            is ProfileIntent.Logout -> {
                val job = launch {
                    reduce { it.copy(isLogoutDialogVisible = false) }
                    authenticationInteractor.logout()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(logoutJob = null) } }
                }
                reduce { it.copy(logoutJob = job) }
            }
            is ProfileIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is ProfileIntent.MessengerClick -> return
            is ProfileIntent.LoyaltyAddCardSheetIntent -> {
                when (val sheetIntent = intent.intent) {
                    is ProfileLoyaltyAddCardIntent.DismissRequest -> {
                        reduce { it.copy(loyaltyAddCardSheet = null) }
                    }
                    is ProfileLoyaltyAddCardIntent.ModeClick -> {
                        reduce { state ->
                            state.copy(
                                loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(
                                    mode = sheetIntent.mode,
                                    isPhoneErrorVisible = false,
                                    isCardErrorVisible = false
                                )
                            )
                        }
                    }
                    is ProfileLoyaltyAddCardIntent.PhoneChange -> {
                        reduce { state ->
                            state.copy(
                                loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(
                                    phone = formatPhoneForDisplay(normalizePhoneInput(sheetIntent.phone, maxDigits = 11)),
                                    isPhoneErrorVisible = false
                                )
                            )
                        }
                    }
                    is ProfileLoyaltyAddCardIntent.CardNumberChange -> {
                        reduce { state ->
                            state.copy(
                                loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(
                                    cardNumber = sheetIntent.cardNumber.filter(Char::isDigit),
                                    isCardErrorVisible = false
                                )
                            )
                        }
                    }
                    is ProfileLoyaltyAddCardIntent.ConfirmClick -> {
                        val sheet = stateFlow.value.loyaltyAddCardSheet
                        if (sheet != null) {
                            when (sheet.mode) {
                                ProfileLoyaltyAddCardMode.Phone -> {
                                    val phone = normalizePhoneInput(sheet.phone, maxDigits = 11)
                                    if (phone.length != 11) {
                                        reduce { state ->
                                            state.copy(loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(isPhoneErrorVisible = true))
                                        }
                                    } else {
                                        launch {
                                            reduce { state ->
                                                state.copy(loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(isLoading = true))
                                            }
                                            runCatching { loyaltyInteractor.linkLoyaltyCardByPhone(phone = phone) }
                                                .onSuccess { isNeedVerification ->
                                                    when {
                                                        isNeedVerification -> {
                                                            val startedAt = System.currentTimeMillis()
                                                            stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
                                                            reduce {
                                                                it.copy(
                                                                    loyaltyAddCardSheet = null,
                                                                    loyaltyCodeSheet = ProfileLoyaltyCodeModel(
                                                                        mode = ProfileLoyaltyAddCardMode.Phone,
                                                                        phone = phone,
                                                                        cardNumber = "",
                                                                        resendTimerStartedAt = startedAt,
                                                                        resendSecondsLeft = codeResendSecondsLeft(startedAt)
                                                                    )
                                                                )
                                                            }
                                                            dispatch(ProfileIntent.LoyaltyCodeSheetIntent(ProfileLoyaltyCodeIntent.StartResendTimerTicker))
                                                        }
                                                        else -> {
                                                            authenticationInteractor.currentUser()
                                                            reduce { it.copy(loyaltyAddCardSheet = null, loyaltyCodeSheet = null) }
                                                            dispatch(ProfileIntent.LoadLoyaltyCardInfo)
                                                        }
                                                    }
                                                }
                                                .onFailure { throwable ->
                                                    reduce { state ->
                                                        state.copy(
                                                            loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(
                                                                isLoading = false,
                                                                isPhoneErrorVisible = false
                                                            )
                                                        )
                                                    }
                                                    throwable.message
                                                        ?.takeIf(String::isNotBlank)
                                                        ?.let { message -> launch { send(ProfileEvent.SnackbarMessage(message)) } }
                                                }
                                        }
                                    }
                                }
                                ProfileLoyaltyAddCardMode.CardNumber -> {
                                    val cardNumber = sheet.cardNumber.trim()
                                    if (cardNumber.isNotEmpty()) {
                                        launch {
                                            reduce { state ->
                                                state.copy(loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(isLoading = true))
                                            }
                                            runCatching { loyaltyInteractor.linkLoyaltyCard(cardNumber = cardNumber) }
                                                .onSuccess {
                                                    val startedAt = System.currentTimeMillis()
                                                    stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
                                                    reduce {
                                                        it.copy(
                                                            loyaltyAddCardSheet = null,
                                                            loyaltyCodeSheet = ProfileLoyaltyCodeModel(
                                                                mode = ProfileLoyaltyAddCardMode.CardNumber,
                                                                phone = "",
                                                                cardNumber = cardNumber,
                                                                resendTimerStartedAt = startedAt,
                                                                resendSecondsLeft = codeResendSecondsLeft(startedAt)
                                                            )
                                                        )
                                                    }
                                                    dispatch(ProfileIntent.LoyaltyCodeSheetIntent(ProfileLoyaltyCodeIntent.StartResendTimerTicker))
                                                }
                                                .onFailure {
                                                    reduce { state ->
                                                        state.copy(
                                                            loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(
                                                                isLoading = false,
                                                                isCardErrorVisible = true
                                                            )
                                                        )
                                                    }
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is ProfileIntent.LoyaltyCodeSheetIntent -> {
                when (val sheetIntent = intent.intent) {
                    is ProfileLoyaltyCodeIntent.DismissRequest -> {
                        stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
                        reduce { it.copy(loyaltyCodeSheet = null) }
                    }
                    is ProfileLoyaltyCodeIntent.StartResendTimerTicker -> {
                        stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
                        val job = launch {
                            while (true) {
                                delay(CODE_RESEND_TIMER_DELAY.milliseconds)
                                val sheet = stateFlow.value.loyaltyCodeSheet ?: break
                                val resendSecondsLeft = codeResendSecondsLeft(sheet.resendTimerStartedAt)
                                if (resendSecondsLeft != sheet.resendSecondsLeft) {
                                    reduce { state ->
                                        state.copy(
                                            loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(
                                                resendSecondsLeft = resendSecondsLeft
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        reduce { state ->
                            state.copy(loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(resendTimerJob = job))
                        }
                    }
                    is ProfileLoyaltyCodeIntent.CodeChange -> {
                        reduce { state ->
                            state.copy(
                                loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(
                                    code = sheetIntent.code.filter(Char::isDigit).take(CODE_LENGTH),
                                    isCodeErrorVisible = false
                                )
                            )
                        }
                    }
                    is ProfileLoyaltyCodeIntent.ConfirmClick -> {
                        val sheet = stateFlow.value.loyaltyCodeSheet
                        if (sheet != null && sheet.isConfirmEnabled) {
                            launch {
                                reduce { state ->
                                    state.copy(
                                        loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(
                                            isLoading = true,
                                            isCodeErrorVisible = false
                                        )
                                    )
                                }
                                runCatching {
                                    when (sheet.mode) {
                                        ProfileLoyaltyAddCardMode.Phone -> {
                                            loyaltyInteractor.verifyLoyaltyCardByPhone(
                                                phone = sheet.phone,
                                                code = sheet.code
                                            )
                                        }
                                        ProfileLoyaltyAddCardMode.CardNumber -> {
                                            loyaltyInteractor.verifyLoyaltyCard(
                                                cardNumber = sheet.cardNumber,
                                                code = sheet.code
                                            )
                                        }
                                    }
                                    authenticationInteractor.currentUser()
                                }.onSuccess {
                                    stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
                                    reduce { it.copy(loyaltyCodeSheet = null) }
                                    dispatch(ProfileIntent.LoadLoyaltyCardInfo)
                                }.onFailure {
                                    reduce { state ->
                                        state.copy(
                                            loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(
                                                isLoading = false,
                                                isCodeErrorVisible = true
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is ProfileLoyaltyCodeIntent.ResendCodeClick -> {
                        val sheet = stateFlow.value.loyaltyCodeSheet
                        if (sheet != null && sheet.resendSecondsLeft <= 0 && !sheet.isResendLoading) {
                            launch {
                                reduce { state ->
                                    state.copy(loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(isResendLoading = true))
                                }
                                runCatching {
                                    when (sheet.mode) {
                                        ProfileLoyaltyAddCardMode.Phone -> loyaltyInteractor.linkLoyaltyCardByPhone(phone = sheet.phone)
                                        ProfileLoyaltyAddCardMode.CardNumber -> {
                                            loyaltyInteractor.linkLoyaltyCard(cardNumber = sheet.cardNumber)
                                            true
                                        }
                                    }
                                }.onSuccess { isNeedVerification ->
                                    when {
                                        isNeedVerification -> {
                                            val startedAt = System.currentTimeMillis()
                                            reduce { state ->
                                                state.copy(
                                                    loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(
                                                        isResendLoading = false,
                                                        resendTimerStartedAt = startedAt,
                                                        resendSecondsLeft = codeResendSecondsLeft(startedAt)
                                                    )
                                                )
                                            }
                                            dispatch(ProfileIntent.LoyaltyCodeSheetIntent(ProfileLoyaltyCodeIntent.StartResendTimerTicker))
                                        }
                                        else -> {
                                            authenticationInteractor.currentUser()
                                            reduce { it.copy(loyaltyCodeSheet = null) }
                                            dispatch(ProfileIntent.LoadLoyaltyCardInfo)
                                        }
                                    }
                                }.onFailure { throwable ->
                                    if (sheet.mode == ProfileLoyaltyAddCardMode.Phone) {
                                        throwable.message
                                            ?.takeIf(String::isNotBlank)
                                            ?.let { message -> launch { send(ProfileEvent.SnackbarMessage(message)) } }
                                    }
                                    reduce { state ->
                                        state.copy(
                                            loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(
                                                isResendLoading = false,
                                                isCodeErrorVisible = sheet.mode != ProfileLoyaltyAddCardMode.Phone
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateAlphaBankBanner(loyaltyCardInfo: LoyaltyCardInfo?) {
        val cardType = loyaltyCardInfo?.typeCard
        val availableCardType = when (cardType) {
            LoyaltyCardType.Black,
            LoyaltyCardType.Gold -> cardType
            LoyaltyCardType.Silver,
            null -> null
        }
        val closeTimestamp = settingsDataStore
            .getValue(PreferenceKey.DisclaimerCloseTimestamp)
            ?: 0L
        val showBanner = availableCardType != null &&
            System.currentTimeMillis() - closeTimestamp >= ALPHA_BANK_DISCLAIMER_HIDE_DURATION_MILLIS

        reduce {
            it.copy(
                alphaBankBannerCardType = availableCardType.takeIf { showBanner },
                profilePrivilegesSheet = it.profilePrivilegesSheet.takeIf { showBanner }
            )
        }
    }
}

private const val ALPHA_BANK_DISCLAIMER_HIDE_DURATION_MILLIS = 7L * 24L * 60L * 60L * 1000L
