package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.json.JsonObject

data class SearchResultsMetadata(
    val categoryId: Int,
    val titleCategoryId: Int,
    val searchText: String,
    val searchRequestId: String?,
    val correction: String?,
    val catalogLink: JsonObject?
) {
    companion object {
        val Empty = SearchResultsMetadata(
            categoryId = 0,
            titleCategoryId = 0,
            searchText = "",
            searchRequestId = null,
            correction = null,
            catalogLink = null
        )
    }
}
