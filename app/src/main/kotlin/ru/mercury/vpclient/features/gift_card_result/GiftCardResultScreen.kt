package ru.mercury.vpclient.features.gift_card_result

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
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
import ru.mercury.vpclient.features.gift_card_result.intent.GiftCardResultIntent
import ru.mercury.vpclient.features.gift_card_result.model.GiftCardResultModel
import ru.mercury.vpclient.features.gift_card_result.navigation.GiftCardResultRoute
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.icons.Logo117
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun GiftCardResultScreen(
    route: GiftCardResultRoute,
    viewModel: GiftCardResultViewModel = hiltViewModel<GiftCardResultViewModel, GiftCardResultViewModelFactory>(
        creationCallback = { factory -> factory.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    GiftCardResultScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun GiftCardResultScreenContent(
    state: GiftCardResultModel,
    dispatch: (GiftCardResultIntent) -> Unit
) {
    SharedScaffold(
        floatingActionButton = {
            Button(
                onClick = { dispatch(GiftCardResultIntent.HomeClick) },
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
                    text = stringResource(ClientStrings.GiftCardResultHome),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Icon(
                    imageVector = Logo117,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 84.dp)
                        .size(width = 114.dp, height = 79.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Text(
                    text = stringResource(
                        when {
                            state.isPaid -> ClientStrings.GiftCardResultSuccessTitle
                            else -> ClientStrings.GiftCardResultNotPaidTitle
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 72.dp, end = 16.dp),
                    style = MaterialTheme.typography.medium18.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            when {
                state.isPaid -> {
                    item {
                        Text(
                            text = buildAnnotatedString {
                                append(stringResource(ClientStrings.GiftCardResultSuccessOrder, state.orderNumber))
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                    append(stringResource(ClientStrings.GiftCardResultSuccessDate, state.deliveryDate, state.deliveryTime))
                                }
                                append(stringResource(ClientStrings.GiftCardResultSuccessEmail))
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                    append(state.email)
                                }
                                append(stringResource(ClientStrings.GiftCardResultSuccessPin))
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) {
                                    append(state.phone)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    item {
                        Text(
                            text = stringResource(ClientStrings.GiftCardResultSuccessInfo),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 20.dp, end = 16.dp),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                else -> {
                    item {
                        Text(
                            text = stringResource(ClientStrings.GiftCardResultNotPaidInfo),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
            item {
                Button(
                    onClick = { dispatch(GiftCardResultIntent.PurchasesClick) },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(46.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.GiftCardResultPurchases),
                        style = MaterialTheme.typography.medium15.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
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

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun GiftCardResultScreenContentPreview(
    @PreviewParameter(GiftCardResultModelPreviewParameterProvider::class) state: GiftCardResultModel
) {
    GiftCardResultScreenContent(
        state = state,
        dispatch = {}
    )
}

private class GiftCardResultModelPreviewParameterProvider: PreviewParameterProvider<GiftCardResultModel> {
    override val values: Sequence<GiftCardResultModel> = sequenceOf(
        GiftCardResultModel(
            isPaid = true,
            orderNumber = "1111-2222-U",
            email = "ivanov@mail.ru",
            phone = "+79859988777",
            deliveryDate = "25 октября",
            deliveryTime = "12:00"
        ),
        GiftCardResultModel(isPaid = false)
    )
}
