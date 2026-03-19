package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.entity.FilterRibbonData

val FilterRibbonData.isEmpty
    get() = this == FilterRibbonData.Empty

val FilterRibbonData.isNotEmpty
    get() = this != FilterRibbonData.Empty
