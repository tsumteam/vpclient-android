package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.green
import ru.mercury.vpclient.core.ui.theme.medium14

@Composable
fun ConsultantActiveButton(
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeText = stringResource(ClientStrings.ConsultantsActiveButton)
    val makeActiveText = stringResource(ClientStrings.ConsultantsMakeActiveButton)
    val text = if (isActive) activeText else makeActiveText
    val containerColor = if (isActive) MaterialTheme.colorScheme.green else MaterialTheme.colorScheme.background
    val contentColor = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground

    Surface(
        onClick = { if (!isActive) onClick() },
        modifier = modifier.requiredHeight(31.dp),
        shape = RoundedCornerShape(4.dp),
        color = containerColor,
        contentColor = contentColor,
        border = if (!isActive) BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground) else null
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = makeActiveText,
                style = MaterialTheme.typography.medium14,
                textAlign = TextAlign.Center,
                color = Color.Transparent
            )

            Text(
                text = text,
                style = MaterialTheme.typography.medium14,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun ConsultantActiveButtonPreview() {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            ConsultantActiveButton(
                isActive = false,
                onClick = {},
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
