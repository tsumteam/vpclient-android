package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.main.tabs.consultants.model.ConsultantsModel

class ConsultantsModelProvider: PreviewParameterProvider<ConsultantsModel> {
    override val values: Sequence<ConsultantsModel> = sequenceOf(
        ConsultantsModel(
            employees = listOf(
                EmployeeEntityProvider().values.first(),
                EmployeeEntityProvider().values.first().copy(
                    employeeId = "2",
                    employeeName = "Екатерина",
                    employeeSurname = "Орлова",
                    employeeBrand = "BORK",
                    employeeBotiqueAddress = "Москва, Петровка, 2",
                    employeeBotiqueAddressShort = "Петровка, 2",
                    isActive = true,
                    basketBadge = 0,
                    fittingBadge = 2,
                    messengerBadge = 3,
                    orderBadge = 0
                )
            )
        ),
        ConsultantsModel(loadConsultantsJob = Job())
    )
}
