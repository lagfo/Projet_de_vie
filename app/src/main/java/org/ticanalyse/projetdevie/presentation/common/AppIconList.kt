package org.ticanalyse.projetdevie.presentation.common

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.nvgraph.AppRoute
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauSubCategoriesRoute

data class AppIcon(
    val route: AppRoute,
    @StringRes val txt: Int,
    @ColorRes val strokeColor: Int,
    @DrawableRes val paint: Int,
)

data class AppSubIcon(
    val category: String,
    @StringRes val txt: Int,
    @ColorRes val strokeColor: Int,
    @DrawableRes val paint: Int,
)

data class AppSkillCardIcon(
    @StringRes val txt: Int,
    @ColorRes val strokeColor: Int,
    @DrawableRes val paint: Int,
)

val monReseauCategories = listOf(
    AppIcon(
        route = MonReseauSubCategoriesRoute(category="acteurFamiliauxSociaux", column = 2),
        txt = R.string.acteur_familiaux_sociaux,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.acteur_familiaux_sociaux
    ),
    AppIcon(
        route = MonReseauSubCategoriesRoute(category="acteurEducatif", column = 2),
        txt = R.string.acteur_educatif,
        strokeColor = R.color.primary_color,
        paint = R.drawable.acteur_educatif
    ),
    AppIcon(
        route = MonReseauSubCategoriesRoute(category="acteurProfessionnel", column = 1),
        txt = R.string.acteur_professionnel,
        strokeColor = R.color.secondary_color,
        paint = R.drawable.acteur_professionnel
    ),
    AppIcon(
        route = MonReseauSubCategoriesRoute(category="acteurInstitutionnel", column = 1),
        txt = R.string.acteur_institutionnel,
        strokeColor = R.color.fourty_color,
        paint = R.drawable.acteur_institutionnel
    ),

)

@Serializable
sealed interface AppRoute

val acteurFamiliauxSociauxSubCategories = listOf(
    AppSubIcon(
        category = "acteurFamiliauxSociaux",
        txt = R.string.parent_tuteur,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.acteur_familiaux_sociaux
    ),
    AppSubIcon(
        category = "acteurFamiliauxSociaux",
        txt = R.string.frere_soeur,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.freres_soeurs
    ),
    AppSubIcon(
        category = "acteurFamiliauxSociaux",
        txt = R.string.voisins,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.voisins
    ),
    AppSubIcon(
        category = "acteurFamiliauxSociaux",
        txt = R.string.chef_coutumier_religieux,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.chef_religieux_coutumier
    ),
    AppSubIcon(
        category = "acteurFamiliauxSociaux",
        txt = R.string.grands_parents,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.grands_parents
    ),
    AppSubIcon(
        category = "acteurFamiliauxSociaux",
        txt = R.string.amis_proche,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.amis_proche
    ),
    AppSubIcon(
        category = "acteurFamiliauxSociaux",
        txt = R.string.mentors,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.mentor
    ),
    AppSubIcon(
        category = "acteurFamiliauxSociaux",
        txt = R.string.leaders,
        strokeColor = R.color.thirty_color,
        paint = R.drawable.leaders
    )

)

val acteurEducatifSubCategories = listOf(
    AppSubIcon(
        category = "acteurEducatif",
        txt = R.string.enseignants_professeurs,
        strokeColor = R.color.primary_color,
        paint = R.drawable.acteur_educatif
    ),
    AppSubIcon(
        category = "acteurEducatif",
        txt = R.string.encadreurs_centre_formation,
        strokeColor = R.color.primary_color,
        paint = R.drawable.encadreurs_centres_formation_professionnelle
    ),
    AppSubIcon(
        category = "acteurEducatif",
        txt = R.string.anciens_camarades,
        strokeColor = R.color.primary_color,
        paint = R.drawable.anciens_camarades_classe
    ),
    AppSubIcon(
        category = "acteurEducatif",
        txt = R.string.conseiller_orientation,
        strokeColor = R.color.primary_color,
        paint = R.drawable.conseiller_educatif
    ),
    AppSubIcon(
        category = "acteurEducatif",
        txt = R.string.animateur_ong,
        strokeColor = R.color.primary_color,
        paint = R.drawable.animateurs_ong_educatives
    )

)

