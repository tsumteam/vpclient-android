package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.FilterRibbonData

val FilterRibbonData.isEmpty
    get() = this == FilterRibbonData.Empty

val FilterRibbonData.isNotEmpty
    get() = this != FilterRibbonData.Empty
