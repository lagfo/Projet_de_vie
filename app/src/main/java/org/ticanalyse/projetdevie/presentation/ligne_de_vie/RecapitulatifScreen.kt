package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.introduction.PageIndicator
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Global
import java.time.LocalDate


@Composable
fun RecapitulatifScreen(
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit
) {

    val viewModel= hiltViewModel<LigneDeVieViewModel>()
    val uiState=viewModel.uiState
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    var isButtonVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Element?>(null) }
    isButtonVisible= pagerState.currentPage != 0
    val listOfElement by viewModel.allElement.collectAsStateWithLifecycle()
    val listOfPassedElement by viewModel.allPassedElement.collectAsStateWithLifecycle()
    val listOfPresentElement by viewModel.allPresentElement.collectAsStateWithLifecycle()
    val reponseQuestion by viewModel.allResponse.collectAsStateWithLifecycle()
    val ttsManager = appTTSManager()
    val sttManager = appSTTManager()
    var reponse1 by remember { mutableStateOf("") }
    var reponse2 by remember { mutableStateOf("") }
    val onSubmit = rememberSaveable { mutableStateOf (false) }
    isButtonVisible = pagerState.currentPage == 2
    var isClicked by remember { mutableStateOf(false) }
    var isResponseValide by remember { mutableStateOf(false) }
    val context = LocalContext.current



    LaunchedEffect(listOfElement,listOfPresentElement,listOfPassedElement,reponseQuestion,isClicked,isClicked) {
        if(reponseQuestion.isNotEmpty()){
            reponse1=reponseQuestion.first().firstResponse
            reponse2=reponseQuestion.first().secondResponse
        }else{
            Log.d("TAG", "RecapitulatifScreen:$reponseQuestion ")

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

            Spacer(modifier = Modifier.height(25.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text ="Récapitulatifs",
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


                Column {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(0.dp, 10.dp),
                        text =if(pagerState.currentPage==0)"Passé" else if(pagerState.currentPage==1)"Présent" else if(pagerState.currentPage==2) "Questions" else "",
                        textAlign = TextAlign.Center,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 19.sp,
                        color = Color.White
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
                                        if(listOfPassedElement.isNotEmpty()){
                                            LazyColumn(
                                                modifier = modifier,
                                                contentPadding = PaddingValues(20.dp)
                                            ) {
                                                itemsIndexed(listOfPassedElement) { index, item ->
                                                    CustomedElementLayout(
                                                        item = item,
                                                        onClick = {
                                                            when(item.id){
                                                                1->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                2->{
                                                                    selectedItem=item
                                                                    showDialog=true

                                                                }
                                                                3->{
                                                                    selectedItem=item
                                                                    showDialog=true

                                                                }
                                                                4->{
                                                                    selectedItem=item
                                                                    showDialog=true


                                                                }
                                                                5->{
                                                                    selectedItem=item
                                                                    showDialog=true

                                                                }
                                                                6->{
                                                                    selectedItem=item
                                                                    showDialog=true

                                                                }
                                                                7->{
                                                                    selectedItem=item
                                                                    showDialog=true

                                                                }
                                                                8->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }

                                                                9->{
                                                                    selectedItem=item
                                                                    showDialog=true

                                                                }
                                                                10->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                11->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                12->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                13->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                14->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                15->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                16->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                17->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                18->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }
                                                                19->{
                                                                    selectedItem=item
                                                                    showDialog=true
                                                                }


                                                            }

                                                        }
                                                    )

                                                    if(index <listOfPassedElement.lastIndex) {
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                    }
                                                }
                                            }

                                        }

                                    }
                                    1 -> {
                                        if(listOfPresentElement.isNotEmpty()){
                                            LazyColumn(
                                                modifier = modifier,
                                                contentPadding = PaddingValues(20.dp)
                                            ) {
                                                itemsIndexed(listOfPresentElement) { index2, item2 ->
                                                    CustomedElementLayout(
                                                        item = item2,
                                                        onClick = {
                                                            when(item2.id){

                                                                1->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                2->{
                                                                    selectedItem=item2
                                                                    showDialog=true

                                                                }
                                                                3->{
                                                                    selectedItem=item2
                                                                    showDialog=true

                                                                }
                                                                4->{
                                                                    selectedItem=item2
                                                                    showDialog=true


                                                                }
                                                                5->{
                                                                    selectedItem=item2
                                                                    showDialog=true

                                                                }
                                                                6->{
                                                                    selectedItem=item2
                                                                    showDialog=true

                                                                }
                                                                7->{
                                                                    selectedItem=item2
                                                                    showDialog=true

                                                                }
                                                                8->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }

                                                                9->{
                                                                    selectedItem=item2
                                                                    showDialog=true

                                                                }
                                                                10->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                11->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                12->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                13->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                14->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                15->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                16->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                17->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                18->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }
                                                                19->{
                                                                    selectedItem=item2
                                                                    showDialog=true
                                                                }

                                                            }

                                                        }
                                                    )

                                                    if(index2 <listOfPresentElement.lastIndex) {
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                    }
                                                }
                                            }

                                        }

                                    }
                                    2 -> {

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
                                                                onSubmit=onSubmit.value,
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
                                                            )

                                                        }

                                                    }

                                                }
                                            }

                                        }

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

                    }


                }

            }

            PageIndicator(
                pageCount = pagerState.pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if(isButtonVisible){
                AppButton (text = "Bilan des compétences", onClick = {
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

    }

    if(showDialog && selectedItem!=null){
        ModalDialogForEdit(item=selectedItem!!, onDismiss ={
            showDialog=false
            selectedItem=null
        },
            viewModel = viewModel
        )

    }


}

@Composable
fun CustomedElementLayout(item: Element, onClick:()->Unit) {
    if(!item.status){

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment =Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(width = 35.dp, height = 35.dp)
                                .align(Alignment.Center),
                            shape = CircleShape,
                            color = Color.White                                      // ← solid background
                        ) {
                            Image(
                                painter =when(item.id){
                                    1->painterResource(R.drawable.ecole_primaire)
                                    2->painterResource(R.drawable.ecole_secondaire)
                                    3->painterResource(R.drawable.universite_ecole_superieur)
                                    4->painterResource(R.drawable.alphabetisation_langue_locale)
                                    5->painterResource(R.drawable.ecole_coranique)
                                    6->painterResource(R.drawable.ecole_formation_prof)
                                    7->painterResource(R.drawable.abandon_scolarite)
                                    8->painterResource(R.drawable.reprise_interruption_etude)
                                    9->painterResource(R.drawable.premier_apprentissage)
                                    10->painterResource(R.drawable.naissance_enfant)
                                    11->painterResource(R.drawable.mariage)
                                    12->painterResource(R.drawable.depart_foyer_familial)
                                    13->painterResource(R.drawable.ecole_coranique)
                                    14->painterResource(R.drawable.premier_emploi)
                                    15->painterResource(R.drawable.projet)
                                    16->painterResource(R.drawable.deces)
                                    17->painterResource(R.drawable.depart_migration)
                                    18->painterResource(R.drawable.retrouvaille)
                                    19->painterResource(R.drawable.grande_decision_personnel)
                                    else -> {painterResource(R.drawable.ecole_primaire)}
                                },
                                contentDescription =item.label,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.background(Color.White)
                            )
                        }

                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick={
                                onClick
                            }
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(35.dp)
                                    .clickable(
                                        onClick = onClick
                                    ),
                                painter = painterResource(R.drawable.edit),
                                contentDescription = "edit",
                                contentScale = ContentScale.Crop
                            )
                        }


                    }

                    ////
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text =item.label,
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black,
                            maxLines =1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text ="Année de début:${item.startYear}",
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.Black,
                            maxLines =1
                        )
                        Text(
                            text ="Année de fin:${item.endYear}",
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.Black,
                            maxLines = 1
                        )
                    }

                    ////

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value =if(item.labelDescription.isNotEmpty()||item.labelDescription.isNotBlank()) item.labelDescription else "Pas de commentaire pour cet élément",
                            onValueChange = {},
                            enabled = false,
                            readOnly = true,
                            colors=OutlinedTextFieldDefaults.colors(
                                disabledTextColor= Color.Black,
                                disabledContainerColor = Color.White,
                                disabledBorderColor = colorResource(R.color.primary_color)
                            )
                        )
                    }

                }
            }

        }
    }else{
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment =Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(width = 35.dp, height = 35.dp)
                                .align(Alignment.Center),
                            shape = CircleShape,
                            color = Color.White                                      // ← solid background
                        ) {
                            Image(
                                painter =when(item.id){
                                    1->painterResource(R.drawable.ecole_primaire)
                                    2->painterResource(R.drawable.ecole_secondaire)
                                    3->painterResource(R.drawable.universite_ecole_superieur)
                                    4->painterResource(R.drawable.alphabetisation_langue_locale)
                                    5->painterResource(R.drawable.ecole_coranique)
                                    6->painterResource(R.drawable.ecole_formation_prof)
                                    7->painterResource(R.drawable.abandon_scolarite)
                                    8->painterResource(R.drawable.reprise_interruption_etude)
                                    9->painterResource(R.drawable.premier_apprentissage)
                                    10->painterResource(R.drawable.naissance_enfant)
                                    11->painterResource(R.drawable.mariage)
                                    12->painterResource(R.drawable.depart_foyer_familial)
                                    13->painterResource(R.drawable.ecole_coranique)
                                    14->painterResource(R.drawable.premier_emploi)
                                    15->painterResource(R.drawable.projet)
                                    16->painterResource(R.drawable.deces)
                                    17->painterResource(R.drawable.depart_migration)
                                    18->painterResource(R.drawable.retrouvaille)
                                    19->painterResource(R.drawable.grande_decision_personnel)
                                    else -> {painterResource(R.drawable.ecole_primaire)}
                                },
                                contentDescription =item.label,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.background(Color.White)
                            )
                        }

                        IconButton(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            onClick={
                                onClick
                            }
                        ) {
                            Image(
                                modifier = Modifier
                                    .size(35.dp)
                                    .clickable(
                                        onClick = onClick
                                    ),
                                painter = painterResource(R.drawable.edit),
                                contentDescription = "edit",
                                contentScale = ContentScale.Crop
                            )
                        }


                    }

                    ////
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text =item.label,
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.Black,
                            maxLines =1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text ="Année de début:${item.startYear}",
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.Black,
                            maxLines =1
                        )
                        Text(
                            text ="Année de fin:${item.endYear}",
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.Black,
                            maxLines = 1
                        )
                    }

                    ////

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value =if(item.labelDescription.isNotEmpty()||item.labelDescription.isNotBlank()) item.labelDescription else "Pas de commentaire pour cet élément",
                            onValueChange = {},
                            enabled = false,
                            readOnly = true,
                            colors=OutlinedTextFieldDefaults.colors(
                                disabledTextColor= Color.Black,
                                disabledContainerColor = Color.White,
                                disabledBorderColor = colorResource(R.color.primary_color)
                            )
                        )
                    }

                }
            }

        }

    }
}

