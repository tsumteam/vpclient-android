package ru.mercury.vpclient.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface VPClientDispatchers {
    val io: CoroutineDispatcher
    val immediate: CoroutineDispatcher
}
