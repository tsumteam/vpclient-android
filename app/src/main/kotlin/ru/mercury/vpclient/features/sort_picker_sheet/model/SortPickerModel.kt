package ru.mercury.vpclient.features.sort_picker_sheet.model

import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.mvi.Model

data class SortPickerModel(
    val selectedSortType: SortType
): Model
