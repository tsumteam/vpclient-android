package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportEmployeeEventsResponseItemResponse(
    @SerialName("areaId") val areaId: String? = null,
    @SerialName("brandId") val brandId: String? = null,
    @SerialName("employeeName") val employeeName: String? = null,
    @SerialName("employeeId") val employeeId: String? = null,
    @SerialName("employeeHrId") val employeeHrId: Int? = null,
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("crmId") val crmId: String? = null,
    @SerialName("eventId") val eventId: Int? = null,
    @SerialName("reasonIds") val reasonIds: String? = null,
    @SerialName("reasonsAsString") val reasonsAsString: String? = null,
    @SerialName("eventCommunicationType") val eventCommunicationType: Int? = null,
    @SerialName("eventCommunicationTypeString") val eventCommunicationTypeString: String? = null,
    @SerialName("eventComment") val eventComment: String? = null,
    @SerialName("eventTime") val eventTime: String? = null,
    @SerialName("taskIdSource") val taskIdSource: Int? = null,
    @SerialName("taskCreatedDateSource") val taskCreatedDateSource: String? = null,
    @SerialName("taskCompletionDateSource") val taskCompletionDateSource: String? = null,
    @SerialName("taskExpectedAmountSource") val taskExpectedAmountSource: Double? = null,
    @SerialName("taskResult") val taskResult: Int? = null,
    @SerialName("taskResultAsString") val taskResultAsString: String? = null,
    @SerialName("guestsNumber") val guestsNumber: Int? = null
)
