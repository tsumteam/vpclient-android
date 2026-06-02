@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_size_require_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.cart_size_require_dialog.intent.CartSizeRequireIntent
import ru.mercury.vpclient.shared.ui.components.SharedBasicAlertDialog
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun CartSizeRequireDialog(
    dispatch: (CartSizeRequireIntent) -> Unit
) {
    SharedBasicAlertDialog(
        onDismissRequest = { dispatch(CartSizeRequireIntent.DismissRequest) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
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

            Button(
                onClick = { dispatch(CartSizeRequireIntent.SelectSizeClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CartSelectSizeForPaymentButton),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}
