package ru.mercury.vpclient.features.consultants.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ConsultantsIntent: Intent {
    data object CollectEmployees: ConsultantsIntent
    data object LoadEmployees: ConsultantsIntent
    data class SetActiveEmployee(val employeeId: String): ConsultantsIntent
    data class EmployeeClick(val employeeId: String): ConsultantsIntent
    data class EmployeeActionClick(val employeeId: String, val actionId: Int): ConsultantsIntent
}
