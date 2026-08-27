package ru.mercury.vpclient.features.messenger_sheet.model

import ru.mercury.vpclient.shared.mvi.Model

data class MessengerModel(
    val name: String = "",
    val brand: String = ""
): Model
