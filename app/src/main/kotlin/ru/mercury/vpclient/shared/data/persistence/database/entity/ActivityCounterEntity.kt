package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ActivityCounter")
data class ActivityCounterEntity(
    @PrimaryKey val type: String,
    val value: Int
)
