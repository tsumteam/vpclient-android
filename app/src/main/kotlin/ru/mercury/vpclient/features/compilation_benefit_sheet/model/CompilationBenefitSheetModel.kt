package ru.mercury.vpclient.features.compilation_benefit_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.mapper.compilationBenefitDiscountPrice
import ru.mercury.vpclient.shared.domain.mapper.compilationBenefitFullPrice
import ru.mercury.vpclient.shared.domain.mapper.formatPriceText
import kotlin.math.roundToInt

data class CompilationBenefitSheetModel(
    val productEntities: List<CatalogFilterProductsEntity>
) {
    val fullPriceText: String
        get() {
            val price = productEntities.sumOf { entity -> entity.compilationBenefitFullPrice.roundToInt() }
            return price.formatPriceText()
        }

    val discountPriceText: String
        get() {
            val price = productEntities.sumOf { entity -> entity.compilationBenefitDiscountPrice.roundToInt() }
            return price.formatPriceText()
        }

    val benefitText: String
        get() {
            val price = productEntities.sumOf { entity ->
                (entity.compilationBenefitFullPrice - entity.compilationBenefitDiscountPrice).roundToInt()
            }
            return price.formatPriceText()
        }
}
