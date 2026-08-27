package ru.mercury.vpclient.features.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.banner.navigation.BannerRoute
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.compilation.navigation.CompilationRoute
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.features.gift_card.navigation.GiftCardRoute
import ru.mercury.vpclient.features.home.event.HomeEvent
import ru.mercury.vpclient.features.home.intent.HomeIntent
import ru.mercury.vpclient.features.home.model.HomeModel
import ru.mercury.vpclient.features.home_root.event.HomeRootEventManager
import ru.mercury.vpclient.features.notifications.navigation.NotificationsRoute
import ru.mercury.vpclient.features.search.navigation.SearchRoute
import ru.mercury.vpclient.shared.data.entity.HomePage
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.data.network.type.MainScreenLinkType
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.mapper.catalogRootId
import ru.mercury.vpclient.shared.domain.mapper.mainScreenCategoryType
import ru.mercury.vpclient.shared.domain.mapper.toCatalogLinkData
import ru.mercury.vpclient.shared.domain.usecase.ActivityCounterFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.HomeSectionEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.MainScreenSectionsUseCase
import ru.mercury.vpclient.shared.domain.usecase.MainScreenSectionsUseCase.MainScreenSectionsException
import ru.mercury.vpclient.shared.domain.usecase.SelectedTabFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetLastCatalogRootIdUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val activityCounterFlowUseCase: ActivityCounterFlowUseCase,
    private val homeSectionEntitiesFlowUseCase: HomeSectionEntitiesFlowUseCase,
    private val mainScreenSectionsUseCase: MainScreenSectionsUseCase,
    private val selectedTabFlowUseCase: SelectedTabFlowUseCase,
    private val setLastCatalogRootIdUseCase: SetLastCatalogRootIdUseCase
): ClientViewModel<HomeIntent, HomeModel, HomeEvent>(HomeModel()) {

    init {
        dispatch(HomeIntent.CollectSelectedTab)
        dispatch(HomeIntent.CollectCartCount)
        dispatch(HomeIntent.CollectFittingCount)
        dispatch(HomeIntent.CollectActiveEmployee)
        dispatch(HomeIntent.CollectNotificationCount)
        dispatch(HomeIntent.LoadCartData)
        dispatch(HomeIntent.CollectMainScreenSections)
    }

    override fun dispatch(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.CollectSelectedTab -> {
                launch {
                    selectedTabFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { tab ->
                            val shouldLoad = tab !in stateFlow.value.loadedTabs &&
                                tab !in stateFlow.value.loadMainScreenSectionsJobs
                            reduce { state -> state.copy(selectedTab = tab) }
                            if (shouldLoad) {
                                dispatch(HomeIntent.LoadMainScreenSections(tab))
                            }
                        }
                }
            }
            is HomeIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is HomeIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(fittingCount = count) } }
                }
            }
            is HomeIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee -> reduce { it.copy(activeEmployee = employee) } }
                }
            }
            is HomeIntent.CollectNotificationCount -> {
                launch {
                    activityCounterFlowUseCase(ActivityCounterType.CLIENT_NOTIFICATION)
                        .distinctUntilChanged()
                        .collectLatest { counter -> reduce { it.copy(notificationCount = counter.value) } }
                }
            }
            is HomeIntent.LoadCartData -> {
                launch {
                    val badge = cartBadgeUseCase(Unit).getOrThrow()
                    reduce { it.copy(cartBadge = badge) }
                }
            }
            is HomeIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is HomeIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is HomeIntent.MessengerClick -> {
                launch { MainEventManager.send(CartRoute(navigateToMessenger = true)) }
            }
            is HomeIntent.GiftCardsClick -> launch { MainEventManager.send(GiftCardRoute) }
            is HomeIntent.SearchClick -> launch { MainEventManager.send(SearchRoute()) }
            is HomeIntent.NotificationClick -> launch { MainEventManager.send(NotificationsRoute) }
            is HomeIntent.CollectMainScreenSections -> {
                TabType.entries.forEach { tab ->
                    launch {
                        homeSectionEntitiesFlowUseCase(tab.mainScreenCategoryType)
                            .distinctUntilChanged()
                            .collectLatest { homeSectionEntities ->
                                reduce { state ->
                                    state.copy(
                                        pages = state.pages.map { page ->
                                            when (page.tab) {
                                                tab -> HomePage(
                                                    tab = tab,
                                                    homeSectionEntities = homeSectionEntities
                                                )
                                                else -> page
                                            }
                                        },
                                        loadedTabs = when {
                                            homeSectionEntities.isNotEmpty() -> state.loadedTabs + tab
                                            else -> state.loadedTabs
                                        }
                                    )
                                }
                            }
                    }
                }
            }
            is HomeIntent.LoadMainScreenSections -> {
                when {
                    intent.tab in stateFlow.value.loadMainScreenSectionsJobs -> return
                    else -> {
                        val job = launch {
                            mainScreenSectionsUseCase(intent.tab.mainScreenCategoryType).getOrThrow()
                            reduce { state -> state.copy(loadedTabs = state.loadedTabs + intent.tab) }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { it.copy(loadMainScreenSectionsJobs = it.loadMainScreenSectionsJobs - intent.tab) }
                            }
                        }
                        reduce { state ->
                            state.copy(
                                loadMainScreenSectionsJobs = state.loadMainScreenSectionsJobs + (intent.tab to job)
                            )
                        }
                    }
                }
            }
            is HomeIntent.RefreshMainScreenSections -> {
                when {
                    intent.tab in stateFlow.value.loadMainScreenSectionsJobs -> return
                    else -> {
                        val job = launch {
                            mainScreenSectionsUseCase(intent.tab.mainScreenCategoryType).getOrThrow()
                            reduce { state -> state.copy(loadedTabs = state.loadedTabs + intent.tab) }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { state ->
                                    state.copy(
                                        loadMainScreenSectionsJobs = state.loadMainScreenSectionsJobs - intent.tab,
                                        refreshMainScreenSectionsJobs = state.refreshMainScreenSectionsJobs - intent.tab
                                    )
                                }
                            }
                        }
                        reduce { state ->
                            state.copy(
                                loadMainScreenSectionsJobs = state.loadMainScreenSectionsJobs + (intent.tab to job),
                                refreshMainScreenSectionsJobs = state.refreshMainScreenSectionsJobs + (intent.tab to job)
                            )
                        }
                    }
                }
            }
            is HomeIntent.SelectTab -> {
                val shouldLoad = intent.tab !in stateFlow.value.loadedTabs &&
                    intent.tab !in stateFlow.value.loadMainScreenSectionsJobs
                reduce { state -> state.copy(selectedTab = intent.tab) }
                launch { setLastCatalogRootIdUseCase(intent.tab.catalogRootId).getOrThrow() }
                when {
                    shouldLoad -> dispatch(HomeIntent.LoadMainScreenSections(intent.tab))
                }
            }
            is HomeIntent.ProductClick -> {
                val productId = intent.item.productId ?: return
                launch {
                    HomeRootEventManager.send(
                        DetailsRoute(
                            id = productId,
                            itemId = intent.item.productItemId,
                            colorId = intent.item.productColorId,
                            isHomeRoot = true
                        )
                    )
                }
            }
            is HomeIntent.SectionItemClick -> {
                val catalogLink = intent.item.catalogLink
                when {
                    intent.item.linkType == MainScreenLinkType.BANNER -> {
                        val url = intent.item.bannerLinkUrl ?: return
                        launch { HomeRootEventManager.send(BannerRoute(url)) }
                    }
                    intent.item.linkType == MainScreenLinkType.FASHION_IMAGE -> {
                        val fashionImageId = intent.item.fashionImageId ?: return
                        launch {
                            MainEventManager.send(
                                CompilationRoute(
                                    id = fashionImageId,
                                    title = intent.item.fashionImageTitle.orEmpty(),
                                    isFashionImage = true
                                )
                            )
                        }
                    }
                    catalogLink != null -> {
                        launch {
                            val catalogLinkData = catalogLink.toCatalogLinkData() ?: return@launch
                            val categoryId = catalogLinkData.categoryId
                                ?: catalogLinkData.rootCategoryId
                                ?: return@launch
                            val rootCategoryId = catalogLinkData.rootCategoryId ?: categoryId
                            val route = FilterRoute(
                                categoryId = categoryId,
                                titleCategoryId = rootCategoryId,
                                subtitleCategoryId = categoryId,
                                titleOverride = catalogLinkData.title,
                                isSingleLineTitle = true,
                                initialSelectedFilterValueChips = catalogLinkData.selectedFilterValueChips,
                                hiddenFilterValueChipIds = catalogLinkData.hiddenFilterValueChipIds,
                                viewTypeOverride = catalogLinkData.viewType,
                                isHomeRoot = true
                            )
                            setLastCatalogRootIdUseCase(rootCategoryId).getOrThrow()
                            HomeRootEventManager.send(route)
                        }
                    }
                    else -> return
                }
            }
            is HomeIntent.SectionViewMoreClick -> {
                val catalogLink = intent.section.titleCatalogLink ?: return
                launch {
                    val catalogLinkData = catalogLink.toCatalogLinkData() ?: return@launch
                    val categoryId = catalogLinkData.categoryId
                        ?: catalogLinkData.rootCategoryId
                        ?: return@launch
                    val rootCategoryId = catalogLinkData.rootCategoryId ?: categoryId
                    val route = FilterRoute(
                        categoryId = categoryId,
                        titleCategoryId = rootCategoryId,
                        subtitleCategoryId = categoryId,
                        titleOverride = catalogLinkData.title,
                        isSingleLineTitle = true,
                        initialSelectedFilterValueChips = catalogLinkData.selectedFilterValueChips,
                        hiddenFilterValueChipIds = catalogLinkData.hiddenFilterValueChipIds,
                        viewTypeOverride = catalogLinkData.viewType,
                        isHomeRoot = true
                    )
                    setLastCatalogRootIdUseCase(rootCategoryId).getOrThrow()
                    HomeRootEventManager.send(route)
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is MainScreenSectionsException -> {
                launch { send(HomeEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> launch { send(HomeEvent.SnackbarErrorMessage(throwable.message)) }
            is RoomException, is RoomSQLiteException -> {
                launch { send(HomeEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
