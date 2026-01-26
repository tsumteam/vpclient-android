package ru.mercury.vpclient.core.ktx

import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.TimeoutCancellationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

val Throwable.isNetworkRelated: Boolean
    get() = this is UnknownHostException || this is ConnectException || this is SocketTimeoutException || this is SSLException || this is TimeoutCancellationException || this is HttpRequestTimeoutException
