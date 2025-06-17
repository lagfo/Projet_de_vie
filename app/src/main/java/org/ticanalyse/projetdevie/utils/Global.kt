package org.ticanalyse.projetdevie.utils

import java.time.LocalDate
import java.time.LocalTime

object Global {

    fun validateTextEntries(vararg values: String): Boolean {
        for (value in values) {
            if (value.isBlank()) {
                return false
            }
        }
        return true
    }

    fun validateAge(value: String):Boolean{
        if(value.isBlank())
            return false
        else if(value.toInt() <= 13)
            return false
        return true
    }

    fun validateNumber(value: String):Boolean{
        if(value.isBlank())
            return false
        else if(value.length <= 7)
            return false
        else if(value.length > 8)
            return false
        return true
    }

    fun validateAnneeDeFin(year:Int): Boolean{
        val actualDate= LocalDate.now()
        return year <actualDate.year || year<0
    }

    fun validateAnneeDeDebut(year:Int,yearFin:Int,birthYear:Int): Boolean{
        if(yearFin-year<0){
            return false
        }else{
            if(year<birthYear-5){
                return false
            }
        }
        return true
    }
}