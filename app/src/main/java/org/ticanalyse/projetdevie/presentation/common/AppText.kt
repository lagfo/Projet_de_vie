package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.BelfastGrotesk
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.TextToSpeechManager

@Composable
fun AppText(
    text: String,
    fontFamily: FontFamily = BelfastGrotesk,
    fontWeight: FontWeight = FontWeight.Black,
    fontStyle: FontStyle = FontStyle.Normal,
    color: Color = colorResource(id = R.color.primary_color),
    fontSize: TextUnit = 25.sp,
    style: TextStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
    ttsManager: TextToSpeechManager?
) {
    Text(
        modifier = Modifier.clickable {
            ttsManager?.speak(text)
        },
        text = text,
        color = color,
        style = style,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontSize = fontSize)
}

@Composable
fun AppIconText(
    modifier: Modifier=Modifier,
    text: String,
    fontFamily: FontFamily = Roboto,
    fontWeight: FontWeight = FontWeight.Black,
    fontStyle: FontStyle = FontStyle.Normal,
    color: Color = colorResource(id = R.color.primary_color),
    fontSize: TextUnit = 25.sp,
    style: TextStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
) {
    Text(
        modifier=modifier,
        text = text,
        color = color,
        style = style,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontSize = fontSize,
        textAlign = TextAlign.Center)
}
