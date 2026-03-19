package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular12

@Composable
fun DiscountBadge(
    percentText: String,
    modifier: Modifier = Modifier
) {
    val badgeColor = MaterialTheme.colorScheme.error

    Box(
        modifier = modifier.size(width = 36.dp, height = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val scaleX = size.width / 36F
            val scaleY = size.height / 20F
            val path = Path().apply {
                moveTo(0.946038F * scaleX, 12.5273F * scaleY)
                lineTo(6.84217F * scaleX, 18.3227F * scaleY)
                cubicTo(
                    7.95949F * scaleX, 19.421F * scaleY,
                    9.45434F * scaleX, 20.0165F * scaleY,
                    11.0518F * scaleX, 19.9997F * scaleY
                )
                lineTo(31.9535F * scaleX, 19.7785F * scaleY)
                cubicTo(
                    32.9871F * scaleX, 19.7676F * scaleY,
                    33.9631F * scaleX, 19.362F * scaleY,
                    34.7048F * scaleX, 18.6329F * scaleY
                )
                cubicTo(
                    35.4428F * scaleX, 17.9075F * scaleY,
                    35.8554F * scaleX, 16.9482F * scaleY,
                    35.8665F * scaleX, 15.9323F * scaleY
                )
                lineTo(35.9998F * scaleX, 3.76455F * scaleY)
                cubicTo(
                    36.0109F * scaleX, 2.74861F * scaleY,
                    35.6192F * scaleX, 1.79783F * scaleY,
                    34.8932F * scaleX, 1.08426F * scaleY
                )
                cubicTo(
                    34.1709F * scaleX, 0.374337F * scaleY,
                    33.2037F * scaleX, -0.0107069F * scaleY,
                    32.1701F * scaleX, 0.000226604F * scaleY
                )
                lineTo(11.2684F * scaleX, 0.221329F * scaleY)
                cubicTo(
                    9.67089F * scaleX, 0.238227F * scaleY,
                    8.16261F * scaleX, 0.865558F * scaleY,
                    7.02102F * scaleX, 1.98766F * scaleY
                )
                lineTo(0.996599F * scaleX, 7.90922F * scaleY)
                cubicTo(
                    -0.312168F * scaleX, 9.19565F * scaleY,
                    -0.33486F * scaleX, 11.2682F * scaleY,
                    0.946038F * scaleX, 12.5273F * scaleY
                )
                close()
            }

            drawPath(
                path = path,
                color = badgeColor
            )
        }

        Text(
            text = percentText,
            style = MaterialTheme.typography.regular12.copy(color = MaterialTheme.colorScheme.onPrimary, lineHeight = 16.sp, letterSpacing = .2.sp, textAlign = TextAlign.Center)
        )
    }
}

@FontScalePreviews
@Composable
private fun DiscountBadgePreview() {
    ClientTheme {
        DiscountBadge(
            percentText = "-20%"
        )
    }
}
