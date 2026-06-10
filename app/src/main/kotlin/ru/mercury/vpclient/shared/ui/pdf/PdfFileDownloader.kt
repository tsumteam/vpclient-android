package ru.mercury.vpclient.shared.ui.pdf

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URI

suspend fun downloadPdfFile(
    url: String,
    cacheDir: File
): File {
    return withContext(Dispatchers.IO) {
        val file = File(cacheDir, "pdf_${url.hashCode()}.pdf")
        if (!file.exists() || file.length() == 0L) {
            val tempFile = File(cacheDir, "${file.name}.tmp")
            URI(url).toURL().openStream().use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.delete()
            tempFile.renameTo(file)
        }
        file
    }
}
