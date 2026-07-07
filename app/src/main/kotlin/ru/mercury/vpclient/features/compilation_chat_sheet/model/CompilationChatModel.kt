package ru.mercury.vpclient.features.compilation_chat_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity

data class CompilationChatModel(
    val compilationEntity: CompilationEntity,
    val selectedLookTitle: String = "",
    val commentText: String = ""
)
