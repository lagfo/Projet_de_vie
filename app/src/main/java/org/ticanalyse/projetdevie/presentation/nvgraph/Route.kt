package org.ticanalyse.projetdevie.presentation.nvgraph

sealed class Route (
    val route:String
){

    object AppStartNavigation: Route("appStartNavigation")
    object SplashScreen: Route("SplashScreen")
    object LoginScreen: Route("LoginScreen")
    object RegisterScreen: Route("RegisterScreen")
    object HomeScreen: Route("homeScreen")

}