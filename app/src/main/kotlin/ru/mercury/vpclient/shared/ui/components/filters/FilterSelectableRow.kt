package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.icons.Check24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun FilterSelectableRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        SharedAnimatedVisibility(
            visible = selected
        ) {
            Icon(
                imageVector = Check24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 48.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 19.sp,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Start
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FilterSelectableRowPreview() {
    Column {
        FilterSelectableRow(
            text = "Длинные",
            selected = true,
            onClick = {}
        )

        FilterSelectableRow(
            text = "До колена",
            selected = true,
            onClick = {}
        )

        FilterSelectableRow(
            text = "До середины бедра",
            selected = false,
            onClick = {}
        )

        FilterSelectableRow(
            text = "Короткие",
            selected = false,
            onClick = {}
        )
    }
}
