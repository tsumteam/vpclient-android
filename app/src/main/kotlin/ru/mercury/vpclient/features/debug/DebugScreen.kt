package ru.mercury.vpclient.features.debug

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.core.event.SnackbarEvent
import ru.mercury.vpclient.core.ktx.snackbarMessage
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.core.ui.components.CloseIconButton
import ru.mercury.vpclient.core.ui.components.VPClientLazyColumn
import ru.mercury.vpclient.core.ui.components.VPClientSnackbarHost
import ru.mercury.vpclient.core.ui.components.VPClientTopAppBar
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.ktx.rememberNavigateToAppSettings
import ru.mercury.vpclient.core.ui.ktx.rememberNavigateToDeveloperSettings
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography
import ru.mercury.vpclient.features.debug.intent.DebugIntent
import ru.mercury.vpclient.features.debug.model.DebugModel
import ru.mercury.vpclient.features.debug.ui.DebugEnvironmentDialog
import ru.mercury.vpclient.main.event.MainEventManager

@Composable
fun DebugScreen(
    viewModel: DebugViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    DebugActivityContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostState = snackbarHostState
    )

    if (state.environmentDialog) {
        DebugEnvironmentDialog(
            onDismissRequest = { viewModel.dispatch(DebugIntent.DismissEnvironmentDialog) },
            selectedEnvironment = state.environment,
            onSelectEnvironment = { viewModel.dispatch(DebugIntent.SelectEnvironment(it)) }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is BackRoute -> activity?.finish()
        }
    }

    ObserveAsEvents(
        flow = MainEventManager.eventFlow
    ) { event ->
        when (event) {
            is SnackbarEvent -> {
                snackbarHostState.run {
                    currentSnackbarData?.dismiss()
                    scope.launch { showSnackbar(event.snackbarMessage(context)) }
                }
            }
        }
    }
}

@Composable
private fun DebugActivityContent(
    state: DebugModel,
    dispatch: (DebugIntent) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val navigateToAppSettings = rememberNavigateToAppSettings()
    val navigateToDeveloperSettings = rememberNavigateToDeveloperSettings()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            VPClientTopAppBar(
                title = { Text("Debug Settings") },
                navigationIcon = {
                    CloseIconButton(
                        onClick = { dispatch(DebugIntent.BackClick) }
                    )
                }
            )
        },
        snackbarHost = { VPClientSnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        VPClientLazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .navigationBarsPadding(),
            state = rememberLazyListState()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(VPClientTypography.SpanStyle_Medium_14_OnBackground) { append("DeviceId: ") }
                            withStyle(VPClientTypography.SpanStyle_Regular_14_OnBackground) { append(state.deviceId) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(VPClientTypography.SpanStyle_Medium_14_OnBackground) { append("UserToken: ") }
                            withStyle(VPClientTypography.SpanStyle_Regular_14_OnBackground) { append(state.userToken) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(VPClientTypography.SpanStyle_Medium_14_OnBackground) { append("VersionName: ") }
                            withStyle(VPClientTypography.SpanStyle_Regular_14_OnBackground) { append(BuildConfig.VERSION_NAME) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(VPClientTypography.SpanStyle_Medium_14_OnBackground) { append("VersionCode: ") }
                            withStyle(VPClientTypography.SpanStyle_Regular_14_OnBackground) { append(BuildConfig.VERSION_CODE.toString()) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = .5.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            item {
                Text(
                    text = "Настройки приложения",
                    style = VPClientTypography.Regular_18_OnBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = navigateToAppSettings)
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    thickness = .1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            item {
                Text(
                    text = "Настройки разработчика",
                    style = VPClientTypography.Regular_18_OnBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = navigateToDeveloperSettings)
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    thickness = .1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            item {
                Text(
                    text = "Очистить локальную базу данных",
                    style = VPClientTypography.Regular_18_OnBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { dispatch(DebugIntent.DropLocalDbClick) })
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                )
            }
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    thickness = .1.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { dispatch(DebugIntent.EnvironmentClick) })
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Text(
                        text = "Окружение",
                        style = VPClientTypography.Regular_18_OnBackground
                    )

                    Text(
                        text = state.environment.name,
                        style = VPClientTypography.Regular_14_Secondary
                    )
                }
            }
        }
    }
}

@Preview(heightDp = 1200)
@Preview(heightDp = 1200, fontScale = 1.5F)
@Composable
private fun DebugActivityContentPreview() {
    VPClientTheme {
        DebugActivityContent(
            snackbarHostState = remember { SnackbarHostState() },
            state = DebugModel(
                deviceId = "e97c1f74-5c30-4e0e-8411-4e82b5fcb0c2",
                userToken = "XX-123456"
            ),
            dispatch = {}
        )
    }
}
