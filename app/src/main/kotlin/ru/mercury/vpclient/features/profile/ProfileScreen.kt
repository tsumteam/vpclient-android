@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.profile.model.ProfileModel
import ru.mercury.vpclient.features.profile_logout_dialog.ProfileLogoutDialog
import ru.mercury.vpclient.features.profile_logout_dialog.intent.ProfileLogoutDialogIntent
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.IndicatorIcon
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.profile.ProfileViewMoreButton
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Basket24
import ru.mercury.vpclient.shared.ui.icons.ChevronEnd24
import ru.mercury.vpclient.shared.ui.icons.Info24
import ru.mercury.vpclient.shared.ui.icons.Logout24
import ru.mercury.vpclient.shared.ui.icons.Man24
import ru.mercury.vpclient.shared.ui.icons.Notification24
import ru.mercury.vpclient.shared.ui.icons.Qr24
import ru.mercury.vpclient.shared.ui.icons.VipBadge24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.livretRegular15
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    ProfileScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )

    if (state.isLogoutDialogVisible) {
        ProfileLogoutDialog(
            dispatch = { intent ->
                when (intent) {
                    is ProfileLogoutDialogIntent.ConfirmRequest -> {
                        viewModel.dispatch(ProfileIntent.Logout)
                    }
                    is ProfileLogoutDialogIntent.DismissRequest -> {
                        viewModel.dispatch(ProfileIntent.DismissLogoutDialog)
                    }
                }
            }
        )
    }
}

@Composable
private fun ProfileScreenContent(
    state: ProfileModel,
    dispatch: (ProfileIntent) -> Unit
) {
    val viewHistoryProducts = state.viewHistoryProducts.take(PROFILE_VIEW_HISTORY_PRODUCTS_COUNT)
    val showViewHistory = state.isViewHistoryLoading || state.viewHistoryProducts.isNotEmpty()
    val showViewHistoryMore = state.viewHistoryProducts.size > PROFILE_VIEW_HISTORY_PRODUCTS_COUNT

    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ProfileTitle),
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ProfileIntent.NotificationClick) }
                    ) {
                        Icon(
                            imageVector = Notification24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                    }
                },
                actions = {
                    if (state.showFittingButton) {
                        FittingIconButton(
                            text = state.fittingText,
                            showBadge = state.showFittingBadge,
                            onClick = { dispatch(ProfileIntent.FittingClick) }
                        )
                    }

                    CartIconButton(
                        text = state.cartText,
                        showBadge = state.showCartBadge,
                        onClick = { dispatch(ProfileIntent.CartClick) }
                    )

                    MessengerIconButton(
                        showBadge = state.showMessengerBadge,
                        onClick = { dispatch(ProfileIntent.MessengerClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            OutlinedButton(
                onClick = { dispatch(ProfileIntent.ShowLogoutDialog) },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isLogoutLoading,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                when {
                    state.isLogoutLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Logout24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Unspecified
                            )

                            Text(
                                text = stringResource(ClientStrings.ProfileLogout),
                                style = MaterialTheme.typography.livretRegular15.copy(
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(top = 16.dp, bottom = 92.dp)
        ) {
            item {
                OutlinedButton(
                    onClick = { dispatch(ProfileIntent.AddLoyaltyCardClick) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = VipBadge24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )

                        Text(
                            text = stringResource(ClientStrings.ProfileAddLoyaltyCard),
                            style = MaterialTheme.typography.medium15.copy(
                                letterSpacing = .3.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier.height(40.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { dispatch(ProfileIntent.MyDataClick) }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Man24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )

                    Text(
                        text = stringResource(ClientStrings.ProfileMyData),
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.medium14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 16.sp
                        )
                    )

                    Icon(
                        imageVector = ChevronEnd24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { dispatch(ProfileIntent.PurchasesClick) }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IndicatorIcon(
                        icon = Basket24,
                        showIndicator = state.showCartBadge
                    )

                    Text(
                        text = stringResource(ClientStrings.ProfilePurchases),
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.medium14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 16.sp
                        )
                    )

                    Icon(
                        imageVector = ChevronEnd24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { dispatch(ProfileIntent.InformationClick) }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Info24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )

                    Text(
                        text = stringResource(ClientStrings.ProfileInformation),
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.medium14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 16.sp
                        )
                    )

                    Icon(
                        imageVector = ChevronEnd24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable { dispatch(ProfileIntent.QrCodeClick) }
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Qr24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )

                    Text(
                        text = stringResource(ClientStrings.ProfileQrCode),
                        modifier = Modifier.weight(1F),
                        style = MaterialTheme.typography.medium14.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 16.sp
                        )
                    )

                    Icon(
                        imageVector = ChevronEnd24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 48.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
            if (showViewHistory) {
                item {
                    Spacer(
                        modifier = Modifier.height(40.dp)
                    )
                }
                item {
                    Text(
                        text = stringResource(ClientStrings.ProfileRecentlyViewedCaps),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .padding(horizontal = 16.dp)
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
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(156.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        when {
                            state.isViewHistoryLoading -> {
                                items(
                                    items = listOf(0, 1, 2, 3),
                                    key = { index -> "profile_history_placeholder_$index" }
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .size(width = 112.dp, height = 156.dp)
                                            .background(MaterialTheme.colorScheme.background)
                                            .placeholder(
                                                visible = true,
                                                highlight = PlaceholderHighlight.shimmer(),
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                }
                            }
                            else -> {
                                items(
                                    items = viewHistoryProducts,
                                    key = { product -> "${product.id}_${product.position}" }
                                ) { product ->
                                    Column(
                                        modifier = Modifier
                                            .size(width = 112.dp, height = 156.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .clickable {
                                                dispatch(ProfileIntent.ViewHistoryProductClick(product.id))
                                            },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        ClientAsyncImage(
                                            imageUrl = product.imageUrl,
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier
                                                .padding(top = 10.dp)
                                                .size(width = 62.dp, height = 96.dp)
                                        )

                                        BrandBox(
                                            entity = BrandEntity(
                                                brand = product.brand,
                                                urlBrandLogo = product.urlBrandLogo
                                            ),
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .size(width = 96.dp, height = 20.dp),
                                            style = MaterialTheme.typography.livretRegular15
                                        )
                                    }
                                }
                                if (showViewHistoryMore) {
                                    item {
                                        Box(
                                            modifier = Modifier.size(width = 112.dp, height = 156.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ProfileViewMoreButton(
                                                onClick = { dispatch(ProfileIntent.ViewHistoryViewMoreClick) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val PROFILE_VIEW_HISTORY_PRODUCTS_COUNT = 10

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileScreenContentPreview(
) {
    ProfileScreenContent(
        state = ProfileModel(),
        dispatch = {}
    )
}
