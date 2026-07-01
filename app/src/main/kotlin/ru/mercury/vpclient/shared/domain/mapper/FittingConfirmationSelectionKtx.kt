package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryGroup
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryInterval
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationDeliveryMode
import ru.mercury.vpclient.shared.data.entity.FittingConfirmationPlaceType
import ru.mercury.vpclient.shared.data.network.type.FittingType

val FittingConfirmationPlaceType.fittingTypeDto: FittingType
    get() = when (this) {
        FittingConfirmationPlaceType.Boutique -> FittingType.IN_THE_STORE
        FittingConfirmationPlaceType.Home -> FittingType.AT_HOME
        FittingConfirmationPlaceType.Other -> FittingType.AT_HOME
    }

val FittingType.fittingConfirmationPlaceType: FittingConfirmationPlaceType
    get() = when (this) {
        FittingType.NONE -> FittingConfirmationPlaceType.Boutique
        FittingType.IN_THE_STORE -> FittingConfirmationPlaceType.Boutique
        FittingType.AT_HOME -> FittingConfirmationPlaceType.Home
    }

fun FittingConfirmationDeliveryMode.updatedFor(
    groups: List<FittingConfirmationDeliveryGroup>
): FittingConfirmationDeliveryMode {
    return when {
        groups.size > 1 -> this
        else -> FittingConfirmationDeliveryMode.Single
    }
}

fun List<FittingConfirmationDeliveryInterval>.selectedDayId(current: String?): String? {
    return current?.takeIf { dayId -> any { interval -> interval.dayId == dayId } }
        ?: firstOrNull()?.dayId
}

fun List<FittingConfirmationDeliveryInterval>.selectedIntervalId(current: String?): String? {
    return current?.takeIf { intervalId -> any { interval -> interval.id == intervalId } }
        ?: firstOrNull()?.id
}

fun List<FittingConfirmationDeliveryGroup>.selectedDeliveryDayIds(
    current: Map<String, String>
): Map<String, String> {
    return associate { group ->
        val selectedDayId = group.intervals.selectedDayId(current[group.id]).orEmpty()
        group.id to selectedDayId
    }.filterValues { it.isNotEmpty() }
}

fun List<FittingConfirmationDeliveryGroup>.selectedDeliveryIntervalIds(
    current: Map<String, String>
): Map<String, String> {
    return associate { group ->
        val selectedIntervalId = group.intervals.selectedIntervalId(current[group.id]).orEmpty()
        group.id to selectedIntervalId
    }.filterValues { it.isNotEmpty() }
}
