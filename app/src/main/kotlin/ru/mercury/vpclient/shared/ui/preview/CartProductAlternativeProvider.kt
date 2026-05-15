package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative

class CartProductAlternativeProvider: PreviewParameterProvider<CartProductAlternative> {
    override val values: Sequence<CartProductAlternative> = sequenceOf(
        CartProductAlternative(
            id = "1",
            detailId = "1",
            brand = "BALMAIN",
            urlBrandLogo = null,
            price = "580 000 ₽",
            imageUrl = "",
            isOriginal = true
        ),
        CartProductAlternative(
            id = "2",
            detailId = "2",
            brand = "DOLCE&GABBANA",
            urlBrandLogo = null,
            price = "1 900 000 ₽",
            imageUrl = "",
            isOriginal = false
        ),
        CartProductAlternative(
            id = "3",
            detailId = "3",
            brand = "MVST",
            urlBrandLogo = null,
            price = "800 000 ₽",
            imageUrl = "",
            isOriginal = false
        )
    )
}
