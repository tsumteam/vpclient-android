package ru.mercury.vpclient.shared.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface SharedDispatchers {
    val io: CoroutineDispatcher
    val immediate: CoroutineDispatcher
}
