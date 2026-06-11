package ru.mercury.vpclient.shared.ui.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.Attention16
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular12

@Composable
fun ProfileOrderPaymentBadge(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(20.dp)
            .background(
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(11.dp)
            )
            .padding(start = 2.dp, end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Attention16,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified
        )

        Text(
            text = stringResource(ClientStrings.ProfileOrdersAwaitingPayment),
            style = MaterialTheme.typography.regular12.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = 16.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileOrderPaymentBadgePreview() {
    ProfileOrderPaymentBadge()
}
