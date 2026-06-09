@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_my_data

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.profile_delete_dialog.ProfileDeleteDialog
import ru.mercury.vpclient.features.profile_delete_dialog.intent.ProfileDeleteDialogIntent
import ru.mercury.vpclient.features.profile_my_data.intent.MyDataIntent
import ru.mercury.vpclient.features.profile_my_data.model.MyDataModel
import ru.mercury.vpclient.shared.ui.components.InfoItem
import ru.mercury.vpclient.shared.ui.components.InfoItemState
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretRegular15
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun MyDataScreen(
    viewModel: MyDataViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    MyDataScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )

    if (state.isDeleteProfileDialogVisible) {
        ProfileDeleteDialog(
            dispatch = { intent ->
                when (intent) {
                    is ProfileDeleteDialogIntent.ConfirmRequest -> {
                        viewModel.dispatch(MyDataIntent.DeleteProfile)
                    }
                    is ProfileDeleteDialogIntent.DismissRequest -> {
                        viewModel.dispatch(MyDataIntent.DismissDeleteProfileDialog)
                    }
                }
            }
        )
    }
}

@Composable
private fun MyDataScreenContent(
    state: MyDataModel,
    dispatch: (MyDataIntent) -> Unit
) {
    SharedScaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.MyDataTitle),
                        style = MaterialTheme.typography.medium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(MyDataIntent.BackClick) }
                    ) {
                        Icon(
                            imageVector = ChevronStart24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    if (state.showFittingButton) {
                        FittingIconButton(
                            text = state.fittingText,
                            showBadge = state.showFittingBadge,
                            onClick = { dispatch(MyDataIntent.FittingClick) }
                        )
                    }

                    CartIconButton(
                        text = state.cartText,
                        showBadge = state.showCartBadge,
                        onClick = { dispatch(MyDataIntent.CartClick) }
                    )

                    MessengerIconButton(
                        showBadge = state.showMessengerBadge,
                        onClick = { dispatch(MyDataIntent.MessengerClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            OutlinedButton(
                onClick = { dispatch(MyDataIntent.ShowDeleteProfileDialog) },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = !state.isDeleteProfileLoading,
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = MaterialTheme.colorScheme.background,
                    disabledContentColor = MaterialTheme.colorScheme.onBackground
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                when {
                    state.isDeleteProfileLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onBackground,
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Text(
                            text = stringResource(ClientStrings.MyDataDeleteProfile),
                            style = MaterialTheme.typography.livretRegular15.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(bottom = 92.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.MyDataSurname),
                        value = state.surname.ifEmpty { stringResource(ClientStrings.MyDataNotSpecified) }
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.MyDataName),
                        value = state.name.ifEmpty { stringResource(ClientStrings.MyDataNotSpecified) }
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.MyDataPhone),
                        value = state.phone.ifEmpty { stringResource(ClientStrings.MyDataNotSpecified) }
                    )
                )
            }
            item {
                InfoItem(
                    state = InfoItemState(
                        label = stringResource(ClientStrings.MyDataEmail),
                        value = state.email.ifEmpty { stringResource(ClientStrings.MyDataNotSpecified) }
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun MyDataScreenContentPreview() {
    MyDataScreenContent(
        state = MyDataModel(
            surname = "Иванов",
            name = "Иван",
            phone = "+7 999 123-45-67",
            email = "ivan@example.com"
        ),
        dispatch = {}
    )
}
