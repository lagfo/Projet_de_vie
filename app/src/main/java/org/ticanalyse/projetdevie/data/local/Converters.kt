package org.ticanalyse.projetdevie.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.ticanalyse.projetdevie.domain.model.ActeursEducatifs
import org.ticanalyse.projetdevie.domain.model.ActeursFamiliauxEtSociaux
import org.ticanalyse.projetdevie.domain.model.ActeursInstitutionnelsEtDeSoutien
import org.ticanalyse.projetdevie.domain.model.ActeursProfessionnels

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromActeursFamiliaux(value: ActeursFamiliauxEtSociaux?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toActeursFamiliaux(value: String?): ActeursFamiliauxEtSociaux? {
        if (value.isNullOrEmpty()) return null
        val type = object : TypeToken<ActeursFamiliauxEtSociaux>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromActeursInstitutionnels(value: ActeursInstitutionnelsEtDeSoutien?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toActeursInstitutionnels(value: String?): ActeursInstitutionnelsEtDeSoutien? {
        if (value.isNullOrEmpty()) return null
        val type = object : TypeToken<ActeursInstitutionnelsEtDeSoutien>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromActeursProfessionnels(value: ActeursProfessionnels?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toActeursProfessionnels(value: String?): ActeursProfessionnels? {
        if (value.isNullOrEmpty()) return null
        val type = object : TypeToken<ActeursProfessionnels>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromActeursEducatifs(value: ActeursEducatifs?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toActeursEducatifs(value: String?): ActeursEducatifs? {
        if (value.isNullOrEmpty()) return null
        val type = object : TypeToken<ActeursEducatifs>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return if (value == null) null else gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value.isNullOrEmpty()) return null
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

}