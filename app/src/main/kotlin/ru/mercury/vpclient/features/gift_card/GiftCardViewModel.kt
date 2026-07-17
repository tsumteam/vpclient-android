package ru.mercury.vpclient.features.gift_card

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.gift_card.event.GiftCardEvent
import ru.mercury.vpclient.features.gift_card.intent.GiftCardIntent
import ru.mercury.vpclient.features.gift_card.model.GiftCardModel
import ru.mercury.vpclient.features.gift_card_checkout.navigation.GiftCardCheckoutRoute
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.GiftCardEntityFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.GiftCardsUseCase
import ru.mercury.vpclient.shared.domain.usecase.GiftCardsUseCase.GiftCardsException
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class GiftCardViewModel @Inject constructor(
    private val giftCardEntityFlowUseCase: GiftCardEntityFlowUseCase,
    private val giftCardsUseCase: GiftCardsUseCase,
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase
): ClientViewModel<GiftCardIntent, GiftCardModel, GiftCardEvent>(GiftCardModel()) {

    init {
        dispatch(GiftCardIntent.CollectGiftCard)
        dispatch(GiftCardIntent.CollectActiveEmployee)
        dispatch(GiftCardIntent.LoadGiftCard)
    }

    override fun dispatch(intent: GiftCardIntent) {
        when (intent) {
            is GiftCardIntent.CollectGiftCard -> {
                launch {
                    giftCardEntityFlowUseCase(GiftCardType.VIRTUAL).collectLatest { entity ->
                        when (entity) {
                            null -> reduce { state -> state.copy(giftCardEntity = null) }
                            else -> {
                                reduce { state ->
                                    state.copy(
                                        giftCardEntity = entity,
                                        selectedTemplateIndex = 0,
                                        amountText = entity.defaultAmount.toString(),
                                        isAmountErrorVisible = false,
                                        isBuyEnabled = entity.defaultAmount in entity.minAmount..entity.maxAmount
                                    )
                                }
                            }
                        }
                    }
                }
            }
            is GiftCardIntent.LoadGiftCard -> {
                val job = launch { giftCardsUseCase(Unit).getOrThrow() }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state -> state.copy(loadGiftCardJob = null) }
                    }
                }
                reduce { state -> state.copy(loadGiftCardJob = job) }
            }
            is GiftCardIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit).collectLatest { employee ->
                        reduce { state -> state.copy(activeEmployee = employee) }
                    }
                }
            }
            is GiftCardIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is GiftCardIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is GiftCardIntent.CartClick -> {
                launch { MainEventManager.send(CartRoute()) }
            }
            is GiftCardIntent.TermsClick -> {
                reduce { state -> state.copy(isTermsVisible = true) }
            }
            is GiftCardIntent.TermsDismiss -> {
                reduce { state -> state.copy(isTermsVisible = false) }
            }
            is GiftCardIntent.BuyClick -> {
                val state = stateFlow.value
                val giftCardEntity = state.giftCardEntity ?: return
                val amount = state.amountText.toIntOrNull() ?: return
                val templateId = state.selectedTemplate.templateId.takeIf { id -> id > 0 } ?: return
                launch {
                    MainEventManager.send(
                        GiftCardCheckoutRoute(
                            itemId = giftCardEntity.itemId,
                            amount = amount,
                            templateId = templateId,
                            type = giftCardEntity.type
                        )
                    )
                }
            }
            is GiftCardIntent.SelectTemplate -> {
                reduce { state -> state.copy(selectedTemplateIndex = intent.index) }
            }
            is GiftCardIntent.AmountChange -> {
                val giftCardEntity = stateFlow.value.giftCardEntity ?: return
                val amount = intent.value
                    .filter { character -> character.isDigit() }
                    .take(9)
                    .toIntOrNull()
                    ?: 0
                reduce { state ->
                    state.copy(
                        amountText = when {
                            amount > 0 -> amount.toString()
                            else -> ""
                        },
                        isAmountErrorVisible = amount !in giftCardEntity.minAmount..giftCardEntity.maxAmount,
                        isBuyEnabled = amount in giftCardEntity.minAmount..giftCardEntity.maxAmount
                    )
                }
            }
            is GiftCardIntent.SelectAmount -> {
                val giftCardEntity = stateFlow.value.giftCardEntity ?: return
                val amount = intent.amount
                    .toString()
                    .filter { character -> character.isDigit() }
                    .take(9)
                    .toIntOrNull()
                    ?: 0
                reduce { state ->
                    state.copy(
                        amountText = when {
                            amount > 0 -> amount.toString()
                            else -> ""
                        },
                        isAmountErrorVisible = amount !in giftCardEntity.minAmount..giftCardEntity.maxAmount,
                        isBuyEnabled = amount in giftCardEntity.minAmount..giftCardEntity.maxAmount
                    )
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        reduce { state -> state.copy(loadGiftCardJob = null) }
        when (throwable) {
            is GiftCardsException -> {
                launch { send(GiftCardEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(GiftCardEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(GiftCardEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
