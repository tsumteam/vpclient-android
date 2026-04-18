package ru.mercury.vpclient.shared.ui.components.filters

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.components.system.ClientAnimatedVisibility
import ru.mercury.vpclient.shared.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun FilterBrandSectionHeader(
    title: String,
    showSelectAll: Boolean,
    onSelectAll: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(start = 16.dp, end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.livretMedium18.copy(
                color = MaterialTheme.colorScheme.error,
                lineHeight = 26.sp,
                letterSpacing = .2.sp
            )
        )

        ClientAnimatedVisibility(
            visible = showSelectAll,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            TextButton(
                onClick = onSelectAll,
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

@FontScalePreviews
@Composable
private fun FilterBrandSectionHeaderWithSelectAllPreview(
    @PreviewParameter(BooleanParameterProvider::class) showSelectAll: Boolean
) {
    ClientTheme {
        FilterBrandSectionHeader(
            title = "ТОП-БРЕНДЫ",
            showSelectAll = showSelectAll,
            onSelectAll = {}
        )
    }
}
