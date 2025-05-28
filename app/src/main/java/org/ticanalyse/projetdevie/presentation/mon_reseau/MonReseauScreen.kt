package org.ticanalyse.projetdevie.presentation.mon_reseau

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun MonReseauScreen (){

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CustomFormShape(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.05f),
            color = Color(0xFF00334D),
            cornerRadius = 0.dp,
            arcHeight = (-30).dp
        )

        Spacer(modifier = Modifier.height(50.dp))

        CustomFormShape(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.9f),
            color = Color(0xFFD2691E),
            cornerRadius = 45.dp,
            arcHeight = (-30).dp
        )
    }

}

@Composable
fun CustomFormShape(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFD2691E),
    cornerRadius: Dp,
    arcHeight: Dp

) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val width = size.width
        val height = size.height
        val cornerRadius = cornerRadius.toPx()
        val arcHeight = arcHeight.toPx()

        val path = Path().apply {
            moveTo(0f, cornerRadius)
            quadraticBezierTo(0f, 0f, cornerRadius, 0f)
            lineTo(width - cornerRadius, 0f)
            quadraticBezierTo(width, 0f, width, cornerRadius)
            lineTo(width, height - arcHeight)
            quadraticTo(
                width / 2, height + arcHeight,
                0f, height - arcHeight
            )
            close()
        }

        drawPath(path, color)
    }
}

@Composable
fun CustomTitleShape(modifier: Modifier = Modifier) {

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.1f)
    ) {
        val width = size.width
        val height = size.height
        val cornerRadius = 0.dp.toPx()
        val bottomCurveHeight = -30.dp.toPx()

        val path = Path().apply {
            // Coin supérieur gauche arrondi
            moveTo(0f, cornerRadius)
            quadraticBezierTo(0f, 0f, cornerRadius, 0f)
            lineTo(width - cornerRadius, 0f)
            // Coin supérieur droit arrondi
            quadraticBezierTo(width, 0f, width, cornerRadius)
            lineTo(width, height - bottomCurveHeight)
            // Courbe intérieure en bas
            quadraticBezierTo(
                width / 2, height + bottomCurveHeight,
                0f, height - bottomCurveHeight
            )
            close()
        }

        drawPath(
            path = path,
            color = Color(0xFF00334D) // Orange foncé, ajuste selon ton besoin
        )
    }
}

@Composable
@Preview(showBackground = true)
fun MonReseauScreenPreview() {
    MonReseauScreen()
}
