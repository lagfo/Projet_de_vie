package org.ticanalyse.projetdevie.utils

import com.itextpdf.commons.actions.IEvent
import com.itextpdf.commons.actions.IEventHandler
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEvent
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEventHandler
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent

class SimpleFooterEventHandler : AbstractPdfDocumentEventHandler() {

    override fun addType(type: String?): AbstractPdfDocumentEventHandler? {
        return super.addType(type)
    }

    override fun onAcceptedEvent(event: AbstractPdfDocumentEvent?) {
        val docEvent = event as PdfDocumentEvent
        val pdfDoc = docEvent.document
        val page = docEvent.page
        val pageNumber = pdfDoc.getPageNumber(page)
        val totalPages = pdfDoc.numberOfPages
        val canvas = PdfCanvas(page)
        val pageSize = page.pageSize

        canvas.beginText()
        val font = PdfFontFactory.createFont()
        canvas.setFontAndSize(font, 10f)
        canvas.moveText(((pageSize.left + pageSize.right) / 2 - 20).toDouble(),
            (pageSize.bottom + 15).toDouble()
        )
        canvas.showText("Page $pageNumber / $totalPages")
        canvas.endText()

    }

    override fun onEvent(event: IEvent?) {
    }
}
