@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.checkout

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.checkout.event.CheckoutEvent
import ru.mercury.vpclient.features.checkout.intent.CheckoutIntent
import ru.mercury.vpclient.features.checkout.model.CheckoutModel
import ru.mercury.vpclient.features.checkout.model.CheckoutSource
import ru.mercury.vpclient.features.checkout.navigation.CheckoutRoute
import ru.mercury.vpclient.features.checkout_amount_changed_dialog.CheckoutAmountChangedDialog
import ru.mercury.vpclient.features.checkout_amount_changed_dialog.intent.CheckoutAmountChangedDialogIntent
import ru.mercury.vpclient.features.checkout_bank_card_sheet.CheckoutBankCardSheet
import ru.mercury.vpclient.features.checkout_bank_card_sheet.intent.CheckoutBankCardSheetIntent
import ru.mercury.vpclient.features.checkout_bank_card_sheet.model.CheckoutBankCardSheetModel
import ru.mercury.vpclient.features.checkout_bonus_sheet.CheckoutBonusSheet
import ru.mercury.vpclient.features.checkout_bonus_sheet.intent.CheckoutBonusIntent
import ru.mercury.vpclient.features.checkout_bonus_sheet.model.CheckoutBonusModel
import ru.mercury.vpclient.features.checkout_payment_method_sheet.CheckoutPaymentMethodSheet
import ru.mercury.vpclient.features.checkout_payment_method_sheet.intent.CheckoutPaymentMethodIntent
import ru.mercury.vpclient.features.checkout_payment_method_sheet.model.CheckoutPaymentMethodModel
import ru.mercury.vpclient.features.checkout_sbp_bank_sheet.CheckoutSbpBankSheet
import ru.mercury.vpclient.features.checkout_sbp_bank_sheet.intent.CheckoutSbpBankSheetIntent
import ru.mercury.vpclient.features.checkout_sbp_bank_sheet.model.CheckoutSbpBankSheetModel
import ru.mercury.vpclient.features.fitting_addresses.event.FittingAddressesEventManager
import ru.mercury.vpclient.features.loyalty_add_card_sheet.LoyaltyAddCardSheet
import ru.mercury.vpclient.features.loyalty_add_card_sheet.intent.LoyaltyAddCardIntent
import ru.mercury.vpclient.features.loyalty_add_card_sheet.model.LoyaltyAddCardMode
import ru.mercury.vpclient.features.loyalty_add_card_sheet.model.LoyaltyAddCardModel
import ru.mercury.vpclient.features.loyalty_code_sheet.LoyaltyCodeSheet
import ru.mercury.vpclient.features.loyalty_code_sheet.intent.LoyaltyCodeIntent
import ru.mercury.vpclient.features.loyalty_code_sheet.model.LoyaltyCodeModel
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutData
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.data.network.type.PaymentType
import ru.mercury.vpclient.shared.domain.mapper.title
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.SharedSwitch
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationPlaceRow
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationPlaceRowState
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationSectionTitle
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationSingleDeliveryContent
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationSingleDeliveryContentState
import ru.mercury.vpclient.shared.ui.icons.BankCard24
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.Sbp52x28
import ru.mercury.vpclient.shared.ui.icons.VipBadge24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.darkGray
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.medium13
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.midnightBlue
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CheckoutScreen(
    route: CheckoutRoute,
    viewModel: CheckoutViewModel = hiltViewModel<CheckoutViewModel, CheckoutViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val snackbarHostStateError = remember { SnackbarHostState() }
    val snackbarHostStateTopError = remember { SnackbarHostState() }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.dispatch(CheckoutIntent.CheckPaymentResult)
    }

    CheckoutScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError,
        snackbarHostStateTopError = snackbarHostStateTopError
    )

    if (state.isLoyaltyAddCardSheetVisible) {
        LoyaltyAddCardSheet(
            state = LoyaltyAddCardModel(
                mode = state.loyaltyAddCardMode,
                phone = state.loyaltyAddCardPhone,
                cardNumber = state.loyaltyAddCardCardNumber,
                isLoading = state.loyaltyAddCardJob?.isActive == true,
                isPhoneErrorVisible = state.isLoyaltyAddCardPhoneErrorVisible
            ),
            dispatch = { intent ->
                when (intent) {
                    is LoyaltyAddCardIntent.DismissRequest -> {
                        viewModel.dispatch(CheckoutIntent.DismissLoyaltyAddCardSheet)
                    }
                    is LoyaltyAddCardIntent.ModeClick -> {
                        viewModel.dispatch(CheckoutIntent.LoyaltyAddCardModeClick(intent.mode))
                    }
                    is LoyaltyAddCardIntent.PhoneChange -> {
                        viewModel.dispatch(CheckoutIntent.LoyaltyAddCardPhoneChange(intent.phone))
                    }
                    is LoyaltyAddCardIntent.CardNumberChange -> {
                        viewModel.dispatch(CheckoutIntent.LoyaltyAddCardCardNumberChange(intent.cardNumber))
                    }
                    is LoyaltyAddCardIntent.ConfirmClick -> {
                        when (state.loyaltyAddCardMode) {
                            LoyaltyAddCardMode.Phone -> {
                                viewModel.dispatch(CheckoutIntent.LoyaltyAddCardPhoneConfirmClick)
                            }
                            LoyaltyAddCardMode.CardNumber -> {
                                viewModel.dispatch(CheckoutIntent.LoyaltyAddCardCardNumberConfirmClick)
                            }
                        }
                    }
                }
            }
        )
    }

    if (state.isLoyaltyCodeSheetVisible) {
        LoyaltyCodeSheet(
            state = LoyaltyCodeModel(
                mode = state.loyaltyCodeMode,
                phone = state.loyaltyCodePhone,
                cardNumber = state.loyaltyCodeCardNumber,
                code = state.loyaltyCode,
                isLoading = state.isLoyaltyCodeLoading,
                isResendLoading = state.isLoyaltyCodeResendLoading,
                isCodeErrorVisible = state.isLoyaltyCodeErrorVisible,
                resendTimerStartedAt = state.loyaltyCodeResendTimerStartedAt,
                resendSecondsLeft = state.loyaltyCodeResendSecondsLeft,
                resendTimerJob = state.loyaltyCodeResendTimerJob
            ),
            dispatch = { intent ->
                when (intent) {
                    is LoyaltyCodeIntent.DismissRequest -> {
                        viewModel.dispatch(CheckoutIntent.DismissLoyaltyCodeSheet)
                    }
                    is LoyaltyCodeIntent.StartResendTimerTicker -> {
                        viewModel.dispatch(CheckoutIntent.StartLoyaltyCodeResendTimerTicker)
                    }
                    is LoyaltyCodeIntent.CodeChange -> {
                        viewModel.dispatch(CheckoutIntent.LoyaltyCodeChange(intent.code))
                    }
                    is LoyaltyCodeIntent.ConfirmClick -> {
                        viewModel.dispatch(CheckoutIntent.LoyaltyCodeConfirmClick)
                    }
                    is LoyaltyCodeIntent.ResendCodeClick -> {
                        viewModel.dispatch(CheckoutIntent.LoyaltyCodeResendCodeClick)
                    }
                }
            }
        )
    }

    if (state.isBonusConfirmationSheetVisible) {
        CheckoutBonusSheet(
            state = CheckoutBonusModel(
                code = state.bonusCode,
                isLoading = state.isBonusCodeLoading,
                isResendLoading = state.isBonusCodeResendLoading,
                isCodeErrorTextVisible = state.isBonusCodeErrorVisible,
                resendSecondsLeft = state.bonusCodeResendSecondsLeft
            ),
            dispatch = { intent ->
                when (intent) {
                    is CheckoutBonusIntent.DismissRequest -> {
                        viewModel.dispatch(CheckoutIntent.DismissBonusConfirmationSheet)
                    }
                    is CheckoutBonusIntent.CodeChange -> {
                        viewModel.dispatch(CheckoutIntent.BonusCodeChange(intent.code))
                    }
                    is CheckoutBonusIntent.ConfirmClick -> {
                        viewModel.dispatch(CheckoutIntent.BonusCodeConfirmClick)
                    }
                    is CheckoutBonusIntent.ResendCodeClick -> {
                        viewModel.dispatch(CheckoutIntent.BonusCodeResendClick)
                    }
                }
            }
        )
    }

    if (state.isPaymentMethodSheetVisible) {
        CheckoutPaymentMethodSheet(
            state = CheckoutPaymentMethodModel(
                cards = state.paymentCards,
                isLoading = state.isPaymentLoading
            ),
            dispatch = { intent ->
                when (intent) {
                    is CheckoutPaymentMethodIntent.DismissRequest -> {
                        viewModel.dispatch(CheckoutIntent.DismissPaymentMethodSheet)
                    }
                    is CheckoutPaymentMethodIntent.CardClick -> {
                        viewModel.dispatch(CheckoutIntent.PaymentCardClick(intent.cardId))
                    }
                    is CheckoutPaymentMethodIntent.DeleteCardClick -> {
                        viewModel.dispatch(CheckoutIntent.DeletePaymentCardClick(intent.cardId))
                    }
                    is CheckoutPaymentMethodIntent.AddNewCardClick -> {
                        viewModel.dispatch(CheckoutIntent.AddNewPaymentCardClick)
                    }
                    is CheckoutPaymentMethodIntent.PayClick -> {
                        viewModel.dispatch(CheckoutIntent.PaymentMethodPayClick)
                    }
                }
            }
        )
    }

    if (state.isSbpBankSheetVisible) {
        CheckoutSbpBankSheet(
            state = CheckoutSbpBankSheetModel(banks = state.sbpBanks),
            dispatch = { intent ->
                when (intent) {
                    is CheckoutSbpBankSheetIntent.DismissRequest -> {
                        viewModel.dispatch(CheckoutIntent.DismissSbpBankSheet)
                    }
                    is CheckoutSbpBankSheetIntent.BankClick -> {
                        viewModel.dispatch(CheckoutIntent.SbpBankClick(intent.bank))
                    }
                }
            }
        )
    }

    if (state.isBankCardSheetVisible) {
        CheckoutBankCardSheet(
            state = CheckoutBankCardSheetModel(
                cardNumber = state.bankCardNumber,
                expirationDate = state.bankCardExpirationDate,
                cvv = state.bankCardCvv,
                isSaveCardChecked = state.isBankCardSaveChecked,
                isCardNumberErrorVisible = state.isBankCardNumberErrorVisible,
                isExpirationDateErrorVisible = state.isBankCardExpirationDateErrorVisible,
                isLoading = state.isPaymentLoading
            ),
            dispatch = { intent ->
                when (intent) {
                    is CheckoutBankCardSheetIntent.DismissRequest -> {
                        viewModel.dispatch(CheckoutIntent.DismissBankCardSheet)
                    }
                    is CheckoutBankCardSheetIntent.SaveCardClick -> {
                        viewModel.dispatch(CheckoutIntent.BankCardSaveClick)
                    }
                    is CheckoutBankCardSheetIntent.PayClick -> {
                        viewModel.dispatch(CheckoutIntent.BankCardPayClick)
                    }
                    is CheckoutBankCardSheetIntent.CardNumberFocusLost -> {
                        viewModel.dispatch(CheckoutIntent.BankCardNumberFocusLost)
                    }
                    is CheckoutBankCardSheetIntent.ExpirationDateFocusLost -> {
                        viewModel.dispatch(CheckoutIntent.BankCardExpirationDateFocusLost)
                    }
                    is CheckoutBankCardSheetIntent.CardNumberChange -> {
                        viewModel.dispatch(CheckoutIntent.BankCardNumberChange(intent.value))
                    }
                    is CheckoutBankCardSheetIntent.ExpirationDateChange -> {
                        viewModel.dispatch(CheckoutIntent.BankCardExpirationDateChange(intent.value))
                    }
                    is CheckoutBankCardSheetIntent.CvvChange -> {
                        viewModel.dispatch(CheckoutIntent.BankCardCvvChange(intent.value))
                    }
                }
            }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is CheckoutEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
            is CheckoutEvent.SnackbarTopErrorMessage -> {
                snackbarHostStateTopError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateTopError.showSnackbar(event.message) }
            }
            is CheckoutEvent.OpenPaymentUrl -> uriHandler.openUri(event.url)
            is CheckoutEvent.OpenSbpBankApp -> {
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, event.paymentUrl.toUri()).setPackage(event.packageName))
                } catch (_: ActivityNotFoundException) {
                    uriHandler.openUri(event.paymentUrl)
                }
            }
        }
    }

    ObserveAsEvents(
        flow = FittingAddressesEventManager.eventFlow
    ) { event ->
        viewModel.dispatch(CheckoutIntent.ReceiveFittingAddressesEvent(event))
    }

    if (state.isAmountChangedDialogVisible) {
        CheckoutAmountChangedDialog(
            message = state.amountChangedMessage,
            dispatch = { intent ->
                when (intent) {
                    is CheckoutAmountChangedDialogIntent.ContinueClick -> {
                        viewModel.dispatch(CheckoutIntent.AmountChangedContinueClick)
                    }
                }
            }
        )
    }
}

