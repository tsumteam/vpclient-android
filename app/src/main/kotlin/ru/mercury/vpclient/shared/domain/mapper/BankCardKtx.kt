package ru.mercury.vpclient.shared.domain.mapper

import java.time.YearMonth

val String.isValidBankCardNumber: Boolean
    get() {
        val digits = filter(Char::isDigit)
        if (digits.length !in 12..19) return false
        val sum = digits
            .reversed()
            .mapIndexed { index, char ->
                val digit = char.digitToInt()
                when {
                    index % 2 == 1 -> (digit * 2).let { value -> if (value > 9) value - 9 else value }
                    else -> digit
                }
            }
            .sum()
        return sum % 10 == 0
    }

val String.isValidBankCardExpirationDate: Boolean
    get() {
        val digits = filter(Char::isDigit)
        if (digits.length != 4) return false
        val month = digits.take(2).toIntOrNull() ?: return false
        if (month !in 1..12) return false
        val year = digits.drop(2).toIntOrNull() ?: return false
        return !YearMonth.of(2000 + year, month).isBefore(YearMonth.now())
    }
