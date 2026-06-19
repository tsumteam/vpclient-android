@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.shared.ui.components.video

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular16

@Composable
fun VideoSpeedDropdownMenu(
    expanded: Boolean,
    selectedSpeed: Float,
    onDismissRequest: () -> Unit,
    onSpeedClick: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.width(140.dp),
        offset = DpOffset(x = (-8).dp, y = (-40).dp),
        shape = RoundedCornerShape(14.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Text(
            text = stringResource(ClientStrings.VideoSpeedTitle),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 11.dp),
            style = MaterialTheme.typography.medium15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = .3.sp
            )
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(ClientStrings.VideoSpeed05),
                    style = when (.5F) {
                        selectedSpeed -> {
                            MaterialTheme.typography.medium15.copy(
                                color = MaterialTheme.colorScheme.error,
                                letterSpacing = .3.sp
                            )
                        }
                        else -> {
                            MaterialTheme.typography.regular16.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 20.sp,
                                letterSpacing = .2.sp
                            )
                        }
                    }
                )
            },
            modifier = Modifier.height(40.dp),
            onClick = { onSpeedClick(.5F) }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(ClientStrings.VideoSpeed1),
                    style = when (1F) {
                        selectedSpeed -> {
                            MaterialTheme.typography.medium15.copy(
                                color = MaterialTheme.colorScheme.error,
                                letterSpacing = .3.sp
                            )
                        }
                        else -> {
                            MaterialTheme.typography.regular16.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 20.sp,
                                letterSpacing = .2.sp
                            )
                        }
                    }
                )
            },
            modifier = Modifier.height(40.dp),
            onClick = { onSpeedClick(1F) }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(ClientStrings.VideoSpeed15),
                    style = when (1.5F) {
                        selectedSpeed -> {
                            MaterialTheme.typography.medium15.copy(
                                color = MaterialTheme.colorScheme.error,
                                letterSpacing = .3.sp
                            )
                        }
                        else -> {
                            MaterialTheme.typography.regular16.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 20.sp,
                                letterSpacing = .2.sp
                            )
                        }
                    }
                )
            },
            modifier = Modifier.height(40.dp),
            onClick = { onSpeedClick(1.5F) }
        )

        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(ClientStrings.VideoSpeed2),
                    style = when (2F) {
                        selectedSpeed -> MaterialTheme.typography.medium15.copy(
                            color = MaterialTheme.colorScheme.error,
                            letterSpacing = .3.sp
                        )
                        else -> MaterialTheme.typography.regular16.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 20.sp,
                            letterSpacing = .2.sp
                        )
                    }
                )
            },
            modifier = Modifier.height(40.dp),
            onClick = { onSpeedClick(2F) }
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VideoSpeedDropdownMenuPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        VideoSpeedDropdownMenu(
            expanded = true,
            selectedSpeed = 1F,
            onDismissRequest = {},
            onSpeedClick = {}
        )
    }
}
