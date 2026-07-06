package ru.mercury.vpclient.shared.domain.mapper

import java.text.NumberFormat
import java.util.Locale

val Int.thousandsSeparator: String
    get() {
        val formatter = NumberFormat.getIntegerInstance(Locale.forLanguageTag("ru-RU"))
        return formatter.format(this).replace('\u00A0', ' ')
    }
