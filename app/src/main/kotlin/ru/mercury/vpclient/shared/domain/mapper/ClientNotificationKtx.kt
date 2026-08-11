package ru.mercury.vpclient.shared.domain.mapper

import java.net.URI
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import ru.mercury.vpclient.shared.data.entity.ClientNotificationCategory
import ru.mercury.vpclient.shared.data.network.response.ClientNotificationFilterResponse
import ru.mercury.vpclient.shared.data.network.response.ClientNotificationResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientNotificationEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CompilationEntity

val String.clientNotificationCompilationId: Int?
    get() {
        val uri = runCatching { URI(trim()) }.getOrNull() ?: return null
        val isClientDeepLink = uri.scheme.equals("vipplatinum", ignoreCase = true) || uri.scheme.equals("vip-platinum", ignoreCase = true)
        if (!isClientDeepLink || !uri.host.equals("compilation", ignoreCase = true)) return null
        return uri.path.orEmpty().trim('/').substringBefore('/').toIntOrNull()?.takeIf { id -> id > 0 }
    }

fun ClientNotificationEntity.withCompilationImage(compilationEntities: List<CompilationEntity>): ClientNotificationEntity {
    val compilationId = deepLinkUrl.clientNotificationCompilationId ?: return this
    val compilation = compilationEntities.firstOrNull { entity -> entity.id == compilationId } ?: return this
    val compilationImageUrl = compilation.imageUrl.takeIf { url -> url.isNotBlank() } ?: return this
    return copy(imageUrl = compilationImageUrl)
}

fun List<ClientNotificationResponse>.entities(
    category: ClientNotificationCategory
): List<ClientNotificationEntity> {
    return mapNotNull { response ->
        val id = response.id ?: return@mapNotNull null
        ClientNotificationEntity(
            id = id,
            category = category,
            badge = response.badge.orEmpty,
            type = response.type.orEmpty,
            imageUrl = response.imageUrl.orEmpty(),
            title = response.title.orEmpty(),
            message = response.message.orEmpty(),
            deepLinkUrl = response.deepLinkUrl.orEmpty(),
            timestamp = response.timestamp.orEmpty()
        )
    }
}

fun ClientNotificationFilterResponse.requestJson(): JsonObject? {
    val filterType = filterType ?: return null
    return JsonObject(
        mapOf(
            "filterType" to JsonPrimitive(filterType),
            "values" to JsonArray(
                values.orEmpty().map { value ->
                    JsonObject(
                        value.filterKeys { key ->
                            key == "valueType" ||
                                key == "value" ||
                                key == "from" ||
                                key == "to" ||
                                key == "clientId" ||
                                key == "contractorId"
                        }
                    )
                }
            )
        )
    )
}

fun String.clientNotificationDateText(
    yesterdayText: String,
    now: LocalDateTime = LocalDateTime.now()
): String {
    val dateTime = runCatching { OffsetDateTime.parse(this).toLocalDateTime() }.getOrNull()
        ?: runCatching { LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }.getOrNull()
        ?: return ""
    return when (dateTime.toLocalDate()) {
        now.toLocalDate() -> dateTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("ru")))
        now.toLocalDate().minusDays(1) -> yesterdayText
        else -> dateTime.format(DateTimeFormatter.ofPattern("d MMMM", Locale.forLanguageTag("ru")))
    }
}
