package ru.mercury.vpclient.shared.ktx

import ru.mercury.vpclient.shared.event.CenterLoading
import ru.mercury.vpclient.activity.event.MainEventManager

suspend fun withCenterLoading(block: suspend () -> Unit) {
    MainEventManager.send(CenterLoading(true))
    try {
        block()
    } finally {
        MainEventManager.send(CenterLoading(false))
    }
}
