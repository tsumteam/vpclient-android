@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.loyalty_code_sheet

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
import ru.mercury.vpclient.features.loyalty_add_card_sheet.model.LoyaltyAddCardMode
import ru.mercury.vpclient.features.loyalty_code_sheet.intent.LoyaltyCodeIntent
import ru.mercury.vpclient.features.loyalty_code_sheet.model.LoyaltyCodeModel
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
fun LoyaltyCodeSheet(
    state: LoyaltyCodeModel,
    dispatch: (LoyaltyCodeIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val sheetDispatch: (LoyaltyCodeIntent) -> Unit = { intent ->
        when (intent) {
            is LoyaltyCodeIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            else -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(LoyaltyCodeIntent.DismissRequest) },
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
                        text = stringResource(ClientStrings.LoyaltyCodeTitleCaps),
                        style = MaterialTheme.typography.livretMedium18
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(LoyaltyCodeIntent.DismissRequest) }
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

            SmsCodeInput(
                state = SmsCodeInputState(
                    value = state.code,
                    isErrorVisible = state.isCodeErrorVisible
                ),
                onValueChange = { sheetDispatch(LoyaltyCodeIntent.CodeChange(it)) },
                focusRequester = focusRequester,
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                    .fillMaxWidth()
                    .semantics { contentType = ContentType.SmsOtpCode },
                keyboardActions = KeyboardActions(
                    onDone = { sheetDispatch(LoyaltyCodeIntent.ConfirmClick) }
                )
            )

            if (state.isCodeErrorVisible) {
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
                    .padding(start = 16.dp, top = 26.dp, end = 16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.regular15.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Center
                )
            )

            when {
                state.resendSecondsLeft > 0 -> {
                    val resendTime = formatCodeResendTime(state.resendSecondsLeft)
                    Text(
                        text = buildAnnotatedString {
                            append(
                                stringResource(
                                    ClientStrings.LoyaltyCodeResendCountdown,
                                    ""
                                )
                            )
                            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                                append(resendTime)
                            }
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.regular15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
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
                            style = MaterialTheme.typography.regular15.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 19.sp,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable(
                                    enabled = !state.isResendLoading,
                                    onClick = { sheetDispatch(LoyaltyCodeIntent.ResendCodeClick) }
                                )
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(ClientStrings.CodeResendButton),
                                modifier = Modifier.alpha(if (state.isResendLoading) 0F else 1F),
                                style = MaterialTheme.typography.medium15.copy(
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center,
                                    letterSpacing = .3.sp
                                )
                            )

                            if (state.isResendLoading) {
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

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Button(
                onClick = { sheetDispatch(LoyaltyCodeIntent.ConfirmClick) },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
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

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun LoyaltyCodeSheetPreview(
    @PreviewParameter(LoyaltyCodeModelProvider::class) state: LoyaltyCodeModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LoyaltyCodeSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class LoyaltyCodeModelProvider: PreviewParameterProvider<LoyaltyCodeModel> {
    override val values: Sequence<LoyaltyCodeModel> = sequenceOf(
        LoyaltyCodeModel(
            mode = LoyaltyAddCardMode.CardNumber,
            resendSecondsLeft = 50
        ),
        LoyaltyCodeModel(
            mode = LoyaltyAddCardMode.CardNumber
        ),
        LoyaltyCodeModel(
            mode = LoyaltyAddCardMode.Phone,
            phone = "79295506234",
            code = "123456"
        ),
        LoyaltyCodeModel(
            mode = LoyaltyAddCardMode.Phone,
            phone = "79295506234",
            code = "123456",
            isCodeErrorVisible = true
        ),
        LoyaltyCodeModel(
            mode = LoyaltyAddCardMode.Phone,
            phone = "79295506234",
            code = "123456",
            isLoading = true
        )
    )
}
