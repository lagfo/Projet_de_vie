package org.ticanalyse.projetdevie.presentation.bilan_competance

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppSkillCardIcon
import org.ticanalyse.projetdevie.presentation.common.AppSkillGrid
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.AppTextButton
import org.ticanalyse.projetdevie.presentation.common.Txt
import org.ticanalyse.projetdevie.presentation.common.UserInfoDialog
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.common.skills
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
fun BilanCompetenceResumeScreen(navController:NavController,onNavigate: () -> Unit) {
    val ttsManager = appTTSManager()
    val context = LocalContext.current
    val viewModel = hiltViewModel<BilanCompetenceViewModel>()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    var showUserDialog by remember { mutableStateOf(false) }

    var selectedSkills by remember { mutableStateOf<List<String>>(emptyList()) }

    val defaultSkills = remember { mutableStateListOf<AppSkillCardIcon>().apply { addAll(skills) } }

    val  finalSkills = remember { mutableStateListOf<AppSkillCardIcon>()}

    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.getSkill { storedSkills ->
            storedSkills?.let { stored ->

                val storedLower = stored.map(String::lowercase).toSet()

                fun getSkillName(skill: AppSkillCardIcon): String = when (val txt = skill.txt) {
                    is Txt.Res -> context.getString(txt.id)
                    is Txt.Raw -> txt.text
                }

                defaultSkills.retainAll { skill ->
                    storedLower.contains(getSkillName(skill).lowercase())
                }

                defaultSkills.replaceAll { skill ->
                    finalSkills.add(
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
                    finalSkills.add(
                        AppSkillCardIcon(
                        txt = Txt.Raw(skillName),
                        strokeColor = R.color.primary_color,
                        paint = R.drawable.default_competence,
                        badgeStatus = true
                        )
                    )
                }

                selectedSkills = stored
            }
        }
    }
    Timber.tag("tag").d("${finalSkills.size}")


    Box(modifier = Modifier.fillMaxSize()) {
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
                .padding(top = MediumPadding3, bottom = MediumPadding1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppText(
                        text = stringResource(id = R.string.bilan_title),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Normal,
                        color = colorResource(id = R.color.text),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        ttsManager = ttsManager
                    )
                    Spacer(modifier = Modifier.height(MediumPadding1))

                    AppSkillGrid(
                        icons = defaultSkills,
                        column = 2,
                        selectedIcons = selectedSkills,
                        onSkillClick = {}
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButton(text = "Retour", onClick = { navController.navigateUp() })
                Spacer(modifier = Modifier.width(14.dp))
                AppButton(text = "Lien avec la vie réelle", onClick = onNavigate)
                Spacer(modifier = Modifier.width(14.dp))
                FloatingActionButton(
                    modifier = Modifier.size(45.dp),
                    onClick = {
                        // Vérifier si l'utilisateur existe avant de générer le PDF
                        if (currentUser != null &&
                            currentUser!!.nom.isNotBlank() &&
                            currentUser!!.prenom.isNotBlank()) {
                            // Générer le PDF directement
                            scope.launch {
                                isLoading = true
                                withContext(Dispatchers.IO) {
                                    generatePdf(
                                        context = context,
                                        user = currentUser!!,
                                        competences = finalSkills,
                                    )
                                }
                                isLoading = false
                            }
                        } else {
                            // Afficher le dialog pour saisir les informations utilisateur
                            showUserDialog = true
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
                    viewModel.setCurrentUser(newUser)

                    showUserDialog = false

                    // Générer le PDF avec les nouvelles informations
                    isLoading = true
                    withContext(Dispatchers.IO) {
                        generatePdf(
                            context = context,
                            user = newUser,
                            competences = finalSkills,
                        )
                    }
                    isLoading = false
                }
            }
        )
    }

}


fun generatePdf(
    context: Context,
    user: User,
    competences: List<AppSkillCardIcon>
){

    val currentDateTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(Date())
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(directory, "Projet_de_vie_bilan_de_competence_${user.nom.replace(" ","_")}${user.prenom.replace(" ","_")}_$currentDateTime.pdf")

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

    // --- Bilan de competence
    document.add(Paragraph("Bilan de compétences")
        .setTextAlignment(TextAlignment.CENTER)
        .setFontSize(17.5f)
        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

    if (competences.isNotEmpty()) {
        val table = Table(UnitValue.createPercentArray(3)).useAllAvailableWidth()

        competences.forEach { competence ->
            val cell = Cell()
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setPadding(10f)
            val cellDiv = Div()

            competence.paint.let { iconRes ->
                val imageData = getImageDataFromResource(context, iconRes)
                val image = Image(imageData)
                    .setWidth(100f)
                    .setHeight(100f)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                cellDiv.add(image)
            }

            val txt = when (val txt = competence.txt) {
                is Txt.Res -> context.getString(txt.id)
                is Txt.Raw -> txt.text
            }
            cellDiv.add(Paragraph(txt)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(12f)
                .setMarginTop(5f)
                .setMarginBottom(0f))

            cell.add(cellDiv)
            table.addCell(cell)
        }

        document.add(table)
    } else {
        document.add(Paragraph("Pas de compétance sélectionner"))
    }

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
