package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GradeResponse(
    @SerialName("gradeCurrent") val gradeCurrent: Double? = null,
    @SerialName("grade2GoalAmount") val grade2GoalAmount: Double? = null,
    @SerialName("grade3GoalAmount") val grade3GoalAmount: Double? = null,
    @SerialName("grade4GoalAmount") val grade4GoalAmount: Double? = null,
    @SerialName("grade5GoalAmount") val grade5GoalAmount: Double? = null
)
