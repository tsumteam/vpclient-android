package ru.mercury.vpclient.features.fitting_success

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ru.mercury.vpclient.features.fitting_success.intent.FittingSuccessIntent
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessDeliveryLine
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessRoute
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.fitting.FittingSuccessDeliveryText
import ru.mercury.vpclient.shared.ui.icons.Logo117
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun FittingSuccessScreen(
    route: FittingSuccessRoute,
    viewModel: FittingSuccessViewModel = hiltViewModel()
) {
    FittingSuccessScreenContent(
        route = route,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun FittingSuccessScreenContent(
    route: FittingSuccessRoute,
    dispatch: (FittingSuccessIntent) -> Unit
) {
    SharedScaffold(
        floatingActionButton = {
            Button(
                onClick = { dispatch(FittingSuccessIntent.CatalogClick) },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.FittingSuccessCatalog),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Logo117,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 84.dp)
                    .size(width = 114.dp, height = 79.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(ClientStrings.FittingSuccessThanks),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 72.dp, end = 16.dp),
                style = MaterialTheme.typography.medium18.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )

            FittingSuccessDeliveryText(
                deliveryLines = route.deliveryLines,
                address = route.address,
                modifier = Modifier.padding(start = 16.dp, top = 24.dp, end = 16.dp)
            )

            Text(
                text = stringResource(ClientStrings.FittingSuccessInfo),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 19.sp,
                    textAlign = TextAlign.Center,
                    letterSpacing = .2.sp
                )
            )

            Button(
                onClick = { dispatch(FittingSuccessIntent.FittingClick) },
                modifier = Modifier
                    .padding(top = 46.dp)
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.FittingSuccessFitting),
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        letterSpacing = .2.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FittingSuccessScreenContentPreview() {
    FittingSuccessScreenContent(
        route = FittingSuccessRoute(
            deliveryLines = listOf(
                FittingSuccessDeliveryLine(
                    intervalSummary = "13 мая c 10:00 до 12:00",
                    productsCount = 2
                )
            ),
            address = "Brioni, Третьяковский проезд."
        ),
        dispatch = {}
    )
}
