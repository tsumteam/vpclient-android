@file:OptIn(ExperimentalFoundationApi::class)

package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.surface4
import kotlin.math.absoluteValue
import kotlin.math.sign

@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    pageCount: Int = pagerState.pageCount,
    pageIndexMapping: (Int) -> Int = { it },
    activeColor: Color = MaterialTheme.colorScheme.onBackground,
    inactiveColor: Color = MaterialTheme.colorScheme.surface4,
    indicatorWidth: Dp = 5.dp,
    indicatorHeight: Dp = indicatorWidth,
    spacing: Dp = 4.dp,
    indicatorShape: Shape = CircleShape
) {
    val indicatorWidthPx = LocalDensity.current.run {
        return@run indicatorWidth.roundToPx()
    }
    val spacingPx = LocalDensity.current.run {
        return@run spacing.roundToPx()
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val indicatorModifier = Modifier
                .size(width = indicatorWidth, height = indicatorHeight)
                .background(color = inactiveColor, shape = indicatorShape)

            repeat(pageCount) {
                Box(modifier = indicatorModifier)
            }
        }

        Box(
            modifier = Modifier
                .offset {
                    if (pageCount <= 0) return@offset IntOffset.Zero
                    val currentPosition = pageIndexMapping(pagerState.currentPage).coerceIn(0, pageCount - 1)
                    val pageOffset = pagerState.currentPageOffsetFraction
                    val targetPage = pagerState.currentPage + pageOffset.sign.toInt()
                    val nextPosition = pageIndexMapping(targetPage).coerceIn(0, pageCount - 1)
                    val scrollPosition = ((nextPosition - currentPosition) * pageOffset.absoluteValue + currentPosition).coerceIn(0F, (pageCount - 1).toFloat())
                    IntOffset(x = ((spacingPx + indicatorWidthPx) * scrollPosition).toInt(), y = 0)
                }
                .size(width = indicatorWidth, height = indicatorHeight)
                .background(color = activeColor, shape = indicatorShape)
        )
    }
}

@FontScalePreviews
@Composable
private fun HorizontalPagerIndicatorPreview() {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 4 }
    )

    ClientTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                indicatorWidth = 5.dp,
                indicatorHeight = 5.dp,
                spacing = 4.dp
            )
        }
    }
}
