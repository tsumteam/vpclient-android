package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.data.network.response.AddressSuggestionResponse
import ru.mercury.vpclient.shared.data.network.response.ClientAddressBaseResponse
import ru.mercury.vpclient.shared.data.network.response.ClientAddressDtoResponse
import ru.mercury.vpclient.shared.data.network.response.ClientAddressWithCoordinateResponse
import ru.mercury.vpclient.shared.data.network.response.CoordinateResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientDeliveryAddressEntity

val ClientAddressWithCoordinateResponse.clientDeliveryAddressEntity: ClientDeliveryAddressEntity?
    get() {
        val id = addressId ?: return null
        val addressValue = address.orEmpty()
        if (addressValue.isBlank()) {
            return null
        }

        return ClientDeliveryAddressEntity(
            id = id,
            address = addressValue,
            flat = flat.orEmpty(),
            entrance = entrance.orEmpty(),
            intercom = intercom.orEmpty(),
            floor = floor.orEmpty(),
            comment = comment.orEmpty(),
            latitude = latitude,
            longitude = longitude
        )
    }

val ClientDeliveryAddressEntity.title: String
    get() = listOfNotNull(
        address,
        flat.takeIf { it.isNotBlank() }?.let { "кв./офис $it" }
    ).joinToString(", ")

val ClientDeliveryAddressEntity.clientAddressBaseDto: ClientAddressBaseResponse
    get() = ClientAddressBaseResponse(
        address = address,
        flat = flat,
        entrance = entrance,
        intercom = intercom,
        floor = floor,
        comment = comment
    )

val ClientDeliveryAddressEntity.clientAddressDto: ClientAddressDtoResponse
    get() = ClientAddressDtoResponse(
        addressId = id,
        address = address,
        flat = flat,
        entrance = entrance,
        intercom = intercom,
        floor = floor,
        comment = comment
    )

fun ClientAddressDtoResponse.clientDeliveryAddress(
    fallback: ClientDeliveryAddressEntity
): ClientDeliveryAddressEntity {
    return ClientDeliveryAddressEntity(
        id = addressId ?: fallback.id,
        address = address ?: fallback.address,
        flat = flat ?: fallback.flat,
        entrance = entrance ?: fallback.entrance,
        intercom = intercom ?: fallback.intercom,
        floor = floor ?: fallback.floor,
        comment = comment ?: fallback.comment,
        latitude = fallback.latitude,
        longitude = fallback.longitude
    )
}

val ClientDeliveryAddressEntity.coordinateDto: CoordinateResponse?
    get() = when {
        latitude != null && longitude != null -> CoordinateResponse(
            latitude = latitude,
            longitude = longitude
        )
        else -> null
    }

val AddressSuggestionResponse.clientDeliveryAddressSuggestion: ClientDeliveryAddressSuggestion?
    get() {
        val titleValue = title.orEmpty()
        if (titleValue.isBlank()) {
            return null
        }

        return ClientDeliveryAddressSuggestion(
            title = titleValue,
            latitude = coordinate?.latitude,
            longitude = coordinate?.longitude
        )
    }
