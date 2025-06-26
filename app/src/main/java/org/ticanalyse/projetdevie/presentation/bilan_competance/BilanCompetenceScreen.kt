package org.ticanalyse.projetdevie.presentation.bilan_competance

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Skill
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppSkillCardIcon
import org.ticanalyse.projetdevie.presentation.common.AppSkillGrid
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.Txt
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.common.skills
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3

@Composable
fun BilanCompetanceScreen(onNavigate: () -> Unit) {
    val ttsManager = appTTSManager()
    var selectedSkills by remember { mutableStateOf<List<String>>(emptyList()) }
    val defaultSkills = remember { mutableStateListOf<AppSkillCardIcon>().apply { addAll(skills) } }
    val viewModel= hiltViewModel<BilanCompetenceViewModel>()
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.getSkill { stored ->
            stored?.forEach { item ->
                val index = defaultSkills.indexOfFirst {
                    when (val txt = it.txt) {
                        is Txt.Res -> context.getString(txt.id).equals(item, ignoreCase = true)
                        is Txt.Raw -> txt.text.equals(item, ignoreCase = true)
                    }
                }

                if (index >= 0) {
                    defaultSkills[index] = defaultSkills[index].copy(badgeStatus = true)
                } else {
                    defaultSkills.add(
                        0,
                        AppSkillCardIcon(
                            txt = Txt.Raw(item),
                            strokeColor = R.color.primary_color,
                            paint = R.drawable.communication,
                            badgeStatus = true
                        )
                    )
                }
            }
            selectedSkills = stored ?: emptyList()
        }
    }

    LaunchedEffect(selectedSkills) {
        viewModel.saveSkill(Skill(skills = selectedSkills))
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.9f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AppText(
                        text = stringResource(id = R.string.competance_title),
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Normal,
                        color = colorResource(id = R.color.text),
                        fontSize = 12.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        ttsManager = ttsManager
                    )
                    Spacer(modifier = Modifier.height(MediumPadding1))

                    AppSkillGrid(icons = defaultSkills, column = 2,
                        selectedIcons = selectedSkills,
                        onSkillClick = {skill ->
                            val skillName = when (val txt = skill.txt) {
                                is Txt.Res -> context.getString(txt.id)
                                is Txt.Raw -> txt.text
                            }

                            selectedSkills = if (selectedSkills.contains(skillName)) {
                                skill.badgeStatus = false
                                selectedSkills - skillName
                            } else {
                                skill.badgeStatus = true
                                selectedSkills + skillName
                                selectedSkills + "Test"
                            }

                        }
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1.5f))

                AppButton(text = "Lien avec la vie réelle", onClick = onNavigate)

                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 5.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Black,
                        shadowElevation = 4.dp
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Ajouter",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }



        }

    }
}