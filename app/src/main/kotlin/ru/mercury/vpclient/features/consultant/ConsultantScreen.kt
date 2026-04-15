package ru.mercury.vpclient.features.consultant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.ui.components.consultants.ConsultantActionsRow
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientSnackbarHost
import ru.mercury.vpclient.shared.ui.components.system.ClientTopAppBar
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.EmployeeEntityProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium21
import ru.mercury.vpclient.shared.ui.theme.medium21
import ru.mercury.vpclient.shared.ui.theme.regular16
import ru.mercury.vpclient.shared.ui.theme.secondary5
import ru.mercury.vpclient.features.consultant.event.ConsultantEvent
import ru.mercury.vpclient.features.consultant.intent.ConsultantIntent
import ru.mercury.vpclient.features.consultant.model.ConsultantModel
import ru.mercury.vpclient.features.consultant.navigation.ConsultantRoute

@Composable
fun ConsultantScreen(
    route: ConsultantRoute,
    viewModel: ConsultantViewModel = hiltViewModel<ConsultantViewModel, ConsultantViewModel.Factory>(creationCallback = { it.create(route) })
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    ConsultantScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is ConsultantEvent.SnackbarMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun ConsultantScreenContent(
    state: ConsultantModel,
    dispatch: (ConsultantIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientTopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ConsultantIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
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
                hostState = snackbarHostStateError,
                modifier = Modifier.padding(horizontal = 16.dp),
                containerColor = MaterialTheme.colorScheme.error
            )
        }
    ) { innerPadding ->
        ClientLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val avatarPlaceholder = ColorPainter(MaterialTheme.colorScheme.secondary5)

                    AsyncImage(
                        model = state.employeeEntity.previewPhotoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = avatarPlaceholder,
                        error = avatarPlaceholder,
                        fallback = avatarPlaceholder,
                        modifier = Modifier
                            .size(218.dp)
                            .clip(CircleShape)
                    )
                }
            }
            item {
                Text(
                    text = state.employeeEntity.employeeName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 32.dp, end = 16.dp),
                    style = MaterialTheme.typography.medium21.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                Text(
                    text = state.employeeEntity.employeeBrand,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                    style = MaterialTheme.typography.livretMedium21.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                Text(
                    text = state.employeeEntity.employeeBotiqueAddressShort.ifEmpty { state.employeeEntity.employeeBotiqueAddress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                    style = MaterialTheme.typography.regular16.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                ConsultantActionsRow(
                    entity = state.employeeEntity,
                    onClick = {},
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 32.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun ConsultantScreenPreview(
    @PreviewParameter(EmployeeEntityProvider::class) state: EmployeeEntity
) {
    ClientTheme {
        ConsultantScreenContent(
            state = ConsultantModel(
                employeeEntity = state
            ),
            dispatch = {},
            snackbarHostStateError = remember { SnackbarHostState() }
        )
    }
}
