@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.push_notifications_sheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.R
import ru.mercury.vpclient.features.push_notifications_sheet.intent.PushNotificationsSheetIntent
import ru.mercury.vpclient.features.push_notifications_sheet.model.PushNotificationsSheetModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun PushNotificationsSheet(
    state: PushNotificationsSheetModel,
    dispatch: (PushNotificationsSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val dismiss: () -> Unit = {
        scope.launch {
            sheetState.hide()
            dispatch(PushNotificationsSheetIntent.DismissClick)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = dismiss,
        sheetState = sheetState
    ) {
        SharedLazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 22.dp,
                end = 16.dp,
                bottom = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = false
        ) {
            item {
                Text(
                    text = stringResource(ClientStrings.PushNotificationsSheetTitle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.livretMedium18.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                Text(
                    text = stringResource(ClientStrings.PushNotificationsSheetDescription),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 20.sp,
                        letterSpacing = .2.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
            item {
                Image(
                    painter = painterResource(R.drawable.push_notifications_sheet),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(406F / 336F),
                    contentScale = ContentScale.Fit
                )
            }
            item {
                Button(
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                            dispatch(PushNotificationsSheetIntent.EnableClick)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = state.isEnableButtonEnabled,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.PushNotificationsSheetEnable),
                        style = MaterialTheme.typography.medium15.copy(
                            letterSpacing = .3.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            item {
                TextButton(
                    onClick = dismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(ClientStrings.PushNotificationsSheetNotNow),
                        style = MaterialTheme.typography.medium15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = .3.sp,
                            textAlign = TextAlign.Center
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
private fun PushNotificationsSheetPreview(
    @PreviewParameter(PushNotificationsSheetModelPreviewParameterProvider::class) state: PushNotificationsSheetModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PushNotificationsSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class PushNotificationsSheetModelPreviewParameterProvider:
    PreviewParameterProvider<PushNotificationsSheetModel> {

    override val values: Sequence<PushNotificationsSheetModel> = sequenceOf(
        PushNotificationsSheetModel()
    )
}
