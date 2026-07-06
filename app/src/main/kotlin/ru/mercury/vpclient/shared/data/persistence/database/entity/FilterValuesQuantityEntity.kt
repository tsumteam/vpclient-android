package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "FilterValuesQuantity",
    primaryKeys = ["chipId"]
)
data class FilterValuesQuantityEntity(
    val chipId: String,
    val quantity: Int?
) {
    companion object {
        val Empty = FilterValuesQuantityEntity(
            chipId = "",
            quantity = null
        )
    }
}
