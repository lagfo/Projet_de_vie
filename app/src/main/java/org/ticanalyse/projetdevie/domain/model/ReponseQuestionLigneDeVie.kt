package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName ="ReponseQuestionLigneDeVie")
data class ReponseQuestionLigneDeVie(
    @PrimaryKey(autoGenerate = true)
    val id:Int=1,
    var firstResponse:String,
    var secondResponse:String,
    var creationDate: String
): Parcelable
