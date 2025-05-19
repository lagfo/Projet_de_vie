package org.ticanalyse.projetdevie.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import org.ticanalyse.projetdevie.R

@Composable
private fun RegisterHeader() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.4f)
    ) {
        val (banner) = createRefs()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(bottomStartPercent = 50, bottomEndPercent = 50))
                .constrainAs(banner) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.register_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.65f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStartPercent = 50, bottomEndPercent = 50))
                    .background(colorResource(id = R.color.primary_color).copy(alpha = 0.5f))
            )
        }
    }
}