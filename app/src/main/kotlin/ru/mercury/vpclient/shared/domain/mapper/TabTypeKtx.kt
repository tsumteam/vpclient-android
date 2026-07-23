package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.TabType

val TabType.catalogRootId: Int
    get() = when (this) {
        TabType.WOMAN -> 2
        TabType.MAN -> 3
        TabType.CHILD -> 4
    }
