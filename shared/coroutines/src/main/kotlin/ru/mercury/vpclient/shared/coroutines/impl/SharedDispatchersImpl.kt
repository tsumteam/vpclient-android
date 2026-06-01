package ru.mercury.vpclient.shared.coroutines.impl

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers

class SharedDispatchersImpl @Inject constructor(): SharedDispatchers {

    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val immediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
}
