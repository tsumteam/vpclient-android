@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.checkout_payment_method_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.R
import ru.mercury.vpclient.features.checkout_payment_method_sheet.intent.CheckoutPaymentMethodIntent
import ru.mercury.vpclient.features.checkout_payment_method_sheet.model.CheckoutPaymentMethodModel
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutPaymentCardModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.icons.BankCard24
import ru.mercury.vpclient.shared.ui.icons.CirclePlus24
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.PaymentCardDelete24
import ru.mercury.vpclient.shared.ui.icons.Selected24
import ru.mercury.vpclient.shared.ui.icons.Unselected24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium17
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CheckoutPaymentMethodSheet(
    state: CheckoutPaymentMethodModel,
    dispatch: (CheckoutPaymentMethodIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CheckoutPaymentMethodIntent) -> Unit = { intent ->
        when (intent) {
            is CheckoutPaymentMethodIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            else -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CheckoutPaymentMethodIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.CheckoutPaymentMethodSheetTitleCaps),
                        style = MaterialTheme.typography.livretMedium17.copy(
                            lineHeight = 26.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(CheckoutPaymentMethodIntent.DismissRequest) }
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

            SharedLazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(
                    count = state.cards.size,
                    key = { index -> state.cards[index].id }
                ) { index ->
                    val card = state.cards[index]
                    val paymentSystemImageRes: Int? = when {
                        card.paymentSystem.contains("master", ignoreCase = true) -> {
                            R.drawable.checkout_mastercard
                        }
                        card.paymentSystem.contains("visa", ignoreCase = true) -> {
                            R.drawable.checkout_visa
                        }
                        card.paymentSystem.contains("mir", ignoreCase = true) ||
                            card.paymentSystem.contains("мир", ignoreCase = true) -> {
                            R.drawable.checkout_mir
                        }
                        else -> null
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clickable { sheetDispatch(CheckoutPaymentMethodIntent.CardClick(card.id)) }
                            .padding(start = 16.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when {
                                card.isSelected -> Selected24
                                else -> Unselected24
                            },
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        when {
                            paymentSystemImageRes != null -> {
                                Image(
                                    painter = painterResource(paymentSystemImageRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 4.dp)
                                        .size(width = 48.dp, height = 34.dp)
                                )
                            }
                            else -> {
                                Icon(
                                    imageVector = BankCard24,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .size(24.dp),
                                    tint = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        Text(
                            text = stringResource(
                                ClientStrings.CheckoutPaymentMethodSheetCard,
                                card.paymentSystem,
                                card.lastFourDigits
                            ),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1F),
                            style = MaterialTheme.typography.regular15.copy(
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp
                            )
                        )

                        IconButton(
                            onClick = { sheetDispatch(CheckoutPaymentMethodIntent.DeleteCardClick(card.id)) }
                        ) {
                            Icon(
                                imageVector = PaymentCardDelete24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { sheetDispatch(CheckoutPaymentMethodIntent.AddNewCardClick) }
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = CirclePlus24,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.error
                )

                Text(
                    text = stringResource(ClientStrings.CheckoutPaymentMethodSheetAddCard),
                    style = MaterialTheme.typography.regular15.copy(
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 19.sp,
                        letterSpacing = .2.sp
                    )
                )
            }

            Button(
                onClick = { sheetDispatch(CheckoutPaymentMethodIntent.PayClick) },
                modifier = Modifier
                    .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.isPayEnabled,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = when {
                        state.isLoading -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.disabled
                    },
                    disabledContentColor = when {
                        state.isLoading -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onDisabled
                    }
                )
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(ClientStrings.CheckoutPaymentMethodSheetPay),
                            style = MaterialTheme.typography.medium15.copy(letterSpacing = .3.sp)
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CheckoutPaymentMethodSheetPreview(
    @PreviewParameter(CheckoutPaymentMethodSheetModelProvider::class) state: CheckoutPaymentMethodModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CheckoutPaymentMethodSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CheckoutPaymentMethodSheetModelProvider: PreviewParameterProvider<CheckoutPaymentMethodModel> {

    override val values: Sequence<CheckoutPaymentMethodModel> = sequenceOf(
        CheckoutPaymentMethodModel(),
        CheckoutPaymentMethodModel(
            cards = listOf(
                FittingCheckoutPaymentCardModel(
                    id = "mir-5743",
                    paymentSystem = "MIR",
                    lastFourDigits = "5743"
                ),
                FittingCheckoutPaymentCardModel(
                    id = "mastercard-9990",
                    paymentSystem = "MasterCard",
                    lastFourDigits = "9990"
                ),
                FittingCheckoutPaymentCardModel(
                    id = "visa-2048",
                    paymentSystem = "Visa",
                    lastFourDigits = "2048"
                ),
                FittingCheckoutPaymentCardModel(
                    id = "2049",
                    paymentSystem = "",
                    lastFourDigits = "2049"
                )
            )
        ),
        CheckoutPaymentMethodModel(
            cards = listOf(
                FittingCheckoutPaymentCardModel(
                    id = "mir-5743",
                    paymentSystem = "MIR",
                    lastFourDigits = "5743",
                    isSelected = true
                ),
                FittingCheckoutPaymentCardModel(
                    id = "mastercard-9990",
                    paymentSystem = "MasterCard",
                    lastFourDigits = "9990"
                ),
                FittingCheckoutPaymentCardModel(
                    id = "visa-2048",
                    paymentSystem = "Visa",
                    lastFourDigits = "2048"
                )
            )
        )
    )
}
