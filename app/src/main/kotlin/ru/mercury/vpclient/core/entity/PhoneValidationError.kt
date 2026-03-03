package ru.mercury.vpclient.core.entity

sealed interface PhoneValidationError {
    data object Empty: PhoneValidationError
    data object Invalid: PhoneValidationError
}
