package ru.mercury.vpclient.features.code

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.data.entity.CodeValidationError
import ru.mercury.vpclient.shared.domain.mapper.formatCodeResendTime
import ru.mercury.vpclient.shared.domain.mapper.formatPhoneForDisplay
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.ClientSnackbarHost
import ru.mercury.vpclient.shared.ui.components.system.ClientTextButton
import ru.mercury.vpclient.shared.ui.icons.Logo82
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.CodeModelProvider
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium21
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular15
import ru.mercury.vpclient.features.code.event.CodeEvents
import ru.mercury.vpclient.features.code.intent.CodeIntent
import ru.mercury.vpclient.features.code.model.CodeModel
import ru.mercury.vpclient.features.code.ui.SmsCodeInput

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

    ObserveAsEvents(flow = viewModel.eventFlow) { event ->
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
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                title = {
                    Icon(
                        imageVector = Logo82,
                        contentDescription = null,
                        modifier = Modifier.size(82.dp, 57.dp),
                        tint = Color.Black
                    )
                }
            )
        },
        bottomBar = {
            ClientButton(
                onClick = { dispatch(CodeIntent.ConfirmClick) },
                text = stringResource(ClientStrings.CodeButton),
                loading = state.isLoading,
                enabled = state.isConfirmEnabled,
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding()
                    .navigationBarsPadding()
            )
        },
        snackbarHost = {
            ClientSnackbarHost(
                hostState = snackbarHostStateError,
                modifier = Modifier.padding(horizontal = 16.dp),
                containerColor = MaterialTheme.colorScheme.error
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(ClientStrings.CodeTitle),
                modifier = Modifier
                    .padding(start = 16.dp, top = 36.dp, end = 16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.livretMedium21.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 46.dp, end = 16.dp)
                    .height(84.dp)
            ) {
                SmsCodeInput(
                    value = state.code,
                    onValueChange = { dispatch(CodeIntent.EnterCode(it)) },
                    focusRequester = focusRequester,
                    isErrorVisible = state.codeValidationError == CodeValidationError.Invalid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .semantics { contentType = ContentType.SmsOtpCode },
                    keyboardActions = KeyboardActions(
                        onDone = { dispatch(CodeIntent.OnKeyboardDone) }
                    )
                )

                if (state.codeValidationError == CodeValidationError.Invalid) {
                    Text(
                        text = stringResource(ClientStrings.CodeInvalidError),
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
                    .padding(start = 16.dp, top = 14.dp, end = 16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.regular15.copy(
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Center
                )
            )

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
                            .padding(start = 16.dp, top = 15.dp, end = 16.dp)
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
                    Text(
                        text = stringResource(ClientStrings.CodeResendQuestion),
                        modifier = Modifier
                            .padding(start = 16.dp, top = 15.dp, end = 16.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.regular15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 19.sp,
                            letterSpacing = .2.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    ClientTextButton(
                        onClick = { dispatch(CodeIntent.ResendCodeClick) },
                        text = stringResource(ClientStrings.CodeResendButton),
                        isLoading = state.isResendLoading,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CodeScreenContentPreview(
    @PreviewParameter(CodeModelProvider::class) state: CodeModel
) {
    ClientTheme {
        CodeScreenContent(
            state = state,
            dispatch = {},
            focusRequester = remember { FocusRequester() },
            snackbarHostStateError = remember { SnackbarHostState() }
        )
    }
}
