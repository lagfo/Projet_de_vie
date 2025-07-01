package org.ticanalyse.projetdevie.domain.usecase.skill

import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.domain.repository.SkillRepository

class UpsertSkill (
    private val skillRepository: SkillRepository
) {
    suspend operator fun invoke(skills: Skill){
        skillRepository.upsertSkills(skills)
    }
}