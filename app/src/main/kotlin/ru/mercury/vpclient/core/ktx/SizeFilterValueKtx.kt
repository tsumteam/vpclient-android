package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.entity.SizeCountry
import ru.mercury.vpclient.core.entity.SizeFilterValue

// fixme

fun SizeFilterValue.displayLabel(country: SizeCountry): String? {
    return when (country) {
        SizeCountry.Russia -> labelRu
        SizeCountry.Italy -> labelItalian
        SizeCountry.France -> labelFrench
        SizeCountry.International -> labelInternational
    }
}
