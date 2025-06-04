package org.ticanalyse.projetdevie.presentation.introduction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.utils.ExoPlayer
import kotlin.collections.get
import kotlin.compareTo
import kotlin.text.compareTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroductionCharactersScreen() {
    val characters = listOf(
        Characters("Ali", R.drawable.ali, R.raw.intro_ali/*, R.raw.intro_ali*/),
        Characters("Safy", R.drawable.saly, R.raw.intro_safi/*, R.raw.intro_safi*/),
        Characters("Myriam", R.drawable.coach, R.raw.intro_myriam/*, R.raw.intro_myriam*/),
    )

    val pagerState = rememberPagerState(
        pageCount = {characters.size},
        initialPage = 0
    )


    val scope = rememberCoroutineScope()
    val soundPlayState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Introduction",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp),
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

                            ExoPlayer(characters[currentPageIndex].video)
//                            Box(
//                                modifier = Modifier
//                                    .background(Color.Black)
//                                    .aspectRatio(16 / 9f)
//                                    .fillMaxWidth()
//                            )

                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                IconButton(
                                    onClick = {
                                        val prevPage = pagerState.currentPage - 1
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
                                        modifier = Modifier.fillMaxHeight(0.7f)
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

//                    IconButton(
//                        onClick = {
//                            soundPlayState.value = !soundPlayState.value
//                        }
//                    ) {
//                        Icon(
//                            painter = if (soundPlayState.value) {
//                                painterResource(id = R.drawable.baseline_volume_up_24)
//                            } else {
//                                painterResource(id = R.drawable.baseline_volume_off_24)
//                            },
//                            contentDescription = null
//                        )
//                    }
                }
                PageIndicator(
                    pageCount = pagerState.pageCount,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            AppButton (text = "Decouvrir mon reseau", onClick = {})
        }
    }

}

data class Characters(
    val name: String,
    val image: Int,
    val video: Int,
//    val audio: Int
)

@Preview
@Composable
fun IntroductionCharactersScreenPreview() {
    IntroductionCharactersScreen()
}