package ru.mercury.vpclient.features.main.tabs.consultants.model

import ru.mercury.vpclient.core.mvi.Model

data class ConsultantsModel(
    val consultants: List<ConsultantUiModel> = emptyList()
): Model
