package ru.mercury.vpclient.features.main.tabs.consultants.model

import ru.mercury.vpclient.core.entity.ConsultantActionModel

data class ConsultantUiModel(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val workplace: String,
    val storeName: String,
    val actions: List<ConsultantActionModel>,
    val isActive: Boolean
) {
    companion object {
        val Empty = ConsultantUiModel(
            id = "",
            name = "",
            avatarUrl = "",
            workplace = "",
            storeName = "",
            actions = emptyList(),
            isActive = false
        )
    }
}
