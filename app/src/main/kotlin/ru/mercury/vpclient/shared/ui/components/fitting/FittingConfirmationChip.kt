package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.theme.medium12

private val FittingConfirmationIntervalShape = RoundedCornerShape(4.dp)
private val FittingConfirmationSelectedIntervalShape = RoundedCornerShape(6.dp)

@Composable
fun FittingConfirmationChip(
    text: String,
    selected: Boolean,
    height: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipContent: @Composable () -> Unit = {
        Surface(
            modifier = Modifier.height(height),
            shape = FittingConfirmationIntervalShape,
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Box(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    maxLines = 1,
                    style = MaterialTheme.typography.medium12.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier
            .height(height + 6.dp)
            .border(
                width = 2.dp,
                color = when {
                    selected -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.background
                },
                shape = FittingConfirmationSelectedIntervalShape
            )
            .clickable(onClick = onClick)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        chipContent()
    }
}
