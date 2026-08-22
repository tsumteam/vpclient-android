package ru.mercury.vpclient.features.push_notifications_sheet.model

import ru.mercury.vpclient.shared.mvi.Model

data class PushNotificationsModel(
    val isEnableButtonEnabled: Boolean = true
): Model
