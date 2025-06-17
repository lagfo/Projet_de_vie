package org.ticanalyse.projetdevie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActeursFamiliauxEtSociaux(
    val amisProches: String="",
    val chefsCoutumiersReligieux: String="",
    val freresSoeursCousinsCousines: String="",
    val grandsParents: String="",
    val leadersCommunautairesAssociationsLocales: String="",
    val mentorModeleCommunaute: String="",
    val parentsTuteurs: String="",
    var voisins: String=""
):Parcelable