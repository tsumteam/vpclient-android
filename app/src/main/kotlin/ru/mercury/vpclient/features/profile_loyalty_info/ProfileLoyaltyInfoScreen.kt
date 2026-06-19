@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_loyalty_info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.R
import ru.mercury.vpclient.features.profile_loyalty_info.intent.ProfileLoyaltyInfoIntent
import ru.mercury.vpclient.features.profile_loyalty_info.model.ProfileLoyaltyInfoModel
import ru.mercury.vpclient.features.profile_loyalty_unlink_dialog.ProfileLoyaltyUnlinkDialog
import ru.mercury.vpclient.features.profile_loyalty_unlink_dialog.intent.ProfileLoyaltyUnlinkDialogIntent
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardDescription
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardInfo
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.data.entity.ProfileLoyaltyInfoRowType
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.profile.ProfileLoyaltyCard
import ru.mercury.vpclient.shared.ui.components.profile.ProfileLoyaltyCardState
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun ProfileLoyaltyInfoScreen(
    viewModel: ProfileLoyaltyInfoViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ProfileLoyaltyInfoScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )

    if (state.isUnlinkDialogVisible) {
        ProfileLoyaltyUnlinkDialog(
            dispatch = { intent ->
                when (intent) {
                    is ProfileLoyaltyUnlinkDialogIntent.ConfirmRequest -> {
                        viewModel.dispatch(ProfileLoyaltyInfoIntent.ConfirmUnlinkClick)
                    }
                    is ProfileLoyaltyUnlinkDialogIntent.DismissRequest -> {
                        viewModel.dispatch(ProfileLoyaltyInfoIntent.DismissUnlinkDialog)
                    }
                }
            }
        )
    }
}

