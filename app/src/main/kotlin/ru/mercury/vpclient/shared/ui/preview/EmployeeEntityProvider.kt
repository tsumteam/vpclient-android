package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.EmployeeEntity

class EmployeeEntityProvider: PreviewParameterProvider<EmployeeEntity> {
    override val values: Sequence<EmployeeEntity> = sequenceOf(
        EmployeeEntity(
            employeeId = "1",
            employeeEmail = "anna@example.com",
            employeeMiddleName = "",
            employeeName = "Анна",
            employeePhone = "+79990000000",
            employeeSurname = "Смирнова",
            photoUrl = "https://i.pravatar.cc/144?img=32",
            previewPhotoUrl = "https://i.pravatar.cc/144?img=32",
            lastActivityColorHex = "",
            lastActivityDate = "",
            employeeBotiqueAddress = "Барвиха Luxury Village",
            employeeBotiqueAddressShort = "Барвиха Luxury Village",
            employeeBrand = "MVST",
            isActive = false,
            basketBadge = 1,
            fittingBadge = 1,
            messengerBadge = 1,
            orderBadge = 1,
            compilationBadge = 0
        ),
        EmployeeEntity.Empty
    )
}
