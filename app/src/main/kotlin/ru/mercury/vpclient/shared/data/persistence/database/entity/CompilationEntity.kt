package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "Compilation",
    primaryKeys = ["id"]
)
data class CompilationEntity(
    val id: Int,
    val position: Int,
    val collageUrl: String,
    val photoUrl: String,
    val name: String,
    val description: String,
    val createDate: String,
    val looksQty: Int,
    val lookProductsQty: Int,
    val badge: Int,
    val isStatsAvailable: Boolean
) {
    companion object {
        val Empty = CompilationEntity(
            id = 0,
            position = 0,
            collageUrl = "",
            photoUrl = "",
            name = "",
            description = "",
            createDate = "",
            looksQty = 0,
            lookProductsQty = 0,
            badge = 0,
            isStatsAvailable = false
        )
    }
}
