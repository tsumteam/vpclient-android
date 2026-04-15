package ru.mercury.vpclient.shared.domain.mapper

import java.text.NumberFormat
import java.util.Locale

val Int?.orEmpty: Int
    get() = this ?: 0

val Int?.isNotEmpty: Boolean
    get() = this != null && this != 0

val Int.thousandsSeparator: String
    get() {
        val formatter = NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-RU"))
        return formatter.format(this).replace('\u00A0', ' ')
    }

val Long?.orEmpty: Long
    get() = this ?: 0L

val Long?.isNotEmpty: Boolean
    get() = this != null && this != 0L

val Float?.orEmpty: Float
    get() = this ?: 0F

val Float?.isNotEmpty: Boolean
    get() = this != null && this != 0F

val Double?.orEmpty: Double
    get() = this ?: .0

val Double?.isEmpty: Boolean
    get() = this == null || this == .0

val Double?.isNotEmpty: Boolean
    get() = this != null && this != .0

val Boolean?.orEmpty: Boolean
    get() = this ?: false
