package org.ticanalyse.projetdevie.utils

import com.itextpdf.commons.actions.IEvent
import com.itextpdf.commons.actions.IEventHandler
import com.itextpdf.io.image.ImageData
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEvent
import com.itextpdf.kernel.pdf.event.AbstractPdfDocumentEventHandler
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState

class WatermarkImageEventHandler(
    private val imageData: ImageData
) : AbstractPdfDocumentEventHandler() {
    override fun addType(type: String?): AbstractPdfDocumentEventHandler? {
        return super.addType(type)
    }

    override fun onAcceptedEvent(event: AbstractPdfDocumentEvent?) {
        val docEvent = event as PdfDocumentEvent
        val pdfCanvas = PdfCanvas(
            docEvent.page.newContentStreamBefore(),
            docEvent.page.resources,
            docEvent.document
        )
        val pageSize = docEvent.page.pageSize

        pdfCanvas.saveState()
        pdfCanvas.setExtGState(PdfExtGState().setFillOpacity(0.1f)) // transparence

        val x = (pageSize.left + pageSize.right) / 2 - imageData.width / 2
        val y = (pageSize.bottom + pageSize.top) / 2 - imageData.height / 2

        pdfCanvas.addImageAt(imageData, x, y, false)
        pdfCanvas.restoreState()
    }
}
