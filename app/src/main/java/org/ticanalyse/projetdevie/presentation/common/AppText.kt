package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itextpdf.layout.properties.LineHeight
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.BelfastGrotesk
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.TextToSpeechManager

@Composable
fun AppText(
    modifier: Modifier = Modifier,
    text: String,
    fontFamily: FontFamily = BelfastGrotesk,
    fontWeight: FontWeight = FontWeight.Black,
    fontStyle: FontStyle = FontStyle.Normal,
    color: Color = colorResource(id = R.color.primary_color),
    fontSize: TextUnit = 25.sp,
    style: TextStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
    ttsManager: TextToSpeechManager?,
    isTextAlignCenter: Boolean = false,
    isDefineMaxLine: Boolean = false,
    isDefineLineHeight: Boolean = false

) {
    Text(
        modifier = modifier.clickable {
            ttsManager?.speak(text)
        },
        text = text,
        color = color,
        style = style,
        fontFamily = fontFamily,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        fontSize = fontSize,
        lineHeight = if(isDefineLineHeight) fontSize * 1.2f else  fontSize * 1f,
        overflow = TextOverflow.Ellipsis,
        textAlign = if (isTextAlignCenter) TextAlign.Center else TextAlign.Unspecified,
        maxLines = if (isDefineMaxLine) 1 else 5
    )

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
    textAlign: TextAlign = TextAlign.Center
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
        textAlign = textAlign)
}
