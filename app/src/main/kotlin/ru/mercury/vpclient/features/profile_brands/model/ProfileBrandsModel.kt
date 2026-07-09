package ru.mercury.vpclient.features.profile_brands.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.domain.usecase.FavoriteBrandEntitiesFlowUseCase.FavoriteBrandEntities
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

data class ProfileBrandsModel(
    val selectedTab: TabType = TabType.WOMAN,
    val favoriteBrandEntities: FavoriteBrandEntities = FavoriteBrandEntities(),
    val loadFavoriteBrandsJob: Job? = null,
    val isBrandSheetVisible: Boolean = false
): Model {

    val isLoading: Boolean
        get() = loadFavoriteBrandsJob?.isActive == true

    val buttonTextRes: Int
        get() {
            val entities = when (selectedTab) {
                TabType.WOMAN -> favoriteBrandEntities.womanEntities
                TabType.MAN -> favoriteBrandEntities.manEntities
                TabType.CHILD -> favoriteBrandEntities.childEntities
            }
            return when {
                entities.isEmpty() -> ClientStrings.ProfileBrandsAddFavoriteBrands
                else -> ClientStrings.ProfileBrandsChangeFavoriteBrands
            }
        }
}
