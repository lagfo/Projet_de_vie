package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActeursProfessionnels(
    val anciensEmployeursMaitresApprentissage: String="",
    val artisansEntrepreneursLocaux: String="",
    val employesONGProjetsDeveloppement: String="",
    val personnesRessourcesCooperativesGroupementsMutuelles: String=""
):Parcelable