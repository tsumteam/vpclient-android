package ru.mercury.vpclient.shared.domain.mapper

import kotlin.jvm.JvmName
import ru.mercury.vpclient.shared.data.network.response.GiftCardResponseItemResponse
import ru.mercury.vpclient.shared.data.network.response.GiftCardTemplateResponseItemResponse
import ru.mercury.vpclient.shared.data.network.type.GiftCardType
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardTemplateEntity

val GiftCardResponseItemResponse.entity: GiftCardEntity
    get() = GiftCardEntity(
        id = id.orEmpty,
        itemId = itemId.orEmpty(),
        type = type ?: GiftCardType.NONE,
        maxAmount = maxAmount.orEmpty.toInt(),
        minAmount = minAmount.orEmpty.toInt(),
        defaultAmount = defaultAmount.orEmpty.toInt(),
        presetAmounts = presetAmounts.orEmpty().map { amount -> amount.toInt() },
        templates = templates.orEmpty().entities.sortedBy { template -> template.orderView }
    )

val GiftCardTemplateResponseItemResponse.entity: GiftCardTemplateEntity
    get() = GiftCardTemplateEntity(
        id = id.orEmpty,
        templateId = templateId.orEmpty,
        name = name.orEmpty(),
        description = description.orEmpty(),
        photoUrl = photoUrl.orEmpty(),
        termOfUse = termOfUse.orEmpty(),
        orderView = orderView.orEmpty
    )

@get:JvmName("getGiftCardEntities")
val List<GiftCardResponseItemResponse>.entities: List<GiftCardEntity>
    get() = map { response -> response.entity }

@get:JvmName("getGiftCardTemplateEntities")
val List<GiftCardTemplateResponseItemResponse>.entities: List<GiftCardTemplateEntity>
    get() = map { response -> response.entity }
