package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddress
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.shared.data.network.entity.AddressSuggestionDto
import ru.mercury.vpclient.shared.data.network.entity.ClientAddressBaseDto
import ru.mercury.vpclient.shared.data.network.entity.ClientAddressDto
import ru.mercury.vpclient.shared.data.network.entity.ClientAddressWithCoordinateDto
import ru.mercury.vpclient.shared.data.network.entity.CoordinateDto

val ClientAddressWithCoordinateDto.clientDeliveryAddress: ClientDeliveryAddress?
    get() {
        val id = addressId ?: return null
        val addressValue = address.orEmpty()
        if (addressValue.isBlank()) {
            return null
        }

        return ClientDeliveryAddress(
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

val ClientDeliveryAddress.clientAddressBaseDto: ClientAddressBaseDto
    get() = ClientAddressBaseDto(
        address = address,
        flat = flat,
        entrance = entrance,
        intercom = intercom,
        floor = floor,
        comment = comment
    )

val ClientDeliveryAddress.clientAddressDto: ClientAddressDto
    get() = ClientAddressDto(
        addressId = id,
        address = address,
        flat = flat,
        entrance = entrance,
        intercom = intercom,
        floor = floor,
        comment = comment
    )

fun ClientAddressDto.clientDeliveryAddress(
    fallback: ClientDeliveryAddress
): ClientDeliveryAddress {
    return ClientDeliveryAddress(
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

val ClientDeliveryAddress.coordinateDto: CoordinateDto?
    get() = when {
        latitude != null && longitude != null -> CoordinateDto(
            latitude = latitude,
            longitude = longitude
        )
        else -> null
    }

val AddressSuggestionDto.clientDeliveryAddressSuggestion: ClientDeliveryAddressSuggestion?
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
