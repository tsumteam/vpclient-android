package ru.mercury.vpclient.core.analytics

import io.appmetrica.analytics.AppMetrica

object VPClientAnalytics {

    fun reportUser(userToken: String) {
        AppMetrica.setUserProfileID(userToken)
    }

    fun reportEvent(name: String) {
        AppMetrica.reportEvent(name)
    }

    fun reportEvent(name: String, params: Map<String, Any>) {
        AppMetrica.reportEvent(name, params)
    }

    object Event {
        const val LOGIN_SUCCESS = "Login_Success"
    }
}