@Composable
private fun CheckoutScreenContent(
    state: CheckoutModel,
    dispatch: (CheckoutIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState,
    snackbarHostStateTopError: SnackbarHostState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SharedScaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        when {
                            state.isToolbarBonusVisible -> {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(3.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(ClientStrings.CheckoutYourBonuses),
                                        style = MaterialTheme.typography.medium15.copy(
                                            lineHeight = 15.sp,
                                            letterSpacing = .3.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )

                                    Text(
                                        text = state.totalAvailableBonusAmountText,
                                        style = MaterialTheme.typography.regular14.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 18.sp,
                                            letterSpacing = .2.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { dispatch(CheckoutIntent.CloseClick) }
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            snackbarHost = {
                SharedSnackbarHost(
                    hostState = snackbarHostStateError,
                    containerColor = MaterialTheme.colorScheme.error
                )
            },
            floatingActionButton = {
                when {
                    state.isLoading -> {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(width = 96.dp, height = 14.dp)
                                        .placeholder(shape = RoundedCornerShape(4.dp))
                                )

                                Spacer(
                                    modifier = Modifier
                                        .size(width = 96.dp, height = 15.dp)
                                        .placeholder(shape = RoundedCornerShape(4.dp))
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(width = 72.dp, height = 14.dp)
                                        .placeholder(shape = RoundedCornerShape(4.dp))
                                )

                                Spacer(
                                    modifier = Modifier
                                        .size(width = 88.dp, height = 15.dp)
                                        .placeholder(shape = RoundedCornerShape(4.dp))
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(52.dp)
                                        .placeholder(shape = RoundedCornerShape(8.dp))
                                )

                                Spacer(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(52.dp)
                                        .placeholder(shape = RoundedCornerShape(8.dp))
                                )
                            }
                        }
                    }
                    else -> {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(ClientStrings.CheckoutOrder),
                                    style = MaterialTheme.typography.regular14.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp,
                                        letterSpacing = .2.sp
                                    )
                                )

                                Text(
                                    text = state.orderAmountText,
                                    style = MaterialTheme.typography.medium15.copy(
                                        lineHeight = 15.sp,
                                        letterSpacing = .3.sp
                                    )
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(ClientStrings.CheckoutItemsCount),
                                    style = MaterialTheme.typography.regular14.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp,
                                        letterSpacing = .2.sp
                                    )
                                )

                                Text(
                                    text = state.fittingCheckoutData.itemCount.toString(),
                                    style = MaterialTheme.typography.medium15.copy(
                                        lineHeight = 15.sp,
                                        letterSpacing = .3.sp
                                    )
                                )
                            }

                            if (state.isPromotionDiscountRowVisible) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(24.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(ClientStrings.CheckoutPromotionDiscount),
                                        style = MaterialTheme.typography.regular14.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 18.sp,
                                            letterSpacing = .2.sp
                                        )
                                    )

                                    Text(
                                        text = state.promotionDiscountText,
                                        style = MaterialTheme.typography.medium15.copy(
                                            color = MaterialTheme.colorScheme.error,
                                            lineHeight = 15.sp,
                                            letterSpacing = .3.sp
                                        )
                                    )
                                }
                            }

                            if (state.isBonusAmountRowVisible) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(24.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(ClientStrings.CheckoutBonuses),
                                        style = MaterialTheme.typography.regular14.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 18.sp,
                                            letterSpacing = .2.sp
                                        )
                                    )

                                    Text(
                                        text = state.bonusAmountText,
                                        style = MaterialTheme.typography.medium15.copy(
                                            color = MaterialTheme.colorScheme.error,
                                            lineHeight = 15.sp,
                                            letterSpacing = .3.sp
                                        )
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(ClientStrings.CheckoutTotal),
                                    style = MaterialTheme.typography.regular14.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp,
                                        letterSpacing = .2.sp
                                    )
                                )

                                Text(
                                    text = state.totalAmountText,
                                    style = MaterialTheme.typography.medium15.copy(
                                        lineHeight = 15.sp,
                                        letterSpacing = .3.sp
                                    )
                                )
                            }

                            when {
                                state.isOnlinePaymentSelected -> {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 24.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { dispatch(CheckoutIntent.PayByCardClick) },
                                            modifier = Modifier
                                                .weight(1F)
                                                .height(52.dp),
                                            enabled = state.isPayByCardEnabled,
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary,
                                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                                disabledContainerColor = MaterialTheme.colorScheme.disabled,
                                                disabledContentColor = MaterialTheme.colorScheme.onDisabled
                                            ),
                                            contentPadding = PaddingValues(horizontal = 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = BankCard24,
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp),
                                                tint = Color.Unspecified
                                            )

                                            Text(
                                                text = stringResource(ClientStrings.CheckoutPayByCard),
                                                modifier = Modifier.padding(start = 8.dp),
                                                style = MaterialTheme.typography.medium15.copy(
                                                    lineHeight = 15.sp,
                                                    letterSpacing = .3.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            )
                                        }

                                        Button(
                                            onClick = { dispatch(CheckoutIntent.PayBySbpClick) },
                                            modifier = Modifier
                                                .weight(1F)
                                                .height(52.dp),
                                            enabled = state.isPaySbpEnabled,
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.midnightBlue,
                                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                                disabledContainerColor = MaterialTheme.colorScheme.darkGray,
                                                disabledContentColor = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            contentPadding = PaddingValues(horizontal = 8.dp)
                                        ) {
                                            Text(
                                                text = stringResource(ClientStrings.CheckoutPayBySbp),
                                                style = MaterialTheme.typography.medium15.copy(
                                                    lineHeight = 15.sp,
                                                    letterSpacing = .3.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            )

                                            Icon(
                                                imageVector = Sbp52x28,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(start = 8.dp)
                                                    .size(width = 52.dp, height = 28.dp)
                                                    .alpha(if (state.isPaySbpEnabled) 1F else .4F),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }
                                }
                                else -> {
                                    Button(
                                        onClick = { dispatch(CheckoutIntent.ConfirmOrderClick) },
                                        modifier = Modifier
                                            .padding(top = 24.dp)
                                            .fillMaxWidth()
                                            .height(52.dp),
                                        enabled = state.isPaymentEnabled,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = stringResource(ClientStrings.CheckoutConfirmOrder),
                                            style = MaterialTheme.typography.medium15.copy(
                                                lineHeight = 15.sp,
                                                letterSpacing = .3.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            SharedLazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding + PaddingValues(top = 8.dp),
                userScrollEnabled = !state.isLoading
            ) {
                when {
                    state.isLoading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .size(width = 120.dp, height = 18.dp)
                                        .placeholder(shape = RoundedCornerShape(4.dp))
                                )
                            }
                        }
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(40.dp)
                                        .placeholder(shape = RoundedCornerShape(10.dp))
                                )

                                Spacer(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(40.dp)
                                        .placeholder(shape = RoundedCornerShape(10.dp))
                                )
                            }
                        }
                        item {
                            Spacer(
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 40.dp, end = 16.dp)
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .placeholder(shape = RoundedCornerShape(8.dp))
                            )
                        }
                    }
                    else -> {
                        if (state.isDeliveryPlaceSectionVisible) {
                            item {
                                FittingConfirmationSectionTitle(
                                    text = stringResource(ClientStrings.CheckoutDeliveryPlaceTitle)
                                )
                            }
                            item {
                                FittingConfirmationPlaceRow(
                                    state = FittingConfirmationPlaceRowState(
                                        text = state.deliveryData.boutiqueAddress
                                            ?: stringResource(ClientStrings.FittingConfirmationPlaceBoutique),
                                        selected = state.selectedPlaceType == FittingConfirmationPlaceType.Boutique,
                                        enabled = true,
                                        showChevron = false
                                    ),
                                    onClick = {
                                        dispatch(CheckoutIntent.SelectPlace(FittingConfirmationPlaceType.Boutique))
                                    },
                                    onChevronClick = { dispatch(CheckoutIntent.OpenAddressSelection) }
                                )
                            }
                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 48.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                            item {
                                FittingConfirmationPlaceRow(
                                    state = FittingConfirmationPlaceRowState(
                                        text = state.selectedClientAddress?.title
                                            ?: stringResource(ClientStrings.FittingConfirmationSelectAddress),
                                        selected = state.selectedPlaceType == FittingConfirmationPlaceType.Home,
                                        enabled = state.deliveryData.isClientAddressAvailable,
                                        showChevron = true
                                    ),
                                    onClick = {
                                        dispatch(CheckoutIntent.SelectPlace(FittingConfirmationPlaceType.Home))
                                    },
                                    onChevronClick = { dispatch(CheckoutIntent.OpenAddressSelection) }
                                )
                            }
                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(start = 48.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                            item {
                                FittingConfirmationSingleDeliveryContent(
                                    state = FittingConfirmationSingleDeliveryContentState(
                                        intervals = state.deliveryData.singleIntervals,
                                        selectedDayId = state.selectedDayId,
                                        selectedIntervalId = state.selectedIntervalId,
                                        onDayClick = { dayId -> dispatch(CheckoutIntent.SelectDeliveryDay(dayId)) },
                                        onIntervalClick = { intervalId ->
                                            dispatch(CheckoutIntent.SelectDeliveryInterval(intervalId))
                                        }
                                    ),
                                    modifier = Modifier.padding(top = 24.dp)
                                )
                            }
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = stringResource(ClientStrings.CheckoutPaymentMethod),
                                    style = MaterialTheme.typography.regular14.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 18.sp,
                                        letterSpacing = .2.sp
                                    )
                                )
                            }
                        }
                        item {
                            val isPaymentTypeChangeEnabled = state.source != CheckoutSource.ExistingOrder

                            SharedTabRow(
                                state = SharedTabRowState(
                                    selectedIndex = state.selectedPaymentTypeIndex,
                                    firstTabText = stringResource(ClientStrings.CheckoutPayOnlineCaps),
                                    secondTabText = stringResource(ClientStrings.CheckoutPayAtCashDeskCaps),
                                    onFirstTabClick = {
                                        if (isPaymentTypeChangeEnabled) dispatch(CheckoutIntent.PayOnlineClick)
                                    },
                                    onSecondTabClick = {
                                        if (isPaymentTypeChangeEnabled) dispatch(CheckoutIntent.PayAtCashDeskClick)
                                    },
                                    isLoading = false
                                ),
                                textStyle = MaterialTheme.typography.medium13.copy(
                                    lineHeight = 16.sp,
                                    letterSpacing = .3.sp
                                ),
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                                    .alpha(if (isPaymentTypeChangeEnabled) 1F else .4F)
                            )
                        }
                        if (state.isAddLoyaltyCardVisible) {
                            item {
                                OutlinedButton(
                                    onClick = { dispatch(CheckoutIntent.AddLoyaltyCardClick) },
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 40.dp, end = 16.dp)
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onBackground
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.background,
                                        contentColor = MaterialTheme.colorScheme.onBackground
                                    ),
                                    contentPadding = PaddingValues(horizontal = 16.dp)
                                ) {
                                    Icon(
                                        imageVector = VipBadge24,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Unspecified
                                    )

                                    Text(
                                        text = stringResource(ClientStrings.CheckoutAddLoyaltyCard),
                                        modifier = Modifier.padding(start = 8.dp),
                                        style = MaterialTheme.typography.medium15.copy(
                                            lineHeight = 15.sp,
                                            letterSpacing = .3.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                            }
                        }
                        if (state.isPayWithBonusesVisible) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .clickable { dispatch(CheckoutIntent.PayWithBonusesClick) }
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = stringResource(ClientStrings.CheckoutPayWithBonuses),
                                        style = MaterialTheme.typography.medium14.copy(
                                            lineHeight = 16.sp
                                        )
                                    )

                                    SharedSwitch(
                                        checked = state.isPayWithBonuses,
                                        onCheckedChange = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        SharedSnackbarHost(
            hostState = snackbarHostStateTopError,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 8.dp),
            containerColor = MaterialTheme.colorScheme.error
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CheckoutScreenContentPreview(
    @PreviewParameter(CheckoutModelPreviewParameterProvider::class) state: CheckoutModel
) {
    CheckoutScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() },
        snackbarHostStateTopError = remember { SnackbarHostState() }
    )
}

