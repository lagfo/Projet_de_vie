package org.ticanalyse.projetdevie.utils

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
}