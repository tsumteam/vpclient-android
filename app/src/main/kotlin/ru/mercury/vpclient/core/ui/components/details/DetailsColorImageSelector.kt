package ru.mercury.vpclient.core.ui.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.core.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.livretMedium19

// fixme добавить клик на шмотку чтобы отображать ее

@Composable
fun DetailsColorImageSelector(
    colorImageUrls: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(159.dp)
    ) {
        Text(
            text = stringResource(ClientStrings.DetailsOtherModelColors),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.livretMedium19.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 26.sp,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Center
            )
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            items(
                items = colorImageUrls
            ) { colorImageUrl ->
                ClientAsyncImage(
                    imageUrl = colorImageUrl,
                    modifier = Modifier
                        .size(width = 64.dp, height = 99.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun DetailsColorImageSelectorPreview() {
    ClientTheme {
        DetailsColorImageSelector(
            colorImageUrls = listOf("", "", "", "")
        )
    }
}
