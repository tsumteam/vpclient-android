package ru.mercury.vpclient.core.repository.inject

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.core.repository.AuthenticationRepository
import ru.mercury.vpclient.core.repository.CatalogRepository
import ru.mercury.vpclient.core.repository.EmployeeRepository
import ru.mercury.vpclient.core.repository.FilterRepository
import ru.mercury.vpclient.core.repository.impl.AuthenticationRepositoryImpl
import ru.mercury.vpclient.core.repository.impl.CatalogRepositoryImpl
import ru.mercury.vpclient.core.repository.impl.EmployeeRepositoryImpl
import ru.mercury.vpclient.core.repository.impl.FilterRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun authenticationRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    fun catalogRepository(impl: CatalogRepositoryImpl): CatalogRepository

    @Binds
    fun filterRepository(impl: FilterRepositoryImpl): FilterRepository

    @Binds
    fun employeeRepository(impl: EmployeeRepositoryImpl): EmployeeRepository
}
