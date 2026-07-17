package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.placeholder

@Composable
fun FittingConfirmationLoadingSpacer(
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .height(40.dp)
            .placeholder(shape = RoundedCornerShape(4.dp))
    )
}
