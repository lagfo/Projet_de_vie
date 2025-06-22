package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(
    tableName ="Element",
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
data class Element(
    @PrimaryKey
    val id:Int=0,
    var label:String="",
    var startYear:Int=0,
    var endYear:Int=0,
    var inProgressYear:Int=0,
    var duration:Int=0,
    var labelDescription:String="",
    var userId:Int=1,
    var status: Boolean=false, // 0 when it 's the pass and 1 when it's the present
    var creationDate: String=""
): Parcelable
