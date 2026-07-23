package ru.mercury.vpclient.features.search.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.entity.SearchSource

@Serializable
data class SearchRoute(
    val source: SearchSource = SearchSource.CATALOG
): NavKey
