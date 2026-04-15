package ru.mercury.vpclient.shared.data.entity

sealed interface PhoneValidationError {
    data object Empty: PhoneValidationError
    data object Invalid: PhoneValidationError
}
