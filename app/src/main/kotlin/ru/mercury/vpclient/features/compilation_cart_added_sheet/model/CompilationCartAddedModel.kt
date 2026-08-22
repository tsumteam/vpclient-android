package ru.mercury.vpclient.features.compilation_cart_added_sheet.model

import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity
import ru.mercury.vpclient.shared.mvi.Model

data class CompilationCartAddedModel(
    val pageEntity: CompilationPreviewPageEntity
): Model
