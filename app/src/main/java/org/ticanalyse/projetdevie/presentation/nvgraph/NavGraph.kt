package org.ticanalyse.projetdevie.presentation.nvgraph

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
//import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigator
import org.ticanalyse.projetdevie.presentation.home.HomeScreen
import org.ticanalyse.projetdevie.presentation.register.RegisterScreen
import org.ticanalyse.projetdevie.presentation.register.RegisterViewModel
import org.ticanalyse.projetdevie.presentation.splash.SplashScreen

//@Composable
//fun NavGraph( startDestination: ) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = startDestination){
//        navigation(
//            route = Route.AppStartNavigation.route,
//            startDestination = Route.SplashScreen.route
//        ){
//            composable(
//                route = Route.SplashScreen.route
//            ){
//                SplashScreen(navController=navController)
//            }
//
//            composable(
//                route = Route.RegisterScreen.route
//            ){
//                val viewModel: RegisterViewModel = hiltViewModel()
//                RegisterScreen(onEvent = viewModel::onEvent)
//            }
//        }
//
//        navigation(
//            route = Route.AppNavigation.route,
//            startDestination = Route.AppNavigationScreen.route
//        ){
//            composable(
//                route = Route.AppNavigationScreen.route
//            ) {
//                AppNavigator()
//            }
//
//        }
//
//
//    }
//}
