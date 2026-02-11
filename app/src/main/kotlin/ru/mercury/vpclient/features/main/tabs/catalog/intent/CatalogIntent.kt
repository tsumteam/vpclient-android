package ru.mercury.vpclient.features.main.tabs.catalog.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface CatalogIntent: Intent {
    data object CollectClientEntity: CatalogIntent
}
