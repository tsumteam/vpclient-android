package ru.mercury.vpclient.shared.ui.components.brands

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.ui.icons.Favorited40
import ru.mercury.vpclient.shared.ui.icons.Unfavorited40
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular15

data class BrandFavoriteRowState(
    val entity: CatalogBrandEntity,
    val enabled: Boolean,
    val onClick: () -> Unit,
    val onFavoriteClick: () -> Unit
)

@Composable
fun BrandFavoriteRow(
    state: BrandFavoriteRowState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = state.onClick)
            .padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state.entity.name,
            modifier = Modifier.weight(1F),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        IconButton(
            onClick = state.onFavoriteClick,
            enabled = state.enabled
        ) {
            Icon(
                imageVector = when {
                    state.entity.isFavorite -> Favorited40
                    else -> Unfavorited40
                },
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun BrandFavoriteRowPreview(
    @PreviewParameter(BrandFavoriteRowStateProvider::class) state: BrandFavoriteRowState
) {
    BrandFavoriteRow(
        state = state
    )
}

private class BrandFavoriteRowStateProvider: PreviewParameterProvider<BrandFavoriteRowState> {
    override val values: Sequence<BrandFavoriteRowState> = sequenceOf(
        BrandFavoriteRowState(
            entity = CatalogBrandEntity(
                pairedUserId = "",
                categoryId = 2,
                categoryName = "Женское",
                brandId = 1,
                name = "Alexander McQueen",
                photoUrl = null,
                isTopBrand = false,
                isFavorite = false,
                restrictionType = null
            ),
            enabled = true,
            onClick = {},
            onFavoriteClick = {}
        ),
        BrandFavoriteRowState(
            entity = CatalogBrandEntity(
                pairedUserId = "",
                categoryId = 2,
                categoryName = "Женское",
                brandId = 2,
                name = "Balenciaga",
                photoUrl = null,
                isTopBrand = false,
                isFavorite = true,
                restrictionType = null
            ),
            enabled = false,
            onClick = {},
            onFavoriteClick = {}
        )
    )
}
