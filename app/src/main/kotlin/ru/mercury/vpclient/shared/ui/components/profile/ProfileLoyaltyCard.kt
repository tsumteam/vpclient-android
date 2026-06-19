package ru.mercury.vpclient.shared.ui.components.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.R
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardInfo
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.gold
import ru.mercury.vpclient.shared.ui.theme.gold2
import ru.mercury.vpclient.shared.ui.theme.gold3
import ru.mercury.vpclient.shared.ui.theme.gold4
import ru.mercury.vpclient.shared.ui.theme.regular10
import ru.mercury.vpclient.shared.ui.theme.regular16
import ru.mercury.vpclient.shared.ui.theme.regular18
import ru.mercury.vpclient.shared.ui.theme.silver
import ru.mercury.vpclient.shared.ui.theme.silver2
import ru.mercury.vpclient.shared.ui.theme.silver3

data class ProfileLoyaltyCardState(
    val cardInfo: LoyaltyCardInfo = LoyaltyCardInfo(),
    val showButtons: Boolean = true
)

@Composable
fun ProfileLoyaltyCard(
    state: ProfileLoyaltyCardState,
    modifier: Modifier = Modifier,
    onQrClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(191.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                when (state.cardInfo.typeCard) {
                    LoyaltyCardType.Black -> Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary
                        )
                    )
                    LoyaltyCardType.Gold -> Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.gold2,
                            MaterialTheme.colorScheme.gold3,
                            MaterialTheme.colorScheme.gold4
                        )
                    )
                    LoyaltyCardType.Silver -> Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.silver,
                            MaterialTheme.colorScheme.silver2
                        )
                    )
                }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp, top = 23.dp, bottom = 19.dp)
        ) {
            Text(
                text = stringResource(ClientStrings.ProfileLoyaltyCardTitle),
                style = MaterialTheme.typography.regular16.copy(
                    color = Color.White,
                    lineHeight = 20.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = state.cardInfo.loyaltyCardNumber,
                modifier = Modifier.padding(top = 7.dp),
                style = MaterialTheme.typography.regular18.copy(
                    color = Color.White,
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp
                )
            )

            Spacer(
                modifier = Modifier.weight(1F)
            )

            Text(
                text = pluralStringResource(
                    id = ClientStrings.ProfileLoyaltyCardBonuses,
                    count = state.cardInfo.bonusAmount,
                    state.cardInfo.bonusAmount
                ),
                style = MaterialTheme.typography.regular16.copy(
                    color = Color.White,
                    lineHeight = 20.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        Image(
            painter = painterResource(
                when (state.cardInfo.typeCard) {
                    LoyaltyCardType.Black -> R.drawable.loyalty_vip_black
                    LoyaltyCardType.Gold -> R.drawable.loyalty_vip_gold
                    LoyaltyCardType.Silver -> R.drawable.loyalty_vip_silver
                }
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 24.dp, end = 16.dp)
                .size(
                    width = when (state.cardInfo.typeCard) {
                        LoyaltyCardType.Black -> 95.dp
                        LoyaltyCardType.Gold -> 89.dp
                        LoyaltyCardType.Silver -> 81.dp
                    },
                    height = when (state.cardInfo.typeCard) {
                        LoyaltyCardType.Black -> 47.dp
                        LoyaltyCardType.Gold -> 50.dp
                        LoyaltyCardType.Silver -> 55.dp
                    }
                )
        )

        if (state.showButtons) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .height(22.dp)
                        .clip(RoundedCornerShape(51.dp))
                        .background(Color.White)
                        .clickable(onClick = onQrClick)
                        .padding(horizontal = 15.5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(ClientStrings.ProfileLoyaltyCardQrCode),
                        style = MaterialTheme.typography.regular10.copy(
                            color = when (state.cardInfo.typeCard) {
                                LoyaltyCardType.Gold -> MaterialTheme.colorScheme.gold
                                LoyaltyCardType.Black,
                                LoyaltyCardType.Silver -> MaterialTheme.colorScheme.onBackground
                            }
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .height(22.dp)
                        .clip(RoundedCornerShape(51.dp))
                        .background(
                            when (state.cardInfo.typeCard) {
                                LoyaltyCardType.Gold -> Color.Transparent
                                LoyaltyCardType.Black,
                                LoyaltyCardType.Silver -> MaterialTheme.colorScheme.silver3
                            }
                        )
                        .then(
                            when (state.cardInfo.typeCard) {
                                LoyaltyCardType.Gold -> Modifier.border(
                                    width = 1.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(11.dp)
                                )
                                LoyaltyCardType.Black,
                                LoyaltyCardType.Silver -> Modifier
                            }
                        )
                        .clickable(onClick = onMoreClick)
                        .padding(horizontal = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(ClientStrings.ProfileLoyaltyCardMore),
                        style = MaterialTheme.typography.regular10.copy(
                            color = when (state.cardInfo.typeCard) {
                                LoyaltyCardType.Gold -> Color.White
                                LoyaltyCardType.Black,
                                LoyaltyCardType.Silver -> MaterialTheme.colorScheme.onBackground
                            },
                            lineHeight = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileLoyaltyCardPreview(
    @PreviewParameter(ProfileLoyaltyCardStatePreviewParameterProvider::class) state: ProfileLoyaltyCardState
) {
    ProfileLoyaltyCard(
        state = state
    )
}

private class ProfileLoyaltyCardStatePreviewParameterProvider: PreviewParameterProvider<ProfileLoyaltyCardState> {
    override val values: Sequence<ProfileLoyaltyCardState> = sequenceOf(
        ProfileLoyaltyCardState(
            cardInfo = LoyaltyCardInfo(
                loyaltyCardNumber = "G40135",
                bonusAmount = 0,
                typeCard = LoyaltyCardType.Black
            )
        ),
        ProfileLoyaltyCardState(
            cardInfo = LoyaltyCardInfo(
                loyaltyCardNumber = "G40135",
                bonusAmount = 0,
                typeCard = LoyaltyCardType.Gold
            )
        ),
        ProfileLoyaltyCardState(
            cardInfo = LoyaltyCardInfo(
                loyaltyCardNumber = "G40135",
                bonusAmount = 0,
                typeCard = LoyaltyCardType.Silver
            )
        )
    )
}
