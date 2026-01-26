package ru.mercury.vpclient.core.persistence.database.converter

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}
