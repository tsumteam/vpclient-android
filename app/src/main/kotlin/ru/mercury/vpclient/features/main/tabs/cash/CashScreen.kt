package ru.mercury.vpclient.features.main.tabs.cash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.core.entity.DriverEntity
import ru.mercury.vpclient.core.ui.components.VPClientLazyColumn
import ru.mercury.vpclient.core.ui.preview.DriverEntityProvider
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography
import ru.mercury.vpclient.features.main.tabs.cash.intent.CashIntent
import ru.mercury.vpclient.features.main.tabs.cash.model.CashModel

@Composable
fun CashScreen(
    viewModel: CashViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    CashScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun CashScreenContent(
    state: CashModel,
    dispatch: (CashIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.surface),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier.height(46.dp)
                )

                Text(
                    text = state.driverEntity.driverName,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 40.dp),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                    style = VPClientTypography.Regular_16_OnBackground
                )
            }
        },
        floatingActionButton = {},
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        VPClientLazyColumn(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {}
    }
}

@Preview
@Preview(fontScale = 1.5F)
@Composable
private fun CashScreenContentPreview(
    @PreviewParameter(DriverEntityProvider::class) driverEntity: DriverEntity
) {
    VPClientTheme {
        CashScreenContent(
            state = CashModel(
                driverEntity = driverEntity
            ),
            dispatch = {}
        )
    }
}
