package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "CompilationPreviewPage",
    primaryKeys = ["compilationId", "id"]
)
data class CompilationPreviewPageEntity(
    val compilationId: Int,
    val id: Int,
    val position: Int,
    val compilationName: String,
    val title: String,
    val imageUrl: String
) {
    companion object {
        val Empty = CompilationPreviewPageEntity(
            compilationId = 0,
            id = 0,
            position = 0,
            compilationName = "",
            title = "",
            imageUrl = ""
        )
    }
}
