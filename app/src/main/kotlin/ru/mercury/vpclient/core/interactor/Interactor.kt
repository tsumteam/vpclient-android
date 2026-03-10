package ru.mercury.vpclient.core.interactor

import javax.inject.Inject

class Interactor @Inject constructor(
    authenticationInteractor: AuthenticationInteractor,
    employeeInteractor: EmployeeInteractor
): AuthenticationInteractor by authenticationInteractor,
    EmployeeInteractor by employeeInteractor
