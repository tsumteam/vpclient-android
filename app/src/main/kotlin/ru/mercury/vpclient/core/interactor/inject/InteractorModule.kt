@file:Suppress("unused")

package ru.mercury.vpclient.core.interactor.inject

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.core.interactor.AuthenticationInteractor
import ru.mercury.vpclient.core.interactor.impl.AuthenticationInteractorImpl

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    fun authenticationInteractor(impl: AuthenticationInteractorImpl): AuthenticationInteractor
}
