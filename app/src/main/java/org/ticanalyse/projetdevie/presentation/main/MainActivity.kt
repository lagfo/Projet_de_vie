package org.ticanalyse.projetdevie.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import org.ticanalyse.projetdevie.presentation.nvgraph.NavGraph
import org.ticanalyse.projetdevie.presentation.nvgraph.Route
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //WindowCompat.setDecorFitsSystemWindows(window, false)
        //enableEdgeToEdge()
        setContent {
            ProjetDeVieTheme {
                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkMode
                    )
                }
                Box(modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize()) {
                    val startDestination = Route.AppStartNavigation.route
                    NavGraph(startDestination = startDestination)
                }
            }
        }
    }
}