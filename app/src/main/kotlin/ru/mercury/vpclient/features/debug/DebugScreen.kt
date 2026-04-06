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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.core.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.core.ui.components.system.ClientSnackbarHost
import ru.mercury.vpclient.core.ui.components.system.ClientTopAppBar
import ru.mercury.vpclient.core.ui.icons.Close24
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.ktx.rememberNavigateToAppSettings
import ru.mercury.vpclient.core.ui.ktx.rememberNavigateToDeveloperSettings
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular18
import ru.mercury.vpclient.core.ui.theme.spanMedium14
import ru.mercury.vpclient.core.ui.theme.spanRegular14
import ru.mercury.vpclient.features.debug.event.DebugEvent
import ru.mercury.vpclient.features.debug.intent.DebugIntent
import ru.mercury.vpclient.features.debug.model.DebugModel
import ru.mercury.vpclient.features.debug.ui.DebugEnvironmentDialog

@Composable
fun DebugScreen(
    viewModel: DebugViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

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
            is DebugEvent.FinishScreen -> activity?.finish()
            is DebugEvent.SnackbarMessage -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostState.showSnackbar(event.message) }
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
            ClientTopAppBar(
                title = {
                    Text(
                        text = "Debug Settings"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(DebugIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        },
        snackbarHost = {
            ClientSnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    ) { innerPadding ->
        ClientLazyColumn(
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
                            withStyle(MaterialTheme.typography.spanMedium14.copy(color = MaterialTheme.colorScheme.onBackground)) { append("DeviceId: ") }
                            withStyle(MaterialTheme.typography.spanRegular14.copy(color = MaterialTheme.colorScheme.onBackground)) { append(state.deviceId) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(MaterialTheme.typography.spanMedium14.copy(color = MaterialTheme.colorScheme.onBackground)) { append("UserToken: ") }
                            withStyle(MaterialTheme.typography.spanRegular14.copy(color = MaterialTheme.colorScheme.onBackground)) { append(state.userToken) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(MaterialTheme.typography.spanMedium14.copy(color = MaterialTheme.colorScheme.onBackground)) { append("VersionName: ") }
                            withStyle(MaterialTheme.typography.spanRegular14.copy(color = MaterialTheme.colorScheme.onBackground)) { append(BuildConfig.VERSION_NAME) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(MaterialTheme.typography.spanMedium14.copy(color = MaterialTheme.colorScheme.onBackground)) { append("VersionCode: ") }
                            withStyle(MaterialTheme.typography.spanRegular14.copy(color = MaterialTheme.colorScheme.onBackground)) { append(BuildConfig.VERSION_CODE.toString()) }
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
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToAppSettings() },
                    headlineContent = {
                        Text(
                            text = "Настройки приложения",
                            style = MaterialTheme.typography.regular18.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = Color.Transparent),
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
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navigateToDeveloperSettings() },
                    headlineContent = {
                        Text(
                            text = "Настройки разработчика",
                            style = MaterialTheme.typography.regular18.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = Color.Transparent),
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
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dispatch(DebugIntent.ToggleRequestDelay(!state.requestDelayEnabled)) },
                    headlineContent = {
                        Text(
                            text = "Задержка API-запросов",
                            style = MaterialTheme.typography.regular18.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = state.requestDelayEnabled,
                            onCheckedChange = null
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = Color.Transparent),
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
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dispatch(DebugIntent.DropLocalDbClick) },
                    headlineContent = {
                        Text(
                            text = "Очистить локальную базу данных",
                            style = MaterialTheme.typography.regular18.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = Color.Transparent),
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
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dispatch(DebugIntent.EnvironmentClick) },
                    headlineContent = {
                        Text(
                            text = "Окружение",
                            style = MaterialTheme.typography.regular18.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    trailingContent = {
                        Text(
                            text = state.environment.name,
                            style = MaterialTheme.typography.regular18.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = Color.Transparent),
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun DebugActivityContentPreview() {
    ClientTheme {
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
