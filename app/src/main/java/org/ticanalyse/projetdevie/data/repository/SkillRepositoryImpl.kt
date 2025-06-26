package org.ticanalyse.projetdevie.data.repository

import org.ticanalyse.projetdevie.data.local.dao.SkillsDao
import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.domain.repository.SkillRepository

class SkillRepositoryImpl(
    private val skillsDao: SkillsDao
) :SkillRepository{
    override suspend fun upsertSkills(skill: Skill) {
        skillsDao.upsert(skill)
    }

    override suspend fun getSkills(): Skill? {
        return skillsDao.getSelectedSkills()
    }
}