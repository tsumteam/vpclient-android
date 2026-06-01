package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FittingConfirmationScreenLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FittingConfirmationLoadingSpacer()
        FittingConfirmationLoadingSpacer()
        FittingConfirmationLoadingSpacer()
        FittingConfirmationLoadingSpacer(
            modifier = Modifier.padding(top = 16.dp)
        )
        FittingConfirmationLoadingSpacer()
        FittingConfirmationLoadingSpacer()
    }
}
