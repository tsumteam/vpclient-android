package ru.mercury.vpclient.features.checkout

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.checkout.event.CheckoutEvent
import ru.mercury.vpclient.features.checkout.intent.CheckoutIntent
import ru.mercury.vpclient.features.checkout.model.CheckoutModel
import ru.mercury.vpclient.features.checkout.model.CheckoutSource
import ru.mercury.vpclient.features.checkout.navigation.CheckoutRoute
import ru.mercury.vpclient.features.checkout_bank_card_sheet.model.CheckoutBankCardModel
import ru.mercury.vpclient.features.checkout_payment_result.model.CheckoutPaymentResultStatus
import ru.mercury.vpclient.features.checkout_payment_result.navigation.CheckoutPaymentResultRoute
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEvent
import ru.mercury.vpclient.features.fitting_addresses.navigation.FittingAddressesOrigin
import ru.mercury.vpclient.features.fitting_addresses.navigation.FittingAddressesRoute
import ru.mercury.vpclient.features.loyalty_add_card_sheet.model.LoyaltyAddCardMode
import ru.mercury.vpclient.features.main.navigation.MainRoute
import ru.mercury.vpclient.shared.data.CODE_RESEND_TIMER_DELAY
import ru.mercury.vpclient.shared.data.LOYALTY_PHONE_NUMBER_LENGTH
import ru.mercury.vpclient.shared.data.entity.CheckoutBankCardPaymentSystem
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutOnlinePaymentMethod
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutPaymentCardModel
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.CheckoutBonusType
import ru.mercury.vpclient.shared.data.network.type.OrderPaymentStatus
import ru.mercury.vpclient.shared.data.network.type.PaymentType
import ru.mercury.vpclient.shared.domain.mapper.codeResendSecondsLeft
import ru.mercury.vpclient.shared.domain.mapper.fittingTypeDto
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.domain.mapper.isValidBankCardExpirationDate
import ru.mercury.vpclient.shared.domain.mapper.isValidBankCardNumber
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.domain.mapper.selectedDayId
import ru.mercury.vpclient.shared.domain.mapper.selectedIntervalId
import ru.mercury.vpclient.shared.domain.mapper.title
import ru.mercury.vpclient.shared.domain.mapper.withCenterLoading
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.Companion.CODE_LENGTH
import ru.mercury.vpclient.shared.domain.usecase.BasketByPairedUserIdForCheckoutUseCase
import ru.mercury.vpclient.shared.domain.usecase.BasketByPairedUserIdForCheckoutUseCase.BasketByPairedUserIdForCheckoutException
import ru.mercury.vpclient.shared.domain.usecase.ClientAddressListUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientAddressesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ClientEntityFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CurrentUserUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCheckoutPaymentResultUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCheckoutPaymentResultUseCase.FittingCheckoutPaymentResultException
import ru.mercury.vpclient.shared.domain.usecase.FittingsByPairedUserIdForCheckoutUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingsByPairedUserIdForCheckoutUseCase.FittingsByPairedUserIdForCheckoutException
import ru.mercury.vpclient.shared.domain.usecase.LinkLoyaltyCardByPhoneUseCase
import ru.mercury.vpclient.shared.domain.usecase.LinkLoyaltyCardByPhoneUseCase.LinkLoyaltyCardByPhoneException
import ru.mercury.vpclient.shared.domain.usecase.LoadCheckoutDeliveryDataUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyLinkUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoyaltyLinkUseCase.LoyaltyLinkException
import ru.mercury.vpclient.shared.domain.usecase.OrderCheckoutDataUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdConfirmBonusesUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdConfirmBonusesUseCase.OrdersByOrderIdConfirmBonusesException
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdPaymentLinkUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdPaymentLinkUseCase.OrdersByOrderIdPaymentLinkException
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdPaymentSbpUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdPaymentSbpUseCase.OrdersByOrderIdPaymentSbpException
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdReserveBonusesUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdReserveBonusesUseCase.OrdersByOrderIdReserveBonusesException
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateFromBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateFromBasketUseCase.OrderPriceChangedException
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateFromBasketUseCase.OrdersCreateFromBasketException
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateFromFittingUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateFromFittingUseCase.OrdersCreateFromFittingException
import ru.mercury.vpclient.shared.domain.usecase.SbpAvailableBanksUseCase
import ru.mercury.vpclient.shared.domain.usecase.SbpAvailableBanksUseCase.SbpAvailableBanksException
import ru.mercury.vpclient.shared.domain.usecase.VerifyLoyaltyCardByPhoneUseCase
import ru.mercury.vpclient.shared.domain.usecase.VerifyLoyaltyCardByPhoneUseCase.VerifyLoyaltyCardByPhoneException
import ru.mercury.vpclient.shared.domain.usecase.VerifyLoyaltyCardUseCase
import ru.mercury.vpclient.shared.domain.usecase.VerifyLoyaltyCardUseCase.VerifyLoyaltyCardException
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.navigation.MainTab
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel(assistedFactory = CheckoutViewModel.Factory::class)
class CheckoutViewModel @AssistedInject constructor(
    @Assisted private val route: CheckoutRoute,
    private val fittingsByPairedUserIdForCheckoutUseCase: FittingsByPairedUserIdForCheckoutUseCase,
    private val basketByPairedUserIdForCheckoutUseCase: BasketByPairedUserIdForCheckoutUseCase,
    private val loadCheckoutDeliveryDataUseCase: LoadCheckoutDeliveryDataUseCase,
    private val clientEntityFlowUseCase: ClientEntityFlowUseCase,
    private val clientAddressesFlowUseCase: ClientAddressesFlowUseCase,
    private val clientAddressListUseCase: ClientAddressListUseCase,
    private val currentUserUseCase: CurrentUserUseCase,
    private val linkLoyaltyCardByPhoneUseCase: LinkLoyaltyCardByPhoneUseCase,
    private val loyaltyLinkUseCase: LoyaltyLinkUseCase,
    private val verifyLoyaltyCardByPhoneUseCase: VerifyLoyaltyCardByPhoneUseCase,
    private val verifyLoyaltyCardUseCase: VerifyLoyaltyCardUseCase,
    private val ordersCreateFromFittingUseCase: OrdersCreateFromFittingUseCase,
    private val ordersCreateFromBasketUseCase: OrdersCreateFromBasketUseCase,
    private val ordersByOrderIdReserveBonusesUseCase: OrdersByOrderIdReserveBonusesUseCase,
    private val ordersByOrderIdConfirmBonusesUseCase: OrdersByOrderIdConfirmBonusesUseCase,
    private val ordersByOrderIdPaymentLinkUseCase: OrdersByOrderIdPaymentLinkUseCase,
    private val ordersByOrderIdPaymentSbpUseCase: OrdersByOrderIdPaymentSbpUseCase,
    private val sbpAvailableBanksUseCase: SbpAvailableBanksUseCase,
    private val orderCheckoutDataUseCase: OrderCheckoutDataUseCase,
    private val fittingCheckoutPaymentResultUseCase: FittingCheckoutPaymentResultUseCase
): ClientViewModel<CheckoutIntent, CheckoutModel, CheckoutEvent>(CheckoutModel(source = route.source)) {

    init {
        dispatch(CheckoutIntent.CollectClientEntity)
        dispatch(CheckoutIntent.LoadData())
        if (route.source == CheckoutSource.Cart) {
            dispatch(CheckoutIntent.CollectClientAddresses)
            dispatch(CheckoutIntent.LoadClientAddresses)
            dispatch(CheckoutIntent.LoadDeliveryData)
        }
    }

    override fun dispatch(intent: CheckoutIntent) {
        when (intent) {
            is CheckoutIntent.CollectClientEntity -> {
                launch {
                    clientEntityFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { clientEntity ->
                            reduce { state -> state.copy(clientEntity = clientEntity) }
                        }
                }
            }
            is CheckoutIntent.CollectClientAddresses -> {
                launch {
                    clientAddressesFlowUseCase(Unit).collectLatest { addresses ->
                        reduce { state ->
                            val selectedId = state.selectedClientAddressId
                                ?.takeIf { id -> addresses.any { address -> address.id == id } }
                                ?: addresses.firstOrNull()?.id
                            state.copy(
                                clientAddresses = addresses,
                                selectedClientAddressId = selectedId
                            )
                        }
                    }
                }
            }
            is CheckoutIntent.LoadClientAddresses -> {
                stateFlow.value.addressListJob?.cancel()
                val job = launch {
                    clientAddressListUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state ->
                            when (state.addressListJob) {
                                launchedJob -> state.copy(addressListJob = null)
                                else -> state
                            }
                        }
                    }
                }
                reduce { it.copy(addressListJob = job) }
            }
            is CheckoutIntent.LoadDeliveryData -> {
                stateFlow.value.deliveryLoadJob?.cancel()
                val job = launch {
                    val state = stateFlow.value
                    val data = loadCheckoutDeliveryDataUseCase(
                        LoadCheckoutDeliveryDataUseCase.Params(
                            fittingType = state.selectedPlaceType.fittingTypeDto,
                            clientAddress = state.selectedClientAddress
                        )
                    ).getOrThrow()
                    reduce {
                        it.copy(
                            deliveryData = data,
                            selectedDayId = data.singleIntervals.selectedDayId(it.selectedDayId),
                            selectedIntervalId = data.singleIntervals.selectedIntervalId(it.selectedIntervalId)
                        )
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { model -> model.copy(deliveryLoadJob = null) } }
                }
                reduce { it.copy(deliveryLoadJob = job) }
            }
            is CheckoutIntent.SelectPlace -> {
                reduce { it.copy(selectedPlaceType = intent.placeType) }
                dispatch(CheckoutIntent.LoadDeliveryData)
            }
            is CheckoutIntent.SelectDeliveryDay -> reduce { it.copy(selectedDayId = intent.dayId) }
            is CheckoutIntent.SelectDeliveryInterval -> reduce { it.copy(selectedIntervalId = intent.intervalId) }
            is CheckoutIntent.OpenAddressSelection -> {
                launch {
                    val state = stateFlow.value
                    MainEventManager.send(
                        FittingAddressesRoute(
                            origin = FittingAddressesOrigin.Checkout,
                            selectedClientAddressId = state.selectedClientAddressId,
                            clientAddress = state.selectedClientAddress?.address
                        )
                    )
                }
                dispatch(CheckoutIntent.LoadClientAddresses)
            }
            is CheckoutIntent.ReceiveFittingAddressesEvent -> {
                when (val event = intent.event) {
                    is FittingAddressesEvent.SelectAddress -> {
                        if (event.origin is FittingAddressesOrigin.Checkout) {
                            reduce {
                                it.copy(
                                    clientAddresses = event.clientAddresses,
                                    selectedClientAddressId = event.selectedClientAddressId,
                                    selectedPlaceType = FittingConfirmationPlaceType.Home
                                )
                            }
                            dispatch(CheckoutIntent.LoadDeliveryData)
                        }
                    }
                }
            }
            is CheckoutIntent.AmountChangedContinueClick -> {
                reduce { it.copy(isCheckoutAmountChangedDialogVisible = false, amountChangedMessage = "") }
                dispatch(CheckoutIntent.LoadData())
                if (route.source == CheckoutSource.Cart) {
                    dispatch(CheckoutIntent.LoadDeliveryData)
                }
            }
            is CheckoutIntent.LoadData -> {
                val job = launch {
                    val data = when (route.source) {
                        CheckoutSource.Cart -> basketByPairedUserIdForCheckoutUseCase(intent.bonusType).getOrThrow()
                        CheckoutSource.Fitting -> fittingsByPairedUserIdForCheckoutUseCase(intent.bonusType).getOrThrow()
                        CheckoutSource.ExistingOrder -> {
                            val orderNumber = requireNotNull(route.orderNumber)
                            orderCheckoutDataUseCase(
                                OrderCheckoutDataUseCase.Params(
                                    orderNumber = orderNumber,
                                    bonusType = intent.bonusType
                                )
                            ).getOrThrow()
                        }
                    }
                    reduce {
                        it.copy(
                            fittingCheckoutData = data,
                            isPayWithBonuses = intent.bonusType == CheckoutBonusType.LOYALTY_CARD,
                            paymentOrderNumber = when (route.source) {
                                CheckoutSource.ExistingOrder -> route.orderNumber.orEmpty()
                                else -> ""
                            },
                            isPaymentExternalFlowStarted = false
                        )
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state -> state.copy(loadDataJob = null) }
                    }
                }
                reduce { it.copy(loadDataJob = job) }
            }
            is CheckoutIntent.CloseClick -> {
                launch { MainEventManager.send(BackRoute) }
            }
            is CheckoutIntent.PayOnlineClick -> {
                reduce {
                    it.copy(
                        paymentType = PaymentType.ONLINE_PAYMENT_CLIENT,
                        paymentOrderNumber = "",
                        isPaymentExternalFlowStarted = false
                    )
                }
            }
            is CheckoutIntent.PayAtCashDeskClick -> {
                reduce {
                    it.copy(
                        paymentType = PaymentType.CASHLESS_COURIER,
                        paymentOrderNumber = "",
                        isPaymentExternalFlowStarted = false
                    )
                }
            }
            is CheckoutIntent.PayWithBonusesClick -> {
                val bonusType = when {
                    stateFlow.value.isPayWithBonuses -> CheckoutBonusType.WITHOUT_BONUS
                    else -> CheckoutBonusType.LOYALTY_CARD
                }
                dispatch(CheckoutIntent.LoadData(bonusType))
            }
            is CheckoutIntent.AddLoyaltyCardClick -> {
                reduce {
                    it.copy(
                        isLoyaltyAddCardSheetVisible = true,
                        loyaltyAddCardMode = LoyaltyAddCardMode.Phone,
                        loyaltyAddCardPhone = formatPhoneForDisplay(
                            normalizePhoneInput(
                                raw = it.clientEntity.phone,
                                maxDigits = LOYALTY_PHONE_NUMBER_LENGTH
                            )
                        ),
                        loyaltyAddCardCardNumber = "",
                        isLoyaltyAddCardPhoneErrorVisible = false
                    )
                }
            }
            is CheckoutIntent.DismissLoyaltyAddCardSheet -> {
                reduce { it.copy(isLoyaltyAddCardSheetVisible = false) }
            }
            is CheckoutIntent.LoyaltyAddCardModeClick -> {
                reduce {
                    it.copy(
                        loyaltyAddCardMode = intent.mode,
                        isLoyaltyAddCardPhoneErrorVisible = false
                    )
                }
            }
            is CheckoutIntent.LoyaltyAddCardPhoneChange -> {
                reduce {
                    it.copy(
                        loyaltyAddCardPhone = formatPhoneForDisplay(
                            normalizePhoneInput(
                                raw = intent.phone,
                                maxDigits = LOYALTY_PHONE_NUMBER_LENGTH
                            )
                        ),
                        isLoyaltyAddCardPhoneErrorVisible = false
                    )
                }
            }
            is CheckoutIntent.LoyaltyAddCardCardNumberChange -> {
                reduce { it.copy(loyaltyAddCardCardNumber = intent.cardNumber.filter(Char::isDigit)) }
            }
            is CheckoutIntent.LoyaltyAddCardPhoneConfirmClick -> {
                val state = stateFlow.value
                val phone = normalizePhoneInput(
                    raw = state.loyaltyAddCardPhone,
                    maxDigits = LOYALTY_PHONE_NUMBER_LENGTH
                )
                when {
                    !state.isLoyaltyAddCardSheetVisible -> return
                    phone.length != LOYALTY_PHONE_NUMBER_LENGTH -> {
                        reduce { it.copy(isLoyaltyAddCardPhoneErrorVisible = true) }
                    }
                    else -> {
                        val job = launch {
                            val isNeedVerification = linkLoyaltyCardByPhoneUseCase(phone).getOrThrow()
                            when {
                                isNeedVerification -> {
                                    val startedAt = System.currentTimeMillis()
                                    stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                                    reduce {
                                        it.copy(
                                            isLoyaltyAddCardSheetVisible = false,
                                            isLoyaltyCodeSheetVisible = true,
                                            loyaltyCodeMode = LoyaltyAddCardMode.Phone,
                                            loyaltyCodePhone = phone,
                                            loyaltyCodeCardNumber = "",
                                            loyaltyCode = "",
                                            isLoyaltyCodeErrorVisible = false,
                                            loyaltyCodeResendTimerStartedAt = startedAt,
                                            loyaltyCodeResendSecondsLeft = codeResendSecondsLeft(startedAt)
                                        )
                                    }
                                    dispatch(CheckoutIntent.StartLoyaltyCodeResendTimerTicker)
                                }
                                else -> {
                                    currentUserUseCase(Unit).getOrThrow()
                                    reduce {
                                        it.copy(
                                            isLoyaltyAddCardSheetVisible = false,
                                            isLoyaltyCodeSheetVisible = false
                                        )
                                    }
                                    dispatch(CheckoutIntent.LoadData())
                                }
                            }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(loyaltyAddCardJob = null) }
                            }
                        }
                        reduce { it.copy(loyaltyAddCardJob = job) }
                    }
                }
            }
            is CheckoutIntent.LoyaltyAddCardCardNumberConfirmClick -> {
                val state = stateFlow.value
                val cardNumber = state.loyaltyAddCardCardNumber.trim()
                when {
                    !state.isLoyaltyAddCardSheetVisible || cardNumber.isEmpty() -> return
                    else -> {
                        val job = launch {
                            loyaltyLinkUseCase(cardNumber).getOrThrow()
                            val startedAt = System.currentTimeMillis()
                            stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                            reduce {
                                it.copy(
                                    isLoyaltyAddCardSheetVisible = false,
                                    isLoyaltyCodeSheetVisible = true,
                                    loyaltyCodeMode = LoyaltyAddCardMode.CardNumber,
                                    loyaltyCodePhone = "",
                                    loyaltyCodeCardNumber = cardNumber,
                                    loyaltyCode = "",
                                    isLoyaltyCodeErrorVisible = false,
                                    loyaltyCodeResendTimerStartedAt = startedAt,
                                    loyaltyCodeResendSecondsLeft = codeResendSecondsLeft(startedAt)
                                )
                            }
                            dispatch(CheckoutIntent.StartLoyaltyCodeResendTimerTicker)
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(loyaltyAddCardJob = null) }
                            }
                        }
                        reduce { it.copy(loyaltyAddCardJob = job) }
                    }
                }
            }
            is CheckoutIntent.DismissLoyaltyCodeSheet -> {
                stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                reduce {
                    it.copy(
                        isLoyaltyCodeSheetVisible = false,
                        loyaltyCodeResendTimerJob = null
                    )
                }
            }
            is CheckoutIntent.StartLoyaltyCodeResendTimerTicker -> {
                stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                val job = launch {
                    while (true) {
                        delay(CODE_RESEND_TIMER_DELAY.milliseconds)
                        val state = stateFlow.value
                        if (!state.isLoyaltyCodeSheetVisible) break
                        val secondsLeft = codeResendSecondsLeft(state.loyaltyCodeResendTimerStartedAt)
                        when {
                            secondsLeft != state.loyaltyCodeResendSecondsLeft -> {
                                reduce { it.copy(loyaltyCodeResendSecondsLeft = secondsLeft) }
                            }
                        }
                    }
                }
                reduce { it.copy(loyaltyCodeResendTimerJob = job) }
            }
            is CheckoutIntent.LoyaltyCodeChange -> {
                reduce {
                    it.copy(
                        loyaltyCode = intent.code.filter(Char::isDigit).take(CODE_LENGTH),
                        isLoyaltyCodeErrorVisible = false
                    )
                }
            }
            is CheckoutIntent.LoyaltyCodeConfirmClick -> {
                val state = stateFlow.value
                when {
                    !state.isLoyaltyCodeSheetVisible || state.loyaltyCode.length != CODE_LENGTH -> return
                    else -> {
                        val job = launch {
                            when (state.loyaltyCodeMode) {
                                LoyaltyAddCardMode.Phone -> {
                                    val params = VerifyLoyaltyCardByPhoneUseCase.Params(
                                        phone = state.loyaltyCodePhone,
                                        code = state.loyaltyCode
                                    )
                                    verifyLoyaltyCardByPhoneUseCase(params).getOrThrow()
                                }
                                LoyaltyAddCardMode.CardNumber -> {
                                    val params = VerifyLoyaltyCardUseCase.Params(
                                        cardNumber = state.loyaltyCodeCardNumber,
                                        code = state.loyaltyCode
                                    )
                                    verifyLoyaltyCardUseCase(params).getOrThrow()
                                }
                            }
                            currentUserUseCase(Unit).getOrThrow()
                            stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                            reduce {
                                it.copy(
                                    isLoyaltyCodeSheetVisible = false,
                                    loyaltyCodeResendTimerJob = null
                                )
                            }
                            dispatch(CheckoutIntent.LoadData())
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(loyaltyCodeConfirmJob = null) }
                            }
                        }
                        reduce { it.copy(loyaltyCodeConfirmJob = job) }
                    }
                }
            }
            is CheckoutIntent.LoyaltyCodeResendCodeClick -> {
                val state = stateFlow.value
                when {
                    !state.isLoyaltyCodeSheetVisible ||
                        state.loyaltyCodeResendSecondsLeft > 0 ||
                        state.isLoyaltyCodeResendLoading -> return
                    else -> {
                        val job = launch {
                            val isNeedVerification = when (state.loyaltyCodeMode) {
                                LoyaltyAddCardMode.Phone -> {
                                    linkLoyaltyCardByPhoneUseCase(state.loyaltyCodePhone).getOrThrow()
                                }
                                LoyaltyAddCardMode.CardNumber -> {
                                    loyaltyLinkUseCase(state.loyaltyCodeCardNumber).getOrThrow()
                                    true
                                }
                            }
                            when {
                                isNeedVerification -> {
                                    val startedAt = System.currentTimeMillis()
                                    reduce {
                                        it.copy(
                                            loyaltyCodeResendTimerStartedAt = startedAt,
                                            loyaltyCodeResendSecondsLeft = codeResendSecondsLeft(startedAt)
                                        )
                                    }
                                    dispatch(CheckoutIntent.StartLoyaltyCodeResendTimerTicker)
                                }
                                else -> {
                                    currentUserUseCase(Unit).getOrThrow()
                                    stateFlow.value.loyaltyCodeResendTimerJob?.cancel()
                                    reduce {
                                        it.copy(
                                            isLoyaltyCodeSheetVisible = false,
                                            loyaltyCodeResendTimerJob = null
                                        )
                                    }
                                    dispatch(CheckoutIntent.LoadData())
                                }
                            }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(loyaltyCodeResendJob = null) }
                            }
                        }
                        reduce { it.copy(loyaltyCodeResendJob = job) }
                    }
                }
            }
            is CheckoutIntent.PayByCardClick,
            is CheckoutIntent.PayBySbpClick -> {
                val state = stateFlow.value
                if (state.isPaymentLoading) return
                val method = when (intent) {
                    is CheckoutIntent.PayByCardClick -> FittingCheckoutOnlinePaymentMethod.Card
                    is CheckoutIntent.PayBySbpClick -> FittingCheckoutOnlinePaymentMethod.Sbp
                }
                val job = launch {
                    withCenterLoading {
                        val orderNumber = state.paymentOrderNumber.takeIf(String::isNotBlank)
                            ?: when (route.source) {
                                CheckoutSource.Cart -> {
                                    val params = OrdersCreateFromBasketUseCase.Params(
                                        paymentType = PaymentType.ONLINE_PAYMENT_CLIENT,
                                        fittingType = state.selectedPlaceType.fittingTypeDto,
                                        interval = state.selectedDeliveryInterval,
                                        clientAddress = state.selectedClientAddress,
                                        boutiqueAddress = state.deliveryData.boutiqueAddress
                                    )
                                    ordersCreateFromBasketUseCase(params).getOrThrow()
                                }
                                CheckoutSource.Fitting -> {
                                    val params = OrdersCreateFromFittingUseCase.Params(
                                        deliveryIds = state.fittingCheckoutData.deliveryIds,
                                        paymentType = PaymentType.ONLINE_PAYMENT_CLIENT
                                    )
                                    ordersCreateFromFittingUseCase(params).getOrThrow()
                                }
                                CheckoutSource.ExistingOrder -> requireNotNull(route.orderNumber)
                            }
                        reduce {
                            it.copy(
                                paymentOrderNumber = orderNumber,
                                pendingOnlinePaymentMethod = method
                            )
                        }
                        when {
                            state.isBonusAmountVisible -> {
                                val params = OrdersByOrderIdReserveBonusesUseCase.Params(
                                    orderId = orderNumber,
                                    bonusAmount = state.fittingCheckoutData.availableBonusAmount.toDouble()
                                )
                                ordersByOrderIdReserveBonusesUseCase(params).getOrThrow()
                                val startedAt = System.currentTimeMillis()
                                reduce {
                                    it.copy(
                                        isBonusConfirmationSheetVisible = true,
                                        bonusCode = "",
                                        isBonusCodeErrorVisible = false,
                                        bonusCodeResendTimerStartedAt = startedAt,
                                        bonusCodeResendSecondsLeft = codeResendSecondsLeft(startedAt)
                                    )
                                }
                                dispatch(CheckoutIntent.StartBonusCodeResendTimerTicker)
                            }
                            method == FittingCheckoutOnlinePaymentMethod.Card -> {
                                reduce { it.copy(isPaymentMethodSheetVisible = true) }
                            }
                            else -> {
                                val url = ordersByOrderIdPaymentSbpUseCase(orderNumber).getOrThrow()
                                val banks = sbpAvailableBanksUseCase(Unit).getOrThrow()
                                reduce {
                                    it.copy(
                                        sbpPaymentUrl = url,
                                        sbpBanks = banks,
                                        isSbpBankSheetVisible = true
                                    )
                                }
                            }
                        }
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { model -> model.copy(paymentJob = null) }
                    }
                }
                reduce { it.copy(paymentJob = job) }
            }
            is CheckoutIntent.ConfirmOrderClick -> {
                val state = stateFlow.value
                if (state.isPaymentLoading || route.source == CheckoutSource.ExistingOrder) return
                val job = launch {
                    when (route.source) {
                        CheckoutSource.Cart -> {
                            val params = OrdersCreateFromBasketUseCase.Params(
                                paymentType = PaymentType.CASHLESS_COURIER,
                                fittingType = state.selectedPlaceType.fittingTypeDto,
                                interval = state.selectedDeliveryInterval,
                                clientAddress = state.selectedClientAddress,
                                boutiqueAddress = state.deliveryData.boutiqueAddress
                            )
                            ordersCreateFromBasketUseCase(params).getOrThrow()
                            val address = when (state.selectedPlaceType) {
                                FittingConfirmationPlaceType.Home -> state.selectedClientAddress?.title
                                else -> state.deliveryData.boutiqueAddress
                            }
                            MainEventManager.send(
                                CheckoutPaymentResultRoute(
                                    status = CheckoutPaymentResultStatus.Ordered,
                                    deliveryInterval = state.selectedDeliveryInterval?.summary.orEmpty(),
                                    address = address.orEmpty(),
                                    itemsCount = state.fittingCheckoutData.itemCount
                                )
                            )
                        }
                        CheckoutSource.Fitting -> {
                            val params = OrdersCreateFromFittingUseCase.Params(
                                deliveryIds = state.fittingCheckoutData.deliveryIds,
                                paymentType = PaymentType.CASHLESS_COURIER
                            )
                            ordersCreateFromFittingUseCase(params).getOrThrow()
                            MainEventManager.send(
                                MainRoute(
                                    popUpToMain = true,
                                    selectedTab = MainTab.PROFILE_ORDERS
                                )
                            )
                        }
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { model -> model.copy(paymentJob = null) }
                    }
                }
                reduce { it.copy(paymentJob = job) }
            }
            is CheckoutIntent.DismissBonusConfirmationSheet -> {
                stateFlow.value.bonusCodeResendTimerJob?.cancel()
                reduce {
                    it.copy(
                        isBonusConfirmationSheetVisible = false,
                        bonusCodeResendTimerJob = null
                    )
                }
            }
            is CheckoutIntent.StartBonusCodeResendTimerTicker -> {
                stateFlow.value.bonusCodeResendTimerJob?.cancel()
                val job = launch {
                    while (true) {
                        delay(CODE_RESEND_TIMER_DELAY.milliseconds)
                        val state = stateFlow.value
                        if (!state.isBonusConfirmationSheetVisible) break
                        val secondsLeft = codeResendSecondsLeft(state.bonusCodeResendTimerStartedAt)
                        when {
                            secondsLeft != state.bonusCodeResendSecondsLeft -> {
                                reduce { it.copy(bonusCodeResendSecondsLeft = secondsLeft) }
                            }
                        }
                    }
                }
                reduce { it.copy(bonusCodeResendTimerJob = job) }
            }
            is CheckoutIntent.BonusCodeChange -> {
                reduce {
                    it.copy(
                        bonusCode = intent.code.filter(Char::isDigit).take(CODE_LENGTH),
                        isBonusCodeErrorVisible = false
                    )
                }
            }
            is CheckoutIntent.BonusCodeConfirmClick -> {
                val state = stateFlow.value
                when {
                    !state.isBonusConfirmationSheetVisible ||
                        state.paymentOrderNumber.isBlank() ||
                        state.bonusCode.length != CODE_LENGTH -> return
                    else -> {
                        val job = launch {
                            val params = OrdersByOrderIdConfirmBonusesUseCase.Params(
                                orderId = state.paymentOrderNumber,
                                code = state.bonusCode
                            )
                            ordersByOrderIdConfirmBonusesUseCase(params).getOrThrow()
                            stateFlow.value.bonusCodeResendTimerJob?.cancel()
                            reduce {
                                it.copy(
                                    isBonusConfirmationSheetVisible = false,
                                    bonusCodeResendTimerJob = null
                                )
                            }
                            when (state.pendingOnlinePaymentMethod) {
                                FittingCheckoutOnlinePaymentMethod.Card -> {
                                    reduce { it.copy(isPaymentMethodSheetVisible = true) }
                                }
                                FittingCheckoutOnlinePaymentMethod.Sbp -> {
                                    val url = ordersByOrderIdPaymentSbpUseCase(state.paymentOrderNumber).getOrThrow()
                                    val banks = sbpAvailableBanksUseCase(Unit).getOrThrow()
                                    reduce {
                                        it.copy(
                                            sbpPaymentUrl = url,
                                            sbpBanks = banks,
                                            isSbpBankSheetVisible = true
                                        )
                                    }
                                }
                            }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(bonusConfirmJob = null) }
                            }
                        }
                        reduce { it.copy(bonusConfirmJob = job) }
                    }
                }
            }
            is CheckoutIntent.BonusCodeResendClick -> {
                val state = stateFlow.value
                when {
                    !state.isBonusConfirmationSheetVisible ||
                        state.paymentOrderNumber.isBlank() ||
                        state.bonusCodeResendSecondsLeft > 0 ||
                        state.isBonusCodeResendLoading -> return
                    else -> {
                        val job = launch {
                            val params = OrdersByOrderIdReserveBonusesUseCase.Params(
                                orderId = state.paymentOrderNumber,
                                bonusAmount = state.fittingCheckoutData.availableBonusAmount.toDouble()
                            )
                            ordersByOrderIdReserveBonusesUseCase(params).getOrThrow()
                            val startedAt = System.currentTimeMillis()
                            reduce {
                                it.copy(
                                    bonusCodeResendTimerStartedAt = startedAt,
                                    bonusCodeResendSecondsLeft = codeResendSecondsLeft(startedAt)
                                )
                            }
                            dispatch(CheckoutIntent.StartBonusCodeResendTimerTicker)
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(bonusResendJob = null) }
                            }
                        }
                        reduce { it.copy(bonusResendJob = job) }
                    }
                }
            }
            is CheckoutIntent.DismissPaymentMethodSheet -> {
                reduce { it.copy(isPaymentMethodSheetVisible = false) }
            }
            is CheckoutIntent.PaymentCardClick -> {
                reduce {
                    it.copy(
                        paymentCards = it.paymentCards.map { card ->
                            card.copy(isSelected = card.id == intent.cardId)
                        }
                    )
                }
            }
            is CheckoutIntent.DeletePaymentCardClick -> {
                reduce {
                    it.copy(
                        paymentCards = it.paymentCards.filterNot { card -> card.id == intent.cardId }
                    )
                }
            }
            is CheckoutIntent.PaymentMethodPayClick -> {
                val state = stateFlow.value
                when {
                    state.paymentOrderNumber.isBlank() || state.isPaymentLoading -> return
                    state.paymentCards.none { card -> card.isSelected } -> return
                    else -> {
                        val job = launch {
                            val url = ordersByOrderIdPaymentLinkUseCase(state.paymentOrderNumber).getOrThrow()
                            reduce {
                                it.copy(
                                    isPaymentMethodSheetVisible = false,
                                    isPaymentExternalFlowStarted = true
                                )
                            }
                            send(CheckoutEvent.OpenPaymentUrl(url))
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(paymentJob = null) }
                            }
                        }
                        reduce { it.copy(paymentJob = job) }
                    }
                }
            }
            is CheckoutIntent.AddNewPaymentCardClick -> {
                reduce {
                    it.copy(
                        isPaymentMethodSheetVisible = false,
                        isCheckoutBankCardSheetVisible = true,
                        bankCardNumber = "",
                        bankCardExpirationDate = "",
                        bankCardCvv = "",
                        isBankCardSaveChecked = false,
                        isBankCardNumberErrorVisible = false,
                        isBankCardExpirationDateErrorVisible = false,
                        isBankCardCvvErrorVisible = false
                    )
                }
            }
            is CheckoutIntent.DismissCheckoutBankCardSheet -> {
                reduce {
                    it.copy(
                        isCheckoutBankCardSheetVisible = false,
                        bankCardNumber = "",
                        bankCardExpirationDate = "",
                        bankCardCvv = "",
                        isBankCardNumberErrorVisible = false,
                        isBankCardExpirationDateErrorVisible = false,
                        isBankCardCvvErrorVisible = false
                    )
                }
            }
            is CheckoutIntent.BankCardSaveClick -> {
                reduce { it.copy(isBankCardSaveChecked = !it.isBankCardSaveChecked) }
            }
            is CheckoutIntent.BankCardNumberChange -> {
                val formattedNumber = intent.value
                    .filter(Char::isDigit)
                    .take(CheckoutBankCardModel.MAX_CARD_NUMBER_LENGTH)
                    .chunked(4)
                    .joinToString(" ")
                val isMaxLength = formattedNumber.filter(Char::isDigit).length ==
                    CheckoutBankCardModel.MAX_CARD_NUMBER_LENGTH
                val paymentSystem = CheckoutBankCardModel(cardNumber = formattedNumber).paymentSystem
                reduce {
                    it.copy(
                        bankCardNumber = formattedNumber,
                        isBankCardNumberErrorVisible = isMaxLength &&
                            (!formattedNumber.isValidBankCardNumber || paymentSystem == CheckoutBankCardPaymentSystem.Unknown)
                    )
                }
            }
            is CheckoutIntent.BankCardExpirationDateChange -> {
                val digits = intent.value.filter(Char::isDigit).take(4)
                val formattedDate = when {
                    digits.length > 2 -> "${digits.take(2)}/${digits.drop(2)}"
                    else -> digits
                }
                reduce {
                    it.copy(
                        bankCardExpirationDate = formattedDate,
                        isBankCardExpirationDateErrorVisible = digits.length == 4 && !formattedDate.isValidBankCardExpirationDate
                    )
                }
            }
            is CheckoutIntent.BankCardCvvChange -> {
                reduce {
                    it.copy(
                        bankCardCvv = intent.value
                            .filter(Char::isDigit)
                            .take(CheckoutBankCardModel.CVV_LENGTH),
                        isBankCardCvvErrorVisible = false
                    )
                }
            }
            is CheckoutIntent.BankCardNumberFocusLost -> {
                reduce { it.copy(isBankCardNumberErrorVisible = !it.bankCardNumber.isValidBankCardNumber) }
            }
            is CheckoutIntent.BankCardExpirationDateFocusLost -> {
                reduce { it.copy(isBankCardExpirationDateErrorVisible = !it.bankCardExpirationDate.isValidBankCardExpirationDate) }
            }
            is CheckoutIntent.BankCardCvvFocusLost -> {
                reduce {
                    it.copy(
                        isBankCardCvvErrorVisible = it.bankCardCvv.isNotEmpty() &&
                            it.bankCardCvv.length != CheckoutBankCardModel.CVV_LENGTH
                    )
                }
            }
            is CheckoutIntent.BankCardPayClick -> {
                val state = stateFlow.value
                val sheet = CheckoutBankCardModel(
                    cardNumber = state.bankCardNumber,
                    expirationDate = state.bankCardExpirationDate,
                    cvv = state.bankCardCvv,
                    isLoading = state.isPaymentLoading
                )
                when {
                    !sheet.isPayEnabled -> return
                    !state.bankCardNumber.isValidBankCardNumber -> {
                        reduce { it.copy(isBankCardNumberErrorVisible = true) }
                    }
                    state.paymentOrderNumber.isBlank() || state.isPaymentLoading -> return
                    else -> {
                        val paymentCard = when {
                            state.isBankCardSaveChecked -> {
                                FittingCheckoutPaymentCardModel(
                                    id = UUID.randomUUID().toString(),
                                    paymentSystem = sheet.paymentSystem.name,
                                    lastFourDigits = state.bankCardNumber.filter(Char::isDigit).takeLast(4),
                                    isSelected = true
                                )
                            }
                            else -> null
                        }
                        val job = launch {
                            val url = ordersByOrderIdPaymentLinkUseCase(state.paymentOrderNumber).getOrThrow()
                            reduce {
                                it.copy(
                                    paymentCards = when {
                                        paymentCard != null -> {
                                            it.paymentCards.map { card -> card.copy(isSelected = false) } + paymentCard
                                        }
                                        else -> it.paymentCards
                                    },
                                    isCheckoutBankCardSheetVisible = false,
                                    bankCardNumber = "",
                                    bankCardExpirationDate = "",
                                    bankCardCvv = "",
                                    isBankCardSaveChecked = false,
                                    isBankCardNumberErrorVisible = false,
                                    isBankCardExpirationDateErrorVisible = false,
                                    isBankCardCvvErrorVisible = false,
                                    isPaymentExternalFlowStarted = true
                                )
                            }
                            send(CheckoutEvent.OpenPaymentUrl(url))
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(paymentJob = null) }
                            }
                        }
                        reduce { it.copy(paymentJob = job) }
                    }
                }
            }
            is CheckoutIntent.CheckPaymentResult -> {
                val state = stateFlow.value
                when {
                    !state.isPaymentExternalFlowStarted ||
                        state.paymentOrderNumber.isBlank() ||
                        state.paymentResultCheckJob?.isActive == true -> return
                    else -> {
                        val job = launch {
                            val result = fittingCheckoutPaymentResultUseCase(state.paymentOrderNumber).getOrThrow()
                            val status = when (result.paymentStatus) {
                                OrderPaymentStatus.PAYMENT_FINISHED -> CheckoutPaymentResultStatus.Success
                                OrderPaymentStatus.PAYMENT_FORBIDDEN -> CheckoutPaymentResultStatus.Error
                                else -> CheckoutPaymentResultStatus.Unpaid
                            }
                            reduce { it.copy(isPaymentExternalFlowStarted = false) }
                            MainEventManager.send(
                                CheckoutPaymentResultRoute(
                                    status = status,
                                    deliveryInterval = result.deliveryInterval,
                                    address = result.address
                                )
                            )
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(paymentResultCheckJob = null) }
                            }
                        }
                        reduce { it.copy(paymentResultCheckJob = job) }
                    }
                }
            }
            is CheckoutIntent.DismissSbpBankSheet -> {
                reduce {
                    it.copy(
                        isSbpBankSheetVisible = false,
                        sbpPaymentUrl = "",
                        sbpBanks = emptyList()
                    )
                }
            }
            is CheckoutIntent.SbpBankClick -> {
                val state = stateFlow.value
                if (state.sbpPaymentUrl.isBlank()) return
                reduce {
                    it.copy(
                        isSbpBankSheetVisible = false,
                        sbpPaymentUrl = "",
                        sbpBanks = emptyList(),
                        isPaymentExternalFlowStarted = true
                    )
                }
                launch {
                    send(
                        CheckoutEvent.OpenSbpBankApp(
                            packageName = intent.bank.packageName,
                            paymentUrl = state.sbpPaymentUrl
                        )
                    )
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is FittingsByPairedUserIdForCheckoutException,
            is BasketByPairedUserIdForCheckoutException -> {
                reduce { it.copy(loadDataJob = null) }
                launch { send(CheckoutEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is LinkLoyaltyCardByPhoneException,
            is LoyaltyLinkException -> {
                reduce {
                    it.copy(
                        loyaltyAddCardJob = null,
                        loyaltyCodeResendJob = null,
                        isLoyaltyAddCardPhoneErrorVisible = false
                    )
                }
                launch { send(CheckoutEvent.SnackbarTopErrorMessage(throwable.message)) }
            }
            is VerifyLoyaltyCardByPhoneException,
            is VerifyLoyaltyCardException -> {
                reduce {
                    it.copy(
                        loyaltyCodeConfirmJob = null,
                        isLoyaltyCodeErrorVisible = true
                    )
                }
            }
            is OrdersByOrderIdConfirmBonusesException -> {
                reduce {
                    it.copy(
                        bonusConfirmJob = null,
                        isBonusCodeErrorVisible = true
                    )
                }
            }
            is OrdersByOrderIdReserveBonusesException -> {
                reduce {
                    it.copy(
                        paymentJob = null,
                        bonusResendJob = null
                    )
                }
                launch { send(CheckoutEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is OrderPriceChangedException -> {
                reduce {
                    it.copy(
                        paymentJob = null,
                        isCheckoutAmountChangedDialogVisible = true,
                        amountChangedMessage = throwable.message
                    )
                }
            }
            is OrdersCreateFromFittingException,
            is OrdersCreateFromBasketException,
            is OrdersByOrderIdPaymentLinkException,
            is OrdersByOrderIdPaymentSbpException,
            is SbpAvailableBanksException -> {
                reduce { it.copy(paymentJob = null) }
                launch {
                    val event = when {
                        stateFlow.value.isCheckoutBankCardSheetVisible -> {
                            CheckoutEvent.SnackbarTopErrorMessage(throwable.message)
                        }
                        else -> CheckoutEvent.SnackbarErrorMessage(throwable.message)
                    }
                    send(event)
                }
            }
            is FittingCheckoutPaymentResultException -> {
                reduce { it.copy(paymentResultCheckJob = null) }
                launch { send(CheckoutEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(CheckoutEvent.SnackbarErrorMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: CheckoutRoute): CheckoutViewModel
    }
}
