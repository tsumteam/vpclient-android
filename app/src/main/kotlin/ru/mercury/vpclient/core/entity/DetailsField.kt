package ru.mercury.vpclient.core.entity

import ru.mercury.vpclient.core.ui.theme.ClientStrings

sealed interface DetailsField {
    val titleRes: Int
    val value: String
    val showCopyIcon: Boolean get() = false

    data class Brand(
        override val value: String
    ): DetailsField {
        override val titleRes = ClientStrings.DetailsFieldBrand
    }

    data class Color(
        override val value: String
    ): DetailsField {
        override val titleRes = ClientStrings.DetailsFieldColor
    }

    data class Composition(
        override val value: String
    ): DetailsField {
        override val titleRes = ClientStrings.DetailsFieldComposition
    }

    data class Country(
        override val value: String
    ): DetailsField {
        override val titleRes = ClientStrings.DetailsFieldCountry
    }

    data class ItemId(
        override val value: String
    ): DetailsField {
        override val titleRes = ClientStrings.DetailsFieldItemId
        override val showCopyIcon = true
    }

    data class Article(
        override val value: String
    ): DetailsField {
        override val titleRes = ClientStrings.DetailsFieldArticle
        override val showCopyIcon = true
    }

    data class Measurements(
        override val value: String
    ): DetailsField {
        override val titleRes = ClientStrings.DetailsFieldMeasurements
    }
}
