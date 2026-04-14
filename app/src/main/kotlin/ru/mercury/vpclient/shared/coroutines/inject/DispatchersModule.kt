@file:Suppress("unused")

package ru.mercury.vpclient.shared.coroutines.inject

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.shared.coroutines.ClientDispatchers
import ru.mercury.vpclient.shared.coroutines.impl.ClientDispatchersImpl

@Module
@InstallIn(SingletonComponent::class)
interface DispatchersModule {

    @Binds
    fun clientDispatchers(impl: ClientDispatchersImpl): ClientDispatchers
}
