@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.profile_order.intent.ProfileOrderIntent
import ru.mercury.vpclient.features.profile_order.model.ProfileOrderModel
import ru.mercury.vpclient.features.profile_order.navigation.ProfileOrderRoute
import ru.mercury.vpclient.shared.data.event.ProfileOrderDeliveryGroupState
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.InfoItem
import ru.mercury.vpclient.shared.ui.components.InfoItemState
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderProductItem
import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderProductItemState
import ru.mercury.vpclient.shared.ui.icons.ChevronEnd24
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun ProfileOrderScreen(
    route: ProfileOrderRoute,
    viewModel: ProfileOrderViewModel = hiltViewModel<ProfileOrderViewModel, ProfileOrderViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ProfileOrderScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun ProfileOrderScreenContent(
    state: ProfileOrderModel,
    dispatch: (ProfileOrderIntent) -> Unit
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = stringResource(ClientStrings.ProfileOrdersNumber, state.orderNumber),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.medium15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                letterSpacing = .3.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        Text(
                            text = when {
                                state.creationDate.isNotBlank() -> stringResource(
                                    ClientStrings.ProfileOrderSubtitle,
                                    state.creationDate,
                                    state.amount
                                )
                                else -> stringResource(
                                    ClientStrings.ProfileOrderSubtitleWithoutDate,
                                    state.amount
                                )
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ProfileOrderIntent.BackClick) }
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
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(top = 2.dp),
            userScrollEnabled = !state.isLoading
        ) {
            if (state.isLoading) {
                items(
                    count = 4
                ) { index ->
                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(
                                when (index) {
                                    0 -> 72.dp
                                    1 -> 40.dp
                                    else -> 179.dp
                                }
                            )
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            } else {
                if (state.showPaymentAlert) {
                    item {
                        val hours = state.paymentAlertRemainingMinutes / 60
                        val minutes = state.paymentAlertRemainingMinutes % 60
                        val remainingTime = listOfNotNull(
                            when {
                                hours > 0 -> pluralStringResource(
                                    ClientStrings.ProfileOrderPaymentAlertHours,
                                    hours,
                                    hours
                                )
                                else -> null
                            },
                            when {
                                minutes > 0 || hours == 0 -> pluralStringResource(
                                    ClientStrings.ProfileOrderPaymentAlertMinutes,
                                    minutes,
                                    minutes
                                )
                                else -> null
                            }
                        ).joinToString(separator = " ")

                        Text(
                            text = stringResource(ClientStrings.ProfileOrderPaymentAlert, remainingTime),
                            modifier = Modifier
                                .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.error,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 12.dp),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onPrimary,
                                lineHeight = 18.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
                item {
                    InfoItem(
                        state = InfoItemState(
                            label = stringResource(ClientStrings.ProfileOrderPaymentMethod),
                            value = stringResource(ClientStrings.ProfileOrderPaymentOnReceipt)
                        )
                    )
                }
                if (state.deliveryGroups.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(ClientStrings.ProfileOrderInfoTitle),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 40.dp)
                                .wrapContentHeight(align = Alignment.CenterVertically)
                                .padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 20.sp
                            )
                        )
                    }
                }
                items(
                    items = state.deliveryGroups,
                    key = { deliveryGroup -> deliveryGroup.id }
                ) { deliveryGroup ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clickable {
                                    dispatch(
                                        ProfileOrderIntent.DeliveryClick(
                                            deliveryId = deliveryGroup.id,
                                            productIds = deliveryGroup.products.map { product -> product.productId }
                                        )
                                    )
                                }
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1F),
                                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = deliveryGroup.date,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.medium14.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        lineHeight = 16.sp
                                    )
                                )

                                Text(
                                    text = deliveryGroup.address,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.regular11.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }

                            Icon(
                                imageVector = ChevronEnd24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        deliveryGroup.products.forEach { product ->
                            ProfileOrderProductItem(
                                state = product,
                                onClick = { productId -> dispatch(ProfileOrderIntent.ProductClick(productId)) }
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(start = 16.dp),
                            color = MaterialTheme.colorScheme.divider
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileOrderScreenContentPreview(
    @PreviewParameter(ProfileOrderModelProvider::class) state: ProfileOrderModel
) {
    ProfileOrderScreenContent(
        state = state,
        dispatch = {}
    )
}

private class ProfileOrderModelProvider: PreviewParameterProvider<ProfileOrderModel> {
    override val values: Sequence<ProfileOrderModel> = sequenceOf(
        ProfileOrderModel(
            orderNumber = "72878-Т",
            amount = "200 000 ₽",
            creationDate = "29.06.2026",
            isLoading = false,
            showPaymentAlert = true,
            paymentAlertRemainingMinutes = 179,
            deliveryGroups = listOf(
                ProfileOrderDeliveryGroupState(
                    id = "delivery_1",
                    date = "12 июня 2026 с 14:00 до 16:00",
                    address = "Brioni, Третьяковский проезд",
                    products = listOf(
                        ProfileOrderProductItemState(
                            productId = "7930630",
                            imageUrl = "",
                            brand = "MVST",
                            urlBrandLogo = null,
                            name = "Куртка",
                            color = "Бежевый",
                            article = "7930630",
                            price = "100 000 ₽",
                            size = "IT 40 | RU 42",
                            status = "Не оплачен",
                            quantity = 1
                        )
                    )
                ),
                ProfileOrderDeliveryGroupState(
                    id = "delivery_2",
                    date = "13 июня 2026 с 14:00 до 16:00",
                    address = "Brioni, Третьяковский проезд",
                    products = listOf(
                        ProfileOrderProductItemState(
                            productId = "79306777",
                            imageUrl = "",
                            brand = "DOLCE & GABBANA",
                            urlBrandLogo = null,
                            name = "Рубашка",
                            color = "Розовый",
                            article = "79306777",
                            price = "100 000 ₽",
                            size = "IT 40 | RU 42",
                            status = "",
                            quantity = 1
                        )
                    )
                )
            )
        )
    )
}
