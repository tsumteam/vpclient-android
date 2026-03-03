package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.coroutines.Job
import ru.mercury.vpclient.core.entity.CodeValidationError
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity
import ru.mercury.vpclient.features.code.model.CodeModel

class CodeModelProvider: PreviewParameterProvider<CodeModel> {
    override val values: Sequence<CodeModel> = sequenceOf(
        baseState,
        baseState.copy(code = "123"),
        baseState.copy(codeValidationError = CodeValidationError.Empty),
        baseState.copy(code = "123456", codeValidationError = CodeValidationError.Invalid),
        baseState.copy(code = "123456", isLoading = true),
        baseState.copy(resendSecondsLeft = 22, resendTimerJob = Job()),
        baseState.copy(resendCodeJob = Job()),
        baseState.copy(code = "123456", resendSecondsLeft = 12, resendCodeJob = Job())
    )

    companion object {
        private val baseState = CodeModel(
            clientEntity = ClientEntity(
                phone = "+79991234567",
                name = "Иван"
            )
        )
    }
}
