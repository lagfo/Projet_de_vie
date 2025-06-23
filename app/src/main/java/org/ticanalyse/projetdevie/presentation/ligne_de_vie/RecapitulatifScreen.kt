package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppShape
import org.ticanalyse.projetdevie.presentation.introduction.PageIndicator
import org.ticanalyse.projetdevie.ui.theme.Roboto


//data class ElementScolarite(val id:Int,val topicImage:Painter,val topicTitle:String)
@Composable
fun RecapitulatifScreen(
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit
) {

    val viewModel= hiltViewModel<LigneDeVieViewModel>()

    val passedElement by viewModel.passedelEments.collectAsState()

    val presentElement by viewModel.elementEncours.collectAsState()
    val element by viewModel.elements.collectAsState()

    Log.d("TAG", "RecapitulatifScreen: evenement passe : $passedElement \n present element : $presentElement and elements $element ")

    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )
    val scope = rememberCoroutineScope()
    var isButtonVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ElementScolarite?>(null) }
    isButtonVisible= pagerState.currentPage != 0

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

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(0.dp, 10.dp),
                    text ="Passé",
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
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                modifier = Modifier.weight(0.2f),
                                onClick = {
                                    Log.d("TAG", "LigneDeVieScreen: current page is ${pagerState.currentPage} ")
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

                                    }
                                }
                                1->{
                                    LazyColumn(
                                        modifier = Modifier.weight(2f),
                                        contentPadding = PaddingValues(20.dp)
                                    ){

                                    }

                                }
                                else->error("Page inexistente")
                            }
                            if(showDialog && selectedItem!=null){
                                Log.d("TAG", "LigneDeVieScreen:Modal dialog is openned")
//                                ModalDialog(item=selectedItem!!, onDismiss ={
//                                    showDialog=false
//                                    selectedItem=null
//                                },
//                                    viewModel = viewModel
//                                )

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

}