package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "Client",
    primaryKeys = ["phone"]
)
data class ClientEntity(
    val phone: String,
    val name: String,
    val codeResendTimer: Long = 0L
) {
    companion object {
        val Empty = ClientEntity(
            phone = "",
            name = "",
            codeResendTimer = 0L
        )
    }
}
