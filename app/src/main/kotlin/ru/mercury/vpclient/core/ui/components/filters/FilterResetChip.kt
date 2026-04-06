package ru.mercury.vpclient.core.ui.components.filters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium16

@Composable
fun FilterResetChip(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(ClientStrings.CommonReset),
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 8.dp)
            .wrapContentSize(Alignment.Center),
        style = MaterialTheme.typography.medium16.copy(
            color = MaterialTheme.colorScheme.error,
            lineHeight = 20.sp,
            letterSpacing = .2.sp,
            textAlign = TextAlign.Center
        )
    )
}

@FontScalePreviews
@Composable
private fun FilterResetChipPreview() {
    ClientTheme {
        FilterResetChip(
            enabled = true,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
