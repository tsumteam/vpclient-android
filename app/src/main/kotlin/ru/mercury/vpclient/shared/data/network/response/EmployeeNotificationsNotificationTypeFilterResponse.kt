package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.EmployeeNotificationsFilterType

@Serializable
data class EmployeeNotificationsNotificationTypeFilterResponse(
    @SerialName("filterType") val filterType: EmployeeNotificationsFilterType? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("labelPhotoUrl") val labelPhotoUrl: String? = null,
    @SerialName("isMultiSelect") val isMultiSelect: Boolean? = null,
    @SerialName("ribbonSettings") val ribbonSettings: FilterRibbonSettingsDtoResponse? = null,
    @SerialName("values") val values: List<FilterStringValueResponse> = emptyList()
)
