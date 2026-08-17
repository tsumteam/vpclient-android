package ru.mercury.vpclient.shared.data.realtime

data class RealtimeSession(
    val baseUrl: String,
    val token: String,
    val userId: String,
    val pairedUserId: String,
    val headers: Map<String, String>
)
