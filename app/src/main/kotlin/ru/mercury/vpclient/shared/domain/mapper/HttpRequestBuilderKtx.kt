package ru.mercury.vpclient.shared.domain.mapper

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormBuilder
import io.ktor.client.request.parameter
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName

fun HttpRequestBuilder.appendQueryParameter(
    name: String,
    value: Any?
) {
    when (value) {
        null -> Unit
        is Iterable<*> -> value.filterNotNull().forEach { item ->
            parameter(name, item.asParameterValue())
        }
        else -> parameter(name, value.asParameterValue())
    }
}

fun FormBuilder.appendFormPart(
    name: String,
    value: Any?
) {
    when (value) {
        null -> Unit
        is Iterable<*> -> value.filterNotNull().forEach { item ->
            append(name, item.asParameterValue())
        }
        else -> append(name, value.asParameterValue())
    }
}

fun FormBuilder.appendFilePart(
    name: String,
    value: ByteArray?
) {
    if (value == null) return
    append(
        name,
        value,
        Headers.build {
            append(HttpHeaders.ContentDisposition, "filename=\"$name\"")
        }
    )
}

fun FormBuilder.appendFileParts(
    name: String,
    values: List<ByteArray>?
) {
    values.orEmpty().forEachIndexed { index, value ->
        append(
            name,
            value,
            Headers.build {
                append(HttpHeaders.ContentDisposition, "filename=\"${name}-${index}\"")
            }
        )
    }
}

private fun Any.asParameterValue(): String {
    return when (this) {
        is Enum<*> -> enumParameterValue(this)
        else -> toString()
    }
}

private fun enumParameterValue(value: Enum<*>): String {
    return value.javaClass.getField(value.name).getAnnotation(SerialName::class.java)?.value ?: value.name
}
