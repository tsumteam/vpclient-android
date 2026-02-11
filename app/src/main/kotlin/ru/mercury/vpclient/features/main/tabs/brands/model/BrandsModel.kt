package ru.mercury.vpclient.features.main.tabs.brands.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

data class BrandsModel(
    val clientEntity: ClientEntity = ClientEntity.Empty
): Model
