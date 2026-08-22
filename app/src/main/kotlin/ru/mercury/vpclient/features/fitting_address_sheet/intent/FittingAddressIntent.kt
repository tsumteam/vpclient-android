package ru.mercury.vpclient.features.fitting_address_sheet.intent

import ru.mercury.vpclient.shared.data.entity.FittingAddressFormField
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressIntent: Intent {
    data object DismissRequest: FittingAddressIntent
    data object OpenAddressSearch: FittingAddressIntent
    data object SaveAddressClick: FittingAddressIntent
    data class AddressFormValueChange(
        val field: FittingAddressFormField,
        val value: String
    ): FittingAddressIntent
}
