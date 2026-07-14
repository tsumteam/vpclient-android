package ru.mercury.vpclient.shared.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionItemEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionType
import ru.mercury.vpclient.shared.ui.components.SharedHorizontalPagerIndicator
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium15
import ru.mercury.vpclient.shared.ui.theme.livretMedium18

data class HomeBannerCarouselState(
    val section: HomeSectionEntity,
    val onItemClick: (HomeSectionItemEntity) -> Unit
) {
    val imageAspectRatio: Float
        get() = when (section.type) {
            HomeSectionType.MEDIUM_BANNERS_WITHOUT_TITLE_CAROUSEL -> 1627F / 1152F
            else -> 1F
        }

    val isSectionTitleVisible: Boolean
        get() = section.title.isNotEmpty()

    val isBannerTitleVisible: Boolean
        get() = section.type == HomeSectionType.BIG_BANNERS_WITH_TITLE_CAROUSEL

    val isPageIndicatorVisible: Boolean
        get() = section.items.size > 1
}

@Composable
fun HomeBannerCarousel(
    state: HomeBannerCarouselState,
    modifier: Modifier = Modifier
) {
    val sectionPagerState = rememberPagerState(
        pageCount = { state.section.items.size.coerceAtLeast(1) }
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
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

        HorizontalPager(
            state = sectionPagerState,
            modifier = Modifier.fillMaxWidth()
        ) { itemIndex ->
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ClientAsyncImage(
                    imageUrl = state.section.items.getOrNull(itemIndex)?.imageUrl.orEmpty(),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(state.imageAspectRatio)
                        .clickable {
                            state.section.items.getOrNull(itemIndex)?.let(state.onItemClick)
                        }
                )

                if (state.isBannerTitleVisible) {
                    Text(
                        text = state.section.items.getOrNull(itemIndex)?.title.orEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.livretMedium15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }

        if (state.isPageIndicatorVisible) {
            SharedHorizontalPagerIndicator(
                pagerState = sectionPagerState,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(16.dp),
                pageCount = state.section.items.size,
                indicatorWidth = 6.dp,
                spacing = 10.dp
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun HomeBannerCarouselPreview(
    @PreviewParameter(HomeBannerCarouselStateProvider::class) state: HomeBannerCarouselState
) {
    HomeBannerCarousel(
        state = state
    )
}

private class HomeBannerCarouselStateProvider: PreviewParameterProvider<HomeBannerCarouselState> {
    override val values: Sequence<HomeBannerCarouselState> = sequenceOf(
        HomeBannerCarouselState(
            section = HomeSectionEntity(
                type = HomeSectionType.BIG_BANNERS_WITH_TITLE_CAROUSEL,
                order = 1,
                title = "ЧТО НОСИТЬ ПРЯМО СЕЙЧАС",
                items = listOf(
                    HomeSectionItemEntity(
                        title = "ВЕЧЕРНЯЯ ПРОГУЛКА В ПАРКЕ"
                    ),
                    HomeSectionItemEntity(
                        title = "МОРСКОЙ БРИЗ"
                    ),
                    HomeSectionItemEntity(
                        title = "ШОРТЫ ДЛЯ ПЛЯЖА И ГОРОДА"
                    )
                )
            ),
            onItemClick = {}
        )
    )
}
