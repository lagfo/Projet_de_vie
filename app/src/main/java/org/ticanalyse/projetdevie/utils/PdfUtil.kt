package org.ticanalyse.projetdevie.utils

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.itextpdf.io.font.constants.StandardFonts
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
import androidx.core.graphics.createBitmap
import com.itextpdf.io.image.ImageData
import com.itextpdf.kernel.pdf.xobject.PdfXObject
import com.itextpdf.layout.element.AreaBreak
import com.itextpdf.layout.properties.AreaBreakType
import com.itextpdf.layout.properties.BackgroundImage
import java.io.File

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
        outputPath: String = "${context.filesDir}/${user.nom} ${user.prenom}.pdf",
        onNavigate: () -> Unit
    ) {
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

    fun getImageDataFromPathOrResource(context: Context, imagePath: String?, fallbackResId: Int): ImageData {
        val bitmap = if (!imagePath.isNullOrEmpty()) {
            BitmapFactory.decodeFile(imagePath)
        } else {
            val drawable = ContextCompat.getDrawable(context, fallbackResId)!!
            if (drawable is BitmapDrawable) {
                drawable.bitmap
            } else {
                val b = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)
                val canvas = Canvas(b)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                b
            }
        }

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return ImageDataFactory.create(stream.toByteArray())
    }

    fun addUserInfoSection(document: Document, user: User, context: Context) {
        // Créer la photo en iText
        val photoImageData = getImageDataFromPathOrResource(context, user.avatarUri, R.drawable.avatar)
        val photo = com.itextpdf.layout.element.Image(photoImageData)
            .scaleToFit(80f, 80f)
            .setHorizontalAlignment(HorizontalAlignment.CENTER)

        // Tableau à 2 colonnes
        val table = Table(UnitValue.createPercentArray(floatArrayOf(1f, 3f)))
            .useAllAvailableWidth()

        // Cellule pour la photo
        table.addCell(
            Cell().add(photo)
            .setBorder(null)
            .setVerticalAlignment(VerticalAlignment.MIDDLE)
        )

        // Cellule pour les infos
        val infos = StringBuilder()
        infos.append("Nom et prénoms: ${user.nom} ${user.prenom}\n")
        infos.append("Sexe: ${user.genre}\n")
        infos.append("Date de naissance: ${user.dateNaissance}\n")
        infos.append("Téléphone: ${user.numTel}\n")
        if (user.email.isNotEmpty() && user.email.isNotBlank())
            infos.append("Email: ${user.email}\n")

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
            document.add(Paragraph(element.first).setTextAlignment(TextAlignment.LEFT)
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