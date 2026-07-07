package ru.mercury.vpclient.features.compilations

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.compilation.navigation.CompilationRoute
import ru.mercury.vpclient.features.compilations.event.CompilationsEvent
import ru.mercury.vpclient.features.compilations.intent.CompilationsIntent
import ru.mercury.vpclient.features.compilations.model.CompilationsModel
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import ru.mercury.vpclient.shared.domain.usecase.CartBadgeUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartProductsFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ChangeFittingPaySwitchUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationEntitiesFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationsClientUseCase
import ru.mercury.vpclient.shared.domain.usecase.CompilationsClientUseCase.CompilationsClientException
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadBasketUseCase
import ru.mercury.vpclient.shared.domain.usecase.LoadFittingUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import javax.inject.Inject

@HiltViewModel
class CompilationsViewModel @Inject constructor(
    private val loadBasketUseCase: LoadBasketUseCase,
    private val loadFittingUseCase: LoadFittingUseCase,
    private val cartBadgeUseCase: CartBadgeUseCase,
    private val cartProductsFlowUseCase: CartProductsFlowUseCase,
    private val changeFittingPaySwitchUseCase: ChangeFittingPaySwitchUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val compilationsClientUseCase: CompilationsClientUseCase,
    private val compilationEntitiesFlowUseCase: CompilationEntitiesFlowUseCase
): ClientViewModel<CompilationsIntent, CompilationsModel, CompilationsEvent>(CompilationsModel()) {

    init {
        dispatch(CompilationsIntent.CollectCartCount)
        dispatch(CompilationsIntent.CollectFittingCount)
        dispatch(CompilationsIntent.CollectCartProducts)
        dispatch(CompilationsIntent.CollectActiveEmployee)
        dispatch(CompilationsIntent.CollectCompilationEntities)
        dispatch(CompilationsIntent.LoadCompilationsClient)
    }

    override fun dispatch(intent: CompilationsIntent) {
        when (intent) {
            is CompilationsIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(cartCount = count) }
                        }
                }
            }
            is CompilationsIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count ->
                            reduce { it.copy(fittingCount = count) }
                        }
                }
            }
            is CompilationsIntent.CollectCartProducts -> {
                launch {
                    cartProductsFlowUseCase(Unit).collectLatest { products ->
                        reduce { it.copy(products = products) }
                    }
                }
            }
            is CompilationsIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee -> reduce { it.copy(activeEmployee = employee) } }
                }
            }
            is CompilationsIntent.CollectCompilationEntities -> {
                launch {
                    compilationEntitiesFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { entities ->
                            reduce { it.copy(compilationEntities = entities) }
                        }
                }
            }
            is CompilationsIntent.LoadCompilationsClient -> {
                if (stateFlow.value.isLoading) return
                val job = launch {
                    compilationsClientUseCase(Unit).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { state -> state.copy(compilationsClientJob = null) }
                    }
                }
                reduce { it.copy(compilationsClientJob = job) }
            }
            is CompilationsIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is CompilationsIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is CompilationsIntent.MessengerClick -> return
            is CompilationsIntent.SearchClick -> return
            is CompilationsIntent.CompilationClick -> {
                launch { MainEventManager.send(CompilationRoute(intent.id)) }
            }
            is CompilationsIntent.HideCompilationChatSheet -> {
                reduce { it.copy(selectedCompilationChatEntity = CompilationEntity.Empty) }
            }
            is CompilationsIntent.CompilationChatClick -> {
                val entity = stateFlow.value.compilationEntities.firstOrNull { entity -> entity.id == intent.id } ?: return
                reduce { it.copy(selectedCompilationChatEntity = entity) }
            }
            is CompilationsIntent.CompilationChatSendClick -> return
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CompilationsClientException -> {
                reduce { state -> state.copy(compilationsClientJob = null) }
                launch { send(CompilationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(CompilationsEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is RoomException, is RoomSQLiteException -> {
                launch { send(CompilationsEvent.SnackbarErrorMessage(throwable.message.orEmpty())) }
            }
            else -> super.catch(throwable)
        }
    }
}
