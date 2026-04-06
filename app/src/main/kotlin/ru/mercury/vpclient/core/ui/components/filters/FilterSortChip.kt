package ru.mercury.vpclient.core.ui.components.filters

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.icons.Sort24
import ru.mercury.vpclient.core.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme

@Composable
fun FilterSortChip(
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
    )
    val iconColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
    )

    Box(
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Sort24,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = iconColor
        )
    }
}

@FontScalePreviews
@Composable
private fun FilterSortChipPreview(
    @PreviewParameter(BooleanParameterProvider::class) selected: Boolean
) {
    ClientTheme {
        FilterSortChip(
            selected = selected,
            enabled = true,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