@Composable
private fun ProfileLoyaltyInfoScreenContent(
    state: ProfileLoyaltyInfoModel,
    dispatch: (ProfileLoyaltyInfoIntent) -> Unit
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ProfileLoyaltyInfoTitle),
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ProfileLoyaltyInfoIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            OutlinedButton(
                onClick = { dispatch(ProfileLoyaltyInfoIntent.UnlinkClick) },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isUnlinkLoading && !state.isLoading,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                when {
                    state.isUnlinkLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(ClientStrings.ProfileLoyaltyInfoUnlink),
                            style = MaterialTheme.typography.medium14.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                SharedLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding + PaddingValues(top = 24.dp, bottom = 96.dp)
                ) {
                    item {
                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(191.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(6.dp)
                                )
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .padding(start = 16.dp, top = 23.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(88.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier
                                .padding(start = 52.dp, top = 100.dp, end = 52.dp)
                                .fillMaxWidth()
                                .height(58.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
            }
            else -> {
                SharedLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding + PaddingValues(top = 8.dp, bottom = 96.dp)
                ) {
                    item {
                        ProfileLoyaltyCard(
                            state = ProfileLoyaltyCardState(
                                cardInfo = state.cardInfo
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onQrClick = { dispatch(ProfileLoyaltyInfoIntent.QrClick) },
                            onMoreClick = {}
                        )
                    }
                    if (state.showBonusRules) {
                        item {
                            Text(
                                text = state.bonusRules,
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.regular14.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    lineHeight = 19.sp,
                                    letterSpacing = .2.sp
                                )
                            )
                        }
                    }
                    item {
                        Text(
                            text = stringResource(ClientStrings.ProfileLoyaltyInfoRulesCaps),
                            modifier = Modifier
                                .padding(start = 16.dp, top = 32.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(56.dp)
                                .wrapContentHeight(Alignment.CenterVertically),
                            style = MaterialTheme.typography.livretMedium18.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 26.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            state.sortedCardTypes.forEach { cardType ->
                                val selected = cardType.type == state.selectedCardType
                                Box(
                                    modifier = Modifier
                                        .size(
                                            width = when {
                                                selected -> 89.dp
                                                else -> 85.dp
                                            },
                                            height = when {
                                                selected -> 59.dp
                                                else -> 55.dp
                                            }
                                        )
                                        .clip(RoundedCornerShape(5.dp))
                                        .border(
                                            width = when {
                                                selected -> 1.dp
                                                else -> 0.dp
                                            },
                                            color = Color.Black,
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .clickable { dispatch(ProfileLoyaltyInfoIntent.CardTypeClick(cardType.type)) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(
                                            when (cardType.type) {
                                                LoyaltyCardType.Black -> R.drawable.loyalty_vip_small_black
                                                LoyaltyCardType.Gold -> R.drawable.loyalty_vip_small_gold
                                                LoyaltyCardType.Silver -> R.drawable.loyalty_vip_small_silver
                                            }
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.size(
                                            width = 85.dp,
                                            height = 55.dp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp)
                        ) {
                            state.infoRows.forEach { row ->
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = stringResource(
                                                when (row.type) {
                                                    ProfileLoyaltyInfoRowType.CardType -> ClientStrings.ProfileLoyaltyInfoCardType
                                                    ProfileLoyaltyInfoRowType.TermsForObtaining -> ClientStrings.ProfileLoyaltyInfoTermsForObtaining
                                                    ProfileLoyaltyInfoRowType.Validity -> ClientStrings.ProfileLoyaltyInfoValidity
                                                    ProfileLoyaltyInfoRowType.RenewalTerms -> ClientStrings.ProfileLoyaltyInfoRenewalTerms
                                                }
                                            ),
                                            modifier = Modifier.width(132.dp),
                                            style = MaterialTheme.typography.medium14.copy(
                                                color = MaterialTheme.colorScheme.onBackground,
                                                lineHeight = 16.sp
                                            )
                                        )

                                        Text(
                                            text = row.value,
                                            modifier = Modifier.weight(1F),
                                            style = MaterialTheme.typography.regular14.copy(
                                                color = MaterialTheme.colorScheme.onBackground,
                                                lineHeight = 18.sp,
                                                letterSpacing = .2.sp
                                            )
                                        )
                                    }

                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.divider
                                    )
                                }
                            }
                        }
                    }
                    item {
                        TextButton(
                            onClick = { dispatch(ProfileLoyaltyInfoIntent.MoreClick) },
                            modifier = Modifier
                                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = stringResource(ClientStrings.ProfileLoyaltyCardMore),
                                style = MaterialTheme.typography.medium14.copy(
                                    color = MaterialTheme.colorScheme.error,
                                    lineHeight = 16.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(heightDp = 1000)
@Composable
private fun ProfileLoyaltyInfoScreenContentPreview(
    @PreviewParameter(ProfileLoyaltyInfoModelPreviewParameterProvider::class) state: ProfileLoyaltyInfoModel
) {
    ProfileLoyaltyInfoScreenContent(
        state = state,
        dispatch = {}
    )
}

private class ProfileLoyaltyInfoModelPreviewParameterProvider: PreviewParameterProvider<ProfileLoyaltyInfoModel> {
    override val values: Sequence<ProfileLoyaltyInfoModel> = sequenceOf(
        ProfileLoyaltyInfoModel(),
        ProfileLoyaltyInfoModel(
            isLoading = false,
            cardInfo = LoyaltyCardInfo(
                loyaltyCardNumber = "G40135",
                bonusAmount = 1500,
                typeCard = LoyaltyCardType.Silver
            ),
            cardTypes = previewLoyaltyCardDescriptions(),
            selectedCardType = LoyaltyCardType.Silver
        ),
        ProfileLoyaltyInfoModel(
            isLoading = false,
            cardInfo = LoyaltyCardInfo(
                loyaltyCardNumber = "G40135",
                bonusAmount = 3200,
                typeCard = LoyaltyCardType.Gold
            ),
            cardTypes = previewLoyaltyCardDescriptions(),
            selectedCardType = LoyaltyCardType.Gold
        ),
        ProfileLoyaltyInfoModel(
            isLoading = false,
            isUnlinkLoading = true,
            cardInfo = LoyaltyCardInfo(
                loyaltyCardNumber = "G40135",
                bonusAmount = 7400,
                typeCard = LoyaltyCardType.Black
            ),
            cardTypes = previewLoyaltyCardDescriptions(),
            selectedCardType = LoyaltyCardType.Black
        )
    )

    private fun previewLoyaltyCardDescriptions(): List<LoyaltyCardDescription> {
        return listOf(
            LoyaltyCardDescription(
                type = LoyaltyCardType.Silver,
                cardName = "VIP Silver",
                bonusRules = "С каждой покупкой на карту начисляется бонус в размере 5% от стоимости приобретаемых товаров в бутиках одежды и Gift Division через 14 дней с даты покупки. Бонусы на товар категории sale и уцененный товар не начисляются.",
                termsForObtaining = "Совершите покупки на сумму от 50 000 ₽.",
                validity = "12 месяцев",
                renewalTerms = "Продлевается при сохранении условий программы"
            ),
            LoyaltyCardDescription(
                type = LoyaltyCardType.Gold,
                cardName = "VIP Gold",
                bonusRules = "С каждой покупкой на карту начисляется бонус в размере 5% от стоимости приобретаемых товаров в бутиках одежды и Gift Division через 14 дней с даты покупки. Бонусы на товар категории sale и уцененный товар не начисляются.",
                termsForObtaining = "Совершите покупки на сумму от 150 000 ₽.",
                validity = "12 месяцев",
                renewalTerms = "Продлевается при сохранении условий программы"
            ),
            LoyaltyCardDescription(
                type = LoyaltyCardType.Black,
                cardName = "VIP Black",
                bonusRules = "С каждой покупкой на карту начисляется бонус в размере 5% от стоимости приобретаемых товаров в бутиках одежды и Gift Division через 14 дней с даты покупки. Бонусы на товар категории sale и уцененный товар не начисляются.",
                termsForObtaining = "Совершите покупки на сумму от 300 000 ₽.",
                validity = "12 месяцев",
                renewalTerms = "Продлевается при сохранении условий программы"
            )
        )
    }
}
