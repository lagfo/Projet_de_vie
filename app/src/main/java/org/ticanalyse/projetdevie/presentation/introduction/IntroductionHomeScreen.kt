package org.ticanalyse.projetdevie.presentation.introduction

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.utils.ExoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntroductionHomeScreen(
    onNavigate: () -> Unit
) {

    Scaffold (
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
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "asdkjflsajdflsajdl alsdkfjlsakdjflaskdjflaksjlfdkjsadlfa asldkfjlaksdjflkajdsflasdkjflaksjdflasdlaskdjflkldas",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            ExoPlayer(R.raw.intro_safi)

//            Box(
//                modifier = Modifier.background(Color.Black)
//                    .aspectRatio(16 / 9f)
//                    .fillMaxWidth()
//            )

            AppButton (text = "Suivant", onClick = onNavigate)
        }

    }

}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            IndicatorDots(isSelected = index == currentPage, modifier = modifier)
        }
    }

}

@Composable
fun IndicatorDots(isSelected: Boolean, modifier: Modifier) {
    val size = animateDpAsState(targetValue = if (isSelected) 12.dp else 10.dp, label = "")
    Box (
        modifier = modifier
            .padding(2.dp)
            .size(size.value)
            .clip(RoundedCornerShape(5.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )
    ){

    }
}

@Preview(showBackground = true)
@Composable
fun PageIntroductionPreview() {
    IntroductionHomeScreen({})
}