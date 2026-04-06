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
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular14

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
            .padding(horizontal = 16.dp)
            .clickable(onClick = onSelect),
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1F),
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        RadioButton(
            selected = selected,
            onClick = null
        )
    }
}

@Preview
@Composable
fun RadioRowPreview() {
    ClientTheme {
        RadioRow(
            text = "Банковская карта",
            selected = false,
            onSelect = {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
