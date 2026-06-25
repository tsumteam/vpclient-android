package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RemoveBackgroundStatus {
    @SerialName("alreadyRemoved") ALREADY_REMOVED,
    @SerialName("removalSuccessful") REMOVAL_SUCCESSFUL,
    @SerialName("removalRequiresUserConfirmation") REMOVAL_REQUIRES_USER_CONFIRMATION
}
