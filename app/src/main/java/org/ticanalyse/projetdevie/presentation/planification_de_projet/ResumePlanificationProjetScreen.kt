package org.ticanalyse.projetdevie.presentation.planification_de_projet

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigationViewModel
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetenceViewModel
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.LienVieReelViewModel
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LigneDeVieViewModel
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauViewModel
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import org.ticanalyse.projetdevie.utils.PdfUtil.createResumePlanificationPdf
import timber.log.Timber
import kotlin.text.ifEmpty

@Composable
fun ResumePlanificationProjetScreen(
    onNavigate: () -> Unit = {},
) {

    val monReseauViewModel = hiltViewModel<MonReseauViewModel>()
    val ligneDeVieViewModel = hiltViewModel<LigneDeVieViewModel>()
    val bilanCompetenceViewModel = hiltViewModel<BilanCompetenceViewModel>()
    val viewModel = hiltViewModel<AppNavigationViewModel>()
    val lienVieReelle = hiltViewModel<LienVieReelViewModel>()

    monReseauViewModel.getMonReseau()
    bilanCompetenceViewModel.getSkills()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val painter = rememberAsyncImagePainter (currentUser?.avatarUri?.ifEmpty{R.drawable.avatar})

    val listActeursFamiliauxEtSociaux = monReseauViewModel.listActeursFamiliaux.collectAsStateWithLifecycle()
    val listActeursEducatifs = monReseauViewModel.listActeursEducatifs.collectAsStateWithLifecycle()
    val listActeursProfessionnels = monReseauViewModel.listActeursProfessionel.collectAsStateWithLifecycle()
    val listActeursInstitutionnelsEtDeSoutien = monReseauViewModel.listActeursInstitutionel.collectAsStateWithLifecycle()

    val listPassedElement = ligneDeVieViewModel.allPassedElement.collectAsStateWithLifecycle()
    val listPresentElement = ligneDeVieViewModel.allPresentElement.collectAsStateWithLifecycle()
    val listQuestionElement = ligneDeVieViewModel.allResponse.collectAsStateWithLifecycle()
    val listBilanCompetence = bilanCompetenceViewModel.skillsState.collectAsStateWithLifecycle()
    val listLienVieReelle = lienVieReelle.allElement.collectAsStateWithLifecycle()
    val context = LocalContext.current


    val mapListActeursFamiliaux = remember { mutableStateOf(mapOf<String, List<String>>()) }
    val mapListActeursEducatifs = remember { mutableStateOf(mapOf<String, List<String>>()) }
    val mapListActeursProfessionnels = remember { mutableStateOf(mapOf<String, List<String>>()) }
    val mapListActeursInstitutionnelsEtDeSoutien = remember { mutableStateOf(mapOf<String, List<String>>()) }


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
                text = "Fiche",
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
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MediumPadding1),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(
                                    width = 1.dp,
                                    color =colorResource(R.color.secondary_color),
                                    shape = CircleShape
                                ),
                            painter = painter,
                            contentScale = ContentScale.Crop,
                            contentDescription = "Profil image"
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Nom et prenoms: ${currentUser?.nom} ${currentUser?.prenom}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Sexe: ${currentUser?.genre}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Date de naissance: ${currentUser?.dateNaissance}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Numero de telephone: ${currentUser?.numTel}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            currentUser?.email?.let {
                                Text(
                                    text = "Email: ${currentUser?.email?.ifEmpty { "Non renseigné" }}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Découvrir mon réseau",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
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
                    MonReseauElement("Acteurs familiaux et sociaux", mapListActeursFamiliaux.value)
                }
                item {
                    mapListActeursEducatifs.value = mapOf(
                        "Enseignants ou professeurs: " to (listActeursEducatifs.value?.EnseignantsProfesseurs?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Encadreurs de centres de formation professionnelle: " to (listActeursEducatifs.value?.EncadreursCentresFormationProfessionnelle?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Anciens camarades de classe: " to (listActeursEducatifs.value?.anciensCamaradesClasse?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Conseillers d'orientation scolaire ou professionnelle: " to (listActeursEducatifs.value?.ConseillersOrientationScolaireProfessionnelle?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Animateurs d'ONG éducatives: " to (listActeursEducatifs.value?.animateursONGEducatives?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                    )
                    MonReseauElement("Acteurs educatifs", mapListActeursEducatifs.value)
                }
                item {
                    mapListActeursProfessionnels.value = mapOf(
                        "Anciens employeurs ou maîtres d'apprentissage: " to (listActeursProfessionnels.value?.anciensEmployeursMaitresApprentissage?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Employés d'ONG ou de projets de développement: " to (listActeursProfessionnels.value?.employesONGProjetsDeveloppement?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Artisans ou entrepreneurs locaux: " to (listActeursProfessionnels.value?.artisansEntrepreneursLocaux?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Personnes ressources dans les coopératives, groupements ou mutuelles: " to (listActeursProfessionnels.value?.personnesRessourcesCooperativesGroupementsMutuelles?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                    )
                    MonReseauElement("Acteurs professionnels", mapListActeursProfessionnels.value)
                }
                item {
                    mapListActeursInstitutionnelsEtDeSoutien.value = mapOf(
                        "Agents des services sociaux ou administratifs: " to (listActeursInstitutionnelsEtDeSoutien.value?.agentsServicesSociauxAdministratifs?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Representants de structures comme une agence nationale pour l'emploi: " to (listActeursInstitutionnelsEtDeSoutien.value?.representantsStructuresCommeAgenceNationaleEmploi?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Formateurs des programmes publics ou privés de formation et insertion: " to (listActeursInstitutionnelsEtDeSoutien.value?.formateursProgrammesPublicsPrivesFormationInsertion?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                        "Personnel de santé: " to (listActeursInstitutionnelsEtDeSoutien.value?.personnelSante?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
                    )
                    MonReseauElement("Acteurs institutionnels et de soutien", mapListActeursInstitutionnelsEtDeSoutien.value)
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
                    LigneDeVieQuestion(
                        question = "Qu'ai-je déjà réalisé ?",
                        response = if (listQuestionElement.value.isNotEmpty()) {
                            listQuestionElement.value.first().firstResponse
                        } else {
                            "Aucune reponse renseignée"
                        }
                    )
                }
                item {
                    LigneDeVieQuestion(
                        question = "Qu'est ce que je suis capable de faire ?",
                        response = if (listQuestionElement.value.isNotEmpty()) {
                            listQuestionElement.value.first().secondResponse
                        } else {
                            "Aucune reponse renseignée"
                        }
                    )
                }

                item {
                    Text(
                        text = "Bilan de compétences",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    BilanCompetenceElement(listItems = listBilanCompetence.value)
                }
                item {
                    Text(
                        text = "Lien avec la vie réelle",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    LienVieReelComponent(
                        question = "Qu'est ce que j'ai actuellement ?",
                        response = if (listLienVieReelle.value.isNotEmpty()) {
                            listLienVieReelle.value.first().firstResponse
                        } else {
                            "Aucune reponse renseignée"
                        }
                    )
                }
                item {
                    LienVieReelComponent(
                        question = "Qu'est ce qui me manque ?",
                        response = if (listLienVieReelle.value.isNotEmpty()) {
                            listLienVieReelle.value.first().secondResponse
                        } else {
                            "Aucune reponse renseignée"
                        }
                    )
                }
                item {
                    LienVieReelComponent(
                        question = "Où puis-je trouver de l'aide ? Quelles personnes peuvent t'aider ?",
                        response = if (listLienVieReelle.value.isNotEmpty()) {
                            listLienVieReelle.value.first().thirdResponse
                        } else {
                            "Aucune reponse renseignée"
                        }
                    )
                }

                item {
                    AppButton (text = "Generer pdf", onClick = {
                        createResumePlanificationPdf(
                            context = context,
                            user = currentUser!!,
                            listActeursFamiliaux = mapListActeursFamiliaux.value,
                            listActeursEducatifs = mapListActeursEducatifs.value,
                            listActeursProfessionnels = mapListActeursProfessionnels.value,
                            listActeursInstitutionnels = mapListActeursInstitutionnelsEtDeSoutien.value,
                            listPassedElement = listPassedElement.value,
                            listPresentElement = listPresentElement.value,
                            listBilanCompetence = listBilanCompetence.value,
                            listQuestionsLigneDeVie = listOf(
                                Pair("Qu'ai-je déjà réalisé ?", if (listQuestionElement.value.isNotEmpty()) {
                                    listQuestionElement.value.first().firstResponse
                                } else {
                                    "Aucune reponse renseignée"
                                }
                                ),
                                Pair("Qu'est ce que je suis capable de faire ?", if (listQuestionElement.value.isNotEmpty()) {
                                    listQuestionElement.value.first().secondResponse
                                } else {
                                    "Aucune reponse renseignée"
                                })
                            ),
                            listQuestionsLienVieReel = listOf(
                                Pair("Qu'est ce que j'ai actuellement ?", if (listLienVieReelle.value.isNotEmpty()) {
                                    listLienVieReelle.value.first().firstResponse
                                } else {
                                    "Aucune reponse renseignée"
                                }
                                ),
                                Pair("Qu'est ce qui me manque ?", if (listLienVieReelle.value.isNotEmpty()) {
                                    listLienVieReelle.value.first().secondResponse
                                } else {
                                    "Aucune reponse renseignée"
                                }),
                                Pair("Où puis-je trouver de l'aide ? Quelles personnes peuvent t'aider ?", if (listLienVieReelle.value.isNotEmpty()) {
                                    listLienVieReelle.value.first().thirdResponse
                                } else {
                                    "Aucune reponse renseignée"
                                })
                            )
                            ,
                        ) {
                            onNavigate()
                        }
                    })
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
        if (listElement.any { list -> list.value.isNotEmpty() }) {
            listElement.filter { list2 -> list2.value.isNotEmpty() }.forEach { element ->
                Timber.d("Element: ${element.key} et valeur: ${element.value} est ce que c'est vide: ${element.value.isEmpty()} et les valeurs: nombre de valeur: ${element.value.size} et premier element: ${element.value.first()}")
                MonReseauSousCategorieElement(element.key, element.value)
            }
        } else {
            Text(
                text = "Aucune information enregistrée pour cette catégorie",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun MonReseauSousCategorieElement(nomSousCategorie: String, listElement: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = nomSousCategorie,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        if (listElement.isNotEmpty() ) {
            listElement.chunked(2).forEach { element ->
                Column(
                    modifier = Modifier.fillMaxWidth().padding(start = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Nom et prénoms: ${element.first()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Description: ${element.last()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
//        else {
//            Text(
//                text = "Pas d'élément",
//                style = MaterialTheme.typography.bodySmall,
//            )
//        }
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
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun LigneDeVieQuestion(question: String = "Questions", response: String = "Description") {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = response,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 6.dp)
        )
    }

}

@Composable
fun LienVieReelComponent(question: String = "Questions", response: String = "Description") {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "    $response",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 6.dp)
        )
    }

}

@Composable
fun BilanCompetenceElement(modifier: Modifier = Modifier, listItems: List<String>?) {
    FlowRow (
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (listItems.isNullOrEmpty()) {
            Text(
                text = "Pas d'élément",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        } else {
            listItems.forEach { element ->
                Text(
                    text = element,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .padding(6.dp)
                )
            }
        }
    }
}


@Preview(device = "id:pixel_8", showSystemUi = true, showBackground = true)
@Composable
fun ResumePlanificationProjetScreenPreview() {
    ResumePlanificationProjetScreen()
}