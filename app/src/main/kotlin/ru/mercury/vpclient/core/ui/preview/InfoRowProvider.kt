package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

class InfoRowProvider: PreviewParameterProvider<ProductEntity> {
    override val values: Sequence<ProductEntity> = sequenceOf(
        ProductEntity.Empty.copy(
            id = "preview-1",
            brand = "Jil Sander",
            colorName = "БЕЛЫЙ (Белый_102)",
            productionStructure = "Хлопок-100%",
            country = "Италия",
            itemId = "7015929",
            article = "J03GC0134/J20244",
            technicalDescription = "Параметры изделия для размера S: Обхват груди 100 см, длина рукава от горловины 37 см, длина изделия по спинке 67 см."
        )
    )
}
