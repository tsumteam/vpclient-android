package ru.mercury.vpclient.shared.ui.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.PREFIX_SPACE
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.ChevronEnd24
import ru.mercury.vpclient.shared.ui.icons.Copy24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.spanRegular14
import ru.mercury.vpclient.shared.ui.theme.success

data class ProfileOrderItemState(
    val numberTitleRes: Int,
    val orderNumber: String,
    val amount: String,
    val statusPrefix: String,
    val statusDescription: String,
    val statusType: ProfileOrderStatusType,
    val showPaymentBadge: Boolean,
    val products: List<ProfileOrderProductState>,
    val hiddenProductsCount: Int = 0
)

data class ProfileOrderProductState(
    val imageUrl: String
)

enum class ProfileOrderStatusType {
    NotFinished,
    Finished
}

@Composable
fun ProfileOrderItem(
    state: ProfileOrderItemState,
    onClick: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusPrefixColor = when (state.statusType) {
        ProfileOrderStatusType.NotFinished -> MaterialTheme.colorScheme.error
        ProfileOrderStatusType.Finished -> MaterialTheme.colorScheme.success
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(state.orderNumber) }
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1F),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(state.numberTitleRes, state.orderNumber),
                    modifier = Modifier.weight(weight = 1F, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.medium18.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                IconButton(
                    onClick = { onCopyClick(state.orderNumber) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Copy24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.showPaymentBadge) {
                    ProfileOrderPaymentBadge()
                }

                Icon(
                    imageVector = ChevronEnd24,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = stringResource(ClientStrings.ProfileOrdersAmount, state.amount),
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp),
            style = MaterialTheme.typography.medium14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp
            )
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.spanRegular14.copy(
                        color = statusPrefixColor
                    )
                ) {
                    append(state.statusPrefix)
                }
                append(PREFIX_SPACE)
                append(state.statusDescription)
            },
            modifier = Modifier.padding(start = 16.dp, top = 10.dp, end = 16.dp),
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 19.sp,
                letterSpacing = .2.sp
            )
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .height(96.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                items = state.products
            ) { product ->
                ClientAsyncImage(
                    imageUrl = product.imageUrl,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(width = 64.dp, height = 96.dp)
                )
            }

            if (state.hiddenProductsCount > 0) {
                item {
                    ProfileOrderMoreButton(
                        count = state.hiddenProductsCount,
                        onClick = onMoreClick
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ProfileOrderItemPreview(
    @PreviewParameter(ProfileOrderItemStateProvider::class) state: ProfileOrderItemState
) {
    ProfileOrderItem(
        state = state,
        onClick = {},
        onCopyClick = {},
        onMoreClick = {}
    )
}

private class ProfileOrderItemStateProvider: PreviewParameterProvider<ProfileOrderItemState> {
    override val values: Sequence<ProfileOrderItemState> = sequenceOf(
        ProfileOrderItemState(
            numberTitleRes = ClientStrings.ProfileOrdersNumber,
            orderNumber = "4143-31Т",
            amount = "1 359 950 ₽",
            statusPrefix = "Не завершен:",
            statusDescription = "в процессе оплаты, не доставлен",
            statusType = ProfileOrderStatusType.NotFinished,
            showPaymentBadge = true,
            products = List(4) {
                ProfileOrderProductState(
                    imageUrl = ""
                )
            },
            hiddenProductsCount = 2
        ),
        ProfileOrderItemState(
            numberTitleRes = ClientStrings.ProfileOrdersNumber,
            orderNumber = "728778-Т",
            amount = "459 000 ₽",
            statusPrefix = "Завершен:",
            statusDescription = "оплачен, доставлен",
            statusType = ProfileOrderStatusType.Finished,
            showPaymentBadge = false,
            products = List(2) {
                ProfileOrderProductState(
                    imageUrl = ""
                )
            }
        )
    )
}
