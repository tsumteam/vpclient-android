package ru.mercury.vpclient.shared.domain.mapper

import java.text.NumberFormat
import java.util.Locale
import ru.mercury.vpclient.shared.data.FORMAT_RUB

val Int.thousandsSeparator: String
    get() {
        val formatter = NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-RU"))
        return formatter.format(this).replace('\u00A0', ' ')
    }

val Int.rubles: String
    get() = FORMAT_RUB.format(thousandsSeparator)
