package org.ticanalyse.projetdevie.presentation.bilan_competance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.domain.usecase.skill.SkillUseCases
import javax.inject.Inject

@HiltViewModel
class BilanCompetenceViewModel @Inject constructor(
    private val skillUseCases: SkillUseCases
): ViewModel(){

    fun getSkill(onResult:(List<String>?) -> Unit){
        viewModelScope.launch {
            onResult(skillUseCases.getSkill()?.skills)
        }
    }

    fun saveSkill(skills: Skill){
        viewModelScope.launch {
            skillUseCases.upsertSkill(skills)
        }

    }

}