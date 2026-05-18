package ru.mercury.vpclient.shared.ui.components.consultants

data class ConsultantCartActionButtonState(
    val label: String,
    val cartText: String,
    val showBadge: Boolean,
    val onClick: () -> Unit
)
