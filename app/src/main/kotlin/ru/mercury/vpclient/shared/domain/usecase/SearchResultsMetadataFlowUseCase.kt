package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.SearchResultsMetadataStore
import ru.mercury.vpclient.shared.data.entity.SearchResultsMetadata
import javax.inject.Inject

class SearchResultsMetadataFlowUseCase @Inject constructor(
    private val store: SearchResultsMetadataStore,
    dispatchers: SharedDispatchers
): FlowUseCase<Unit, SearchResultsMetadata>(dispatchers.io) {

    override fun execute(parameters: Unit): Flow<SearchResultsMetadata> {
        return store.metadataFlow
    }
}
