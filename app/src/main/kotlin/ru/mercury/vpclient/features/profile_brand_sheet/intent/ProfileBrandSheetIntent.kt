package ru.mercury.vpclient.features.profile_brand_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileBrandSheetIntent: Intent {
    data object DismissRequest: ProfileBrandSheetIntent
    data object CollectBrands: ProfileBrandSheetIntent
    data object LoadBrands: ProfileBrandSheetIntent
    data object SaveClick: ProfileBrandSheetIntent
    data class SearchQueryChange(val query: String): ProfileBrandSheetIntent
    data class BrandClick(val brandId: Int): ProfileBrandSheetIntent
}
