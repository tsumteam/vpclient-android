package ru.mercury.vpclient.features.main.tabs.consultants.api

import ru.mercury.vpclient.core.entity.ConsultantActionModel
import ru.mercury.vpclient.core.ui.icons.Basket24
import ru.mercury.vpclient.core.ui.icons.Chat24
import ru.mercury.vpclient.core.ui.icons.FittingShirt24
import ru.mercury.vpclient.core.ui.icons.Phone24
import ru.mercury.vpclient.core.ui.icons.Selection24
import ru.mercury.vpclient.features.main.tabs.consultants.model.ConsultantUiModel

//fixme
object ConsultantsMockApi {

    fun getConsultants(): List<ConsultantUiModel> {
        return listOf(
            ConsultantUiModel(
                id = "consultant_1",
                name = "Анна Смирнова",
                avatarUrl = "https://i.pravatar.cc/144?img=32",
                workplace = "MVST",
                storeName = "Барвиха Luxury Village",
                isActive = true,
                actions = consultantActions()
            ),
            ConsultantUiModel(
                id = "consultant_2",
                name = "Мария Иванова",
                avatarUrl = "https://i.pravatar.cc/144?img=47",
                workplace = "MVST",
                storeName = "Барвиха Luxury Village",
                isActive = false,
                actions = consultantActions()
            ),
            ConsultantUiModel(
                id = "consultant_3",
                name = "Олег Петров",
                avatarUrl = "https://i.pravatar.cc/144?img=12",
                workplace = "MVST",
                storeName = "Барвиха Luxury Village",
                isActive = false,
                actions = consultantActions()
            )
        )
    }
}

private fun consultantActions(): List<ConsultantActionModel> {
    return listOf(
        ConsultantActionModel(id = "call", label = "Позвонить", icon = Phone24),
        ConsultantActionModel(id = "fitting", label = "Примерка", icon = FittingShirt24),
        ConsultantActionModel(id = "cart", label = "Корзина", icon = Basket24),
        ConsultantActionModel(id = "chat", label = "Чат", icon = Chat24, showNotificationBadge = true),
        ConsultantActionModel(id = "selection", label = "Подборка", icon = Selection24)
    )
}
