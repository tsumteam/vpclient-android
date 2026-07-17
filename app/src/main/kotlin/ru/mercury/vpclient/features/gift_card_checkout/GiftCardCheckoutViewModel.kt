package ru.mercury.vpclient.features.gift_card_checkout

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.gift_card_checkout.event.GiftCardCheckoutEvent
import ru.mercury.vpclient.features.gift_card_checkout.intent.GiftCardCheckoutIntent
import ru.mercury.vpclient.features.gift_card_checkout.model.GiftCardCheckoutModel
import ru.mercury.vpclient.features.gift_card_checkout.navigation.GiftCardCheckoutRoute
import ru.mercury.vpclient.features.gift_card_result.navigation.GiftCardResultRoute
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.GiftCardPaymentData
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.OrderPaymentStatus
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.domain.mapper.normalizePhoneInput
import ru.mercury.vpclient.shared.domain.mapper.withCenterLoading
import ru.mercury.vpclient.shared.domain.usecase.CurrentUserUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdPaymentStatusUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersByOrderIdPaymentStatusUseCase.OrdersByOrderIdPaymentStatusException
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateWithGiftCardPaymentLinkUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateWithGiftCardPaymentLinkUseCase.OrdersCreateWithGiftCardPaymentLinkException
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateWithGiftCardPaymentSbpUseCase
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateWithGiftCardPaymentSbpUseCase.OrdersCreateWithGiftCardPaymentSbpException
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@HiltViewModel(assistedFactory = GiftCardCheckoutViewModel.Factory::class)
class GiftCardCheckoutViewModel @AssistedInject constructor(
    @Assisted route: GiftCardCheckoutRoute,
    @ApplicationContext private val context: Context,
    private val currentUserUseCase: CurrentUserUseCase,
    private val ordersByOrderIdPaymentStatusUseCase: OrdersByOrderIdPaymentStatusUseCase,
    private val ordersCreateWithGiftCardPaymentLinkUseCase: OrdersCreateWithGiftCardPaymentLinkUseCase,
    private val ordersCreateWithGiftCardPaymentSbpUseCase: OrdersCreateWithGiftCardPaymentSbpUseCase
): ClientViewModel<GiftCardCheckoutIntent, GiftCardCheckoutModel, GiftCardCheckoutEvent>(GiftCardCheckoutModel()) {

    init {
        dispatch(GiftCardCheckoutIntent.LoadData(route))
    }

    override fun dispatch(intent: GiftCardCheckoutIntent) {
        when (intent) {
            is GiftCardCheckoutIntent.LoadData -> {
                reduce { state ->
                    state.copy(
                        itemId = intent.route.itemId,
                        templateId = intent.route.templateId,
                        amount = intent.route.amount,
                        type = intent.route.type
                    )
                }
                val job = launch {
                    val now = ZonedDateTime.now().truncatedTo(ChronoUnit.MINUTES)
                    val apiDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
                    val dayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.forLanguageTag("ru"))
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("ru"))
                    val intervals = buildList {
                        (0L..6L).forEach { dayOffset ->
                            val day = now.toLocalDate().plusDays(dayOffset)
                            val dayTitle = when (dayOffset) {
                                0L -> context.getString(ClientStrings.GiftCardCheckoutToday)
                                1L -> context.getString(ClientStrings.GiftCardCheckoutTomorrow)
                                else -> day.format(dayFormatter)
                            }
                            var dateTime = when (dayOffset) {
                                0L -> now
                                else -> day.atStartOfDay(now.zone)
                            }
                            while (dateTime.toLocalDate() == day) {
                                val isNow = dayOffset == 0L && dateTime == now
                                val dateTimeValue = dateTime.format(apiDateFormatter)
                                val timeTitle = when {
                                    isNow -> context.getString(ClientStrings.GiftCardCheckoutNow)
                                    else -> dateTime.format(timeFormatter)
                                }
                                add(
                                    FittingConfirmationDeliveryInterval(
                                        id = dateTimeValue,
                                        dayId = day.toString(),
                                        dayTitle = dayTitle,
                                        timeTitle = timeTitle,
                                        summary = "$dayTitle $timeTitle",
                                        from = dateTimeValue,
                                        to = dateTimeValue
                                    )
                                )
                                dateTime = when {
                                    isNow && dateTime.minute < 30 -> dateTime.withMinute(30)
                                    isNow -> dateTime.plusHours(1).withMinute(0)
                                    else -> dateTime.plusMinutes(30)
                                }
                            }
                        }
                    }
                    reduce { state ->
                        state.copy(
                            deliveryIntervals = intervals,
                            selectedDayId = intervals.firstOrNull()?.dayId,
                            selectedIntervalId = intervals.firstOrNull()?.id
                        )
                    }

                    val user = currentUserUseCase(Unit).getOrThrow()
                    val phoneText = formatPhoneForDisplay(user.clientPhone.orEmpty())
                    reduce { state ->
                        val isPhoneValid = phoneText.count(Char::isDigit) == 11
                        val isEmailValid = state.emailText.matches(
                            Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")
                        )
                        state.copy(
                            phoneText = phoneText,
                            isPhoneErrorVisible = phoneText.isNotEmpty() && !isPhoneValid,
                            isPaymentEnabled = isEmailValid && isPhoneValid && state.selectedIntervalId != null
                        )
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state -> state.copy(loadDataJob = null) }
                    }
                }
                reduce { state -> state.copy(loadDataJob = job) }
            }
            is GiftCardCheckoutIntent.BackClick -> {
                launch { MainEventManager.send(BackRoute) }
            }
            is GiftCardCheckoutIntent.CheckPaymentResult -> {
                val state = stateFlow.value
                when {
                    state.paymentOrderNumber.isEmpty() -> return
                    state.paymentResultCheckJob?.isActive == true -> return
                    else -> {
                        val job = launch {
                            withCenterLoading {
                                val paymentStatus = ordersByOrderIdPaymentStatusUseCase(state.paymentOrderNumber).getOrThrow()
                                val interval = state.deliveryIntervals
                                    .firstOrNull { item -> item.id == state.selectedIntervalId }
                                    ?: return@withCenterLoading
                                val dateFrom = interval.from ?: return@withCenterLoading
                                val dateTime = ZonedDateTime.parse(
                                    dateFrom,
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
                                )
                                MainEventManager.send(
                                    GiftCardResultRoute(
                                        isPaid = paymentStatus == OrderPaymentStatus.PAYMENT_FINISHED,
                                        orderNumber = state.paymentOrderNumber,
                                        email = state.emailText,
                                        phone = "+${normalizePhoneInput(state.phoneText, maxDigits = 11)}",
                                        deliveryDate = dateTime.format(
                                            DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru"))
                                        ),
                                        deliveryTime = dateTime.format(
                                            DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("ru"))
                                        )
                                    )
                                )
                            }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { model -> model.copy(paymentResultCheckJob = null) }
                            }
                        }
                        reduce { model -> model.copy(paymentResultCheckJob = job) }
                    }
                }
            }
            is GiftCardCheckoutIntent.PayByCardClick -> {
                val state = stateFlow.value
                val interval = state.deliveryIntervals
                    .firstOrNull { item -> item.id == state.selectedIntervalId }
                    ?: return
                val dateFrom = interval.from ?: return
                val dateTo = interval.to ?: return
                val paymentData = GiftCardPaymentData(
                    itemId = state.itemId,
                    templateId = state.templateId,
                    amount = state.amount.toDouble(),
                    type = state.type,
                    email = state.emailText,
                    phone = "+${normalizePhoneInput(state.phoneText, maxDigits = 11)}",
                    dateFrom = dateFrom,
                    dateTo = dateTo
                )
                val job = launch {
                    withCenterLoading {
                        val paymentLink = ordersCreateWithGiftCardPaymentLinkUseCase(paymentData).getOrThrow()
                        reduce { model -> model.copy(paymentOrderNumber = paymentLink.orderNumber) }
                        send(GiftCardCheckoutEvent.OpenPaymentUrl(paymentLink.url))
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { model -> model.copy(paymentJob = null) }
                    }
                }
                reduce { model -> model.copy(paymentJob = job) }
            }
            is GiftCardCheckoutIntent.PayBySbpClick -> {
                val state = stateFlow.value
                val interval = state.deliveryIntervals
                    .firstOrNull { item -> item.id == state.selectedIntervalId }
                    ?: return
                val dateFrom = interval.from ?: return
                val dateTo = interval.to ?: return
                val paymentData = GiftCardPaymentData(
                    itemId = state.itemId,
                    templateId = state.templateId,
                    amount = state.amount.toDouble(),
                    type = state.type,
                    email = state.emailText,
                    phone = "+${normalizePhoneInput(state.phoneText, maxDigits = 11)}",
                    dateFrom = dateFrom,
                    dateTo = dateTo
                )
                val job = launch {
                    withCenterLoading {
                        val paymentLink = ordersCreateWithGiftCardPaymentSbpUseCase(paymentData).getOrThrow()
                        reduce { model -> model.copy(paymentOrderNumber = paymentLink.orderNumber) }
                        send(GiftCardCheckoutEvent.OpenPaymentUrl(paymentLink.url))
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { model -> model.copy(paymentJob = null) }
                    }
                }
                reduce { model -> model.copy(paymentJob = job) }
            }
            is GiftCardCheckoutIntent.EmailChange -> {
                val emailText = intent.value.take(100)
                val isEmailValid = emailText.matches(
                    Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")
                )
                val isPhoneValid = stateFlow.value.phoneText.count(Char::isDigit) == 11
                reduce { state ->
                    state.copy(
                        emailText = emailText,
                        isEmailErrorVisible = emailText.isNotEmpty() && !isEmailValid,
                        isPaymentEnabled = isEmailValid && isPhoneValid && state.selectedIntervalId != null
                    )
                }
            }
            is GiftCardCheckoutIntent.PhoneChange -> {
                val phoneText = intent.value.take(18)
                val isPhoneValid = phoneText.count(Char::isDigit) == 11
                val isEmailValid = stateFlow.value.emailText.matches(
                    Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")
                )
                reduce { state ->
                    state.copy(
                        phoneText = phoneText,
                        isPhoneErrorVisible = phoneText.isNotEmpty() && !isPhoneValid,
                        isPaymentEnabled = isEmailValid && isPhoneValid && state.selectedIntervalId != null
                    )
                }
            }
            is GiftCardCheckoutIntent.SelectDay -> {
                val intervalId = stateFlow.value.deliveryIntervals
                    .firstOrNull { interval -> interval.dayId == intent.dayId }
                    ?.id
                reduce { state ->
                    state.copy(
                        selectedDayId = intent.dayId,
                        selectedIntervalId = intervalId,
                        isPaymentEnabled = state.emailText.matches(
                            Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")
                        ) && state.phoneText.count(Char::isDigit) == 11 && intervalId != null
                    )
                }
            }
            is GiftCardCheckoutIntent.SelectInterval -> {
                reduce { state ->
                    state.copy(
                        selectedIntervalId = intent.intervalId,
                        isPaymentEnabled = state.emailText.matches(
                            Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")
                        ) && state.phoneText.count(Char::isDigit) == 11
                    )
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is OrdersByOrderIdPaymentStatusException -> {
                reduce { state -> state.copy(paymentResultCheckJob = null) }
                launch { send(GiftCardCheckoutEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is OrdersCreateWithGiftCardPaymentLinkException,
            is OrdersCreateWithGiftCardPaymentSbpException -> {
                reduce { state -> state.copy(paymentJob = null) }
                launch { send(GiftCardCheckoutEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            is ClientException -> {
                reduce { state -> state.copy(loadDataJob = null) }
                launch { send(GiftCardCheckoutEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                reduce { state -> state.copy(loadDataJob = null) }
                launch { send(GiftCardCheckoutEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: GiftCardCheckoutRoute): GiftCardCheckoutViewModel
    }
}
