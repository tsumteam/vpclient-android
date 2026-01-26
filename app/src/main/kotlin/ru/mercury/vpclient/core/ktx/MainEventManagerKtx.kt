package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.event.CenterLoading
import ru.mercury.vpclient.main.event.MainEventManager

suspend fun withCenterLoading(block: suspend () -> Unit) {
    MainEventManager.send(CenterLoading(true))
    try {
        block()
    } finally {
        MainEventManager.send(CenterLoading(false))
    }
}
