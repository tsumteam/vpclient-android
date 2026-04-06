package ru.mercury.vpclient.core.ui.components.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.components.HorizontalPagerIndicator
import ru.mercury.vpclient.core.ui.icons.Video24
import ru.mercury.vpclient.core.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.secondary4
import ru.mercury.vpclient.core.ui.theme.secondary5

@Composable
fun DetailsPagerIndicator(
    pagerState: PagerState,
    pageCount: Int,
    showVideoIcon: Boolean,
    modifier: Modifier = Modifier,
    pageIndexMapping: (Int) -> Int = { it }
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = pageCount,
            pageIndexMapping = pageIndexMapping,
            activeColor = MaterialTheme.colorScheme.onBackground,
            inactiveColor = MaterialTheme.colorScheme.secondary5,
            indicatorWidth = 6.dp,
            indicatorHeight = 6.dp,
            spacing = 10.dp
        )

        if (showVideoIcon) {
            val isLastPage = pageIndexMapping(pagerState.currentPage) == pageCount - 1
            val videoIconColor by animateColorAsState(
                targetValue = if (isLastPage) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary4
            )

            Icon(
                imageVector = Video24,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(24.dp),
                tint = videoIconColor
            )
        }
    }
}

@FontScalePreviews
@Composable
private fun DetailsPagerIndicatorPreview(
    @PreviewParameter(BooleanParameterProvider::class) showVideoIcon: Boolean
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 5 }
    )

    ClientTheme {
        DetailsPagerIndicator(
            pagerState = pagerState,
            pageCount = 5,
            showVideoIcon = showVideoIcon,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
        )
    }
}
