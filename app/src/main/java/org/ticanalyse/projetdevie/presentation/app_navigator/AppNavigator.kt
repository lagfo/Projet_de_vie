package org.ticanalyse.projetdevie.presentation.app_navigator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import org.ticanalyse.projetdevie.presentation.common.AppResetModal
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.BottomNavigationItem
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.home.HomeScreen
import org.ticanalyse.projetdevie.presentation.introduction.IntroductionCharactersScreen
import org.ticanalyse.projetdevie.presentation.introduction.IntroductionHomeScreen
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.FormulaireScreen
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.LienVieReelPdfViewerScreen
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.LienVieReelResume
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.LienVieReelScreen
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.RecapitulatifLienVieReelScreen
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LienDeVieIntroductionScreen
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LigneDeVieScreen
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.RecapitulatifScreen
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauCategoriesScreen
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauIntroductionScreen
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauResumeScreen
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
import org.ticanalyse.projetdevie.presentation.nvgraph.LienVieReelPdfViewerRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.LigneDeVieElementRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.LigneDeVieRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauCategoriesRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauIntroductionRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauResumeRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauSubCategoriesRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanActionEditRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanActionTableRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanificationProjetEtapeRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanificationProjetPdfViewerRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanificationProjetResumeRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanificationProjetRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.ProfileRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.RecapitulatifLienVieReelRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.RecapitulatifRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.ResumeLienVieReelRoute
import org.ticanalyse.projetdevie.presentation.planification_de_projet.PdfViewerScreen
import org.ticanalyse.projetdevie.presentation.planification_de_projet.PlanActionEdit
import org.ticanalyse.projetdevie.presentation.planification_de_projet.PlanActionTable
import org.ticanalyse.projetdevie.presentation.planification_de_projet.PlanificationProjetEtapeScreen
import org.ticanalyse.projetdevie.presentation.planification_de_projet.PlanificationProjetScreen
import org.ticanalyse.projetdevie.presentation.planification_de_projet.ResumePlanificationProjetScreen
import org.ticanalyse.projetdevie.ui.theme.Roboto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigator() {
    val ttsManager = appTTSManager()
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(icon = Icons.Filled.Home, description = "Accueil")
        )
    }

    val viewModel = hiltViewModel<AppNavigationViewModel>()
    var showBottomSheet by remember { mutableStateOf(false) }

    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable {
        mutableIntStateOf(0)
    }


    selectedItem = when {
        backStackState?.destination?.route == HomeRoute::class.qualifiedName -> 0
        backStackState?.destination?.route == IntroductionRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == IntroductionCharacterRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanificationProjetRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanificationProjetResumeRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanificationProjetPdfViewerRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == MonReseauIntroductionRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == MonReseauCategoriesRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == LigneDeVieRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == LigneDeVieElementRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == RecapitulatifRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == LienVieReelIntroductionScreenRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == FormulaireScreenRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == RecapitulatifLienVieReelRoute::class.qualifiedName ->-1
        backStackState?.destination?.route?.startsWith(
            MonReseauSubCategoriesRoute::class.qualifiedName ?: ""
        ) == true -> -1

        backStackState?.destination?.route == BilanCompetanceIntroductionRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == BilanCompetanceRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == BilanCompetenceResumeRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanificationProjetRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanificationProjetResumeRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanificationProjetEtapeRoute::class.qualifiedName -> -1
        backStackState?.destination?.route == PlanActionTableRoute::class.qualifiedName -> -1
        backStackState?.destination?.route?.startsWith(
            PlanActionEditRoute::class.qualifiedName ?: ""
        ) == true -> -1
        backStackState?.destination?.route == MonReseauResumeRoute::class.qualifiedName -> -1
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

                TopAppBar(
                    title= {
                        AppText(
                            text = "Mon projet de vie",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            color = Color.White,
                            fontSize = 25.sp,
                            ttsManager = ttsManager
                        )
                    },
                    navigationIcon={
                        Icon(painter = painterResource(id =R.drawable.logo),contentDescription = "logo", modifier = Modifier.size(20.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorResource(R.color.primary_color),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    actions={
                        IconButton(
                            onClick = {
                                showBottomSheet = true
                            }
                        ){
                            Icon(imageVector = Icons.Filled.ChangeCircle,contentDescription = null, tint=Color.White, modifier = Modifier.size(30.dp))
                        }
                    }
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
                    backStackState?.destination?.route == IntroductionCharacterRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.introduction_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == MonReseauIntroductionRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.mon_reseau_title,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route == MonReseauCategoriesRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.mon_reseau_title,
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
                    backStackState?.destination?.route == ResumeLienVieReelRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.lien_vie_reelle,
                        R.color.primary_color
                    )
                    backStackState?.destination?.route == LienVieReelPdfViewerRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.lien_vie_reelle,
                        R.color.primary_color
                    )

                    backStackState?.destination?.route?.startsWith(
                        MonReseauSubCategoriesRoute::class.qualifiedName ?: ""
                    ) == true -> {
                        val category = backStackState.arguments?.getString("category")
                        when (category) {
                            "acteurFamiliauxSociaux" -> AppModuleTopBar(
                                title = R.string.mon_reseau_title,
                                R.color.thirty_color
                            )

                            "acteurProfessionnel" -> AppModuleTopBar(
                                title = R.string.mon_reseau_title,
                                R.color.secondary_color
                            )

                            "acteurEducatif" -> AppModuleTopBar(
                                title = R.string.mon_reseau_title,
                                R.color.primary_color
                            )

                            "acteurInstitutionnel" -> AppModuleTopBar(
                                title = R.string.mon_reseau_title,
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
                    backStackState?.destination?.route == PlanificationProjetEtapeRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.planification_projet_title,
                        R.color.primary_color
                    )
                    backStackState?.destination?.route == PlanActionTableRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.planification_projet_title,
                        R.color.primary_color
                    )


                    backStackState?.destination?.route?.startsWith(
                        PlanActionEditRoute::class.qualifiedName ?: ""
                    ) == true -> {
                        AppModuleTopBar(
                            title = R.string.planification_projet_title,
                            R.color.primary_color
                        )

                    }

                    backStackState?.destination?.route == MonReseauResumeRoute::class.qualifiedName -> AppModuleTopBar(
                        title = R.string.mon_reseau_title,
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
                    when (index) {
                        0 -> navController.navigate(
                            HomeRoute
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

            composable<MonReseauResumeRoute> {
                MonReseauResumeScreen(
                    navController = navController,
                    onNavigate = {
                        navigateToScreen(
                            navController = navController,
                            route = LigneDeVieRoute
                        )
                    }
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
                        navController.navigate(PlanificationProjetRoute)
                    },
                    onNavigatePdf = {
                        navController.navigate(ResumeLienVieReelRoute)
                    }
                )
            }
            composable<ResumeLienVieReelRoute> {
                LienVieReelResume (
                    onNavigate = {
                        navController.navigate(LienVieReelPdfViewerRoute)
                    }
                )
            }
            composable<LienVieReelPdfViewerRoute> {
                LienVieReelPdfViewerScreen()
            }

            composable<BilanCompetanceIntroductionRoute> {
                BilanCompetanceIntroductionScreen {from->
                    navigateToScreen(navController = navController, route = BilanCompetanceRoute(from))
                }
            }

            composable<BilanCompetanceRoute> {
                val args=it.toRoute<BilanCompetanceRoute>()
                BilanCompetanceScreen(
                    navController = navController,
                    from=args.from,
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
                   // navController.navigate(PlanificationProjetResumeRoute)
                    navController.navigate(PlanificationProjetEtapeRoute)
                }
            }
            composable<PlanActionTableRoute>{
                PlanActionTable{
                    it->
                    navController.navigate(PlanActionEditRoute(it))
                }
            }

            composable<PlanActionEditRoute>{
                val args=it.toRoute<PlanActionEditRoute>()
                PlanActionEdit(
                    id=args.id,
                    onNavigate={navController.navigateUp()
                })
            }
            composable<PlanificationProjetEtapeRoute> {
//                ResumePlanificationProjetScreen {
//                    navController.navigate(PlanificationProjetPdfViewerRoute)
//                }
                PlanificationProjetEtapeScreen {from->
                    if(from=="planification"){
                        navController.navigate(BilanCompetanceRoute(from))
                    }else if(from=="tableau"){
                        navController.navigate(PlanActionTableRoute)
                    } else if(from=="pdf"){
                        navController.navigate(PlanificationProjetResumeRoute)
                    }
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

    AppResetModal(
        showBottomSheet = showBottomSheet,
        onDismissRequest = { showBottomSheet = false }
    )
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