package ru.mercury.vpclient.shared.ui.components.compilations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import ru.mercury.vpclient.shared.domain.mapper.imageUrl
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14

data class CompilationChatCardState(
    val entity: CompilationEntity,
    val selectedLookTitle: String
)

@Composable
fun CompilationChatCard(
    state: CompilationChatCardState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        ClientAsyncImage(
            imageUrl = state.entity.imageUrl,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(width = 85.dp, height = 130.dp)
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = state.entity.name,
                style = MaterialTheme.typography.medium15.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 15.sp,
                    letterSpacing = .3.sp
                )
            )

            if (state.selectedLookTitle.isNotEmpty()) {
                Text(
                    text = state.selectedLookTitle,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp,
                        letterSpacing = .2.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CompilationChatCardPreview() {
    CompilationChatCard(
        state = CompilationChatCardState(
            entity = CompilationEntity.Empty.copy(
                id = 1,
                photoUrl = "",
                name = "BLV/Hotel"
            ),
            selectedLookTitle = "ОБРАЗ 1"
        )
    )
}
