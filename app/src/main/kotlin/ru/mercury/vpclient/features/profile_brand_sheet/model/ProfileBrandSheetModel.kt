package ru.mercury.vpclient.features.profile_brand_sheet.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.mvi.Model

data class ProfileBrandSheetModel(
    val catalogBrandEntities: List<CatalogBrandEntity> = emptyList(),
    val sections: List<ProfileBrandSection> = emptyList(),
    val selectedBrandIds: Set<Int> = emptySet(),
    val initialSelectedBrandIds: Set<Int> = emptySet(),
    val searchQuery: String = "",
    val loadBrandsJob: Job? = null,
    val saveFavoriteBrandsJob: Job? = null
): Model {

    val isLoading: Boolean
        get() = loadBrandsJob?.isActive == true

    val isSaving: Boolean
        get() = saveFavoriteBrandsJob?.isActive == true

    val isEmptySearchVisible: Boolean
        get() = searchQuery.isNotBlank() && sections.isEmpty()

    val isAlphabetVisible: Boolean
        get() = searchQuery.isBlank() && sections.isNotEmpty()

    data class ProfileBrandSection(
        val letter: String,
        val catalogBrandEntities: List<CatalogBrandEntity>
    )
}
