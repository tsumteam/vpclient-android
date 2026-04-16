package ru.mercury.vpclient.shared.ui.components.consultants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.ConsultantAvatarPlaceholderStyle
import ru.mercury.vpclient.shared.ui.preview.ConsultantAvatarPlaceholderStyleProvider
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.regular21
import ru.mercury.vpclient.shared.ui.theme.regular32
import ru.mercury.vpclient.shared.ui.theme.secondary4

@Composable
fun ConsultantAvatarPlaceholder(
    name: String,
    style: ConsultantAvatarPlaceholderStyle,
    modifier: Modifier = Modifier
) {
    val size = when (style) {
        ConsultantAvatarPlaceholderStyle.Card -> 72.dp
        ConsultantAvatarPlaceholderStyle.Screen -> 116.dp
    }
    val textStyle = when (style) {
        ConsultantAvatarPlaceholderStyle.Card -> MaterialTheme.typography.regular21
        ConsultantAvatarPlaceholderStyle.Screen -> MaterialTheme.typography.regular32
    }

    Box(
        modifier = modifier
            .size(size)
            .background(
                color = MaterialTheme.colorScheme.secondary4,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercaseChar()?.toString().orEmpty(),
            style = textStyle.copy(
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Preview
@Composable
private fun ConsultantAvatarPlaceholderPreview(
    @PreviewParameter(ConsultantAvatarPlaceholderStyleProvider::class) style: ConsultantAvatarPlaceholderStyle
) {
    ClientTheme {
        ConsultantAvatarPlaceholder(
            name = "Анастасия",
            style = style
        )
    }
}
