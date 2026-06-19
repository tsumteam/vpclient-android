package ru.mercury.vpclient.shared.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.AlphaBankCard36x55
import ru.mercury.vpclient.shared.ui.icons.CloseSmall24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun ProfileAlphaBankCardBanner(
    onCloseClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.divider,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onMoreClick)
            .padding(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = AlphaBankCard36x55,
            contentDescription = null,
            modifier = Modifier.size(width = 36.dp, height = 55.dp),
            tint = Color.Unspecified
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(ClientStrings.ProfileAlphaBankBannerGoldTitle),
                style = MaterialTheme.typography.medium14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 16.sp
                )
            )

            Text(
                text = stringResource(ClientStrings.ProfileAlphaBankBannerDescription),
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = stringResource(ClientStrings.ProfileAlphaBankMore),
                style = MaterialTheme.typography.medium14.copy(
                    color = MaterialTheme.colorScheme.error,
                    lineHeight = 16.sp
                )
            )
        }

        IconButton(
            onClick = onCloseClick
        ) {
            Icon(
                imageVector = CloseSmall24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileAlphaBankCardBannerPreview() {
    ProfileAlphaBankCardBanner(
        onCloseClick = {},
        onMoreClick = {}
    )
}
