package ru.mercury.vpclient.shared.ui.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.ui.components.SharedLazyRow
import ru.mercury.vpclient.shared.ui.components.brands.BrandBox
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretRegular11

data class SearchViewHistoryRowState(
    val productEntities: List<CatalogFilterProductsEntity>,
    val onProductClick: (CatalogFilterProductsEntity) -> Unit
)

@Composable
fun SearchViewHistoryRow(
    state: SearchViewHistoryRowState,
    modifier: Modifier = Modifier
) {
    SharedLazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(156.dp)
    ) {
        items(
            items = state.productEntities,
            key = { entity -> entity.id }
        ) { entity ->
            Column(
                modifier = Modifier
                    .size(
                        width = 112.dp,
                        height = 156.dp
                    )
                    .clickable { state.onProductClick(entity) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ClientAsyncImage(
                    imageUrl = entity.imageUrl,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(
                            width = 62.dp,
                            height = 96.dp
                        ),
                    contentScale = ContentScale.Fit
                )

                BrandBox(
                    entity = BrandEntity(
                        brand = entity.brand,
                        urlBrandLogo = entity.urlBrandLogo
                    ),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(
                            width = 96.dp,
                            height = 20.dp
                        ),
                    style = MaterialTheme.typography.livretRegular11
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun SearchViewHistoryRowPreview(
    @PreviewParameter(SearchViewHistoryRowStateProvider::class) state: SearchViewHistoryRowState
) {
    SearchViewHistoryRow(
        state = state
    )
}

private class SearchViewHistoryRowStateProvider: PreviewParameterProvider<SearchViewHistoryRowState> {
    override val values: Sequence<SearchViewHistoryRowState> = sequenceOf(
        SearchViewHistoryRowState(
            productEntities = listOf(
                CatalogFilterProductsEntity.Empty.copy(
                    id = "1",
                    brand = "BALMAIN",
                    imageUrl = ""
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    id = "2",
                    brand = "DOLCE&GABBANA",
                    imageUrl = ""
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    id = "3",
                    brand = "MVST",
                    imageUrl = ""
                )
            ),
            onProductClick = {}
        )
    )
}
