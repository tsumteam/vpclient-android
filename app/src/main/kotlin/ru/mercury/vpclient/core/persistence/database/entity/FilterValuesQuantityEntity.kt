package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FilterValuesQuantity")
data class FilterValuesQuantityEntity(
    @PrimaryKey val chipId: String,
    val quantity: Int?
) {
    companion object {
        val Empty = FilterValuesQuantityEntity(
            chipId = "",
            quantity = null
        )
    }
}
