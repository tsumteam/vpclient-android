package ru.mercury.vpclient.features.main.tabs.profile.model

import ru.mercury.vpclient.core.entity.DriverEntity
import ru.mercury.vpclient.core.mvi.Model

data class ProfileModel(
    val driverEntity: DriverEntity = DriverEntity.Empty
): Model
