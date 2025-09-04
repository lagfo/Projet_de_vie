package org.ticanalyse.projetdevie.presentation.mon_reseau

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Div
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigationViewModel
import org.ticanalyse.projetdevie.presentation.bilan_competance.generatePdf
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.UserInfoDialog
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import org.ticanalyse.projetdevie.utils.PdfUtil.addUserInfoSection
import org.ticanalyse.projetdevie.utils.PdfUtil.getImageDataFromResource
import org.ticanalyse.projetdevie.utils.SimpleFooterEventHandler
import org.ticanalyse.projetdevie.utils.WatermarkImageEventHandler
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MonReseauResumeScreen(navController: NavController,onNavigate: () -> Unit) {
    val ttsManager = appTTSManager()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    val monReseauViewModel = hiltViewModel<MonReseauViewModel>()
    monReseauViewModel.getMonReseau()

    val currentUser by monReseauViewModel.currentUser.collectAsStateWithLifecycle()
    var showUserDialog by remember { mutableStateOf(false) }

    val listActeursFamiliauxEtSociaux = monReseauViewModel.listActeursFamiliaux.collectAsStateWithLifecycle()
    Timber.tag("reseau").d("${listActeursFamiliauxEtSociaux.value}")
    val listActeursEducatifs = monReseauViewModel.listActeursEducatifs.collectAsStateWithLifecycle()
    val listActeursProfessionnels = monReseauViewModel.listActeursProfessionel.collectAsStateWithLifecycle()
    val listActeursInstitutionnelsEtDeSoutien = monReseauViewModel.listActeursInstitutionel.collectAsStateWithLifecycle()

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MediumPadding3),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.fillMaxSize().weight(.9f)) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AppText(
                        text = "Mon Réseau",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Normal,
                        color = colorResource(id = R.color.text),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        ttsManager = ttsManager,
                    )
                    Spacer(modifier = Modifier.height(MediumPadding1))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(MediumPadding1),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        item {
                            Timber.tag("reseau").d("${acteurFamiliauxSociaux}")
                            MonReseauItem(listActeurs = acteurFamiliauxSociaux)
                        }

                        item {
                            MonReseauItem(listActeurs = acteurEducatifs)
                        }

                        item {
                            MonReseauItem(listActeurs = acteurProfessionnels)
                        }

                        item {
                            MonReseauItem(listActeurs = acteurInstitutionnelsEtDeSoutien)
                        }

                    }

                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp).weight(.1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButton(text = "Retour", onClick = { navController.navigateUp() })
                Spacer(modifier = Modifier.width(14.dp))
                AppButton(text = "Ligne de vie", onClick = onNavigate)
                Spacer(modifier = Modifier.width(14.dp))
                FloatingActionButton(
                    modifier = Modifier.size(45.dp),
                    onClick = {
                        scope.launch {
                            isLoading = true
                            withContext(Dispatchers.IO) {
                                generatePdf(
                                    context=context,
                                    user = currentUser!!,
                                    listActeursFamiliaux = acteurFamiliauxSociaux,
                                    listActeursEducatifs = acteurEducatifs,
                                    listActeursProfessionnels = acteurProfessionnels,
                                    listActeursInstitutionnelsEtDeSoutien = acteurInstitutionnelsEtDeSoutien
                                )
                            }
                            isLoading = false
                        }
                    },
                    containerColor = colorResource(id = R.color.secondary_color),
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        painter  = painterResource(id=R.drawable.picture_as_pdf_24),
                        contentDescription = "Generer le PDF",
                    )
                }

            }




        }


    }


    // Dialog pour saisir les informations utilisateur
    if (showUserDialog) {
        UserInfoDialog(
            onDismiss = { showUserDialog = false },
            onConfirm = { nom, prenom, telephone ->
                scope.launch {
                    // Sauvegarder l'utilisateur
                    val newUser = User(nom = nom, prenom = prenom, numTel = telephone)
                    monReseauViewModel.setCurrentUser(newUser)

                    showUserDialog = false

                    // Générer le PDF avec les nouvelles informations
                    isLoading = true
                    withContext(Dispatchers.IO) {
                        generatePdf(
                            context=context,
                            user = currentUser!!,
                            listActeursFamiliaux = acteurFamiliauxSociaux,
                            listActeursEducatifs = acteurEducatifs,
                            listActeursProfessionnels = acteurProfessionnels,
                            listActeursInstitutionnelsEtDeSoutien = acteurInstitutionnelsEtDeSoutien
                        )
                    }
                    isLoading = false
                }
            }
        )
    }
}

