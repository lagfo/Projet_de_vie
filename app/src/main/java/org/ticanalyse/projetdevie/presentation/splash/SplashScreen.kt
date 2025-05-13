package org.ticanalyse.projetdevie.presentation.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.nvgraph.Route
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit
) {
    val userEntry by viewModel.userEntry.collectAsState()
    LaunchedEffect(Unit) {
        delay(2000)
        when (userEntry) {
            true -> onNavigate(Route.LoginScreen.route)
            false -> onNavigate(Route.RegisterScreen.route)
            null -> {}
        }
    }

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
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    fontSize = 25.sp,
                )
                Text(
                    text = stringResource(id = R.string.splash_body),
                    color = colorResource(id = R.color.text),
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 15.sp
                )

                DotsLoadingIndicator()
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
                Spacer(modifier = Modifier.height(50.dp)) // Exemple de padding
            }
        }
    }
}

@Composable
fun DotsLoadingIndicator(
    dotCount: Int = 8,
    dotSize: Dp = 4.dp,
    radius: Dp = 50.dp,
    color: Color = colorResource(id = R.color.primary_color),
    animationDuration: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDuration, easing = LinearEasing)
        )
    )

    Box(
        modifier = Modifier.size(radius * 2),
        contentAlignment = Alignment.Center
    ) {
        repeat(dotCount) { index ->
            val angle = (360f / dotCount) * index + angleOffset
            val angleRad = Math.toRadians(angle.toDouble())
            val x = cos(angleRad) * radius
            val y = sin(angleRad) * radius

            Box(
                modifier = Modifier
                    .offset { IntOffset(x.value.roundToInt(), y.value.roundToInt()) }
                    .size(dotSize)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}
