package ru.mercury.vpclient.features.checkout_sbp_bank_sheet.model

import ru.mercury.vpclient.shared.data.entity.CheckoutSbpBank
import ru.mercury.vpclient.shared.mvi.Model

data class CheckoutSbpBankSheetModel(
    val banks: List<CheckoutSbpBank> = emptyList()
): Model
