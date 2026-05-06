package ru.mercury.vpclient.shared.domain.interactor

import javax.inject.Inject

class Interactor @Inject constructor(
    authenticationInteractor: AuthenticationInteractor,
    cartInteractor: CartInteractor,
    catalogInteractor: CatalogInteractor,
    employeeInteractor: EmployeeInteractor,
    filterInteractor: FilterInteractor,
    productInteractor: ProductInteractor
): AuthenticationInteractor by authenticationInteractor,
    CartInteractor by cartInteractor,
    CatalogInteractor by catalogInteractor,
    FilterInteractor by filterInteractor,
    EmployeeInteractor by employeeInteractor,
    ProductInteractor by productInteractor
