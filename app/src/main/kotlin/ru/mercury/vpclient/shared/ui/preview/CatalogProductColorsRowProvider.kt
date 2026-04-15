package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class CatalogProductColorsRowProvider: PreviewParameterProvider<List<String>> {
    override val values: Sequence<List<String>> = sequenceOf(
        listOf(
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png"
        ),
        listOf(
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Bordo.png"
        ),
        listOf(
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Bordo.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Black.png"
        ),
        listOf(
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Bordo.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Black.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Grey.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Green.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png"
        )
    )
}
