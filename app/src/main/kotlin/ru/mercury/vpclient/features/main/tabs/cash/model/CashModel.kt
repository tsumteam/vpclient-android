package ru.mercury.vpclient.features.main.tabs.cash.model

import ru.mercury.vpclient.core.entity.DriverEntity
import ru.mercury.vpclient.core.mvi.Model

data class CashModel(
    val driverEntity: DriverEntity = DriverEntity.Empty
): Model
