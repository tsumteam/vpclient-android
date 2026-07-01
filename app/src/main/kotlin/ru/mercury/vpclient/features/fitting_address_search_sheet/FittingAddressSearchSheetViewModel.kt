package ru.mercury.vpclient.features.fitting_address_search_sheet

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.fitting_address_search_sheet.intent.FittingAddressSearchSheetIntent
import ru.mercury.vpclient.features.fitting_address_search_sheet.model.FittingAddressSearchModel
import ru.mercury.vpclient.shared.domain.usecase.AddressSuggestionUseCase
import ru.mercury.vpclient.shared.domain.usecase.AddressSuggestionUseCase.Companion.ADDRESS_SEARCH_DEBOUNCE_MS
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@HiltViewModel(assistedFactory = FittingAddressSearchSheetViewModel.Factory::class)
class FittingAddressSearchSheetViewModel @AssistedInject constructor(
    @Assisted private val initialQuery: String,
    private val addressSuggestionUseCase: AddressSuggestionUseCase
): ClientViewModel<FittingAddressSearchSheetIntent, FittingAddressSearchModel, Event>(FittingAddressSearchModel()) {

    init {
        dispatch(FittingAddressSearchSheetIntent.CollectInitialQuery)
        dispatch(FittingAddressSearchSheetIntent.CollectAddressSuggestions)
    }

    override fun dispatch(intent: FittingAddressSearchSheetIntent) {
        when (intent) {
            is FittingAddressSearchSheetIntent.CollectInitialQuery -> {
                reduce {
                    it.copy(
                        query = initialQuery,
                        suggestions = emptyList(),
                        isSuggestionsLoading = false,
                        lastSearchQuery = null
                    )
                }
            }
            is FittingAddressSearchSheetIntent.CollectAddressSuggestions -> {
                launch {
                    stateFlow
                        .map { it.query.trim() }
                        .distinctUntilChanged()
                        .debounce(ADDRESS_SEARCH_DEBOUNCE_MS.milliseconds)
                        .collectLatest { query ->
                            val state = stateFlow.value
                            when {
                                query.isBlank() -> {
                                    reduce { it.copy(
                                        suggestions = emptyList(),
                                        isSuggestionsLoading = false,
                                        lastSearchQuery = null
                                    ) }
                                }
                                query == state.lastSearchQuery -> Unit
                                else -> {
                                    reduce {
                                        when (it.query.trim()) {
                                            query -> it.copy(isSuggestionsLoading = true)
                                            else -> it
                                        }
                                    }
                                    try {
                                        val suggestions = addressSuggestionUseCase(query).getOrThrow()
                                        reduce {
                                            when (it.query.trim()) {
                                                query -> it.copy(
                                                    suggestions = suggestions,
                                                    isSuggestionsLoading = false,
                                                    lastSearchQuery = query
                                                )
                                                else -> it
                                            }
                                        }
                                    } catch (throwable: Throwable) {
                                        if (throwable is CancellationException) {
                                            throw throwable
                                        }
                                        reduce { it.copy(isSuggestionsLoading = false) }
                                        catch(throwable)
                                    }
                                }
                            }
                        }
                }
            }
            is FittingAddressSearchSheetIntent.QueryChange -> {
                reduce {
                    when {
                        intent.value.isBlank() -> {
                            it.copy(
                                query = intent.value,
                                suggestions = emptyList(),
                                isSuggestionsLoading = false,
                                lastSearchQuery = null
                            )
                        }
                        else -> it.copy(query = intent.value)
                    }
                }
            }
            else -> Unit
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(initialQuery: String): FittingAddressSearchSheetViewModel
    }
}
