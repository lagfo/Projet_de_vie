package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Environment
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itextpdf.io.font.constants.StandardFonts
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.event.PdfDocumentEvent
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Div
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.layout.properties.VerticalAlignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.app_navigator.AppNavigationViewModel
import org.ticanalyse.projetdevie.presentation.bilan_competance.generatePdf
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.UserInfoDialog
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.presentation.introduction.PageIndicator
import org.ticanalyse.projetdevie.ui.theme.Roboto
import org.ticanalyse.projetdevie.utils.Global
import org.ticanalyse.projetdevie.utils.PdfUtil.addUserInfoSection
import org.ticanalyse.projetdevie.utils.PdfUtil.getImageDataFromResource
import org.ticanalyse.projetdevie.utils.SimpleFooterEventHandler
import org.ticanalyse.projetdevie.utils.WatermarkImageEventHandler
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale


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
    val pdfscope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val appNavigationViewModel = hiltViewModel<AppNavigationViewModel>()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    var showUserDialog by remember { mutableStateOf(false) }
    val setOfIds = setOf(10,11, 12, 16, 19)



    LaunchedEffect(reponseQuestion,isClicked,context) {

//        reponse1=reponseQuestion[0].firstResponse
//        reponse2=reponseQuestion[0].secondResponse
        if(reponseQuestion.isNotEmpty()){
            Timber.tag("TAG").d("RecapitulatifScreen: $reponseQuestion ")
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

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(25.dp))
            AppText(
                text = "Récapitulatifs",
                fontFamily = Roboto,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Normal,
                color = colorResource(R.color.primary_color),
                fontSize = 21.sp,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                ttsManager = ttsManager
            )

            Box {
                AppShape(
                    cornerRadius = 45.dp,
                    arcHeight = 0.dp,
                    modifier = Modifier.fillMaxHeight(.85f),
                    bottomCornerRadius = 45.dp
                )


                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier=Modifier.height(5.dp))

                    AppText(
                        text = if(pagerState.currentPage==0)"Passé" else if(pagerState.currentPage==1)"Présent" else if(pagerState.currentPage==2) "Questions" else "",
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Normal,
                        color = Color.White,
                        fontSize = 19.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        ttsManager = ttsManager,
                        isTextAlignCenter = true
                    )

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

                                                        },setOfIds
                                                    )

                                                    if(index <listOfPassedElement.lastIndex) {
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                    }
                                                }
                                            }

                                        }else{
                                            AppText(
                                                text = "Aucun évènement passé n'a été enregistré",
                                                fontFamily = Roboto,
                                                fontWeight = FontWeight.Black,
                                                fontStyle = FontStyle.Normal,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                ttsManager = ttsManager,
                                                isTextAlignCenter = true
                                            )

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

                                                        },
                                                        setOfIds
                                                    )

                                                    if(index2 <listOfPresentElement.lastIndex) {
                                                        Spacer(modifier = Modifier.height(10.dp))
                                                    }
                                                }
                                            }

                                        }else{
                                            AppText(
                                                text = "Aucun évènement présent n'a été enregistré",
                                                fontFamily = Roboto,
                                                fontWeight = FontWeight.Black,
                                                fontStyle = FontStyle.Normal,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                                ttsManager = ttsManager,
                                                isTextAlignCenter = true
                                            )

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


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

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

                    Spacer(modifier = Modifier.width(14.dp))
                    FloatingActionButton(
                        modifier = Modifier.size(45.dp),
                        onClick = {
                            showUserDialog = true

                                  },
                        containerColor = colorResource(id = R.color.secondary_color),
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(
                            painter  = painterResource(id=R.drawable.picture_as_pdf_24),
                            contentDescription = "Generer le PDF",
                        )
                    }


                    // Dialog pour saisir les informations utilisateur
                    if (showUserDialog) {
                        UserInfoDialog(
                            onDismiss = { showUserDialog = false },
                            onConfirm = { nom, prenom, telephone ->
                                scope.launch {
                                    // Sauvegarder l'utilisateur
                                    val newUser = User(nom = nom, prenom = prenom, numTel = telephone)
                                    viewModel.setCurrentUser(newUser)
                                    showUserDialog = false
                                    // Générer le PDF avec les nouvelles informations
                                    isLoading = true
                                    withContext(Dispatchers.IO) {
                                        generatePdf(
                                            context=context,
                                            user = newUser,
                                            listOfPassedElement = listOfPassedElement,
                                            listOfPresentElement  = listOfPresentElement,
                                            listQuestionsLigneDeVie = listOf(
                                                Pair("Qu'ai-je déjà réalisé ?", reponse1.ifBlank {
                                                    "Aucune reponse renseignée"
                                                }
                                                ),
                                                Pair("Qu'est ce que je suis capable de faire ?",
                                                    reponse2.ifBlank {
                                                        "Aucune reponse renseignée"
                                                    })
                                            )
                                        )
                                    }
                                    isLoading = false
                                }
                            }
                        )
                    }


                }


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
fun CustomedElementLayout(item: Element, onClick:()->Unit,setOfIds:Set<Int>) {
    if(!item.status && item.id !in setOfIds){

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
    }else if(item.status && item.id !in setOfIds){
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
                            text ="Année de début:${item.inProgressYear}",
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.Black,
                            maxLines =1
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
                            text ="Année:${item.inProgressYear}",
                            textAlign = TextAlign.Center,
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Color.Black,
                            maxLines =1
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

fun generatePdf(
    context: Context,
    user: User,
    listOfPassedElement: List<Element>,
    listOfPresentElement: List<Element>,
    listQuestionsLigneDeVie: List<Pair<String, String>>
){
    val setOfIds = setOf(10,11, 12, 16, 19)
    val currentDateTime = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(Date())
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val file = File(directory, "Projet_de_vie_ligne_de_vie_${user.nom.replace(" ","_")}${user.prenom.replace(" ","_")}_$currentDateTime.pdf")

    val pdfWriter = PdfWriter(file)
    val pdfDoc = PdfDocument(pdfWriter)
    pdfDoc.defaultPageSize = PageSize.A4
    val document = Document(pdfDoc, PageSize.A4)
    document.setMargins(50f, 40f, 50f, 40f)

    val watermarkImageData = getImageDataFromResource(context, R.drawable.logo)
    pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, WatermarkImageEventHandler(watermarkImageData))
    pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, SimpleFooterEventHandler())


    addUserInfoSection(document, user, context)

    document.add(Paragraph("\n"))
    val pageWidth = pdfDoc.defaultPageSize.width
    addLigneDeVieSection(context,document, "Évènements du passé", listOfPassedElement,pageWidth,setOfIds)
    document.add(Paragraph("\n\n\n"))
    addLigneDeVieSection(context,document, "Évènements du présent", listOfPresentElement,pageWidth,setOfIds)
    document.add(Paragraph("\n\n\n"))
    addQuestions(document, "Questions sur la ligne de vie", listQuestionsLigneDeVie)


    document.close()

    val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)

    if (file.exists()) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("application/pdf")
        intent.clipData = ClipData.newRawUri(file.name, uri)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.setDataAndType(uri, "application/pdf")
        context.startActivity(Intent.createChooser(intent, "Veuillez choisir une application"))
    }
}


