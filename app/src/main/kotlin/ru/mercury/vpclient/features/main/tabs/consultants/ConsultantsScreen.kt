package ru.mercury.vpclient.features.main.tabs.consultants

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.core.ui.components.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.core.ui.components.ClientLazyColumn
import ru.mercury.vpclient.core.ui.components.ClientSnackbarHost
import ru.mercury.vpclient.core.ui.components.ConsultantBox
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.preview.ConsultantsModelProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium17
import ru.mercury.vpclient.core.ui.theme.onBackground
import ru.mercury.vpclient.features.main.tabs.consultants.event.ConsultantsEvents
import ru.mercury.vpclient.features.main.tabs.consultants.intent.ConsultantsIntent
import ru.mercury.vpclient.features.main.tabs.consultants.model.ConsultantsModel

@Composable
fun ConsultantsScreen(
    viewModel: ConsultantsViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    ConsultantsScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is ConsultantsEvents.SnackbarMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun ConsultantsScreenContent(
    state: ConsultantsModel,
    dispatch: (ConsultantsIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ConsultantsTitle),
                        style = MaterialTheme.typography.medium17.copy(textAlign = TextAlign.Center).onBackground()
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = Color.White)
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
        ClientLazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
        ) {
            if (state.isLoading && state.employees.isEmpty()) {
                items(
                    count = 3,
                    key = { index -> "consultant_loading_$index" }
                ) {
                    ConsultantBox(
                        employee = EmployeeEntity.Empty,
                        onActionClick = {},
                        onActiveClick = {},
                        onClick = {}
                    )
                }
            } else {
                items(
                    items = state.employees,
                    key = { employee -> employee.employeeId }
                ) { employee ->
                    ConsultantBox(
                        employee = employee,
                        onActionClick = {},
                        onActiveClick = { dispatch(ConsultantsIntent.SetActiveConsultant(employee.employeeId)) },
                        onClick = { dispatch(ConsultantsIntent.ConsultantClick(employee.employeeId)) }
                    )
                }
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun ConsultantsScreenContentPreview(
    @PreviewParameter(ConsultantsModelProvider::class) state: ConsultantsModel
) {
    ClientTheme {
        ConsultantsScreenContent(
            state = state,
            dispatch = {},
            snackbarHostStateError = remember { SnackbarHostState() }
        )
    }
}
