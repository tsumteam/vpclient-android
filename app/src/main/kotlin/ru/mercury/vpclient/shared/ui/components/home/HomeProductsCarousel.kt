package ru.mercury.vpclient.shared.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionItemEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionType
import ru.mercury.vpclient.shared.ui.components.brands.BrandBox
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.livretRegular15
import ru.mercury.vpclient.shared.ui.theme.regular14

data class HomeProductsCarouselState(
    val section: HomeSectionEntity,
    val onProductClick: (HomeSectionItemEntity) -> Unit,
    val onViewMoreClick: () -> Unit
) {
    val isSectionTitleVisible: Boolean
        get() = section.title.isNotEmpty()

    fun isProductClickEnabled(item: HomeSectionItemEntity): Boolean {
        return item.productId != null
    }

    fun brandEntity(item: HomeSectionItemEntity): BrandEntity {
        return BrandEntity(
            brand = item.brand,
            urlBrandLogo = item.brandLogoUrl
        )
    }
}

@Composable
fun HomeProductsCarousel(
    state: HomeProductsCarouselState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = state.onViewMoreClick),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (state.isSectionTitleVisible) {
            Text(
                text = state.section.title,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                style = MaterialTheme.typography.livretMedium18.copy(
                    color = MaterialTheme.colorScheme.error,
                    lineHeight = 26.sp,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Center
                )
            )
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(493.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.section.items
            ) { item ->
                Column(
                    modifier = Modifier
                        .size(width = 262.dp, height = 493.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable(
                            enabled = state.isProductClickEnabled(item),
                            onClick = { state.onProductClick(item) }
                        ),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ClientAsyncImage(
                        imageUrl = item.imageUrl,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(width = 262.dp, height = 393.dp)
                    )

                    BrandBox(
                        entity = state.brandEntity(item),
                        modifier = Modifier.size(width = 180.dp, height = 50.dp)
                    )

                    Text(
                        text = item.title,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 18.sp,
                            letterSpacing = .2.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .width(84.dp)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(ClientStrings.HomeWatchMore),
                        modifier = Modifier
                            .size(84.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RoundedCornerShape(18.dp)
                            )
                            .wrapContentHeight(Alignment.CenterVertically),
                        style = MaterialTheme.typography.livretRegular15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(widthDp = 570, showBackground = true)
@Composable
private fun HomeProductsCarouselPreview(
    @PreviewParameter(HomeProductsCarouselStateProvider::class) state: HomeProductsCarouselState
) {
    HomeProductsCarousel(
        state = state
    )
}

private class HomeProductsCarouselStateProvider: PreviewParameterProvider<HomeProductsCarouselState> {
    override val values: Sequence<HomeProductsCarouselState> = sequenceOf(
        HomeProductsCarouselState(
            section = HomeSectionEntity(
                type = HomeSectionType.PRODUCTS_CAROUSEL,
                order = 1,
                title = "БЕСТСЕЛЛЕРЫ",
                items = listOf(
                    HomeSectionItemEntity(
                        title = "Платье из шелка",
                        brand = "DOLCE & GABBANA",
                        productId = "preview-product-1"
                    ),
                    HomeSectionItemEntity(
                        title = "Жакет из шерсти",
                        brand = "SAINT LAURENT",
                        productId = "preview-product-2"
                    )
                )
            ),
            onProductClick = {},
            onViewMoreClick = {}
        )
    )
}
