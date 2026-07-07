package ru.mercury.vpclient.shared.ui.components.compilations

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium13
import ru.mercury.vpclient.shared.ui.theme.medium15

data class CompilationPromotionBannerState(
    val text: String,
    val detailsText: String,
    val onClick: () -> Unit = {}
)

@Composable
fun CompilationPromotionBanner(
    state: CompilationPromotionBannerState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = state.onClick)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 26.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = state.text,
            style = MaterialTheme.typography.medium15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 15.sp,
                letterSpacing = .3.sp,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = state.detailsText,
            style = MaterialTheme.typography.medium13.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CompilationPromotionBannerPreview() {
    CompilationPromotionBanner(
        state = CompilationPromotionBannerState(
            text = "Сейчас действует акция «Black Friday»,\nпри покупке всего образа для Вас будет\nскидка 72 935 ₽",
            detailsText = "Подробнее"
        )
    )
}
