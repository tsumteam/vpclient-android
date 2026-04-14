package ru.mercury.vpclient.shared.repository.inject

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mercury.vpclient.shared.repository.AuthenticationRepository
import ru.mercury.vpclient.shared.repository.CatalogRepository
import ru.mercury.vpclient.shared.repository.EmployeeRepository
import ru.mercury.vpclient.shared.repository.FilterRepository
import ru.mercury.vpclient.shared.repository.ProductRepository
import ru.mercury.vpclient.shared.repository.impl.AuthenticationRepositoryImpl
import ru.mercury.vpclient.shared.repository.impl.CatalogRepositoryImpl
import ru.mercury.vpclient.shared.repository.impl.EmployeeRepositoryImpl
import ru.mercury.vpclient.shared.repository.impl.FilterRepositoryImpl
import ru.mercury.vpclient.shared.repository.impl.ProductRepositoryImpl

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

    @Binds
    fun productRepository(impl: ProductRepositoryImpl): ProductRepository
}
