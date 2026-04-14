package ru.mercury.vpclient.shared.ui.components.consultants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
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
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.green
import ru.mercury.vpclient.shared.ui.theme.medium14

@Composable
fun ConsultantActiveBadge(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .requiredHeight(31.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.green),
        contentAlignment = Alignment.Center
    ) {
        ConsultantTextStub(
            text = stringResource(ClientStrings.ConsultantsMakeActiveButton),
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.medium14.copy(
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = stringResource(ClientStrings.ConsultantsActiveButton),
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.medium14.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        )
    }
}

@FontScalePreviews
@Composable
private fun ConsultantActiveBadgePreview() {
    ClientTheme {
        ConsultantActiveBadge(
            modifier = Modifier.padding(16.dp)
        )
    }
}
