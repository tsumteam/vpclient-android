@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart.fitting_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CartFittingSheet(
    clientName: String,
    clientFeminine: Boolean,
    allProductsCount: Int,
    allProductsSummary: String,
    paymentProductsCount: Int,
    paymentProductsSummary: String,
    hasProductsWithoutSize: Boolean,
    onConfirmClick: (CartFittingSheetOption) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var selectedOption by remember { mutableStateOf(CartFittingSheetOption.AllProducts) }

    val dismiss: () -> Unit = {
        scope.launch {
            sheetState.hide()
            onDismissRequest()
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        CartFittingSheetContent(
            clientName = clientName,
            clientFeminine = clientFeminine,
            selectedOption = selectedOption,
            allProductsCount = allProductsCount,
            allProductsSummary = allProductsSummary,
            paymentProductsCount = paymentProductsCount,
            paymentProductsSummary = paymentProductsSummary,
            hasProductsWithoutSize = hasProductsWithoutSize,
            onOptionClick = { selectedOption = it },
            onConfirmClick = {
                scope.launch {
                    sheetState.hide()
                    onConfirmClick(selectedOption)
                }
            },
            onDismissClick = dismiss
        )
    }
}

@Composable
private fun CartFittingSheetContent(
    clientName: String,
    clientFeminine: Boolean,
    selectedOption: CartFittingSheetOption,
    allProductsCount: Int,
    allProductsSummary: String,
    paymentProductsCount: Int,
    paymentProductsSummary: String,
    hasProductsWithoutSize: Boolean,
    onOptionClick: (CartFittingSheetOption) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Text(
                    text = stringResource(
                        when {
                            clientFeminine -> ClientStrings.CartFittingSheetGreetingFemale
                            else -> ClientStrings.CartFittingSheetGreetingMale
                        },
                        clientName
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 35.dp, end = 16.dp),
                    style = MaterialTheme.typography.livretMedium18.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
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
                CartFittingSheetOptionRow(
                    title = stringResource(ClientStrings.CartFittingSheetAllProducts),
                    subtitle = allProductsSummary,
                    selected = selectedOption == CartFittingSheetOption.AllProducts,
                    enabled = allProductsCount > 0,
                    onClick = { onOptionClick(CartFittingSheetOption.AllProducts) },
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            item {
                CartFittingSheetDivider()
            }

            item {
                CartFittingSheetOptionRow(
                    title = stringResource(ClientStrings.CartFittingSheetPaymentProducts),
                    subtitle = paymentProductsSummary,
                    selected = selectedOption == CartFittingSheetOption.PaymentProducts,
                    enabled = true,
                    onClick = { onOptionClick(CartFittingSheetOption.PaymentProducts) }
                )
            }

            item {
                CartFittingSheetDivider()
            }

            item {
                CartFittingSheetOptionRow(
                    title = stringResource(ClientStrings.CartFittingSheetManual),
                    subtitle = null,
                    selected = selectedOption == CartFittingSheetOption.Manual,
                    enabled = allProductsCount > 0,
                    onClick = { onOptionClick(CartFittingSheetOption.Manual) }
                )
            }

            item {
                CartFittingSheetDivider()
            }

            item {
                CartFittingTabHintText(
                    modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp)
                )
            }

            if (hasProductsWithoutSize) {
                item {
                    CartFittingNoSizeHintText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                    )
                }
            }

            item {
                ClientButton(
                    onClick = onConfirmClick,
                    text = when (selectedOption) {
                        CartFittingSheetOption.AllProducts,
                        CartFittingSheetOption.PaymentProducts -> {
                            stringResource(ClientStrings.CartFittingSheetConfirm)
                        }
                        CartFittingSheetOption.Manual -> stringResource(ClientStrings.CartFittingSheetContinue)
                    },
                    enabled = when (selectedOption) {
                        CartFittingSheetOption.AllProducts -> allProductsCount > 0
                        CartFittingSheetOption.PaymentProducts -> paymentProductsCount > 0
                        CartFittingSheetOption.Manual -> allProductsCount > 0
                    },
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
            }
        }

        IconButton(
            onClick = onDismissClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 8.dp)
        ) {
            Icon(
                imageVector = Close24,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun CartFittingTabHintText(
    modifier: Modifier = Modifier
) {
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
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.regular14.copy(
            color = textColor,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            letterSpacing = .2.sp
        )
    )
}

@Composable
private fun CartFittingNoSizeHintText(
    modifier: Modifier = Modifier
) {
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
        modifier = modifier,
        style = MaterialTheme.typography.regular14.copy(
            color = textColor,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            letterSpacing = .2.sp
        )
    )
}

@Composable
private fun CartFittingSheetOptionRow(
    title: String,
    subtitle: String?,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            enabled = enabled,
            onClick = null
        )

        Column(
            modifier = Modifier
                .weight(1F)
                .padding(start = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.regular14.copy(
                    color = when {
                        enabled -> MaterialTheme.colorScheme.onBackground
                        else -> MaterialTheme.colorScheme.outline
                    },
                    letterSpacing = .2.sp
                )
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = .2.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun CartFittingSheetDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier.padding(start = 52.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingSheetContentPreview() {
    CartFittingSheetContent(
        clientName = "Иван Петрович",
        clientFeminine = false,
        selectedOption = CartFittingSheetOption.AllProducts,
        allProductsCount = 3,
        allProductsSummary = "3 шт на сумму 320 000 ₽",
        paymentProductsCount = 1,
        paymentProductsSummary = "1 шт на сумму 120 000 ₽",
        hasProductsWithoutSize = true,
        onOptionClick = {},
        onConfirmClick = {},
        onDismissClick = {}
    )
}
