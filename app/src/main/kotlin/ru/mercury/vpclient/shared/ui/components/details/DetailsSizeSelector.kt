package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ru.mercury.vpclient.features.cart_size_picker_sheet.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun DetailsSizeSelector(
    state: SizeSelectorState,
    onSizeClick: (Int) -> Unit,
    onSizeTableClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedSize = state.sizes.firstOrNull { it.selected }

    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (titleRef, tableButtonRef, textsRef, sizeListRef, statusRef) = createRefs()

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
                onClick = onSizeTableClick,
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

        Column(
            modifier = Modifier.constrainAs(textsRef) {
                width = Dimension.value(50.dp)
                height = Dimension.value(58.dp)
                start.linkTo(parent.start, 16.dp)
                top.linkTo(sizeListRef.top)
                bottom.linkTo(sizeListRef.bottom)
            },
            verticalArrangement = Arrangement.spacedBy(
                space = 6.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Text(
                text = state.topText.ifEmpty { selectedSize?.topText.orEmpty() },
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = state.bottomText.ifEmpty { selectedSize?.bottomText.orEmpty() },
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = .2.sp
                )
            )
        }

        LazyRow(
            modifier = Modifier.constrainAs(sizeListRef) {
                width = Dimension.fillToConstraints
                start.linkTo(textsRef.end, 8.dp)
                top.linkTo(titleRef.bottom, 16.dp)
                end.linkTo(parent.end)
            },
            contentPadding = PaddingValues(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(state.sizes) { index, sizeState ->
                DetailsSizeButton(
                    state = sizeState,
                    onClick = { onSizeClick(index) }
                )
            }
        }

        Text(
            text = when {
                selectedSize == null -> stringResource(ClientStrings.DetailsSizeSelect)
                selectedSize.enabled -> stringResource(ClientStrings.DetailsSizeInStock)
                else -> stringResource(ClientStrings.DetailsSizeSold)
            },
            modifier = Modifier.constrainAs(statusRef) {
                centerHorizontallyTo(parent)
                top.linkTo(sizeListRef.bottom, 16.dp)
            },
            style = MaterialTheme.typography.regular12.copy(
                color = when {
                    selectedSize == null -> MaterialTheme.colorScheme.outline
                    else -> MaterialTheme.colorScheme.error
                },
                lineHeight = 16.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun DetailsSizeSelectorPreview(
    @PreviewParameter(DetailsSizeSelectorSizeSelectorStateProvider::class) state: SizeSelectorState
) {
    DetailsSizeSelector(
        state = state,
        onSizeClick = {},
        onSizeTableClick = {}
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
