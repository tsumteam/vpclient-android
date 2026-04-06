package ru.mercury.vpclient.core.ui.components.details

import android.content.ClipData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.entity.DetailsField
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.core.ui.icons.Copy24
import ru.mercury.vpclient.core.ui.preview.InfoRowProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium14
import ru.mercury.vpclient.core.ui.theme.regular14
import ru.mercury.vpclient.core.ui.theme.surface4
import ru.mercury.vpclient.features.details.model.DetailsModel

@Composable
fun DetailsFieldRow(
    field: DetailsField,
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(field.titleRes),
            modifier = Modifier.weight(2F),
            style = MaterialTheme.typography.medium14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp,
                textAlign = TextAlign.Start
            )
        )

        Row(
            modifier = Modifier.weight(3F),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = field.value,
                modifier = Modifier
                    .padding(end = if (field.showCopyIcon) 0.dp else 16.dp)
                    .weight(1F),
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Start
                )
            )

            if (field.showCopyIcon) {
                IconButton(
                    onClick = {
                        scope.launch {
                            clipboard.setClipEntry(ClipEntry(ClipData.newPlainText(null, field.value)))
                        }
                    }
                ) {
                    Icon(
                        imageVector = Copy24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun DetailsFieldRowPreview(
    @PreviewParameter(InfoRowProvider::class) productEntity: ProductEntity
) {
    val state = DetailsModel(
        productEntity = productEntity
    )

    ClientTheme {
        LazyColumn {
            state.detailFields.forEachIndexed { index, field ->
                item {
                    DetailsFieldRow(
                        field = field,
                        modifier = Modifier.padding(start = 16.dp, top = 24.dp)
                    )
                }
                if (index != state.detailFields.lastIndex) {
                    item {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.surface4
                        )
                    }
                }
            }
        }
    }
}
