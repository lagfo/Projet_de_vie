package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(
    tableName ="ReponseQuestionLigneDeVie",
    foreignKeys=[
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
    )
data class ReponseQuestionLigneDeVie(
    @PrimaryKey(autoGenerate = true)
    val id:Int=1,
    var firstResponse:String,
    var secondResponse:String,
    var userId:Int,
    var status: Boolean, // 0 when it 's the pass and 1 when it's the present
    var creationDate: String
): Parcelable
