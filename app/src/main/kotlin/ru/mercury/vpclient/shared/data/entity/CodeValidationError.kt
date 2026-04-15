package ru.mercury.vpclient.shared.data.entity

sealed interface CodeValidationError {
    data object Empty: CodeValidationError
    data object Invalid: CodeValidationError
}
