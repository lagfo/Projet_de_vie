package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class LienVieReel(
    @PrimaryKey val id: Int = 1,
    val firstResponse: String,
    val secondResponse: String,
    val thirdResponse: String,
    var creationDate: String=""
): Parcelable