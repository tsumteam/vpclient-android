package ru.mercury.vpclient.shared.domain.repository

interface CartRepository {

    suspend fun cartItemsCount(): Int

    suspend fun cartBadge(): Int
}
