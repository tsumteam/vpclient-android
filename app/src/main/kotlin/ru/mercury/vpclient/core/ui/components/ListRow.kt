package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.icons.Selected24
import ru.mercury.vpclient.core.ui.icons.Unselected24
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular15

@Composable
fun ListRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (selected) Selected24 else Unselected24,
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp)
        )

        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular15.copy(color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Start)
        )
    }
}

@FontScalePreviews
@Composable
private fun ListRowPreview() {
    ClientTheme {
        Column {
            ListRow(
                text = "Наш выбор",
                selected = true,
                onClick = {}
            )

            ListRow(
                text = "По возрастанию цены",
                selected = false,
                onClick = {}
            )

            ListRow(
                text = "По убыванию цены",
                selected = false,
                onClick = {}
            )

            ListRow(
                text = "По новизне",
                selected = false,
                onClick = {}
            )
        }
    }
}
