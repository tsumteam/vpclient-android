package ru.mercury.vpclient.shared.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mercury.vpclient.shared.data.entity.SearchResultsMetadata
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchResultsMetadataStore @Inject constructor() {

    private val mutableMetadataFlow = MutableStateFlow(SearchResultsMetadata.Empty)
    val metadataFlow: StateFlow<SearchResultsMetadata> = mutableMetadataFlow

    fun update(metadata: SearchResultsMetadata) {
        mutableMetadataFlow.value = metadata
    }
}
