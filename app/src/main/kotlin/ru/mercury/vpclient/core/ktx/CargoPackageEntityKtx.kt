package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.network.request.CargoPackageReceiveRequest
import ru.mercury.vpclient.core.persistence.database.entity.CargoEntity

fun CargoEntity.request(
    driverBarcode: String
): CargoPackageReceiveRequest {
    return CargoPackageReceiveRequest(
        cargoPackages = listOf(barcode),
        emplBarcode = driverBarcode,
        inventLocationId = boutiqueId
    )
}
