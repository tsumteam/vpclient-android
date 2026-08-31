package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
enum class MessengerMessagePayloadType {
    Product,
    CompilationLook,
    ClientCompilation,
    BasketLook,
    Images,
    Videos,
    GiftCard,
    Order
}
