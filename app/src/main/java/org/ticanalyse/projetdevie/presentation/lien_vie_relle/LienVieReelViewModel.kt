package org.ticanalyse.projetdevie.presentation.lien_vie_relle

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
import org.ticanalyse.projetdevie.domain.model.LienVieReel
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.repository.lienVieReelRepository.LienVieReelRepository
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
class LienVieReelViewModel @Inject constructor(
    private val repository: LienVieReelRepository,
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
    fun addLienDeVieReelLine(
        firstResponse:String,
        secondResponse:String,
        thirdResponse:String,
        creationDate:String){

        val lienVieReelLigne= LienVieReel(
            firstResponse = firstResponse,
            secondResponse = secondResponse,
            thirdResponse =thirdResponse,
            creationDate = creationDate
        )
        viewModelScope.launch {
            try{
                repository.insertLienVieReel(lienVieReelLigne)
                _upsertSuccess.value=true
                Timber.tag("TAG").d("addElement: insertion succeed and status is $upsertSuccess")

            }catch (e: Exception){
                _upsertSuccess.value=false
                Timber.tag("TAG").d("addElement: insertion failed . Error message : ${e.message}")
            }
        }
    }

    val uiState=mutableStateOf(ScreenState())
init{
    getLienDeVieReelLine()
}
    private val _allElement = MutableStateFlow<List<LienVieReel>>(emptyList())
    val allElement: StateFlow<List<LienVieReel>> = _allElement.asStateFlow()
    fun getLienDeVieReelLine(){
        viewModelScope.launch {
            repository.getLineLienVieReel().collect { listElement ->
                _allElement.value=listElement
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