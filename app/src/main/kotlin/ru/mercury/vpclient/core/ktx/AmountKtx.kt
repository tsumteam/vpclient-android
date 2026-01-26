package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.Amount
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

val Amount.rub: String
    get() {
        val totalKopecks = (this.toBigDecimal() * BigDecimal(100)).setScale(0, RoundingMode.HALF_UP).toBigInteger()
        val rubles = totalKopecks / BigInteger.valueOf(100)
        val kopecks = totalKopecks % BigInteger.valueOf(100)
        val rublesFormatted = "%,d".format(rubles).replace(',', ' ')
        return if (kopecks == BigInteger.ZERO) rublesFormatted else "%s.%02d".format(rublesFormatted, kopecks.toInt())
    }
