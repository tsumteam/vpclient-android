package ru.mercury.vpclient.shared.ui.components.brands

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandFilterValue
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular15

data class BrandChipState(
    val brand: BrandFilterValue,
    val onClick: () -> Unit
)

@Composable
fun BrandChip(
    state: BrandChipState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = state.onClick),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.brand.labelPhotoUrl != null -> {
                ClientAsyncImage(
                    imageUrl = state.brand.labelPhotoUrl,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    contentScale = ContentScale.Fit
                )
            }
            else -> {
                Text(
                    text = state.brand.label,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.regular15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .2.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun BrandChipPreview(
    @PreviewParameter(BrandChipStateProvider::class) state: BrandChipState
) {
    BrandChip(
        state = state
    )
}

private class BrandChipStateProvider: PreviewParameterProvider<BrandChipState> {
    override val values: Sequence<BrandChipState> = sequenceOf(
        BrandChipState(
            brand = BrandFilterValue(
                id = "mercury",
                label = "Mercury",
                labelPhotoUrl = null,
                isFavorite = true,
                isTopBrand = true
            ),
            onClick = {}
        ),
        BrandChipState(
            brand = BrandFilterValue(
                id = "loewe",
                label = "LOEWE",
                labelPhotoUrl = "https://example.com/brand-logo.png",
                isFavorite = true,
                isTopBrand = true
            ),
            onClick = {}
        )
    )
}
