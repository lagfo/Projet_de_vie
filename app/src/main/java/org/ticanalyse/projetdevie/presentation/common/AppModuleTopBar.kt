package org.ticanalyse.projetdevie.presentation.common

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.RectangleBorder

@Composable
fun AppModuleTopBar(
    @StringRes title: Int,
    @ColorRes colorRes: Int

) {

    val ttsManager = appTTSManager()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        contentAlignment = Alignment.Center
    ) {
        AppShape(
            modifier = Modifier.fillMaxSize(),
            color = colorResource(id = colorRes),
            cornerRadius = RectangleBorder,
            arcHeight = (-30).dp
        )
        AppText(
            text = stringResource(id = title),
            fontFamily = Roboto,
            fontWeight = FontWeight.ExtraBold,
            fontStyle = FontStyle.Normal,
            color = Color.White,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleLarge,
            ttsManager = ttsManager
        )
    }

}