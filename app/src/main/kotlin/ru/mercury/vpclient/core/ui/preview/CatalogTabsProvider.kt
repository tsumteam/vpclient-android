package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class CatalogTabsProvider: PreviewParameterProvider<List<String>> {
    override val values: Sequence<List<String>> = sequenceOf(
        listOf("Женское", "Мужское", "Детское"),
        emptyList()
    )
}
