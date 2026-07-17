package ru.mercury.vpclient.shared.data.persistence.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.GiftCardTemplateEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductButtonEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductOtherColorEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductRelatedItemEntity

// fixme

class Converter {

    private val json = Json

    @TypeConverter
    fun fromJsonObject(value: JsonObject?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toJsonObject(data: String?): JsonObject? {
        return data?.let { value -> json.parseToJsonElement(value).jsonObject }
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toIntList(data: String): List<Int> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromGiftCardTemplateEntityList(list: List<GiftCardTemplateEntity>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toGiftCardTemplateEntityList(data: String): List<GiftCardTemplateEntity> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromCartProductAlternativeList(list: List<CartProductAlternative>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toCartProductAlternativeList(data: String): List<CartProductAlternative> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromCartProductSizeList(list: List<CartProductSize>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toCartProductSizeList(data: String): List<CartProductSize> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromFilterValueItemEntityList(list: List<FilterValueItemEntity>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toFilterValueItemEntityList(data: String): List<FilterValueItemEntity> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromProductOtherColorEntityList(list: List<ProductOtherColorEntity>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toProductOtherColorEntityList(data: String): List<ProductOtherColorEntity> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromProductRelatedItemEntityList(list: List<ProductRelatedItemEntity>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toProductRelatedItemEntityList(data: String): List<ProductRelatedItemEntity> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromProductButtonEntityList(list: List<ProductButtonEntity>): String {
        return json.encodeToString(list)
    }

    @TypeConverter
    fun toProductButtonEntityList(data: String): List<ProductButtonEntity> {
        return when {
            data.isEmpty() -> emptyList()
            else -> json.decodeFromString(data)
        }
    }

    @TypeConverter
    fun fromProductAvailableSizesEntity(entity: ProductAvailableSizesEntity?): String? {
        return entity?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toProductAvailableSizesEntity(data: String?): ProductAvailableSizesEntity? {
        return when {
            data.isNullOrEmpty() -> null
            else -> json.decodeFromString(data)
        }
    }
}
