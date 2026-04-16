package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.ConsultantAvatarPlaceholderStyle

class ConsultantAvatarPlaceholderStyleProvider: PreviewParameterProvider<ConsultantAvatarPlaceholderStyle> {
    override val values: Sequence<ConsultantAvatarPlaceholderStyle> = sequenceOf(
        ConsultantAvatarPlaceholderStyle.Card,
        ConsultantAvatarPlaceholderStyle.Screen
    )
}
