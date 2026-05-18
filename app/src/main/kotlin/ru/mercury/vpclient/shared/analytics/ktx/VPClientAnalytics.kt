package ru.mercury.vpclient.shared.analytics.ktx

import ru.mercury.vpclient.shared.analytics.VPClientAnalytics

fun VPClientAnalytics.reportLoginSuccess() {
    reportEvent(VPClientAnalytics.Event.LOGIN_SUCCESS)
}
