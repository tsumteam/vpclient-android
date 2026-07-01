package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity
import ru.mercury.vpclient.shared.ui.components.cart.CartAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular14

data class FittingProductRowState(
    val cartProductEntity: CartProductEntity,
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
) {
    val sizeNames: List<String>
        get() {
            val selectedSizeNames = cartProductEntity.sizeItems
                .map { size -> size.name }
                .filter { size -> size.isNotBlank() }

            return when {
                selectedSizeNames.isNotEmpty() -> selectedSizeNames
                else -> cartProductEntity.size
                    .split(",")
                    .map { size -> size.trim() }
                    .filter { size -> size.isNotEmpty() }
                    .takeIf { sizes -> sizes.isNotEmpty() } ?: listOf(cartProductEntity.size)
            }
        }
}

@Composable
fun FittingProductRow(
    state: FittingProductRowState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .clickable { state.onCheckedChange(!state.checked) }
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.checked,
            onCheckedChange = state.onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.outline,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        CartAsyncImage(
            imageUrl = state.cartProductEntity.imageUrl,
            modifier = Modifier.size(width = 64.dp, height = 96.dp)
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.cartProductEntity.brand,
                    modifier = Modifier.weight(1F),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.medium14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .2.sp
                    )
                )

                Text(
                    text = state.cartProductEntity.price,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.medium14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.End
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = state.cartProductEntity.name,
                    modifier = Modifier.weight(1F),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    state.sizeNames.forEach { size ->
                        Text(
                            text = size,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.End
                            )
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingProductRowPreview(
    @PreviewParameter(FittingProductRowStateProvider::class) state: FittingProductRowState
) {
    FittingProductRow(
        state = state
    )
}

private class FittingProductRowStateProvider: PreviewParameterProvider<FittingProductRowState> {
    override val values: Sequence<FittingProductRowState> = sequenceOf(
        FittingProductRowState(
            cartProductEntity = CartProductEntity(
                id = "1",
                position = 0,
                detailId = "1",
                itemId = "1",
                colorId = "1",
                brand = "BRUNELLO CUCINELLI",
                urlBrandLogo = null,
                name = "Хлопковая футболка с логотипом",
                article = "MP827743",
                color = "Белый",
                size = "IT 48",
                price = "1 600 000 ₽",
                oldPrice = null,
                lookId = "look_1",
                lookName = "Образ",
                lookImageUrl = "",
                imageUrl = "",
                imageUrls = emptyList(),
                isForPayment = true,
                isSold = false,
                isLastInStock = false,
                hasActions = false,
                isAlternativesPaletteOpen = false,
                isAlternativePaletteControlsAvailable = false,
                isSwitchAlternativeBackToOriginalAvailable = false,
                alternatives = emptyList(),
                discountPercentage = 0,
                quantity = 1,
                sizeCount = 1,
                sizeId = "48",
                sizeItems = emptyList(),
                priceValue = 1_600_000.0
            ),
            checked = true,
            onCheckedChange = {}
        ),
        FittingProductRowState(
            cartProductEntity = CartProductEntity(
                id = "2",
                position = 1,
                detailId = "2",
                itemId = "2",
                colorId = "2",
                brand = "SAINT LAURENT",
                urlBrandLogo = null,
                name = "Кожаная куртка",
                article = "SL908221",
                color = "Черный",
                size = "FR 38, FR 40",
                price = "300 000 ₽",
                oldPrice = null,
                lookId = null,
                lookName = null,
                lookImageUrl = null,
                imageUrl = "",
                imageUrls = emptyList(),
                isForPayment = false,
                isSold = false,
                isLastInStock = false,
                hasActions = false,
                isAlternativesPaletteOpen = false,
                isAlternativePaletteControlsAvailable = false,
                isSwitchAlternativeBackToOriginalAvailable = false,
                alternatives = emptyList(),
                discountPercentage = 0,
                quantity = 1,
                priceValue = 300_000.0,
                sizeCount = 2,
                sizeId = "38",
                sizeItems = listOf(
                    CartProductSize(
                        id = "38",
                        name = "FR 38",
                        productId = "2"
                    ),
                    CartProductSize(
                        id = "40",
                        name = "FR 40",
                        productId = "2"
                    )
                )
            ),
            checked = false,
            onCheckedChange = {}
        )
    )
}
