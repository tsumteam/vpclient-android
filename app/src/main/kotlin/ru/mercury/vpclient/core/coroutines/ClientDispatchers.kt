package ru.mercury.vpclient.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface ClientDispatchers {
    val io: CoroutineDispatcher
    val immediate: CoroutineDispatcher
}
