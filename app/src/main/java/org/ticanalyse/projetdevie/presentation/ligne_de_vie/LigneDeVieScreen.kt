package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.introduction.IndicatorDots
import org.ticanalyse.projetdevie.presentation.introduction.PageIndicator
import org.ticanalyse.projetdevie.ui.theme.Roboto
import timber.log.Timber


data class ElementScolarite(val id:Int,val topicImage:Painter,val topicTitle:String)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LigneDeVieScreen(modifier: Modifier = Modifier) {

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
        ElementScolarite(13,painterResource(R.drawable.ecole_coranique),"Déménagement / Changement de\n" +
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
    val viewModel= hiltViewModel<LigneDeVieViewModel>()
    val element= Element()
    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    var reponse1 by remember { mutableStateOf("") }
    var reponse2 by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var title by remember { mutableStateOf("") }

//            Box(
//                modifier= Modifier.fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ) {
//                AppShape(
//                    cornerRadius = 0.dp,
//                    arcHeight = (-20).dp,
//                    modifier = Modifier.fillMaxHeight(.06f)
//                )
//                Text(
//                    text="LIGNE DE VIE",
//                    fontFamily = Roboto,
//                    fontSize = 24.sp,
//                    color=Color.White,
//                    fontWeight = FontWeight.Bold
//                )
//
//            }



            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                text = if(pagerState.currentPage==0)stringResource(R.string.instructionLigneDeVie) else if(pagerState.currentPage==1)stringResource(R.string.instructionLigneDeVie) else "" ,
                maxLines = 5,
                textAlign = TextAlign.Center,
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,

                )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text =if(pagerState.currentPage==0)"Éléments de Scolarité" else if(pagerState.currentPage==1) "Autres Événements Importants de la Vie" else "Bilan",
                textAlign = TextAlign.Center,
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                color = colorResource(R.color.primary_color)
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
                                                        Text(
                                                            text ="Qu'ai-je déjà réalisé ?",
                                                            color = Color.White
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
                                                        Text(
                                                            text ="Qu'est-ce que je suis capable de faire ?",
                                                            color = Color.White
                                                        )
                                                        AppInputFieldMultiLine(
                                                            value =reponse2,
                                                            onValueChange = {
                                                                reponse2=it
                                                            },
                                                            label ="",
                                                            ttsManager =ttsManager,
                                                            sttManager =sttManager,
                                                            onSubmit=onSubmit.value
                                                        )

                                                    }

                                                }

                                            }
                                        }
                                    }
                                }
                                else->error("Page inexistente")
                            }
                            if(showDialog && selectedItem!=null){
                                Timber.tag("TAG").d("LigneDeVieScreen:Modal dialog is openned")
                                ModalDialog(item=selectedItem!!, onDismiss ={
                                    showDialog=false
                                    selectedItem=null
                                },
                                    viewModel = viewModel
                                )

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
            AppButton (text = "Voir bilan", onClick = {
            })
        }
        Image(
            painter = painterResource(id = R.drawable.bg_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alpha =if(pagerState.currentPage==0) 0.07f else if(pagerState.currentPage==1) 0.07f else 0.01f

        )

    }


}




@Composable
fun ItemScolariteLayout(item: ElementScolarite, onClick:()->Unit) {
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
            modifier = Modifier.size(width = 35.dp, height = 35.dp),
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
        Text(
            text=item.topicTitle,
            fontFamily = Roboto,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            color =Color.White,
            style = TextStyle(fontSize =20.sp)
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
    LigneDeVieScreen()
}