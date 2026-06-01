package ru.mercury.vpclient.features.fitting_address_sheet.model

data class FittingAddressModel(
    val addressId: Int? = null,
    val address: String = "",
    val flat: String = "",
    val entrance: String = "",
    val intercom: String = "",
    val floor: String = "",
    val comment: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    val isEdit: Boolean
        get() {
            return addressId != null
        }

    val isSaveEnabled: Boolean
        get() {
            return address.isNotBlank()
        }
}
