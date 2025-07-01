package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.LigneDeVieRepository

enum class  DISPLAY_TYPE{
    ALL,
    IN_PROGRESS,
    PASSED,
    ON_GOING
}
data  class ScreenState(
    val  displayType: DISPLAY_TYPE=DISPLAY_TYPE.ALL,
    val currentList: List<Element> = emptyList(),
)
@HiltViewModel
class LigneDeVieViewModel @Inject constructor(
    private val repository: LigneDeVieRepository
): ViewModel() {

    private val _upsertSuccess = MutableStateFlow(false)
    val upsertSuccess: StateFlow<Boolean> = _upsertSuccess.asStateFlow()
    fun resetUpsertStatus(){
        _upsertSuccess.value=false
    }
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
            try{
                repository.insertLigneDeVieElement(element)
                _upsertSuccess.value=true
                Log.d("TAG", "addElement: insertion succeed and status is $upsertSuccess")

            }catch (e: Exception){
                _upsertSuccess.value=false
                Log.d("TAG", "addElement: insertion failed . Error message : ${e.message}")
            }
        }
    }

//    private val _elements = MutableLiveData<List<Element>>(emptyList())
//    val elements: LiveData<List<Element>> = _elements
//
//    private val _passedelements = MutableLiveData<List<Element>>(emptyList())
//    val passedelEments: LiveData<List<Element>> = _passedelements
//
//    private val _presentElements= MutableLiveData<List<Element>>(emptyList())
//    val elementEncours: LiveData<List<Element>> =_presentElements

//    init {
//
//        viewModelScope.launch {
//            getLineDeVieElement().collect { listElement ->
//                _elements.value = listElement
//            }
//        }
//
//        viewModelScope.launch {
//            getPassedElement().collect{passedelEment->
//                _passedelements.value=passedelEment
//            }
//        }
//
//        viewModelScope.launch {
//            getPresentElement().collect{presentElement->
//                _presentElements.postValue(presentElement)
//            }
//        }
//    }

//    val passedElements: LiveData<List<Element>> =getPassedElement()
//    val presentElements: LiveData<List<Element>> =getPresentElement()

//    private val _uiState=MutableStateFlow(ScreenState())
//    val uiState: StateFlow<ScreenState> =_uiState.asStateFlow()
//
//    init{
//        loadPassedElements()
//        loadPresentElements()
//    }

//    private fun loadPassedElements(){
//        viewModelScope.launch {
//            _uiState.value=_uiState.value.copy(isLoadingPassedElement = true)
//            try {
//                getPassedElement().collect { passedElement ->
//                    _uiState.value = _uiState.value.copy(
//                        passedElements = passedElement,
//                        isLoadingPassedElement = false
//                    )
//                }
//            }catch (e: Exception){
//                _uiState.value=_uiState.value.copy(error = e.message, isLoadingPassedElement = false)
//
//            }
//        }
//    }

//    private fun loadPresentElements(){
//        viewModelScope.launch {
//            _uiState.value=_uiState.value.copy(isLoadingPresentElement = true)
//            try {
//                getPresentElement().collect {presentElement ->
//                    _uiState.value = _uiState.value.copy(
//                        presentElements = presentElement,
//                        isLoadingPresentElement = false
//                    )
//                }
//                Log.d("TAG", "loadPresentElements: ${_uiState.value.presentElements} ")
//            }catch (e: Exception){
//                _uiState.value=_uiState.value.copy(error = e.message, isLoadingPresentElement =false)
//            }
//        }
//
//    }

    val uiState=mutableStateOf(ScreenState())
init{
    getAllElements()
    getPassedElements()
    getPresentElements()
}
    private val _allElement = MutableStateFlow<List<Element>>(emptyList())
    val allElement: StateFlow<List<Element>> = _allElement.asStateFlow()
    fun getAllElements(){
        viewModelScope.launch {
            repository.getElements().collect { listElement ->
                _allElement.value=listElement
            }
        }
        Log.d("TAG", "getAllElements: $allElement ")
    }

    private val _allPassedElement = MutableStateFlow<List<Element>>(emptyList())
    val allPassedElement: StateFlow<List<Element>> = _allPassedElement.asStateFlow()
    fun getPassedElements(){
        viewModelScope.launch {
            repository.getPassedElements().collect { listPassedElement->
                _allPassedElement.value=listPassedElement
            }
        }
    }

    private val _allPresentElement = MutableStateFlow<List<Element>>(emptyList())
    val allPresentElement: StateFlow<List<Element>> = _allPresentElement.asStateFlow()
    fun getPresentElements(){
        viewModelScope.launch {
            repository.getPresentElements().collect { listPresentElement->
                _allPresentElement.value=listPresentElement
            }
        }
    }

}