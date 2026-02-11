package ru.mercury.vpclient.features.main.tabs.profile.model

import ru.mercury.vpclient.core.mvi.Model
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

data class ProfileModel(
    val clientEntity: ClientEntity = ClientEntity.Empty
): Model
