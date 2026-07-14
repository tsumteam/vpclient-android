package ru.mercury.vpclient.shared.ui.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionType
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18

data class HomeGiftCardsState(
    val section: HomeSectionEntity
)

@Composable
fun HomeGiftCards(
    state: HomeGiftCardsState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        Text(
            text = state.section.title.ifEmpty { stringResource(ClientStrings.HomeGiftCards) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            style = MaterialTheme.typography.livretMedium18.copy(
                color = MaterialTheme.colorScheme.error,
                lineHeight = 26.sp,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Center
            )
        )

        ClientAsyncImage(
            imageUrl = state.section.giftCardPhotoUrl,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(213.dp)
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun HomeGiftCardsPreview(
    @PreviewParameter(HomeGiftCardsStateProvider::class) state: HomeGiftCardsState
) {
    HomeGiftCards(
        state = state
    )
}

private class HomeGiftCardsStateProvider: PreviewParameterProvider<HomeGiftCardsState> {
    override val values: Sequence<HomeGiftCardsState> = sequenceOf(
        HomeGiftCardsState(
            section = HomeSectionEntity(
                type = HomeSectionType.GIFT_CARDS,
                order = 1,
                title = "ПОДАРОЧНЫЕ КАРТЫ"
            )
        )
    )
}
