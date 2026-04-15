package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.BrandFilterValue
import ru.mercury.vpclient.shared.data.entity.ColorFilterValue
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.entity.SizeFilterValue
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity

val FilterValuesEntity.isEmpty: Boolean
    get() = this == FilterValuesEntity.Empty

// fixme

val FilterValuesEntity.values: List<FilterChip>
    get() = valueIds.zip(valueLabels).map { (id, label) -> FilterChip(id = id, label = label) }

val FilterValuesEntity.colorValues: List<ColorFilterValue>
    get() = valueIds.mapIndexedNotNull { index, valueId ->
        val valueLabel = valueLabels.getOrNull(index).orEmpty()
        if (valueLabel.isBlank()) {
            null
        } else {
            ColorFilterValue(
                id = valueId,
                label = valueLabel,
                imageUrl = valueLabelPhotoUrls.getOrNull(index).orEmpty().ifBlank { null }
            )
        }
    }

val FilterValuesEntity.sizeValues: List<SizeFilterValue>
    get() = valueIds.mapIndexedNotNull { index, valueId ->
        val valueLabel = valueLabels.getOrNull(index).orEmpty()
        if (valueLabel.isBlank()) {
            null
        } else {
            SizeFilterValue(
                id = valueId,
                labelRu = valueLabel,
                labelItalian = valueLabelsItalian.getOrNull(index).orEmpty().ifBlank { null },
                labelFrench = valueLabelsFrench.getOrNull(index).orEmpty().ifBlank { null },
                labelInternational = valueLabelsInternational.getOrNull(index).orEmpty().ifBlank { null }
            )
        }
    }

val FilterValuesEntity.brandValues: List<BrandFilterValue>
    get() = valueIds.mapIndexedNotNull { index, valueId ->
        val valueLabel = valueLabels.getOrNull(index).orEmpty()
        if (valueLabel.isBlank()) {
            null
        } else {
            BrandFilterValue(
                id = valueId,
                label = valueLabel,
                labelPhotoUrl = valueLabelPhotoUrls.getOrNull(index).orEmpty().ifBlank { null },
                isFavorite = valueIsFavorites.getOrNull(index) == "true",
                isTopBrand = valueIsTopBrands.getOrNull(index) == "true"
            )
        }
    }
