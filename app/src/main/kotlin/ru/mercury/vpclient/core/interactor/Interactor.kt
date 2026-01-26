package ru.mercury.vpclient.core.interactor

import javax.inject.Inject

class Interactor @Inject constructor(
    authenticationInteractor: AuthenticationInteractor
): AuthenticationInteractor by authenticationInteractor
