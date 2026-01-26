package ru.mercury.vpclient.core.entity

sealed class VPClientError(
    open val message: String,
    open val httpCode: Int? = null,
    open val backendCode: Int? = null,
    open val backendReason: String? = null,
    open val cause: Throwable? = null
) {
    data class Network(
        override val message: String,
        override val cause: Throwable? = null
    ): VPClientError(message, cause = cause)

    data class Http(
        override val message: String,
        override val httpCode: Int,
        override val backendCode: Int? = null,
        override val backendReason: String? = null
    ): VPClientError(message, httpCode, backendCode, backendReason)

    data class Unknown(
        override val message: String,
        override val cause: Throwable? = null
    ): VPClientError(message, cause = cause)
}
