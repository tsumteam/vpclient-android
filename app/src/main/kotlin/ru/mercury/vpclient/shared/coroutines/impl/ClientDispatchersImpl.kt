package ru.mercury.vpclient.shared.coroutines.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.mercury.vpclient.shared.coroutines.ClientDispatchers
import javax.inject.Inject

class ClientDispatchersImpl @Inject constructor(): ClientDispatchers {

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val immediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
}
