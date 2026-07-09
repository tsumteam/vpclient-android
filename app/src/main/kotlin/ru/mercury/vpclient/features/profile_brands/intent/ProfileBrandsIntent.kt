package ru.mercury.vpclient.features.profile_brands.intent

import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.entity.FavoriteBrandEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ProfileBrandsIntent: Intent {
    data object CollectSelectedTab: ProfileBrandsIntent
    data object CollectFavoriteBrands: ProfileBrandsIntent
    data object LoadFavoriteBrands: ProfileBrandsIntent
    data object BackClick: ProfileBrandsIntent
    data object AddFavoriteBrandsClick: ProfileBrandsIntent
    data object HideBrandSheet: ProfileBrandsIntent
    data class SelectTab(val tab: TabType): ProfileBrandsIntent
    data class BrandDeleteClick(val entity: FavoriteBrandEntity): ProfileBrandsIntent
}
