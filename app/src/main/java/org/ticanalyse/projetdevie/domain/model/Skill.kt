package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Skill(
    @PrimaryKey val id: Int = 1,
    val skills: List<String> ?
): Parcelable