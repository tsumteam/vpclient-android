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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.mercury.vpclient.core.ui.components.ClientButton
import ru.mercury.vpclient.core.ui.components.ClientColumn
import ru.mercury.vpclient.core.ui.components.ClientTextButton
import ru.mercury.vpclient.core.ui.theme.ClientIcons
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.ClientTypography
import ru.mercury.vpclient.features.welcome.intent.WelcomeIntent

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
        ClientColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(ClientIcons.Logo117),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 90.dp)
                    .size(width = 117.dp, height = 82.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(ClientStrings.WelcomeTitle),
                modifier = Modifier
                    .padding(start = 16.dp, top = 106.dp, end = 16.dp)
                    .fillMaxWidth(),
                style = ClientTypography.Medium_21_OnBackground.copy(lineHeight = 21.sp, letterSpacing = .2.sp, textAlign = TextAlign.Center)
            )

            Text(
                text = stringResource(ClientStrings.WelcomeDescription),
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp)
                    .fillMaxWidth(),
                style = ClientTypography.Regular_16_OnBackground.copy(lineHeight = 20.sp, letterSpacing = .2.sp, textAlign = TextAlign.Center)
            )

            ClientButton(
                onClick = { dispatch(WelcomeIntent.RegisterClick) },
                modifier = Modifier
                    .padding(start = 16.dp, top = 61.dp, end = 16.dp),
                text = stringResource(ClientStrings.WelcomeRegister)
            )

            ClientTextButton(
                onClick = { dispatch(WelcomeIntent.LoginClick) },
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                text = stringResource(ClientStrings.WelcomeLogin)
            )
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
