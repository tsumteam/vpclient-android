package ru.mercury.vpclient.core.network.env

enum class VPClientEnvironment(
    val url: String
) {
    BCA("https://test-bca.mhpost.ru/"),
    TEST("https://test-ax-api-mercury.mhpost.ru/"),
    UAT("https://uat-ax-api-mercury.mhpost.ru/"),
    PROD("https://prod-bca.mhpost.ru/")
}
