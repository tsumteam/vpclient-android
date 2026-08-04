package ru.mercury.vpclient.features.compilation_benefit_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CompilationBenefitIntent: Intent {
    data object DismissRequest: CompilationBenefitIntent
}
