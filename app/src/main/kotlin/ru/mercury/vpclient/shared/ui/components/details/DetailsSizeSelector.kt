package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.shared.ui.components.product.ProductSizeButtonsRow
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular12

data class SizeSelectorState(
    val sizes: List<SizeState>,
    val topText: String,
    val bottomText: String,
    val isSizeTableVisible: Boolean,
    val isSizeSelectTextVisible: Boolean = true,
    val onSizeClick: (Int) -> Unit = {},
    val onSizeTableClick: () -> Unit = {}
) {
    val selectedSize: SizeState?
        get() = sizes.firstOrNull { it.selected }

    companion object {
        val Empty = SizeSelectorState(
            sizes = emptyList(),
            topText = "",
            bottomText = "",
            isSizeTableVisible = false
        )
    }
}

@Composable
fun DetailsSizeSelector(
    state: SizeSelectorState,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (titleRef, tableButtonRef, sizeButtonsRowRef, statusRef) = createRefs()

        Text(
            text = stringResource(ClientStrings.DetailsSizeTitle),
            modifier = Modifier.constrainAs(titleRef) {
                start.linkTo(parent.start, 16.dp)
                top.linkTo(parent.top, 16.dp)
            },
            style = MaterialTheme.typography.medium14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp
            )
        )

        if (state.isSizeTableVisible) {
            TextButton(
                onClick = state.onSizeTableClick,
                modifier = Modifier.constrainAs(tableButtonRef) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, 8.dp)
                },
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(ClientStrings.DetailsSizeTable),
                    style = MaterialTheme.typography.medium14.copy(
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 16.sp
                    )
                )
            }
        }

        ProductSizeButtonsRow(
            state = state,
            modifier = Modifier.constrainAs(sizeButtonsRowRef) {
                width = Dimension.fillToConstraints
                start.linkTo(parent.start)
                top.linkTo(titleRef.bottom, 16.dp)
                end.linkTo(parent.end)
            }
        )

        Text(
            text = when {
                state.selectedSize == null && state.isSizeSelectTextVisible -> {
                    stringResource(ClientStrings.DetailsSizeSelect)
                }
                state.selectedSize == null -> ""
                state.selectedSize?.enabled == true -> {
                    stringResource(ClientStrings.DetailsSizeInStock)
                }
                else -> stringResource(ClientStrings.DetailsSizeSold)
            },
            modifier = Modifier.constrainAs(statusRef) {
                centerHorizontallyTo(parent)
                top.linkTo(sizeButtonsRowRef.bottom, 16.dp)
            },
            style = MaterialTheme.typography.regular12.copy(
                color = when {
                    state.selectedSize == null -> MaterialTheme.colorScheme.outline
                    else -> MaterialTheme.colorScheme.error
                },
                lineHeight = 16.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun DetailsSizeSelectorPreview(
    @PreviewParameter(DetailsSizeSelectorSizeSelectorStateProvider::class) state: SizeSelectorState
) {
    DetailsSizeSelector(
        state = state
    )
}

private class DetailsSizeSelectorSizeSelectorStateProvider: PreviewParameterProvider<SizeSelectorState> {

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
        ),
        SizeSelectorState(
            sizes = sizes.mapIndexed { index, state -> state.copy(selected = index == 3) },
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        )
    )
}
