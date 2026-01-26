package ru.mercury.vpclient.core.ktx

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.core.entity.VPClientError
import ru.mercury.vpclient.core.exception.VPClientEmptyException
import ru.mercury.vpclient.core.exception.VPClientException
import ru.mercury.vpclient.core.network.response.BaseResponse
import ru.mercury.vpclient.core.network.response.ResultResponse
import ru.mercury.vpclient.core.network.response.RoutesResponse

suspend fun <T> handleResponse(
    request: suspend () -> BaseResponse<T>,
    onSuccess: suspend (T) -> Unit = {},
    onEmpty: suspend () -> Unit = {},
    onFailure: (suspend (VPClientError) -> Unit)? = null
) {
    runCatching { request() }
        .onSuccess { response ->
            val data = response.data
            val error = response.error
            val errors = response.errors
            when {
                data != null -> {
                    when (data) {
                        is RoutesResponse -> {
                            when {
                                data.routes.isEmpty() -> onEmpty()
                                else -> onSuccess(data)
                            }
                        }
                        is ResultResponse -> {
                            when {
                                data.result == ResultResponse.RESULT_OK -> onSuccess(data)
                                else -> onEmpty()
                            }
                        }
                        else -> onSuccess(data)
                    }
                }
                error != null -> {
                    val message = (error.display ?: error.msg).orEmpty()
                    val error = VPClientError.Http(
                        message = message.ifEmpty { "Ошибка запроса" },
                        httpCode = response.status ?: error.code ?: 0,
                        backendCode = error.code,
                        backendReason = error.reason
                    )
                    when {
                        onFailure != null -> onFailure(error)
                        else -> throw VPClientException(error.message)
                    }
                }
                errors != null -> {
                    val message = errors.values.flatten().joinToString(", ")
                    val error = VPClientError.Http(
                        message = message.ifEmpty { "Ошибка валидации" },
                        httpCode = response.status ?: 422
                    )
                    when {
                        onFailure != null -> onFailure(error)
                        else -> throw VPClientException(error.message)
                    }
                }
                else -> {
                    val error = VPClientError.Unknown("Неизвестная ошибка")
                    when {
                        onFailure != null -> onFailure(error)
                        else -> throw VPClientException(error.message)
                    }
                }
            }
        }
        .onFailure { exception ->
            if (exception !is CancellationException) {
                val error = when {
                    exception.isNetworkRelated -> VPClientError.Network(exception.message.orEmpty(), exception)
                    else -> VPClientError.Unknown(exception.message.orEmpty(), exception)
                }
                when {
                    onFailure != null -> onFailure(error)
                    else -> throw VPClientException(error.message)
                }
            }
        }
}

suspend fun <T> handleResponseResult(
    request: suspend () -> BaseResponse<T>
): Result<T> {
    return try {
        val response = request()
        val data = response.data
        val error = response.error
        val errors = response.errors
        when {
            data != null -> {
                when (data) {
                    is RoutesResponse -> {
                        when {
                            data.routes.isEmpty() -> Result.failure(VPClientEmptyException())
                            else -> Result.success(data)
                        }
                    }
                    is ResultResponse -> {
                        when {
                            data.result == ResultResponse.RESULT_OK -> Result.success(data)
                            else -> Result.failure(VPClientEmptyException())
                        }
                    }
                    else -> Result.success(data)
                }
            }
            error != null -> {
                val message = error.display.orEmpty().ifEmpty { error.msg.orEmpty() }
                Result.failure(VPClientException(message))
            }
            errors != null -> {
                val message = errors.values.flatten().joinToString(", ")
                Result.failure(VPClientException(message))
            }
            else -> {
                val message = "Неизвестная ошибка"
                Result.failure(VPClientException(message))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
