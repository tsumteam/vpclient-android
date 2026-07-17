@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.gift_card_checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.gift_card_checkout.event.GiftCardCheckoutEvent
import ru.mercury.vpclient.features.gift_card_checkout.intent.GiftCardCheckoutIntent
import ru.mercury.vpclient.features.gift_card_checkout.model.GiftCardCheckoutModel
import ru.mercury.vpclient.features.gift_card_checkout.navigation.GiftCardCheckoutRoute
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationDaysRow
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationDaysRowState
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationIntervalsRow
import ru.mercury.vpclient.shared.ui.components.fitting.FittingConfirmationIntervalsRowState
import ru.mercury.vpclient.shared.ui.components.system.ClientTextField
import ru.mercury.vpclient.shared.ui.icons.BankCard24
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.Sbp52x28
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.darkGray
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.midnightBlue
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15
import java.time.LocalDate

@Composable
fun GiftCardCheckoutScreen(
    route: GiftCardCheckoutRoute,
    viewModel: GiftCardCheckoutViewModel = hiltViewModel<GiftCardCheckoutViewModel, GiftCardCheckoutViewModel.Factory>(
        creationCallback = { factory -> factory.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current

    GiftCardCheckoutScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError,
        focusManager = LocalFocusManager.current
    )

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.dispatch(GiftCardCheckoutIntent.CheckPaymentResult)
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is GiftCardCheckoutEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
            is GiftCardCheckoutEvent.OpenPaymentUrl -> uriHandler.openUri(event.url)
        }
    }
}

