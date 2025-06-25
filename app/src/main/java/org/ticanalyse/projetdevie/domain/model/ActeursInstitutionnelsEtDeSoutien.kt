package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActeursInstitutionnelsEtDeSoutien(
    val agentsServicesSociauxAdministratifs: String="",
    val formateursProgrammesPublicsPrivesFormationInsertion: String="",
    val personnelSante: String="",
    val representantsStructuresCommeAgenceNationaleEmploi: String=""
):Parcelable