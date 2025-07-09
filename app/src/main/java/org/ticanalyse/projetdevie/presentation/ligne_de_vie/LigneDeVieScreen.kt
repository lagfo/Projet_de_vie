package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.material3.ripple
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.AppTextInput
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.home.CustomItemLayout
import org.ticanalyse.projetdevie.presentation.home.SharpEllipse
import org.ticanalyse.projetdevie.presentation.home.Topic
import org.ticanalyse.projetdevie.presentation.introduction.IndicatorDots
import org.ticanalyse.projetdevie.presentation.introduction.PageIndicator
import org.ticanalyse.projetdevie.ui.theme.BelfastGrotesk
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding1
import org.ticanalyse.projetdevie.utils.Dimens.MediumPadding3
import org.ticanalyse.projetdevie.utils.ExoPlayer
import org.ticanalyse.projetdevie.utils.Global
import timber.log.Timber
import java.time.LocalDate
import kotlin.collections.lastIndex


data class ElementScolarite(val id:Int,val topicImage:Painter,val topicTitle:String)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LigneDeVieScreen(
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit
) {

    val onSubmit = rememberSaveable { mutableStateOf (false) }


    val items= listOf(
        ElementScolarite(1,painterResource(R.drawable.ecole_primaire),"École primaire"),
        ElementScolarite(2,painterResource(R.drawable.ecole_secondaire),"École secondaire (collège/lycée)"),
        ElementScolarite(3,painterResource(R.drawable.universite_ecole_superieur),"Université / Études supérieures"),
        ElementScolarite(4,painterResource(R.drawable.alphabetisation_langue_locale),"Alphabétisation en langue locale"),
        ElementScolarite(5,painterResource(R.drawable.ecole_coranique),"École coranique"),
        ElementScolarite(6,painterResource(R.drawable.ecole_formation_prof),"École de formation professionnelle"),
        ElementScolarite(7,painterResource(R.drawable.abandon_scolarite),"Abandon ou interruption de la scolarité"),
        ElementScolarite(8,painterResource(R.drawable.reprise_interruption_etude),"Reprise des études après interruption")
    )
    val items2= listOf(
        ElementScolarite(9,painterResource(R.drawable.premier_apprentissage),"Premier apprentissage / métier exercé"),
        ElementScolarite(10,painterResource(R.drawable.naissance_enfant),"Naissance d’un enfant"),
        ElementScolarite(11,painterResource(R.drawable.mariage),"Mariage"),
        ElementScolarite(12,painterResource(R.drawable.depart_foyer_familial),"Départ du foyer familial"),
        ElementScolarite(13,painterResource(R.drawable.depart_migration),"Déménagement / Changement de\n" +
                "village ou de ville"),
        ElementScolarite(14,painterResource(R.drawable.premier_emploi),"Premier emploi"),
        ElementScolarite(15,painterResource(R.drawable.projet),"Création de ton premier petit commerce\n" +
                "ou projet"),
        ElementScolarite(16,painterResource(R.drawable.deces),"Décès d’un proche marquant"),
        ElementScolarite(17,painterResource(R.drawable.depart_migration),"Départ en migration"),
        ElementScolarite(18,painterResource(R.drawable.retrouvaille),"Retrouvailles ou coupure avec des\n" +
                "membres de la famille"),
        ElementScolarite(19,painterResource(R.drawable.grande_decision_personnel),"Moment de grande décision personnelle")
    )


    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ElementScolarite?>(null) }
    val viewModel = hiltViewModel<LigneDeVieViewModel>()
    val element= Element()
    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    var reponse1 by remember { mutableStateOf("") }
    var reponse2 by remember { mutableStateOf("") }
    var isResponseValide by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
    var isButtonVisible by remember { mutableStateOf(false) }
    isButtonVisible=if(pagerState.currentPage==0) false else if(pagerState.currentPage==1) false else if(pagerState.currentPage==2) true else false
    val context = LocalContext.current
    val reponseQuestion by viewModel.allResponse.collectAsStateWithLifecycle()
    LaunchedEffect(isResponseValide,isClicked){
        if(!isResponseValide && isClicked){
            Toast.makeText(context, "Vous devriez obligatoirement repondre aux deux questions", Toast.LENGTH_SHORT).show()
            isClicked=false
        }
    }

    LaunchedEffect(reponseQuestion,isClicked,context) {

        if(reponseQuestion.isNotEmpty()){
            Log.d("TAG", "RecapitulatifScreen: $reponseQuestion ")
            reponse1=reponseQuestion[0].firstResponse
            reponse2=reponseQuestion[0].secondResponse
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
                .fillMaxSize().padding(top= MediumPadding3, bottom = MediumPadding1, start = 5.dp, end = 5.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            AppText(
                text = if(pagerState.currentPage==0)stringResource(R.string.instructionLigneDeVie) else if(pagerState.currentPage==1)stringResource(R.string.instructionLigneDeVie) else "" ,
                fontFamily = Roboto,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Normal,
                color = colorResource(R.color.primary_color),
                fontSize = 12.sp,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                ttsManager = ttsManager,
                isDefineMaxLine = false,
                isTextAlignCenter = true
            )

            AppText(
                text = if(pagerState.currentPage==0)"Éléments de Scolarité" else if(pagerState.currentPage==1) "Autres Événements Importants de la Vie" else "Questions",
                fontFamily = Roboto,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Normal,
                color = colorResource(R.color.primary_color),
                fontSize = 18.sp,
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

                Box(
                    modifier = Modifier.fillMaxHeight(.80f),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth(),

                        ) { currentPageIndex ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                modifier = Modifier.weight(0.2f),
                                onClick = {
                                    Timber.tag("TAG")
                                        .d("LigneDeVieScreen: current page is ${pagerState.currentPage} ")
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

                            when(currentPageIndex){
                                0->{
                                    LazyColumn(
                                        modifier = Modifier.weight(2f),
                                        contentPadding = PaddingValues(20.dp)
                                    ){
                                        itemsIndexed(items) { index,item->
                                            ItemScolariteLayout(item =item, onClick = {
                                                when(item.id){
                                                    1->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                    2->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                    3->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                    4->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                    5->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                    6->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                    7->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                    8->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                }
                                            })

                                            if(index<items.lastIndex){
                                                Spacer(modifier = Modifier.height(10.dp))
                                            }


                                        }

                                    }
                                }
                                1->{
                                    LazyColumn(
                                        modifier = Modifier.weight(2f),
                                        contentPadding = PaddingValues(20.dp)
                                    ){
                                        itemsIndexed(items2) { index,item->
                                            ItemScolariteLayout(item =item, onClick = {
                                                when(item.id){
                                                    9->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle
                                                    }
                                                    10->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }
                                                    11->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }
                                                    12->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }
                                                    13->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }
                                                    14->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }
                                                    15->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle


                                                    }
                                                    16->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }
                                                    17->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }
                                                    18->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }
                                                    19->{
                                                        selectedItem=item
                                                        showDialog=true
                                                        element.label=item.topicTitle

                                                    }

                                                }
                                            })

                                            if(index<items2.lastIndex){
                                                Spacer(modifier = Modifier.height(10.dp))
                                            }


                                        }

                                    }

                                }
                                2->{
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
                                                            text = "Qu'ai-je déjà réalisé ?",
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
                                                            text = "Qu'est-ce que je suis capable de faire ?",
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
                                else->error("Page inexistente")
                            }
                            IconButton(
                                modifier = Modifier.weight(0.2f),
                                onClick = {
                                    Log.d("TAG", "LigneDeVieScreen: current page is ${pagerState.currentPage} ")
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

                }


            }

            PageIndicator(
                pageCount = pagerState.pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if(isButtonVisible){
                AppButton (text = "Voir récapitulatif", onClick = {
                    if(Global.isValideResponse(reponse1,reponse2)){
                        isResponseValide=true
                        viewModel.addResponsesLigneDeVie(
                            id=1,
                            firstResponse = reponse1,
                            secondResponse = reponse2,
                            creationDate = LocalDate.now().toString()
                        )
                        onNavigate()
                    }else{
                        isResponseValide=false
                    }
                    isClicked=true
                })
            }
        }
        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha =if(pagerState.currentPage==0) 0.07f else if(pagerState.currentPage==1) 0.07f else 0.01f

        )

    }

    if(showDialog && selectedItem!=null){
        Log.d("TAG", "LigneDeVieScreen:Modal dialog is openned")
        ModalDialog(item=selectedItem!!, onDismiss ={
            showDialog=false
            selectedItem=null
        },
            viewModel = viewModel
        )

    }



}




@Composable
fun ItemScolariteLayout(item: ElementScolarite, onClick:()->Unit) {
    val ttsManager = appTTSManager()
    val interactionSource = remember { MutableInteractionSource() }
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(color = Color.White),
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ){
        Surface(
            modifier = Modifier.size(width = 36.dp, height = 36.dp),
            shape = CircleShape,
            color = Color.White                                      // ← solid background
        ) {
            Image(
                painter = item.topicImage,
                contentDescription = item.topicTitle,
                contentScale = ContentScale.Crop,
                modifier = Modifier.background(Color.White)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        AppText(
            text = item.topicTitle,
            fontFamily = Roboto,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Normal,
            color = Color.White,
            fontSize = 14.sp,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            ttsManager = ttsManager
        )
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            IndicatorDots(isSelected = index == currentPage, modifier = modifier)
        }
    }

}


@Composable
@Preview(showBackground = true)
fun LigneDeViePreview(modifier: Modifier = Modifier) {
    LigneDeVieScreen(onNavigate = {})
}