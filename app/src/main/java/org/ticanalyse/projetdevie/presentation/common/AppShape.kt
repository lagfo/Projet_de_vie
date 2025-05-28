package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import org.ticanalyse.projetdevie.R

@Composable
fun AppShape(
    modifier: Modifier = Modifier,
    color: Color = colorResource(id = R.color.primary_color),
    cornerRadius: Dp,
    arcHeight: Dp
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val width = size.width
        val height = size.height
        val corner = cornerRadius.toPx()
        val arc = arcHeight.toPx()

        val path = Path().apply {
            moveTo(0f, corner)
            quadraticBezierTo(0f, 0f, corner, 0f)
            lineTo(width - corner, 0f)
            quadraticBezierTo(width, 0f, width, corner)
            lineTo(width, height - arc)
            quadraticTo(
                width / 2, height + arc,
                0f, height - arc
            )
            close()
        }

        drawPath(path, color)
    }
}