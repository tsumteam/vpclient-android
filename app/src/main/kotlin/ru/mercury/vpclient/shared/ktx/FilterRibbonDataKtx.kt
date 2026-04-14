package ru.mercury.vpclient.shared.ktx

import ru.mercury.vpclient.shared.entity.FilterRibbonData

val FilterRibbonData.isEmpty
    get() = this == FilterRibbonData.Empty

val FilterRibbonData.isNotEmpty
    get() = this != FilterRibbonData.Empty
