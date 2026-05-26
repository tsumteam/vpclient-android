@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CartFittingProductsSheet(
    products: List<CartProduct>,
    onConfirmClick: (List<String>) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var selectedProductIds by remember(products) { mutableStateOf(emptySet<String>()) }

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
        CartFittingProductsSheetContent(
            products = products,
            selectedProductIds = selectedProductIds,
            onProductCheckedChange = { productId, checked ->
                selectedProductIds = when {
                    checked -> selectedProductIds + productId
                    else -> selectedProductIds - productId
                }
            },
            onConfirmClick = {
                scope.launch {
                    sheetState.hide()
                    onConfirmClick(selectedProductIds.toList())
                }
            },
            onDismissClick = dismiss
        )
    }
}

@Composable
private fun CartFittingProductsSheetContent(
    products: List<CartProduct>,
    selectedProductIds: Set<String>,
    onProductCheckedChange: (String, Boolean) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp)
        ) {
            IconButton(
                onClick = onDismissClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Close24,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = stringResource(ClientStrings.CartFittingProductsSheetTitle),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 56.dp),
                style = MaterialTheme.typography.livretMedium18.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            itemsIndexed(
                items = products,
                key = { _, product -> product.id }
            ) { index, product ->
                CartFittingProductRow(
                    product = product,
                    checked = selectedProductIds.contains(product.id),
                    onCheckedChange = { checked -> onProductCheckedChange(product.id, checked) }
                )

                if (index < products.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }

        ClientButton(
            onClick = onConfirmClick,
            text = when {
                selectedProductIds.isEmpty() -> stringResource(ClientStrings.CartFittingProductsSheetSelect)
                else -> stringResource(ClientStrings.CartFittingSheetConfirm)
            },
            enabled = selectedProductIds.isNotEmpty(),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
        )
    }
}

@Composable
private fun CartFittingProductRow(
    product: CartProduct,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .clickable { onCheckedChange(!checked) }
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.outline,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        CartAsyncImage(
            imageUrl = product.imageUrl,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(width = 64.dp, height = 96.dp)
        )

        Column(
            modifier = Modifier
                .weight(1F)
                .padding(start = 12.dp)
        ) {
            Text(
                text = product.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = product.name,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        Column(
            modifier = Modifier
                .width(112.dp)
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = product.price,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = product.size,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingProductsSheetContentPreview() {
    val products = CartProductProvider().values.toList().filter { it.size.isNotBlank() && !it.isSold }

    CartFittingProductsSheetContent(
        products = products,
        selectedProductIds = setOf("1"),
        onProductCheckedChange = { _, _ -> },
        onConfirmClick = {},
        onDismissClick = {}
    )
}
