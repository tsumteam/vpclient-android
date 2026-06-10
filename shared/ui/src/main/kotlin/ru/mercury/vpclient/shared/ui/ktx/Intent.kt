package ru.mercury.vpclient.shared.ui.ktx

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openDialer(phone: String) {
    val intent = Intent(
        Intent.ACTION_DIAL,
        Uri.parse("tel:${phone.filterNot(Char::isWhitespace)}")
    )
    runCatching { startActivity(intent) }
}

fun Context.openEmail(email: String) {
    val intent = Intent(
        Intent.ACTION_SENDTO,
        Uri.parse("mailto:${email.trim()}")
    )
    runCatching { startActivity(intent) }
}
