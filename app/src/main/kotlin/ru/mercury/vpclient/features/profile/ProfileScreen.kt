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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile.event.ProfileEvent
import ru.mercury.vpclient.features.profile.intent.ProfileIntent
import ru.mercury.vpclient.features.profile.model.ProfileModel
import ru.mercury.vpclient.features.profile_logout_dialog.ProfileLogoutDialog
import ru.mercury.vpclient.features.profile_logout_dialog.intent.ProfileLogoutDialogIntent
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.ProfileLoyaltyAddCardSheet
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.intent.ProfileLoyaltyAddCardIntent
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardMode
import ru.mercury.vpclient.features.profile_loyalty_add_card_sheet.model.ProfileLoyaltyAddCardModel
import ru.mercury.vpclient.features.profile_loyalty_code_sheet.ProfileLoyaltyCodeSheet
import ru.mercury.vpclient.features.profile_loyalty_code_sheet.intent.ProfileLoyaltyCodeIntent
import ru.mercury.vpclient.features.profile_loyalty_code_sheet.model.ProfileLoyaltyCodeModel
import ru.mercury.vpclient.features.profile_privileges_sheet.ProfilePrivilegesSheet
import ru.mercury.vpclient.features.profile_privileges_sheet.intent.ProfilePrivilegeIntent
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.LoyaltyCardType
import ru.mercury.vpclient.shared.data.persistence.database.entity.LoyaltyCardInfoEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.brands.BrandBox
import ru.mercury.vpclient.shared.ui.components.IndicatorIcon
import ru.mercury.vpclient.shared.ui.components.NotificationIconButton
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.profile.ProfileAlphaBankBanner
import ru.mercury.vpclient.shared.ui.components.profile.ProfileAlphaBankCardBanner
import ru.mercury.vpclient.shared.ui.components.profile.ProfileLoyaltyCard
import ru.mercury.vpclient.shared.ui.components.profile.ProfileLoyaltyCardState
import ru.mercury.vpclient.shared.ui.components.profile.ProfileViewMoreButton
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Basket24
import ru.mercury.vpclient.shared.ui.icons.ChevronEnd24
import ru.mercury.vpclient.shared.ui.icons.Heart24
import ru.mercury.vpclient.shared.ui.icons.Info24
import ru.mercury.vpclient.shared.ui.icons.Logout24
import ru.mercury.vpclient.shared.ui.icons.Man24
import ru.mercury.vpclient.shared.ui.icons.Qr24
import ru.mercury.vpclient.shared.ui.icons.VipBadge24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
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
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarHostStateError = remember { SnackbarHostState() }
    val snackbarHostStateTopError = remember { SnackbarHostState() }

    ProfileScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostState = snackbarHostState,
        snackbarHostStateError = snackbarHostStateError,
        snackbarHostStateTopError = snackbarHostStateTopError
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

    if (state.isLoyaltyAddCardSheetVisible) {
        ProfileLoyaltyAddCardSheet(
            state = ProfileLoyaltyAddCardModel(
                mode = state.loyaltyAddCardMode,
                phone = state.loyaltyAddCardPhone,
                cardNumber = state.loyaltyAddCardCardNumber,
                isLoading = state.loyaltyAddCardJob?.isActive == true,
                isPhoneErrorVisible = state.isLoyaltyAddCardPhoneErrorVisible
            ),
            dispatch = { intent ->
                when (intent) {
                    is ProfileLoyaltyAddCardIntent.DismissRequest -> {
                        viewModel.dispatch(ProfileIntent.DismissLoyaltyAddCardSheet)
                    }
                    is ProfileLoyaltyAddCardIntent.ModeClick -> {
                        viewModel.dispatch(ProfileIntent.LoyaltyAddCardModeClick(intent.mode))
                    }
                    is ProfileLoyaltyAddCardIntent.PhoneChange -> {
                        viewModel.dispatch(ProfileIntent.LoyaltyAddCardPhoneChange(intent.phone))
                    }
                    is ProfileLoyaltyAddCardIntent.CardNumberChange -> {
                        viewModel.dispatch(ProfileIntent.LoyaltyAddCardCardNumberChange(intent.cardNumber))
                    }
                    is ProfileLoyaltyAddCardIntent.ConfirmClick -> {
                        when (state.loyaltyAddCardMode) {
                            ProfileLoyaltyAddCardMode.Phone -> {
                                viewModel.dispatch(ProfileIntent.LoyaltyAddCardPhoneConfirmClick)
                            }
                            ProfileLoyaltyAddCardMode.CardNumber -> {
                                viewModel.dispatch(ProfileIntent.LoyaltyAddCardCardNumberConfirmClick)
                            }
                        }
                    }
                }
            }
        )
    }

    if (state.isLoyaltyCodeSheetVisible) {
        ProfileLoyaltyCodeSheet(
            state = ProfileLoyaltyCodeModel(
                mode = state.loyaltyCodeMode,
                phone = state.loyaltyCodePhone,
                cardNumber = state.loyaltyCodeCardNumber,
                code = state.loyaltyCode,
                isLoading = state.isLoyaltyCodeLoading,
                isResendLoading = state.isLoyaltyCodeResendLoading,
                isCodeErrorVisible = state.isLoyaltyCodeErrorVisible,
                resendTimerStartedAt = state.loyaltyCodeResendTimerStartedAt,
                resendSecondsLeft = state.loyaltyCodeResendSecondsLeft,
                resendTimerJob = state.loyaltyCodeResendTimerJob
            ),
            dispatch = { intent ->
                when (intent) {
                    is ProfileLoyaltyCodeIntent.DismissRequest -> {
                        viewModel.dispatch(ProfileIntent.DismissLoyaltyCodeSheet)
                    }
                    is ProfileLoyaltyCodeIntent.StartResendTimerTicker -> {
                        viewModel.dispatch(ProfileIntent.StartLoyaltyCodeResendTimerTicker)
                    }
                    is ProfileLoyaltyCodeIntent.CodeChange -> {
                        viewModel.dispatch(ProfileIntent.LoyaltyCodeChange(intent.code))
                    }
                    is ProfileLoyaltyCodeIntent.ConfirmClick -> {
                        viewModel.dispatch(ProfileIntent.LoyaltyCodeConfirmClick)
                    }
                    is ProfileLoyaltyCodeIntent.ResendCodeClick -> {
                        viewModel.dispatch(ProfileIntent.LoyaltyCodeResendCodeClick)
                    }
                }
            }
        )
    }

    if (state.isProfilePrivilegesSheetVisible) {
        ProfilePrivilegesSheet(
            state = state.profilePrivilegesModel,
            dispatch = { intent ->
                when (intent) {
                    is ProfilePrivilegeIntent.DismissRequest -> {
                        viewModel.dispatch(ProfileIntent.DismissAlphaBankPrivilegesSheet)
                    }
                }
            }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is ProfileEvent.SnackbarMessage -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostState.showSnackbar(event.message) }
            }
            is ProfileEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
            is ProfileEvent.SnackbarTopErrorMessage -> {
                snackbarHostStateTopError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateTopError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun ProfileScreenContent(
    state: ProfileModel,
    dispatch: (ProfileIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    snackbarHostStateError: SnackbarHostState,
    snackbarHostStateTopError: SnackbarHostState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SharedScaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.ProfileTitle),
                            style = MaterialTheme.typography.medium18
                        )
                    },
                    navigationIcon = {
                        NotificationIconButton(
                            showBadge = state.isNotificationBadgeVisible,
                            onClick = { dispatch(ProfileIntent.NotificationClick) }
                        )
                    },
                    actions = {
                        if (state.isFittingButtonVisible) {
                            FittingIconButton(
                                text = state.fittingText,
                                showBadge = state.isFittingBadgeVisible,
                                onClick = { dispatch(ProfileIntent.FittingClick) }
                            )
                        }

                        CartIconButton(
                            text = state.cartText,
                            showBadge = state.isCartBadgeVisible,
                            onClick = { dispatch(ProfileIntent.CartClick) }
                        )

                        MessengerIconButton(
                            showBadge = state.isMessengerBadgeVisible,
                            onClick = { dispatch(ProfileIntent.MessengerClick) }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
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
            },
            snackbarHost = {
                Box(
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    SharedSnackbarHost(
                        hostState = snackbarHostState
                    )

                    SharedSnackbarHost(
                        hostState = snackbarHostStateError,
                        containerColor = MaterialTheme.colorScheme.error
                    )
                }
            }
        ) { innerPadding ->
            SharedLazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding + PaddingValues(top = 16.dp, bottom = 92.dp)
            ) {
                if (state.isLoyaltyCardVisible) {
                    item {
                        ProfileLoyaltyCard(
                            state = ProfileLoyaltyCardState(
                                loyaltyCardInfoEntity = state.visibleLoyaltyCardInfo,
                                onQrButtonClick = { dispatch(ProfileIntent.LoyaltyCardQrClick) },
                                onMoreButtonClick = { dispatch(ProfileIntent.LoyaltyCardMoreClick) }
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                if (state.isAddLoyaltyCardVisible) {
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
                }
                if (state.isLoyaltyCardPlaceholderVisible) {
                    item {
                        Spacer(
                            modifier = Modifier.height(52.dp)
                        )
                    }
                }
                if (state.isAlphaBankBannerVisible) {
                    item {
                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                    }
                    item {
                        when (state.visibleAlphaBankBannerCardType) {
                            LoyaltyCardType.Black -> {
                                ProfileAlphaBankBanner(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    onCloseClick = { dispatch(ProfileIntent.AlphaBankBannerCloseClick) },
                                    onMoreClick = { dispatch(ProfileIntent.AlphaBankBannerMoreClick) }
                                )
                            }
                            LoyaltyCardType.Gold -> {
                                ProfileAlphaBankCardBanner(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    onCloseClick = { dispatch(ProfileIntent.AlphaBankBannerCloseClick) },
                                    onMoreClick = { dispatch(ProfileIntent.AlphaBankBannerMoreClick) }
                                )
                            }
                            LoyaltyCardType.Silver -> Unit
                        }
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier.height(
                            if (state.isLoyaltyCardVisible) 21.dp else 40.dp
                        )
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
                            tint = MaterialTheme.colorScheme.onBackground
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
                            showIndicator = state.isPurchasesBadgeVisible
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
                            tint = MaterialTheme.colorScheme.onBackground
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
                            tint = MaterialTheme.colorScheme.onBackground
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
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { dispatch(ProfileIntent.FavoriteBrandsClick) }
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Heart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = stringResource(ClientStrings.ProfileFavoriteBrands),
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
                if (state.isViewHistoryVisible) {
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
                                        count = 4
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
                                        items = state.visibleViewHistoryProducts,
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
                                    if (state.isViewHistoryMoreVisible) {
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

        SharedSnackbarHost(
            hostState = snackbarHostStateTopError,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 8.dp),
            containerColor = MaterialTheme.colorScheme.error
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileScreenContentPreview(
    @PreviewParameter(ProfileModelPreviewParameterProvider::class) state: ProfileModel
) {
    ProfileScreenContent(
        state = state,
        dispatch = {},
        snackbarHostState = remember { SnackbarHostState() },
        snackbarHostStateError = remember { SnackbarHostState() },
        snackbarHostStateTopError = remember { SnackbarHostState() }
    )
}

private class ProfileModelPreviewParameterProvider: PreviewParameterProvider<ProfileModel> {
    override val values: Sequence<ProfileModel> = sequenceOf(
        ProfileModel(),
        ProfileModel(
            loyaltyCardInfoEntity = LoyaltyCardInfoEntity.Empty.copy(
                loyaltyCardNumber = "G40135",
                bonusAmount = 0,
                typeCard = LoyaltyCardType.Black
            )
        ),
        ProfileModel(
            loyaltyCardInfoEntity = LoyaltyCardInfoEntity.Empty.copy(
                loyaltyCardNumber = "G40135",
                bonusAmount = 0,
                typeCard = LoyaltyCardType.Gold
            )
        ),
        ProfileModel(
            loyaltyCardInfoEntity = LoyaltyCardInfoEntity.Empty.copy(
                loyaltyCardNumber = "G40135",
                bonusAmount = 0,
                typeCard = LoyaltyCardType.Black
            ),
            alphaBankBannerCardType = LoyaltyCardType.Black
        ),
        ProfileModel(
            loyaltyCardInfoEntity = LoyaltyCardInfoEntity.Empty.copy(
                loyaltyCardNumber = "G40135",
                bonusAmount = 0,
                typeCard = LoyaltyCardType.Gold
            ),
            alphaBankBannerCardType = LoyaltyCardType.Gold
        ),
        ProfileModel(
            loyaltyCardInfoEntity = LoyaltyCardInfoEntity.Empty.copy(
                loyaltyCardNumber = "G40135",
                bonusAmount = 0,
                typeCard = LoyaltyCardType.Silver
            )
        )
    )
}
