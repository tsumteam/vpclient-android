package ru.mercury.vpclient.shared.ui.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import ru.mercury.vpclient.shared.ui.components.video.VideoPlayer
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium18

data class HomeVideoCarouselState(
    val section: HomeSectionEntity
) {
    val videoAspectRatio: Float
        get() = when (section.type) {
            HomeSectionType.VERTICAL_VIDEOS_WITHOUT_TITLE_CAROUSEL,
            HomeSectionType.VERTICAL_VIDEOS_WITH_TITLE_CAROUSEL -> 3F / 4F
            else -> 1F
        }

    val isSectionTitleVisible: Boolean
        get() = section.title.isNotEmpty()

    val isPageIndicatorVisible: Boolean
        get() = section.items.size > 1
}

@Composable
fun HomeVideoCarousel(
    state: HomeVideoCarouselState,
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
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(state.videoAspectRatio)
        ) { itemIndex ->
            VideoPlayer(
                videoUrl = state.section.items.getOrNull(itemIndex)?.videoUrl.orEmpty(),
                isVisible = sectionPagerState.currentPage == itemIndex,
                modifier = Modifier.fillMaxSize()
            )
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
private fun HomeVideoCarouselPreview(
    @PreviewParameter(HomeVideoCarouselStateProvider::class) state: HomeVideoCarouselState
) {
    HomeVideoCarousel(
        state = state
    )
}

private class HomeVideoCarouselStateProvider: PreviewParameterProvider<HomeVideoCarouselState> {
    override val values: Sequence<HomeVideoCarouselState> = sequenceOf(
        HomeVideoCarouselState(
            section = HomeSectionEntity(
                type = HomeSectionType.VERTICAL_VIDEOS_WITH_TITLE_CAROUSEL,
                order = 1,
                title = "ВИДЕО",
                items = listOf(
                    HomeSectionItemEntity(),
                    HomeSectionItemEntity()
                )
            )
        )
    )
}
