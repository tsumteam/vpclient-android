package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InternalExcelImportAppendCollectionPostResponse(
    @SerialName("formFile") val formFile: ByteArray? = null
)
