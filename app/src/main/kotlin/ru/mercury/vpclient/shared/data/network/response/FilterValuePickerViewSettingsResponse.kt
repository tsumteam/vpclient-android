package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FilterValuePickerViewSettingsResponse(
    @SerialName("showSearchBar") val showSearchBar: Boolean? = null,
    @SerialName("showSidePanelWithLetters") val showSidePanelWithLetters: Boolean? = null,
    @SerialName("showPhotos") val showPhotos: Boolean? = null
)
