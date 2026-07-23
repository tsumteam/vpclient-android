package ru.mercury.vpclient.shared.data

import java.util.UUID

object DigineticaIdentity {
    val sessionId: String = UUID.randomUUID().toString()
    val userGuid: String = "0:${System.currentTimeMillis().toString(36).uppercase()}:${UUID.randomUUID()}"
}
