package ru.mercury.vpclient.shared.data.persistence.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductOtherColorEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductRelatedItemEntity

// fixme

class Converter {

    private val json = Json

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
