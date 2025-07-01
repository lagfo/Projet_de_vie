package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import org.ticanalyse.projetdevie.utils.ExoPlayer


@Composable
fun LienDeVieIntroductionScreen(modifier: Modifier = Modifier,onNavigate:()->Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha =0.07f
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top= MediumPadding3, bottom = MediumPadding1),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Box {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(7.dp),
                        text = stringResource(R.string.ligne_de_vie_introduction) ,
                        maxLines = 6,
                        textAlign = TextAlign.Center,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Normal,
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    ExoPlayer(R.raw.intro_safi)
                }

            }
            AppButton (text = "Suivant", onClick = onNavigate)

        }

    }

}

