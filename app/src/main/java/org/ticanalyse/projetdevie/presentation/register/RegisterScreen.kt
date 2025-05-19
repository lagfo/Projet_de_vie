package org.ticanalyse.projetdevie.presentation.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppPhoneInput
import org.ticanalyse.projetdevie.presentation.common.AppSelection
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.AppTextInput
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme


@Composable
fun RegisterScreen (){
    var nom by rememberSaveable { mutableStateOf("") }
    var prenom by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    val genres = listOf("Homme", "Femme")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.primary_color).copy(alpha = 0.05f))
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f
        )
        Column(modifier = Modifier.fillMaxSize()) {
            //RegisterHeader()
        }
    }


}





@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(){
    ProjetDeVieTheme {
        RegisterScreen ()
    }
}
