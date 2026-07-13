package ru.mercury.vpclient.shared.ui.components.brands

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15

data class BrandSectionHeaderState(
    val title: String,
    val showSelectAll: Boolean,
    val onSelectAll: () -> Unit
)

@Composable
fun BrandSectionHeader(
    state: BrandSectionHeaderState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(start = 16.dp, end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.title,
            style = MaterialTheme.typography.livretMedium18.copy(
                color = MaterialTheme.colorScheme.error,
                lineHeight = 26.sp,
                letterSpacing = .2.sp
            )
        )

        SharedAnimatedVisibility(
            visible = state.showSelectAll,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            TextButton(
                onClick = state.onSelectAll,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(ClientStrings.FilterBrandSelectAll),
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.error,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun BrandSectionHeaderPreview(
    @PreviewParameter(BrandSectionHeaderStateProvider::class) state: BrandSectionHeaderState
) {
    BrandSectionHeader(
        state = state
    )
}

private class BrandSectionHeaderStateProvider: PreviewParameterProvider<BrandSectionHeaderState> {
    override val values: Sequence<BrandSectionHeaderState> = sequenceOf(
        BrandSectionHeaderState(
            title = "ЛЮБИМЫЕ БРЕНДЫ",
            showSelectAll = false,
            onSelectAll = {}
        ),
        BrandSectionHeaderState(
            title = "ТОП-БРЕНДЫ",
            showSelectAll = true,
            onSelectAll = {}
        ),
        BrandSectionHeaderState(
            title = "ВСЕ БРЕНДЫ",
            showSelectAll = false,
            onSelectAll = {}
        )
    )
}
