package org.ticanalyse.projetdevie.presentation.planification_de_projet

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maxkeppeker.sheets.core.views.Grid
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetenceViewModel
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LigneDeVieViewModel
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauViewModel
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import org.ticanalyse.projetdevie.utils.ExoPlayer

@Composable
fun ResumePlanificationProjetScreen() {

    val monReseauViewModel = hiltViewModel<MonReseauViewModel>()
    val ligneDeVieViewModel = hiltViewModel<LigneDeVieViewModel>()
    val bilanCompetenceViewModel = hiltViewModel<BilanCompetenceViewModel>()
//    val lienVieReelle = hiltViewModel<LienVie>()

    monReseauViewModel.getMonReseau()
    bilanCompetenceViewModel.getSkills()
    val listActeursFamiliauxEtSociaux = monReseauViewModel.listActeursFamiliaux.collectAsStateWithLifecycle()
    val listActeursEducatifs = monReseauViewModel.listActeursEducatifs.collectAsStateWithLifecycle()
    val listActeursProfessionnels = monReseauViewModel.listActeursProfessionel.collectAsStateWithLifecycle()
    val listActeursInstitutionnelsEtDeSoutien = monReseauViewModel.listActeursInstitutionel.collectAsStateWithLifecycle()
    val listPassedElement = ligneDeVieViewModel.allPassedElement.collectAsStateWithLifecycle()
    val listPresentElement = ligneDeVieViewModel.allPresentElement.collectAsStateWithLifecycle()
    val listQuestionElement: List<Element> = emptyList()
    val listBilanCompetence = bilanCompetenceViewModel.skillsState.collectAsStateWithLifecycle()
    val listLienVieReelle: List<String> = emptyList()

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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "asdkjflsajdflsajdl alsdkfjlsakdjflaskdjflaksjlfdkjsadlfa asldkfjlaksdjflkajdsflasdkjflaksjdflasdlaskdjflkldas",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(MediumPadding1),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    Text(
                        text = "Découvrir mon réseau",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    val listActeursFamiliaux = remember { mutableStateOf(mapOf(
                        "Parents ou tuteurs: " to (listActeursFamiliauxEtSociaux.value?.parentsTuteurs?.split("|") ?: emptyList()),
                        "Frères, soeurs, cousins ou cousines: " to (listActeursFamiliauxEtSociaux.value?.freresSoeursCousinsCousines?.split("|") ?: emptyList()),
                        "Voisins" to (listActeursFamiliauxEtSociaux.value?.voisins?.split("|") ?: emptyList()),
                        "Chefs coutumiers ou religieux: " to (listActeursFamiliauxEtSociaux.value?.chefsCoutumiersReligieux?.split("|") ?: emptyList()),
                        "Grands-parents: " to (listActeursFamiliauxEtSociaux.value?.grandsParents?.split("|") ?: emptyList()),
                        "Amis proches: " to (listActeursFamiliauxEtSociaux.value?.amisProches?.split("|") ?: emptyList()),
                        "Mentor ou modèle dans la communauté: " to (listActeursFamiliauxEtSociaux.value?.mentorModeleCommunaute?.split("|") ?: emptyList()),
                        "Leaders communautaires ou d'associations locales: " to (listActeursFamiliauxEtSociaux.value?.leadersCommunautairesAssociationsLocales?.split("|") ?: emptyList()),
                    )) }
                    MonReseauElement("Acteurs familiaux et sociaux", listActeursFamiliaux.value)
                }
                item {
                    val listActeursEducatifs = mapOf(
                        "Enseignants ou professeurs: " to (listActeursEducatifs.value?.EnseignantsProfesseurs?.split("|") ?: emptyList()),
                        "Encadreurs de centres de formation professionnelle: " to (listActeursEducatifs.value?.EncadreursCentresFormationProfessionnelle?.split("|") ?: emptyList()),
                        "Anciens camarades de classe: " to (listActeursEducatifs.value?.anciensCamaradesClasse?.split("|") ?: emptyList()),
                        "Conseillers d'orientation scolaire ou professionnelle: " to (listActeursEducatifs.value?.ConseillersOrientationScolaireProfessionnelle?.split("|") ?: emptyList()),
                        "Animateurs d'ONG éducatives: " to (listActeursEducatifs.value?.animateursONGEducatives?.split("|") ?: emptyList()),
                    )
                    MonReseauElement("Acteurs educatifs", listActeursEducatifs)
                }
                item {
                    val listActeursProfessionnels = mapOf(
                        "Anciens employeurs ou maîtres d'apprentissage: " to (listActeursProfessionnels.value?.anciensEmployeursMaitresApprentissage?.split("|") ?: emptyList()),
                        "Employés d'ONG ou de projets de développement: " to (listActeursProfessionnels.value?.employesONGProjetsDeveloppement?.split("|") ?: emptyList()),
                        "Artisans ou entrepreneurs locaux: " to (listActeursProfessionnels.value?.artisansEntrepreneursLocaux?.split("|") ?: emptyList()),
                        "Personnes ressources dans les coopératives, groupements ou mutuelles: " to (listActeursProfessionnels.value?.personnesRessourcesCooperativesGroupementsMutuelles?.split("|") ?: emptyList()),
                    )
                    MonReseauElement("Acteurs professionnels", listActeursProfessionnels)
                }
                item {
                    val listActeursInstitutionnelsEtDeSoutien = mapOf(
                        "Agents des services sociaux ou administratifs: " to (listActeursInstitutionnelsEtDeSoutien.value?.agentsServicesSociauxAdministratifs?.split("|") ?: emptyList()),
                        "Representants de structures comme une agence nationale pour l'emploi: " to (listActeursInstitutionnelsEtDeSoutien.value?.representantsStructuresCommeAgenceNationaleEmploi?.split("|") ?: emptyList()),
                        "Formateurs des programmes publics ou privés de formation et insertion: " to (listActeursInstitutionnelsEtDeSoutien.value?.formateursProgrammesPublicsPrivesFormationInsertion?.split("|") ?: emptyList()),
                        "Personnel de santé: " to (listActeursInstitutionnelsEtDeSoutien.value?.personnelSante?.split("|") ?: emptyList()),
                    )
                    MonReseauElement("Acteurs institutionnels et de soutien", listActeursInstitutionnelsEtDeSoutien)
                }

                item {
                    Text(
                        text = "Ligne de vie",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    LigneDeVieComponent("Les évènements du passées", listPassedElement.value)
                }
                item {
                    LigneDeVieComponent("Les évènements du présent", listPresentElement.value)
                }
                item {
                    LigneDeVieQuestion()
                }

                item {
                    Text(
                        text = "Bilan de compétences",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    BilanCompetenceElement(listItems = listBilanCompetence.value!!)
                }
                item {
                    Text(
                        text = "Lien avec la vie réelle",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }



//                item {
//                    Text("Ligne de vie")
//                    Spacer(Modifier.height(8.dp))
//                    Text("Liste des evenements passees")
//                }
//                    if (listPassedElement.isNotEmpty()) {
//                        items(
//                            listPassedElement,
//                        ) { passedElement ->
//                            LigneDeVieComponent(element = passedElement)
//                        }
//                    } else {
//                        item {
//                            Text("Pas d'élément passé")
//                        }
//                    }

//                    item {
//                        Text("Liste des evenements present")
//                        Spacer(Modifier.height(8.dp))
//                    }
//                    if (listPresentElement.isNotEmpty()) {
//                        items(
//                            listPresentElement,
//                        ) { presentElement ->
//                            LigneDeVieComponent(element = presentElement)
//                        }
//                    }

//                    item {
//                        Text("Liste des evenements present")
//                        Spacer(Modifier.height(8.dp))
//                    }

                    item {
                        AppButton (text = "Generer pdf", onClick = {})
                    }
            }

        }
    }

}

