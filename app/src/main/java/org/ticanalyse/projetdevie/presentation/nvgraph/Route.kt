package org.ticanalyse.projetdevie.presentation.nvgraph

sealed class Route (
    val route:String
){

    object AppStartNavigation: Route("appStartNavigation")
    object AppNavigation: Route("appNavigation")
    object AppNavigationScreen: Route("appNavigationScreen")
    object SplashScreen: Route("SplashScreen")
    object RegisterScreen: Route("RegisterScreen")
    object HomeScreen: Route("homeScreen")

}