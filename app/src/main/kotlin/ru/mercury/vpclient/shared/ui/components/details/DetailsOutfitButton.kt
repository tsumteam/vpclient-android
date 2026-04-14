package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.medium11

@Composable
fun DetailsOutfitButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(ClientStrings.DetailsWhatToWear),
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .wrapContentSize(Alignment.Center),
        style = MaterialTheme.typography.medium11.copy(
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 13.sp,
            textAlign = TextAlign.Center
        )
    )
}

@FontScalePreviews
@Composable
private fun DetailsOutfitButtonPreview() {
    ClientTheme {
        DetailsOutfitButton(
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
