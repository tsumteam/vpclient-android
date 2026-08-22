package ru.mercury.vpclient.features.checkout_sbp_bank_sheet.intent

import ru.mercury.vpclient.shared.data.entity.CheckoutSbpBank
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CheckoutSbpBankIntent: Intent {
    data object DismissClick: CheckoutSbpBankIntent
    data class BankClick(val bank: CheckoutSbpBank): CheckoutSbpBankIntent
}
