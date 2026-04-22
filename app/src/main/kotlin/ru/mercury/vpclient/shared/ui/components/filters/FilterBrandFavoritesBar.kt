package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.icons.Favorited40
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

@Composable
fun FilterBrandFavoritesBar(
    isFavorited: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Favorited40,
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clickableWithoutRipple(onClick = onClick),
            tint = Color.Unspecified
        )
    }
}

@FontScalePreviews
@Composable
private fun FilterBrandFavoritesBarPreview(
    @PreviewParameter(BooleanParameterProvider::class) isFavorited: Boolean
) {
    ClientTheme {
        FilterBrandFavoritesBar(
            isFavorited = isFavorited,
            onClick = {}
        )
    }
}
