package ru.mercury.vpclient.features.main.tabs.fitting.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

data class FittingModel(
    val clientEntity: ClientEntity = ClientEntity.Empty
): Model
