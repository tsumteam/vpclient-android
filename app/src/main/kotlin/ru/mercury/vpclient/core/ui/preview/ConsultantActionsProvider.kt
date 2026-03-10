package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.ConsultantActionModel
import ru.mercury.vpclient.core.ui.icons.Basket24
import ru.mercury.vpclient.core.ui.icons.Chat24
import ru.mercury.vpclient.core.ui.icons.FittingShirt24
import ru.mercury.vpclient.core.ui.icons.Phone24
import ru.mercury.vpclient.core.ui.icons.Selection24

class ConsultantActionsProvider: PreviewParameterProvider<List<ConsultantActionModel>> {
    override val values: Sequence<List<ConsultantActionModel>> = sequenceOf(
        listOf(
            ConsultantActionModel(id = "call", label = "Позвонить", icon = Phone24),
            ConsultantActionModel(id = "fitting", label = "Примерка", icon = FittingShirt24),
            ConsultantActionModel(id = "cart", label = "Корзина", icon = Basket24),
            ConsultantActionModel(id = "chat", label = "Чат", icon = Chat24, showNotificationBadge = true),
            ConsultantActionModel(id = "selection", label = "Подборка", icon = Selection24)
        )
    )
}
