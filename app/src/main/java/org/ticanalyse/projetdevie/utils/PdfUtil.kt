package org.ticanalyse.projetdevie.utils

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.createBitmap
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.HorizontalAlignment
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.User
import timber.log.Timber
import java.io.File
import androidx.core.net.toUri
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Div
import com.itextpdf.layout.element.Image
import org.ticanalyse.projetdevie.domain.model.PlanAction
import org.ticanalyse.projetdevie.presentation.common.AppSkillCardIcon
import org.ticanalyse.projetdevie.presentation.common.Txt
import org.ticanalyse.projetdevie.presentation.mon_reseau.ReseauSection
import org.ticanalyse.projetdevie.presentation.mon_reseau.drawableToImage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object PdfUtil {
    fun createResumePlanificationPdf(
        context: Context,
        user: User,
        listActeursFamiliaux: Map<String, List<String>>,
        listActeursEducatifs: Map<String, List<String>>,
        listActeursProfessionnels: Map<String, List<String>>,
        listActeursInstitutionnels: Map<String, List<String>>,
        listPassedElement: List<Element>,
        listPresentElement: List<Element>,
        listQuestionsLigneDeVie: List<Pair<String, String>>,
        listBilanCompetence: List<String>?,
        listQuestionsLienVieReel: List<Pair<String, String>>,
        outputPath: String = "${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}/resume_planification.pdf",
        onNavigate: () -> Unit
    ) {
        Timber.tag("pdf").d("$outputPath")
        val pdfWriter = PdfWriter(outputPath)
        val pdfDoc = PdfDocument(pdfWriter)
        pdfDoc.defaultPageSize = PageSize.A4
        val document = Document(pdfDoc, PageSize.A4)
        document.setMargins(50f, 40f, 50f, 40f)

        // watermark et footer handlers
        val watermarkImageData = getImageDataFromResource(context, R.drawable.logo)
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, WatermarkImageEventHandler(watermarkImageData))
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, SimpleFooterEventHandler())

        // --- Infos utilisateur
        addUserInfoSection(document, user, context)

        document.add(Paragraph("\n\n\n"))


        // --- Réseau
        addReseauSection(document, "Acteurs familiaux et sociaux", listActeursFamiliaux)
        addReseauSection(document, "Acteurs éducatifs", listActeursEducatifs)
        addReseauSection(document, "Acteurs professionnels", listActeursProfessionnels)
        addReseauSection(document, "Acteurs institutionnels et de soutien", listActeursInstitutionnels)

        document.add(Paragraph("\n\n\n"))

        // --- Ligne de vie
        document.add(Paragraph("Ligne de vie").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        addLigneDeVieSection(document, "Évènements du passé", listPassedElement)
        addLigneDeVieSection(document, "Évènements du présent", listPresentElement)
        addQuestions(document, "Questions sur la ligne de vie", listQuestionsLigneDeVie)

        document.add(Paragraph("\n\n\n"))

        // --- Bilan de competence
        document.add(Paragraph("Bilan de compétences")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        if (!listBilanCompetence.isNullOrEmpty() ) {
            val table = Table(UnitValue.createPercentArray(3)).useAllAvailableWidth()
            listBilanCompetence.forEach { competence ->
                table.addCell(competence)
            }
            document.add(table)
        } else {
            document.add(Paragraph("Pas d'éléments"))
        }

        document.add(Paragraph("\n\n\n"))
        // --- Lien de vie réel
        addQuestions(document, "Lien avec la vie réel", listQuestionsLienVieReel)

        document.close()

        onNavigate()
    }

    fun createLienVieReelPdf(
        context: Context,
        user: User,
        listQuestionsLienVieReel: List<Pair<String, String>>,
        outputPath: String = "${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}" +
                "/lien_vie_reel_${user.nom}_${user.prenom}_${LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))}.pdf",
//        onNavigate: () -> Unit
        ) {

        Timber.tag("pdf").d(outputPath)
        val pdfWriter = PdfWriter(outputPath)
        val pdfDoc = PdfDocument(pdfWriter)
        pdfDoc.defaultPageSize = PageSize.A4
        val document = Document(pdfDoc, PageSize.A4)
        document.setMargins(50f, 40f, 50f, 40f)

        // watermark et footer handlers
        val watermarkImageData = getImageDataFromResource(context, R.drawable.logo)
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, WatermarkImageEventHandler(watermarkImageData))
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, SimpleFooterEventHandler())

        // --- Infos utilisateur
        addUserInfoSection(document, user, context)

        document.add(Paragraph("\n\n\n"))

        // --- Lien de vie réel
        addQuestions(document, "Lien avec la vie réel", listQuestionsLienVieReel)

        document.close()

        val file = File(outputPath)
        sharePdf(file, context)
