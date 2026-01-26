package ru.mercury.vpclient.core.network.inject

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.network.sockets.SocketTimeoutException
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import ru.mercury.vpclient.BuildConfig
import ru.mercury.vpclient.core.network.env.VPClientEnvironment
import ru.mercury.vpclient.core.network.provideLoggingInterceptor
import ru.mercury.vpclient.core.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.core.persistence.datastore.SettingsDataStore
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideKtorHttpClient(
        @ApplicationContext context: Context,
        settingsDataStore: Provider<SettingsDataStore>
    ): HttpClient {
        val ktor = HttpClient(OkHttp) {
            defaultRequest {
                val vpclientEnvironment = when {
                    BuildConfig.DEBUG -> {
                        val vpclientEnvironmentName = settingsDataStore.get().getValueBlocking(PreferenceKey.Environment)
                        if (vpclientEnvironmentName.isNullOrEmpty()) VPClientEnvironment.BCA else VPClientEnvironment.valueOf(vpclientEnvironmentName)
                    }
                    else -> VPClientEnvironment.PROD
                }
                url(vpclientEnvironment.url)

                header("X-ApiKey", BuildConfig.VP_VPCLIENT_API_KEY)

                val applicationType = settingsDataStore.get().getValueBlocking(PreferenceKey.ApplicationType).orEmpty().ifEmpty { DEFAULT_APPLICATION_TYPE }
                header("X-ApplicationType", applicationType)

                val pairedUser = settingsDataStore.get().getValueBlocking(PreferenceKey.PairedUser).orEmpty()
                if (pairedUser.isNotEmpty()) {
                    header("X-PairedUser", pairedUser)
                }

                val userToken = settingsDataStore.get().getValueBlocking(PreferenceKey.UserToken).orEmpty()
                if (userToken.isNotEmpty()) {
                    header("X-UserToken", userToken)
                    header("Authorization", "Bearer $userToken")
                }

                val deviceId = settingsDataStore.get().getValueBlocking(PreferenceKey.DeviceId).orEmpty().ifEmpty { DEFAULT_DEVICE_ID }
                if (deviceId.isNotEmpty()) {
                    header("X-DeviceId", deviceId)
                }

                contentType(ContentType.Application.Json)
            }
            install(ContentNegotiation) {
                val json = Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
                json(json)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = if (BuildConfig.DEBUG) DEBUG_REQUEST_TIMEOUT_MILLIS else REQUEST_TIMEOUT_MILLIS
                connectTimeoutMillis = if (BuildConfig.DEBUG) DEBUG_CONNECT_TIMEOUT_MILLIS else CONNECT_TIMEOUT_MILLIS
                socketTimeoutMillis = if (BuildConfig.DEBUG) DEBUG_SOCKET_TIMEOUT_SECONDS else SOCKET_TIMEOUT_SECONDS
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            install(HttpCallValidator) {
                handleResponseExceptionWithRequest { cause, request ->
                    val url = request.url.toString().substringBefore("?")
                    Unit
                }
            }
            engine {
                clientCacheSize = HTTP_CACHE_SIZE_BYTES
                config {
                    val chuckerCollector = ChuckerCollector(
                        context = context,
                        showNotification = BuildConfig.DEBUG,
                        retentionPeriod = RetentionManager.Period.FOREVER
                    )
                    val chuckerBuilder = ChuckerInterceptor.Builder(context)
                        .collector(chuckerCollector)
                        .createShortcut(true)
                    addInterceptor(chuckerBuilder.build())
                    if (BuildConfig.DEBUG) {
                        addInterceptor(provideLoggingInterceptor())
                    }
                }
            }
        }
        return ktor
    }

    private const val REQUEST_TIMEOUT_MILLIS = 10_000L
    private const val SOCKET_TIMEOUT_SECONDS = 10_000L
    private const val CONNECT_TIMEOUT_MILLIS = 5_000L

    private const val DEBUG_REQUEST_TIMEOUT_MILLIS = 10_000L
    private const val DEBUG_SOCKET_TIMEOUT_SECONDS = 10_000L
    private const val DEBUG_CONNECT_TIMEOUT_MILLIS = 5_000L

    private const val HTTP_CACHE_SIZE_BYTES = 52_428_800 // 1024 * 1024 * 50

    private const val MAX_PREVIEW = 8192
    private const val ERROR_TEXT_LENGTH = 1024
    private const val DEFAULT_APPLICATION_TYPE = "api"
    private const val DEFAULT_DEVICE_ID = "swagger"
}
