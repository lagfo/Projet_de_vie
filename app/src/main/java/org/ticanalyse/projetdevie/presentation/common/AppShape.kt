package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme
import org.ticanalyse.projetdevie.utils.Dimens.ArcHeight
import org.ticanalyse.projetdevie.utils.Dimens.RectangleBorder
import org.ticanalyse.projetdevie.utils.Dimens.RectangleBorderRounded

@Composable
fun AppShape(
    modifier: Modifier = Modifier,
    color: Color = colorResource(id = R.color.primary_color),
    cornerRadius: Dp,
    arcHeight: Dp,
    bottomCornerRadius: Dp = cornerRadius // New parameter for bottom corners
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
    ) {
        val width = size.width
        val height = size.height
        val corner = cornerRadius.toPx()
        val arc = arcHeight.toPx()
        val bottomCorner = bottomCornerRadius.toPx()

        val path = Path().apply {
            moveTo(0f, corner)

            // Top-left rounded corner
            quadraticTo(0f, 0f, corner, 0f)

            // Top edge
            lineTo(width - corner, 0f)

            // Top-right rounded corner
            quadraticTo(width, 0f, width, corner)

            // Right edge down to bottom corner start
            lineTo(width, height - arc - bottomCorner)

            // Bottom-right rounded corner (before the arc)
            quadraticTo(
                width, height - arc,
                width - bottomCorner, height - arc
            )

            // Curved bottom edge with arc
            quadraticTo(
                width / 2, height + arc,
                bottomCorner, height - arc
            )

            // Bottom-left rounded corner (after the arc)
            quadraticTo(
                0f, height - arc,
                0f, height - arc - bottomCorner
            )

            // Left edge back to start
            lineTo(0f, corner)

            close()
        }

        drawPath(path, color)
    }
}