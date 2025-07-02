package org.ticanalyse.projetdevie.presentation.nvgraph

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute

@Serializable
object AppNavigation

@Serializable
object SplashRoute:AppRoute

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object HomeRoute:AppRoute

@Serializable
object ProfileRoute:AppRoute

@Serializable
object IntroductionRoute:AppRoute

@Serializable
object IntroductionCharacterRoute:AppRoute

@Serializable
object DiscoverMyNetworkRoute:AppRoute

@Serializable
object MonReseauIntroductionRoute:AppRoute

@Serializable
object MonReseauCategoriesRoute:AppRoute

@Serializable
data class MonReseauSubCategoriesRoute(val category: String,val column: Int):AppRoute

@Serializable
object LigneDeVieRoute: AppRoute

@Serializable
object BilanCompetanceIntroductionRoute:AppRoute

@Serializable
object BilanCompetanceRoute:AppRoute
@Serializable
object RecapitulatifRoute: AppRoute
@Serializable
object LigneDeVieElementRoute: AppRoute

@Serializable
object LienVieReelIntroductionScreenRoute: AppRoute
@Serializable
object FormulaireScreenRoute: AppRoute


@Serializable
object PlanificationProjetRoute:AppRoute

@Serializable
object PlanificationProjetResumeRoute:AppRoute

@Serializable
object BilanCompetenceResumeRoute:AppRoute


//@Serializable
//data class HomeRoute (
//    val id: Int? = null,
//    val nom: String,
//    val prenom: String,
//    val age: String,
//    val numTel: String,
//    val genre: String,
//    val avatarUri: String
//)