package org.ticanalyse.projetdevie.presentation.mon_reseau

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppCategoryGrid
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.common.monReseauCategories
import org.ticanalyse.projetdevie.presentation.nvgraph.MonReseauResumeRoute
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3

@Composable
fun MonReseauCategoriesScreen(navController:NavController,onNavigate: () -> Unit) {
    val ttsManager = appTTSManager()
    val monReseauViewModel = hiltViewModel<MonReseauViewModel>()
    monReseauViewModel.getMonReseau()



    val listActeursFamiliauxEtSociaux = monReseauViewModel.listActeursFamiliaux.collectAsStateWithLifecycle()
    val listActeursEducatifs = monReseauViewModel.listActeursEducatifs.collectAsStateWithLifecycle()
    val listActeursProfessionnels = monReseauViewModel.listActeursProfessionel.collectAsStateWithLifecycle()
    val listActeursInstitutionnelsEtDeSoutien = monReseauViewModel.listActeursInstitutionel.collectAsStateWithLifecycle()

    val mapListActeursFamiliaux = remember { mutableStateOf(mapOf<String, List<String>>()) }
    val mapListActeursEducatifs = remember { mutableStateOf(mapOf<String, List<String>>()) }
    val mapListActeursProfessionnels = remember { mutableStateOf(mapOf<String, List<String>>()) }
    val mapListActeursInstitutionnelsEtDeSoutien = remember { mutableStateOf(mapOf<String, List<String>>()) }

    mapListActeursFamiliaux.value = mapOf(
        "Parents ou tuteurs: " to (listActeursFamiliauxEtSociaux.value?.parentsTuteurs?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Frères, soeurs, cousins ou cousines: " to (listActeursFamiliauxEtSociaux.value?.freresSoeursCousinsCousines?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Voisins" to (listActeursFamiliauxEtSociaux.value?.voisins?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Chefs coutumiers ou religieux: " to (listActeursFamiliauxEtSociaux.value?.chefsCoutumiersReligieux?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Grands-parents: " to (listActeursFamiliauxEtSociaux.value?.grandsParents?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Amis proches: " to (listActeursFamiliauxEtSociaux.value?.amisProches?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Mentor ou modèle dans la communauté: " to (listActeursFamiliauxEtSociaux.value?.mentorModeleCommunaute?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Leaders communautaires ou d'associations locales: " to (listActeursFamiliauxEtSociaux.value?.leadersCommunautairesAssociationsLocales?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
    )

    mapListActeursEducatifs.value = mapOf(
        "Enseignants ou professeurs: " to (listActeursEducatifs.value?.EnseignantsProfesseurs?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Encadreurs de centres de formation professionnelle: " to (listActeursEducatifs.value?.EncadreursCentresFormationProfessionnelle?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Anciens camarades de classe: " to (listActeursEducatifs.value?.anciensCamaradesClasse?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Conseillers d'orientation scolaire ou professionnelle: " to (listActeursEducatifs.value?.ConseillersOrientationScolaireProfessionnelle?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Animateurs d'ONG éducatives: " to (listActeursEducatifs.value?.animateursONGEducatives?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
    )

    mapListActeursProfessionnels.value = mapOf(
        "Anciens employeurs ou maîtres d'apprentissage: " to (listActeursProfessionnels.value?.anciensEmployeursMaitresApprentissage?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Employés d'ONG ou de projets de développement: " to (listActeursProfessionnels.value?.employesONGProjetsDeveloppement?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Artisans ou entrepreneurs locaux: " to (listActeursProfessionnels.value?.artisansEntrepreneursLocaux?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Personnes ressources dans les coopératives, groupements ou mutuelles: " to (listActeursProfessionnels.value?.personnesRessourcesCooperativesGroupementsMutuelles?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
    )

    mapListActeursInstitutionnelsEtDeSoutien.value = mapOf(
        "Agents des services sociaux ou administratifs: " to (listActeursInstitutionnelsEtDeSoutien.value?.agentsServicesSociauxAdministratifs?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Representants de structures comme une agence nationale pour l'emploi: " to (listActeursInstitutionnelsEtDeSoutien.value?.representantsStructuresCommeAgenceNationaleEmploi?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Formateurs des programmes publics ou privés de formation et insertion: " to (listActeursInstitutionnelsEtDeSoutien.value?.formateursProgrammesPublicsPrivesFormationInsertion?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
        "Personnel de santé: " to (listActeursInstitutionnelsEtDeSoutien.value?.personnelSante?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.07f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MediumPadding3, bottom = MediumPadding1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppText(
                        text = stringResource(id = R.string.mon_reseau_categories_title),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Normal,
                        color = colorResource(id = R.color.text),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        ttsManager = ttsManager
                    )
                    Spacer(modifier = Modifier.height(MediumPadding1))

                    AppCategoryGrid(icons = monReseauCategories, column = 2, navController = navController)
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButton(text = "Ligne de Vie", onClick = onNavigate )

                if(
                    mapListActeursFamiliaux.value.any { it.value.isNotEmpty() } ||
                    mapListActeursEducatifs.value.any { it.value.isNotEmpty() } ||
                    mapListActeursProfessionnels.value.any { it.value.isNotEmpty() } ||
                    mapListActeursInstitutionnelsEtDeSoutien.value.any { it.value.isNotEmpty() }

                    ){

                    Spacer(modifier = Modifier.width(14.dp))

                    AppButton(text = "Voir mon réseau", onClick = {navController.navigate(MonReseauResumeRoute)})

                }

            }
        }

    }
}






