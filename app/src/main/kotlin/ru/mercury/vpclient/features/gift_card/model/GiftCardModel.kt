package ru.mercury.vpclient.features.gift_card.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.entity.GiftCardAmountModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardTemplateEntity
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.rubles
import ru.mercury.vpclient.shared.mvi.Model

data class GiftCardModel(
    val giftCardEntity: GiftCardEntity? = null,
    val selectedTemplateIndex: Int = 0,
    val amountText: String = "",
    val isAmountErrorVisible: Boolean = false,
    val isBuyEnabled: Boolean = false,
    val isTermsVisible: Boolean = false,
    val cartBadge: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty,
    val loadGiftCardJob: Job? = null
): Model {

    val isLoading: Boolean
        get() = giftCardEntity == null && loadGiftCardJob?.isActive == true

    val templates: List<GiftCardTemplateEntity>
        get() = giftCardEntity?.templates.orEmpty()

    val selectedTemplate: GiftCardTemplateEntity
        get() = templates.getOrElse(selectedTemplateIndex) { GiftCardTemplateEntity.Empty }

    val minAmountText: String
        get() = giftCardEntity?.minAmount?.rubles.orEmpty()

    val maxAmountText: String
        get() = giftCardEntity?.maxAmount?.rubles.orEmpty()

    val presetAmounts: List<GiftCardAmountModel>
        get() = giftCardEntity?.presetAmounts.orEmpty().map { amount ->
            GiftCardAmountModel(
                amount = amount,
                text = amount.rubles
            )
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge
}
