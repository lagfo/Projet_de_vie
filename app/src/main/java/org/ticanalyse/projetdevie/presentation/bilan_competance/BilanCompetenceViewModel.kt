package org.ticanalyse.projetdevie.presentation.bilan_competance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.domain.usecase.skill.SkillUseCases
import org.ticanalyse.projetdevie.domain.usecase.user.GetCurrentUserUseCase
import org.ticanalyse.projetdevie.domain.usecase.user.SetCurrentUserUseCase
import org.ticanalyse.projetdevie.utils.Result
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BilanCompetenceViewModel @Inject constructor(
    private val skillUseCases: SkillUseCases,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val setCurrentUserUseCase: SetCurrentUserUseCase
): ViewModel(){

    private val _skillsState = MutableStateFlow<List<String>?>(null)
    val skillsState = _skillsState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        getCurrentUser()
    }

    fun getSkill(onResult:(List<String>?) -> Unit){
        viewModelScope.launch {
            onResult(skillUseCases.getSkill()?.skills)
        }
    }

    fun getSkills() = viewModelScope.launch {
        _skillsState.value = skillUseCases.getSkill()?.skills
    }

    fun saveSkill(skills: Skill){
        viewModelScope.launch {
            skillUseCases.upsertSkill(skills)
        }

    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is Result.Success -> {
                    _currentUser.value = result.data
                }
                is Result.Error -> {
                    _currentUser.value = null
                }

                is Result.Loading<*> -> true
            }
        }
    }

    fun setCurrentUser(user: User) {
        viewModelScope.launch {
            when (val result = setCurrentUserUseCase(user)) {
                is Result.Success -> {
                    _currentUser.value = result.data
                }
                is Result.Error -> {
                    Timber.e("Erreur lors de la sauvegarde de l'utilisateur: ${result.message}")
                }

                is Result.Loading<*> -> true
            }
        }
    }

}