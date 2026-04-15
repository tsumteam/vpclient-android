package ru.mercury.vpclient.shared.data.entity

sealed interface NameValidationError {
    data object Empty: NameValidationError
}
