package ru.mercury.vpclient.features.debug

import android.content.ClipData
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.features.debug.event.DebugEvent
import ru.mercury.vpclient.features.debug.intent.DebugIntent
import ru.mercury.vpclient.features.debug.model.DebugModel
import ru.mercury.vpclient.features.debug.ui.DebugEnvironmentDialog
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientSnackbarHost
import ru.mercury.vpclient.shared.ui.components.system.ClientTopAppBar
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.Copy24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.rememberNavigateToAppSettings
import ru.mercury.vpclient.shared.ui.ktx.rememberNavigateToDeveloperSettings
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.medium16
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular18

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
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

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
                            modifier = Modifier.size(24.dp),
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
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            item {
                DebugInfoListItem(
                    title = "UserToken",
                    value = state.userToken.ifEmpty { "Отобразится после авторизации" },
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    ),
                    onCopy = {
                        scope.launch {
                            clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, state.userToken)))
                        }
                    }
                )
            }
            item {
                DebugInfoListItem(
                    title = "VersionName",
                    value = BuildConfig.VERSION_NAME,
                    shape = RoundedCornerShape(4.dp),
                    onCopy = {
                        scope.launch {
                            clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, BuildConfig.VERSION_NAME)))
                        }
                    }
                )
            }
            item {
                DebugInfoListItem(
                    title = "VersionCode",
                    value = BuildConfig.VERSION_CODE.toString(),
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 4.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    onCopy = {
                        scope.launch {
                            clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, BuildConfig.VERSION_CODE.toString())))
                        }
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                DebugActionListItem(
                    title = "Настройки приложения",
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    ),
                    onClick = navigateToAppSettings
                )
            }
            item {
                DebugActionListItem(
                    title = "Настройки разработчика",
                    shape = RoundedCornerShape(4.dp),
                    onClick = navigateToDeveloperSettings
                )
            }
            item {
                DebugActionListItem(
                    title = "Задержка запросов",
                    subtitle = "5 сек",
                    shape = RoundedCornerShape(4.dp),
                    trailingContent = {
                        Switch(
                            checked = state.requestDelayEnabled,
                            onCheckedChange = null
                        )
                    },
                    onClick = { dispatch(DebugIntent.ToggleRequestDelay(!state.requestDelayEnabled)) }
                )
            }
            item {
                DebugActionListItem(
                    title = "Очистить локальную БД",
                    shape = RoundedCornerShape(4.dp),
                    onClick = { dispatch(DebugIntent.DropLocalDbClick) }
                )
            }
            item {
                DebugActionListItem(
                    title = "Окружение",
                    subtitle = "test/uat/prod",
                    shape = RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 4.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    trailingContent = {
                        Text(
                            text = state.environment.name,
                            style = MaterialTheme.typography.medium16.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    },
                    onClick = { dispatch(DebugIntent.EnvironmentClick) }
                )
            }
        }
    }
}

@Composable
private fun DebugInfoListItem(
    title: String,
    value: String,
    shape: Shape,
    onCopy: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.regular18
            )
        },
        supportingContent = {
            Text(
                text = value,
                style = MaterialTheme.typography.regular14
            )
        },
        modifier = Modifier.clip(shape),
        trailingContent = {
            IconButton(onClick = onCopy) {
                Icon(
                    imageVector = Copy24,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        colors = ListItemDefaults.colors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = MaterialTheme.colorScheme.onBackground,
            supportingTextColor = MaterialTheme.colorScheme.secondary,
            trailingIconColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
private fun DebugActionListItem(
    title: String,
    shape: Shape,
    onClick: () -> Unit,
    subtitle: String? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.regular18
            )
        },
        supportingContent = subtitle?.let { text ->
            {
                Text(
                    text = text,
                    style = MaterialTheme.typography.regular14
                )
            }
        },
        trailingContent = trailingContent,
        modifier = Modifier
            .clip(shape)
            .clickable(onClick = onClick),
        colors = ListItemDefaults.colors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            headlineColor = MaterialTheme.colorScheme.onBackground,
            supportingTextColor = MaterialTheme.colorScheme.secondary,
            trailingIconColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@FontScalePreviews
@Composable
private fun DebugActivityContentPreview() {
    ClientTheme {
        DebugActivityContent(
            snackbarHostState = remember { SnackbarHostState() },
            state = DebugModel(
                userToken = "XX-123456"
            ),
            dispatch = {}
        )
    }
}
