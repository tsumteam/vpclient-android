package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ActionItemType {
    @SerialName("newCollection") NEW_COLLECTION,
    @SerialName("httpLinkAction") HTTP_LINK_ACTION,
    @SerialName("newCollectionVM") NEW_COLLECTION_VM,
    @SerialName("specialOfferAction") SPECIAL_OFFER_ACTION
}
