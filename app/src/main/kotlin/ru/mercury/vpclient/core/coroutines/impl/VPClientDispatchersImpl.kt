package ru.mercury.vpclient.core.coroutines.impl

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.mercury.vpclient.core.coroutines.VPClientDispatchers
import javax.inject.Inject

class VPClientDispatchersImpl @Inject constructor(): VPClientDispatchers {

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val immediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
}
