package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.FilterTitleEntity
import ru.mercury.vpclient.shared.data.entity.TopBarState

class TopBarStateProvider: PreviewParameterProvider<TopBarState> {
    override val values: Sequence<TopBarState> = sequenceOf(
        TopBarState.Logo,
        TopBarState.Title(title = "Бренды"),
        TopBarState.Catalog(navigationClick = {}),
        TopBarState.Details(
            navigationClick = {},
            onClick = {},
            entity = BrandEntity.Empty,
            showBrandBox = false
        ),
        TopBarState.Details(
            navigationClick = {},
            onClick = {},
            entity = BrandEntity(brand = "SAINT LAURENT", urlBrandLogo = null),
            showBrandBox = true
        ),
        TopBarState.Category(title = "Пуховики", navigationClick = {}, searchClick = {}),
        TopBarState.Filter(entity = FilterTitleEntity.Empty, onClick = {}, navigationClick = {}, searchClick = {}),
        TopBarState.FilterBrand(
            entity = BrandEntity(brand = "SAINT LAURENT", urlBrandLogo = null),
            navigationClick = {},
            searchClick = {}
        )
    )
}
