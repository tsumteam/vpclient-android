package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.mercury.vpclient.shared.ui.preview.BooleanParameterProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretRegular11

@Composable
fun FilterColorBox(
    text: String,
    imageUrl: String?,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderSize by animateDpAsState(
        targetValue = if (selected) 1.dp else 0.dp,
        label = "color_box_border_size"
    )
    val cornerSize by animateDpAsState(
        targetValue = if (selected) 14.dp else 12.dp,
        label = "color_box_corner_size"
    )
    val imageSize by animateDpAsState(
        targetValue = if (selected) 63.dp else 69.dp,
        label = "color_box_image_size"
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(69.dp)
                .border(
                    width = borderSize,
                    color = if (selected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(cornerSize)
                )
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = text,
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.livretRegular11.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )
    }
}

@FontScalePreviews
@Composable
private fun FilterColorBoxPreview(
    @PreviewParameter(BooleanParameterProvider::class) selected: Boolean
) {
    ClientTheme {
        FilterColorBox(
            text = "Бордовый",
            imageUrl = "",
            selected = selected,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
