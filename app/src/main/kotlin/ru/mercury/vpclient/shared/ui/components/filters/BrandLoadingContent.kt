package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer

@Composable
fun BrandLoadingContent(
    showButton: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 88.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = false
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
            }
            item {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .width(112.dp)
                        .height(20.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
            item {
                Column(
                    modifier = Modifier.padding(top = 16.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(3) {
                                Box(
                                    modifier = Modifier
                                        .weight(1F)
                                        .height(46.dp)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .width(112.dp)
                        .height(20.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
            item {
                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }
            items(8) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .height(48.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(52.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun BrandLoadingContentPreview() {
    BrandLoadingContent(
        showButton = true,
        modifier = Modifier.fillMaxSize()
    )
}
