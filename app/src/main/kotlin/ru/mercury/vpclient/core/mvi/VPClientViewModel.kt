package ru.mercury.vpclient.core.mvi

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import ru.mercury.vpclient.core.coroutines.viewmodel.CoroutineViewModel

abstract class VPClientViewModel<I: Intent, S: Model>(
    initialState: S
): CoroutineViewModel() {

    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow: StateFlow<S> = _stateFlow

    private val _eventChannel = Channel<Any>()
    val eventFlow: Flow<Any> = _eventChannel.receiveAsFlow()

    protected fun reduce(reducer: (S) -> S) {
        _stateFlow.update(reducer)
    }

    protected suspend fun push(event: Any) {
        _eventChannel.send(event)
    }

    abstract fun dispatch(intent: I)
}
