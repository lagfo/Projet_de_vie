package org.ticanalyse.projetdevie.presentation.planification_de_projet

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigationViewModel
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.utils.PdfBitmapConverter
import org.ticanalyse.projetdevie.utils.PdfUtil.sharePdf
import java.io.File
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun PdfViewerScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val pdfBitmapConverter = remember {
        PdfBitmapConverter(context)
    }

    var pdfUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var renderedPages by remember {
        mutableStateOf<List<Bitmap>>(emptyList())
    }

    val viewModel = hiltViewModel<AppNavigationViewModel>()

    val fileUri = viewModel.resumeUri.collectAsStateWithLifecycle().value
    val file = remember { mutableStateOf( File(fileUri)) }
    Timber.d("fileUri: $fileUri")

    pdfUri = file.value.toUri()

    LaunchedEffect(key1 = pdfUri) {
        delay(1000)
        if (pdfUri != null) {
            Timber.d("pdfUri: $pdfUri")
            renderedPages = pdfBitmapConverter.pdfToBitmap(pdfUri!!)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        LazyColumn (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            items(renderedPages) { page ->
                PdfPage(page)
            }
        }

        AppButton("Partager") {
            sharePdf(file.value, context)
        }
    }
}


@Composable
fun PdfPage(
    page: Bitmap,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = page,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(page.width.toFloat() / page.height.toFloat())
    )
}