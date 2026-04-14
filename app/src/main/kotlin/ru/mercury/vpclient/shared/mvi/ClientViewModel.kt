package ru.mercury.vpclient.shared.mvi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ru.mercury.vpclient.shared.coroutines.viewmodel.CoroutineViewModel

abstract class ClientViewModel<I: Intent, S: Model, E: Event>(
    initialState: S
): CoroutineViewModel() {

    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow: StateFlow<S> = _stateFlow

    private val _eventChannel = Channel<E>()
    val eventFlow: Flow<E> = _eventChannel.receiveAsFlow()

    protected fun reduce(reducer: (S) -> S) {
        _stateFlow.update(reducer)
    }

    protected suspend fun send(event: E) {
        _eventChannel.send(event)
    }

    abstract fun dispatch(intent: I)
}
