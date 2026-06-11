package ru.mercury.vpclient.features.auth_welcome

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.mercury.vpclient.features.auth_welcome.intent.WelcomeIntent
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.icons.Logo117
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium21
import ru.mercury.vpclient.shared.ui.theme.medium15
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
    SharedScaffold { innerPadding ->
        SharedLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding + PaddingValues(horizontal = 16.dp),
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
                        .padding(top = 106.dp)
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
                        .padding(top = 24.dp)
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
                Button(
                    onClick = { dispatch(WelcomeIntent.RegisterClick) },
                    modifier = Modifier
                        .padding(top = 61.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.WelcomeRegister),
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
                        )
                    )
                }
            }
            item {
                TextButton(
                    onClick = { dispatch(WelcomeIntent.LoginClick) },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.WelcomeLogin),
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
                        )
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreenContent(
        dispatch = {}
    )
}
