package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "ActivityCounter",
    primaryKeys = ["type"]
)
data class ActivityCounterEntity(
    val type: String,
    val value: Int
)
