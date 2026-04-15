package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.CODE_RESEND_MAX_TIME
import ru.mercury.vpclient.shared.data.CODE_RESEND_TIMER_DELAY
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity

fun ClientEntity.resendCodeTimerSec(
    now: Long = System.currentTimeMillis()
): Int {
    val elapsedMillis = now - codeResendTimer
    if (codeResendTimer == 0L || elapsedMillis >= CODE_RESEND_MAX_TIME) return 0
    val remainingMillis = CODE_RESEND_MAX_TIME - elapsedMillis
    val remainingSeconds = (remainingMillis / CODE_RESEND_TIMER_DELAY).toInt()
    val maxSeconds = (CODE_RESEND_MAX_TIME / CODE_RESEND_TIMER_DELAY).toInt().coerceAtLeast(1)
    return remainingSeconds.coerceIn(1, maxSeconds)
}
