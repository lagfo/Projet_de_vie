package org.ticanalyse.projetdevie.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.User
import org.ticanalyse.projetdevie.presentation.common.TopBarComponent
import org.ticanalyse.projetdevie.ui.theme.BelfastGrotesk


data class Topic(val id:Int,val topicImage:Painter,val topicTitle:String)
@Composable
fun HomeScreen(
    currentUser: User,
    onNavigate: (String) -> Unit = {}
) {

    val items= listOf(
        Topic(1,painterResource(R.drawable.introduction),"Introduction"),
        Topic(2,painterResource(R.drawable.decouvrir_reseau),"Découvrir\nmon réseau"),
        Topic(3,painterResource(R.drawable.ligne_de_vie),"Ligne de vie"),
        Topic(4,painterResource(R.drawable.bilan_de_competences),"Bilan de\ncompétences"),
        Topic(5,painterResource(R.drawable.lien_avec_la_vie_reelle),"Lien avec\nla vie réelle"),
        Topic(6,painterResource(R.drawable.planification_du_projet),"Planification\ndu projet")
    )

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ) {
                TopBarComponent(currentUser.nom,currentUser.prenom,currentUser.avatarUri)
            }
        }
    ){innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()){

            Column(
                modifier= Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color.White)
            ) {

                Spacer(modifier= Modifier.height(40.dp))
                Text(
                    modifier= Modifier.fillMaxWidth(),
                    text = "Mon projet de vie !",
                    fontFamily = BelfastGrotesk,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize =33.sp),
                    color =colorResource(R.color.primary_color),
                )
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                ) {

                    itemsIndexed(items){ index, item ->

                        CustomItemLayout(item=item, onClick = {
                            when(item.id){
                                1->{
                                    onNavigate("Introduction")
                                }
                                2->{
                                    onNavigate("Mon Reseau")

                                }
                                3->{
                                    onNavigate("Ligne de vie")

                                }
                                4->{
                                    onNavigate("Bilan")

                                }
                                5->{
                                    onNavigate("Lien vie reel")

                                }
                                6->{
                                    onNavigate("Plannification")

                                }
                            }

                        })

                        if(index<items.lastIndex){
                            SharpEllipse(modifier = Modifier.size(10.dp,15.dp).offset(x=50.dp), color = colorResource(R.color.ellipsis_separator))
                        }

                    }
                }

            }

            Image(
                painter = painterResource(id = R.drawable.bg_img),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
                alpha = 0.07f

            )


        }
    }
}

@Composable
fun CustomItemLayout(item: Topic,onClick:()->Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Row (
        modifier = Modifier.fillMaxWidth().clickable(
            interactionSource=interactionSource,
            indication = ripple(color=colorResource(R.color.primary_color)),
            onClick = onClick
        ),
        verticalAlignment = Alignment.CenterVertically
    ){
        Surface(
            modifier = Modifier.size(width = 105.dp, height = 70.dp),
            shape = MaterialTheme.shapes.medium,
            color = Color.White,                                      // ← solid background
            border = BorderStroke(2.dp, colorResource(R.color.primary_color))
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
            fontFamily = BelfastGrotesk,
            fontWeight = FontWeight.Black,
            fontStyle = FontStyle.Italic,
            color =colorResource(R.color.secondary_color),
            style = TextStyle(fontSize =24.sp)

        )
    }
}


@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
//    HomeScreen()
}
@Composable
@Preview(showBackground = true)
fun HomeScreenPreview1() {

//    HomeScreen()
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview2() {

//    HomeScreen()
}

@Composable
fun SharpEllipse(
    modifier: Modifier = Modifier,
    color: Color = Color.Blue
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val centerX = size.width / 2f
        val centerY = size.height / 2f

        val path = Path().apply {
            moveTo(centerX, 0f) // Top point
            cubicTo(
                width, 0f,           // Control point 1
                width, height,       // Control point 2
                centerX, height      // Bottom point
            )
            cubicTo(
                0f, height,          // Control point 3
                0f, 0f,              // Control point 4
                centerX, 0f          // Back to top
            )
            close()
        }

        drawPath(path = path, color = color)
    }
}
