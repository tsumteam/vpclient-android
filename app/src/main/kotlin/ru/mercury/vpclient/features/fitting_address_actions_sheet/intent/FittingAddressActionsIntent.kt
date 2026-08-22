package ru.mercury.vpclient.features.fitting_address_actions_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FittingAddressActionsIntent: Intent {
    data object DismissClick: FittingAddressActionsIntent
    data object EditClick: FittingAddressActionsIntent
    data object DeleteClick: FittingAddressActionsIntent
}
