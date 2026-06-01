package ru.mercury.vpclient.features.fitting_address_delete_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressDeleteDialogIntent: Intent {
    data object ConfirmClick: FittingAddressDeleteDialogIntent
    data object DismissRequest: FittingAddressDeleteDialogIntent
}
