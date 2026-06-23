@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.consultant

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import ru.mercury.vpclient.features.consultant.event.ConsultantEvent
import ru.mercury.vpclient.features.consultant.intent.ConsultantIntent
import ru.mercury.vpclient.features.consultant.model.ConsultantModel
import ru.mercury.vpclient.features.consultant.navigation.ConsultantRoute
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.ConsultantAvatarPlaceholderStyle
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeBadgeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.EmployeePojo
import ru.mercury.vpclient.shared.domain.mapper.boutiqueAddress
import ru.mercury.vpclient.shared.domain.mapper.isPhotoEmpty
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.consultants.ConsultantActionsRow
import ru.mercury.vpclient.shared.ui.components.consultants.ConsultantAvatarPlaceholder
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.launcherDialer
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium19
import ru.mercury.vpclient.shared.ui.theme.regular16

@Composable
fun ConsultantScreen(
    route: ConsultantRoute,
    viewModel: ConsultantViewModel = hiltViewModel<ConsultantViewModel, ConsultantViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
            is ConsultantEvent.LaunchDialer -> context.launcherDialer(event.phone)
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
    SharedScaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(ConsultantIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
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
            contentPadding = innerPadding + PaddingValues(
                top = if (state.employeePojo.isPhotoEmpty) 8.dp else 0.dp,
                bottom = 16.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                when {
                    !state.employeePojo.isPhotoEmpty -> {
                        ClientAsyncImage(
                            imageUrl = state.employeePojo.entity.previewPhotoUrl,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(408.dp)
                        )
                    }
                    else -> {
                        ConsultantAvatarPlaceholder(
                            name = state.employeePojo.entity.employeeName,
                            style = ConsultantAvatarPlaceholderStyle.Screen
                        )
                    }
                }
            }
            item {
                Text(
                    text = state.employeePojo.entity.employeeName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 32.dp, end = 16.dp),
                    style = MaterialTheme.typography.medium19.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                BrandBox(
                    entity = BrandEntity(
                        brand = state.employeePojo.entity.employeeBrand,
                        urlBrandLogo = null
                    ),
                    modifier = Modifier.padding(vertical = 3.dp)
                )
            }
            item {
                Text(
                    text = state.employeePojo.boutiqueAddress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.regular16.copy(
                        color = MaterialTheme.colorScheme.secondary,
                        lineHeight = 20.sp,
                        letterSpacing = .2.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                ConsultantActionsRow(
                    employeePojo = state.employeePojo,
                    onClick = { actionId -> dispatch(ConsultantIntent.ActionClick(actionId)) },
                    modifier = Modifier.padding(start = 16.dp, top = 32.dp, end = 16.dp)
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ConsultantScreenPreview(
    @PreviewParameter(ConsultantScreenEmployeeEntityProvider::class) state: EmployeePojo
) {
    ConsultantScreenContent(
        state = ConsultantModel(
            employeePojo = state
        ),
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class ConsultantScreenEmployeeEntityProvider: PreviewParameterProvider<EmployeePojo> {
    override val values: Sequence<EmployeePojo> = sequenceOf(
        EmployeePojo(
            entity = EmployeeEntity(
                employeeId = "1",
                employeeEmail = "anna@example.com",
                employeeMiddleName = "",
                employeeName = "Анна",
                employeePhone = "+79990000000",
                employeeSurname = "Смирнова",
                photoUrl = "",
                previewPhotoUrl = "",
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
        ),
        EmployeePojo.Empty
    )
}
