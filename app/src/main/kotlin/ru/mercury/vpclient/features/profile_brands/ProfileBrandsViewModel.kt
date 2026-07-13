package ru.mercury.vpclient.features.profile_brands

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.profile_brands.event.ProfileBrandsEvent
import ru.mercury.vpclient.features.profile_brands.intent.ProfileBrandsIntent
import ru.mercury.vpclient.features.profile_brands.model.ProfileBrandsModel
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandFavoritesUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandFavoritesUseCase.CatalogBrandFavoritesException
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandUnlikeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandUnlikeUseCase.CatalogBrandUnlikeException
import ru.mercury.vpclient.shared.domain.usecase.FavoriteBrandEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.SelectedTabFlowUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import javax.inject.Inject

@HiltViewModel
class ProfileBrandsViewModel @Inject constructor(
    private val selectedTabFlowUseCase: SelectedTabFlowUseCase,
    private val favoriteBrandEntitiesFlowUseCase: FavoriteBrandEntitiesFlowUseCase,
    private val catalogBrandFavoritesUseCase: CatalogBrandFavoritesUseCase,
    private val catalogBrandUnlikeUseCase: CatalogBrandUnlikeUseCase
): ClientViewModel<ProfileBrandsIntent, ProfileBrandsModel, ProfileBrandsEvent>(ProfileBrandsModel()) {

    init {
        dispatch(ProfileBrandsIntent.CollectSelectedTab)
        dispatch(ProfileBrandsIntent.CollectFavoriteBrands)
        dispatch(ProfileBrandsIntent.LoadFavoriteBrands)
    }

    override fun dispatch(intent: ProfileBrandsIntent) {
        when (intent) {
            is ProfileBrandsIntent.CollectSelectedTab -> {
                launch {
                    selectedTabFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { tab -> reduce { it.copy(selectedTab = tab) } }
                }
            }
            is ProfileBrandsIntent.CollectFavoriteBrands -> {
                launch {
                    favoriteBrandEntitiesFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { entities -> reduce { it.copy(favoriteBrandEntities = entities) } }
                }
            }
            is ProfileBrandsIntent.LoadFavoriteBrands -> {
                val job = launch {
                    catalogBrandFavoritesUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state -> state.copy(loadFavoriteBrandsJob = null) }
                    }
                }
                reduce { it.copy(loadFavoriteBrandsJob = job) }
            }
            is ProfileBrandsIntent.BackClick -> launch { MainEventManager.send(BackRoute) }
            is ProfileBrandsIntent.AddFavoriteBrandsClick -> {
                reduce { it.copy(isBrandSheetVisible = true) }
            }
            is ProfileBrandsIntent.HideBrandSheet -> {
                reduce { it.copy(isBrandSheetVisible = false) }
            }
            is ProfileBrandsIntent.SelectTab -> reduce { it.copy(selectedTab = intent.tab) }
            is ProfileBrandsIntent.BrandDeleteClick -> {
                launch {
                    val params = CatalogBrandUnlikeUseCase.Params(
                        brandId = intent.entity.brandId,
                        categoryId = intent.entity.categoryId
                    )
                    catalogBrandUnlikeUseCase(params).getOrThrow()
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CatalogBrandFavoritesException -> {
                reduce { state -> state.copy(loadFavoriteBrandsJob = null) }
                launch { send(ProfileBrandsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is CatalogBrandUnlikeException -> {
                launch { send(ProfileBrandsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(ProfileBrandsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(ProfileBrandsEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
