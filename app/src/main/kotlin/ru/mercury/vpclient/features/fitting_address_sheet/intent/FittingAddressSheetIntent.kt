package ru.mercury.vpclient.features.fitting_address_sheet.intent

import ru.mercury.vpclient.shared.data.entity.FittingAddressFormField
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressSheetIntent: Intent {
    data object DismissRequest: FittingAddressSheetIntent
    data object OpenAddressSearch: FittingAddressSheetIntent
    data object SaveAddressClick: FittingAddressSheetIntent
    data class AddressFormValueChange(
        val field: FittingAddressFormField,
        val value: String
    ): FittingAddressSheetIntent
}
