package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.PhoneValidationError
import ru.mercury.vpclient.features.login.model.LoginModel

class LoginModelProvider: PreviewParameterProvider<LoginModel> {
    override val values: Sequence<LoginModel> = sequenceOf(
        LoginModel(),
        LoginModel(phone = "79991234567"),
        LoginModel(phoneValidationError = PhoneValidationError.Empty),
        LoginModel(phoneValidationError = PhoneValidationError.Invalid),
        LoginModel(phone = "79991234567", phoneValidationError = PhoneValidationError.Empty),
        LoginModel(phone = "79991234567", isLoading = true)
    )
}
