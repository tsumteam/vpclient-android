@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.loyalty_add_card_sheet

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.loyalty_add_card_sheet.intent.LoyaltyAddCardIntent
import ru.mercury.vpclient.features.loyalty_add_card_sheet.model.LoyaltyAddCardMode
import ru.mercury.vpclient.features.loyalty_add_card_sheet.model.LoyaltyAddCardModel
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.components.system.ClientTextField
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium17
import ru.mercury.vpclient.shared.ui.theme.medium12
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular12

@Composable
fun LoyaltyAddCardSheet(
    state: LoyaltyAddCardModel,
    dispatch: (LoyaltyAddCardIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val sheetDispatch: (LoyaltyAddCardIntent) -> Unit = { intent ->
        when (intent) {
            is LoyaltyAddCardIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            else -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(LoyaltyAddCardIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .navigationBarsPadding()
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.LoyaltyAddCardTitleCaps),
                        style = MaterialTheme.typography.livretMedium17.copy(
                            lineHeight = 26.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(LoyaltyAddCardIntent.DismissRequest) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )

            SharedTabRow(
                state = SharedTabRowState(
                    selectedIndex = when (state.mode) {
                        LoyaltyAddCardMode.Phone -> 0
                        LoyaltyAddCardMode.CardNumber -> 1
                    },
                    firstTabText = stringResource(ClientStrings.LoyaltyAddCardPhoneModeCaps),
                    secondTabText = stringResource(ClientStrings.LoyaltyAddCardCardModeCaps),
                    onFirstTabClick = {
                        sheetDispatch(LoyaltyAddCardIntent.ModeClick(LoyaltyAddCardMode.Phone))
                    },
                    onSecondTabClick = {
                        sheetDispatch(LoyaltyAddCardIntent.ModeClick(LoyaltyAddCardMode.CardNumber))
                    },
                    isLoading = false
                ),
                textStyle = MaterialTheme.typography.medium12.copy(letterSpacing = .3.sp),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            when (state.mode) {
                LoyaltyAddCardMode.Phone -> {
                    ClientTextField(
                        value = state.phone,
                        onValueChange = { sheetDispatch(LoyaltyAddCardIntent.PhoneChange(it)) },
                        label = stringResource(ClientStrings.LoyaltyAddCardPhoneLabel),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .height(52.dp)
                            .focusRequester(focusRequester)
                            .then(
                                when {
                                    state.isPhoneErrorVisible -> Modifier.border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.error,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    else -> Modifier
                                }
                            ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
                LoyaltyAddCardMode.CardNumber -> {
                    ClientTextField(
                        value = state.cardNumber,
                        onValueChange = { sheetDispatch(LoyaltyAddCardIntent.CardNumberChange(it)) },
                        label = stringResource(ClientStrings.LoyaltyAddCardCardPlaceholder),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .height(52.dp)
                            .focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                if (state.mode == LoyaltyAddCardMode.Phone && state.isPhoneErrorVisible) {
                    Text(
                        text = stringResource(ClientStrings.LoyaltyAddCardWrongPhoneError),
                        modifier = Modifier
                            .padding(start = 32.dp, top = 4.dp, end = 16.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.regular12.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 16.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }

            Button(
                onClick = { sheetDispatch(LoyaltyAddCardIntent.ConfirmClick) },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.isConfirmEnabled && !state.isLoading,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = when {
                        state.isLoading -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.disabled
                    },
                    disabledContentColor = when {
                        state.isLoading -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onDisabled
                    }
                )
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(ClientStrings.ProfileLoyaltyConfirm),
                            style = MaterialTheme.typography.medium15.copy(
                                letterSpacing = .3.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(state.mode) {
        focusRequester.requestFocus()
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun LoyaltyAddCardSheetPreview(
    @PreviewParameter(LoyaltyAddCardModelProvider::class) state: LoyaltyAddCardModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LoyaltyAddCardSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class LoyaltyAddCardModelProvider: PreviewParameterProvider<LoyaltyAddCardModel> {
    override val values: Sequence<LoyaltyAddCardModel> = sequenceOf(
        LoyaltyAddCardModel(
            phone = "+7 929 550-62-34"
        ),
        LoyaltyAddCardModel(
            phone = "+7 929 550-62",
            isPhoneErrorVisible = true
        ),
        LoyaltyAddCardModel(
            mode = LoyaltyAddCardMode.CardNumber
        ),
        LoyaltyAddCardModel(
            mode = LoyaltyAddCardMode.CardNumber,
            cardNumber = "1234567890",
            isLoading = true
        )
    )
}
