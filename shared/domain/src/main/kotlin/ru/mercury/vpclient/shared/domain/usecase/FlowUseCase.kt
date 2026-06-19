package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {

    operator fun invoke(params: P): Flow<R> {
        return execute(params).flowOn(coroutineDispatcher)
    }

    protected abstract fun execute(parameters: P): Flow<R>
}
