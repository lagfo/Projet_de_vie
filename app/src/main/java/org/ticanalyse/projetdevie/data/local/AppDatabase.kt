package org.ticanalyse.projetdevie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ticanalyse.projetdevie.data.local.dao.AppDao
import org.ticanalyse.projetdevie.data.local.dao.LienVieReelDao
import org.ticanalyse.projetdevie.data.local.dao.LigneDeVieDao
import org.ticanalyse.projetdevie.data.local.dao.MonReseauDao
import org.ticanalyse.projetdevie.data.local.dao.PlanActionDao
import org.ticanalyse.projetdevie.data.local.dao.PlanificationProjetDao
import org.ticanalyse.projetdevie.data.local.dao.ReponseQuestionLigneDeVieDao
import org.ticanalyse.projetdevie.data.local.dao.SkillsDao
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.LienVieReel
import org.ticanalyse.projetdevie.domain.model.MonReseau
import org.ticanalyse.projetdevie.domain.model.PlanAction
import org.ticanalyse.projetdevie.domain.model.ProjectInfo
import org.ticanalyse.projetdevie.domain.model.ReponseQuestionLigneDeVie
import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.domain.model.User

@Database(
    entities = [User::class,MonReseau::class, Element::class,ReponseQuestionLigneDeVie::class, Skill::class, LienVieReel::class, ProjectInfo::class, PlanAction::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val appDao: AppDao
    abstract val monReseauDao: MonReseauDao
    abstract val ligneDeVieDao: LigneDeVieDao
    abstract val reponseQuestionLigneDeVieDao: ReponseQuestionLigneDeVieDao
    abstract val skillsDao: SkillsDao
    abstract val lienVieReelDao: LienVieReelDao
    abstract val planificationProjetDao: PlanificationProjetDao
    abstract val planActionDao: PlanActionDao

}