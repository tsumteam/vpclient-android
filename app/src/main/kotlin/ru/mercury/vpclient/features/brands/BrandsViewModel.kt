package ru.mercury.vpclient.features.brands

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.brand_root.event.BrandRootEventManager
import ru.mercury.vpclient.features.brands.event.BrandsEvent
import ru.mercury.vpclient.features.brands.intent.BrandsIntent
import ru.mercury.vpclient.features.brands.model.BrandsModel
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.BrandsPage
import ru.mercury.vpclient.shared.data.entity.BrandsSection
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.network.type.CatalogViewType
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandFavoriteUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandFavoriteUseCase.CatalogBrandFavoriteException
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandFavoritesUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandFavoritesUseCase.CatalogBrandFavoritesException
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandsUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandsUseCase.CatalogBrandsException
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class BrandsViewModel @Inject constructor(
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val catalogBrandEntitiesFlowUseCase: CatalogBrandEntitiesFlowUseCase,
    private val catalogBrandsUseCase: CatalogBrandsUseCase,
    private val catalogBrandFavoriteUseCase: CatalogBrandFavoriteUseCase,
    private val catalogBrandFavoritesUseCase: CatalogBrandFavoritesUseCase
): ClientViewModel<BrandsIntent, BrandsModel, BrandsEvent>(BrandsModel()) {

    init {
        dispatch(BrandsIntent.CollectBrands)
        dispatch(BrandsIntent.CollectCartCount)
        dispatch(BrandsIntent.CollectFittingCount)
        dispatch(BrandsIntent.CollectActiveEmployee)
        dispatch(BrandsIntent.LoadBrands)
    }

    override fun dispatch(intent: BrandsIntent) {
        when (intent) {
            is BrandsIntent.CollectBrands -> {
                TabType.entries.forEach { tab ->
                    launch {
                        catalogBrandEntitiesFlowUseCase(tab.value)
                            .distinctUntilChanged()
                            .collectLatest { entities ->
                                val favoriteBrandEntities = entities.filter { entity -> entity.isFavorite }
                                val topBrandEntities = entities.filter { entity -> entity.isTopBrand && !entity.isFavorite }
                                val sections = entities
                                    .groupBy { entity ->
                                        val firstChar = entity.name.firstOrNull()?.uppercaseChar()
                                        when (firstChar) {
                                            in 'A'..'Z' -> firstChar.toString()
                                            else -> "#"
                                        }
                                    }
                                    .entries
                                    .sortedWith(
                                        compareBy<Map.Entry<String, List<CatalogBrandEntity>>> { entry -> entry.key != "#" }
                                            .thenBy { entry -> entry.key }
                                    )
                                    .map { entry ->
                                        BrandsSection(
                                            letter = entry.key,
                                            catalogBrandEntities = entry.value
                                        )
                                    }
                                val page = BrandsPage(
                                    tab = tab,
                                    catalogBrandEntities = entities,
                                    favoriteBrandEntities = favoriteBrandEntities,
                                    topBrandEntities = topBrandEntities,
                                    sections = sections
                                )
                                reduce { state ->
                                    val pages = state.pages.map { currentPage ->
                                        when (currentPage.tab) {
                                            tab -> page
                                            else -> currentPage
                                        }
                                    }
                                    val favoriteBrandsCount = page.favoriteBrandEntities.size
                                    return@reduce when (state.selectedTab) {
                                        tab -> state.copy(
                                            pages = pages,
                                            favoriteBrandsText = when {
                                                favoriteBrandsCount > 0 -> favoriteBrandsCount.toString()
                                                else -> ""
                                            },
                                            isFavoriteBrandsButtonVisible = favoriteBrandsCount > 0
                                        )
                                        else -> state.copy(pages = pages)
                                    }
                                }
                            }
                    }
                }
            }
            is BrandsIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is BrandsIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(fittingCount = count) } }
                }
            }
            is BrandsIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee -> reduce { it.copy(activeEmployee = employee) } }
                }
            }
            is BrandsIntent.LoadBrands -> {
                val job = launch {
                    catalogBrandsUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state -> state.copy(loadBrandsJob = null) }
                    }
                }
                reduce { it.copy(loadBrandsJob = job) }
            }
            is BrandsIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is BrandsIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is BrandsIntent.MessengerClick -> return
            is BrandsIntent.FavoriteBrandsClick -> {
                launch {
                    val state = stateFlow.value
                    val page = state.pages.first { page -> page.tab == state.selectedTab }
                    when {
                        page.favoriteBrandEntities.isEmpty() -> return@launch
                    }
                    val route = FilterRoute(
                        categoryId = state.selectedTab.value,
                        titleCategoryId = state.selectedTab.value,
                        subtitleCategoryId = state.selectedTab.value,
                        isFavoriteBrands = true,
                        initialSelectedFilterValueChips = page.favoriteBrandEntities.map { entity ->
                            FilterChip(
                                id = "${CatalogFilterRequest.BRAND}_${entity.brandId}",
                                label = entity.name
                            )
                        },
                        viewTypeOverride = CatalogViewType.TEXT_SEARCH,
                        isBrandRoot = true
                    )
                    BrandRootEventManager.send(route)
                }
            }
            is BrandsIntent.SearchClick -> return
            is BrandsIntent.SelectTab -> {
                val page = stateFlow.value.pages.first { page -> page.tab == intent.tab }
                val favoriteBrandsCount = page.favoriteBrandEntities.size
                reduce {
                    it.copy(
                        selectedTab = intent.tab,
                        favoriteBrandsText = when {
                            favoriteBrandsCount > 0 -> favoriteBrandsCount.toString()
                            else -> ""
                        },
                        isFavoriteBrandsButtonVisible = favoriteBrandsCount > 0
                    )
                }
            }
            is BrandsIntent.BrandClick -> {
                launch {
                    val brandEntity = BrandEntity(
                        brand = intent.entity.name,
                        urlBrandLogo = intent.entity.photoUrl
                    )
                    val route = FilterRoute(
                        categoryId = intent.entity.categoryId,
                        titleCategoryId = intent.entity.categoryId,
                        subtitleCategoryId = intent.entity.categoryId,
                        brandEntity = brandEntity,
                        hiddenFilterValueChipIds = listOf(
                            "${CatalogFilterRequest.BRAND}_${intent.entity.brandId}"
                        ),
                        viewTypeOverride = CatalogViewType.BRAND,
                        isBrandRoot = true
                    )
                    BrandRootEventManager.send(route)
                }
            }
            is BrandsIntent.FavoriteClick -> {
                val brandId = intent.entity.brandId
                when {
                    brandId in stateFlow.value.saveFavoriteBrandJobs -> return
                    else -> {
                        val job = launch {
                            val params = CatalogBrandFavoriteUseCase.Params(
                                brandId = brandId,
                                categoryId = intent.entity.categoryId,
                                isFavorite = !intent.entity.isFavorite
                            )
                            catalogBrandFavoriteUseCase(params).getOrThrow()
                            catalogBrandFavoritesUseCase(Unit).getOrThrow()
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { state ->
                                    state.copy(saveFavoriteBrandJobs = state.saveFavoriteBrandJobs - brandId)
                                }
                            }
                        }
                        reduce { state ->
                            state.copy(saveFavoriteBrandJobs = state.saveFavoriteBrandJobs + (brandId to job))
                        }
                    }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CatalogBrandsException -> {
                reduce { it.copy(loadBrandsJob = null) }
                launch { send(BrandsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is CatalogBrandFavoriteException, is CatalogBrandFavoritesException -> {
                launch { send(BrandsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(BrandsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(BrandsEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
