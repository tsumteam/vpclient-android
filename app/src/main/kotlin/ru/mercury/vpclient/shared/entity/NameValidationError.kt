package ru.mercury.vpclient.shared.entity

sealed interface NameValidationError {
    data object Empty: NameValidationError
}
