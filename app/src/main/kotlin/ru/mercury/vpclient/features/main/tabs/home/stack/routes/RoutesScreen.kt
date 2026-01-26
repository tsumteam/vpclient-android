package ru.mercury.vpclient.features.main.tabs.home.stack.routes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Job
import ru.mercury.vpclient.core.ui.components.VPClientTopAppBar
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.intent.RoutesIntent
import ru.mercury.vpclient.features.main.tabs.home.stack.routes.model.RoutesModel

@Composable
fun RoutesScreen(
    viewModel: RoutesViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val pullToRefreshState = rememberPullToRefreshState()

    RoutesScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        pullToRefreshState = pullToRefreshState
    )
}

@Composable
private fun RoutesScreenContent(
    state: RoutesModel,
    dispatch: (RoutesIntent) -> Unit,
    pullToRefreshState: PullToRefreshState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            VPClientTopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(VPClientIcons.LogoVipVPClient),
                            modifier = Modifier.size(54.dp, 49.dp),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { dispatch(RoutesIntent.PullToRefresh) },
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize(),
            state = pullToRefreshState
        ) {}
    }
}


/**
 * Нет рейсов.
 */
@Preview
@Composable
private fun RoutesScreenContentPreview() {
    VPClientTheme {
        RoutesScreenContent(
            state = RoutesModel(),
            dispatch = {},
            pullToRefreshState = rememberPullToRefreshState()
        )
    }
}

/**
 * Загрузка рейсов.
 */
@Preview
@Composable
private fun RoutesScreenContentPreview2() {
    VPClientTheme {
        RoutesScreenContent(
            state = RoutesModel(
                routesJob = Job()
            ),
            dispatch = {},
            pullToRefreshState = rememberPullToRefreshState()
        )
    }
}