private class CheckoutModelPreviewParameterProvider: PreviewParameterProvider<CheckoutModel> {
    override val values: Sequence<CheckoutModel> = sequenceOf(
        CheckoutModel(
            loadDataJob = Job()
        ),
        CheckoutModel(
            fittingCheckoutData = FittingCheckoutData.Empty.copy(
                deliveryIds = listOf("delivery"),
                itemCount = 2,
                orderAmount = 259_950,
                availableBonusAmount = 12_000,
                totalAvailableBonusAmount = 12_000,
                loyaltyCardNumber = "G40135"
            ),
            isPayWithBonuses = false
        ),
        CheckoutModel(
            fittingCheckoutData = FittingCheckoutData.Empty.copy(
                deliveryIds = listOf("delivery"),
                itemCount = 2,
                orderAmount = 259_950,
                availableBonusAmount = 12_000,
                totalAvailableBonusAmount = 12_000,
                loyaltyCardNumber = "G40135"
            )
        ),
        CheckoutModel(
            fittingCheckoutData = FittingCheckoutData.Empty.copy(
                deliveryIds = listOf("delivery"),
                itemCount = 2,
                orderAmount = 259_950,
                availableBonusAmount = 12_000,
                totalAvailableBonusAmount = 12_000,
                loyaltyCardNumber = "G40135"
            ),
            paymentType = PaymentType.CASHLESS_COURIER
        ),
        CheckoutModel(
            fittingCheckoutData = FittingCheckoutData.Empty.copy(
                deliveryIds = listOf("delivery"),
                itemCount = 2,
                orderAmount = 247_950,
                loyaltyCardNumber = null
            )
        )
    )
}
