package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.FORMAT_MASK
import ru.mercury.vpclient.core.PIN_MAX_TIME
import ru.mercury.vpclient.core.PIN_TIMER_DELAY
import ru.mercury.vpclient.core.persistence.database.entity.LoyaltyEntity
import java.util.Locale

val String.masked: String
    get() = String.format(Locale.getDefault(), FORMAT_MASK, takeLast(2))

val LoyaltyEntity.cardMasked: String
    get() = card.masked

val LoyaltyEntity?.orEmpty: LoyaltyEntity
    get() = this ?: LoyaltyEntity.Empty

val LoyaltyEntity.isNotEmpty: Boolean
    get() = this != LoyaltyEntity.Empty

fun LoyaltyEntity.pinTimerSec(
    now: Long = System.currentTimeMillis()
): Int {
    val elapsedMillis = now - pinTimer
    if (elapsedMillis >= PIN_MAX_TIME) return 0
    val remainingMillis = PIN_MAX_TIME - elapsedMillis
    return (remainingMillis / PIN_TIMER_DELAY).toInt()
}
