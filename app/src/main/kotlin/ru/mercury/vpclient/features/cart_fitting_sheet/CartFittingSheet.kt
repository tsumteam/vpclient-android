@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_fitting_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart_fitting_sheet.intent.CartFittingSheetIntent
import ru.mercury.vpclient.features.cart_fitting_sheet.model.CartFittingSheetModel
import ru.mercury.vpclient.shared.data.entity.CartFittingSheetOption
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CartFittingSheet(
    state: CartFittingSheetModel,
    dispatch: (CartFittingSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CartFittingSheetIntent) -> Unit = { intent ->
        when (intent) {
            is CartFittingSheetIntent.ConfirmClick -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is CartFittingSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { sheetDispatch(CartFittingSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        var selectedOption by remember { mutableStateOf(CartFittingSheetOption.AllProducts) }

        SharedLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            item {
                CenterAlignedTopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .padding(end = 48.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Text(
                                text = stringResource(
                                    when {
                                        state.clientFeminine -> ClientStrings.CartFittingSheetGreetingFemale
                                        else -> ClientStrings.CartFittingSheetGreetingMale
                                    },
                                    state.clientName
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp),
                                style = MaterialTheme.typography.livretMedium18.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { sheetDispatch(CartFittingSheetIntent.DismissRequest) }
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
            item {
                Text(
                    text = stringResource(ClientStrings.CartFittingSheetDescription),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 6.dp, end = 16.dp),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        letterSpacing = .2.sp
                    )
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable(
                            enabled = state.allProductsCount > 0,
                            onClick = { selectedOption = CartFittingSheetOption.AllProducts }
                        )
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == CartFittingSheetOption.AllProducts,
                        enabled = state.allProductsCount > 0,
                        onClick = null
                    )

                    Column(
                        modifier = Modifier.weight(1F),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(ClientStrings.CartFittingSheetAllProducts),
                            style = MaterialTheme.typography.regular14.copy(
                                color = when {
                                    state.allProductsCount > 0 -> MaterialTheme.colorScheme.onBackground
                                    else -> MaterialTheme.colorScheme.outline
                                },
                                letterSpacing = .2.sp
                            )
                        )

                        Text(
                            text = state.allProductsSummary,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 52.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable { selectedOption = CartFittingSheetOption.PaymentProducts }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == CartFittingSheetOption.PaymentProducts,
                        enabled = true,
                        onClick = null
                    )

                    Column(
                        modifier = Modifier.weight(1F),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(ClientStrings.CartFittingSheetPaymentProducts),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                letterSpacing = .2.sp
                            )
                        )

                        Text(
                            text = state.paymentProductsSummary,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                letterSpacing = .2.sp
                            )
                        )
                    }
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 52.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable(
                            enabled = state.allProductsCount > 0,
                            onClick = { selectedOption = CartFittingSheetOption.Manual }
                        )
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == CartFittingSheetOption.Manual,
                        enabled = state.allProductsCount > 0,
                        onClick = null
                    )

                    Column(
                        modifier = Modifier.weight(1F),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(ClientStrings.CartFittingSheetManual),
                            style = MaterialTheme.typography.regular14.copy(
                                color = when {
                                    state.allProductsCount > 0 -> MaterialTheme.colorScheme.onBackground
                                    else -> MaterialTheme.colorScheme.outline
                                },
                                letterSpacing = .2.sp
                            )
                        )
                    }
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 52.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
            item {
                val textColor = MaterialTheme.colorScheme.onBackground
                val accentColor = MaterialTheme.colorScheme.error
                val prefix = stringResource(ClientStrings.CartFittingSheetFittingTabHintPrefix)
                val accent = stringResource(ClientStrings.CartFittingSheetFittingTabHintAccent)
                val suffix = stringResource(ClientStrings.CartFittingSheetFittingTabHintSuffix)

                Text(
                    text = buildAnnotatedString {
                        append(prefix)
                        append(" ")
                        withStyle(
                            style = SpanStyle(
                                color = accentColor
                            )
                        ) {
                            append(accent)
                        }
                        append(suffix)
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.regular14.copy(
                        color = textColor,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = .2.sp
                    )
                )
            }
            if (state.hasProductsWithoutSize) {
                item {
                    val textColor = MaterialTheme.colorScheme.onBackground
                    val accentColor = MaterialTheme.colorScheme.error
                    val hint = stringResource(ClientStrings.CartFittingSheetNoSizeHint)

                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = accentColor
                                )
                            ) {
                                append("*")
                            }
                            append(" ")
                            append(hint)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                        style = MaterialTheme.typography.regular14.copy(
                            color = textColor,
                            lineHeight = 18.sp,
                            textAlign = TextAlign.Center,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }
            item {
                Button(
                    onClick = { sheetDispatch(CartFittingSheetIntent.ConfirmClick(selectedOption)) },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = when (selectedOption) {
                        CartFittingSheetOption.AllProducts -> state.allProductsCount > 0
                        CartFittingSheetOption.PaymentProducts -> state.paymentProductsCount > 0
                        CartFittingSheetOption.Manual -> state.allProductsCount > 0
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.disabled,
                        disabledContentColor = MaterialTheme.colorScheme.onDisabled
                    )
                ) {
                    Text(
                        text = when (selectedOption) {
                            CartFittingSheetOption.AllProducts,
                            CartFittingSheetOption.PaymentProducts -> {
                                stringResource(ClientStrings.CartFittingSheetConfirm)
                            }
                            CartFittingSheetOption.Manual -> stringResource(ClientStrings.CartFittingSheetContinue)
                        },
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
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
private fun CartFittingSheetPreview(
    @PreviewParameter(CartFittingSheetModelProvider::class) state: CartFittingSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CartFittingSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CartFittingSheetModelProvider: PreviewParameterProvider<CartFittingSheetModel> {
    override val values: Sequence<CartFittingSheetModel> = sequenceOf(
        CartFittingSheetModel(
            clientName = "Иван Петрович",
            clientFeminine = false,
            allProductsCount = 3,
            allProductsSummary = "3 шт на сумму 320 000 ₽",
            paymentProductsCount = 1,
            paymentProductsSummary = "1 шт на сумму 120 000 ₽",
            hasProductsWithoutSize = true
        ),
        CartFittingSheetModel(
            clientName = "Анна Петровна",
            clientFeminine = true,
            allProductsCount = 0,
            allProductsSummary = "0 шт на сумму 0 ₽",
            paymentProductsCount = 0,
            paymentProductsSummary = "0 шт на сумму 0 ₽",
            hasProductsWithoutSize = false
        )
    )
}
