package ru.mercury.vpclient.shared.ui.ktx

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.launcherDialer(phone: String) {
    val intent = Intent(
        Intent.ACTION_DIAL,
        "tel:${phone.filterNot(Char::isWhitespace)}".toUri()
    )
    runCatching { startActivity(intent) }
}

fun Context.launcherEmail(email: String) {
    val intent = Intent(
        Intent.ACTION_SENDTO,
        "mailto:${email.trim()}".toUri()
    )
    runCatching { startActivity(intent) }
}
