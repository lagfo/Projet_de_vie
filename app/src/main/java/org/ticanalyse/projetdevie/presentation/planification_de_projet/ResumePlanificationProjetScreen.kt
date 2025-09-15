package org.ticanalyse.projetdevie.presentation.planification_de_projet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.PlanAction
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigationViewModel
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetenceViewModel
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppSkillCardIcon
import org.ticanalyse.projetdevie.presentation.common.AppSkillIconCard
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.Txt
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.common.skills
import org.ticanalyse.projetdevie.presentation.lien_vie_relle.LienVieReelViewModel
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LigneDeVieViewModel
import org.ticanalyse.projetdevie.presentation.mon_reseau.MonReseauViewModel
import org.ticanalyse.projetdevie.presentation.mon_reseau.ReseauSection
import org.ticanalyse.projetdevie.presentation.mon_reseau.ReseauSubSection
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import org.ticanalyse.projetdevie.utils.PdfUtil.createAllProjectPdf
import org.ticanalyse.projetdevie.utils.PdfUtil.createPlanificationProjetPdf
import org.ticanalyse.projetdevie.utils.PdfUtil.sharePdf
import org.ticanalyse.projetdevie.utils.TextToSpeechManager
import timber.log.Timber

@Composable
fun ResumePlanificationProjetScreen(
    onNavigate: () -> Unit = {},
) {

    val context = LocalContext.current
    val planificationViewModel = hiltViewModel<PlanificationViewModel>()
    val viewModel = hiltViewModel<AppNavigationViewModel>()

    val currentUser by planificationViewModel.currentUser.collectAsStateWithLifecycle()

    val monReseauViewModel = hiltViewModel<MonReseauViewModel>()
    val ligneDeVieViewModel= hiltViewModel<LigneDeVieViewModel>()
    val bilanCompetenceViewModel = hiltViewModel<BilanCompetenceViewModel>()
    val lienVieReelle = hiltViewModel<LienVieReelViewModel>()
    monReseauViewModel.getMonReseau()

    val listActeursFamiliauxEtSociaux = monReseauViewModel.listActeursFamiliaux.collectAsStateWithLifecycle()
    Timber.tag("reseau").d("${listActeursFamiliauxEtSociaux.value}")
    val listActeursEducatifs = monReseauViewModel.listActeursEducatifs.collectAsStateWithLifecycle()
    val listActeursProfessionnels = monReseauViewModel.listActeursProfessionel.collectAsStateWithLifecycle()
    val listActeursInstitutionnelsEtDeSoutien = monReseauViewModel.listActeursInstitutionel.collectAsStateWithLifecycle()

    val listOfPassedElement by ligneDeVieViewModel.allPassedElement.collectAsStateWithLifecycle()
    val listOfPresentElement by ligneDeVieViewModel.allPresentElement.collectAsStateWithLifecycle()
    val reponseQuestion by ligneDeVieViewModel.allResponse.collectAsStateWithLifecycle()

    val defaultSkillsBilanCompetence = remember { mutableStateListOf<AppSkillCardIcon>().apply { addAll(skills) } }

    val  finalSkills = remember { mutableStateListOf<AppSkillCardIcon>()}
    val listLienVieReelle = lienVieReelle.allElement.collectAsStateWithLifecycle()
    val setOfIds = setOf(10,11, 12, 16, 19)



// Suppression des remember - calcul direct à partir des états observés
    val acteurFamiliauxSociaux = ReseauSection(
        category = "Acteurs familiaux et sociaux",
        categoryPaint = R.drawable.acteur_familiaux_sociaux,
        reseauSubSection = listOf(
            ReseauSubSection(
                subCategory = "Parents ou tuteurs: ",
                subCategoryPaint = R.drawable.acteur_familiaux_sociaux,
                listActeur = listActeursFamiliauxEtSociaux.value?.parentsTuteurs
                    ?.split("|")
                    ?.filter { it.isNotBlank() && it.isNotEmpty() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Frères, soeurs, cousins ou cousines: ",
                subCategoryPaint = R.drawable.freres_soeurs,
                listActeur = listActeursFamiliauxEtSociaux.value?.freresSoeursCousinsCousines
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Voisins",
                subCategoryPaint = R.drawable.voisin,
                listActeur = listActeursFamiliauxEtSociaux.value?.voisins
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Chefs coutumiers ou religieux: ",
                subCategoryPaint = R.drawable.chef_religieux_coutumier,
                listActeur = listActeursFamiliauxEtSociaux.value?.chefsCoutumiersReligieux
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Grands-parents: ",
                subCategoryPaint = R.drawable.grands_parents,
                listActeur = listActeursFamiliauxEtSociaux.value?.grandsParents
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Amis proches: ",
                subCategoryPaint = R.drawable.amis_proche,
                listActeur = listActeursFamiliauxEtSociaux.value?.amisProches
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Mentor ou modèle dans la communauté: ",
                subCategoryPaint = R.drawable.mentor,
                listActeur = listActeursFamiliauxEtSociaux.value?.mentorModeleCommunaute
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Leaders communautaires ou d'associations locales: ",
                subCategoryPaint = R.drawable.leaders,
                listActeur = listActeursFamiliauxEtSociaux.value?.leadersCommunautairesAssociationsLocales
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            )
        )
    )

    val acteurEducatifs = ReseauSection(
        category = "Acteurs éducatifs",
        categoryPaint = R.drawable.acteur_educatif,
        reseauSubSection = listOf(
            ReseauSubSection(
                subCategory = "Enseignants ou professeurs: ",
                subCategoryPaint = R.drawable.acteur_educatif,
                listActeur = listActeursEducatifs.value?.EnseignantsProfesseurs
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Encadreurs de centres de formation professionnelle: ",
                subCategoryPaint = R.drawable.encadreurs_centres_formation_professionnelle,
                listActeur = listActeursEducatifs.value?.EncadreursCentresFormationProfessionnelle
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Anciens camarades de classe: ",
                subCategoryPaint = R.drawable.anciens_camarades_classe,
                listActeur = listActeursEducatifs.value?.anciensCamaradesClasse
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Conseillers d'orientation scolaire ou professionnelle: ",
                subCategoryPaint = R.drawable.conseiller_educatif,
                listActeur = listActeursEducatifs.value?.ConseillersOrientationScolaireProfessionnelle
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Animateurs d'ONG éducatives: ",
                subCategoryPaint = R.drawable.animateurs_ong_educatives,
                listActeur = listActeursEducatifs.value?.animateursONGEducatives
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            )
        )
    )

    val acteurProfessionnels = ReseauSection(
        category = "Acteurs professionnels",
        categoryPaint = R.drawable.acteur_professionnel,
        reseauSubSection = listOf(
            ReseauSubSection(
                subCategory = "Anciens employeurs ou maîtres d'apprentissage: ",
                subCategoryPaint = R.drawable.acteur_professionnel,
                listActeur = listActeursProfessionnels.value?.anciensEmployeursMaitresApprentissage
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Employé d'ONG ou de projets de développement: ",
                subCategoryPaint = R.drawable.employes_ong_projets_developpement,
                listActeur = listActeursProfessionnels.value?.employesONGProjetsDeveloppement
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Artisans ou entrepreneurs locaux: ",
                subCategoryPaint = R.drawable.artisans_entrepreneurs_locaux,
                listActeur = listActeursProfessionnels.value?.artisansEntrepreneursLocaux
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Personnes ressources dans les coopérative, groupements ou mutuelles: ",
                subCategoryPaint = R.drawable.personnes_ressources_cooperatives_groupements_mutuelles,
                listActeur = listActeursProfessionnels.value?.personnesRessourcesCooperativesGroupementsMutuelles
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            )
        )
    )

    val acteurInstitutionnelsEtDeSoutien = ReseauSection(
        category = "Acteurs institutionnels et de soutient",
        categoryPaint = R.drawable.acteur_institutionnel,
        reseauSubSection = listOf(
            ReseauSubSection(
                subCategory = "Agents des services sociaux ou administratifs: ",
                subCategoryPaint = R.drawable.acteur_institutionnel,
                listActeur = listActeursInstitutionnelsEtDeSoutien.value?.agentsServicesSociauxAdministratifs
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Représentants de structures comme une agence Nationale pour l'emploi: ",
                subCategoryPaint = R.drawable.repr_sentants_structures_agence_nationale_emploi,
                listActeur = listActeursInstitutionnelsEtDeSoutien.value?.representantsStructuresCommeAgenceNationaleEmploi
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Formateurs des programmes publics ou privés de formation et insertion: ",
                subCategoryPaint = R.drawable.formateurs_programmes_publics_prives_formation_insertion,
                listActeur = listActeursInstitutionnelsEtDeSoutien.value?.formateursProgrammesPublicsPrivesFormationInsertion
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            ),
            ReseauSubSection(
                subCategory = "Personnel de santé: ",
                subCategoryPaint = R.drawable.personnel_sante,
                listActeur = listActeursInstitutionnelsEtDeSoutien.value?.personnelSante
                    ?.split("|")
                    ?.filter { it.isNotBlank() }
                    ?.map { it.trimStart() }
                    ?: emptyList()
            )
        )
    )

    LaunchedEffect(Unit) {
        bilanCompetenceViewModel.getSkill { storedSkills ->
            storedSkills?.let { stored ->

                val storedLower = stored.map(String::lowercase).toSet()

                fun getSkillName(skill: AppSkillCardIcon): String = when (val txt = skill.txt) {
                    is Txt.Res -> context.getString(txt.id)
                    is Txt.Raw -> txt.text
                }

                defaultSkillsBilanCompetence.retainAll { skill ->
                    storedLower.contains(getSkillName(skill).lowercase())
                }

                defaultSkillsBilanCompetence.replaceAll { skill ->
                    finalSkills.add(
                        skill.copy(badgeStatus = true)
                    )
                    skill.copy(badgeStatus = true)

                }

                stored.filter { skillName ->
                    defaultSkillsBilanCompetence.none { skill ->
                        getSkillName(skill).equals(skillName, ignoreCase = true)
                    }
                }.forEach { skillName ->
                    defaultSkillsBilanCompetence.add(
                        AppSkillCardIcon(
                            txt = Txt.Raw(skillName),
                            strokeColor = R.color.primary_color,
                            paint = R.drawable.default_competence,
                            badgeStatus = true
                        )
                    )
                    finalSkills.add(
                        AppSkillCardIcon(
                            txt = Txt.Raw(skillName),
                            strokeColor = R.color.primary_color,
                            paint = R.drawable.default_competence,
                            badgeStatus = true
                        )
                    )
                }

            }
        }
    }

    val projectInfo = planificationViewModel.planificationInfo.collectAsStateWithLifecycle()
    val listPlanAction = planificationViewModel.planAction.collectAsStateWithLifecycle()

    val ttsManager = appTTSManager()

    val defaultSkills = remember { mutableStateListOf<AppSkillCardIcon>().apply { addAll(skills) } }

    val  avalaibleSkills = remember { mutableStateListOf<AppSkillCardIcon>()}
    val  unAvailableSkills = remember { mutableStateListOf<AppSkillCardIcon>()}

    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }


    Timber.d("projectInfo: ${projectInfo.value}")

    LaunchedEffect(projectInfo.value) {
        if (projectInfo.value.isEmpty()) return@LaunchedEffect
        Timber.d("competence disponible: ${projectInfo.value.first().competenceDisponible}")
        projectInfo.value.first().competenceDisponible?.let { stored ->
            Timber.d("Stored1: $stored")
            val storedLower = stored.map(String::lowercase).toSet()

            fun getSkillName(skill: AppSkillCardIcon): String = when (val txt = skill.txt) {
                is Txt.Res -> context.getString(txt.id)
                is Txt.Raw -> txt.text
            }

            defaultSkills.retainAll { skill ->
                storedLower.contains(getSkillName(skill).lowercase())
            }

            defaultSkills.replaceAll { skill ->
                avalaibleSkills.add(
                    skill.copy(badgeStatus = false)
                )
                skill.copy(badgeStatus = false)

            }

            stored.filter { skillName ->
                defaultSkills.none { skill ->
                    getSkillName(skill).equals(skillName, ignoreCase = true)
                }
            }.forEach { skillName ->
                defaultSkills.add(
                    AppSkillCardIcon(
                        txt = Txt.Raw(skillName),
                        strokeColor = R.color.primary_color,
                        paint = R.drawable.default_competence,
                        badgeStatus = false
                    )
                )
                avalaibleSkills.add(
                    AppSkillCardIcon(
                        txt = Txt.Raw(skillName),
                        strokeColor = R.color.primary_color,
                        paint = R.drawable.default_competence,
                        badgeStatus = false
                    )
                )
            }
        }
        Timber.d("avalaibleSkills: $avalaibleSkills")
    }
    LaunchedEffect(projectInfo.value) {
        Timber.d("competence non disponible: ${projectInfo.value.firstOrNull()?.competenceNonDisponible}")
        if (projectInfo.value.isEmpty()) return@LaunchedEffect
        projectInfo.value.first().competenceNonDisponible?.let { stored ->

            Timber.d("Stored: $stored")
            val storedLower = stored.map(String::lowercase).toSet()

            fun getSkillName(skill: AppSkillCardIcon): String = when (val txt = skill.txt) {
                is Txt.Res -> context.getString(txt.id)
                is Txt.Raw -> txt.text
            }

            defaultSkills.retainAll { skill ->
                storedLower.contains(getSkillName(skill).lowercase())
            }

            defaultSkills.replaceAll { skill ->
                unAvailableSkills.add(
                    skill.copy(badgeStatus = true)
                )
                skill.copy(badgeStatus = true)

            }

            stored.filter { skillName ->
                defaultSkills.none { skill ->
                    getSkillName(skill).equals(skillName, ignoreCase = true)
                }
            }.forEach { skillName ->
                defaultSkills.add(
                    AppSkillCardIcon(
                        txt = Txt.Raw(skillName),
                        strokeColor = R.color.primary_color,
                        paint = R.drawable.default_competence,
                        badgeStatus = true
                    )
                )
                unAvailableSkills.add(
                    AppSkillCardIcon(
                        txt = Txt.Raw(skillName),
                        strokeColor = R.color.primary_color,
                        paint = R.drawable.default_competence,
                        badgeStatus = true
                    )
                )
            }
        }
        Timber.d("unavalaibleSkills: $unAvailableSkills")
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.07f
        )
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MediumPadding3, bottom = MediumPadding1, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(MediumPadding1),
                    verticalAlignment = Alignment.CenterVertically
                ) {

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
                            text = "Numero de telephone: ${currentUser?.numTel}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )

                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppText(
                        text = "Idée du projet",
                        style = MaterialTheme.typography.titleMedium,
                        isTextAlignCenter = true,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        ttsManager = ttsManager
                    )
                    if (projectInfo.value.firstOrNull() == null) {
                        AppText(
                            text = "Pas d'information",
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            ttsManager = ttsManager
                        )
                    } else {
                        AppText(
                            text = projectInfo.value.first().projetIdee,
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            ttsManager = ttsManager
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppText(
                        text = "Motivation",
                        style = MaterialTheme.typography.titleMedium,
                        isTextAlignCenter = true,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        ttsManager = ttsManager
                    )
                    if (projectInfo.value.firstOrNull() == null) {
                        AppText(
                            text = "Pas d'information",
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            ttsManager = ttsManager
                        )
                    } else {
                        AppText(
                            text = projectInfo.value.first().motivation,
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            ttsManager = ttsManager
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppText(
                        text = "Mes compétences disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        isTextAlignCenter = true,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        ttsManager = ttsManager
                    )
                    BilanCompetenceElement(listItems = avalaibleSkills, ttsManager = ttsManager)
//                    BilanCompetenceElement(default = defaultSkills, listItems = projectInfo.value.firstOrNull()?.competenceDisponible ?: emptyList(), ttsManager = ttsManager)
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppText(
                        text = "Mes compétences non disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        isTextAlignCenter = true,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        ttsManager = ttsManager
                    )
                    BilanCompetenceElement(listItems = unAvailableSkills, ttsManager = ttsManager)
//                    BilanCompetenceElement(default = defaultSkills, listItems = projectInfo.value.firstOrNull()?.competenceNonDisponible ?: emptyList(), ttsManager = ttsManager)
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppText(
                        text = "Mes ressources disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        isTextAlignCenter = true,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        ttsManager = ttsManager
                    )
                    if (projectInfo.value.firstOrNull() == null) {
                        AppText(
                            text = "Pas d'information",
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            ttsManager = ttsManager
                        )
                    } else {
                        AppText(
                            text = projectInfo.value.first().ressourceDisponible,
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            ttsManager = ttsManager
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppText(
                        text = "Mes ressources non disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        isTextAlignCenter = true,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        ttsManager = ttsManager
                    )
                    if (projectInfo.value.firstOrNull() == null) {
                        AppText(
                            text = "Pas d'information",
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            ttsManager = ttsManager
                        )
                    } else {
                        AppText(
                            text = projectInfo.value.first().ressourceNonDispnible,
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.fillMaxWidth(),
                            ttsManager = ttsManager
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppText(
                        text = "Plan d'action",
                        style = MaterialTheme.typography.titleMedium,
                        isTextAlignCenter = true,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        ttsManager = ttsManager
                    )
                    PlanActionElement(listElement = listPlanAction.value, ttsManager = ttsManager)
                }

            }

            item {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    AppButton(text = "Télécharger pdf planification de projet") {
                        scope.launch {
                            isLoading = true  // démarre le loader
                            withContext(Dispatchers.IO) {
                                createPlanificationProjetPdf(
                                    context = context,
                                    user = currentUser!!,
                                    ideeProjet = projectInfo.value.firstOrNull()?.projetIdee ?: "Pas d'information",
                                    motivation = projectInfo.value.firstOrNull()?.motivation ?: "Pas d'information",
                                    ressourceDisponible = projectInfo.value.firstOrNull()?.ressourceDisponible ?: "Pas d'information",
                                    ressourceIndisponible = projectInfo.value.firstOrNull()?.ressourceNonDispnible ?: "Pas d'information",
                                    competenceDisponible = avalaibleSkills,
                                    competenceIndisponible = unAvailableSkills,
                                    planAction = listPlanAction.value
                                ){
//                                    file.value = it
                                    sharePdf(it, context)
                                }
                            }
//                            withContext(Dispatchers.Main) {
//                                Toast.makeText(context, "Pdf téléchargé", Toast.LENGTH_SHORT).show()
//                            }
                            isLoading = false  // arrête le loader
                        }


//                    {
//                        viewModel.setResumeUri("${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}" +
//                                "/planification_projet_${currentUser!!.nom}_${currentUser!!.prenom}_${LocalDateTime.now().format(
//                                    DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))}.pdf"
//                        , "planificationProjet")
//                        onNavigate()
//                    }
                    }
                    AppButton("Telecharger PDF projet de vie complet") {
                        scope.launch {
                            isLoading = true  // démarre le loader
                            withContext(Dispatchers.IO) {
                                createAllProjectPdf(
                                    context = context,
                                    user = currentUser!!,
                                    listActeursFamiliaux = acteurFamiliauxSociaux,
                                    listActeursEducatifs = acteurEducatifs,
                                    listActeursProfessionnels = acteurProfessionnels,
                                    listActeursInstitutionnelsEtDeSoutien = acteurInstitutionnelsEtDeSoutien,
                                    listOfPassedElement = listOfPassedElement,
                                    listOfPresentElement  = listOfPresentElement,
                                    listQuestionsLigneDeVie = listOf(
                                        Pair("Qu'ai-je déjà réalisé ?", if (reponseQuestion.isNotEmpty()) {
                                            reponseQuestion[0].firstResponse.ifBlank {
                                                "Aucune reponse renseignée"
                                            }
                                        } else {
                                            "Aucune reponse renseignée"
                                        }
                                        ),
                                        Pair("Qu'est ce que je suis capable de faire ?",
                                            if (reponseQuestion.isNotEmpty()) {
                                                reponseQuestion[0].secondResponse.ifBlank {
                                                    "Aucune reponse renseignée"
                                                }
                                            } else {
                                                "Aucune reponse renseignée"
                                            })
                                    ),
                                    competences = finalSkills,
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
                                    ),
                                    ideeProjet = projectInfo.value.firstOrNull()?.projetIdee ?: "Pas d'information",
                                    motivation = projectInfo.value.firstOrNull()?.motivation ?: "Pas d'information",
                                    ressourceDisponible = projectInfo.value.firstOrNull()?.ressourceDisponible ?: "Pas d'information",
                                    ressourceIndisponible = projectInfo.value.firstOrNull()?.ressourceNonDispnible ?: "Pas d'information",
                                    competenceDisponible = avalaibleSkills,
                                    competenceIndisponible = unAvailableSkills,
                                    planAction = listPlanAction.value,
                                    setOfIds = setOfIds
                                )
                            }
                            isLoading = false
                        }
                    }
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
fun PlanActionElement(listElement: List<PlanAction>, ttsManager: TextToSpeechManager) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (listElement.isEmpty()) {
            AppText(
                text = "Aucun plan d'action enregistré",
                fontSize = TextUnit(17f, TextUnitType.Sp),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                ttsManager = ttsManager
            )
        } else {
            listElement.forEachIndexed { index, element ->
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AppText(
                        text = "Plan d'action ${index + 1}",
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        ttsManager = ttsManager
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AppText(
                            text = "Activité (Quoi):",
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            ttsManager = ttsManager
                        )
                        AppText(
                            text = element.activite,
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            ttsManager = ttsManager
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AppText(
                            text = "Qui fait:",
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            ttsManager = ttsManager
                        )
                        AppText(
                            text = element.activite,
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            ttsManager = ttsManager
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AppText(
                            text = "Qui Finance:",
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            ttsManager = ttsManager
                        )
                        AppText(
                            text = element.activite,
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            ttsManager = ttsManager
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        AppText(
                            text = "Quand:",
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            ttsManager = ttsManager
                        )
                        AppText(
                            text = element.activite,
                            fontSize = TextUnit(17f, TextUnitType.Sp),
                            style = MaterialTheme.typography.bodyMedium,
                            ttsManager = ttsManager
                        )
                    }
                }
            }
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp),
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
fun BilanCompetenceElement(modifier: Modifier = Modifier, listItems: List<AppSkillCardIcon>, ttsManager: TextToSpeechManager) {

    FlowRow (
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (listItems.isEmpty()) {
            AppText(
                text = "Pas d'élément",
                fontSize = TextUnit(17f, TextUnitType.Sp),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                ttsManager = ttsManager
            )
        } else {
            listItems.forEach { element ->
                AppSkillIconCard(
                    icon = element,
                    selected = false,
                    onClick = {}
                )
            }
        }
    }
}
//@Composable
//fun BilanCompetenceElement(modifier: Modifier = Modifier, listItems: List<String>?) {
//    FlowRow (
//        modifier = modifier
//            .fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        if (listItems.isNullOrEmpty()) {
//            Text(
//                text = "Pas d'élément",
//                style = MaterialTheme.typography.bodySmall,
//                fontWeight = FontWeight.Bold
//            )
//        } else {
//            listItems.forEach { element ->
//                Text(
//                    text = element,
//                    style = MaterialTheme.typography.bodySmall,
//                    modifier = Modifier
//                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
//                        .padding(6.dp)
//                )
//            }
//        }
//    }
//}

//    val monReseauViewModel = hiltViewModel<MonReseauViewModel>()
//    val ligneDeVieViewModel = hiltViewModel<LigneDeVieViewModel>()
//    val bilanCompetenceViewModel = hiltViewModel<BilanCompetenceViewModel>()
//    val viewModel = hiltViewModel<AppNavigationViewModel>()
//    val lienVieReelle = hiltViewModel<LienVieReelViewModel>()
//
//    monReseauViewModel.getMonReseau()
//    bilanCompetenceViewModel.getSkills()
//    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
//    val painter = rememberAsyncImagePainter (currentUser?.avatarUri?.ifEmpty{R.drawable.avatar})
//
//    val listActeursFamiliauxEtSociaux = monReseauViewModel.listActeursFamiliaux.collectAsStateWithLifecycle()
//    val listActeursEducatifs = monReseauViewModel.listActeursEducatifs.collectAsStateWithLifecycle()
//    val listActeursProfessionnels = monReseauViewModel.listActeursProfessionel.collectAsStateWithLifecycle()
//    val listActeursInstitutionnelsEtDeSoutien = monReseauViewModel.listActeursInstitutionel.collectAsStateWithLifecycle()
//
//    val listPassedElement = ligneDeVieViewModel.allPassedElement.collectAsStateWithLifecycle()
//    val listPresentElement = ligneDeVieViewModel.allPresentElement.collectAsStateWithLifecycle()
//    val listQuestionElement = ligneDeVieViewModel.allResponse.collectAsStateWithLifecycle()
//    val listBilanCompetence = bilanCompetenceViewModel.skillsState.collectAsStateWithLifecycle()
//    val listLienVieReelle = lienVieReelle.allElement.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//
//
//    val mapListActeursFamiliaux = remember { mutableStateOf(mapOf<String, List<String>>()) }
//    val mapListActeursEducatifs = remember { mutableStateOf(mapOf<String, List<String>>()) }
//    val mapListActeursProfessionnels = remember { mutableStateOf(mapOf<String, List<String>>()) }
//    val mapListActeursInstitutionnelsEtDeSoutien = remember { mutableStateOf(mapOf<String, List<String>>()) }
//
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.bg_img),
//            contentDescription = null,
//            modifier = Modifier.fillMaxSize(),
//            contentScale = ContentScale.FillBounds,
//            alpha = 0.07f
//        )
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(top = MediumPadding3, bottom = MediumPadding1),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Fiche",
//                style = MaterialTheme.typography.bodySmall,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                    .padding(horizontal = 8.dp),
//                verticalArrangement = Arrangement.spacedBy(MediumPadding1),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                item {
//                    Row (
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(MediumPadding1),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Image(
//                            modifier = Modifier
//                                .size(90.dp)
//                                .clip(CircleShape)
//                                .border(
//                                    width = 1.dp,
//                                    color =colorResource(R.color.secondary_color),
//                                    shape = CircleShape
//                                ),
//                            painter = painter,
//                            contentScale = ContentScale.Crop,
//                            contentDescription = "Profil image"
//                        )
//                        Column(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalArrangement = Arrangement.spacedBy(8.dp),
//                            horizontalAlignment = Alignment.Start
//                        ) {
//                            Text(
//                                text = "Nom et prenoms: ${currentUser?.nom} ${currentUser?.prenom}",
//                                style = MaterialTheme.typography.bodyMedium,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Text(
//                                text = "Sexe: ${currentUser?.genre}",
//                                style = MaterialTheme.typography.bodyMedium,
//                                fontWeight = FontWeight.Bold
//                            )
//
//                            Text(
//                                text = "Date de naissance: ${currentUser?.dateNaissance}",
//                                style = MaterialTheme.typography.bodyMedium,
//                                fontWeight = FontWeight.Bold
//                            )
//                            Text(
//                                text = "Numero de telephone: ${currentUser?.numTel}",
//                                style = MaterialTheme.typography.bodyMedium,
//                                fontWeight = FontWeight.Bold
//                            )
//                            currentUser?.email?.let {
//                                Text(
//                                    text = "Email: ${currentUser?.email?.ifEmpty { "Non renseigné" }}",
//                                    style = MaterialTheme.typography.bodyMedium,
//                                    fontWeight = FontWeight.Bold
//                                )
//                            }
//                        }
//                    }
//                }
//
//                item {
//                    Text(
//                        text = "Découvrir mon réseau",
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                item {
//                    mapListActeursFamiliaux.value = mapOf(
//                        "Parents ou tuteurs: " to (listActeursFamiliauxEtSociaux.value?.parentsTuteurs?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Frères, soeurs, cousins ou cousines: " to (listActeursFamiliauxEtSociaux.value?.freresSoeursCousinsCousines?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Voisins" to (listActeursFamiliauxEtSociaux.value?.voisins?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Chefs coutumiers ou religieux: " to (listActeursFamiliauxEtSociaux.value?.chefsCoutumiersReligieux?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Grands-parents: " to (listActeursFamiliauxEtSociaux.value?.grandsParents?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Amis proches: " to (listActeursFamiliauxEtSociaux.value?.amisProches?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Mentor ou modèle dans la communauté: " to (listActeursFamiliauxEtSociaux.value?.mentorModeleCommunaute?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Leaders communautaires ou d'associations locales: " to (listActeursFamiliauxEtSociaux.value?.leadersCommunautairesAssociationsLocales?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                    )
//                    MonReseauElement("Acteurs familiaux et sociaux", mapListActeursFamiliaux.value)
//                }
//                item {
//                    mapListActeursEducatifs.value = mapOf(
//                        "Enseignants ou professeurs: " to (listActeursEducatifs.value?.EnseignantsProfesseurs?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Encadreurs de centres de formation professionnelle: " to (listActeursEducatifs.value?.EncadreursCentresFormationProfessionnelle?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Anciens camarades de classe: " to (listActeursEducatifs.value?.anciensCamaradesClasse?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Conseillers d'orientation scolaire ou professionnelle: " to (listActeursEducatifs.value?.ConseillersOrientationScolaireProfessionnelle?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Animateurs d'ONG éducatives: " to (listActeursEducatifs.value?.animateursONGEducatives?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                    )
//                    MonReseauElement("Acteurs educatifs", mapListActeursEducatifs.value)
//                }
//                item {
//                    mapListActeursProfessionnels.value = mapOf(
//                        "Anciens employeurs ou maîtres d'apprentissage: " to (listActeursProfessionnels.value?.anciensEmployeursMaitresApprentissage?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Employés d'ONG ou de projets de développement: " to (listActeursProfessionnels.value?.employesONGProjetsDeveloppement?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Artisans ou entrepreneurs locaux: " to (listActeursProfessionnels.value?.artisansEntrepreneursLocaux?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Personnes ressources dans les coopératives, groupements ou mutuelles: " to (listActeursProfessionnels.value?.personnesRessourcesCooperativesGroupementsMutuelles?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                    )
//                    MonReseauElement("Acteurs professionnels", mapListActeursProfessionnels.value)
//                }
//                item {
//                    mapListActeursInstitutionnelsEtDeSoutien.value = mapOf(
//                        "Agents des services sociaux ou administratifs: " to (listActeursInstitutionnelsEtDeSoutien.value?.agentsServicesSociauxAdministratifs?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Representants de structures comme une agence nationale pour l'emploi: " to (listActeursInstitutionnelsEtDeSoutien.value?.representantsStructuresCommeAgenceNationaleEmploi?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Formateurs des programmes publics ou privés de formation et insertion: " to (listActeursInstitutionnelsEtDeSoutien.value?.formateursProgrammesPublicsPrivesFormationInsertion?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                        "Personnel de santé: " to (listActeursInstitutionnelsEtDeSoutien.value?.personnelSante?.split("|")?.filter { it.isNotBlank() && it.isNotEmpty() }?.map { it.trimStart() } ?: emptyList()),
//                    )
//                    MonReseauElement("Acteurs institutionnels et de soutien", mapListActeursInstitutionnelsEtDeSoutien.value)
//                }
//
//                item {
//                    Text(
//                        text = "Ligne de vie",
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//                item {
//                    LigneDeVieComponent("Les évènements du passées", listPassedElement.value)
//                }
//                item {
//                    LigneDeVieComponent("Les évènements du présent", listPresentElement.value)
//                }
//                item {
//                    LigneDeVieQuestion(
//                        question = "Qu'ai-je déjà réalisé ?",
//                        response = if (listQuestionElement.value.isNotEmpty()) {
//                            listQuestionElement.value.first().firstResponse
//                        } else {
//                            "Aucune reponse renseignée"
//                        }
//                    )
//                }
//                item {
//                    LigneDeVieQuestion(
//                        question = "Qu'est ce que je suis capable de faire ?",
//                        response = if (listQuestionElement.value.isNotEmpty()) {
//                            listQuestionElement.value.first().secondResponse
//                        } else {
//                            "Aucune reponse renseignée"
//                        }
//                    )
//                }
//
//                item {
//                    Text(
//                        text = "Bilan de compétences",
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//                item {
//                    BilanCompetenceElement(listItems = listBilanCompetence.value)
//                }
//                item {
//                    Text(
//                        text = "Lien avec la vie réelle",
//                        style = MaterialTheme.typography.bodyMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//                item {
//                    LienVieReelComponent(
//                        question = "Qu'est ce que j'ai actuellement ?",
//                        response = if (listLienVieReelle.value.isNotEmpty()) {
//                            listLienVieReelle.value.first().firstResponse
//                        } else {
//                            "Aucune reponse renseignée"
//                        }
//                    )
//                }
//                item {
//                    LienVieReelComponent(
//                        question = "Qu'est ce qui me manque ?",
//                        response = if (listLienVieReelle.value.isNotEmpty()) {
//                            listLienVieReelle.value.first().secondResponse
//                        } else {
//                            "Aucune reponse renseignée"
//                        }
//                    )
//                }
//                item {
//                    LienVieReelComponent(
//                        question = "Où puis-je trouver de l'aide ? Quelles personnes peuvent t'aider ?",
//                        response = if (listLienVieReelle.value.isNotEmpty()) {
//                            listLienVieReelle.value.first().thirdResponse
//                        } else {
//                            "Aucune reponse renseignée"
//                        }
//                    )
//                }
//
//                item {
//                    AppButton (text = "Generer pdf", onClick = {
//                        createResumePlanificationPdf(
//                            context = context,
//                            user = currentUser!!,
//                            listActeursFamiliaux = mapListActeursFamiliaux.value,
//                            listActeursEducatifs = mapListActeursEducatifs.value,
//                            listActeursProfessionnels = mapListActeursProfessionnels.value,
//                            listActeursInstitutionnels = mapListActeursInstitutionnelsEtDeSoutien.value,
//                            listPassedElement = listPassedElement.value,
//                            listPresentElement = listPresentElement.value,
//                            listBilanCompetence = listBilanCompetence.value,
//                            listQuestionsLigneDeVie = listOf(
//                                Pair("Qu'ai-je déjà réalisé ?", if (listQuestionElement.value.isNotEmpty()) {
//                                    listQuestionElement.value.first().firstResponse
//                                } else {
//                                    "Aucune reponse renseignée"
//                                }
//                                ),
//                                Pair("Qu'est ce que je suis capable de faire ?", if (listQuestionElement.value.isNotEmpty()) {
//                                    listQuestionElement.value.first().secondResponse
//                                } else {
//                                    "Aucune reponse renseignée"
//                                })
//                            ),
//                            listQuestionsLienVieReel = listOf(
//                                Pair("Qu'est ce que j'ai actuellement ?", if (listLienVieReelle.value.isNotEmpty()) {
//                                    listLienVieReelle.value.first().firstResponse
//                                } else {
//                                    "Aucune reponse renseignée"
//                                }
//                                ),
//                                Pair("Qu'est ce qui me manque ?", if (listLienVieReelle.value.isNotEmpty()) {
//                                    listLienVieReelle.value.first().secondResponse
//                                } else {
//                                    "Aucune reponse renseignée"
//                                }),
//                                Pair("Où puis-je trouver de l'aide ? Quelles personnes peuvent t'aider ?", if (listLienVieReelle.value.isNotEmpty()) {
//                                    listLienVieReelle.value.first().thirdResponse
//                                } else {
//                                    "Aucune reponse renseignée"
//                                })
//                            ),
//                        ) {
//                            viewModel.setResumeUri("${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}" +
//                                    "/lien_vie_reel_${currentUser!!.nom}_${currentUser!!.prenom}_${LocalDateTime.now().format(
//                                        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))}.pdf", "planificationProjet")
//                            onNavigate()
//                        }
//                    })
//                }
//
//
//            }
//
//        }
//    }


@Preview(device = "id:pixel_8", showSystemUi = true, showBackground = true)
@Composable
fun ResumePlanificationProjetScreenPreview() {
    ResumePlanificationProjetScreen()
}