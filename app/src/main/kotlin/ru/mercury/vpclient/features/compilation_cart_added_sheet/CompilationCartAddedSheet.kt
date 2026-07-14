@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.compilation_cart_added_sheet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.compilation_cart_added_sheet.intent.CompilationCartAddedSheetIntent
import ru.mercury.vpclient.features.compilation_cart_added_sheet.model.CompilationCartAddedSheetModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.compilations.CompilationCartAddedCard
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun CompilationCartAddedSheet(
    state: CompilationCartAddedSheetModel,
    dispatch: (CompilationCartAddedSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CompilationCartAddedSheetIntent) -> Unit = { intent ->
        scope.launch {
            sheetState.hide()
            dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CompilationCartAddedSheetIntent.DismissRequest) },
        sheetState = sheetState,
        containerColor = Color.Transparent
    ) {
        SharedLazyColumn(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.CompilationCartAddedSheetTitle),
                            style = MaterialTheme.typography.livretMedium18
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            item {
                CompilationCartAddedCard(
                    entity = state.pageEntity
                )
            }
            item {
                OutlinedButton(
                    onClick = { sheetDispatch(CompilationCartAddedSheetIntent.ReturnToCompilationClick) },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.CompilationCartAddedSheetReturnToCompilation),
                        style = MaterialTheme.typography.medium15.copy(
                            textAlign = TextAlign.Center,
                            letterSpacing = .3.sp
                        )
                    )
                }
            }
            item {
                Button(
                    onClick = { sheetDispatch(CompilationCartAddedSheetIntent.CartClick) },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.CompilationCartAddedSheetCart),
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
@Preview(showBackground = true)
@Composable
private fun CompilationCartAddedSheetPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CompilationCartAddedSheet(
            state = CompilationCartAddedSheetModel(
                pageEntity = CompilationPreviewPageEntity.Empty.copy(
                    compilationId = 103,
                    id = 1,
                    position = 0,
                    compilationName = "Летние образы",
                    title = "Образ 1",
                    imageUrl = ""
                )
            ),
            dispatch = {}
        )
    }
}
