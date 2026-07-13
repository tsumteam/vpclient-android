package ru.mercury.vpclient.shared.ui.components.brands

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14

data class BrandGridState(
    val headerState: BrandSectionHeaderState,
    val catalogBrandEntities: List<CatalogBrandEntity>,
    val onBrandClick: (CatalogBrandEntity) -> Unit
) {
    companion object {
        const val COLUMN_COUNT = 3
    }
}

@Composable
fun BrandGrid(
    state: BrandGridState,
    modifier: Modifier = Modifier
) {
    val rowCount = state.catalogBrandEntities.chunked(BrandGridState.COLUMN_COUNT).size

    LazyVerticalGrid(
        columns = GridCells.Fixed(BrandGridState.COLUMN_COUNT),
        modifier = modifier
            .fillMaxWidth()
            .height((44 + rowCount * 48 + rowCount * 8).dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        userScrollEnabled = false
    ) {
        item(
            key = state.headerState.title,
            span = { GridItemSpan(maxLineSpan) }
        ) {
            BrandSectionHeader(
                state = state.headerState,
                modifier = Modifier.animateItem()
            )
        }
        items(
            items = state.catalogBrandEntities,
            key = { entity -> entity.brandId }
        ) { entity ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .animateItem()
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { state.onBrandClick(entity) },
                contentAlignment = Alignment.Center
            ) {
                when {
                    entity.photoUrl.isNullOrEmpty() -> {
                        Text(
                            text = entity.name,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.medium14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    else -> {
                        ClientAsyncImage(
                            imageUrl = entity.photoUrl,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
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
private fun BrandGridPreview(
    @PreviewParameter(BrandGridStateProvider::class) state: BrandGridState
) {
    BrandGrid(
        state = state
    )
}

private class BrandGridStateProvider: PreviewParameterProvider<BrandGridState> {
    override val values: Sequence<BrandGridState> = sequenceOf(
        BrandGridState(
            headerState = BrandSectionHeaderState(
                title = "ТОП-БРЕНДЫ",
                showSelectAll = false,
                onSelectAll = {}
            ),
            catalogBrandEntities = listOf(
                CatalogBrandEntity("", 2, "Женское", 1, "RALPH LAUREN", null, true, true, null),
                CatalogBrandEntity("", 2, "Женское", 2, "SAINT LAURENT", null, true, false, null),
                CatalogBrandEntity("", 2, "Женское", 3, "LOEWE", null, true, false, null),
                CatalogBrandEntity("", 2, "Женское", 4, "BALENCIAGA", null, true, false, null),
                CatalogBrandEntity("", 2, "Женское", 5, "GUCCI", null, true, false, null),
                CatalogBrandEntity("", 2, "Женское", 6, "PRADA", null, true, false, null)
            ),
            onBrandClick = {}
        )
    )
}
