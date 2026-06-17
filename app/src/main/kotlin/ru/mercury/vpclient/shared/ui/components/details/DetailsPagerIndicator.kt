package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.components.HorizontalPagerIndicator
import ru.mercury.vpclient.shared.ui.icons.Video24
import ru.mercury.vpclient.shared.ui.preview.BooleanPreviewParameterProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun DetailsPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    showVideoIcon: Boolean,
    modifier: Modifier = Modifier,
    onVideoClick: () -> Unit = {},
    pageIndexMapping: (Int) -> Int = { it }
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = pageCount,
            pageIndexMapping = pageIndexMapping,
            activeColor = MaterialTheme.colorScheme.onBackground,
            inactiveColor = MaterialTheme.colorScheme.outlineVariant,
            indicatorWidth = 6.dp,
            indicatorHeight = 6.dp,
            spacing = 10.dp
        )

        if (showVideoIcon) {
            val isLastPage = pageIndexMapping(pagerState.currentPage) == pageCount - 1
            val videoIconColor by animateColorAsState(
                targetValue = if (isLastPage) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.outline
            )

            IconButton(
                onClick = onVideoClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Video24,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = videoIconColor
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun DetailsPagerIndicatorPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) showVideoIcon: Boolean
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 5 }
    )

    DetailsPagerIndicator(
        pagerState = pagerState,
        pageCount = 5,
        showVideoIcon = showVideoIcon,
        onVideoClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
    )
}
