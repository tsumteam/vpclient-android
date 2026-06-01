package ru.mercury.vpclient.features.consultants

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.consultants.event.ConsultantsEvents
import ru.mercury.vpclient.features.consultants.intent.ConsultantsIntent
import ru.mercury.vpclient.features.consultants.model.ConsultantsModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity.Companion.ID_CART
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.consultants.ConsultantCard
import ru.mercury.vpclient.shared.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.shared.ui.components.system.TopBarState
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

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
    SharedScaffold(
        topBar = {
            ClientCenterAlignedTopAppBar(
                state = TopBarState.Title(stringResource(ClientStrings.ConsultantsTitle))
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
            contentPadding = innerPadding
        ) {
            if (state.isLoading && state.employees.isEmpty()) {
                items(
                    count = 3,
                    key = { index -> "consultant_loading_$index" }
                ) {
                    ConsultantCard(
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
                    ConsultantCard(
                        employee = employee,
                        onActionClick = { actionId ->
                            when (actionId) {
                                ID_CART -> dispatch(ConsultantsIntent.CartClick(employee.employeeId))
                            }
                        },
                        onActiveClick = { dispatch(ConsultantsIntent.SetActiveConsultant(employee.employeeId)) },
                        onClick = { dispatch(ConsultantsIntent.ConsultantClick(employee.employeeId)) }
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
    private val employee = EmployeeEntity(
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
        basketBadge = 1,
        fittingNumber = 2,
        fittingBadge = 1,
        messengerBadge = 1,
        orderBadge = 1,
        compilationBadge = 0
    )

    override val values: Sequence<ConsultantsModel> = sequenceOf(
        ConsultantsModel(
            employees = listOf(
                employee,
                employee.copy(
                    employeeId = "2",
                    employeeName = "Екатерина",
                    employeeSurname = "Орлова",
                    employeeBrand = "BORK",
                    employeeBotiqueAddress = "Москва, Петровка, 2",
                    employeeBotiqueAddressShort = "Петровка, 2",
                    isActive = true,
                    basketBadge = 0,
                    fittingNumber = 2,
                    fittingBadge = 2,
                    messengerBadge = 3,
                    orderBadge = 0
                )
            )
        ),
        ConsultantsModel(loadConsultantsJob = Job())
    )
}
