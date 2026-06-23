@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.consultants

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.consultants.event.ConsultantsEvents
import ru.mercury.vpclient.features.consultants.intent.ConsultantsIntent
import ru.mercury.vpclient.features.consultants.model.ConsultantsModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.consultants.ConsultantCard
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.launcherDialer
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun ConsultantsScreen(
    viewModel: ConsultantsViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
            is ConsultantsEvents.LaunchDialer -> context.launcherDialer(event.phone)
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
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.ConsultantsTitle),
                        style = MaterialTheme.typography.medium18
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
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
            contentPadding = innerPadding + PaddingValues(bottom = 8.dp)
        ) {
            if (state.isLoading && state.employeePojos.isEmpty()) {
                items(
                    count = 4
                ) {
                    ConsultantCard(
                        employeePojo = EmployeePojo.Empty,
                        onActionClick = {},
                        onActiveClick = {},
                        onClick = {}
                    )
                }
            } else {
                items(
                    items = state.employeePojos,
                    key = { employee -> employee.entity.employeeId }
                ) { employee ->
                    ConsultantCard(
                        employeePojo = employee,
                        onActionClick = { actionId -> dispatch(ConsultantsIntent.EmployeeActionClick(employee.entity.employeeId, actionId)) },
                        onActiveClick = { dispatch(ConsultantsIntent.SetActiveEmployee(employee.entity.employeeId)) },
                        onClick = { dispatch(ConsultantsIntent.EmployeeClick(employee.entity.employeeId)) }
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ConsultantsScreenContentPreview(
    @PreviewParameter(ConsultantsModelProvider::class) state: ConsultantsModel
) {
    ConsultantsScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class ConsultantsModelProvider: PreviewParameterProvider<ConsultantsModel> {
    private val employee = EmployeePojo(
        entity = EmployeeEntity(
            employeeId = "1",
            employeeEmail = "anna@example.com",
            employeeMiddleName = "",
            employeeName = "Анна",
            employeePhone = "+79990000000",
            employeeSurname = "Смирнова",
            photoUrl = "https://i.pravatar.cc/144?img=32",
            previewPhotoUrl = "https://i.pravatar.cc/144?img=32",
            lastActivityColorHex = "",
            lastActivityDate = "",
            employeeBotiqueAddress = "Барвиха Luxury Village",
            employeeBotiqueAddressShort = "Барвиха Luxury Village",
            employeeBrand = "MVST",
            isActive = false,
            position = 0,
            basketNumber = 0,
            basketBadge = 0,
            fittingNumber = 0,
            fittingBadge = 0,
            messengerBadge = 0,
            orderBadge = 0,
            compilationBadge = 0
        ),
        badgeEntity = EmployeeBadgeEntity.Empty.copy(employeeId = "1")
    )

    override val values: Sequence<ConsultantsModel> = sequenceOf(
        ConsultantsModel(
            employeePojos = listOf(
                employee,
                employee.copy(
                    entity = employee.entity.copy(
                        employeeId = "2",
                        employeeName = "Екатерина",
                        employeeSurname = "Орлова",
                        employeeBrand = "BORK",
                        employeeBotiqueAddress = "Москва, Петровка, 2",
                        employeeBotiqueAddressShort = "Петровка, 2",
                        isActive = true
                    ),
                    badgeEntity = EmployeeBadgeEntity.Empty.copy(employeeId = "2")
                )
            )
        ),
        ConsultantsModel(
            loadJob = Job()
        )
    )
}
