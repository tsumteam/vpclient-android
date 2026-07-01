package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.request.CatalogBrandFavoriteRequest
import ru.mercury.vpclient.shared.data.persistence.database.dao.FilterValuesDao
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class ToggleBrandFavoriteUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val filterValuesDao: FilterValuesDao,
    dispatchers: SharedDispatchers
): UseCase<ToggleBrandFavoriteUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pickerChipId = params.chipId.substringBefore("_")
        val entity = filterValuesDao.select(pickerChipId)
        val updatedEntity = entity?.copy(
            items = entity.items.map { item ->
                if (item.requestValue == params.brandId.toString()) item.copy(isFavorite = params.isFavorite) else item
            }
        )
        if (updatedEntity != null) {
            filterValuesDao.upsert(updatedEntity)
        }
        handleResponse(
            request = {
                val request = CatalogBrandFavoriteRequest(
                    brandId = params.brandId,
                    categoryId = params.categoryId
                )
                if (params.isFavorite) networkService.catalogBrandsLike(request)
                else networkService.catalogBrandsUnlike(request)
            },
            onFailure = {
                if (entity != null) {
                    filterValuesDao.upsert(entity)
                }
            }
        )
    }

    data class Params(
        val chipId: String,
        val brandId: Int,
        val categoryId: Int,
        val isFavorite: Boolean
    )
}