val acteurProfessionnelSubCategories = listOf(
    AppSubIcon(
        category = "acteurProfessionnel",
        txt = R.string.anciens_emploiyeurs,
        strokeColor = R.color.secondary_color,
        paint = R.drawable.acteur_professionnel
    ),
    AppSubIcon(
        category = "acteurProfessionnel",
        txt = R.string.employe_ong,
        strokeColor = R.color.secondary_color,
        paint = R.drawable.employes_ong_projets_developpement
    ),
    AppSubIcon(
        category = "acteurProfessionnel",
        txt = R.string.artisant_entrepreneur_locaux,
        strokeColor = R.color.secondary_color,
        paint = R.drawable.artisans_entrepreneurs_locaux
    ),
    AppSubIcon(
        category = "acteurProfessionnel",
        txt = R.string.personnes_ressources_cooperative,
        strokeColor = R.color.secondary_color,
        paint = R.drawable.personnes_ressources_cooperatives_groupements_mutuelles
    )

)

val acteurInstitutionnelSubCategories = listOf(
    AppSubIcon(
        category = "acteurInstitutionnel",
        txt = R.string.agents_services_sociaux,
        strokeColor = R.color.fourty_color,
        paint = R.drawable.acteur_institutionnel
    ),
    AppSubIcon(
        category = "acteurInstitutionnel",
        txt = R.string.representant_strutures,
        strokeColor = R.color.fourty_color,
        paint = R.drawable.repr_sentants_structures_agence_nationale_emploi
    ),
    AppSubIcon(
        category = "acteurInstitutionnel",
        txt = R.string.formateur_programmes_publics_prives,
        strokeColor = R.color.fourty_color,
        paint = R.drawable.formateurs_programmes_publics_prives_formation_insertion
    ),
    AppSubIcon(
        category = "acteurInstitutionnel",
        txt = R.string.personnel_sante,
        strokeColor = R.color.fourty_color,
        paint = R.drawable.personnel_sante
    )

)


