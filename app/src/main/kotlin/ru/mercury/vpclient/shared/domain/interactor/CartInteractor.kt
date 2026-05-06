package ru.mercury.vpclient.shared.domain.interactor

interface CartInteractor {

    suspend fun cartItemsCount(): Int

    suspend fun cartBadge(): Int
}
