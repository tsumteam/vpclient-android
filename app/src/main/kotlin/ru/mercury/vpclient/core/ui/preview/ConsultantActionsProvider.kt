package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.ConsultantActionModel
import ru.mercury.vpclient.core.ui.icons.Brands24
import ru.mercury.vpclient.core.ui.icons.Catalog24
import ru.mercury.vpclient.core.ui.icons.Consultants24
import ru.mercury.vpclient.core.ui.icons.Fitting24
import ru.mercury.vpclient.core.ui.icons.Home24

class ConsultantActionsProvider: PreviewParameterProvider<List<ConsultantActionModel>> {
    override val values: Sequence<List<ConsultantActionModel>> = sequenceOf(
        listOf(
            ConsultantActionModel(id = "call", label = "Позвонить", icon = Home24),
            ConsultantActionModel(id = "fitting", label = "Примерка", icon = Fitting24),
            ConsultantActionModel(id = "cart", label = "Корзина", icon = Catalog24),
            ConsultantActionModel(id = "chat", label = "Чат", icon = Consultants24, showNotificationBadge = true),
            ConsultantActionModel(id = "selection", label = "Подборка", icon = Brands24)
        )
    )
}
