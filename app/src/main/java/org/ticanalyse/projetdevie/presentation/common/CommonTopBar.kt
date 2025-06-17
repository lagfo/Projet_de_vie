package org.ticanalyse.projetdevie.presentation.common


import android.icu.text.CaseMap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.Roboto

@Composable
fun CommonTopBar(
    modifier: Modifier = Modifier,
    backGroundColor:Color=colorResource(R.color.primary_color),
    fontFamily: FontFamily=Roboto,
    title:String="",
    titleSize: TextStyle=TextStyle(fontSize =24.sp),
    fontWeight: FontWeight= FontWeight.Bold,
    titleColor: Color=Color.White
) {
    Box(
        modifier= Modifier.fillMaxWidth()
            .height(70.dp)
            .background(backGroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text=title.uppercase(),
            fontFamily =fontFamily,
            style =titleSize,
            fontWeight=fontWeight,
            color=titleColor
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CommonTopBarPreview(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier= Modifier.fillMaxSize()
    ){
        CommonTopBar(
            backGroundColor=colorResource(R.color.primary_color),
            title ="Découvrir mon réseau"
        )
    }
}