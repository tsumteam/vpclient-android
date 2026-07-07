package ru.mercury.vpclient.features.compilation.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationPreviewPageEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductAvailableSizesEntity
import ru.mercury.vpclient.shared.domain.mapper.compilationBenefitDiscountPrice
import ru.mercury.vpclient.shared.domain.mapper.compilationBenefitFullPrice
import ru.mercury.vpclient.shared.mvi.Model
import kotlin.math.roundToInt

data class CompilationModel(
    val compilationPreviewPageEntities: List<CompilationPreviewPageEntity> = emptyList(),
    val compilationPreviewProductEntities: List<CatalogFilterProductsEntity> = emptyList(),
    val selectedLookIndex: Int = 0,
    val basketProductKeys: Set<String> = emptySet(),
    val addToBasketDialogSelectedProductIds: Set<String> = emptySet(),
    val addToBasketDialogAvailableSizes: Map<String, ProductAvailableSizesEntity> = emptyMap(),
    val addToBasketDialogOneSizeProductIds: Set<String> = emptySet(),
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val messageSheetProductEntity: CatalogFilterProductsEntity? = null,
    val isCompilationActionsSheetVisible: Boolean = false,
    val isCompilationChatSheetVisible: Boolean = false,
    val isCompilationAddToBasketSheetVisible: Boolean = false,
    val isCompilationCartAddedSheetVisible: Boolean = false,
    val isCompilationBenefitSheetVisible: Boolean = false,
    val compilationPreviewJob: Job? = null,
    val cartBadgeJob: Job? = null,
    val addToBasketJob: Job? = null,
    val addToBasketAvailableSizesJob: Job? = null
): Model {

    val isLoading: Boolean
        get() = compilationPreviewPageEntities.isEmpty()

    val isAddToBasketLoading: Boolean
        get() = addToBasketJob?.isActive == true

    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val isMessageSheetVisible: Boolean
        get() = messageSheetProductEntity != null

    val selectedPageEntity: CompilationPreviewPageEntity?
        get() = compilationPreviewPageEntities.getOrNull(selectedLookIndex)

    val compilationName: String
        get() = selectedPageEntity?.compilationName
            ?: compilationPreviewPageEntities.firstOrNull()?.compilationName.orEmpty()

    val selectedLookTitle: String
        get() = selectedPageEntity?.title.orEmpty().uppercase()

    val selectedLookNumberText: String
        get() = when {
            compilationPreviewPageEntities.size <= 1 -> ""
            else -> "${selectedLookIndex + 1}/${compilationPreviewPageEntities.size}"
        }

    val selectedLookProductEntities: List<CatalogFilterProductsEntity>
        get() {
            val lookId = selectedPageEntity?.id ?: return emptyList()
            return compilationPreviewProductEntities.filter { entity -> entity.titleCategoryId == lookId }
        }

    val selectedLookAddToBasketProductEntities: List<CatalogFilterProductsEntity>
        get() = selectedLookProductEntities.filter { entity ->
            entity.id.isNotBlank() &&
                entity.itemId.isNotBlank() &&
                entity.brand.isNotBlank()
        }

    val isPromotionVisible: Boolean
        get() = selectedLookActionProductEntities.isNotEmpty()

    val selectedLookPromotionName: String
        get() = selectedLookProductEntities
            .firstNotNullOfOrNull { entity -> entity.lookActionName?.takeIf { name -> name.isNotBlank() } }
            .orEmpty()

    val selectedLookPromotionNameText: String
        get() = when {
            selectedLookPromotionName.isBlank() -> ""
            else -> " «$selectedLookPromotionName»"
        }

    val selectedLookBenefitAmountText: String
        get() = selectedLookBenefitAmount.formatPriceText()

    private val selectedLookActionProductEntities: List<CatalogFilterProductsEntity>
        get() = selectedLookProductEntities.filter { entity ->
            entity.lookActionPrice != null && entity.lookActionPriceWithoutDiscount != null
        }

    private val selectedLookBenefitAmount: Int
        get() = selectedLookActionProductEntities.sumOf { entity ->
            (entity.compilationBenefitFullPrice - entity.compilationBenefitDiscountPrice).roundToInt()
        }

    val compilationChatEntity: CompilationEntity
        get() {
            val pageEntity = selectedPageEntity ?: compilationPreviewPageEntities.firstOrNull() ?: return CompilationEntity.Empty
            return CompilationEntity.Empty.copy(
                id = pageEntity.compilationId,
                photoUrl = pageEntity.imageUrl,
                name = pageEntity.compilationName,
                looksQty = compilationPreviewPageEntities.size,
                lookProductsQty = compilationPreviewProductEntities.size
            )
        }

    val imageUrls: List<String>
        get() = compilationPreviewPageEntities.map { entity -> entity.imageUrl }

    fun isProductInBasket(entity: CatalogFilterProductsEntity): Boolean {
        return "${entity.itemId}:${entity.colorId}:" in basketProductKeys ||
            "${entity.itemId}:${entity.colorId}:NS" in basketProductKeys
    }
}

private fun Int.formatPriceText(): String {
    return "%,d ₽".format(this).replace(',', ' ')
}
