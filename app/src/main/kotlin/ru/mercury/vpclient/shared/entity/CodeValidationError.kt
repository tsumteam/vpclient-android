package ru.mercury.vpclient.shared.entity

sealed interface CodeValidationError {
    data object Empty: CodeValidationError
    data object Invalid: CodeValidationError
}
