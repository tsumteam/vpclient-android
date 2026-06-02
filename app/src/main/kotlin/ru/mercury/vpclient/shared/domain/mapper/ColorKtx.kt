package ru.mercury.vpclient.shared.domain.mapper

import androidx.compose.ui.graphics.Color

fun String.colorFromHex(fallback: Color): Color {
    val cleanHex = removePrefix("#")
    return when (cleanHex.length) {
        6 -> runCatching { Color(("FF$cleanHex").toLong(16)) }.getOrDefault(fallback)
        8 -> runCatching { Color(cleanHex.toLong(16)) }.getOrDefault(fallback)
        else -> fallback
    }
}
