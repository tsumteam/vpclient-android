package ru.mercury.vpclient.core.entity

import ru.mercury.vpclient.core.Amount

data class DriverEntity(
    val driverName: String,
    val driverBarcode: String,
    val cashOnHand: Amount
) {
    companion object {
        val Empty = DriverEntity(
            driverName = "",
            driverBarcode = "",
            cashOnHand = 0.0
        )
    }
}
