package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium13

@Composable
fun CartTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickableWithoutRipple(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.medium13.copy(
                color = when (selected) {
                    true -> MaterialTheme.colorScheme.onBackground
                    false -> MaterialTheme.colorScheme.outline
                },
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartTabAllPreview(
    @PreviewParameter(BooleanParameterProvider::class) selected: Boolean
) {
    CartTab(
        text = if (selected) "ВСЕ ТОВАРЫ (100)" else "В КОРЗИНЕ (0)",
        selected = selected,
        onClick = {}
    )
}
