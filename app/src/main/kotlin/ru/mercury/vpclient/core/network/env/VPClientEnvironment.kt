package ru.mercury.vpclient.core.network.env

enum class VPClientEnvironment(
    val url: String
) {
    TEST("https://test-bca.mhpost.ru/api/"),
    UAT("https://uat-bca.mhpost.ru/api/"),
    PROD("https://prod-bca.mhpost.ru/api/")
}