@Composable
fun MonReseauItem( listActeurs: ReseauSection) {
    val ttsManager = appTTSManager()

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.align(Alignment.Start).padding(start = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color =colorResource(R.color.secondary_color),
                        shape = CircleShape
                    ),
                painter = painterResource(id=listActeurs.categoryPaint) ,
                contentScale = ContentScale.Crop,
                contentDescription = "category image"
            )
            Spacer(modifier = Modifier.width(10.dp))
            AppText(
                text = listActeurs.category,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal,
                color = colorResource(id = R.color.text),
                fontSize = 15.sp,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                ttsManager = ttsManager,
            )

        }
        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (listActeurs.reseauSubSection.isNotEmpty()) {
                val filteredSubSections = listActeurs.reseauSubSection.filter {
                    it.subCategory.isNotBlank() && it.listActeur.isNotEmpty()
                }

                if (filteredSubSections.isNotEmpty()) {
                    filteredSubSections.forEach { subSection ->

                        Row(
                            modifier = Modifier.align(Alignment.Start).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,

                        ) {
                            Image(
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RectangleShape)
                                    .border(
                                        width = 1.dp,
                                        color =colorResource(R.color.secondary_color),
                                        shape = RectangleShape
                                    ),
                                painter = painterResource(id=subSection.subCategoryPaint) ,
                                contentScale = ContentScale.Crop,
                                contentDescription = "subCategory image"
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            AppText(
                                text = subSection.subCategory,
                                fontFamily = Roboto,
                                fontWeight = FontWeight.ExtraBold,
                                fontStyle = FontStyle.Normal,
                                color = colorResource(id = R.color.text),
                                fontSize = 13.sp,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                                ttsManager = ttsManager,
                            )

                        }

                        Column(modifier = Modifier.fillMaxWidth().align(Alignment.Start).padding(start = 15.dp, top = 10.dp)) {
                            if(subSection.listActeur.isNotEmpty()){
                                for (i in subSection.listActeur.indices step 2) {
                                    val acteurNom = subSection.listActeur[i]
                                    val commentaire = if (i + 1 < subSection.listActeur.size) subSection.listActeur[i + 1] else ""
                                    AppText(
                                        text = "Nom, Prenom(s):",
                                        fontFamily = Roboto,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontStyle = FontStyle.Normal,
                                        color = colorResource(id = R.color.text),
                                        fontSize = 12.sp,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        ttsManager = ttsManager,
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    AppText(
                                        text = acteurNom,
                                        fontFamily = Roboto,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontStyle = FontStyle.Normal,
                                        color = colorResource(id = R.color.text),
                                        fontSize = 12.sp,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        ttsManager = ttsManager,
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    AppText(
                                        text = "Commentaire:",
                                        fontFamily = Roboto,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontStyle = FontStyle.Normal,
                                        color = colorResource(id = R.color.text),
                                        fontSize = 12.sp,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        ttsManager = ttsManager,
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    AppText(
                                        text = commentaire,
                                        fontFamily = Roboto,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontStyle = FontStyle.Normal,
                                        color = colorResource(id = R.color.text),
                                        fontSize = 12.sp,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        ttsManager = ttsManager,
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                }

                            }
                        }


                    }

                    Spacer(modifier = Modifier.height(5.dp))

                }else{
                    AppText(
                        text = "Aucune information enregistrée",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Normal,
                        color = colorResource(id = R.color.text),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                        ttsManager = ttsManager,
                    )
                }
            }else{
                AppText(
                    text = "Aucune information enregistrée",
                    fontFamily = Roboto,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Normal,
                    color = colorResource(id = R.color.text),
                    fontSize = 12.sp,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                    ttsManager = ttsManager,
                )

            }

        }

    }
    Spacer(modifier = Modifier.height(15.dp))
}

data class ReseauSection(
    val category:String,
    @DrawableRes val categoryPaint: Int,
    var reseauSubSection: List<ReseauSubSection>

)

data class ReseauSubSection(
    val subCategory:String,
    @DrawableRes val subCategoryPaint: Int,
    val listActeur: List<String>
)


fun generatePdf(
    context: Context,
    user: User,
    listActeursFamiliaux: ReseauSection,
    listActeursEducatifs: ReseauSection,
    listActeursProfessionnels: ReseauSection,
    listActeursInstitutionnelsEtDeSoutien: ReseauSection
){

    val currentDateTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(Date())
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(directory, "Projet_de_vie_mon_reseau_${user.nom.replace(" ","_")}${user.prenom.replace(" ","_")}_$currentDateTime.pdf")

    val pdfWriter = PdfWriter(file)
    val pdfDoc = PdfDocument(pdfWriter)
    pdfDoc.defaultPageSize = PageSize.A4
    val document = Document(pdfDoc, PageSize.A4)
    document.setMargins(50f, 40f, 50f, 40f)

    val watermarkImageData = getImageDataFromResource(context, R.drawable.logo)
    pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, WatermarkImageEventHandler(watermarkImageData))
    pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, SimpleFooterEventHandler())


    addUserInfoSection(document, user, context)

    document.add(Paragraph("\n"))
    val pageWidth = pdfDoc.defaultPageSize.width
    addReseauSection(document, context, listActeursFamiliaux, pageWidth)
    addReseauSection(document, context, listActeursEducatifs, pageWidth)
    addReseauSection(document, context, listActeursProfessionnels, pageWidth)
    addReseauSection(document, context, listActeursInstitutionnelsEtDeSoutien, pageWidth)

    document.close()

    val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)

    if (file.exists()) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("application/pdf")
        intent.clipData = ClipData.newRawUri(file.name, uri)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.setDataAndType(uri, "application/pdf")
        context.startActivity(Intent.createChooser(intent, "Veuillez choisir une application"))
    }

}

