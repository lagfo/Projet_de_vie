package org.ticanalyse.projetdevie.domain.usecase.skill

import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.domain.repository.SkillRepository

class GetSkill(
    private val skillRepository: SkillRepository
) {
    suspend operator fun invoke(): Skill? {
        return skillRepository.getSkills()
    }
}