private fun addLigneDeVieSection(context: Context,document: Document, titre: String, elements: List<Element>,pageWidth: Float,setOfIds:Set<Int>) {
    val tableWidth = pageWidth - 80f
    document.add(Paragraph(titre)
        .setFontSize(17.5f)
        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

    document.add(Paragraph("\n"))
    val container = Div().setWidth(tableWidth)

    if (elements.isNotEmpty()) {
        elements.forEach { element ->

            val labelTable = Table(UnitValue.createPercentArray(floatArrayOf(10f, 90f)))
                .setWidth(UnitValue.createPercentValue(100f))
                .setMarginBottom(10f)

            val labelImageCell = Cell()
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.RIGHT)

            val imageId = when(element.id){
                1->R.drawable.ecole_primaire
                2->R.drawable.ecole_secondaire
                3->R.drawable.universite_ecole_superieur
                4->R.drawable.alphabetisation_langue_locale
                5->R.drawable.ecole_coranique
                6->R.drawable.ecole_formation_prof
                7->R.drawable.abandon_scolarite
                8->R.drawable.reprise_interruption_etude
                9->R.drawable.premier_apprentissage
                10->R.drawable.naissance_enfant
                11->R.drawable.mariage
                12->R.drawable.depart_foyer_familial
                13->R.drawable.ecole_coranique
                14->R.drawable.premier_emploi
                15->R.drawable.projet
                16->R.drawable.deces
                17->R.drawable.depart_migration
                18->R.drawable.retrouvaille
                19->R.drawable.grande_decision_personnel
                else -> R.drawable.ecole_primaire
            }

            val labelImage = Image(getImageDataFromResource(context, imageId))
                .setWidth(40f)
                .setHeight(40f)

            labelImageCell.add(labelImage)

            val labelTextCell = Cell()
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.LEFT)

            val labelText = Paragraph(element.label.replace("\n", " "))
                .setFontSize(15f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD))

            labelTextCell.add(labelText)
            labelTable.addCell(labelImageCell)
            labelTable.addCell(labelTextCell)
            container.add(labelTable)


            val yearText = if (element.status && element.id !in setOfIds) {
                "Année: ${element.inProgressYear}"
            } else if(!element.status && element.id !in setOfIds) {
                "De ${element.startYear} à ${element.endYear}"
            }else{
                "Année: ${element.inProgressYear}"
            }

            val fullText = "▪ $yearText : ${element.labelDescription}"

            val descriptionTable = Table(UnitValue.createPercentArray(floatArrayOf(100f)))
                .setWidth(tableWidth)
                .setMarginBottom(5f)

            val descriptionCell = Cell()
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.LEFT)

            val descriptionParagraph = Paragraph(fullText)
                .setFontSize(13f)
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))

            descriptionCell.add(descriptionParagraph)
            descriptionTable.addCell(descriptionCell)
            container.add(descriptionTable)
        }
    } else {
        container.add(Paragraph("Pas d'élément").setTextAlignment(TextAlignment.LEFT)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
    }
    document.add(container)
}

private fun addQuestions(document: Document, titre: String, questions: List<Pair<String, String>>) {
    document.add(Paragraph(titre).setTextAlignment(TextAlignment.CENTER)
        .setFontSize(17.5f)
        .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))

    questions.forEach { element ->
        document.add(Paragraph(element.first).setTextAlignment(TextAlignment.LEFT)
            .setFontSize(15f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)))
        document.add(Paragraph(element.second).setTextAlignment(TextAlignment.LEFT)
            .setFontSize(13f)
            .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)))
    }
}