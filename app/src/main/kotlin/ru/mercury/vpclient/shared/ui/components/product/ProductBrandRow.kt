package ru.mercury.vpclient.shared.ui.components.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FavoriteBrandEntity
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

data class ProductBrandRowState(
    val entity: FavoriteBrandEntity,
    val onCloseClick: () -> Unit
)

@Composable
fun ProductBrandRow(
    state: ProductBrandRowState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 16.dp, end = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProductBrandBox(
            entity = BrandEntity(
                brand = state.entity.name,
                urlBrandLogo = state.entity.photoUrl
            ),
            modifier = Modifier
                .weight(1F)
                .height(32.dp)
        )

        IconButton(
            onClick = state.onCloseClick
        ) {
            Icon(
                imageVector = Close24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ProductBrandRowPreview(
    @PreviewParameter(ProductBrandRowStateProvider::class) state: ProductBrandRowState
) {
    ProductBrandRow(
        state = state
    )
}

private class ProductBrandRowStateProvider: PreviewParameterProvider<ProductBrandRowState> {

    override val values: Sequence<ProductBrandRowState> = sequenceOf(
        ProductBrandRowState(
            entity = FavoriteBrandEntity(
                pairedUserId = "",
                categoryId = 2,
                categoryName = "Женское",
                brandId = 1,
                name = "MVST",
                photoUrl = null,
                isTopBrand = false,
                isFavorite = true,
                restrictionType = null,
                position = 0
            ),
            onCloseClick = {}
        ),
        ProductBrandRowState(
            entity = FavoriteBrandEntity(
                pairedUserId = "",
                categoryId = 2,
                categoryName = "Женское",
                brandId = 2,
                name = "BALMAIN",
                photoUrl = "https://example.com/brand-logo.png",
                isTopBrand = true,
                isFavorite = true,
                restrictionType = null,
                position = 1
            ),
            onCloseClick = {}
        )
    )
}
