@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.auth_code

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.auth_code.event.CodeEvents
import ru.mercury.vpclient.features.auth_code.intent.CodeIntent
import ru.mercury.vpclient.features.auth_code.model.CodeModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.shared.domain.mapper.formatCodeResendTime
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.domain.usecase.AuthValidateCodeUseCase.CodeValidationError
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.SmsCodeInput
import ru.mercury.vpclient.shared.ui.components.SmsCodeInputState
import ru.mercury.vpclient.shared.ui.icons.Logo82
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium21
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CodeScreen(
    viewModel: CodeViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    CodeScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        focusRequester = focusRequester,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is CodeEvents.ClearFocus -> focusManager.clearFocus()
            is CodeEvents.SnackbarMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
private fun CodeScreenContent(
    state: CodeModel,
    dispatch: (CodeIntent) -> Unit,
    focusRequester: FocusRequester,
    snackbarHostStateError: SnackbarHostState
) {
    SharedScaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { dispatch(CodeIntent.HideKeyboard) })
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Icon(
                        imageVector = Logo82,
                        contentDescription = null,
                        modifier = Modifier.size(82.dp, 57.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { dispatch(CodeIntent.ConfirmClick) },
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding()
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.isConfirmEnabled && !state.isLoading,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isConfirmEnabled || state.isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.disabled,
                    contentColor = if (state.isConfirmEnabled || state.isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onDisabled,
                    disabledContainerColor = if (state.isConfirmEnabled || state.isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.disabled,
                    disabledContentColor = if (state.isConfirmEnabled || state.isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onDisabled
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
                            text = stringResource(ClientStrings.CodeButton),
                            maxLines = 1,
                            style = MaterialTheme.typography.medium15.copy(
                                textAlign = TextAlign.Center,
                                letterSpacing = .3.sp
                            )
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
                containerColor = MaterialTheme.colorScheme.error
            )
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(ClientStrings.CodeTitle),
                    modifier = Modifier
                        .padding(top = 36.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.livretMedium21.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 46.dp)
                        .height(84.dp)
                ) {
                    SmsCodeInput(
                        state = SmsCodeInputState(
                            value = state.code,
                            isErrorVisible = state.codeValidationError != null
                        ),
                        onValueChange = { dispatch(CodeIntent.EnterCode(it)) },
                        focusRequester = focusRequester,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .semantics { contentType = ContentType.SmsOtpCode },
                        keyboardActions = KeyboardActions(
                            onDone = { dispatch(CodeIntent.OnKeyboardDone) }
                        )
                    )

                    if (state.codeValidationError != null) {
                        Text(
                            text = when (state.codeValidationError) {
                                CodeValidationError.Empty -> stringResource(ClientStrings.CodeEmptyError)
                                CodeValidationError.Invalid -> stringResource(ClientStrings.CodeInvalidError)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                            style = MaterialTheme.typography.regular12.copy(
                                color = MaterialTheme.colorScheme.error,
                                letterSpacing = .2.sp,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
            item {
                Text(
                    text = buildAnnotatedString {
                        val formattedPhone = formatPhoneForDisplay(state.clientEntity.phone)
                        val codeSentDescription = stringResource(ClientStrings.CodeSentDescription, formattedPhone)
                        val phoneStart = codeSentDescription.indexOf(formattedPhone)
                        append(codeSentDescription.take(phoneStart))
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(formattedPhone) }
                        append(codeSentDescription.substring(phoneStart + formattedPhone.length))
                    },
                    modifier = Modifier
                        .padding(top = 14.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.regular15.copy(
                        lineHeight = 19.sp,
                        letterSpacing = .2.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                when {
                    state.resendSecondsLeft > 0 -> {
                        Text(
                            text = buildAnnotatedString {
                                append(stringResource(ClientStrings.CodeResendCountdown))
                                append(" ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(formatCodeResendTime(state.resendSecondsLeft))
                                }
                            },
                            modifier = Modifier
                                .padding(top = 4.dp)
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
                                .padding(top = 2.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                space = 2.dp,
                                alignment = Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(ClientStrings.CodeResendQuestion),
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
                                        onClick = { dispatch(CodeIntent.ResendCodeClick) }
                                    )
                                    .padding(2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(ClientStrings.CodeResendButton),
                                    modifier = Modifier.alpha(if (state.isResendLoading) 0F else 1F),
                                    style = MaterialTheme.typography.medium15.copy(
                                        textAlign = TextAlign.Center,
                                        letterSpacing = .3.sp
                                    )
                                )

                                if (state.isResendLoading) {
                                    LinearProgressIndicator(
                                        modifier = Modifier.width(120.dp),
                                        color = MaterialTheme.colorScheme.onBackground,
                                        trackColor = MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = .2F
                                        )
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

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CodeScreenContentPreview(
    @PreviewParameter(CodeModelProvider::class) state: CodeModel
) {
    CodeScreenContent(
        state = state,
        dispatch = {},
        focusRequester = remember { FocusRequester() },
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class CodeModelProvider: PreviewParameterProvider<CodeModel> {
    override val values: Sequence<CodeModel> = sequenceOf(
        baseState,
        baseState.copy(code = "123"),
        baseState.copy(codeValidationError = CodeValidationError.Empty),
        baseState.copy(code = "123456", codeValidationError = CodeValidationError.Invalid),
        baseState.copy(code = "123456", isLoading = true),
        baseState.copy(resendSecondsLeft = 22, resendTimerJob = Job()),
        baseState.copy(resendCodeJob = Job()),
        baseState.copy(code = "123456", resendSecondsLeft = 12, resendCodeJob = Job())
    )

    companion object {
        private val baseState = CodeModel(
            clientEntity = ClientEntity(
                phone = "+79991234567",
                name = "Иван"
            )
        )
    }
}
