package ru.mercury.vpclient.features.authentication

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isSensitiveData
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.mercury.vpclient.core.entity.LoginFailure
import ru.mercury.vpclient.core.event.FocusEvent
import ru.mercury.vpclient.core.ui.components.VPClientButton
import ru.mercury.vpclient.core.ui.components.VPClientSecureTextField
import ru.mercury.vpclient.core.ui.components.VPClientTextField
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientStrings
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography
import ru.mercury.vpclient.features.authentication.intent.AuthenticationIntent
import ru.mercury.vpclient.features.authentication.model.AuthenticationModel
import ru.mercury.vpclient.features.authentication.ui.AuthenticationAttentionDialog

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    AuthenticationScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is FocusEvent.Clear -> focusManager.clearFocus()
            is FocusEvent.Down -> focusManager.moveFocus(FocusDirection.Down)
        }
    }

    if (state.failure is LoginFailure.Network) {
        AuthenticationAttentionDialog(
            onDismissRequest = { viewModel.dispatch(AuthenticationIntent.DismissAttention) }
        )
    }
}

@Composable
private fun AuthenticationScreenContent(
    state: AuthenticationModel,
    dispatch: (AuthenticationIntent) -> Unit
) {
    val passwordTextState = rememberTextFieldState(initialText = state.password.value)

    LaunchedEffect(passwordTextState) {
        snapshotFlow { passwordTextState.text.toString() }
            .distinctUntilChanged()
            .collect { dispatch(AuthenticationIntent.EnterPassword(it)) }
    }

    LaunchedEffect(state.password.value) {
        if (passwordTextState.text.toString() != state.password.value) {
            passwordTextState.setTextAndPlaceCursorAtEnd(state.password.value)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        ConstraintLayout(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .fillMaxSize()
        ) {
            val (
                logoIcon,
                titleText,
                usernameField,
                passwordField,
                loginSpacer,
                loginButton,
                bottomSpacer
            ) = createRefs()

            val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

            Icon(
                painter = painterResource(VPClientIcons.VipPlatinumVPClient),
                contentDescription = null,
                modifier = Modifier.constrainAs(logoIcon) {
                    width = Dimension.value(95.dp)
                    height = Dimension.value(69.dp)
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, if (imeVisible) 32.dp else 0.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(titleText.top)
                },
                tint = Color.Black
            )

            Text(
                text = stringResource(VPClientStrings.AppName),
                modifier = Modifier
                    .constrainAs(titleText) {
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                        start.linkTo(parent.start)
                        top.linkTo(logoIcon.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(usernameField.top)
                    }
                    .padding(start = 16.dp, top = if (imeVisible) 16.dp else 70.dp, end = 16.dp),
                textAlign = TextAlign.Center,
                style = VPClientTypography.Regular_22_OnBackground
            )

            VPClientTextField(
                value = state.username.value,
                onValueChange = { dispatch(AuthenticationIntent.EnterUsername(it)) },
                label = stringResource(VPClientStrings.AppName),
                isErrorVisible = state.isLoginErrorVisible,
                error = stringResource(VPClientStrings.AppName),
                modifier = Modifier
                    .constrainAs(usernameField) {
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                        start.linkTo(parent.start)
                        top.linkTo(titleText.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(passwordField.top)
                    }
                    .padding(start = 16.dp, top = 42.dp, end = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { dispatch(AuthenticationIntent.DownFocus) }
                )
            )

            VPClientSecureTextField(
                state = passwordTextState,
                label = stringResource(VPClientStrings.AppName),
                isErrorVisible = state.isPasswordErrorVisible,
                error = stringResource(VPClientStrings.AppName),
                isPasswordVisible = state.isPasswordVisible,
                onPasswordVisibilityClick = { dispatch(AuthenticationIntent.PasswordVisibilityClick) },
                modifier = Modifier
                    .constrainAs(passwordField) {
                        width = Dimension.fillToConstraints
                        height = Dimension.wrapContent
                        start.linkTo(parent.start)
                        top.linkTo(usernameField.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(loginSpacer.top)
                    }
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                    .semantics { isSensitiveData = true },
                onImeAction = { dispatch(AuthenticationIntent.ClearFocus) }
            )

            Spacer(
                modifier = Modifier.constrainAs(loginSpacer) {
                    width = Dimension.fillToConstraints
                    height = Dimension.value(46.dp)
                    start.linkTo(parent.start)
                    top.linkTo(passwordField.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(loginButton.top)
                }
            )

            VPClientButton(
                onClick = { dispatch(AuthenticationIntent.LoginClick) },
                modifier = Modifier
                    .constrainAs(loginButton) {
                        width = Dimension.fillToConstraints
                        height = Dimension.value(56.dp)
                        start.linkTo(parent.start)
                        top.linkTo(loginSpacer.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(bottomSpacer.top)
                    }
                    .padding(horizontal = 16.dp),
                text = stringResource(VPClientStrings.AppName),
                loading = state.isAuthJobActive
            )

            Spacer(
                modifier = Modifier.constrainAs(bottomSpacer) {
                    width = Dimension.fillToConstraints
                    height = Dimension.value(if (imeVisible) 0.dp else 16.dp)
                    start.linkTo(parent.start)
                    top.linkTo(loginButton.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )

            createVerticalChain(
                logoIcon, titleText, usernameField, passwordField, loginSpacer, loginButton, bottomSpacer,
                chainStyle = ChainStyle.Packed
            )
        }
    }
}

@Preview
@Preview(fontScale = 1.5F)
@Composable
private fun AuthenticationScreenContentPreview() {
    VPClientTheme {
        AuthenticationScreenContent(
            state = AuthenticationModel(),
            dispatch = {}
        )
    }
}
