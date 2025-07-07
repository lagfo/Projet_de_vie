package org.ticanalyse.projetdevie.presentation.app_navigator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetanceIntroductionScreen
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetanceScreen
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetenceResumeScreen
import org.ticanalyse.projetdevie.presentation.common.AppBottomNavigation
import org.ticanalyse.projetdevie.presentation.common.AppModuleTopBar
import org.ticanalyse.projetdevie.presentation.common.BottomNavigationItem
import org.ticanalyse.projetdevie.presentation.common.TopBarComponent
import org.ticanalyse.projetdevie.presentation.home.HomeScreen
import org.ticanalyse.projetdevie.presentation.introduction.IntroductionCharactersScreen
import org.ticanalyse.projetdevie.presentation.introduction.IntroductionHomeScreen
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.FormulaireScreen
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.LienVieReelScreen
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.RecapitulatifLienVieReelScreen
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LienDeVieIntroductionScreen
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LigneDeVieScreen
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.RecapitulatifScreen
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauCategoriesScreen
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauIntroductionScreen
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauSubCategoriesScreen
import org.ticanalyse.projetdevie.presentation.nvgraph.AppRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.BilanCompetanceIntroductionRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.BilanCompetanceRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.BilanCompetenceResumeRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.FormulaireScreenRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.HomeRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.IntroductionCharacterRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.IntroductionRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.LienVieReelIntroductionScreenRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.LigneDeVieElementRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.LigneDeVieRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauCategoriesRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauIntroductionRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauSubCategoriesRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanificationProjetPdfViewerRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanificationProjetResumeRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanificationProjetRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.ProfileRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.RecapitulatifLienVieReelRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.RecapitulatifRoute
import org.ticanalyse.projetdevie.presentation.planification_de_projet.PdfViewerScreen
import org.ticanalyse.projetdevie.presentation.planification_de_projet.PlanificationProjetScreen
import org.ticanalyse.projetdevie.presentation.planification_de_projet.ResumePlanificationProjetScreen
import org.ticanalyse.projetdevie.presentation.profile.ProfileScreen
import timber.log.Timber

