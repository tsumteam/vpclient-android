package ru.mercury.vpclient.shared.data.event

import ru.mercury.vpclient.shared.ui.components.profile.ProfileOrderProductItemState

data class ProfileOrderDeliveryGroupState(
    val id: String,
    val date: String,
    val address: String,
    val products: List<ProfileOrderProductItemState>
)
