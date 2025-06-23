package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.usecase.ligne_de_vie_usecase.AddLigneDeVieElement
import org.ticanalyse.projetdevie.domain.usecase.ligne_de_vie_usecase.GetLineDeVieElement
import org.ticanalyse.projetdevie.domain.usecase.ligne_de_vie_usecase.GetPassedElement
import org.ticanalyse.projetdevie.domain.usecase.ligne_de_vie_usecase.GetPresentElement

@HiltViewModel
class LigneDeVieViewModel @Inject constructor(
    private val addLigneDeVie: AddLigneDeVieElement,
    private val getLineDeVieElement: GetLineDeVieElement,
    private val getPassedElement: GetPassedElement,
    private val getPresentElement: GetPresentElement
): ViewModel() {
    fun addElement(
        id:Int,
        label:String,
        startYear:Int,
        endYear:Int,
        inProgressYear:Int,
        duration:Int,
        labelDescription:String,
        userId:Int=1,
        status: Boolean
        ,creationDate:String){

        val element= Element(
            id = id,
            label = label,
            startYear = startYear,
            endYear = endYear,
            inProgressYear =inProgressYear ,
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

    private val _elements = MutableStateFlow<List<Element>>(emptyList())
    val elements: StateFlow<List<Element>> = _elements

    private val _passedelements = MutableStateFlow<List<Element>>(emptyList())
    val passedelEments: StateFlow<List<Element>> = _passedelements

    private val _presentElements=MutableStateFlow<List<Element>>(emptyList())
    val elementEncours: StateFlow<List<Element>> =_presentElements

    init {

        viewModelScope.launch {
            getLineDeVieElement().collect { listElement ->
                _elements.value = listElement
            }
        }

        viewModelScope.launch {
            getPassedElement().collect{passedelEment->
                _passedelements.value=passedelEment
            }
        }

        viewModelScope.launch {
            getPresentElement().collect{presentElement->
                _presentElements.value=presentElement
            }
        }
    }
}