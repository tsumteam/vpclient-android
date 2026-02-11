package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.CODE_RESEND_MAX_TIME
import ru.mercury.vpclient.core.CODE_RESEND_TIMER_DELAY
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

fun ClientEntity.resendCodeTimerSec(
    now: Long = System.currentTimeMillis()
): Int {
    val elapsedMillis = now - codeResendTimer
    if (codeResendTimer == 0L || elapsedMillis >= CODE_RESEND_MAX_TIME) return 0
    val remainingMillis = CODE_RESEND_MAX_TIME - elapsedMillis
    val remainingSeconds = (remainingMillis / CODE_RESEND_TIMER_DELAY).toInt()
    return remainingSeconds.coerceIn(1, 59)
}
