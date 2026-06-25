package ru.mercury.vpclient.shared.domain.mapper

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import ru.mercury.vpclient.shared.data.network.response.BaseResponse
import ru.mercury.vpclient.shared.data.network.response.ErrorResponse

private val baseResponseJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

suspend fun <T> HttpResponse.bodyAsBaseResponse(serializer: KSerializer<T>): BaseResponse<T> {
    val responseText = bodyAsText()
    if (responseText.isNotBlank()) {
        val response = runCatching { baseResponseJson.decodeFromString(BaseResponse.serializer(serializer), responseText) }
        response.getOrNull()?.let { decodedResponse -> return decodedResponse }
        if (status.value < 400) {
            return response.getOrThrow()
        }
    }

    return BaseResponse(
        data = null,
        error = when {
            status.value >= 400 -> ErrorResponse(
                code = status.value,
                display = null,
                msg = null,
                reason = JsonPrimitive(status.description)
            )
            else -> null
        },
        errors = null,
        type = null,
        title = status.description,
        status = status.value,
        traceId = null
    )
}
