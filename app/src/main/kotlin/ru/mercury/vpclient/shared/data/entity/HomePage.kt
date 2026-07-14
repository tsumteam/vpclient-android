package ru.mercury.vpclient.shared.data.entity

data class HomePage(
    val tab: TabType,
    val homeSectionEntities: List<HomeSectionEntity> = emptyList()
)
