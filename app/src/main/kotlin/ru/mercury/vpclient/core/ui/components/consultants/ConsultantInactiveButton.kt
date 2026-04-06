package ru.mercury.vpclient.core.ui.components.consultants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium14

@Composable
fun ConsultantInactiveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.requiredHeight(31.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onBackground
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Text(
            text = stringResource(ClientStrings.ConsultantsMakeActiveButton),
            style = MaterialTheme.typography.medium14.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

@FontScalePreviews
@Composable
private fun ConsultantInactiveButtonPreview() {
    ClientTheme {
        ConsultantInactiveButton(
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
