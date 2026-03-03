package ru.mercury.vpclient.features.register

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
import androidx.compose.ui.focus.FocusDirection
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
import ru.mercury.vpclient.core.entity.PhoneValidationError
import ru.mercury.vpclient.core.ui.components.AgreementText
import ru.mercury.vpclient.core.ui.components.ClientButton
import ru.mercury.vpclient.core.ui.components.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.core.ui.components.ClientSnackbarHost
import ru.mercury.vpclient.core.ui.components.ClientTextField
import ru.mercury.vpclient.core.ui.icons.Logo82
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.preview.RegisterModelProvider
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.livretMedium21
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.core.ui.transformation.PhoneInputTransformation
import ru.mercury.vpclient.core.ui.transformation.PhoneOutputTransformation
import ru.mercury.vpclient.features.register.event.RegisterEvents
import ru.mercury.vpclient.features.register.intent.RegisterIntent
import ru.mercury.vpclient.features.register.model.RegisterModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val snackbarHostStateError = remember { SnackbarHostState() }

    RegisterScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        focusRequester = focusRequester,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(flow = viewModel.eventFlow) { event ->
        when (event) {
            is RegisterEvents.MoveFocusDown -> focusManager.moveFocus(FocusDirection.Down)
            is RegisterEvents.ClearFocus -> focusManager.clearFocus()
            is RegisterEvents.SnackbarMessage -> {
                snackbarHostStateError.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.message) }
                }
            }
        }
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
private fun RegisterScreenContent(
    state: RegisterModel,
    dispatch: (RegisterIntent) -> Unit,
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
                onClick = { dispatch(RegisterIntent.RegisterClick) },
                text = stringResource(ClientStrings.RegisterButton),
                loading = state.isLoading,
                enabled = state.isRegisterEnabled,
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
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(ClientStrings.RegisterTitle),
                modifier = Modifier
                    .padding(start = 16.dp, top = 36.dp, end = 16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.livretMedium21.copy(textAlign = TextAlign.Center).onBackground()
            )

            ClientTextField(
                value = state.name,
                onValueChange = { dispatch(RegisterIntent.EnterName(it)) },
                label = stringResource(ClientStrings.RegisterNameLabel),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 40.dp, end = 16.dp)
                    .focusRequester(focusRequester)
                    .semantics { contentType = ContentType.PersonFullName },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { dispatch(RegisterIntent.MoveFocusDown) }
                )
            )

            ClientTextField(
                value = state.phone,
                onValueChange = { dispatch(RegisterIntent.EnterPhone(it)) },
                label = stringResource(ClientStrings.RegisterPhoneLabel),
                isErrorVisible = state.phoneValidationError == PhoneValidationError.Invalid,
                error = stringResource(ClientStrings.RegisterPhoneInvalidError),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                    .semantics { contentType = ContentType.PhoneNumber },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { dispatch(RegisterIntent.OnKeyboardDone) }
                ),
                inputTransformation = phoneInputTransformation,
                outputTransformation = phoneOutputTransformation
            )

            AgreementText(
                agreementTextRes = ClientStrings.RegisterAgreementText,
                modifier = Modifier
                    .padding(start = 16.dp, top = 28.dp, end = 16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun RegisterScreenContentPreview(
    @PreviewParameter(RegisterModelProvider::class) state: RegisterModel
) {
    ClientTheme {
        RegisterScreenContent(
            state = state,
            dispatch = {},
            focusRequester = remember { FocusRequester() },
            snackbarHostStateError = remember { SnackbarHostState() }
        )
    }
}
