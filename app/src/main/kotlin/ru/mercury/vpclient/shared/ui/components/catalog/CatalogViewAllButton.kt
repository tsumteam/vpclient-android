package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretRegular15

@Composable
fun CatalogViewAllButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(ClientStrings.CatalogWatchAll),
        modifier = modifier
            .size(86.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .wrapContentSize(Alignment.Center),
        style = MaterialTheme.typography.livretRegular15.copy(
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    )
}

@FontScalePreviews
@Composable
private fun CatalogViewAllButtonPreview() {
    ClientTheme {
        CatalogViewAllButton(
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
