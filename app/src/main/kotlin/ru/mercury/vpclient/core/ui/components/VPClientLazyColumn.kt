package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.ktx.disableSplitMotionEvents
import ru.mercury.vpclient.core.ui.theme.VPClientTheme

@Composable
fun VPClientLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit
) {
    LazyColumn(
        modifier = modifier.disableSplitMotionEvents(),
        state = state,
        contentPadding = contentPadding,
        userScrollEnabled = userScrollEnabled,
        content = content
    )
}

@Preview
@Composable
private fun VPClientLazyColumnPreview() {
    VPClientTheme {
        VPClientLazyColumn(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            item {
                Text(text = "Item")
            }
        }
    }
}
