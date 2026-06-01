package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddress
import ru.mercury.vpclient.shared.data.entity.ClientDeliveryAddressSuggestion
import ru.mercury.vpclient.features.fitting_address_sheet.model.FittingAddressModel
import ru.mercury.vpclient.shared.data.entity.FittingAddressFormField

val ClientDeliveryAddress.fittingAddressModel: FittingAddressModel
    get() {
        return FittingAddressModel(
            addressId = id,
            address = address,
            flat = flat,
            entrance = entrance,
            intercom = intercom,
            floor = floor,
            comment = comment,
            latitude = latitude,
            longitude = longitude
        )
    }

fun FittingAddressModel.withSuggestion(
    suggestion: ClientDeliveryAddressSuggestion
): FittingAddressModel {
    return copy(
        address = suggestion.title,
        latitude = suggestion.latitude,
        longitude = suggestion.longitude
    )
}

fun FittingAddressModel.updated(
    field: FittingAddressFormField,
    value: String
): FittingAddressModel {
    return when (field) {
        FittingAddressFormField.Address -> copy(address = value)
        FittingAddressFormField.Flat -> copy(flat = value)
        FittingAddressFormField.Entrance -> copy(entrance = value)
        FittingAddressFormField.Intercom -> copy(intercom = value)
        FittingAddressFormField.Floor -> copy(floor = value)
        FittingAddressFormField.Comment -> copy(comment = value)
    }
}

fun FittingAddressModel.clientDeliveryAddress(): ClientDeliveryAddress {
    return ClientDeliveryAddress(
        id = addressId ?: 0,
        address = address,
        flat = flat,
        entrance = entrance,
        intercom = intercom,
        floor = floor,
        comment = comment,
        latitude = latitude,
        longitude = longitude
    )
}
