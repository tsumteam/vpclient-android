@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.toCatalogFilterProductsEntity
import javax.inject.Inject

class CatalogFashionImageByIdUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<CatalogFashionImageByIdUseCase.Params, CatalogFashionImageByIdUseCase.Result>(dispatchers.io) {

    override suspend fun execute(params: Params): Result {
        var result = Result()
        handleResponse(
            request = { networkService.catalogFashionImageById(params.id) },
            onSuccess = { response ->
                val title = params.title.ifBlank { DEFAULT_TITLE }
                val pageEntities = response.items.orEmpty().mapIndexed { index, item ->
                    CompilationPreviewPageEntity(
                        compilationId = params.id,
                        id = index,
                        position = index,
                        compilationName = title,
                        title = when {
                            response.items.orEmpty().size > 1 -> "$LOOK_TITLE ${index + 1}"
                            else -> ""
                        },
                        imageUrl = item.imageUrl.orEmpty()
                    )
                }
                val productEntities = response.items.orEmpty().flatMapIndexed { pageIndex, item ->
                    item.items.orEmpty().mapIndexed { productIndex, product ->
                        product.toCatalogFilterProductsEntity(
                            position = productIndex,
                            categoryId = params.id,
                            titleCategoryId = pageIndex
                        )
                    }
                }
                result = Result(
                    pageEntities = pageEntities,
                    productEntities = productEntities
                )
            },
            onFailure = { error -> throw CatalogFashionImageByIdException(error.message) }
        )
        return result
    }

    data class Params(
        val id: Int,
        val title: String
    )

    data class Result(
        val pageEntities: List<CompilationPreviewPageEntity> = emptyList(),
        val productEntities: List<CatalogFilterProductsEntity> = emptyList()
    )

    data class CatalogFashionImageByIdException(
        override val message: String
    ): ClientException(message)

    private companion object {
        private const val DEFAULT_TITLE = "Образы"
        private const val LOOK_TITLE = "Образ"
    }
}
