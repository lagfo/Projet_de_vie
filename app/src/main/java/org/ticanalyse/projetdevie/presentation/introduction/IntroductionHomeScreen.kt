package org.ticanalyse.projetdevie.presentation.introduction

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import org.ticanalyse.projetdevie.utils.ExoPlayer

@Composable
fun IntroductionHomeScreen(
    onNavigate: () -> Unit
) {

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
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           Box {
               Column(horizontalAlignment = Alignment.CenterHorizontally) {
                   Text(
                       text = "\uD83D\uDC4B Salut à toi et bienvenue dans cette première activité !\n" +
                               "Aujourd’hui, on commence un super voyage : celui de ton Projet de Vie. \uD83C\uDF31\n" +
                               " Retiens bien : tu es la force du changement dans ta propre vie.\n" +
                               "\uD83C\uDFAF Tu es prêt(e) à avancer dans la construction de ton avenir. Alors,\n" +
                               "découvrons ensemble les étapes suivantes de ton parcours pour définir ton\n" +
                               "« Projet de Vie » ! \uD83D\uDE80",
                       style = MaterialTheme.typography.bodySmall,
                       textAlign = TextAlign.Center,
                       modifier = Modifier.fillMaxWidth()
                   )
                   Spacer(modifier = Modifier.height(MediumPadding1))
                   ExoPlayer(R.raw.intro_safy)
               }

           }

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