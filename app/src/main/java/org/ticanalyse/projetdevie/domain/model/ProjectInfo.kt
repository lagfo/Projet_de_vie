package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class ProjectInfo(
    @PrimaryKey val id: Int = 1,
    var projetIdee:String="",
    var motivation:String="",
    var competenceDisponible: List<String> ?=emptyList(),
    var competenceNonDisponible: List<String> ?=emptyList(),
    var ressourceDisponible:String="",
    var ressourceNonDispnible:String="",
    var creationDate: String=""
): Parcelable