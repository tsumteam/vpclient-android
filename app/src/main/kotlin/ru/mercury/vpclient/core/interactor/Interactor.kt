package ru.mercury.vpclient.core.interactor

import javax.inject.Inject

class Interactor @Inject constructor(
    authenticationInteractor: AuthenticationInteractor,
    catalogInteractor: CatalogInteractor,
    employeeInteractor: EmployeeInteractor
): AuthenticationInteractor by authenticationInteractor,
    CatalogInteractor by catalogInteractor,
    EmployeeInteractor by employeeInteractor
