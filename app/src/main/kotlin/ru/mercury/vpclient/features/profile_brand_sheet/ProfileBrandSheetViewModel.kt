package ru.mercury.vpclient.features.profile_brand_sheet

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.profile_brand_sheet.event.ProfileBrandEvent
import ru.mercury.vpclient.features.profile_brand_sheet.intent.ProfileBrandIntent
import ru.mercury.vpclient.features.profile_brand_sheet.model.ProfileBrandModel
import ru.mercury.vpclient.features.profile_brand_sheet.model.ProfileBrandModel.ProfileBrandSection
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandFavoritesUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandFavoritesUseCase.CatalogBrandFavoritesException
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandsSaveFavoritesUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandsSaveFavoritesUseCase.CatalogBrandsSaveFavoritesException
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandsUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogBrandsUseCase.CatalogBrandsException
import ru.mercury.vpclient.shared.mvi.ClientViewModel

@HiltViewModel(assistedFactory = ProfileBrandSheetViewModel.Factory::class)
class ProfileBrandSheetViewModel @AssistedInject constructor(
    @Assisted private val categoryId: Int,
    private val catalogBrandEntitiesFlowUseCase: CatalogBrandEntitiesFlowUseCase,
    private val catalogBrandsUseCase: CatalogBrandsUseCase,
    private val catalogBrandsSaveFavoritesUseCase: CatalogBrandsSaveFavoritesUseCase,
    private val catalogBrandFavoritesUseCase: CatalogBrandFavoritesUseCase
): ClientViewModel<ProfileBrandIntent, ProfileBrandModel, ProfileBrandEvent>(ProfileBrandModel()) {

    init {
        dispatch(ProfileBrandIntent.CollectBrands)
        dispatch(ProfileBrandIntent.LoadBrands)
    }

    override fun dispatch(intent: ProfileBrandIntent) {
        when (intent) {
            is ProfileBrandIntent.DismissClick -> {
                launch { send(ProfileBrandEvent.Dismiss) }
            }
            is ProfileBrandIntent.CollectBrands -> {
                launch {
                    catalogBrandEntitiesFlowUseCase(categoryId)
                        .distinctUntilChanged()
                        .collectLatest { entities ->
                            val selectedBrandIds = entities
                                .filter { entity -> entity.isFavorite }
                                .map { entity -> entity.brandId }
                                .toSet()
                            val searchQuery = stateFlow.value.searchQuery
                            val filteredEntities = when {
                                searchQuery.isBlank() -> entities
                                else -> entities.filter { entity -> entity.name.contains(searchQuery, ignoreCase = true) }
                            }
                            val sections = filteredEntities
                                .sortedBy { entity -> entity.name.lowercase() }
                                .groupBy { entity ->
                                    val firstChar = entity.name.firstOrNull()?.uppercaseChar()
                                    when {
                                        firstChar?.isLetter() == true -> firstChar.toString()
                                        else -> "#"
                                    }
                                }
                                .entries
                                .sortedWith(
                                    compareBy<Map.Entry<String, List<CatalogBrandEntity>>> { entry -> entry.key == "#" }
                                        .thenBy { entry -> entry.key }
                                )
                                .map { entry ->
                                    ProfileBrandSection(
                                        letter = entry.key,
                                        catalogBrandEntities = entry.value
                                    )
                                }
                            reduce { state ->
                                state.copy(
                                    catalogBrandEntities = entities,
                                    sections = sections,
                                    selectedBrandIds = selectedBrandIds,
                                    initialSelectedBrandIds = selectedBrandIds
                                )
                            }
                        }
                }
            }
            is ProfileBrandIntent.LoadBrands -> {
                val job = launch {
                    catalogBrandsUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state -> state.copy(loadBrandsJob = null) }
                    }
                }
                reduce { it.copy(loadBrandsJob = job) }
            }
            is ProfileBrandIntent.SaveClick -> {
                val state = stateFlow.value
                val addedBrandIds = state.selectedBrandIds - state.initialSelectedBrandIds
                val removedBrandIds = state.initialSelectedBrandIds - state.selectedBrandIds
                when {
                    state.isSaving -> return
                    addedBrandIds.isEmpty() && removedBrandIds.isEmpty() -> {
                        launch { send(ProfileBrandEvent.Dismiss) }
                    }
                    else -> {
                        val job = launch {
                            val params = CatalogBrandsSaveFavoritesUseCase.Params(
                                categoryId = categoryId,
                                addedBrandIds = addedBrandIds,
                                removedBrandIds = removedBrandIds
                            )
                            catalogBrandsSaveFavoritesUseCase(params).getOrThrow()
                            catalogBrandFavoritesUseCase(Unit).getOrThrow()
                            send(ProfileBrandEvent.Dismiss)
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { currentState -> currentState.copy(saveFavoriteBrandsJob = null) }
                            }
                        }
                        reduce { it.copy(saveFavoriteBrandsJob = job) }
                    }
                }
            }
            is ProfileBrandIntent.SearchQueryChange -> {
                val catalogBrandEntities = stateFlow.value.catalogBrandEntities
                val filteredEntities = when {
                    intent.query.isBlank() -> catalogBrandEntities
                    else -> catalogBrandEntities.filter { entity -> entity.name.contains(intent.query, ignoreCase = true) }
                }
                val sections = filteredEntities
                    .sortedBy { entity -> entity.name.lowercase() }
                    .groupBy { entity ->
                        val firstChar = entity.name.firstOrNull()?.uppercaseChar()
                        when {
                            firstChar?.isLetter() == true -> firstChar.toString()
                            else -> "#"
                        }
                    }
                    .entries
                    .sortedWith(
                        compareBy<Map.Entry<String, List<CatalogBrandEntity>>> { entry -> entry.key == "#" }
                            .thenBy { entry -> entry.key }
                    )
                    .map { entry ->
                        ProfileBrandSection(
                            letter = entry.key,
                            catalogBrandEntities = entry.value
                        )
                    }
                reduce {
                    it.copy(
                        searchQuery = intent.query,
                        sections = sections
                    )
                }
            }
            is ProfileBrandIntent.BrandClick -> {
                val selectedBrandIds = stateFlow.value.selectedBrandIds
                val updatedBrandIds = when (intent.brandId) {
                    in selectedBrandIds -> selectedBrandIds - intent.brandId
                    else -> selectedBrandIds + intent.brandId
                }
                reduce { it.copy(selectedBrandIds = updatedBrandIds) }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CatalogBrandsException -> {
                reduce { it.copy(loadBrandsJob = null) }
                launch { send(ProfileBrandEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is CatalogBrandsSaveFavoritesException, is CatalogBrandFavoritesException -> {
                reduce { it.copy(saveFavoriteBrandsJob = null) }
                launch { send(ProfileBrandEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(ProfileBrandEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(ProfileBrandEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(categoryId: Int): ProfileBrandSheetViewModel
    }
}
