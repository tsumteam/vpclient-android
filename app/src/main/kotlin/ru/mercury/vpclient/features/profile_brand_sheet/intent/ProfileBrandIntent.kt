package ru.mercury.vpclient.features.profile_brand_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileBrandIntent: Intent {
    data object DismissClick: ProfileBrandIntent
    data object CollectBrands: ProfileBrandIntent
    data object LoadBrands: ProfileBrandIntent
    data object SaveClick: ProfileBrandIntent
    data class SearchQueryChange(val query: String): ProfileBrandIntent
    data class BrandClick(val brandId: Int): ProfileBrandIntent
}
