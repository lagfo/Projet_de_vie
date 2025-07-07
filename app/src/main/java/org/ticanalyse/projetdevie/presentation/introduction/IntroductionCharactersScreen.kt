package org.ticanalyse.projetdevie.presentation.introduction

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import org.ticanalyse.projetdevie.utils.ExoPlayer
import timber.log.Timber
import androidx.core.net.toUri

@Composable
fun IntroductionCharactersScreen(
    onNavigate: () -> Unit,
    onBackPressed: () -> Unit
) {
    val characters = listOf(
        Characters("Ali", R.drawable.ali, R.raw.intro_aly/*, R.raw.intro_ali*/),
        Characters("Safy", R.drawable.saly, R.raw.intro_safy/*, R.raw.intro_safi*/),
        Characters("Myriam", R.drawable.coach, R.raw.intro_myriam/*, R.raw.intro_myriam*/),
    )

    val pagerState = rememberPagerState(
        pageCount = {characters.size},
        initialPage = 0
    )

    Timber.tag("btntag").d("page state: ${pagerState.currentPage}")

    BackHandler {
        onBackPressed()
    }

    val scope = rememberCoroutineScope()
//    val soundPlayState = remember { mutableStateOf(false) }

    val nextPage = remember { mutableIntStateOf(0) }

    LaunchedEffect(nextPage) {
        pagerState.animateScrollToPage(nextPage.intValue)
    }

    /*LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        while (true) {
            delay(15000)
            nextPage.intValue = (pagerState.currentPage + 1) % pagerState.pageCount
        }
    }
 LaunchedEffect(pagerState.currentPage) {
        delay(15000)
        val next = (pagerState.currentPage + 1) % pagerState.pageCount
        pagerState.animateScrollToPage(next)
    }
     */

    val context = LocalContext.current
    LaunchedEffect(pagerState.currentPage) {
        val duration = getVideoDuration(context, characters[pagerState.currentPage].video)
        delay(duration + 1000L) // +1s buffer
        pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.07f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top= MediumPadding3, bottom = MediumPadding1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Personnages",
                style = MaterialTheme.typography.bodyMedium)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth(),

                        ) { currentPageIndex ->
                        Column (
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Timber.tag("btntag").d("currentPageIndex: ${pagerState.currentPage}")

                            //ExoPlayer(characters[currentPageIndex].video, currentPageIndex == pagerState.currentPage)
                            if (currentPageIndex == pagerState.currentPage) {
                                ExoPlayer(characters[currentPageIndex].video, true)
                            }

                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                IconButton(
                                    onClick = {
                                        val prevPage = pagerState.currentPage - 1
                                        Timber.tag("btntag").d("prev page $prevPage: current page ${pagerState.currentPage}")
                                        if (prevPage >= 0) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(prevPage)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                                Column (
                                    modifier = Modifier.fillMaxWidth(.6f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        painter = painterResource(id = characters[currentPageIndex].image),
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxHeight(0.6f)
                                    )
                                    Text(
                                        text = characters[currentPageIndex].name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        val nextPage = pagerState.currentPage + 1
                                        Timber.tag("btntag").d(" next page $nextPage: curent page${pagerState.currentPage}")
                                        if (nextPage < pagerState.pageCount) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(nextPage)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.size(48.dp)
                                    )

                                }
                            }
                        }

                    }

                }
                PageIndicator(
                    pageCount = pagerState.pageCount,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            AppButton (text = "Decouvrir mon reseau", onClick = onNavigate)
        }

    }


}


suspend fun getVideoDuration(context: Context, videoResId: Int): Long =
    withContext(Dispatchers.IO) {
        runCatching {
            MediaMetadataRetriever().use { retriever ->
                val uri = "android.resource://${context.packageName}/$videoResId".toUri()
                retriever.setDataSource(context, uri)
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 15000L
            }
        }.getOrElse { 15000L }
    }

data class Characters(
    val name: String,
    val image: Int,
    val video: Int,
//    val audio: Int
)

