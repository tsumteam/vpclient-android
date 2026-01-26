package ru.mercury.vpclient.features.main.tabs.home.stack.routes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.components.VPClientLazyColumn
import ru.mercury.vpclient.core.ui.placeholder.PlaceholderHighlight
import ru.mercury.vpclient.core.ui.placeholder.material3.fade
import ru.mercury.vpclient.core.ui.placeholder.placeholder
import ru.mercury.vpclient.core.ui.theme.VPClientTheme

@Composable
fun PageLoading(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues()
) {
    VPClientLazyColumn(
        modifier = modifier,
        contentPadding = paddingValues,
        userScrollEnabled = false
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .placeholder(
                        visible = true,
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp),
                        highlight = PlaceholderHighlight.fade()
                    )
            )
        }
        items(3) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(342.dp)
                    .placeholder(
                        visible = true,
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp),
                        highlight = PlaceholderHighlight.fade()
                    )
            )
        }
    }
}

@Preview
@Composable
fun PageLoadingPreview() {
    VPClientTheme {
        PageLoading()
    }
}
