package org.ticanalyse.projetdevie.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PdfBitmapConverter(
    private val context: Context,
) {
    var renderer: PdfRenderer? = null

    suspend fun pdfToBitmap(contentUri: Uri): List<Bitmap> {
        return withContext(Dispatchers.IO) {
            renderer?.close()

            context.contentResolver.openFileDescriptor(
                contentUri,
                "r",
            )?.use { descriptor ->
                with(PdfRenderer(descriptor)) {
                    renderer = this

                    return@withContext(0 until pageCount).map { index ->
                        async {
                            openPage(index).use { page ->
                                val bitmap = createBitmap(page.width, page.height)

                                val canvas = Canvas(bitmap).apply {
                                    drawColor(Color.White.toArgb())
                                    drawBitmap(bitmap, 0f, 0f, null)
                                }

                                page.render(
                                    bitmap,
                                    null,
                                    null,
                                    PdfRenderer.Page.RENDER_MODE_FOR_PRINT
                                )

                                bitmap
                            }
                        }
                    }.awaitAll()
                }
            }
            return@withContext emptyList()
        }
    }
}