package ru.mercury.vpclient.features.size_picker_sheet.model

import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState

data class SizePickerModel(
    val sizeSelectorState: SizeSelectorState,
    val buttonText: Int
): Model
