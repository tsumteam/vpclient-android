package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

val CompilationEntity.imageUrl: String
    get() = photoUrl.ifEmpty { collageUrl }

val CompilationEntity.date: String
    get() {
        val value = createDate.trim()
        if (value.isEmpty()) return "-"
        val date = runCatching { OffsetDateTime.parse(value).toLocalDate() }.getOrNull()
            ?: runCatching { ZonedDateTime.parse(value).toLocalDate() }.getOrNull()
            ?: runCatching { LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate() }.getOrNull()
            ?: runCatching { LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE) }.getOrNull()
            ?: return value
        return date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.forLanguageTag("ru")))
    }

val CompilationEntity.infoString: String
    @Composable get() {
        val looks = pluralStringResource(
            ClientStrings.FittingCompilationLooks,
            looksQty,
            looksQty
        )
        val things = pluralStringResource(
            ClientStrings.FittingCompilationThings,
            lookProductsQty,
            lookProductsQty
        )
        return "$looks, $things"
    }

val CompilationEntity.hasBadge: Boolean
    get() = badge > 0
