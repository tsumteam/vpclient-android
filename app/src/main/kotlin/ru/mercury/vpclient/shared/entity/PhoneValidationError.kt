package ru.mercury.vpclient.shared.entity

sealed interface PhoneValidationError {
    data object Empty: PhoneValidationError
    data object Invalid: PhoneValidationError
}
