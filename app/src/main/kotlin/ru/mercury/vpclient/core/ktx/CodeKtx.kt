package ru.mercury.vpclient.core.ktx

import java.util.Locale

fun formatCodeResendTime(seconds: Int): String {
    val minutes = (seconds / 60).coerceAtLeast(0)
    val remainingSeconds = (seconds % 60).coerceAtLeast(0)
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
}

fun formatPhoneForDisplay(phone: String): String {
    val digits = phone.filter(Char::isDigit)
    if (digits.isEmpty()) return ""

    val result = StringBuilder("+").append(digits[0])

    val part2 = digits.segment(1, 4)
    if (part2.isNotEmpty()) {
        result.append(" ").append(part2)
    }

    val part3 = digits.segment(4, 7)
    if (part3.isNotEmpty()) {
        result.append(" ").append(part3)
    }

    val part4 = digits.segment(7, 9)
    if (part4.isNotEmpty()) {
        result.append("-").append(part4)
    }

    val part5 = digits.segment(9, 11)
    if (part5.isNotEmpty()) {
        result.append("-").append(part5)
    }

    if (digits.length > 11) {
        result.append(" ").append(digits.substring(11))
    }

    return result.toString()
}

private fun String.segment(start: Int, endExclusive: Int): String {
    if (length <= start) return ""
    return substring(start, minOf(endExclusive, length))
}
