package ru.mercury.vpclient.features.compilation

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.compilation.event.CompilationEvent
import ru.mercury.vpclient.features.compilation.intent.CompilationIntent
import ru.mercury.vpclient.features.compilation.model.CompilationModel
import ru.mercury.vpclient.features.compilation.navigation.CompilationRoute
import ru.mercury.vpclient.features.compilation_actions_sheet.intent.CompilationActionsIntent
import ru.mercury.vpclient.features.compilation_add_to_basket_sheet.intent.CompilationAddToBasketIntent
import ru.mercury.vpclient.features.compilation_benefit_sheet.intent.CompilationBenefitIntent
import ru.mercury.vpclient.features.compilation_cart_added_sheet.intent.CompilationCartAddedIntent
import ru.mercury.vpclient.features.compilation_chat_sheet.intent.CompilationChatIntent
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.details_message_sheet.intent.DetailsMessageIntent
import ru.mercury.vpclient.features.media.navigation.MediaRoute
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartProductsFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogAvailableForMultipleSizesUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogAvailableForMultipleSizesUseCase.CatalogAvailableForMultipleSizesException
import ru.mercury.vpclient.shared.domain.usecase.CatalogFashionImageByIdUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogFashionImageByIdUseCase.CatalogFashionImageByIdException
import ru.mercury.vpclient.shared.domain.usecase.CatalogFilterProductsEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationPreviewPageEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationsClientByIdUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationsClientByIdUseCase.CompilationsClientByIdException
import ru.mercury.vpclient.shared.domain.usecase.CompilationsClientLookByIdToBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationsClientLookByIdToBasketUseCase.CompilationsClientLookByIdToBasketException
import ru.mercury.vpclient.shared.domain.usecase.ToggleBasketProductUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = CompilationViewModel.Factory::class)
class CompilationViewModel @AssistedInject constructor(
    @Assisted private val route: CompilationRoute,
    private val compilationPreviewPageEntitiesFlowUseCase: CompilationPreviewPageEntitiesFlowUseCase,
    private val catalogFilterProductsEntitiesFlowUseCase: CatalogFilterProductsEntitiesFlowUseCase,
    private val compilationsClientByIdUseCase: CompilationsClientByIdUseCase,
    private val catalogFashionImageByIdUseCase: CatalogFashionImageByIdUseCase,
    private val compilationsClientLookByIdToBasketUseCase: CompilationsClientLookByIdToBasketUseCase,
    private val catalogAvailableForMultipleSizesUseCase: CatalogAvailableForMultipleSizesUseCase,
    private val toggleBasketProductUseCase: ToggleBasketProductUseCase,
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val cartProductsFlowUseCase: CartProductsFlowUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase
): ClientViewModel<CompilationIntent, CompilationModel, CompilationEvent>(
    CompilationModel(isFashionImage = route.isFashionImage)
) {

    init {
        if (!route.isFashionImage) {
            dispatch(CompilationIntent.CollectCompilation)
            dispatch(CompilationIntent.CollectCompilationProducts)
        }
        dispatch(CompilationIntent.CollectCartProducts)
        dispatch(CompilationIntent.CollectCartCount)
        dispatch(CompilationIntent.LoadCompilation)
    }

    override fun dispatch(intent: CompilationIntent) {
        when (intent) {
            is CompilationIntent.CollectCompilation -> {
                launch {
                    compilationPreviewPageEntitiesFlowUseCase(route.id).collectLatest { entities ->
                        val selectedLookIndex = when {
                            entities.isEmpty() -> 0
                            stateFlow.value.selectedLookIndex > entities.lastIndex -> entities.lastIndex
                            else -> stateFlow.value.selectedLookIndex
                        }
                        reduce {
                            it.copy(
                                compilationPreviewPageEntities = entities,
                                selectedLookIndex = selectedLookIndex
                            )
                        }
                    }
                }
            }
            is CompilationIntent.CollectCompilationProducts -> {
                launch {
                    catalogFilterProductsEntitiesFlowUseCase(route.id).collectLatest { entities ->
                        reduce { it.copy(compilationPreviewProductEntities = entities) }
                    }
                }
            }
            is CompilationIntent.CollectCartProducts -> {
                launch {
                    cartProductsFlowUseCase(Unit).collectLatest { products ->
                        reduce {
                            it.copy(
                                basketProductKeys = products
                                    .filter { product -> product.itemId.isNotEmpty() && product.colorId.isNotEmpty() }
                                    .map { product -> "${product.itemId}:${product.colorId}:${product.sizeId}" }
                                    .toSet()
                            )
                        }
                    }
                }
            }
            is CompilationIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is CompilationIntent.LoadCompilation -> {
                val job = launch {
                    when {
                        route.isFashionImage -> {
                            val result = catalogFashionImageByIdUseCase(
                                CatalogFashionImageByIdUseCase.Params(
                                    id = route.id,
                                    title = route.title
                                )
                            ).getOrThrow()
                            reduce {
                                it.copy(
                                    compilationPreviewPageEntities = result.pageEntities,
                                    compilationPreviewProductEntities = result.productEntities
                                )
                            }
                        }
                        else -> compilationsClientByIdUseCase(route.id).getOrThrow()
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { it.copy(compilationPreviewJob = null) }
                    }
                }
                reduce { it.copy(compilationPreviewJob = job) }
            }
            is CompilationIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is CompilationIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is CompilationIntent.MenuClick -> {
                reduce { it.copy(isCompilationActionsSheetVisible = true) }
            }
            is CompilationIntent.OnCompilationActionsIntent -> {
                when (intent.intent) {
                    is CompilationActionsIntent.DismissClick -> {
                        reduce { it.copy(isCompilationActionsSheetVisible = false) }
                    }
                    is CompilationActionsIntent.ShowCompilationChatSheet -> {
                        reduce { it.copy(isCompilationActionsSheetVisible = false, isCompilationChatSheetVisible = true) }
                    }
                    is CompilationActionsIntent.ShowAddToBasketDialog -> {
                        dispatch(CompilationIntent.ShowAddToBasketDialog)
                    }
                }
            }
            is CompilationIntent.OnCompilationChatIntent -> {
                when (intent.intent) {
                    is CompilationChatIntent.DismissClick -> {
                        reduce { it.copy(isCompilationChatSheetVisible = false) }
                    }
                    is CompilationChatIntent.SendClick -> return // fixme
                }
            }
            is CompilationIntent.ShowAddToBasketDialog -> {
                if (route.isFashionImage) return
                stateFlow.value.addToBasketAvailableSizesJob?.cancel()
                val productEntities = stateFlow.value.selectedLookAddToBasketProductEntities
                val selectedProductIds = productEntities.map { entity -> entity.id }.toSet()
                reduce {
                    it.copy(
                        isCompilationActionsSheetVisible = false,
                        isCompilationAddToBasketSheetVisible = true,
                        addToBasketDialogSelectedProductIds = selectedProductIds,
                        addToBasketDialogAvailableSizes = emptyMap(),
                        addToBasketDialogOneSizeProductIds = emptySet(),
                        addToBasketAvailableSizesJob = null
                    )
                }
                val job = launch {
                    val sizes = catalogAvailableForMultipleSizesUseCase(productEntities).getOrThrow()
                    reduce {
                        when {
                            it.isCompilationAddToBasketSheetVisible -> {
                                it.copy(
                                    addToBasketDialogAvailableSizes = sizes.associate { size -> size.productId to size.availableSizes },
                                    addToBasketDialogOneSizeProductIds = sizes
                                        .filter { size -> size.oneSize }
                                        .map { size -> size.productId }
                                        .toSet()
                                )
                            }
                            else -> it
                        }
                    }
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { it.copy(addToBasketAvailableSizesJob = null) }
                    }
                }
                reduce { it.copy(addToBasketAvailableSizesJob = job) }
            }
            is CompilationIntent.OnCompilationCartAddedIntent -> {
                when (intent.intent) {
                    is CompilationCartAddedIntent.DismissClick -> {
                        reduce { it.copy(isCompilationCartAddedSheetVisible = false) }
                    }
                    is CompilationCartAddedIntent.CartClick -> {
                        reduce { it.copy(isCompilationCartAddedSheetVisible = false) }
                        launch { MainEventManager.send(CartRoute()) }
                    }
                }
            }
            is CompilationIntent.ShowBenefitSheet -> {
                reduce { it.copy(isCompilationBenefitSheetVisible = true) }
            }
            is CompilationIntent.OnCompilationBenefitIntent -> {
                when (intent.intent) {
                    is CompilationBenefitIntent.DismissClick -> {
                        reduce { it.copy(isCompilationBenefitSheetVisible = false) }
                    }
                }
            }
            is CompilationIntent.OnDetailsMessageIntent -> {
                when (intent.intent) {
                    is DetailsMessageIntent.DismissClick -> {
                        reduce { it.copy(messageSheetProductEntity = null) }
                    }
                    is DetailsMessageIntent.SendClick -> {
                        // fixme
                    }
                }
            }
            is CompilationIntent.PageChange -> {
                val lastIndex = stateFlow.value.compilationPreviewPageEntities.lastIndex
                if (lastIndex < 0) return
                reduce { it.copy(selectedLookIndex = intent.index.coerceIn(0, lastIndex)) }
            }
            is CompilationIntent.ImageClick -> {
                launch {
                    val imageUrls = stateFlow.value.imageUrls
                    if (imageUrls.isNotEmpty()) {
                        MainEventManager.send(
                            MediaRoute(
                                imageUrls = imageUrls,
                                videoUrl = null,
                                initialPage = intent.initialPage.coerceIn(0, imageUrls.lastIndex)
                            )
                        )
                    }
                }
            }
            is CompilationIntent.ProductClick -> {
                launch { MainEventManager.send(DetailsRoute(id = intent.id, openedFromCart = true)) }
            }
            is CompilationIntent.ProductBasketClick -> {
                launch { toggleBasketProductUseCase(intent.productEntity).getOrThrow() }
            }
            is CompilationIntent.ProductMessageClick -> {
                reduce { it.copy(messageSheetProductEntity = intent.productEntity) }
            }
            is CompilationIntent.OnCompilationAddToBasketIntent -> {
                when (intent.intent) {
                    is CompilationAddToBasketIntent.DismissClick -> {
                        stateFlow.value.addToBasketAvailableSizesJob?.cancel()
                        reduce {
                            it.copy(
                                isCompilationAddToBasketSheetVisible = false,
                                addToBasketDialogSelectedProductIds = emptySet(),
                                addToBasketDialogAvailableSizes = emptyMap(),
                                addToBasketDialogOneSizeProductIds = emptySet(),
                                addToBasketAvailableSizesJob = null
                            )
                        }
                    }
                    is CompilationAddToBasketIntent.AddToBasketClick -> {
                        if (stateFlow.value.isAddToBasketLoading) return
                        val page = stateFlow.value.selectedPageEntity ?: return
                        reduce { it.copy(isCompilationAddToBasketSheetVisible = false) }
                        val job = launch {
                            compilationsClientLookByIdToBasketUseCase(page.id).getOrThrow()
                            reduce { it.copy(isCompilationCartAddedSheetVisible = true) }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { it.copy(addToBasketJob = null) }
                            }
                        }
                        reduce { it.copy(addToBasketJob = job) }
                    }
                    is CompilationAddToBasketIntent.AddToBasketProductCheckedChange -> {
                        val selectedIds = when {
                            intent.intent.checked -> stateFlow.value.addToBasketDialogSelectedProductIds + intent.intent.productId
                            else -> stateFlow.value.addToBasketDialogSelectedProductIds - intent.intent.productId
                        }
                        reduce { it.copy(addToBasketDialogSelectedProductIds = selectedIds) }
                    }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CatalogFashionImageByIdException -> {
                reduce { it.copy(compilationPreviewJob = null) }
                launch { send(CompilationEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is CompilationsClientByIdException -> {
                reduce { it.copy(compilationPreviewJob = null) }
                launch { send(CompilationEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is CompilationsClientLookByIdToBasketException -> {
                launch { send(CompilationEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is CatalogAvailableForMultipleSizesException -> {
                reduce { it.copy(addToBasketAvailableSizesJob = null) }
                launch { send(CompilationEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(CompilationEvent.SnackbarErrorMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: CompilationRoute): CompilationViewModel
    }
}