//        onNavigate()

    }


    fun createPlanificationProjetPdf(
        context: Context,
        user: User,
        ideeProjet: String,
        motivation: String,
        ressourceDisponible: String,
        ressourceIndisponible: String,
        competenceDisponible: List<AppSkillCardIcon>,
        competenceIndisponible: List<AppSkillCardIcon>,
        planAction: List<PlanAction>,
        outputPath: String = "${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}" +
                "/planification_projet_${user.nom}_${user.prenom}_${LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))}.pdf",
        onEnd: (File) -> Unit
        ) {

        Timber.tag("pdf").d(outputPath)
        val pdfWriter = PdfWriter(outputPath)
        val pdfDoc = PdfDocument(pdfWriter)
        pdfDoc.defaultPageSize = PageSize.A4
        val document = Document(pdfDoc, PageSize.A4)
        document.setMargins(50f, 40f, 50f, 40f)

        // watermark et footer handlers
        val watermarkImageData = getImageDataFromResource(context, R.drawable.logo)
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, WatermarkImageEventHandler(watermarkImageData))
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, SimpleFooterEventHandler())

        // --- Infos utilisateur
        addUserInfoSection(document, user, context)

        document.add(Paragraph("\n\n\n"))

        document.add(Paragraph("Planification de projet").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n\n\n"))

        // --- Idee de projet
        document.add(Paragraph("Idée de projet").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(ideeProjet)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n\n\n"))

        // --- Motivation
        document.add(Paragraph("Motivation").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(motivation)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        // --- Competences disponible
        document.add(Paragraph("Competences disponibles")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        if (competenceDisponible.isNotEmpty()) {
            val table = Table(UnitValue.createPercentArray(3)).useAllAvailableWidth()

            competenceDisponible.forEach { competence ->
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
            document.add(Paragraph("Pas de compétance disponible"))
        }

        document.add(Paragraph("\n\n\n"))

        // --- Competences non disponible
        document.add(Paragraph("Competences non disponible")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        if (competenceIndisponible.isNotEmpty()) {
            val table = Table(UnitValue.createPercentArray(3)).useAllAvailableWidth()

            competenceIndisponible.forEach { competence ->
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
            document.add(Paragraph("Pas de compétence non disponible"))
        }

        document.add(Paragraph("\n\n\n"))

        // --- Ressources disponibles
        document.add(Paragraph("Ressources disponibles").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(ressourceDisponible)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n\n\n"))

        // --- Ressources disponibles
        document.add(Paragraph("Ressources non disponibles").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(ressourceIndisponible)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n\n\n"))

        // --- Plan d'action
        document.add(Paragraph("Plan d'action")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        if (planAction.isNotEmpty()) {
            val table = Table(UnitValue.createPercentArray(4)).useAllAvailableWidth()

            table.addCell(Cell()
                .add(Paragraph("Activité (quoi ?)"))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            table.addCell(Cell()
                .add(Paragraph("Qui fait ?"))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            table.addCell(Cell()
                .add(Paragraph("Qui finance ?"))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            table.addCell(Cell()
                .add(Paragraph("Quand ?"))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            planAction.forEach { action ->
                val cellActivite = Cell()
                    .add(Paragraph(action.activite))
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10f)
                val cellActeur = Cell()
                    .add(Paragraph(action.acteur))
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10f)
                val cellFinance = Cell()
                    .add(Paragraph(action.financement))
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10f)
                val cellPeriode = Cell()
                    .add(Paragraph(action.periode))
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10f)

                table.addCell(cellActivite)
                table.addCell(cellActeur)
                table.addCell(cellFinance)
                table.addCell(cellPeriode)
            }

            document.add(table)
        } else {
            document.add(Paragraph("Pas de plan d'action defini"))
        }


        document.close()

        val file = File(outputPath)
        onEnd(file)

    }

    fun createAllProjectPdf(
        context: Context,
        user: User,
        listActeursFamiliaux: ReseauSection,
        listActeursEducatifs: ReseauSection,
        listActeursProfessionnels: ReseauSection,
        listActeursInstitutionnelsEtDeSoutien: ReseauSection,
        listQuestionsLienVieReel: List<Pair<String, String>>,
        listOfPassedElement: List<Element>,
        listOfPresentElement: List<Element>,
        listQuestionsLigneDeVie: List<Pair<String, String>>,
        competences: List<AppSkillCardIcon>,
        outputPath: String = "${context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)}" +
                "/projet_de_vie_complet${user.nom}_${user.prenom}_${LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))}.pdf",
        ideeProjet: String,
        motivation: String,
        ressourceDisponible: String,
        ressourceIndisponible: String,
        competenceDisponible: List<AppSkillCardIcon>,
        competenceIndisponible: List<AppSkillCardIcon>,
        planAction: List<PlanAction>,
        setOfIds:Set<Int>

        ) {
        Timber.tag("pdf").d(outputPath)
        val pdfWriter = PdfWriter(outputPath)
        val pdfDoc = PdfDocument(pdfWriter)
        pdfDoc.defaultPageSize = PageSize.A4
        val document = Document(pdfDoc, PageSize.A4)
        document.setMargins(50f, 40f, 50f, 40f)
        val pageWidth = pdfDoc.defaultPageSize.width

        // watermark et footer handlers
        val watermarkImageData = getImageDataFromResource(context, R.drawable.logo)
        pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, WatermarkImageEventHandler(watermarkImageData))
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, SimpleFooterEventHandler())

        // --- Infos utilisateur
        addUserInfoSection(document, user, context)

        document.add(Paragraph("\n\n\n"))

        // --- Decouvrir mon reseau

        addReseauSection(
            document,
            context,
            listActeursFamiliaux,
            pageWidth
        )
        addReseauSection(
            document,
            context,
            listActeursEducatifs,
            pageWidth
        )
        addReseauSection(
            document,
            context,
            listActeursProfessionnels,
            pageWidth
        )
        addReseauSection(
            document,
            context,
            listActeursInstitutionnelsEtDeSoutien,
            pageWidth
        )

        document.add(Paragraph("\n\n\n"))

        //----- Ligne de vie

        addLigneDeVieSection(
            context,
            document,
            "Évènements du passé",
            listOfPassedElement,
            pageWidth,
            setOfIds
        )
        document.add(Paragraph("\n\n\n"))
        addLigneDeVieSection(
            context,
            document,
            "Évènements du présent",
            listOfPresentElement,
            pageWidth,
            setOfIds
        )
        document.add(Paragraph("\n\n\n"))
        addQuestions(
            document,
            "Questions sur la ligne de vie",
            listQuestionsLigneDeVie
        )

        document.add(Paragraph("\n\n\n"))

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

        document.add(Paragraph("\n\n\n"))

        // --- Lien de vie réel
        addQuestions(document, "Lien avec la vie réel", listQuestionsLienVieReel)

        document.add(Paragraph("Planification de projet").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n\n\n"))

        // --- Idee de projet
        document.add(Paragraph("Idée de projet").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(ideeProjet)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n\n\n"))

        // --- Motivation
        document.add(Paragraph("Motivation").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(motivation)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        // --- Competences disponible
        document.add(Paragraph("Competences disponibles")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        if (competenceDisponible.isNotEmpty()) {
            val table = Table(UnitValue.createPercentArray(3)).useAllAvailableWidth()

            competenceDisponible.forEach { competence ->
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
            document.add(Paragraph("Pas de compétance disponible"))
        }

        document.add(Paragraph("\n\n\n"))

        // --- Competences non disponible
        document.add(Paragraph("Competences non disponible ou à renforcer")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        if (competenceIndisponible.isNotEmpty()) {
            val table = Table(UnitValue.createPercentArray(3)).useAllAvailableWidth()

            competenceIndisponible.forEach { competence ->
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
            document.add(Paragraph("Pas de compétence non disponible"))
        }

        document.add(Paragraph("\n\n\n"))

        // --- Ressources disponibles
        document.add(Paragraph("Ressources disponibles").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(ressourceDisponible)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n\n\n"))

        // --- Ressources disponibles
        document.add(Paragraph("Ressources non disponibles").setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(ressourceIndisponible)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n\n\n"))

        // --- Plan d'action
        document.add(Paragraph("Plan d'action")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        if (planAction.isNotEmpty()) {
            val table = Table(UnitValue.createPercentArray(4)).useAllAvailableWidth()

            table.addCell(Cell()
                .add(Paragraph("Activité (quoi ?)"))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            table.addCell(Cell()
                .add(Paragraph("Qui fait ?"))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            table.addCell(Cell()
                .add(Paragraph("Qui finance ?"))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            table.addCell(Cell()
                .add(Paragraph("Quand ?"))
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(16f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            planAction.forEach { action ->
                val cellActivite = Cell()
                    .add(Paragraph(action.activite))
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10f)
                val cellActeur = Cell()
                    .add(Paragraph(action.acteur))
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10f)
                val cellFinance = Cell()
                    .add(Paragraph(action.financement))
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10f)
                val cellPeriode = Cell()
                    .add(Paragraph(action.periode))
                    .setFontSize(14f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setPadding(10f)

                table.addCell(cellActivite)
                table.addCell(cellActeur)
                table.addCell(cellFinance)
                table.addCell(cellPeriode)
            }

            document.add(table)
        } else {
            document.add(Paragraph("Pas de plan d'action defini"))
        }


        document.close()

        val file = File(outputPath)
        sharePdf(file, context)

    }

    private fun addLigneDeVieSection(context: Context,document: Document, titre: String, elements: List<Element>,pageWidth: Float,setOfIds:Set<Int>) {
        val tableWidth = pageWidth - 80f
        document.add(Paragraph(titre)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        document.add(Paragraph("\n"))
        val container = Div().setWidth(tableWidth)

        if (elements.isNotEmpty()) {
            elements.forEach { element ->

                val labelTable = Table(UnitValue.createPercentArray(floatArrayOf(10f, 90f)))
                    .setWidth(UnitValue.createPercentValue(100f))
                    .setMarginBottom(10f)

                val labelImageCell = Cell()
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.RIGHT)

                val imageId = when(element.id){
                    1->R.drawable.ecole_primaire
                    2->R.drawable.ecole_secondaire
                    3->R.drawable.universite_ecole_superieur
                    4->R.drawable.alphabetisation_langue_locale
                    5->R.drawable.ecole_coranique
                    6->R.drawable.ecole_formation_prof
                    7->R.drawable.abandon_scolarite
                    8->R.drawable.reprise_interruption_etude
                    9->R.drawable.premier_apprentissage
                    10->R.drawable.naissance_enfant
                    11->R.drawable.mariage
                    12->R.drawable.depart_foyer_familial
                    13->R.drawable.ecole_coranique
                    14->R.drawable.premier_emploi
                    15->R.drawable.projet
                    16->R.drawable.deces
                    17->R.drawable.depart_migration
                    18->R.drawable.retrouvaille
                    19->R.drawable.grande_decision_personnel
                    else -> R.drawable.ecole_primaire
                }

                val labelImage = Image(getImageDataFromResource(context, imageId))
                    .setWidth(40f)
                    .setHeight(40f)

                labelImageCell.add(labelImage)

                val labelTextCell = Cell()
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT)

                val labelText = Paragraph(element.label.replace("\n", " "))
                    .setFontSize(15f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))

                labelTextCell.add(labelText)
                labelTable.addCell(labelImageCell)
                labelTable.addCell(labelTextCell)
                container.add(labelTable)


                val yearText = if (element.status && element.id !in setOfIds) {
                    "Année: ${element.inProgressYear}"
                } else if(!element.status && element.id !in setOfIds) {
                    "De ${element.startYear} à ${element.endYear}"
                }else{
                    "Année: ${element.inProgressYear}"
                }

                val fullText = "▪ $yearText : ${element.labelDescription}"

                val descriptionTable = Table(UnitValue.createPercentArray(floatArrayOf(100f)))
                    .setWidth(tableWidth)
                    .setMarginBottom(5f)

                val descriptionCell = Cell()
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.LEFT)

                val descriptionParagraph = Paragraph(fullText)
                    .setFontSize(13f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))

                descriptionCell.add(descriptionParagraph)
                descriptionTable.addCell(descriptionCell)
                container.add(descriptionTable)
            }
        } else {
            container.add(Paragraph("Pas d'élément").setTextAlignment(TextAlignment.LEFT)
                .setFontSize(15f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        }
        document.add(container)
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

    fun getImageDataFromPathOrResource(context: Context, imagePath: String?, fallbackResId: Int): ImageData {
        val bitmap = if (!imagePath.isNullOrEmpty()) {
            if (imagePath.startsWith("content://")) {
                // Chargement depuis un content Uri
                try {
                    val uri = imagePath.toUri()
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        BitmapFactory.decodeStream(inputStream)
                    }
                } catch (e: Exception) {
                    // Si échec, fallback sur le drawable resource
                    loadBitmapFromResource(context, fallbackResId)
                }
            } else {
                // Chargement depuis un chemin de fichier "classique"
                BitmapFactory.decodeFile(imagePath) ?: loadBitmapFromResource(context, fallbackResId)
            }
        } else {
            // Pas d'image, fallback sur le drawable resource
            loadBitmapFromResource(context, fallbackResId)
        }

        // Contrôle nullité finale
        if (bitmap == null) {
            throw IllegalStateException("Failed to load image from URI, file path and resource")
        }

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return ImageDataFactory.create(stream.toByteArray())
    }


    private fun loadBitmapFromResource(context: Context, resId: Int): Bitmap? {
        return try {
            val drawable = ContextCompat.getDrawable(context, resId)
                ?: return null

            if (drawable is BitmapDrawable) {
                drawable.bitmap
            } else {
                val width = drawable.intrinsicWidth
                val height = drawable.intrinsicHeight

                // Check for valid dimensions
                if (width <= 0 || height <= 0) {
                    return null
                }

                val bitmap = createBitmap(width, height)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, width, height)
                drawable.draw(canvas)
                bitmap
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun addUserInfoSection(document: Document, user: User, context: Context) {


        // Tableau à 2 colonnes
        val table = Table(UnitValue.createPercentArray(floatArrayOf(1f, 3f)))
            .useAllAvailableWidth()

        // Cellule pour les infos
        val infos = StringBuilder()
        infos.append("Nom : ${user.nom} \n")
        infos.append("Prénoms: ${user.prenom}\n")

        if (user.numTel.isNotEmpty() && user.numTel.isNotBlank())
            infos.append("Tel: ${user.numTel}\n")

        table.addCell(Cell().add(Paragraph(infos.toString()))
            .setBorder(null)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
        )

        document.add(table)
    }

    fun addReseauSection(document: Document, titre: String, data: Map<String, List<String>>) {
        document.add(Paragraph(titre)
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        if (data.any { it.value.isNotEmpty() }) {
            data.filter { it.value.isNotEmpty() }.forEach { (categorie, valeurs) ->
                document.add(Paragraph(categorie).setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(15f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
                val table = Table(UnitValue.createPercentArray(2)).useAllAvailableWidth()
                valeurs.chunked(2).forEach { chunk ->
                    table.addCell("Nom et prénoms: ${chunk.getOrNull(0) ?: ""}").setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(13f)
                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                    table.addCell("Description: ${chunk.getOrNull(1) ?: ""}").setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(13f)
                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                }
                document.add(table)
            }
        } else {
            document.add(Paragraph("Aucune information enregistrée"))
        }
    }

    fun addLigneDeVieSection(document: Document, titre: String, elements: List<Element>) {
        document.add(Paragraph(titre)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        if (elements.isNotEmpty()) {
            elements.forEach { element ->
                document.add(Paragraph(element.label).setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(15f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)))

                if (element.status) {
                    document.add(Paragraph("Année: ${element.inProgressYear}").setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(15f)
                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)))
                } else {
                    document.add(Paragraph("De ${element.startYear} à ${element.endYear}").setTextAlignment(TextAlignment.LEFT)
                        .setFontSize(15f)
                        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)))
                }
                document.add(Paragraph(element.labelDescription).setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(13f)
                    .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)))
            }
        } else {
            document.add(Paragraph("Pas d'élément").setTextAlignment(TextAlignment.LEFT)
                .setFontSize(15f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        }
    }

    fun addQuestions(document: Document, titre: String, questions: List<Pair<String, String>>) {
        document.add(Paragraph(titre).setTextAlignment(TextAlignment.CENTER)
            .setFontSize(17.5f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

        questions.forEach { element ->
            document.add(Paragraph(element.first).setTextAlignment(TextAlignment.LEFT)
                .setFontSize(15f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
            document.add(Paragraph(element.second).setTextAlignment(TextAlignment.LEFT)
                .setFontSize(13f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)))
        }
    }

    fun getImageDataFromResource(context: Context, resId: Int): com.itextpdf.io.image.ImageData {
        val drawable: Drawable = ContextCompat.getDrawable(context, resId)!!
        val bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val b = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
            val canvas = Canvas(b)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            b
        }

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return ImageDataFactory.create(stream.toByteArray())
    }

    fun sharePdf(file: File, context: Context) {
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

}