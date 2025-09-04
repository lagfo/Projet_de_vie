package org.ticanalyse.projetdevie.presentation.main

//import org.ticanalyse.projetdevie.presentation.nvgraph.NavGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigator
import org.ticanalyse.projetdevie.presentation.nvgraph.AppNavigation
import org.ticanalyse.projetdevie.presentation.nvgraph.SplashRoute
import org.ticanalyse.projetdevie.presentation.splash.SplashScreen
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configuration du Splash Screen
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                mainViewModel.splashCondition
            }
        }
        // Configuration edge-to-edge
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        //enableEdgeToEdge()

//        val splashScreen = installSplashScreen()

        setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()

            ProjetDeVieTheme {
                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.White,
                        darkIcons = !isSystemInDarkMode
                    )
                }
                val systemBarsPadding = WindowInsets.safeDrawing.asPaddingValues()
                Box(modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(systemBarsPadding)) {
                    NavHost(
                        navController = navController,
                        startDestination = SplashRoute
                    ) {
                        composable<SplashRoute> {
                            LaunchedEffect(Unit) {
                                delay(2000)
                                navController.navigate(AppNavigation) {
                                    popUpTo<SplashRoute> {
                                        inclusive = true
                                    }
                                }
                            }
                            SplashScreen()
                        }

                        composable<AppNavigation> {

                            AppNavigator()
                        }

                        }
                    }
                }
            }
        }
    }

