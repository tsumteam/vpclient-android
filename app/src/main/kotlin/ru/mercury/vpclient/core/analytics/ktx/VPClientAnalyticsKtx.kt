package ru.mercury.vpclient.core.analytics.ktx

import ru.mercury.vpclient.core.analytics.VPClientAnalytics

fun VPClientAnalytics.reportLoginSuccess() {
    reportEvent(VPClientAnalytics.Event.LOGIN_SUCCESS)
}