val skills = listOf(
    AppSkillCardIcon(txt = R.string.skill_agriculture, strokeColor = R.color.primary_color, paint = R.drawable.agriculture),
    AppSkillCardIcon(txt = R.string.skill_apiculture, strokeColor = R.color.primary_color, paint = R.drawable.apiculture),
    AppSkillCardIcon(txt = R.string.skill_arts, strokeColor = R.color.primary_color, paint = R.drawable.arts),
    AppSkillCardIcon(txt = R.string.skill_aviculture, strokeColor = R.color.primary_color, paint = R.drawable.aviculture),
    AppSkillCardIcon(txt = R.string.skill_calcul_mathematique, strokeColor = R.color.primary_color, paint = R.drawable.calcul),
    AppSkillCardIcon(txt = R.string.skill_chauffeur, strokeColor = R.color.primary_color, paint = R.drawable.chauffeur),
    AppSkillCardIcon(txt = R.string.skill_coiffure, strokeColor = R.color.primary_color, paint = R.drawable.coiffure),
    AppSkillCardIcon(txt = R.string.skill_communication, strokeColor = R.color.primary_color, paint = R.drawable.communication),
    AppSkillCardIcon(txt = R.string.skill_comptabilite, strokeColor = R.color.primary_color, paint = R.drawable.comptabilite),
    AppSkillCardIcon(txt = R.string.skill_cordonnerie, strokeColor = R.color.primary_color, paint = R.drawable.cordonnerie),
    AppSkillCardIcon(txt = R.string.skill_couture, strokeColor = R.color.primary_color, paint = R.drawable.couture),
    AppSkillCardIcon(txt = R.string.skill_cuisine, strokeColor = R.color.primary_color, paint = R.drawable.cuisine),
    AppSkillCardIcon(txt = R.string.skill_ecriture, strokeColor = R.color.primary_color, paint = R.drawable.ecriture),
    AppSkillCardIcon(txt = R.string.skill_electricite, strokeColor = R.color.primary_color, paint = R.drawable.electricite),
    AppSkillCardIcon(txt = R.string.skill_elevage, strokeColor = R.color.primary_color, paint = R.drawable.elevage),
    AppSkillCardIcon(txt = R.string.skill_enseignement, strokeColor = R.color.primary_color, paint = R.drawable.enseignement),
    AppSkillCardIcon(txt = R.string.skill_fabrication_savon, strokeColor = R.color.primary_color, paint = R.drawable.fabrication_savon),
    AppSkillCardIcon(txt = R.string.skill_force_ordre, strokeColor = R.color.primary_color, paint = R.drawable.force_ordre),
    AppSkillCardIcon(txt = R.string.skill_froid, strokeColor = R.color.primary_color, paint = R.drawable.froid),
    AppSkillCardIcon(txt = R.string.skill_infirmerie_aidesoignant, strokeColor = R.color.primary_color, paint = R.drawable.infirmerie_aidesoignant),
    AppSkillCardIcon(txt = R.string.skill_infographie, strokeColor = R.color.primary_color, paint = R.drawable.infographie),
    AppSkillCardIcon(txt = R.string.skill_informatique, strokeColor = R.color.primary_color, paint = R.drawable.informatique),
    AppSkillCardIcon(txt = R.string.skill_langues_etrangeres, strokeColor = R.color.primary_color, paint = R.drawable.langues_etrangeres),
    AppSkillCardIcon(txt = R.string.skill_litterature_histoire, strokeColor = R.color.primary_color, paint = R.drawable.litterature_histoire),
    AppSkillCardIcon(txt = R.string.skill_menage, strokeColor = R.color.primary_color, paint = R.drawable.menage),
    AppSkillCardIcon(txt = R.string.skill_maconnerie, strokeColor = R.color.primary_color, paint = R.drawable.maconnerie),
    AppSkillCardIcon(txt = R.string.skill_maraichage, strokeColor = R.color.primary_color, paint = R.drawable.maraichage),
    AppSkillCardIcon(txt = R.string.skill_marketing, strokeColor = R.color.primary_color, paint = R.drawable.marketing),
    AppSkillCardIcon(txt = R.string.skill_mecanique, strokeColor = R.color.primary_color, paint = R.drawable.mecanique),
    AppSkillCardIcon(txt = R.string.skill_menuiserie_ebenisterie_charpenterie, strokeColor = R.color.primary_color, paint = R.drawable.menuiserie_ebenisterie_charpenterie),
    AppSkillCardIcon(txt = R.string.skill_musique_dance, strokeColor = R.color.primary_color, paint = R.drawable.musique_dance),
    AppSkillCardIcon(txt = R.string.skill_peche, strokeColor = R.color.primary_color, paint = R.drawable.peche),
    AppSkillCardIcon(txt = R.string.skill_pisciculture, strokeColor = R.color.primary_color, paint = R.drawable.pisciculture),
    AppSkillCardIcon(txt = R.string.skill_plomberie, strokeColor = R.color.primary_color, paint = R.drawable.plomberie),
    AppSkillCardIcon(txt = R.string.skill_restauration, strokeColor = R.color.primary_color, paint = R.drawable.restauration),
    AppSkillCardIcon(txt = R.string.skill_secretariat, strokeColor = R.color.primary_color, paint = R.drawable.secretariat),
    AppSkillCardIcon(txt = R.string.skill_soudure_menuiserie_metallique, strokeColor = R.color.primary_color, paint = R.drawable.soudure_menuiserie_metallique),
    AppSkillCardIcon(txt = R.string.skill_sport, strokeColor = R.color.primary_color, paint = R.drawable.sport),
    AppSkillCardIcon(txt = R.string.skill_travail_social, strokeColor = R.color.primary_color, paint = R.drawable.travail_social),
    AppSkillCardIcon(txt = R.string.skill_tissage, strokeColor = R.color.primary_color, paint = R.drawable.tissage),
    AppSkillCardIcon(txt = R.string.skill_transformation_produits_agricoles, strokeColor = R.color.primary_color, paint = R.drawable.transformation_produits_agricoles),
    AppSkillCardIcon(txt = R.string.skill_vente_commerce, strokeColor = R.color.primary_color, paint = R.drawable.vente_commerce)
)
