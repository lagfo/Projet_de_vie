package org.ticanalyse.projetdevie.presentation.nvgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import org.ticanalyse.projetdevie.presentation.login.LoginScreen
import org.ticanalyse.projetdevie.presentation.register.RegisterScreen
import org.ticanalyse.projetdevie.presentation.splash.SplashScreen

@Composable
fun NavGraph( startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination){
        navigation(
            route = Route.AppStartNavigation.route,
            startDestination = Route.SplashScreen.route
        ){
            composable(
                route = Route.SplashScreen.route
            ){
                SplashScreen { route ->
                    navigateToScreen(
                        navController=navController,
                        route=route,
                        popUpToRoute = Route.SplashScreen.route,
                        inclusive = true
                        )
                }
            }

            composable(
                route = Route.LoginScreen.route
            ){
                LoginScreen()
            }

            composable(
                route = Route.RegisterScreen.route
            ){
                RegisterScreen()
            }
        }

    }
}

private fun navigateToScreen(
    navController: NavController,
    route: String,
    popUpToRoute: String? = null,
    inclusive: Boolean = false
) {
    navController.navigate(route) {
        popUpToRoute?.let { routeToPop ->
            popUpTo(routeToPop) {
                this.inclusive = inclusive
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}
