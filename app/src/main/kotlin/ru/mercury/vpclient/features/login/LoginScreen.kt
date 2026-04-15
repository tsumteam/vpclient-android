package ru.mercury.vpclient.features.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.data.entity.PhoneValidationError
import ru.mercury.vpclient.shared.ui.components.AgreementText
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.ClientSnackbarHost
import ru.mercury.vpclient.shared.ui.components.system.ClientTextField
import ru.mercury.vpclient.shared.ui.icons.Logo82
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.LoginModelProvider
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium21
import ru.mercury.vpclient.shared.ui.transformation.PhoneInputTransformation
import ru.mercury.vpclient.shared.ui.transformation.PhoneOutputTransformation
import ru.mercury.vpclient.features.login.event.LoginEvents
import ru.mercury.vpclient.features.login.intent.LoginIntent
import ru.mercury.vpclient.features.login.model.LoginModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val snackbarHostStateError = remember { SnackbarHostState() }

    LoginScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        focusRequester = focusRequester,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(flow = viewModel.eventFlow) { event ->
        when (event) {
            is LoginEvents.ClearFocus -> focusManager.clearFocus()
            is LoginEvents.SnackbarMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
private fun LoginScreenContent(
    state: LoginModel,
    dispatch: (LoginIntent) -> Unit,
    focusRequester: FocusRequester,
    snackbarHostStateError: SnackbarHostState
) {
    val phoneInputTransformation = remember { PhoneInputTransformation() }
    val phoneOutputTransformation = remember { PhoneOutputTransformation() }

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
                onClick = { dispatch(LoginIntent.LoginClick) },
                text = stringResource(ClientStrings.LoginButton),
                loading = state.isLoading,
                enabled = state.isLoginEnabled,
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
                text = stringResource(ClientStrings.LoginTitle),
                modifier = Modifier
                    .padding(start = 16.dp, top = 36.dp, end = 16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.livretMedium21.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )

            ClientTextField(
                value = state.phone,
                onValueChange = { dispatch(LoginIntent.EnterPhone(it)) },
                label = stringResource(ClientStrings.LoginPhoneLabel),
                isErrorVisible = state.phoneValidationError == PhoneValidationError.Invalid,
                error = stringResource(ClientStrings.LoginPhoneInvalidError),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 40.dp, end = 16.dp)
                    .focusRequester(focusRequester)
                    .semantics { contentType = ContentType.PhoneNumber },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { dispatch(LoginIntent.OnKeyboardDone) }
                ),
                inputTransformation = phoneInputTransformation,
                outputTransformation = phoneOutputTransformation
            )

            AgreementText(
                agreementTextRes = ClientStrings.LoginAgreementText,
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenContentPreview(
    @PreviewParameter(LoginModelProvider::class) state: LoginModel
) {
    ClientTheme {
        LoginScreenContent(
            state = state,
            dispatch = {},
            focusRequester = remember { FocusRequester() },
            snackbarHostStateError = remember { SnackbarHostState() }
        )
    }
}
