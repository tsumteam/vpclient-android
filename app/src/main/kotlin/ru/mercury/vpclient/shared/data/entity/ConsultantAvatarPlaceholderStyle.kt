package ru.mercury.vpclient.shared.data.entity

sealed interface ConsultantAvatarPlaceholderStyle {
    data object Card: ConsultantAvatarPlaceholderStyle
    data object Screen: ConsultantAvatarPlaceholderStyle
}
