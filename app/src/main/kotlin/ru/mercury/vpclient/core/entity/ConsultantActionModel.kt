package ru.mercury.vpclient.core.entity

import androidx.compose.ui.graphics.vector.ImageVector

data class ConsultantActionModel(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val showNotificationBadge: Boolean = false
)
