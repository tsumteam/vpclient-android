package ru.mercury.vpclient.features.checkout_payment_result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.checkout_payment_result.intent.CheckoutPaymentResultIntent
import ru.mercury.vpclient.features.checkout_payment_result.model.CheckoutPaymentResultModel
import ru.mercury.vpclient.features.checkout_payment_result.model.CheckoutPaymentResultStatus
import ru.mercury.vpclient.features.checkout_payment_result.navigation.CheckoutPaymentResultRoute
import ru.mercury.vpclient.shared.data.PREFIX_SPACE
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.icons.Logo117
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CheckoutPaymentResultScreen(
    route: CheckoutPaymentResultRoute,
    viewModel: CheckoutPaymentResultViewModel =
        hiltViewModel<CheckoutPaymentResultViewModel, CheckoutPaymentResultViewModelFactory>(
            creationCallback = { factory -> factory.create(route) }
        )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    CheckoutPaymentResultScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun CheckoutPaymentResultScreenContent(
    state: CheckoutPaymentResultModel,
    dispatch: (CheckoutPaymentResultIntent) -> Unit
) {
    SharedScaffold(
        floatingActionButton = {
            Button(
                onClick = { dispatch(CheckoutPaymentResultIntent.CatalogClick) },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CheckoutPaymentResultCatalog),
                    style = MaterialTheme.typography.medium15.copy(
                        letterSpacing = .3.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Logo117,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 84.dp)
                    .size(width = 114.dp, height = 79.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(
                    when (state.status) {
                        CheckoutPaymentResultStatus.Success -> {
                            ClientStrings.CheckoutPaymentResultSuccessTitle
                        }
                        CheckoutPaymentResultStatus.Ordered -> {
                            ClientStrings.CheckoutPaymentResultOrderedTitle
                        }
                        CheckoutPaymentResultStatus.Unpaid -> {
                            ClientStrings.CheckoutPaymentResultUnpaidTitle
                        }
                        CheckoutPaymentResultStatus.Error -> {
                            ClientStrings.CheckoutPaymentResultErrorTitle
                        }
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 72.dp, end = 16.dp),
                style = MaterialTheme.typography.medium18.copy(
                    color = when (state.status) {
                        CheckoutPaymentResultStatus.Unpaid -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onBackground
                    },
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                )
            )

            when (state.status) {
                CheckoutPaymentResultStatus.Success, CheckoutPaymentResultStatus.Ordered -> {
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(ClientStrings.CheckoutPaymentResultSuccessDeliveryPrefix))
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                append(state.deliveryInterval)
                            }
                            if (state.status == CheckoutPaymentResultStatus.Ordered) {
                                append(" (")
                                append(
                                    pluralStringResource(
                                        ClientStrings.FittingConfirmationDeliveryProductsCount,
                                        state.itemsCount,
                                        state.itemsCount
                                    )
                                )
                                append(")")
                            }
                            append(stringResource(ClientStrings.CheckoutPaymentResultSuccessAddressPrefix))
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                append(state.address)
                            }
                            append(stringResource(ClientStrings.CheckoutPaymentResultSuccessSuffix))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        style = MaterialTheme.typography.regular14.copy(
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    Text(
                        text = stringResource(ClientStrings.CheckoutPaymentResultSuccessInfo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 20.dp, end = 16.dp),
                        style = MaterialTheme.typography.regular14.copy(
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                CheckoutPaymentResultStatus.Unpaid -> {
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(ClientStrings.CheckoutPaymentResultUnpaidPrefix))
                            append(PREFIX_SPACE)
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                append(stringResource(ClientStrings.CheckoutPaymentResultUnpaidAccent))
                            }
                            append(stringResource(ClientStrings.CheckoutPaymentResultUnpaidSuffix))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        style = MaterialTheme.typography.regular14.copy(
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                CheckoutPaymentResultStatus.Error -> {
                    Text(
                        text = stringResource(ClientStrings.CheckoutPaymentResultErrorInfo),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        style = MaterialTheme.typography.regular14.copy(
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }

            Button(
                onClick = { dispatch(CheckoutPaymentResultIntent.PurchasesClick) },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CheckoutPaymentResultPurchases),
                    style = MaterialTheme.typography.medium15.copy(
                        letterSpacing = .3.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CheckoutPaymentResultScreenContentPreview(
    @PreviewParameter(CheckoutPaymentResultModelProvider::class) state: CheckoutPaymentResultModel
) {
    CheckoutPaymentResultScreenContent(
        state = state,
        dispatch = {}
    )
}

private class CheckoutPaymentResultModelProvider:
    PreviewParameterProvider<CheckoutPaymentResultModel> {

    override val values: Sequence<CheckoutPaymentResultModel> = sequenceOf(
        CheckoutPaymentResultModel(
            status = CheckoutPaymentResultStatus.Success,
            deliveryInterval = "15 июля с 14:00 до 16:00",
            address = "Москва, Рублевское ш., д. 9, кв./офис 14"
        ),
        CheckoutPaymentResultModel(
            status = CheckoutPaymentResultStatus.Ordered,
            deliveryInterval = "15 июля с 14:00 до 16:00",
            address = "Москва, Рублевское ш., д. 9, кв./офис 14",
            itemsCount = 4
        ),
        CheckoutPaymentResultModel(status = CheckoutPaymentResultStatus.Unpaid),
        CheckoutPaymentResultModel(status = CheckoutPaymentResultStatus.Error)
    )
}
