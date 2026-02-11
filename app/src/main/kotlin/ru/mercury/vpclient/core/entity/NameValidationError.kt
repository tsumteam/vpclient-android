package ru.mercury.vpclient.core.entity

sealed interface NameValidationError {
    data object Empty: NameValidationError
}