@Composable
fun MonReseauElement(nomCategorie: String, listElement: Map<String, List<String>>) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = nomCategorie,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
        if (listElement.isEmpty()) {
            Text(
                text = "Pas d'élément",
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            listElement.forEach { element ->
                MonReseauSousCategorieElement(element.key, element.value)
            }
        }
    }
}

@Composable
fun MonReseauSousCategorieElement(nomSousCategorie: String, listElement: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = nomSousCategorie,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        if (listElement.isEmpty()) {
            Text(
                text = "Pas d'élément",
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            listElement.forEach { element ->
                Text(
                    text = "- $element",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
    }
}

@Composable
fun LigneDeVieComponent(nomCategorie: String, element: List<Element>) {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = nomCategorie,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        if (element.isNotEmpty()) {
            element.forEach { element ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = element.label,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (element.status) {
                        Text(
                            text = "${element.inProgressYear}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(MediumPadding1),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "${element.startYear}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = " à ",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${element.endYear}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Text(
                        text = element.labelDescription,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        } else {
            Text(
                text = "Pas d'élément",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun LigneDeVieQuestion(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MediumPadding1),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Question",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Description",
            style = MaterialTheme.typography.bodySmall,
        )
    }

}

@Composable
fun BilanCompetenceElement(modifier: Modifier = Modifier, listItems: List<String>) {
    FlowRow (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MediumPadding1),
        horizontalArrangement = Arrangement.spacedBy(MediumPadding1),
        verticalArrangement = Arrangement.spacedBy(MediumPadding1)
    ) {
        listItems.forEach { element ->
            Text(
                text = element,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(5.dp))
                    .padding(MediumPadding1)
            )
        }
    }
}

@Preview(device = "id:pixel_8", showSystemUi = true, showBackground = true)
@Composable
fun ResumePlanificationProjetScreenPreview() {
    ResumePlanificationProjetScreen()
}