package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.LigneDeVieRepository
import org.ticanalyse.projetdevie.domain.repository.ligneDeVieRepository.ResponseLigneDeVieElementRepository
import org.ticanalyse.projetdevie.domain.usecase.user.GetCurrentUserUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.SetCurrentUserUseCase

enum class DISPLAY_TYPE {
    ALL,
    IN_PROGRESS,
    PASSED,
    ON_GOING
}

data class ScreenState(
    val displayType: DISPLAY_TYPE = DISPLAY_TYPE.ALL,
    val currentList: List<Element> = emptyList(),
)

@HiltViewModel
class LigneDeVieViewModel @Inject constructor(
    private val repository: LigneDeVieRepository,
    private val reponseLigneDeVieRepository: ResponseLigneDeVieElementRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val setCurrentUserUseCase: SetCurrentUserUseCase
): ViewModel() {

    // CORRECTION 1: Déplacer toutes les déclarations de StateFlow avant l'init
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _upsertSuccess = MutableStateFlow(false)
    val upsertSuccess: StateFlow<Boolean> = _upsertSuccess.asStateFlow()

    private val _allElement = MutableStateFlow<List<Element>>(emptyList())
    val allElement: StateFlow<List<Element>> = _allElement.asStateFlow()

    private val _allPassedElement = MutableStateFlow<List<Element>>(emptyList())
    val allPassedElement: StateFlow<List<Element>> = _allPassedElement.asStateFlow()

    private val _allPresentElement = MutableStateFlow<List<Element>>(emptyList())
    val allPresentElement: StateFlow<List<Element>> = _allPresentElement.asStateFlow()

    private val _ligneDeVieResponse = MutableStateFlow<List<ReponseQuestionLigneDeVie>>(emptyList())
    val allResponse: StateFlow<List<ReponseQuestionLigneDeVie>> = _ligneDeVieResponse.asStateFlow()

    // CORRECTION 2: Utiliser MutableStateFlow au lieu de mutableStateOf pour la cohérence
    private val _uiState = MutableStateFlow(ScreenState())
    val uiState: StateFlow<ScreenState> = _uiState.asStateFlow()

    // CORRECTION 3: Job pour gérer les coroutines et éviter les fuites mémoire
    private var dataLoadingJob: Job? = null

    init {
        getCurrentUser()
        loadAllData()
    }

    // CORRECTION 4: Grouper toutes les opérations de chargement dans une fonction
    private fun loadAllData() {
        dataLoadingJob?.cancel() // Annuler les précédentes opérations si elles existent
        dataLoadingJob = viewModelScope.launch {
            try {
                // Lancer toutes les opérations de chargement en parallèle
                launch { getAllElements() }
                launch { getPassedElements() }
                launch { getPresentElements() }
                launch { getResponses() }
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "Erreur lors du chargement des données", e)
            }
        }
    }

    fun resetUpsertStatus() {
        _upsertSuccess.value = false
    }

    fun addElement(
        id: Int,
        label: String,
        startYear: Int,
        endYear: Int,
        inProgressYear: Int,
        duration: Int,
        labelDescription: String,
        status: Boolean,
        creationDate: String
    ) {
        val element = Element(
            id = id,
            label = label,
            startYear = startYear,
            endYear = endYear,
            inProgressYear = inProgressYear,
            duration = duration,
            labelDescription = labelDescription,
            status = status,
            creationDate = creationDate
        )

        viewModelScope.launch {
            try {
                repository.insertLigneDeVieElement(element)
                _upsertSuccess.value = true
                Log.d("LigneDeVieViewModel", "addElement: insertion réussie")
            } catch (e: Exception) {
                _upsertSuccess.value = false
                Log.e("LigneDeVieViewModel", "addElement: échec insertion", e)
            }
        }
    }

    fun addResponsesLigneDeVie(
        id: Int = 1,
        firstResponse: String,
        secondResponse: String,
        creationDate: String,
    ) {
        val response = ReponseQuestionLigneDeVie(
            id = id,
            firstResponse = firstResponse,
            secondResponse = secondResponse,
            creationDate = creationDate
        )

        viewModelScope.launch {
            try {
                reponseLigneDeVieRepository.insertResponseLigneDevie(response)
                Log.d("LigneDeVieViewModel", "addResponsesLigneDeVie: insertion réussie")
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "addResponsesLigneDeVie: échec insertion", e)
            }
        }
    }

    // CORRECTION 5: Ajouter des vérifications de sécurité et gestion d'erreurs
    private fun getAllElements() {
        viewModelScope.launch {
            try {
                repository.getElements().catch { e ->
                    Log.e("LigneDeVieViewModel", "Erreur lors de la récupération des éléments", e)
                }.collect { listElement ->
                    _allElement.value = listElement
                }
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "Erreur inattendue dans getAllElements", e)
            }
        }
    }

    private fun getPassedElements() {
        viewModelScope.launch {
            try {
                repository.getPassedElements().catch { e ->
                    Log.e("LigneDeVieViewModel", "Erreur lors de la récupération des éléments passés", e)
                }.collect { listPassedElement ->
                    val sortedList = listPassedElement.sortedWith(
                        compareBy<Element> { it.startYear }.thenBy { it.endYear }
                    )
                    _allPassedElement.value = sortedList
                }
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "Erreur inattendue dans getPassedElements", e)
            }
        }
    }

    private fun getPresentElements() {
        viewModelScope.launch {
            try {
                repository.getPresentElements().catch { e ->
                    Log.e("LigneDeVieViewModel", "Erreur lors de la récupération des éléments présents", e)
                }.collect { listPresentElement ->
                    val sortedList = listPresentElement.sortedBy { it.inProgressYear }
                    _allPresentElement.value = sortedList
                }
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "Erreur inattendue dans getPresentElements", e)
            }
        }
    }

    private fun getResponses() {
        viewModelScope.launch {
            try {
                reponseLigneDeVieRepository.getResponse().catch { e ->
                    Log.e("LigneDeVieViewModel", "Erreur lors de la récupération des réponses", e)
                }.collect { responses ->
                    _ligneDeVieResponse.value = responses
                }
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "Erreur inattendue dans getResponses", e)
            }
        }
    }

    fun getElementById(id: Int, callback: (Element?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.getElementById(id).catch { e ->
                    Log.e("LigneDeVieViewModel", "Erreur lors de la récupération de l'élément par ID", e)
                    callback(null)
                }.collect { element ->
                    callback(element)
                }
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "Erreur inattendue dans getElementById", e)
                callback(null)
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                when (val result = getCurrentUserUseCase()) {
                    is org.ticanalyse.projetdevie.utils.Result.Success -> {
                        _currentUser.value = result.data
                    }
                    is org.ticanalyse.projetdevie.utils.Result.Error -> {
                        _currentUser.value = null
                        Log.e("LigneDeVieViewModel", "Erreur getCurrentUser: ${result.message}")
                    }
                    is org.ticanalyse.projetdevie.utils.Result.Loading<*> -> {
                        // État de chargement, ne rien faire
                    }
                }
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "Erreur inattendue dans getCurrentUser", e)
                _currentUser.value = null
            }
        }
    }

    fun setCurrentUser(user: User) {
        viewModelScope.launch {
            try {
                when (val result = setCurrentUserUseCase(user)) {
                    is org.ticanalyse.projetdevie.utils.Result.Success -> {
                        _currentUser.value = result.data
                    }
                    is org.ticanalyse.projetdevie.utils.Result.Error -> {
                        Log.e("LigneDeVieViewModel", "Erreur setCurrentUser: ${result.message}")
                    }
                    is org.ticanalyse.projetdevie.utils.Result.Loading<*> -> {
                        // État de chargement, ne rien faire
                    }
                }
            } catch (e: Exception) {
                Log.e("LigneDeVieViewModel", "Erreur inattendue dans setCurrentUser", e)
            }
        }
    }

    // CORRECTION 6: Nettoyer les ressources
    override fun onCleared() {
        super.onCleared()
        dataLoadingJob?.cancel()
    }

}