package ru.mercury.vpclient.core.persistence.database.converter

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
}
