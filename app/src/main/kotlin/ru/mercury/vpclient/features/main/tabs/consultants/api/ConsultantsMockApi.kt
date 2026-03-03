package ru.mercury.vpclient.features.main.tabs.consultants.api

import ru.mercury.vpclient.core.entity.ConsultantActionModel
import ru.mercury.vpclient.core.ui.icons.Brands24
import ru.mercury.vpclient.core.ui.icons.Catalog24
import ru.mercury.vpclient.core.ui.icons.Consultants24
import ru.mercury.vpclient.core.ui.icons.Fitting24
import ru.mercury.vpclient.core.ui.icons.Home24
import ru.mercury.vpclient.features.main.tabs.consultants.model.ConsultantUiModel

//fixme
object ConsultantsMockApi {

    fun getConsultants(): List<ConsultantUiModel> {
        return listOf(
            ConsultantUiModel(
                id = "consultant_1",
                name = "Анна Смирнова",
                avatarUrl = "https://i.pravatar.cc/144?img=32",
                isActive = true,
                actions = consultantActions()
            ),
            ConsultantUiModel(
                id = "consultant_2",
                name = "Мария Иванова",
                avatarUrl = "https://i.pravatar.cc/144?img=47",
                isActive = false,
                actions = consultantActions()
            ),
            ConsultantUiModel(
                id = "consultant_3",
                name = "Олег Петров",
                avatarUrl = "https://i.pravatar.cc/144?img=12",
                isActive = false,
                actions = consultantActions()
            )
        )
    }
}

private fun consultantActions(): List<ConsultantActionModel> {
    return listOf(
        ConsultantActionModel(id = "call", label = "Позвонить", icon = Home24),
        ConsultantActionModel(id = "fitting", label = "Примерка", icon = Fitting24),
        ConsultantActionModel(id = "cart", label = "Корзина", icon = Catalog24),
        ConsultantActionModel(id = "chat", label = "Чат", icon = Consultants24, showNotificationBadge = true),
        ConsultantActionModel(id = "selection", label = "Подборка", icon = Brands24)
    )
}
