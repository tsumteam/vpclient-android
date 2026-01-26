package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ru.mercury.vpclient.core.BoutiqueId
import ru.mercury.vpclient.core.RouteId

@Entity(
    tableName = "boutiques",
    primaryKeys = ["boutiqueId"],
    foreignKeys = [
        ForeignKey(
            entity = RouteEntity::class,
            parentColumns = ["routeId"],
            childColumns = ["routeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routeId")]
)
data class BoutiqueEntity(
    val routeId: RouteId,
    val routeStatus: String,
    val boutiqueId: BoutiqueId,
    val boutiqueName: String,

    val position: Int
) {
    companion object {
        val Empty = BoutiqueEntity(
            routeId = "",
            routeStatus = "",
            boutiqueId = "",
            boutiqueName = "",
            position = 0
        )
    }
}
