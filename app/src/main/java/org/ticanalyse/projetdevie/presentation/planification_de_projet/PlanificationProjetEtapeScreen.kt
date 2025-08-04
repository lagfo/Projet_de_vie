package org.ticanalyse.projetdevie.presentation.planification_de_projet

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.ProjectInfo
import org.ticanalyse.projetdevie.presentation.bilan_competance.BilanCompetenceViewModel
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.introduction.PageIndicator
import org.ticanalyse.projetdevie.presentation.ligne_de_vie.LigneDeVieViewModel
import org.ticanalyse.projetdevie.presentation.nvgraph.PlanificationProjetRoute
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Global
import timber.log.Timber
import java.time.LocalDate

object PlanificationProjet{
    val projectInfo = ProjectInfo()
    var listOfSkill:List<String>?=null
}

@Composable
fun PlanificationProjetEtapeScreen(
    modifier: Modifier = Modifier,
    onNavigate:(String)->Unit
) {

    val pagerState = rememberPagerState(pageCount = { 6 })
    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    val scope = rememberCoroutineScope()
    var isButtonVisible by remember { mutableStateOf(false) }
    var isRegisterButtonVisible by remember { mutableStateOf(false) }
    val onSubmit = rememberSaveable { mutableStateOf (false) }
    isButtonVisible = pagerState.currentPage == 2
    isRegisterButtonVisible=if(pagerState.currentPage==0||pagerState.currentPage==1||pagerState.currentPage==4) true else false
    var isClicked by remember { mutableStateOf(false) }
    var isResponseValide by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var ideeProjet by rememberSaveable{ mutableStateOf("") }
    var motivation by rememberSaveable{ mutableStateOf("") }
    var reponse1 by rememberSaveable{ mutableStateOf("") }
    var reponse2 by rememberSaveable{ mutableStateOf("") }
    val ligneDeVieviewModel = hiltViewModel<LigneDeVieViewModel>()
    val viewModel=hiltViewModel<PlanificationViewModel>()
    val reponseQuestion by ligneDeVieviewModel.allResponse.collectAsStateWithLifecycle()
    val status by viewModel.upsertSuccess.collectAsStateWithLifecycle()
    val viewModelBilanCompetance = hiltViewModel<BilanCompetenceViewModel>()
    val planActions by viewModel.planAction.collectAsStateWithLifecycle()
    val planInfo by viewModel.planificationInfo.collectAsStateWithLifecycle()

    LaunchedEffect(status){
        if(status){
            when (pagerState.currentPage) {
                0 -> {
                    Toast.makeText(context, "Idée de projet enregistrée", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    Toast.makeText(context, "Modivation  enregistrée", Toast.LENGTH_SHORT).show()
                }
                4 -> {
                    Toast.makeText(context, "Ressources enregistrées", Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.resetUpsertStatus()
        }
    }

    LaunchedEffect(planInfo, pagerState.currentPage) { // Add currentPage as dependency
        if(planInfo.isNotEmpty()){
            Log.d("TAG", "PlanificationProjetEtapeScreen planInfo: $planInfo, currentPage: ${pagerState.currentPage}")
            when(pagerState.currentPage) {
                0 -> {
                    if(planInfo.first().projetIdee.isNotEmpty()){
                        ideeProjet = planInfo.first().projetIdee
                        Log.d("TAG", "PlanificationProjetEtapeScreen planInfo 1")
                    }
                }
                1 -> {
                    if(planInfo.first().motivation.isNotEmpty()){
                        motivation = planInfo.first().motivation
                        Log.d("TAG", "PlanificationProjetEtapeScreen planInfo 2")
                    }
                }
                4 -> {
                    if(planInfo.first().ressourceDisponible.isNotEmpty()){
                        reponse1 = planInfo.first().ressourceDisponible
                        Log.d("TAG", "PlanificationProjetEtapeScreen planInfo 3")
                    }
                    if(planInfo.first().ressourceNonDispnible.isNotEmpty()){
                        reponse2 = planInfo.first().ressourceNonDispnible
                        Log.d("TAG", "PlanificationProjetEtapeScreen planInfo 4")
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModelBilanCompetance.getSkill {
            skill->
            PlanificationProjet.listOfSkill=skill
        }
    }

    LaunchedEffect(ideeProjet,motivation,reponse1,reponse2) {
        if(ideeProjet.isNotEmpty()||ideeProjet.isNotBlank()){
            PlanificationProjet.projectInfo.projetIdee=ideeProjet
        }

        if(motivation.isNotEmpty()||motivation.isNotBlank()){
            PlanificationProjet.projectInfo.motivation=motivation
        }

        if(reponse1.isNotEmpty() || reponse1.isNotBlank()){
            PlanificationProjet.projectInfo.ressourceDisponible=reponse1
        }
        if(reponse2.isNotEmpty() || reponse2.isNotBlank()){
            PlanificationProjet.projectInfo.ressourceNonDispnible=reponse2
        }
    }
    LaunchedEffect(reponseQuestion,isClicked,context) {

        if(reponseQuestion.isNotEmpty()){
            Timber.tag("TAG").d("RecapitulatifScreen: $reponseQuestion ")
            reponse1=reponseQuestion[0].firstResponse
            reponse2=reponseQuestion[0].secondResponse
            PlanificationProjet.projectInfo.ressourceDisponible=reponse1
            PlanificationProjet.projectInfo.ressourceNonDispnible=reponse2
            Timber.tag("TAG")
                .d("RecapitulatifScreen: $reponseQuestion and project entity is ${PlanificationProjet.projectInfo} ")

        }
        if(!isResponseValide && isClicked){
            Toast.makeText(context, "Vous devriez obligatoirement repondre aux deux questions", Toast.LENGTH_SHORT).show()
            isClicked=false
        }

    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(35.dp))
            AppText(
                text = if(pagerState.currentPage==0)"Idée de projet" else if(pagerState.currentPage==1)"Mes motivations" else if(pagerState.currentPage==2) "Mes compétences disponibles" else if(pagerState.currentPage==3) "Mes compétences non-disponibles\n ou à renforcer" else if(pagerState.currentPage==4)"Mes ressources " else if(pagerState.currentPage==5) "Plan d'action" else "",
                fontFamily = Roboto,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Normal,
                color = colorResource(R.color.primary_color),
                fontSize = 21.sp,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                ttsManager = ttsManager,
                isTextAlignCenter = true
            )

            Box {
                AppShape(
                    cornerRadius = 45.dp,
                    arcHeight = 0.dp,
                    modifier = Modifier.fillMaxHeight(.85f),
                    bottomCornerRadius = 45.dp
                )


                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Box(
                        modifier = Modifier.fillMaxHeight(.80f),
                        contentAlignment = Alignment.Center
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),

                            ) { currentPageIndex ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    modifier = Modifier.weight(0.2f),
                                    onClick = {
                                        val prevPage = pagerState.currentPage - 1
                                        if (prevPage >= 0) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(prevPage)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(70.dp)
                                    )
                                }
                                //Setting pages
                                when(currentPageIndex) {
                                    0 -> {
                                        AppInputFieldMultiLine(
                                            value =ideeProjet,
                                            onValueChange = {
                                                ideeProjet=it
                                            },
                                            label ="Idée de projet",
                                            ttsManager =ttsManager,
                                            sttManager =sttManager
                                        )

                                    }
                                    1 -> {
                                        AppInputFieldMultiLine(
                                            value =motivation,
                                            onValueChange = {
                                                motivation=it
                                            },
                                            label ="Mes motivations",
                                            ttsManager =ttsManager,
                                            sttManager =sttManager
                                        )

                                    }
                                    2 -> {
                                        CompetenceDisponibleScreen()
                                    }
                                    3 -> {
                                        CompetenceNonDisponibleScreen()
                                    }
                                    4 -> {
                                        Column{
                                            Spacer(modifier = Modifier.height(25.dp))
                                            LazyColumn {
                                                item {

                                                    Box(
                                                        modifier= Modifier.weight(1f)
                                                    ) {
                                                        Column(
                                                            modifier = Modifier.padding(10.dp,0.dp),
                                                            horizontalAlignment = Alignment.CenterHorizontally

                                                        ) {
                                                            AppText(
                                                                text = "Mes ressourcees disponibles",
                                                                fontFamily = Roboto,
                                                                fontWeight = FontWeight.Black,
                                                                fontStyle = FontStyle.Normal,
                                                                color = Color.White,
                                                                fontSize = 12.sp,
                                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                                ttsManager = ttsManager
                                                            )

                                                            AppInputFieldMultiLine(
                                                                value =reponse1,
                                                                onValueChange = {
                                                                    reponse1=it
                                                                },
                                                                label ="",
                                                                ttsManager =ttsManager,
                                                                sttManager =sttManager,
                                                                onSubmit=onSubmit.value
                                                            )

                                                        }

                                                    }
                                                    Box(
                                                        modifier= Modifier.weight(1f)
                                                    ) {
                                                        Column(
                                                            modifier = Modifier.padding(10.dp,0.dp),
                                                            horizontalAlignment = Alignment.CenterHorizontally

                                                        ) {
                                                            AppText(
                                                                text = "Mes ressources à rechercher",
                                                                fontFamily = Roboto,
                                                                fontWeight = FontWeight.Black,
                                                                fontStyle = FontStyle.Normal,
                                                                color = Color.White,
                                                                fontSize = 12.sp,
                                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                                ttsManager = ttsManager
                                                            )
                                                            AppInputFieldMultiLine(
                                                                value =reponse2,
                                                                onValueChange = {
                                                                    reponse2=it
                                                                },
                                                                label ="",
                                                                ttsManager =ttsManager,
                                                                sttManager =sttManager
                                                            )

                                                        }

                                                    }

                                                }
                                            }

                                        }

                                    }
                                    5->{
                                        PlanActionScreen()
                                    }

                                }
                                IconButton(
                                    modifier = Modifier.weight(0.2f),
                                    onClick = {
                                        val nextPage = pagerState.currentPage + 1
                                        if (nextPage < pagerState.pageCount) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(nextPage)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(70.dp)
                                    )

                                }
                            }

                            //

                        }

                        if(isButtonVisible){
                            FloatingActionButton(
                                onClick = {
                                onNavigate("planification")
                            },
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.BottomEnd)
                                    .offset(((-10).dp), 0.dp),
                                containerColor =Color.Black,
                                shape = CircleShape
                            ){
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Ajouter",
                                    tint = Color.White
                                )

                            }

                        }

                    }

                }

            }

            PageIndicator(
                pageCount = pagerState.pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if(isRegisterButtonVisible){
                AppButton (text ="Enregistrer", onClick = {
                    if(pagerState.currentPage==0){
                        if(PlanificationProjet.projectInfo.projetIdee.isNotEmpty()&&PlanificationProjet.projectInfo.projetIdee.isNotBlank()){
                            viewModel.addProjectInfo(
                                projectIdee =PlanificationProjet.projectInfo.projetIdee,
                                motivation =PlanificationProjet.projectInfo.motivation,
                                competenceDisponible =PlanificationProjet.projectInfo.competenceDisponible,
                                competenceNonDisponible = PlanificationProjet.projectInfo.competenceNonDisponible,
                                ressourceDisponible = PlanificationProjet.projectInfo.ressourceDisponible,
                                ressourceNonDisponible =PlanificationProjet.projectInfo.ressourceNonDispnible,
                            )
                        }
                    }
                    if(pagerState.currentPage==1){
                        if(PlanificationProjet.projectInfo.motivation.isNotEmpty()&&PlanificationProjet.projectInfo.motivation.isNotBlank()){
                            viewModel.addProjectInfo(
                                projectIdee =PlanificationProjet.projectInfo.projetIdee,
                                motivation =PlanificationProjet.projectInfo.motivation,
                                competenceDisponible =PlanificationProjet.projectInfo.competenceDisponible,
                                competenceNonDisponible = PlanificationProjet.projectInfo.competenceNonDisponible,
                                ressourceDisponible = PlanificationProjet.projectInfo.ressourceDisponible,
                                ressourceNonDisponible =PlanificationProjet.projectInfo.ressourceNonDispnible,
                            )

                        }
                    }
                    if(pagerState.currentPage==4){
                        if(PlanificationProjet.projectInfo.ressourceDisponible.isNotEmpty()&&PlanificationProjet.projectInfo.ressourceDisponible.isNotBlank() && PlanificationProjet.projectInfo.ressourceNonDispnible.isNotEmpty()&&PlanificationProjet.projectInfo.ressourceNonDispnible.isNotBlank() ){
                            viewModel.addProjectInfo(
                                projectIdee =PlanificationProjet.projectInfo.projetIdee,
                                motivation =PlanificationProjet.projectInfo.motivation,
                                competenceDisponible =PlanificationProjet.projectInfo.competenceDisponible,
                                competenceNonDisponible = PlanificationProjet.projectInfo.competenceNonDisponible,
                                ressourceDisponible = PlanificationProjet.projectInfo.ressourceDisponible,
                                ressourceNonDisponible =PlanificationProjet.projectInfo.ressourceNonDispnible,
                            )

                        }
                    }
                })
            }


            if(pagerState.currentPage==5){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppButton("Voir tableau") {
                        viewModel.addProjectInfo(
                            projectIdee = PlanificationProjet.projectInfo.projetIdee,
                            motivation= PlanificationProjet.projectInfo.motivation,
                            competenceDisponible = PlanificationProjet.projectInfo.competenceDisponible,
                            competenceNonDisponible = PlanificationProjet.projectInfo.competenceNonDisponible,
                            ressourceDisponible = PlanificationProjet.projectInfo.ressourceDisponible,
                            ressourceNonDisponible = PlanificationProjet.projectInfo.ressourceNonDispnible,
                            creationDate = LocalDate.now().toString()
                        )
                        onNavigate("tableau")
                    }
                    AppButton("Télécharger pdf") {
                        viewModel.addProjectInfo(
                            projectIdee = PlanificationProjet.projectInfo.projetIdee,
                            motivation= PlanificationProjet.projectInfo.motivation,
                            competenceDisponible = PlanificationProjet.projectInfo.competenceDisponible,
                            competenceNonDisponible = PlanificationProjet.projectInfo.competenceNonDisponible,
                            ressourceDisponible = PlanificationProjet.projectInfo.ressourceDisponible,
                            ressourceNonDisponible = PlanificationProjet.projectInfo.ressourceNonDispnible,
                            creationDate = LocalDate.now().toString()
                        )
                        onNavigate("pdf")
                    }

                }
            }



        }

    }

}

@Composable
fun AppButtonAdd(
    text: String,
    enabled:Boolean=true,
    onClick: () -> Unit,
    modifier: Modifier
){

    Button(
        modifier = modifier,
        onClick = onClick,
        enabled=enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.secondary_color),
            contentColor = Color.White
        ),
        shape = CircleShape
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
    }

}
