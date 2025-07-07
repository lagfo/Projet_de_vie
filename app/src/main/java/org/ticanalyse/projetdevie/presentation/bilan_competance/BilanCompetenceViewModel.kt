package org.ticanalyse.projetdevie.presentation.bilan_competance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.domain.usecase.skill.SkillUseCases
import javax.inject.Inject

@HiltViewModel
class BilanCompetenceViewModel @Inject constructor(
    private val skillUseCases: SkillUseCases
): ViewModel(){

    private val _skillsState = MutableStateFlow<List<String>?>(null)
    val skillsState = _skillsState.asStateFlow()

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

}