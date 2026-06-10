package ru.mercury.vpclient.shared.ui.components.profile

import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.pdf.PdfDocument
import androidx.pdf.RenderParams
import androidx.pdf.SandboxedPdfLoader
import androidx.pdf.compose.PdfViewer
import androidx.pdf.compose.PdfViewerState
import kotlinx.coroutines.Dispatchers
import ru.mercury.vpclient.shared.ui.pdf.downloadPdfFile

@Composable
fun ProfilePdfView(
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val loader = remember(context) { SandboxedPdfLoader(context, Dispatchers.IO) }
    val state = remember { PdfViewerState() }
    var pdfDocument by remember { mutableStateOf<PdfDocument?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(url) {
        pdfDocument = null
        isLoading = true
        isError = false

        runCatching {
            val file = downloadPdfFile(
                url = url,
                cacheDir = context.cacheDir
            )
            val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            loader.openDocument(
                uri = Uri.fromFile(file),
                fileDescriptor = fileDescriptor,
                password = null,
                renderParams = RenderParams(RenderParams.RENDER_MODE_FOR_DISPLAY)
            )
        }.onSuccess { document ->
            pdfDocument = document
            isLoading = false
        }.onFailure {
            isError = true
            isLoading = false
        }
    }

    val document = pdfDocument
    DisposableEffect(document) {
        onDispose { document?.close() }
    }

    when {
        isError -> {
            ProfileWebView(
                url = url,
                modifier = modifier
            )
        }
        else -> {
            Box(
                modifier = modifier
            ) {
                PdfViewer(
                    pdfDocument = pdfDocument,
                    state = state,
                    modifier = Modifier.fillMaxSize()
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
