package ru.mercury.vpclient.core.entity

sealed interface CodeValidationError {
    data object Empty: CodeValidationError
    data object Invalid: CodeValidationError
}
