package ru.mercury.vpclient.features.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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
import ru.mercury.vpclient.features.profile_my_data.navigation.MyDataRoute
import ru.mercury.vpclient.features.profile_orders.navigation.ProfileOrdersRoute
import ru.mercury.vpclient.features.profile_qr.navigation.ProfileQrRoute
import ru.mercury.vpclient.features.profile_stack.event.ProfileStackEventManager
import ru.mercury.vpclient.shared.data.CODE_LENGTH
import ru.mercury.vpclient.shared.data.CODE_RESEND_MAX_TIME
import ru.mercury.vpclient.shared.data.CODE_RESEND_TIMER_DELAY
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.domain.interactor.Interactor
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val interactor: Interactor
): ClientViewModel<ProfileIntent, ProfileModel, ProfileEvent>(ProfileModel()) {

    init {
        dispatch(ProfileIntent.CollectCartSize)
        dispatch(ProfileIntent.CollectClientEntity)
        dispatch(ProfileIntent.CollectActiveEmployee)
        dispatch(ProfileIntent.CollectViewHistoryProducts)
        dispatch(ProfileIntent.LoadEmployees)
        dispatch(ProfileIntent.LoadCartData)
        dispatch(ProfileIntent.LoadViewHistoryProducts)
    }

    override fun dispatch(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.CollectCartSize -> {
                launch {
                    interactor.cartSize
                        .distinctUntilChanged()
                        .collectLatest { size ->
                            reduce { it.copy(cartSize = size) }
                        }
                }
            }
            is ProfileIntent.CollectClientEntity -> {
                launch {
                    interactor.clientEntityFlow
                        .distinctUntilChanged()
                        .collectLatest { clientEntity ->
                            reduce { it.copy(clientEntity = clientEntity) }
                        }
                }
            }
            is ProfileIntent.CollectActiveEmployee -> {
                launch {
                    interactor.employeeEntitiesFlow
                        .map { employees -> employees.firstOrNull { it.isActive } }
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee ?: EmployeeEntity.Empty) }
                            if (employee != null) {
                                dispatch(ProfileIntent.LoadCartData)
                            }
                        }
                }
            }
            is ProfileIntent.CollectViewHistoryProducts -> {
                launch {
                    interactor.viewHistoryProductsFlow()
                        .distinctUntilChanged()
                        .collectLatest { products ->
                            reduce { it.copy(viewHistoryProducts = products) }
                        }
                }
            }
            is ProfileIntent.LoadEmployees -> launch { runCatching { interactor.syncEmployees() } }
            is ProfileIntent.LoadCartData -> {
                launch {
                    runCatching { interactor.loadBasket() }

                    val badge = runCatching { interactor.cartBadge() }.getOrDefault(0)
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is ProfileIntent.LoadViewHistoryProducts -> {
                launch {
                    reduce { it.copy(isViewHistoryLoading = true) }
                    runCatching {
                        interactor.loadViewHistoryProducts(VIEW_HISTORY_PRODUCTS_LIMIT)
                    }
                    reduce { it.copy(isViewHistoryLoading = false) }
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
            is ProfileIntent.MyDataClick -> launch { ProfileStackEventManager.send(MyDataRoute) }
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
                    interactor.logout()
                    MainEventManager.send(WelcomeRoute)
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(logoutJob = null) } }
                }
                reduce { it.copy(logoutJob = job) }
            }
            is ProfileIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is ProfileIntent.FittingClick -> launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            is ProfileIntent.MessengerClick -> return
            is ProfileIntent.LoyaltyAddCardSheetIntent -> dispatchLoyaltyAddCardIntent(intent.intent)
            is ProfileIntent.LoyaltyCodeSheetIntent -> dispatchLoyaltyCodeIntent(intent.intent)
        }
    }

    private fun dispatchLoyaltyAddCardIntent(intent: ProfileLoyaltyAddCardIntent) {
        when (intent) {
            is ProfileLoyaltyAddCardIntent.DismissRequest -> {
                reduce { it.copy(loyaltyAddCardSheet = null) }
            }
            is ProfileLoyaltyAddCardIntent.ModeClick -> {
                reduce { state ->
                    state.copy(
                        loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(
                            mode = intent.mode,
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
                            phone = formatPhoneForDisplay(normalizePhoneInput(intent.phone, maxDigits = 11)),
                            isPhoneErrorVisible = false
                        )
                    )
                }
            }
            is ProfileLoyaltyAddCardIntent.CardNumberChange -> {
                reduce { state ->
                    state.copy(
                        loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(
                            cardNumber = intent.cardNumber.filter(Char::isDigit),
                            isCardErrorVisible = false
                        )
                    )
                }
            }
            is ProfileLoyaltyAddCardIntent.ConfirmClick -> confirmLoyaltyAddCard()
        }
    }

    private fun dispatchLoyaltyCodeIntent(intent: ProfileLoyaltyCodeIntent) {
        when (intent) {
            is ProfileLoyaltyCodeIntent.DismissRequest -> {
                stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
                reduce { it.copy(loyaltyCodeSheet = null) }
            }
            is ProfileLoyaltyCodeIntent.StartResendTimerTicker -> startLoyaltyCodeTimer()
            is ProfileLoyaltyCodeIntent.CodeChange -> {
                reduce { state ->
                    state.copy(
                        loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(
                            code = intent.code.filter(Char::isDigit).take(CODE_LENGTH),
                            isCodeErrorVisible = false
                        )
                    )
                }
            }
            is ProfileLoyaltyCodeIntent.ConfirmClick -> confirmLoyaltyCode()
            is ProfileLoyaltyCodeIntent.ResendCodeClick -> resendLoyaltyCode()
        }
    }

    private fun confirmLoyaltyAddCard() {
        val sheet = stateFlow.value.loyaltyAddCardSheet ?: return
        when (sheet.mode) {
            ProfileLoyaltyAddCardMode.Phone -> {
                val phone = normalizePhoneInput(sheet.phone, maxDigits = 11)
                if (phone.length != 11) {
                    reduce { state ->
                        state.copy(loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(isPhoneErrorVisible = true))
                    }
                    return
                }

                launch {
                    reduce { state ->
                        state.copy(loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(isLoading = true))
                    }
                    runCatching { interactor.linkLoyaltyCardByPhone(phone = phone) }
                        .onSuccess { isNeedVerification ->
                            when {
                                isNeedVerification -> showLoyaltyCodeSheet(
                                    mode = ProfileLoyaltyAddCardMode.Phone,
                                    phone = phone,
                                    cardNumber = ""
                                )
                                else -> {
                                    interactor.currentUser()
                                    reduce { it.copy(loyaltyAddCardSheet = null, loyaltyCodeSheet = null) }
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
            ProfileLoyaltyAddCardMode.CardNumber -> {
                val cardNumber = sheet.cardNumber.trim()
                if (cardNumber.isEmpty()) return

                launch {
                    reduce { state ->
                        state.copy(loyaltyAddCardSheet = state.loyaltyAddCardSheet?.copy(isLoading = true))
                    }
                    runCatching { interactor.linkLoyaltyCard(cardNumber = cardNumber) }
                        .onSuccess {
                            showLoyaltyCodeSheet(
                                mode = ProfileLoyaltyAddCardMode.CardNumber,
                                phone = "",
                                cardNumber = cardNumber
                            )
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

    private fun showLoyaltyCodeSheet(
        mode: ProfileLoyaltyAddCardMode,
        phone: String,
        cardNumber: String
    ) {
        val startedAt = System.currentTimeMillis()
        stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
        reduce {
            it.copy(
                loyaltyAddCardSheet = null,
                loyaltyCodeSheet = ProfileLoyaltyCodeModel(
                    mode = mode,
                    phone = phone,
                    cardNumber = cardNumber,
                    resendTimerStartedAt = startedAt,
                    resendSecondsLeft = profileLoyaltyResendSecondsLeft(startedAt)
                )
            )
        }
        dispatch(ProfileIntent.LoyaltyCodeSheetIntent(ProfileLoyaltyCodeIntent.StartResendTimerTicker))
    }

    private fun startLoyaltyCodeTimer() {
        stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
        val job = launch {
            while (true) {
                delay(CODE_RESEND_TIMER_DELAY.milliseconds)
                val sheet = stateFlow.value.loyaltyCodeSheet ?: break
                val resendSecondsLeft = profileLoyaltyResendSecondsLeft(sheet.resendTimerStartedAt)
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

    private fun confirmLoyaltyCode() {
        val sheet = stateFlow.value.loyaltyCodeSheet ?: return
        if (!sheet.isConfirmEnabled) return

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
                        interactor.verifyLoyaltyCardByPhone(
                            phone = sheet.phone,
                            code = sheet.code
                        )
                    }
                    ProfileLoyaltyAddCardMode.CardNumber -> {
                        interactor.verifyLoyaltyCard(
                            cardNumber = sheet.cardNumber,
                            code = sheet.code
                        )
                    }
                }
                interactor.currentUser()
            }.onSuccess {
                stateFlow.value.loyaltyCodeSheet?.resendTimerJob?.cancel()
                reduce { it.copy(loyaltyCodeSheet = null) }
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

    private fun resendLoyaltyCode() {
        val sheet = stateFlow.value.loyaltyCodeSheet ?: return
        if (sheet.resendSecondsLeft > 0 || sheet.isResendLoading) return

        launch {
            reduce { state ->
                state.copy(loyaltyCodeSheet = state.loyaltyCodeSheet?.copy(isResendLoading = true))
            }
            runCatching {
                when (sheet.mode) {
                    ProfileLoyaltyAddCardMode.Phone -> interactor.linkLoyaltyCardByPhone(phone = sheet.phone)
                    ProfileLoyaltyAddCardMode.CardNumber -> {
                        interactor.linkLoyaltyCard(cardNumber = sheet.cardNumber)
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
                                    resendSecondsLeft = profileLoyaltyResendSecondsLeft(startedAt)
                                )
                            )
                        }
                        dispatch(ProfileIntent.LoyaltyCodeSheetIntent(ProfileLoyaltyCodeIntent.StartResendTimerTicker))
                    }
                    else -> {
                        interactor.currentUser()
                        reduce { it.copy(loyaltyCodeSheet = null) }
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

    private companion object {
        private const val VIEW_HISTORY_PRODUCTS_LIMIT = 11
    }
}

private fun profileLoyaltyResendSecondsLeft(
    startedAt: Long,
    now: Long = System.currentTimeMillis()
): Int {
    val elapsedMillis = now - startedAt
    if (elapsedMillis >= CODE_RESEND_MAX_TIME) return 0
    val remainingMillis = CODE_RESEND_MAX_TIME - elapsedMillis
    return (remainingMillis / CODE_RESEND_TIMER_DELAY).toInt().coerceAtLeast(1)
}
