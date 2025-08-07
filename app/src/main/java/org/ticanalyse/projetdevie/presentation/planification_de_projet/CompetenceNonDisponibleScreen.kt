package org.ticanalyse.projetdevie.presentation.planification_de_projet

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.ProjectInfo
import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetenceViewModel
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppSkillCardIcon
import org.ticanalyse.projetdevie.presentation.common.AppSkillGrid
import org.ticanalyse.projetdevie.presentation.common.AppSkillModal
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.Txt
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.common.skills
import org.ticanalyse.projetdevie.presentation.nvgraph.BilanCompetenceResumeRoute
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import java.time.LocalDate


@Composable
fun CompetenceNonDisponibleScreen() {
    val ttsManager = appTTSManager()
    val context = LocalContext.current
    val viewModel = hiltViewModel<BilanCompetenceViewModel>()
    val viewModelPlan=hiltViewModel<PlanificationViewModel>()
    val planificationViewModel=hiltViewModel<PlanificationViewModel>()
    var selectedSkills by remember { mutableStateOf<List<String>>(emptyList()) }
    var navigateToBilan by remember {  mutableStateOf(false) }


    val defaultSkills = remember { mutableStateListOf<AppSkillCardIcon>().apply { addAll(skills) } }

    var showBottomSheet by remember { mutableStateOf(false) }



    fun syncBadges() {
        defaultSkills.replaceAll { skill ->
            val skillName = when (val txt = skill.txt) {
                is Txt.Res -> context.getString(txt.id)
                is Txt.Raw -> txt.text
            }
            skill.copy(badgeStatus = selectedSkills.any { it.equals(skillName, ignoreCase = true) })
        }
    }

    LaunchedEffect(Unit) {
        PlanificationProjet.projectInfo.competenceNonDisponible?.let {
            val storedLower = it.map(String::lowercase)
            defaultSkills.replaceAll { skill ->
                val skillName = when (val txt = skill.txt) {
                    is Txt.Res -> context.getString(txt.id)
                    is Txt.Raw -> txt.text
                }
                skill.copy(badgeStatus = storedLower.contains(skillName.lowercase()))
            }
            it.filter { skillName ->
                defaultSkills.none { skill ->
                    val name = when (val txt = skill.txt) {
                        is Txt.Res -> context.getString(txt.id)
                        is Txt.Raw -> txt.text
                    }
                    name.equals(skillName, ignoreCase = true)
                }
            }.forEach { skillName ->
                defaultSkills.add(
                    0,
                    AppSkillCardIcon(
                        txt = Txt.Raw(skillName),
                        strokeColor = R.color.primary_color,
                        paint = R.drawable.default_competence,
                        badgeStatus =true
                    )
                )
            }
            selectedSkills = it
        }
    }

    LaunchedEffect(selectedSkills) {
        syncBadges()
        //viewModel.saveSkill(Skill(skills = selectedSkills))
        if(selectedSkills.isNotEmpty()){
            PlanificationProjet.projectInfo.competenceNonDisponible=selectedSkills

//            PlanificationProjet.projectInfo.competenceNonDisponible!!.forEach {
//                skillName->
//                defaultSkills.add(
//                    0,
//                    AppSkillCardIcon(
//                        txt = Txt.Raw(skillName),
//                        strokeColor = R.color.primary_color,
//                        paint = R.drawable.default_competence,
//                        badgeStatus =true
//                    )
//                )
//            }
        }
        navigateToBilan = selectedSkills.isNotEmpty()
//        viewModelPlan.addProjectInfo(
//            projectIdee = PlanificationProjet.projectInfo.projetIdee,
//            motivation= PlanificationProjet.projectInfo.motivation,
//            competenceDisponible = PlanificationProjet.projectInfo.competenceDisponible,
//            competenceNonDisponible = PlanificationProjet.projectInfo.competenceNonDisponible,
//            ressourceDisponible = PlanificationProjet.projectInfo.ressourceDisponible,
//            ressourceNonDisponible = PlanificationProjet.projectInfo.ressourceNonDispnible,
//            creationDate = LocalDate.now().toString()
//        )

    }

    fun onAddSkills(newSkills: List<String>) {
        val skillsToAdd = newSkills.filter { newSkill ->
            val exists = selectedSkills.any { it.equals(newSkill, ignoreCase = true) }
            if (exists) {
                Toast.makeText(context, "La compétence \"$newSkill\" existe déjà", Toast.LENGTH_SHORT).show()
                false
            } else{
                Toast.makeText(context, "Compétence non-disponibles(s) ou à renforcer ajoutée(s)", Toast.LENGTH_SHORT).show()
                true
            }
        }

        if (skillsToAdd.isEmpty()) return

        selectedSkills = selectedSkills + skillsToAdd

        skillsToAdd.forEach { skillName ->
            val index = defaultSkills.indexOfFirst { skill ->
                val skillNameInList = when (val txt = skill.txt) {
                    is Txt.Res -> context.getString(txt.id)
                    is Txt.Raw -> txt.text
                }
                skillNameInList.equals(skillName, ignoreCase = true)
            }
            if (index == -1) {
                defaultSkills.add(
                    0,
                    AppSkillCardIcon(
                        txt = Txt.Raw(skillName),
                        strokeColor = R.color.primary_color,
                        paint = R.drawable.default_competence,
                        badgeStatus = true
                    )
                )
            } else {
                defaultSkills[index] = defaultSkills[index].copy(badgeStatus = true)
            }
        }

    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha = 0.07f
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = MediumPadding3, bottom = MediumPadding1),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.9f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppSkillGrid(
                        icons = defaultSkills,
                        column = 2,
                        selectedIcons = selectedSkills,
                        onSkillClick = { skill ->
                            val skillName = when (val txt = skill.txt) {
                                is Txt.Res -> context.getString(txt.id)
                                is Txt.Raw -> txt.text
                            }
                            selectedSkills = if (selectedSkills.any { it.equals(skillName, ignoreCase = true) }) {
                                selectedSkills.filterNot { it.equals(skillName, ignoreCase = true) }
                            } else {
                                ttsManager.speak(skillName)
                                selectedSkills + skillName
                            }
                        }
                    )
                }

                FloatingActionButton(
                    modifier = Modifier.size(48.dp).align(Alignment.BottomEnd).offset(((-10).dp),
                        (60).dp
                    ),
                    onClick = { showBottomSheet = true },
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Ajouter",
                    )
                }

            }


        }
    }

    AppSkillModal(
        showBottomSheet = showBottomSheet,
        onDismissRequest = { showBottomSheet = false },
        onAddSkills = ::onAddSkills
    )
}