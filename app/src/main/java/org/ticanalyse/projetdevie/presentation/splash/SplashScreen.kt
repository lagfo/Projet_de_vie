package org.ticanalyse.projetdevie.presentation.splash

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme


@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.bg))
    ) {
        Box(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(300.dp)
                )
                Text(
                    text = stringResource(id = R.string.splash_title),
                    color = colorResource(id = R.color.text),
                    fontSize = 25.sp,
                )
                Text(
                    text = stringResource(id = R.string.splash_body),
                    color = colorResource(id = R.color.text),
                    fontSize = 15.sp
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_helvetas),
                    contentDescription = "Logo Helvetas",
                    modifier = Modifier.fillMaxWidth(1f)
                )
                Image(
                    painter = painterResource(id = R.drawable.logo_tica),
                    contentDescription = "Logo Tica",
                    modifier = Modifier.fillMaxWidth(1f)
                )
                Spacer(modifier = Modifier.height(16.dp)) // Exemple de padding
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SplashScreenPreview() {
    ProjetDeVieTheme {
        SplashScreen()
    }
}