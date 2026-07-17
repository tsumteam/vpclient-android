package ru.mercury.vpclient.shared.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.placeholder

data class SharedTabRowState(
    val selectedIndex: Int,
    val firstTabText: String,
    val secondTabText: String,
    val onFirstTabClick: () -> Unit,
    val onSecondTabClick: () -> Unit,
    val isLoading: Boolean
)

@Composable
fun SharedTabRow(
    state: SharedTabRowState,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = when {
                    state.isLoading -> Color.Transparent
                    else -> MaterialTheme.colorScheme.surfaceContainer
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp)
    ) {
        val tabWidth = maxWidth / 2

        when {
            state.isLoading -> {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .weight(1F)
                            .height(36.dp)
                            .placeholder(shape = RoundedCornerShape(10.dp))
                    )

                    Spacer(
                        modifier = Modifier
                            .weight(1F)
                            .height(36.dp)
                            .placeholder(shape = RoundedCornerShape(10.dp))
                    )
                }
            }
            else -> {
                val indicatorOffset = animateDpAsState(
                    targetValue = when (state.selectedIndex) {
                        0 -> 0.dp
                        else -> tabWidth
                    },
                    label = "SharedTabRowIndicatorOffset"
                )

                Box(
                    modifier = Modifier
                        .offset { IntOffset(x = indicatorOffset.value.roundToPx(), y = 0) }
                        .width(tabWidth)
                        .height(36.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(10.dp),
                            clip = false
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.background)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickableWithoutRipple(onClick = state.onFirstTabClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.firstTabText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = textStyle.copy(
                                color = when (state.selectedIndex) {
                                    0 -> MaterialTheme.colorScheme.onBackground
                                    else -> MaterialTheme.colorScheme.outline
                                },
                                lineHeight = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .height(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickableWithoutRipple(onClick = state.onSecondTabClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.secondTabText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = textStyle.copy(
                                color = when (state.selectedIndex) {
                                    1 -> MaterialTheme.colorScheme.onBackground
                                    else -> MaterialTheme.colorScheme.outline
                                },
                                lineHeight = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SharedTabRowPreview(
    @PreviewParameter(SharedTabRowStateProvider::class) state: SharedTabRowState
) {
    SharedTabRow(
        state = state,
        textStyle = MaterialTheme.typography.labelMedium
    )
}

private class SharedTabRowStateProvider: PreviewParameterProvider<SharedTabRowState> {
    override val values: Sequence<SharedTabRowState> = sequenceOf(
        SharedTabRowState(
            selectedIndex = 0,
            firstTabText = "Первая вкладка",
            secondTabText = "Вторая вкладка",
            onFirstTabClick = {},
            onSecondTabClick = {},
            isLoading = false
        ),
        SharedTabRowState(
            selectedIndex = 1,
            firstTabText = "Первая вкладка",
            secondTabText = "Вторая вкладка",
            onFirstTabClick = {},
            onSecondTabClick = {},
            isLoading = false
        ),
        SharedTabRowState(
            selectedIndex = 0,
            firstTabText = "Первая вкладка",
            secondTabText = "Вторая вкладка",
            onFirstTabClick = {},
            onSecondTabClick = {},
            isLoading = true
        )
    )
}
