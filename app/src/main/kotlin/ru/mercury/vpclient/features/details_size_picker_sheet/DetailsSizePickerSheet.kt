@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.details_size_picker_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart_size_picker_sheet.SizeSelectorState
import ru.mercury.vpclient.features.details_size_picker_sheet.intent.DetailsSizePickerSheetIntent
import ru.mercury.vpclient.features.details_size_picker_sheet.model.DetailsSizePickerSheetModel
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.details.DetailsSizeSelector
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun DetailsSizePickerSheet(
    state: DetailsSizePickerSheetModel,
    dispatch: (DetailsSizePickerSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val sheetDispatch: (DetailsSizePickerSheetIntent) -> Unit = { intent ->
        when (intent) {
            is DetailsSizePickerSheetIntent.AddToBasketClick,
            is DetailsSizePickerSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is DetailsSizePickerSheetIntent.SizeClick,
            is DetailsSizePickerSheetIntent.SizeTableClick -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(DetailsSizePickerSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        DetailsSizePickerSheetContent(
            state = state,
            dispatch = sheetDispatch
        )
    }
}

@Composable
private fun DetailsSizePickerSheetContent(
    state: DetailsSizePickerSheetModel,
    dispatch: (DetailsSizePickerSheetIntent) -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        ) {
            IconButton(
                onClick = { dispatch(DetailsSizePickerSheetIntent.DismissRequest) },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Close24,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = stringResource(ClientStrings.DetailsSizeSelectCaps),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 56.dp),
                style = MaterialTheme.typography.livretMedium19.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }

        DetailsSizeSelector(
            state = state.sizeSelectorState,
            onSizeClick = { index -> dispatch(DetailsSizePickerSheetIntent.SizeClick(index)) },
            onSizeTableClick = { dispatch(DetailsSizePickerSheetIntent.SizeTableClick) },
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = { dispatch(DetailsSizePickerSheetIntent.AddToBasketClick) },
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

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun DetailsSizePickerSheetContentPreview(
    @PreviewParameter(DetailsSizePickerSheetSizeSelectorStateProvider::class) state: SizeSelectorState
) {
    DetailsSizePickerSheetContent(
        state = DetailsSizePickerSheetModel(sizeSelectorState = state),
        dispatch = {}
    )
}

private class DetailsSizePickerSheetSizeSelectorStateProvider: PreviewParameterProvider<SizeSelectorState> {

    private val sizes = listOf(
        SizeState(topText = "RU 36", bottomText = "IT 34", selected = false, enabled = true),
        SizeState(topText = "RU 38", bottomText = "IT 36", selected = false, enabled = true),
        SizeState(topText = "RU 40", bottomText = "IT 38", selected = false, enabled = true),
        SizeState(topText = "RU 42", bottomText = "IT 40", selected = false, enabled = false),
        SizeState(topText = "RU 44", bottomText = "IT 42", selected = false, enabled = true),
        SizeState(topText = "RU 46", bottomText = "IT 44", selected = false, enabled = false)
    )

    override val values: Sequence<SizeSelectorState> = sequenceOf(
        SizeSelectorState.Empty,
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
