package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.network.response.ProductResponse
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

class ProductEntityProvider3: PreviewParameterProvider<ProductEntity> {
    override val values: Sequence<ProductEntity> = sequenceOf(
        ProductEntity.Empty.copy(
            routeId = "КР-0137320",
            boutiqueId = "0046",
            deliveryId = "ЗД02316904",
            cis = "46807180544945DC0001822653",
            barcode = "0104680718054494215DC0001822653\u001d91EE10\u001d92E+b9KG/7jLEEeikRXmJEWY2DxhcHpM6TbJ98TI7MtfE=",
            itemColorId = "Серый_0940",
            itemId = "6931977",
            itemName = "Брюки",
            itemSizeId = "40",
            lineId = "c76df7gf-3453-43j5-f837-8435h3857483h",
            price = 643500.0,
            priceWithDisc = 643500.0,
            quantity = 1,
            imageUrl = "https://st-cdn.tsum.com/sig/285ef6bfa14fbd81865f5dc189e4054f/width/247/i/d9/7e/aa/6f/38408711-0d2b-4d19-a900-80bc80b98940.jpg",
            nameAlias = "EXBCK-Y089-3N/YLN006",
            submarkName = "MVST"
        ),
        ProductEntity.Empty.copy(
            routeId = "КР-0137320",
            boutiqueId = "0046",
            deliveryId = "ЗД02316904",
            cis = "46807180544945DC0001822653",
            barcode = "0104680718054494215DC0001822653\u001d91EE10\u001d92E+b9KG/7jLEEeikRXmJEWY2DxhcHpM6TbJ98TI7MtfE=",
            itemColorId = "Серый_0940",
            itemId = "6931977",
            itemName = "Брюки",
            itemSizeId = "40",
            lineId = "c76df7gf-3453-43j5-f837-8435h3857483h",
            price = 643500.0,
            priceWithDisc = 643500.0,
            quantity = 1,
            status = ProductResponse.STATUS_DELIVERED,
            imageUrl = "https://st-cdn.tsum.com/sig/285ef6bfa14fbd81865f5dc189e4054f/width/247/i/d9/7e/aa/6f/38408711-0d2b-4d19-a900-80bc80b98940.jpg",
            nameAlias = "EXBCK-Y089-3N/YLN006",
            submarkName = "MVST"
        ),
        ProductEntity.Empty.copy(
            routeId = "КР-0137320",
            boutiqueId = "0046",
            deliveryId = "ЗД02316904",
            cis = "46807180544945DC0001822653",
            barcode = "0104680718054494215DC0001822653\u001d91EE10\u001d92E+b9KG/7jLEEeikRXmJEWY2DxhcHpM6TbJ98TI7MtfE=",
            itemColorId = "Серый_0940",
            itemId = "6931977",
            itemName = "Брюки",
            itemSizeId = "40",
            lineId = "c76df7gf-3453-43j5-f837-8435h3857483h",
            price = 643500.0,
            priceWithDisc = 643500.0,
            quantity = 1,
            status = ProductResponse.STATUS_RETURN,
            imageUrl = "https://st-cdn.tsum.com/sig/285ef6bfa14fbd81865f5dc189e4054f/width/247/i/d9/7e/aa/6f/38408711-0d2b-4d19-a900-80bc80b98940.jpg",
            nameAlias = "EXBCK-Y089-3N/YLN006",
            submarkName = "MVST"
        )
    )
}
