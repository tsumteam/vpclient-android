package ru.mercury.vpclient.features.fitting_address_delete_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressDeleteIntent: Intent {
    data object ConfirmClick: FittingAddressDeleteIntent
    data object DismissRequest: FittingAddressDeleteIntent
}
