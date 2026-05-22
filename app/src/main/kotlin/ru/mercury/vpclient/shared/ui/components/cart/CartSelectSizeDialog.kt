@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.components.system.ClientButton
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun CartSelectSizeDialog(
    onSelectSizeClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(ClientStrings.CartSelectSizeForPaymentTitle),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.medium18.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                )
            )

            ClientButton(
                onClick = onSelectSizeClick,
                text = stringResource(ClientStrings.CartSelectSizeForPaymentButton),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartSelectSizeDialogPreview() {
    CartSelectSizeDialog(
        onSelectSizeClick = {},
        onDismissRequest = {}
    )
}