@Composable
fun AppNavigator() {

    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(icon = Icons.Filled.Home, description = "Accueil"),
            BottomNavigationItem(icon = Icons.Filled.Person, description = "Profil")
        )
    }

    val viewModel = hiltViewModel<AppNavigationViewModel>()
    val currentUserState by viewModel.currentUser.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }


    selectedItem = when {
        backStackState?.destination?.route == HomeRoute::class.qualifiedName -> 0
        backStackState?.destination?.route == ProfileRoute::class.qualifiedName -> 1
        backStackState?.destination?.route == IntroductionRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == MonReseauIntroductionRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == MonReseauCategoriesRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == LigneDeVieRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == LienVieReelIntroductionScreenRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == FormulaireScreenRoute::class.qualifiedName -> -1
        backStackState?.destination?.route?.startsWith(
            MonReseauSubCategoriesRoute::class.qualifiedName ?: ""
        ) == true -> -1

        backStackState?.destination?.route == BilanCompetanceIntroductionRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == BilanCompetanceRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == BilanCompetenceResumeRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanificationProjetRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanificationProjetResumeRoute::class.qualifiedName -> -1
        else -> 0
    }

    val isHomeTopBarVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == HomeRoute::class.qualifiedName
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            if (isHomeTopBarVisible) {
                TopBarComponent(
                    currentUserState?.nom ?: "",
                    currentUserState?.prenom ?: "",
                    currentUserState?.avatarUri ?: ""
                )
            } else {

                when {
                    backStackState?.destination?.route == HomeRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.introduction_title,
                        R.color.primary_color
                    )
                    //backStackState?.destination?.route == ProfileRoute::class.qualifiedName -> AppModuleTopBar(title = R.string.introduction_title,R.color.primary_color)
                    backStackState?.destination?.route == IntroductionRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.introduction_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == MonReseauIntroductionRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.introduction_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == MonReseauCategoriesRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.introduction_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == LigneDeVieElementRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.ligne_vie,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == LigneDeVieRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.ligne_vie,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == RecapitulatifRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.ligne_vie,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == LienVieReelIntroductionScreenRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.lien_vie_reelle,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == FormulaireScreenRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.lien_vie_reelle,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == RecapitulatifLienVieReelRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.lien_vie_reelle,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route?.startsWith(
                        MonReseauSubCategoriesRoute::class.qualifiedName ?: ""
                    ) == true -> {
                        val category = backStackState.arguments?.getString("category")
                        when (category) {
                            "acteurFamiliauxSociaux" -> AppModuleTopBar(
                                title = R.string.introduction_title,
                                R.color.thirty_color
                            )

                            "acteurProfessionnel" -> AppModuleTopBar(
                                title = R.string.introduction_title,
                                R.color.secondary_color
                            )

                            "acteurEducatif" -> AppModuleTopBar(
                                title = R.string.introduction_title,
                                R.color.primary_color
                            )

                            "acteurInstitutionnel" -> AppModuleTopBar(
                                title = R.string.introduction_title,
                                R.color.fourty_color
                            )
                        }
                    }

                    backStackState?.destination?.route == BilanCompetanceIntroductionRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.bilan_competance_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == BilanCompetanceRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.bilan_competance_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == BilanCompetenceResumeRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.bilan_competance_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == PlanificationProjetRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.planification_projet_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == PlanificationProjetResumeRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.planification_projet_title,
                        R.color.primary_color
                    )

                }
                !isHomeTopBarVisible
            }
        },
        bottomBar = {
            AppBottomNavigation(
                items = bottomNavigationItems,
                selectedItem = selectedItem,
                onItemClick = { index ->
                    Timber.tag("timber").d("index : $index")
                    when (index) {
                        0 -> navController.navigate(
                            HomeRoute
                        )

                        1 -> navigateToScreen(
                            navController = navController,
                            route = ProfileRoute
                        )

                    }
                }
            )
        }
    ) {
        val bottomPadding = it.calculateBottomPadding()
        val topPadding = it.calculateTopPadding()
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(top = topPadding, bottom = bottomPadding)
        ) {

            composable<HomeRoute> {
                HomeScreen(
                    onNavigate = { route ->
                        when (route) {
                            "Introduction" -> {
                                navController.navigate(IntroductionRoute)
                            }

                            "Mon Reseau" -> {
                                navController.navigate(MonReseauIntroductionRoute)
                            }

                            "Ligne de vie" -> {
                                navController.navigate(LigneDeVieRoute)
                            }

                            "Bilan" -> {
                                navController.navigate(BilanCompetanceIntroductionRoute)
                            }

                            "Lien vie reel" -> {
                                navController.navigate(LienVieReelIntroductionScreenRoute)
                            }

                            "Plannification" -> {
                                navController.navigate(PlanificationProjetRoute)
                            }
                        }
                    }
                )
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    navController = navController,
                    sharedViewModel = viewModel
                )
            }

            composable<IntroductionRoute> {
                IntroductionHomeScreen {
                    navController.navigate(IntroductionCharacterRoute)
                }
            }
            composable<IntroductionCharacterRoute> {
                IntroductionCharactersScreen(
                    onNavigate = {
                        navController.navigate(MonReseauIntroductionRoute) {
                            popUpTo(IntroductionRoute) {
                                inclusive = true
                            }
                        }
                    },
                    onBackPressed = {
                        navController.navigateUp()
                    }
                )
            }

            composable<MonReseauIntroductionRoute> {
                MonReseauIntroductionScreen {
                    navigateToScreen(
                        navController = navController,
                        route = MonReseauCategoriesRoute
                    )
                }
            }

            composable<MonReseauCategoriesRoute> {
                MonReseauCategoriesScreen(
                    navController = navController,
                    onNavigate = {
                        navigateToScreen(
                            navController = navController,
                            route = LigneDeVieRoute
                        )
                    }
                )

            }

            composable<MonReseauSubCategoriesRoute> { route ->
                val arg = route.toRoute<MonReseauSubCategoriesRoute>()

                MonReseauSubCategoriesScreen(
                    navController = navController,
                    category = arg.category,
                    column = arg.column
                )
            }

            composable<LigneDeVieRoute> {
//                LigneDeVieScreen(
//                    onNavigate = {
//                        navController.navigate(RecapitulatifRoute)
//                    }
//                )
                LienDeVieIntroductionScreen(
                    onNavigate = {
                        navController.navigate(LigneDeVieElementRoute)
                    }
                )
            }

            composable<LigneDeVieElementRoute> {
                LigneDeVieScreen(
                    onNavigate = {
                        navController.navigate(RecapitulatifRoute)
                    }
                )

            }

            composable<RecapitulatifRoute> {
                RecapitulatifScreen(
                    onNavigate = {
                        // navController.navigate(LienVieReelIntroductionScreenRoute)
                        navController.navigate(BilanCompetanceIntroductionRoute)
                    }
                )
            }

            composable<LienVieReelIntroductionScreenRoute> {
                LienVieReelScreen(
                    onNavigate = {
                        navController.navigate(FormulaireScreenRoute)
                    }
                )
            }
            composable<FormulaireScreenRoute> {
                FormulaireScreen(
                    onNavigate = {
                        navController.navigate(RecapitulatifLienVieReelRoute)
                    }
                )
            }
            composable<RecapitulatifLienVieReelRoute> {
                RecapitulatifLienVieReelScreen(
                    onNavigate = {
                    }
                )
            }

            composable<BilanCompetanceIntroductionRoute> {
                BilanCompetanceIntroductionScreen {
                    navigateToScreen(navController = navController, route = BilanCompetanceRoute)
                }
            }

            composable<BilanCompetanceRoute> {
                BilanCompetanceScreen(
                    navController = navController,
                    onNavigateToLienAvecLaVieReele = {
                        navigateToScreen(
                            navController = navController,
                            route = LienVieReelIntroductionScreenRoute
                        )
                    }
                )

            }

            composable<BilanCompetenceResumeRoute> {
                BilanCompetenceResumeScreen(
                    navController = navController,
                    onNavigate = {
                        navigateToScreen(
                            navController = navController,
                            route = LienVieReelIntroductionScreenRoute
                        )
                    }
                )

            }

            composable<PlanificationProjetRoute> {
                PlanificationProjetScreen {
                    navController.navigate(PlanificationProjetResumeRoute)
                }
            }
            composable<PlanificationProjetResumeRoute> {
                ResumePlanificationProjetScreen {
                    navController.navigate(PlanificationProjetPdfViewerRoute)
                }
            }
            composable<PlanificationProjetPdfViewerRoute> {
                PdfViewerScreen()
            }
        }

    }
}


@Composable
fun OnBackClickStateSaver(navController: NavController, route: AppRoute) {
    BackHandler(true) {
        navigateToScreen(
            navController = navController,
            route = route
        )
    }
}

private fun navigateToScreen(navController: NavController, route: AppRoute) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { screen_route ->
            popUpTo(screen_route) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
    }
}