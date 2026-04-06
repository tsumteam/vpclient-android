package ru.mercury.vpclient.features.details.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface DetailsIntent: Intent {
    data object CollectProduct: DetailsIntent
    data object LoadProduct: DetailsIntent
    data object BackClick: DetailsIntent
    data object MessageClick: DetailsIntent
    data object SizeTableClick: DetailsIntent
    data class SizeClick(val index: Int): DetailsIntent
}
