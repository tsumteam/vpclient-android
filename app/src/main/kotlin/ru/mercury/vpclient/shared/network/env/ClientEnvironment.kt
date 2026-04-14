package ru.mercury.vpclient.shared.network.env

enum class ClientEnvironment(
    val url: String
) {
    TEST("https://test-bca.mhpost.ru/api/"),
    UAT("https://uat-bca.mhpost.ru/api/"),
    PROD("https://prod-bca.mhpost.ru/api/")
}
