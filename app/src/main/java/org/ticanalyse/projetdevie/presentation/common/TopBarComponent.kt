package org.ticanalyse.projetdevie.presentation.common

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.BelfastGrotesk
import java.io.File

@Composable
fun TopBarComponent(
    nom:String,
    prenom:String,
    profileImagePath:String
){
    val painter= rememberAsyncImagePainter(
        model=File(profileImagePath),
        onSuccess = {
            Log.d("TAG", "ProjetVieScreen:La photo de profile a été chargé avec succès ")
        },
        onError = {
            Log.d("TAG", "ProjetVieScreen:La photo de profile n'a pas pu être chargée chargé  ")

        },
        placeholder = painterResource(R.drawable.avatar)
    )

 //   var isDropdownMenuExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(85.dp)
            .background(Color.White)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(R.color.primary_color))
                .padding(16.dp)
        )
        Image(
            painter = painterResource(R.drawable.image2),
            contentDescription ="Image2",
            modifier = Modifier
                .height(85.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillBounds
        )
        Image(
            painter = painterResource(R.drawable.image1),
            contentDescription ="Image1",
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier.fillMaxSize().absoluteOffset(0.dp,5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(105.dp))
            Text(
                modifier = Modifier.width(200.dp),
                text="$nom $prenom uojojugygguuuuiyiuyiuiiu",
                fontFamily = BelfastGrotesk,
                fontWeight= FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style= TextStyle(Color.White)
            )

        }
        Box(
            modifier = Modifier.size(110.dp).absoluteOffset((10).dp, (-4).dp),
            contentAlignment= Alignment.Center,

        ) {
            Image(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color =colorResource(R.color.secondary_color),
                        shape = CircleShape
                    ),
                painter =painter,
                contentDescription = "Profil image"
            )
            Image(
                modifier = Modifier
                    .size(20.dp)
                    .offset(x=30.dp,y=20.dp),
                painter = painterResource(R.drawable.badge_icon),
                contentDescription = "Profil image"
            )



        }
//        Box(
//            contentAlignment= Alignment.BottomEnd,
//            modifier= Modifier.fillMaxWidth().height(55.dp),
//
//
//            ) {
//
//            IconButton(
//                onClick = {
//                    isDropdownMenuExpanded=!isDropdownMenuExpanded
//                }
//            ) {
//                Icon(imageVector = Icons.Default.Menu, contentDescription ="menu icon", tint = Color.White)
//            }
//        }
//        DropdownMenu(
//            expanded=isDropdownMenuExpanded,
//            onDismissRequest = {isDropdownMenuExpanded=false},
//            offset = DpOffset(x = 235.dp, y = (-35).dp)
//        ) {
//
//
//            DropdownMenuItem(
//                text = { Text("Profile") },
//                onClick = {
//                    // Handle Profile click
//                    isDropdownMenuExpanded = false
//                },
//                leadingIcon = {
//                    Icon(
//                        Icons.Default.Person,
//                        contentDescription = "Profile"
//                    )
//                }
//            )
//
//        }


    }
}

@Composable
@Preview(showBackground = true, device = "id:pixel_6")
fun ProjetVieScreenPreview(){
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold (
            topBar = {
                TopBarComponent("RAYAISSE","Patrick","")
            }
        ){it->
            Column(
                modifier = Modifier.padding(it)
            ) {

            }
        }
    }
}