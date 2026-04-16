package ru.mercury.vpclient.features.welcome

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.mercury.vpclient.features.welcome.intent.WelcomeIntent
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.components.system.ClientTextButton
import ru.mercury.vpclient.shared.ui.icons.Logo117
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium21
import ru.mercury.vpclient.shared.ui.theme.regular16

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    WelcomeScreenContent(
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun WelcomeScreenContent(
    dispatch: (WelcomeIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        ClientLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Icon(
                    imageVector = Logo117,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 90.dp)
                        .size(width = 117.dp, height = 82.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            item {
                Text(
                    text = stringResource(ClientStrings.WelcomeTitle),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 106.dp, end = 16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.livretMedium21.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                )
            }
            item {
                Text(
                    text = stringResource(ClientStrings.WelcomeDescription),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.regular16.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 20.sp,
                        letterSpacing = .2.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                ClientButton(
                    onClick = { dispatch(WelcomeIntent.RegisterClick) },
                    modifier = Modifier.padding(start = 16.dp, top = 61.dp, end = 16.dp),
                    text = stringResource(ClientStrings.WelcomeRegister)
                )
            }
            item {
                ClientTextButton(
                    onClick = { dispatch(WelcomeIntent.LoginClick) },
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                    text = stringResource(ClientStrings.WelcomeLogin)
                )
            }
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    ClientTheme {
        WelcomeScreenContent(
            dispatch = {}
        )
    }
}
