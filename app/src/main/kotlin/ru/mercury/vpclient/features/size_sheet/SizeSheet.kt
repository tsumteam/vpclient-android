@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.size_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.size_sheet.intent.SizeSheetIntent
import ru.mercury.vpclient.features.size_sheet.model.SizeSheetModel
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.details.DetailsSizeSelector
import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun SizeSheet(
    state: SizeSheetModel,
    dispatch: (SizeSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (SizeSheetIntent) -> Unit = { intent ->
        when (intent) {
            is SizeSheetIntent.AddToBasketClick,
            is SizeSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is SizeSheetIntent.SizeClick,
            is SizeSheetIntent.SizeTableClick -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(SizeSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.DetailsSizeSelectCaps),
                        style = MaterialTheme.typography.livretMedium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 26.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(SizeSheetIntent.DismissRequest) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

            DetailsSizeSelector(
                state = state.sizeSelectorState,
                onSizeClick = { index -> sheetDispatch(SizeSheetIntent.SizeClick(index)) },
                onSizeTableClick = { sheetDispatch(SizeSheetIntent.SizeTableClick) },
                modifier = Modifier.padding(top = 8.dp)
            )

            Button(
                onClick = { sheetDispatch(SizeSheetIntent.AddToBasketClick) },
                modifier = Modifier
                    .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
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
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun SizeSheetPreview(
    @PreviewParameter(SizeSheetSizeSelectorStateProvider::class) state: SizeSelectorState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SizeSheet(
            state = SizeSheetModel(sizeSelectorState = state),
            dispatch = {}
        )
    }
}

private class SizeSheetSizeSelectorStateProvider: PreviewParameterProvider<SizeSelectorState> {

    private val sizes = listOf(
        SizeState(topText = "RU 36", bottomText = "IT 34", selected = false, enabled = true),
        SizeState(topText = "RU 38", bottomText = "IT 36", selected = false, enabled = true),
        SizeState(topText = "RU 40", bottomText = "IT 38", selected = false, enabled = true),
        SizeState(topText = "RU 42", bottomText = "IT 40", selected = false, enabled = false),
        SizeState(topText = "RU 44", bottomText = "IT 42", selected = false, enabled = true),
        SizeState(topText = "RU 46", bottomText = "IT 44", selected = false, enabled = false)
    )

    override val values: Sequence<SizeSelectorState> = sequenceOf(
        SizeSelectorState(
            sizes = sizes,
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        ),
        SizeSelectorState(
            sizes = sizes.mapIndexed { index, state -> state.copy(selected = index == 1) },
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        )
    )
}
