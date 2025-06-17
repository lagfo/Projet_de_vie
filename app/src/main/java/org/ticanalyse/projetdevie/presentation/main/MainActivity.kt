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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigator
import org.ticanalyse.projetdevie.presentation.nvgraph.AppNavigation
import org.ticanalyse.projetdevie.presentation.nvgraph.RegisterRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.SplashRoute
import org.ticanalyse.projetdevie.presentation.register.RegisterScreen
import org.ticanalyse.projetdevie.presentation.register.RegisterViewModel
import org.ticanalyse.projetdevie.presentation.splash.SplashScreen
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    private val viewModel by viewModels<RegisterViewModel>()

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

            val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
            val currentUser = viewModel.currentUser.collectAsStateWithLifecycle()
            val scope = rememberCoroutineScope()

            ProjetDeVieTheme {
                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
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
                            LaunchedEffect(isLoading.value) {
                                delay(1500)
                                if (currentUser.value != null) {
                                    navController.navigate(AppNavigation) {
                                        popUpTo<SplashRoute> {
                                            inclusive = true
                                        }
                                    }
                                } else if (currentUser.value == null && !isLoading.value) {
                                    navController.navigate(RegisterRoute) {
                                        popUpTo<SplashRoute> {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                            SplashScreen()
                        }
                        composable<RegisterRoute> {
                            RegisterScreen(
                                onSubmitClick = { user ->
                                    viewModel.onSubmit(user)
                                    scope.launch {
                                        delay(1500)
                                        navController.navigate(AppNavigation) {
                                            popUpTo<RegisterRoute> {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        composable<AppNavigation> {
                            LaunchedEffect(currentUser.value) {
                                if (currentUser.value == null) {
                                    navController.navigate(RegisterRoute) {
                                        popUpTo<AppNavigation>{inclusive = true}
                                    }
                                }
                            }
                            AppNavigator(currentUser = currentUser.value!!)
                        }



                        }
                    }
//                    val startDestination = viewModel.startDestination
//                    NavGraph(startDestination = startDestination)
                }
            }
        }
    }

