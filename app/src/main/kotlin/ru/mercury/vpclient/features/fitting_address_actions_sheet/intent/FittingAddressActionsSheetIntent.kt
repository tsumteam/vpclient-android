package ru.mercury.vpclient.features.fitting_address_actions_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressActionsSheetIntent: Intent {
    data object EditClick: FittingAddressActionsSheetIntent
    data object DeleteClick: FittingAddressActionsSheetIntent
    data object DismissRequest: FittingAddressActionsSheetIntent
}