@Composable
private fun GiftCardCheckoutScreenContent(
    state: GiftCardCheckoutModel,
    dispatch: (GiftCardCheckoutIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState,
    focusManager: FocusManager
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(ClientStrings.GiftCardCheckoutTitle),
                            style = MaterialTheme.typography.medium15.copy(
                                lineHeight = 15.sp,
                                letterSpacing = .3.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        Text(
                            text = state.amountText,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(GiftCardCheckoutIntent.BackClick) }
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(ClientStrings.GiftCardCheckoutTotal),
                        style = MaterialTheme.typography.regular15.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 19.sp,
                            letterSpacing = .2.sp
                        )
                    )

                    Text(
                        text = state.amountText,
                        style = MaterialTheme.typography.medium15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 15.sp,
                            letterSpacing = .3.sp
                        )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { dispatch(GiftCardCheckoutIntent.PayByCardClick) },
                        modifier = Modifier
                            .weight(1F)
                            .height(46.dp),
                        enabled = state.isPayButtonEnabled,
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
                            text = stringResource(ClientStrings.GiftCardCheckoutPayByCard),
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.medium15.copy(
                                textAlign = TextAlign.Center,
                                lineHeight = 15.sp,
                                letterSpacing = .3.sp
                            )
                        )
                    }

                    Button(
                        onClick = { dispatch(GiftCardCheckoutIntent.PayBySbpClick) },
                        modifier = Modifier
                            .weight(1F)
                            .height(46.dp),
                        enabled = state.isPayButtonEnabled,
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
                            text = stringResource(ClientStrings.GiftCardCheckoutPayBySbp),
                            style = MaterialTheme.typography.medium15.copy(
                                textAlign = TextAlign.Center,
                                lineHeight = 15.sp,
                                letterSpacing = .3.sp
                            )
                        )

                        Icon(
                            imageVector = Sbp52x28,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(width = 52.dp, height = 28.dp)
                                .alpha(if (state.isPayButtonEnabled) 1F else .4F),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(bottom = 132.dp)
        ) {
            item {
                ClientTextField(
                    value = state.emailText,
                    accepted = !state.isEmailErrorVisible,
                    onValueChange = { value -> dispatch(GiftCardCheckoutIntent.EmailChange(value)) },
                    placeholder = stringResource(ClientStrings.GiftCardCheckoutEmailPlaceholder),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    enabled = !state.isLoading,
                    placeholderTextStyle = MaterialTheme.typography.regular15.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
            }
            item {
                Text(
                    text = stringResource(
                        when {
                            state.isEmailErrorVisible -> ClientStrings.GiftCardCheckoutEmailError
                            else -> ClientStrings.GiftCardCheckoutEmailHint
                        }
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                    style = MaterialTheme.typography.regular12.copy(
                        color = when {
                            state.isEmailErrorVisible -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        lineHeight = 16.sp,
                        letterSpacing = .2.sp
                    )
                )
            }
            item {
                ClientTextField(
                    value = state.phoneText,
                    accepted = !state.isPhoneErrorVisible,
                    onValueChange = { value -> dispatch(GiftCardCheckoutIntent.PhoneChange(value)) },
                    placeholder = stringResource(ClientStrings.GiftCardCheckoutPhonePlaceholder),
                    modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp),
                    enabled = !state.isLoading,
                    placeholderTextStyle = MaterialTheme.typography.regular15.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )
            }
            item {
                Text(
                    text = stringResource(
                        when {
                            state.isPhoneErrorVisible -> ClientStrings.GiftCardCheckoutPhoneError
                            else -> ClientStrings.GiftCardCheckoutPhoneHint
                        }
                    ),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                    style = MaterialTheme.typography.regular12.copy(
                        color = when {
                            state.isPhoneErrorVisible -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        lineHeight = 16.sp,
                        letterSpacing = .2.sp
                    )
                )
            }
            item {
                Text(
                    text = stringResource(ClientStrings.GiftCardCheckoutDeliveryDate),
                    modifier = Modifier.padding(start = 16.dp, top = 36.dp, end = 16.dp),
                    style = MaterialTheme.typography.regular12.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp,
                        letterSpacing = .2.sp
                    )
                )
            }
            item {
                FittingConfirmationDaysRow(
                    state = FittingConfirmationDaysRowState(
                        intervals = state.deliveryIntervals,
                        selectedDayId = state.selectedDayId,
                        onDayClick = { dayId -> dispatch(GiftCardCheckoutIntent.SelectDay(dayId)) }
                    ),
                    paddingTop = 12.dp
                )
            }
            item {
                FittingConfirmationIntervalsRow(
                    state = FittingConfirmationIntervalsRowState(
                        intervals = state.selectedDayIntervals,
                        selectedIntervalId = state.selectedIntervalId,
                        onIntervalClick = { intervalId ->
                            dispatch(GiftCardCheckoutIntent.SelectInterval(intervalId))
                        }
                    ),
                    paddingTop = 8.dp
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun GiftCardCheckoutScreenContentPreview(
    @PreviewParameter(GiftCardCheckoutModelPreviewParameterProvider::class) state: GiftCardCheckoutModel
) {
    GiftCardCheckoutScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() },
        focusManager = LocalFocusManager.current
    )
}

private class GiftCardCheckoutModelPreviewParameterProvider: PreviewParameterProvider<GiftCardCheckoutModel> {
    private val tomorrow = LocalDate.now().plusDays(1)
    private val intervals = listOf(
        FittingConfirmationDeliveryInterval(
            id = "$tomorrow-10:00-12:00",
            dayId = tomorrow.toString(),
            dayTitle = tomorrow.toString(),
            timeTitle = "10:00-12:00",
            summary = "$tomorrow 10:00-12:00"
        ),
        FittingConfirmationDeliveryInterval(
            id = "$tomorrow-12:00-14:00",
            dayId = tomorrow.toString(),
            dayTitle = tomorrow.toString(),
            timeTitle = "12:00-14:00",
            summary = "$tomorrow 12:00-14:00"
        )
    )

    override val values: Sequence<GiftCardCheckoutModel> = sequenceOf(
        GiftCardCheckoutModel(
            amount = 50000,
            phoneText = "+7 919 220-65-34",
            deliveryIntervals = intervals,
            selectedDayId = intervals.first().dayId,
            selectedIntervalId = intervals.first().id
        ),
        GiftCardCheckoutModel(
            amount = 50000,
            emailText = "1234fgvk",
            phoneText = "+7 919 220-65-34",
            deliveryIntervals = intervals,
            selectedDayId = intervals.first().dayId,
            selectedIntervalId = intervals.first().id,
            isEmailErrorVisible = true
        ),
        GiftCardCheckoutModel(
            amount = 50000,
            emailText = "Katya12345@gmail.com",
            phoneText = "+7 919 220-65-34",
            deliveryIntervals = intervals,
            selectedDayId = intervals.first().dayId,
            selectedIntervalId = intervals.first().id,
            isPaymentEnabled = true
        )
    )
}
