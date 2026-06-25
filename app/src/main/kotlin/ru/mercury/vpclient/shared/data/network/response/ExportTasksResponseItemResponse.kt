package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportTasksResponseItemResponse(
    @SerialName("areaId") val areaId: String? = null,
    @SerialName("brandId") val brandId: String? = null,
    @SerialName("employeeName") val employeeName: String? = null,
    @SerialName("employeeId") val employeeId: String? = null,
    @SerialName("employeeHrId") val employeeHrId: Int? = null,
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("crmId") val crmId: String? = null,
    @SerialName("taskId") val taskId: Int? = null,
    @SerialName("reasons") val reasons: String? = null,
    @SerialName("reasonsAsString") val reasonsAsString: String? = null,
    @SerialName("taskCommunicationType") val taskCommunicationType: Int? = null,
    @SerialName("taskCommunicationTypeString") val taskCommunicationTypeString: String? = null,
    @SerialName("taskComment") val taskComment: String? = null,
    @SerialName("taskCreatedDate") val taskCreatedDate: String? = null,
    @SerialName("taskCompletionDate") val taskCompletionDate: String? = null,
    @SerialName("taskRemindTimeSpan") val taskRemindTimeSpan: String? = null,
    @SerialName("taskIsOverdue") val taskIsOverdue: Boolean? = null,
    @SerialName("eventIdSource") val eventIdSource: Int? = null,
    @SerialName("taskExpectedAmount") val taskExpectedAmount: Double? = null,
    @SerialName("taskResult") val taskResult: Int? = null,
    @SerialName("taskResultAsString") val taskResultAsString: String? = null,
    @SerialName("guestsNumber") val guestsNumber: Int? = null
)
