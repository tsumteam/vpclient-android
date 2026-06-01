package ru.mercury.vpclient.features.register

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import ru.mercury.vpclient.features.register.event.RegisterEvents
import ru.mercury.vpclient.features.register.intent.RegisterIntent
import ru.mercury.vpclient.features.register.model.RegisterModel
import ru.mercury.vpclient.shared.data.entity.NameValidationError
import ru.mercury.vpclient.shared.data.entity.PhoneValidationError
import ru.mercury.vpclient.shared.ui.components.AgreementText
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.ClientTextField
import ru.mercury.vpclient.shared.ui.components.system.TopBarState
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.livretMedium21
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.transformation.PhoneInputTransformation
import ru.mercury.vpclient.shared.ui.transformation.PhoneOutputTransformation

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

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is RegisterEvents.MoveFocusDown -> focusManager.moveFocus(FocusDirection.Down)
            is RegisterEvents.ClearFocus -> focusManager.clearFocus()
            is RegisterEvents.SnackbarMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
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
    val nameError = when (state.nameValidationError) {
        NameValidationError.Empty -> stringResource(ClientStrings.RegisterNameEmptyError)
        null -> ""
    }
    val phoneError = when (state.phoneValidationError) {
        PhoneValidationError.Empty -> stringResource(ClientStrings.RegisterPhoneEmptyError)
        PhoneValidationError.Invalid -> stringResource(ClientStrings.RegisterPhoneInvalidError)
        null -> ""
    }

    SharedScaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { dispatch(RegisterIntent.HideKeyboard) })
        },
        topBar = {
            ClientCenterAlignedTopAppBar(
                state = TopBarState.Logo
            )
        },
        bottomBar = {
            Button(
                onClick = { dispatch(RegisterIntent.RegisterClick) },
                modifier = Modifier
                    .padding(16.dp)
                    .imePadding()
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.isRegisterEnabled && !state.isLoading,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (state.isRegisterEnabled || state.isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.disabled,
                    contentColor = if (state.isRegisterEnabled || state.isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onDisabled,
                    disabledContainerColor = if (state.isRegisterEnabled || state.isLoading) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.disabled,
                    disabledContentColor = if (state.isRegisterEnabled || state.isLoading) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onDisabled
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
                            text = stringResource(ClientStrings.RegisterButton),
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
        },
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = stringResource(ClientStrings.RegisterTitle),
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
                ClientTextField(
                    value = state.name,
                    onValueChange = { dispatch(RegisterIntent.EnterName(it)) },
                    label = stringResource(ClientStrings.RegisterNameLabel),
                    isErrorVisible = state.nameValidationError != null,
                    error = nameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                        .focusRequester(focusRequester)
                        .semantics { contentType = ContentType.PersonFullName },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { dispatch(RegisterIntent.MoveFocusDown) }
                    )
                )
            }
            item {
                ClientTextField(
                    value = state.phone,
                    onValueChange = { dispatch(RegisterIntent.EnterPhone(it)) },
                    label = stringResource(ClientStrings.RegisterPhoneLabel),
                    isErrorVisible = state.phoneValidationError != null,
                    error = phoneError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
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
            }
            item {
                AgreementText(
                    agreementTextRes = ClientStrings.RegisterAgreementText,
                    modifier = Modifier
                        .padding(top = 28.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun RegisterScreenContentPreview(
    @PreviewParameter(RegisterModelProvider::class) state: RegisterModel
) {
    RegisterScreenContent(
        state = state,
        dispatch = {},
        focusRequester = remember { FocusRequester() },
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class RegisterModelProvider: PreviewParameterProvider<RegisterModel> {
    override val values: Sequence<RegisterModel> = sequenceOf(
        RegisterModel(),
        RegisterModel(name = "Иван"),
        RegisterModel(phone = "79991234567"),
        RegisterModel(name = "Иван", phone = "79991234567"),
        RegisterModel(nameValidationError = NameValidationError.Empty),
        RegisterModel(phoneValidationError = PhoneValidationError.Empty),
        RegisterModel(phoneValidationError = PhoneValidationError.Invalid),
        RegisterModel(
            nameValidationError = NameValidationError.Empty,
            phoneValidationError = PhoneValidationError.Empty
        ),
        RegisterModel(name = "Иван", phone = "79991234567", isLoading = true)
    )
}
