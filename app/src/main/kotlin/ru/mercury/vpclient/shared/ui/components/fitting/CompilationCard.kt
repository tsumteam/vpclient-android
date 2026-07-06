package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.ChevronEnd24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CompilationCard(
    compilation: CompilationEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 144.dp)
            .clickable(onClick = onClick)
            .padding(start = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(end = if (compilation.hasBadge) 56.dp else 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClientAsyncImage(
                imageUrl = compilation.imageUrl,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(width = 75.dp, height = 112.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1F)
                    .height(112.dp)
                    .padding(start = 16.dp, top = 2.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = compilation.name,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .3.sp
                    )
                )

                Text(
                    text = compilation.date,
                    modifier = Modifier.padding(top = 4.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )

                Text(
                    text = compilation.infoString,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 14.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (compilation.hasBadge) {
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                )
            }

            Icon(
                imageVector = ChevronEnd24,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CompilationCardPreview(
    @PreviewParameter(CompilationEntityProvider::class) compilation: CompilationEntity
) {
    CompilationCard(
        compilation = compilation,
        onClick = {}
    )
}

private class CompilationEntityProvider: PreviewParameterProvider<CompilationEntity> {
    override val values: Sequence<CompilationEntity> = sequenceOf(
        CompilationEntity(
            id = 1,
            position = 0,
            collageUrl = "",
            photoUrl = "",
            name = "Подборка от стилиста",
            description = "",
            createDate = "2026-07-11",
            looksQty = 5,
            lookProductsQty = 36,
            badge = 1,
            isStatsAvailable = false
        ),
        CompilationEntity(
            id = 2,
            position = 1,
            collageUrl = "",
            photoUrl = "",
            name = "Подборка для деловой поездки и вечерних мероприятий",
            description = "",
            createDate = "2026-07-10",
            looksQty = 3,
            lookProductsQty = 17,
            badge = 1,
            isStatsAvailable = false
        ),
        CompilationEntity(
            id = 3,
            position = 2,
            collageUrl = "",
            photoUrl = "",
            name = "Повседневные образы",
            description = "",
            createDate = "2026-07-09",
            looksQty = 1,
            lookProductsQty = 4,
            badge = 0,
            isStatsAvailable = false
        )
    )
}
