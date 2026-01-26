package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography

@Composable
fun RadioRow(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onSelect),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = VPClientTypography.Regular_14_OnBackground.copy(lineHeight = 20.sp)
        )

        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

@Preview
@Composable
fun RadioRowPreview() {
    VPClientTheme {
        RadioRow(
            text = "Банковская карта",
            selected = false,
            onSelect = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
