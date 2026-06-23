package ru.mercury.vpclient.shared.domain.interactor

import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse

interface AuthenticationInteractor {

    suspend fun currentUser(): CurrentUserResponse

    suspend fun userId(): String

    suspend fun deleteProfile()

    suspend fun logout()
}
