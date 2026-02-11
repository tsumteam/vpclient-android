package ru.mercury.vpclient.features.main.tabs.catalog.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

data class CatalogModel(
    val clientEntity: ClientEntity = ClientEntity.Empty
): Model
