package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.FilterTitleEntity
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.FilterTitleEntityProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.secondary6
import ru.mercury.vpclient.shared.ui.theme.surface4

@Composable
fun FilterScreenTitle(
    entity: FilterTitleEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isTitlePlaceholderVisible = entity.titleCatalogCategoryEntity.isEmpty
    val isSubtitlePlaceholderVisible = entity.subtitleCatalogCategoryEntity.isEmpty

    Column(
        modifier = modifier
            .clickableWithoutRipple(onClick)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = entity.titleCatalogCategoryEntity.name,
            modifier = Modifier
                .then(
                    when {
                        isTitlePlaceholderVisible -> Modifier.width(132.dp)
                        else -> Modifier.fillMaxWidth()
                    }
                )
                .height(20.dp)
                .placeholder(
                    visible = isTitlePlaceholderVisible,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surface4,
                    shape = RoundedCornerShape(4.dp)
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.medium15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = .3.sp,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = entity.subtitleCatalogCategoryEntity.name,
            modifier = Modifier
                .then(
                    when {
                        isSubtitlePlaceholderVisible -> Modifier.width(88.dp)
                        else -> Modifier.fillMaxWidth()
                    }
                )
                .height(19.dp)
                .placeholder(
                    visible = isSubtitlePlaceholderVisible,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surface4,
                    shape = RoundedCornerShape(4.dp)
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.secondary6,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@FontScalePreviews
@Composable
private fun FilterScreenTitlePreview(
    @PreviewParameter(FilterTitleEntityProvider::class) entity: FilterTitleEntity
) {
    ClientTheme {
        FilterScreenTitle(
            entity = entity,
            onClick = {}
        )
    }
}
