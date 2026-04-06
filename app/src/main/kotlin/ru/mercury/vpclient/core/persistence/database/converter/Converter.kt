package ru.mercury.vpclient.core.persistence.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import ru.mercury.vpclient.core.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.core.persistence.database.entity.ProductRelatedItemEntity

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
