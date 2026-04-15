package ru.mercury.vpclient.shared.domain.interactor.inject

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.shared.domain.interactor.AuthenticationInteractor
import ru.mercury.vpclient.shared.domain.interactor.CatalogInteractor
import ru.mercury.vpclient.shared.domain.interactor.EmployeeInteractor
import ru.mercury.vpclient.shared.domain.interactor.FilterInteractor
import ru.mercury.vpclient.shared.domain.interactor.ProductInteractor
import ru.mercury.vpclient.shared.domain.interactor.impl.AuthenticationInteractorImpl
import ru.mercury.vpclient.shared.domain.interactor.impl.CatalogInteractorImpl
import ru.mercury.vpclient.shared.domain.interactor.impl.EmployeeInteractorImpl
import ru.mercury.vpclient.shared.domain.interactor.impl.FilterInteractorImpl
import ru.mercury.vpclient.shared.domain.interactor.impl.ProductInteractorImpl
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
    fun filterInteractor(impl: FilterInteractorImpl): FilterInteractor

    @Binds
    @Singleton
    fun employeeInteractor(impl: EmployeeInteractorImpl): EmployeeInteractor

    @Binds
    @Singleton
    fun productInteractor(impl: ProductInteractorImpl): ProductInteractor
}
