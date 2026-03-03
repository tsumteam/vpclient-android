package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.NameValidationError
import ru.mercury.vpclient.core.entity.PhoneValidationError
import ru.mercury.vpclient.features.register.model.RegisterModel

class RegisterModelProvider: PreviewParameterProvider<RegisterModel> {
    override val values: Sequence<RegisterModel> = sequenceOf(
        RegisterModel(),
        RegisterModel(name = "Иван"),
        RegisterModel(phone = "79991234567"),
        RegisterModel(name = "Иван", phone = "79991234567"),
        RegisterModel(nameValidationError = NameValidationError.Empty),
        RegisterModel(phoneValidationError = PhoneValidationError.Empty),
        RegisterModel(phoneValidationError = PhoneValidationError.Invalid),
        RegisterModel(
            nameValidationError = NameValidationError.Empty,
            phoneValidationError = PhoneValidationError.Empty
        ),
        RegisterModel(name = "Иван", phone = "79991234567", isLoading = true)
    )
}