fun drawableToImage(context: Context, drawableResId: Int): Image {
    val imageData = getImageDataFromResource(context, drawableResId)
    val image = Image(imageData)
        .setWidth(50f)
        .setHeight(50f)
        .setHorizontalAlignment(HorizontalAlignment.CENTER)
    return image
}

fun addReseauSection(
    document: Document,
    context: Context,
    data: ReseauSection,
    pageWidth: Float
) {
    val container = Div().setWidth(UnitValue.createPercentValue(100f))

    val tableWidth = pageWidth - 80f

    val categoryTable = Table(UnitValue.createPercentArray(floatArrayOf(20f, 80f))).useAllAvailableWidth()

    val categoryImage = drawableToImage(context, data.categoryPaint)
        .setWidth(50f)
        .setHeight(50f)
        .setHorizontalAlignment(HorizontalAlignment.CENTER)

    val categoryCellImage = Cell()
        .setBorder(Border.NO_BORDER)
        .setVerticalAlignment(VerticalAlignment.MIDDLE)
    val categoryDivImage = Div().add(categoryImage)
    categoryCellImage.add(categoryDivImage)

    val categoryCellText = Cell()
        .setBorder(Border.NO_BORDER)
        .setVerticalAlignment(VerticalAlignment.MIDDLE)
    val categoryDivText = Div().add(
        Paragraph(data.category)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))
    )
    categoryCellText.add(categoryDivText)

    categoryTable.addCell(categoryCellImage)
    categoryTable.addCell(categoryCellText)

    container.add(categoryTable)
    container.add(Paragraph("\n"))

    if (data.reseauSubSection.isNotEmpty()) {
        val filteredSubSections = data.reseauSubSection.filter {
            it.subCategory.isNotBlank() && it.listActeur.isNotEmpty()
        }

        if (filteredSubSections.isNotEmpty()) {
            filteredSubSections.forEach { subSection ->
                val subCategoryTable = Table(UnitValue.createPercentArray(floatArrayOf(10f, 90f)))
                    .useAllAvailableWidth()
                    .setMarginBottom(10f)

                val imageCell = Cell()
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.RIGHT)

                val subCategoryImage = drawableToImage(context, subSection.subCategoryPaint)
                    .setWidth(40f)
                    .setHeight(40f)

                imageCell.add(subCategoryImage)

                val textCell = Cell()
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT)

                val subCategoryText = Paragraph(subSection.subCategory)
                    .setFontSize(15f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))

                textCell.add(subCategoryText)
                subCategoryTable.addCell(imageCell)
                subCategoryTable.addCell(textCell)
                container.add(subCategoryTable)

                val acteurs = subSection.listActeur
                val acteursTable = Table(UnitValue.createPercentArray(floatArrayOf(50f, 50f)))
                    .setWidth(tableWidth)
                    .setFixedLayout()

                if(acteurs.isNotEmpty()){
                    val acteurCellTitle = Cell()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(10f)

                    val acteurCellDivTitle = Div()
                    acteurCellDivTitle.add(
                        Paragraph("Nom, Prenom(s)")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12f)
                            .setMarginTop(5f)
                            .setMarginBottom(0f)
                            .setMultipliedLeading(1.2f)
                    )
                    acteurCellTitle.add(acteurCellDivTitle)
                    acteursTable.addCell(acteurCellTitle)

                    val commCellTitle = Cell()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(10f)

                    val comCellDivTitle = Div()
                    comCellDivTitle.add(
                        Paragraph("Commentaire")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12f)
                            .setMarginTop(5f)
                            .setMarginBottom(0f)
                            .setMultipliedLeading(1.2f)
                            .setKeepTogether(false)
                    )
                    commCellTitle.add(comCellDivTitle)
                    acteursTable.addCell(commCellTitle)
                }

                for (i in acteurs.indices step 2) {
                    val acteurNom = acteurs[i]
                    val commentaire = if (i + 1 < acteurs.size) acteurs[i + 1] else ""

                    val acteurCell = Cell()
                        .setTextAlignment(TextAlignment.LEFT)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(10f)

                    val acteurCellDiv = Div()
                    acteurCellDiv.add(
                        Paragraph(acteurNom)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setFontSize(12f)
                            .setMarginTop(5f)
                            .setMarginBottom(0f)
                            .setMultipliedLeading(1.2f)
                    )
                    acteurCell.add(acteurCellDiv)
                    acteursTable.addCell(acteurCell)

                    val commCell = Cell()
                        .setTextAlignment(TextAlignment.LEFT)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(10f)

                    val comCellDiv = Div()
                    comCellDiv.add(
                        Paragraph(commentaire)
                            .setTextAlignment(TextAlignment.LEFT)
                            .setFontSize(12f)
                            .setMarginTop(5f)
                            .setMarginBottom(0f)
                            .setMultipliedLeading(1.2f)
                            .setKeepTogether(false)
                    )
                    commCell.add(comCellDiv)
                    acteursTable.addCell(commCell)
                }

                container.add(acteursTable)
                container.add(Paragraph("\n"))
            }
        } else {
            container.add(
                Paragraph("Aucune information enregistrée")
                    .setFontSize(13f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC))
                    .setTextAlignment(TextAlignment.CENTER)
            )
        }
    } else {
        container.add(
            Paragraph("Aucune information enregistrée")
                .setFontSize(13f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC))
                .setTextAlignment(TextAlignment.CENTER)
        )
    }

    document.add(container)
}