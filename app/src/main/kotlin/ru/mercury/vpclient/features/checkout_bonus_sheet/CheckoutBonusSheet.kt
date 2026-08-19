@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.checkout_bonus_sheet

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.checkout_bonus_sheet.intent.CheckoutBonusIntent
import ru.mercury.vpclient.features.checkout_bonus_sheet.model.CheckoutBonusModel
import ru.mercury.vpclient.shared.domain.mapper.formatCodeResendTime
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.SmsCodeInput
import ru.mercury.vpclient.shared.ui.components.SmsCodeInputState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CheckoutBonusSheet(
    state: CheckoutBonusModel,
    dispatch: (CheckoutBonusIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val sheetDispatch: (CheckoutBonusIntent) -> Unit = { intent ->
        when (intent) {
            is CheckoutBonusIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            else -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CheckoutBonusIntent.DismissRequest) },
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
                        text = stringResource(ClientStrings.CheckoutBonusSheetTitleCaps),
                        style = MaterialTheme.typography.livretMedium18.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(CheckoutBonusIntent.DismissRequest) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    Spacer(
                        modifier = Modifier.size(48.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )

            SmsCodeInput(
                state = SmsCodeInputState(
                    value = state.code,
                    isErrorVisible = state.isCodeErrorTextVisible
                ),
                onValueChange = { sheetDispatch(CheckoutBonusIntent.CodeChange(it)) },
                focusRequester = focusRequester,
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.SmsOtpCode },
                keyboardActions = KeyboardActions(
                    onDone = { sheetDispatch(CheckoutBonusIntent.ConfirmClick) }
                )
            )

            if (state.isCodeErrorTextVisible) {
                Text(
                    text = stringResource(ClientStrings.CodeInvalidError),
                    modifier = Modifier
                        .padding(start = 32.dp, top = 4.dp, end = 16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )
            }

            Text(
                text = stringResource(ClientStrings.LoyaltyCodeSentCard),
                modifier = Modifier
                    .padding(start = 16.dp, top = 32.dp, end = 16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.regular15.copy(
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Center
                )
            )

            when {
                state.isResendTextVisible -> {
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(ClientStrings.LoyaltyCodeResendCountdown, ""))
                            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                                append(formatCodeResendTime(state.resendSecondsLeft))
                            }
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 3.dp, end = 16.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.regular15.copy(
                            lineHeight = 19.sp,
                            letterSpacing = .2.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                else -> {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 2.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(ClientStrings.LoyaltyCodeResendQuestion),
                            style = MaterialTheme.typography.regular15
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable(
                                    enabled = !state.isResendLoading,
                                    onClick = { sheetDispatch(CheckoutBonusIntent.ResendCodeClick) }
                                )
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(ClientStrings.CodeResendButton),
                                modifier = Modifier.alpha(if (state.isResendLoading) 0F else 1F),
                                style = MaterialTheme.typography.medium15.copy(
                                    color = MaterialTheme.colorScheme.error,
                                    letterSpacing = .3.sp
                                )
                            )

                            when {
                                state.isResendLoadingIndicatorVisible -> {
                                    LinearProgressIndicator(
                                        modifier = Modifier.width(120.dp),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .2F)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(58.dp)
            )

            Button(
                onClick = { sheetDispatch(CheckoutBonusIntent.ConfirmClick) },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.isConfirmEnabled,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
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
                    state.isLoadingIndicatorVisible -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(ClientStrings.ProfileLoyaltyConfirm),
                            style = MaterialTheme.typography.medium15.copy(letterSpacing = .3.sp)
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CheckoutBonusSheetPreview(
    @PreviewParameter(CheckoutBonusSheetModelProvider::class) state: CheckoutBonusModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CheckoutBonusSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CheckoutBonusSheetModelProvider: PreviewParameterProvider<CheckoutBonusModel> {
    override val values: Sequence<CheckoutBonusModel> = sequenceOf(
        CheckoutBonusModel(resendSecondsLeft = 30),
        CheckoutBonusModel(
            code = "152",
            resendSecondsLeft = 30
        ),
        CheckoutBonusModel(
            code = "152901",
            resendSecondsLeft = 30
        ),
        CheckoutBonusModel(
            code = "152901",
            isCodeErrorTextVisible = true,
            resendSecondsLeft = 30
        ),
        CheckoutBonusModel(
            code = "152901",
            isLoading = true,
            resendSecondsLeft = 30
        ),
        CheckoutBonusModel(),
        CheckoutBonusModel(
            isResendLoading = true
        )
    )
}
