package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.DriverEntity

class DriverEntityProvider: PreviewParameterProvider<DriverEntity> {
    override val values: Sequence<DriverEntity> = sequenceOf(
        DriverEntity(
            driverName = "Иванов Иван Иваныч",
            driverBarcode = "1234567890",
            cashOnHand = 100_000_000_000_000.0
        )
    )
}
