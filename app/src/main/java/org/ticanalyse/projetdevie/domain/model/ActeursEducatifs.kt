package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActeursEducatifs(
    val anciensCamaradesClasse: String="",
    val animateursONGEducatives: String="",
    val ConseillersOrientationScolaireProfessionnelle: String="",
    val EncadreursCentresFormationProfessionnelle: String="",
    val EnseignantsProfesseurs: String=""
):Parcelable