@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart.model.CartEditProductAction
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun CartEditProductSheet(
    actions: List<CartEditProductAction>,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val dismiss: () -> Unit = {
        scope.launch {
            sheetState.hide()
            onDismissRequest()
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        CartEditProductSheetContent(
            actions = actions.map { action ->
                CartEditProductAction(action.text) {
                    scope.launch {
                        sheetState.hide()
                        onDismissRequest()
                        action.onClick()
                    }
                }
            },
            onCancelClick = dismiss
        )
    }
}

@Composable
fun CartEditProductSheetContent(
    actions: List<CartEditProductAction>,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .navigationBarsPadding()
            .padding(bottom = 8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                actions.forEachIndexed { index, action ->
                    if (index > 0) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 1.dp
                        )
                    }

                    CartEditProductSheetItem(
                        text = action.text,
                        onClick = action.onClick
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            CartEditProductSheetItem(
                text = stringResource(ClientStrings.CartEditCancel),
                onClick = onCancelClick
            )
        }
    }
}

@Composable
private fun CartEditProductSheetItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.medium15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartEditProductSheetContentPreview() {
    CartEditProductSheetContent(
        actions = listOf(
            CartEditProductAction(text = "Добавить размер", onClick = {}),
            CartEditProductAction(text = "Изменить размер", onClick = {}),
            CartEditProductAction(text = "Изменить количество", onClick = {}),
            CartEditProductAction(text = "Изменить цвет", onClick = {})
        ),
        onCancelClick = {}
    )
}
