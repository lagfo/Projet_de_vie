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
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.LigneDeVieRepository
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.ReponseQuestionLigneDeVieRepository
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.ResponseLigneDeVieElementRepository
import org.ticanalyse.projetdevie.domain.usecase.user.GetCurrentUserUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.SetCurrentUserUseCase
import org.ticanalyse.projetdevie.utils.Result
import timber.log.Timber

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
    private val repository: LigneDeVieRepository,
    private val reponseLigneDeVieRepository: ResponseLigneDeVieElementRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val setCurrentUserUseCase: SetCurrentUserUseCase
): ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        getCurrentUser()
    }

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
    fun addResponsesLigneDeVie(
        id:Int=1,
        firstResponse:String,
        secondResponse:String,
        creationDate: String,
        ){

        val response= ReponseQuestionLigneDeVie(
            id = id,
            firstResponse = firstResponse,
            secondResponse = secondResponse,
            creationDate =creationDate
        )
        viewModelScope.launch {
            try{
                reponseLigneDeVieRepository.insertResponseLigneDevie(response)

            }catch (e: Exception){
                Log.d("TAG", "addElement: insertion failed . Error message : ${e.message}")
            }
        }
    }

    val uiState=mutableStateOf(ScreenState())
init{
    getAllElements()
    getPassedElements()
    getPresentElements()
    getResponses()
}
    private val _allElement = MutableStateFlow<List<Element>>(emptyList())
    val allElement: StateFlow<List<Element>> = _allElement.asStateFlow()
    fun getAllElements(){
        viewModelScope.launch {
            repository.getElements().collect { listElement ->
                _allElement.value=listElement
            }
        }
    }

    private val _allPassedElement = MutableStateFlow<List<Element>>(emptyList())
    val allPassedElement: StateFlow<List<Element>> = _allPassedElement.asStateFlow()
    fun getPassedElements(){
        viewModelScope.launch {
            repository.getPassedElements().collect { listPassedElement->
                val sortedList = listPassedElement.sortedWith(
                    compareBy<Element> { it.startYear }.thenBy { it.endYear }
                )
                _allPassedElement.value = sortedList
                //_allPassedElement.value=listPassedElement
            }
        }
    }

    private val _allPresentElement = MutableStateFlow<List<Element>>(emptyList())
    val allPresentElement: StateFlow<List<Element>> = _allPresentElement.asStateFlow()
    fun getPresentElements(){
        viewModelScope.launch {
            repository.getPresentElements().collect { listPresentElement->
                val sortedList = listPresentElement.sortedBy { it.inProgressYear }
                _allPresentElement.value=sortedList
            }
        }
    }

    private  val _LigneDeVieResponse=MutableStateFlow<List<ReponseQuestionLigneDeVie>>(emptyList())
    val allResponse: StateFlow<List<ReponseQuestionLigneDeVie>> = _LigneDeVieResponse.asStateFlow()
    fun getResponses(){
        viewModelScope.launch {
            reponseLigneDeVieRepository.getResponse().collect { responses->
                _LigneDeVieResponse.value=responses
            }
        }
    }

    fun getElementById(id: Int, callback: (Element?) -> Unit) {
        viewModelScope.launch {
            repository.getElementById(id).collect { element ->
                callback(element)
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is org.ticanalyse.projetdevie.utils.Result.Success -> {
                    _currentUser.value = result.data
                }
                is org.ticanalyse.projetdevie.utils.Result.Error -> {
                    _currentUser.value = null
                }

                is org.ticanalyse.projetdevie.utils.Result.Loading<*> -> true
            }
        }
    }

    fun setCurrentUser(user: User) {
        viewModelScope.launch {
            when (val result = setCurrentUserUseCase(user)) {
                is org.ticanalyse.projetdevie.utils.Result.Success -> {
                    _currentUser.value = result.data
                }
                is org.ticanalyse.projetdevie.utils.Result.Error -> {
                    Timber.e("Erreur lors de la sauvegarde de l'utilisateur: ${result.message}")
                }

                is Result.Loading<*> -> true
            }
        }
    }
}