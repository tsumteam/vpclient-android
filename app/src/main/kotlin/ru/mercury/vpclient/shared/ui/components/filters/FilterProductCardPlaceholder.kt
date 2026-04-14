package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.surface4

@Composable
fun FilterProductCardPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(388.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 12.dp, top = 30.dp, end = 12.dp, bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(216.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surface4,
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Box(
            modifier = Modifier
                .padding(top = 13.dp)
                .fillMaxWidth(.6F)
                .height(14.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surface4,
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(.4F)
                .height(14.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surface4,
                    shape = RoundedCornerShape(4.dp)
                )
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(.5F)
                .height(14.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surface4,
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}

@Preview(showBackground = true, widthDp = 220)
@Composable
private fun FilterProductCardPlaceholderPreview() {
    ClientTheme {
        FilterProductCardPlaceholder()
    }
}
