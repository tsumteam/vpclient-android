package ru.mercury.vpclient.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import ru.mercury.vpclient.core.network.request.AuthenticationContinueLoginRequest
import ru.mercury.vpclient.core.network.request.AuthenticationLoginRequest
import ru.mercury.vpclient.core.network.request.AuthenticationRegisterRequest
import ru.mercury.vpclient.core.network.response.BaseResponse
import ru.mercury.vpclient.core.network.response.TokenResponse
import javax.inject.Inject

class NetworkService @Inject constructor(
    private val ktorHttpClient: HttpClient
) {

    suspend fun authenticationRegister(
        request: AuthenticationRegisterRequest
    ): BaseResponse<String> {
        return ktorHttpClient.post("authentication/register") {
            setBody(request)
        }.body()
    }

    suspend fun authenticationLogin(
        request: AuthenticationLoginRequest
    ): BaseResponse<String> {
        return ktorHttpClient.post("authentication/login") {
            setBody(request)
        }.body()
    }

    suspend fun authenticationContinueLogin(
        request: AuthenticationContinueLoginRequest
    ): BaseResponse<TokenResponse> {
        return ktorHttpClient.post("authentication/continue-login") {
            setBody(request)
        }.body()
    }

    suspend fun authenticationLogout(): BaseResponse<String> {
        return ktorHttpClient.post("authentication/logout").body()
    }
}
