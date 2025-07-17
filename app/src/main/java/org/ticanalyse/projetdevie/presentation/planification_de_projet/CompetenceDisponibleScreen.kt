package org.ticanalyse.projetdevie.presentation.planification_de_projet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.ProjectInfo
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetenceViewModel
import org.ticanalyse.projetdevie.presentation.common.AppSkillCardIcon
import org.ticanalyse.projetdevie.presentation.common.AppSkillGrid
import org.ticanalyse.projetdevie.presentation.common.Txt
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.common.skills
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3

@Composable
fun CompetenceDisponibleScreen() {
    val ttsManager = appTTSManager()
    val context = LocalContext.current
    val viewModel = hiltViewModel<BilanCompetenceViewModel>()

    var selectedSkills by remember { mutableStateOf<List<String>>(emptyList()) }

    val defaultSkills = remember { mutableStateListOf<AppSkillCardIcon>().apply { addAll(skills) } }



    LaunchedEffect(Unit) {
        viewModel.getSkill { storedSkills ->

            //Store list of  competences
            PlanificationProjet.projectInfo.competenceDisponible=storedSkills
            storedSkills?.let { stored ->

                val storedLower = stored.map(String::lowercase).toSet()

                fun getSkillName(skill: AppSkillCardIcon): String = when (val txt = skill.txt) {
                    is Txt.Res -> context.getString(txt.id)
                    is Txt.Raw -> txt.text
                }

                defaultSkills.retainAll { skill ->
                    storedLower.contains(getSkillName(skill).lowercase())
                }

                defaultSkills.replaceAll { skill ->
                    skill.copy(badgeStatus = true)
                }

                stored.filter { skillName ->
                    defaultSkills.none { skill ->
                        getSkillName(skill).equals(skillName, ignoreCase = true)
                    }
                }.forEach { skillName ->
                    defaultSkills.add(
                        AppSkillCardIcon(
                            txt = Txt.Raw(skillName),
                            strokeColor = R.color.primary_color,
                            paint = R.drawable.default_competence,
                            badgeStatus = true
                        )
                    )
                }

                selectedSkills = stored
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
                        onSkillClick = {}
                    )
                }
            }

//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 4.dp),
//                horizontalArrangement = Arrangement.Absolute.Right,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//               // AppButton(text = "Retour", onClick = { navController.navigateUp() })
//               // Spacer(modifier = Modifier.width(24.dp))
//                //Navigue sur la page pour ajouter des compétences
//                AppButtonCustomized(text = "Ajouter une compétence", onClick = { onNavigate("planification_projet") })
//            }

        }
    }


}