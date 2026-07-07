package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.components.product.ProductBrandBox
import ru.mercury.vpclient.shared.ui.components.product.ProductSizeButtonsRow
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14

data class CompilationPreviewBasketProductCardState(
    val entity: CatalogFilterProductsEntity,
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit,
    val sizeSelectorState: SizeSelectorState? = null
)

@Composable
fun CompilationPreviewBasketProductCard(
    state: CompilationPreviewBasketProductCardState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 122.dp)
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

            Row(
                modifier = Modifier
                    .weight(1F)
                    .heightIn(min = 96.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                ClientAsyncImage(
                    imageUrl = state.entity.imageUrl,
                    modifier = Modifier.size(width = 64.dp, height = 96.dp),
                    contentScale = ContentScale.Fit
                )

                Column(
                    modifier = Modifier.weight(1F),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ProductBrandBox(
                        entity = BrandEntity(
                            brand = state.entity.brand,
                            urlBrandLogo = state.entity.urlBrandLogo
                        ),
                        modifier = Modifier.height(24.dp)
                    )

                    Text(
                        text = state.entity.name,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )

                    Text(
                        text = state.entity.colorId,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp
                        )
                    )

                    Text(
                        text = stringResource(ClientStrings.CartArticle, state.entity.itemId),
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

        state.sizeSelectorState?.let { sizeSelectorState ->
            ProductSizeButtonsRow(
                state = sizeSelectorState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp)
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CompilationPreviewBasketProductCardPreview(
    @PreviewParameter(CompilationPreviewBasketProductCardStateProvider::class) state: CompilationPreviewBasketProductCardState
) {
    CompilationPreviewBasketProductCard(
        state = state
    )
}

private class CompilationPreviewBasketProductCardStateProvider:
    PreviewParameterProvider<CompilationPreviewBasketProductCardState> {

    private val entity = CatalogFilterProductsEntity.Empty.copy(
        id = "preview-1",
        itemId = "5558447",
        colorId = "black",
        brand = "SAINT LAURENT",
        name = "Куртка из кожи",
        price = 129_900.0,
        imageUrl = ""
    )

    override val values: Sequence<CompilationPreviewBasketProductCardState> = sequenceOf(
        CompilationPreviewBasketProductCardState(
            entity = entity,
            checked = true,
            onCheckedChange = {}
        ),
        CompilationPreviewBasketProductCardState(
            entity = entity,
            checked = false,
            onCheckedChange = {}
        ),
        CompilationPreviewBasketProductCardState(
            entity = entity,
            checked = true,
            onCheckedChange = {},
            sizeSelectorState = SizeSelectorState(
                sizes = listOf(
                    SizeState(topText = "IT 44", bottomText = "RU 44", selected = false, enabled = true),
                    SizeState(topText = "IT 46", bottomText = "RU 46", selected = true, enabled = true),
                    SizeState(topText = "IT 48", bottomText = "RU 48", selected = false, enabled = false),
                    SizeState(topText = "IT 50", bottomText = "RU 50", selected = false, enabled = true)
                ),
                topText = "IT",
                bottomText = "RU",
                isSizeTableVisible = true,
                isSizeSelectTextVisible = false
            )
        )
    )
}
