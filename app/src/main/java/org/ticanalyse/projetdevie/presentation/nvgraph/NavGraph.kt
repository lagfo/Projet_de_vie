package org.ticanalyse.projetdevie.presentation.nvgraph

//import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigator

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
