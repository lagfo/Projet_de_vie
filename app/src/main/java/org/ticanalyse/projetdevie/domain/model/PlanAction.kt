package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class PlanAction(
    @PrimaryKey(autoGenerate = true) val id:Int=0,
    var activite:String="",
    var acteur:String="",
    var financement:String="",
    var periode:String="",
    var creationDate: String=""
): Parcelable