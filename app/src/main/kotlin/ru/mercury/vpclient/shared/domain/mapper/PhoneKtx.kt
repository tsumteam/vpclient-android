package ru.mercury.vpclient.shared.domain.mapper

private const val MIN_PHONE_LENGTH = 5
private const val MAX_PHONE_LENGTH = 13

fun normalizePhoneInput(raw: String, maxDigits: Int = MAX_PHONE_LENGTH): String {
    val digits = raw.filter(Char::isDigit).take(maxDigits)
    if (digits.isEmpty()) return ""
    val normalizedFirst = if (digits.first() == '8') '7' else digits.first()
    return buildString(digits.length) {
        append(normalizedFirst)
        append(digits.drop(1))
    }
}

fun String.isValidPhoneNumber(): Boolean {
    val normalized = normalizePhoneInput(this)
    return normalized.length in MIN_PHONE_LENGTH..MAX_PHONE_LENGTH
}
