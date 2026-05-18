package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.ui.components.consultants.ConsultantCartActionButtonState

class ConsultantCartActionButtonStateProvider: PreviewParameterProvider<ConsultantCartActionButtonState> {
    override val values: Sequence<ConsultantCartActionButtonState> = sequenceOf(
        ConsultantCartActionButtonState(
            label = "Корзина",
            cartText = "",
            showBadge = false,
            onClick = {}
        ),
        ConsultantCartActionButtonState(
            label = "Корзина",
            cartText = "2",
            showBadge = false,
            onClick = {}
        ),
        ConsultantCartActionButtonState(
            label = "Корзина",
            cartText = "12",
            showBadge = true,
            onClick = {}
        )
    )
}
