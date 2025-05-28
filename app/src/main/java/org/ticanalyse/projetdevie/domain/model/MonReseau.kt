package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MonReseau(
    @PrimaryKey val id: Int = 1,
    val acteursFamiliauxSociaux: ActeursFamiliauxEtSociaux?,
    val acteursInstitutionnelsSoutien: ActeursInstitutionnelsEtDeSoutien?,
    val acteursProfessionnels: ActeursProfessionnels?,
    val acteursEducatifs: ActeursEducatifs?
):Parcelable