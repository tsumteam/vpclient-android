package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class UseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(params: P): Result<R> {
        return try {
            withContext(coroutineDispatcher) {
                Result.success(execute(params))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: P): R
}
