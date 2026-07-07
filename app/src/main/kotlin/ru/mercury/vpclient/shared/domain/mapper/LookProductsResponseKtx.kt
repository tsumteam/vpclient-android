package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import ru.mercury.vpclient.shared.data.network.response.CatalogProductSearchCardResponse
import ru.mercury.vpclient.shared.data.network.response.LookProductsResponse
import ru.mercury.vpclient.shared.data.network.response.LookProductsResponseItemResponse
import ru.mercury.vpclient.shared.data.network.response.LooksResponseItemDtoResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity

fun LookProductsResponse?.pageEntity(
    compilationId: Int,
    compilationName: String,
    lookId: Int,
    position: Int,
    fallback: LooksResponseItemDtoResponse
): CompilationPreviewPageEntity {
    val lookInfo = this?.lookInfo ?: fallback

    return CompilationPreviewPageEntity(
        compilationId = compilationId,
        id = lookId,
        position = position,
        compilationName = compilationName,
        title = lookInfo.name.orEmpty(),
        imageUrl = lookInfo.photoUrl.orEmpty().ifEmpty { lookInfo.collageUrl.orEmpty() }
    )
}

fun LookProductsResponse?.productEntities(
    compilationId: Int,
    lookId: Int
): List<CatalogFilterProductsEntity> {
    return this?.products.orEmpty().mapIndexedNotNull { index, productElement ->
        val wrappedProduct = productElement.toLookProductsResponseItemResponse()
        val product = wrappedProduct?.searchCard
            ?: productElement.toCatalogProductSearchCardResponse()
            ?: return@mapIndexedNotNull null
        product.toCatalogFilterProductsEntity(
            position = index,
            categoryId = compilationId,
            titleCategoryId = lookId,
            lookAction = wrappedProduct?.action
        )
    }
}

private fun JsonElement.toLookProductsResponseItemResponse(): LookProductsResponseItemResponse? {
    return runCatching {
        lookProductsJson.decodeFromJsonElement<LookProductsResponseItemResponse>(this)
    }.getOrNull()
}

private fun JsonElement.toCatalogProductSearchCardResponse(): CatalogProductSearchCardResponse? {
    val product = runCatching {
        lookProductsJson.decodeFromJsonElement<CatalogProductSearchCardResponse>(this)
    }.getOrNull()

    return product?.takeIf { it.id.isNullOrEmpty().not() }
}

private val lookProductsJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}
