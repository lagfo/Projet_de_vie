package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.usecase.ligne_de_vie_usecase.AddLigneDeVieElement
import org.ticanalyse.projetdevie.domain.usecase.ligne_de_vie_usecase.GetLineDeVieElement

@HiltViewModel
class LigneDeVieViewModel @Inject constructor(
    private val addLigneDeVie: AddLigneDeVieElement,
    private val getLineDeVieElement: GetLineDeVieElement
): ViewModel() {

    fun addElement(
        label:String,
        startYear:Int,
        endYear:Int,
        duration:Int,
        labelDescription:String,
        userId:Int=1,
        status: Boolean
        ,creationDate:String){

        val element= Element(
            label = label,
            startYear = startYear,
            endYear = endYear,
            duration = duration,
            labelDescription =labelDescription,
            userId =userId,
            status =status,
            creationDate =creationDate
        )
        viewModelScope.launch {
            addLigneDeVie(element)
        }
    }

}