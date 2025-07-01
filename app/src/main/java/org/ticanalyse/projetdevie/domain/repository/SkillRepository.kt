package org.ticanalyse.projetdevie.domain.repository

import org.ticanalyse.projetdevie.domain.model.Skill

interface SkillRepository {
    suspend fun upsertSkills(skill: Skill)
    suspend fun getSkills(): Skill?
}