package ru.mercury.vpclient.features.main

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.main.intent.MainIntent
import ru.mercury.vpclient.features.main.model.MainModel
import ru.mercury.vpclient.shared.data.network.type.ActivityCounterType
import ru.mercury.vpclient.shared.domain.usecase.ActivityCounterFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ActivityCountersUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.mvi.Event
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val activityCounterFlowUseCase: ActivityCounterFlowUseCase,
    private val activityCountersUseCase: ActivityCountersUseCase
): ClientViewModel<MainIntent, MainModel, Event>(MainModel()) {

    init {
        dispatch(MainIntent.CollectActivityCounters)
        dispatch(MainIntent.LoadActivityCounters)
    }

    override fun dispatch(intent: MainIntent) {
        when (intent) {
            is MainIntent.CollectActivityCounters -> {
                launch {
                    activityCounterFlowUseCase(ActivityCounterType.MESSAGES)
                        .distinctUntilChanged()
                        .collectLatest { counter ->
                            reduce { it.copy(consultantsBadge = counter.value) }
                        }
                }
            }
            is MainIntent.LoadActivityCounters -> launch { activityCountersUseCase(Unit) }
            is MainIntent.SelectTab -> reduce { it.copy(selectedRoute = intent.route) }
        }
    }
}
