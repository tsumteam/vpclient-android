package ru.mercury.vpclient.features.main.tabs.consultants.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

data class ConsultantsModel(
    val clientEntity: ClientEntity = ClientEntity.Empty
): Model
