@file:Suppress("unused")

package ru.mercury.vpclient.shared.coroutines.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.coroutines.impl.SharedDispatchersImpl

@Module
@InstallIn(SingletonComponent::class)
interface DispatchersModule {

    @Binds
    fun sharedDispatchers(impl: SharedDispatchersImpl): SharedDispatchers
}
