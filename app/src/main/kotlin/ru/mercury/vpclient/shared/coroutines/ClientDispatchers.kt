package ru.mercury.vpclient.shared.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface ClientDispatchers {
    val io: CoroutineDispatcher
    val immediate: CoroutineDispatcher
}