@Composable
@Preview(showBackground = true)
fun CustomedElementLayoutPreview(modifier: Modifier = Modifier) {
    CustomedElementLayout(item =Element(id=1) ,onClick={})
}

//@Composable
//fun PageContent(
//    elements: List<Element>,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(
//        modifier = modifier,
//        contentPadding = PaddingValues(20.dp)
//    ) {
//        itemsIndexed(elements) { index, item ->
//            CustomedElementLayout(
//                item = item,
//                onClick = {
//                    when(item.id){
//                        1->{
//                            selectedItem=item
//                            showDialog=true
//                            item.label=item.topicTitle
//                        }
//                        2->{
//                            selectedItem=item
//                            showDialog=true
//                            element.label=item.topicTitle
//                        }
//                        3->{
//                            selectedItem=item
//                            showDialog=true
//                            element.label=item.topicTitle
//                        }
//                        4->{
//                            selectedItem=item
//                            showDialog=true
//                            element.label=item.topicTitle
//                        }
//                        5->{
//                            selectedItem=item
//                            showDialog=true
//                            element.label=item.topicTitle
//                        }
//                        6->{
//                            selectedItem=item
//                            showDialog=true
//                            element.label=item.topicTitle
//                        }
//                        7->{
//                            selectedItem=item
//                            showDialog=true
//                            element.label=item.topicTitle
//                        }
//                        8->{
//                            selectedItem=item
//                            showDialog=true
//                            element.label=item.topicTitle
//                        }
//                    }
//
//                }
//            )
//
//            if(index < elements.lastIndex) {
//                Spacer(modifier = Modifier.height(10.dp))
//            }
//        }
//    }
//}
