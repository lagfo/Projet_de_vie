package org.ticanalyse.projetdevie.presentation.mon_reseau

sealed class MonReseauEvent {

    data class UpsertMonReseau(val index:Int,val category: String, val nom:String,val description:String ):MonReseauEvent()
}