@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.SearchResultsMetadataStore
import ru.mercury.vpclient.shared.data.entity.SearchResultsMetadata
import javax.inject.Inject

class SetSearchResultsMetadataUseCase @Inject constructor(
    private val store: SearchResultsMetadataStore,
    dispatchers: SharedDispatchers
): UseCase<SearchResultsMetadata, Unit>(dispatchers.immediate) {

    override suspend fun execute(metadata: SearchResultsMetadata) {
        store.update(metadata)
    }
}
