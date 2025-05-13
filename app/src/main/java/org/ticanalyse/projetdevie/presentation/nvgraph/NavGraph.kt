package org.ticanalyse.projetdevie.presentation.nvgraph

import androidx.compose.runtime.Composable
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
                SplashScreen(navController)
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