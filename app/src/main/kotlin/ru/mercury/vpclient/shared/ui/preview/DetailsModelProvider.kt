package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.features.details.model.DetailsModel

class DetailsModelProvider: PreviewParameterProvider<DetailsModel> {
    override val values: Sequence<DetailsModel> = sequenceOf(
        DetailsModel(),
        DetailsModel(
            productEntity = ProductEntity.Empty.copy(
                id = "preview",
                name = "Куртка из кожи",
                price = 129_900.0,
                itemId = "5558447",
                brand = "SAINT LAURENT",
                article = "BRG-CARVE-STEER",
                longDescription = "Куртка прямого кроя с лаконичной отделкой и мягкой фактурой.",
                productionStructure = "натуральная кожа 100%",
                country = "Италия",
                technicalDescription = "Длина изделия 62 см, длина рукава 64 см.",
                breadcrumbs = listOf("Каталог", "Женское", "Одежда", "Куртки"),
                colorImageUrls = listOf(
                    "https://st-m-vpr-s3.vp.ru/cms/98/43/98437c3a-da76-4aaa-87b7-fdf5f4a1fd70.jpg",
                    "https://st-m-vpr-s3.vp.ru/cms/51/f0/51f07d1d-0449-41a7-9e2a-952d85f279a7.jpg"
                ),
                otherColorImageUrls = listOf(
                    "https://st-m-vpr-s3.vp.ru/cms/98/43/98437c3a-da76-4aaa-87b7-fdf5f4a1fd70.jpg",
                    "https://st-m-vpr-s3.vp.ru/cms/51/f0/51f07d1d-0449-41a7-9e2a-952d85f279a7.jpg"
                )
            )
        )
    )
}
