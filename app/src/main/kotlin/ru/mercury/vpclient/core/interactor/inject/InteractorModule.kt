package ru.mercury.vpclient.core.interactor.inject

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.core.interactor.AuthenticationInteractor
import ru.mercury.vpclient.core.interactor.CatalogInteractor
import ru.mercury.vpclient.core.interactor.EmployeeInteractor
import ru.mercury.vpclient.core.interactor.impl.AuthenticationInteractorImpl
import ru.mercury.vpclient.core.interactor.impl.CatalogInteractorImpl
import ru.mercury.vpclient.core.interactor.impl.EmployeeInteractorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InteractorModule {

    @Binds
    @Singleton
    fun authenticationInteractor(impl: AuthenticationInteractorImpl): AuthenticationInteractor

    @Binds
    @Singleton
    fun catalogInteractor(impl: CatalogInteractorImpl): CatalogInteractor

    @Binds
    @Singleton
    fun employeeInteractor(impl: EmployeeInteractorImpl): EmployeeInteractor
}
