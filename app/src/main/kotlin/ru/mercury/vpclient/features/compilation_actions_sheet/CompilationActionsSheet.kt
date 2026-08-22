@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.compilation_actions_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.compilation_actions_sheet.intent.CompilationActionsIntent
import ru.mercury.vpclient.shared.ui.components.SharedColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun CompilationActionsSheet(
    dispatch: (CompilationActionsIntent) -> Unit
) {
    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CompilationActionsIntent.DismissClick) },
        containerColor = Color.Transparent
    ) {
        SharedColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Button(
                onClick = { dispatch(CompilationActionsIntent.ShowCompilationChatSheet) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CompilationPreviewDiscussInChat),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Button(
                onClick = { dispatch(CompilationActionsIntent.ShowAddToBasketDialog) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.DetailsAddToBasket),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Button(
                onClick = { dispatch(CompilationActionsIntent.DismissClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CartEditCancel),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CompilationActionsSheetPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CompilationActionsSheet(
            dispatch = {}
        )
    }
}
