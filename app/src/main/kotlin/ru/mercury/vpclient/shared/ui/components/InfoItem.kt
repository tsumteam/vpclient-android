package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular13

data class InfoItemState(
    val label: String,
    val value: String
)

@Composable
fun InfoItem(
    state: InfoItemState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp)
    ) {
        Text(
            text = state.label,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.regular13.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        )

        Text(
            text = state.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 21.dp),
            style = MaterialTheme.typography.medium14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp
            )
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.divider
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun InfoItemPreview() {
    InfoItem(
        state = InfoItemState(
            label = "Способ доставки",
            value = "Служба логистики"
        )
    )
}
