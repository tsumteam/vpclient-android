@file:Suppress("unused")

package ru.mercury.vpclient.core.coroutines.inject

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.core.coroutines.VPClientDispatchers
import ru.mercury.vpclient.core.coroutines.impl.VPClientDispatchersImpl

@Module
@InstallIn(SingletonComponent::class)
interface DispatchersModule {

    @Binds
    fun vpclientDispatchers(impl: VPClientDispatchersImpl): VPClientDispatchers
}
