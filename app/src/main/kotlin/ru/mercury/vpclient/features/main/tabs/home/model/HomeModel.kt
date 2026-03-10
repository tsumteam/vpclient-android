package ru.mercury.vpclient.features.main.tabs.home.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

data class HomeModel(
    val clientEntity: ClientEntity = ClientEntity.Empty
): Model
