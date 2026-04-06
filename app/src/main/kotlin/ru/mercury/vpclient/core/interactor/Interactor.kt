package ru.mercury.vpclient.core.interactor

import javax.inject.Inject

class Interactor @Inject constructor(
    authenticationInteractor: AuthenticationInteractor,
    catalogInteractor: CatalogInteractor,
    employeeInteractor: EmployeeInteractor,
    filterInteractor: FilterInteractor,
    productInteractor: ProductInteractor
): AuthenticationInteractor by authenticationInteractor,
    CatalogInteractor by catalogInteractor,
    FilterInteractor by filterInteractor,
    EmployeeInteractor by employeeInteractor,
    ProductInteractor by productInteractor
