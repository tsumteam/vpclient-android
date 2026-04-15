package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.data.entity.ClientError
import ru.mercury.vpclient.shared.data.error.ClientEmptyException
import ru.mercury.vpclient.shared.data.error.ClientException
import ru.mercury.vpclient.shared.data.network.response.BaseResponse
import ru.mercury.vpclient.shared.data.network.response.DataResponse

suspend fun <T> handleResponse(
    request: suspend () -> BaseResponse<T>,
    onSuccess: suspend (T) -> Unit = {},
    onEmpty: suspend () -> Unit = {},
    onFailure: (suspend (ClientError) -> Unit)? = null
) {
    runCatching { request() }
        .onSuccess { response ->
            val data = response.data
            val error = response.error
            val errors = response.errors
            when {
                data != null -> {
                    when (data) {
                        is DataResponse -> {
                            when {
                                data.data == DataResponse.RESULT_OK -> onSuccess(data)
                                else -> onEmpty()
                            }
                        }
                        else -> onSuccess(data)
                    }
                }
                error != null -> {
                    val message = (error.display ?: error.msg).orEmpty()
                    val error = ClientError.Http(
                        message = message.ifEmpty { "Ошибка запроса" },
                        httpCode = response.status ?: error.code ?: 0,
                        backendCode = error.code,
                        backendReason = error.reason
                    )
                    when {
                        onFailure != null -> onFailure(error)
                        else -> throw ClientException(error.message)
                    }
                }
                errors != null -> {
                    val message = errors.values.flatten().joinToString(", ")
                    val error = ClientError.Http(
                        message = message.ifEmpty { "Ошибка валидации" },
                        httpCode = response.status ?: 422
                    )
                    when {
                        onFailure != null -> onFailure(error)
                        else -> throw ClientException(error.message)
                    }
                }
                else -> {
                    val error = ClientError.Unknown("Неизвестная ошибка")
                    when {
                        onFailure != null -> onFailure(error)
                        else -> throw ClientException(error.message)
                    }
                }
            }
        }
        .onFailure { exception ->
            if (exception !is CancellationException) {
                val error = when {
                    exception.isNetworkRelated -> ClientError.Network(exception.message.orEmpty(), exception)
                    else -> ClientError.Unknown(exception.message.orEmpty(), exception)
                }
                when {
                    onFailure != null -> onFailure(error)
                    else -> throw ClientException(error.message)
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
                    is DataResponse -> {
                        when {
                            data.data == DataResponse.RESULT_OK -> Result.success(data)
                            else -> Result.failure(ClientEmptyException())
                        }
                    }
                    else -> Result.success(data)
                }
            }
            error != null -> {
                val message = error.display.orEmpty().ifEmpty { error.msg.orEmpty() }
                Result.failure(ClientException(message))
            }
            errors != null -> {
                val message = errors.values.flatten().joinToString(", ")
                Result.failure(ClientException(message))
            }
            else -> {
                val message = "Неизвестная ошибка"
                Result.failure(ClientException(message))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
