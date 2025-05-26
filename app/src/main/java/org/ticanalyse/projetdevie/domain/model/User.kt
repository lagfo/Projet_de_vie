package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey val id: Int = 0,
    val nom: String,
    val prenom: String,
    val age: String,
    val numTel: String,
    val genre: String,
    val avatarUri: String
): Parcelable
