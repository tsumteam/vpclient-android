package ru.mercury.vpclient.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import ru.mercury.vpclient.core.network.request.AuthenticationContinueLoginRequest
import ru.mercury.vpclient.core.network.request.AuthenticationLoginRequest
import ru.mercury.vpclient.core.network.request.AuthenticationRegisterRequest
import javax.inject.Inject

class NetworkService @Inject constructor(
    private val ktorHttpClient: HttpClient
) {

    suspend fun authenticationRegister(
        request: AuthenticationRegisterRequest
    ): String {
        return ktorHttpClient.post("/api/authentication/register") {
            setBody(request)
        }.bodyAsText()
    }

    suspend fun authenticationLogin(
        request: AuthenticationLoginRequest
    ): String {
        return ktorHttpClient.post("/api/authentication/login") {
            setBody(request)
        }.bodyAsText()
    }

    suspend fun authenticationContinueLogin(
        request: AuthenticationContinueLoginRequest
    ): String {
        return ktorHttpClient.post("/api/authentication/continue-login") {
            setBody(request)
        }.bodyAsText()
    }

    suspend fun authenticationLogout() {
        return ktorHttpClient.post("/api/authentication/logout").body()
    }
}
