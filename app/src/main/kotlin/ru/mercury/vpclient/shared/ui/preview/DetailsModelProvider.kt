package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.features.details.model.DetailsModel
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductButtonEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductOtherColorEntity

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
                buttons = listOf(
                    ProductButtonEntity(title = "Женская одежда"),
                    ProductButtonEntity(title = "Куртки")
                ),
                colorImageUrls = listOf(
                    "https://st-m-vpr-s3.vp.ru/cms/98/43/98437c3a-da76-4aaa-87b7-fdf5f4a1fd70.jpg",
                    "https://st-m-vpr-s3.vp.ru/cms/51/f0/51f07d1d-0449-41a7-9e2a-952d85f279a7.jpg"
                ),
                otherColors = listOf(
                    ProductOtherColorEntity(
                        imageUrls = listOf("https://st-m-vpr-s3.vp.ru/cms/98/43/98437c3a-da76-4aaa-87b7-fdf5f4a1fd70.jpg")
                    ),
                    ProductOtherColorEntity(
                        imageUrls = listOf("https://st-m-vpr-s3.vp.ru/cms/51/f0/51f07d1d-0449-41a7-9e2a-952d85f279a7.jpg")
                    )
                ),
                /*wearWithProducts = listOf(
                    ProductRelatedItemEntity(
                        id = "wear_with_1",
                        itemId = "7312451",
                        colorId = "100",
                        name = "Юбка из шерсти",
                        brand = "BRUNELLO CUCINELLI",
                        urlBrandLogo = null,
                        price = 89_900.0,
                        priceWithoutDiscount = 112_400.0,
                        imageUrl = "https://st-m-vpr-s3.vp.ru/cms/98/43/98437c3a-da76-4aaa-87b7-fdf5f4a1fd70.jpg",
                        imageUrls = listOf("https://st-m-vpr-s3.vp.ru/cms/98/43/98437c3a-da76-4aaa-87b7-fdf5f4a1fd70.jpg")
                    ),
                    ProductRelatedItemEntity(
                        id = "wear_with_2",
                        itemId = "7312452",
                        colorId = "200",
                        name = "Кожаные ботильоны",
                        brand = "GIANVITO ROSSI",
                        urlBrandLogo = null,
                        price = 76_500.0,
                        priceWithoutDiscount = null,
                        imageUrl = "https://st-m-vpr-s3.vp.ru/cms/51/f0/51f07d1d-0449-41a7-9e2a-952d85f279a7.jpg",
                        imageUrls = listOf("https://st-m-vpr-s3.vp.ru/cms/51/f0/51f07d1d-0449-41a7-9e2a-952d85f279a7.jpg")
                    )
                ),
                completeSetProducts = listOf(
                    ProductRelatedItemEntity(
                        id = "complete_set_1",
                        itemId = "8452190",
                        colorId = "300",
                        name = "Сумка из зернистой кожи",
                        brand = "VALENTINO",
                        urlBrandLogo = null,
                        price = 154_900.0,
                        priceWithoutDiscount = 179_900.0,
                        imageUrl = "https://st-m-vpr-s3.vp.ru/cms/98/43/98437c3a-da76-4aaa-87b7-fdf5f4a1fd70.jpg",
                        imageUrls = listOf("https://st-m-vpr-s3.vp.ru/cms/98/43/98437c3a-da76-4aaa-87b7-fdf5f4a1fd70.jpg")
                    )
                ),*/
                hasWearWith = true,
                wearWithButtonEnabled = true
            )
        )
    )
}
