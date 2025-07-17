package org.ticanalyse.projetdevie.presentation.planification_de_projet

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
import org.ticanalyse.projetdevie.domain.model.PlanAction
import org.ticanalyse.projetdevie.domain.model.ProjectInfo
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie
import org.ticanalyse.projetdevie.domain.repository.PlanificationProjetRepository.PlanificationProjetRepository
import org.ticanalyse.projetdevie.domain.repository.lienVieReelRepository.LienVieReelRepository
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.LigneDeVieRepository
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.ReponseQuestionLigneDeVieRepository
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.ResponseLigneDeVieElementRepository
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
class PlanificationViewModel @Inject constructor(
    private val repository: PlanificationProjetRepository,
): ViewModel() {

    private val _upsertSuccess = MutableStateFlow(false)
    val upsertSuccess: StateFlow<Boolean> = _upsertSuccess.asStateFlow()
    fun resetUpsertStatus(){
        _upsertSuccess.value=false
    }
    fun addProjectInfo(
        projectIdee:String,
        motivation:String,
        competenceDisponible:List<String>?,
        competenceNonDisponible:List<String>?,
        ressourceDisponible: String,
        ressourceNonDisponible:String
        ){

        val projectInfo= ProjectInfo(
            projetIdee = projectIdee,
            motivation = motivation,
            competenceDisponible = competenceDisponible,
            competenceNonDisponible = competenceNonDisponible,
            ressourceDisponible = ressourceDisponible,
            ressourceNonDispnible = ressourceNonDisponible
        )
        viewModelScope.launch {
            try{
                repository.insertProjectInfo(projectInfo)
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
    getPlanificationLineLine()
    getPlanActionLines()
}
    private val _allElement = MutableStateFlow<List<ProjectInfo>>(emptyList())
    val planificationInfo: StateFlow<List<ProjectInfo>> = _allElement.asStateFlow()
    fun getPlanificationLineLine(){
        viewModelScope.launch {
            repository.getProjectInfo().collect { listElement ->
                _allElement.value=listElement
            }
        }
    }

    //Plan action

    fun addPLanAction(
        planAction: PlanAction
    ){
        viewModelScope.launch {
            try{
                repository.insertPlanAction(planAction)
                _upsertSuccess.value=true
                Timber.tag("TAG").d("addElement: insertion succeed and status is $upsertSuccess")

            }catch (e: Exception){
                _upsertSuccess.value=false
                Timber.tag("TAG").d("addElement: insertion failed . Error message : ${e.message}")
            }
        }
    }


    private val _allPlanActionElement = MutableStateFlow<List<PlanAction>>(emptyList())
    val planAction: StateFlow<List<PlanAction>> = _allPlanActionElement.asStateFlow()
    fun getPlanActionLines(){
        viewModelScope.launch {
            repository.getPlanAction().collect { listElement ->
                _allPlanActionElement.value=listElement
            }
        }
    }


}


