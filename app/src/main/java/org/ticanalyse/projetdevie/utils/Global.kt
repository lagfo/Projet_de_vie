package org.ticanalyse.projetdevie.utils

import android.content.Context
import android.net.Uri
import android.util.Patterns
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object Global {

    fun validateTextEntries(vararg values: String): Boolean {
        for (value in values) {
            if (value.isBlank()) {
                return false
            }
        }
        return true
    }

    /*fun validateAge(value: String): Boolean{
        if(value.isBlank())
            return false
        else if(value.toInt() <= 13)
            return false
        return true
    }

     */

    private val BIRTHDATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    fun validateAge(value: String): Boolean =
        value.takeIf { it.isNotBlank() }
            ?.let {
                runCatching { LocalDate.parse(it, BIRTHDATE_FORMATTER) }
                    .getOrNull()
            }
            ?.let { Period.between(it, LocalDate.now()).years > 13 }
            ?: false


    fun validateNumber(value: String): Boolean{
        if(value.isBlank())
            return false
        else if(value.length <= 7)
            return false
        else if(value.length > 8)
            return false
        return true
    }

    fun validateEmail(email: String): Boolean {
        return if (email.isBlank() || email.isEmpty()) {
            true
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
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

    fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",
            Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        // Get the directory: context.filesDir/images/
        val storageDir = File(context.cacheDir.path)
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File.createTempFile(
            imageFileName, /* prefix */
            ".jpg",        /* suffix */
            storageDir     /* directory */
        )
    }

    fun getUriForFile(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Authority matches AndroidManifest
            file
        )
    }

    fun checkPassedAndPresent(startYear:Int,endYear:Int,inProgressYear: Int): Boolean{
        return !(startYear.toString().isNotBlank() && endYear.toString().isNotBlank() && inProgressYear.toString().isNotBlank())
    }

//    fun checkYearLength(startYear:Int,endYear:Int,inProgressYear: Int): Boolean{
//        if(startYear.toString().length==4 && startYear.toString().startsWith('1') || startYear.toString().length==4 && startYear.toString().startsWith('2')){
//            return true;
//        }else if(endYear.toString().length==4 && endYear.toString().startsWith('1') || endYear.toString().length==4 && endYear.toString().startsWith('2')){
//            return true
//        }else if(){
//
//            return true
//        }else{
//            return false;
//        }
//    }


    fun isValideResponse(response1:String,response2: String): Boolean{
        return response1.isNotBlank()&&response1.isNotEmpty()&&response2.isNotEmpty()&&response2.isNotBlank()
    }
}