package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.mercury.vpclient.shared.ui.ktx.disableSplitMotionEvents

@Composable
fun SharedColumn(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: PaddingValues = PaddingValues(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    userScrollEnabled: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .disableSplitMotionEvents()
            .verticalScroll(state = scrollState, enabled = userScrollEnabled)
            .padding(contentPadding),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun SharedColumnPreview() {
    SharedColumn {
        Text(text = "Item1")
        Text(text = "Item2")
        Text(text = "Item3")
    }
}
