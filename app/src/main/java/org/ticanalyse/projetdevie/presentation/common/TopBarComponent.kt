package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.BelfastGrotesk
import timber.log.Timber

@Composable
fun TopBarComponent(
    nom:String,
    prenom:String,
    profileImagePath:String,
){
    Timber.d("TopBarComponent: $profileImagePath")
 //   var isDropdownMenuExpanded by remember { mutableStateOf(false) }
    val painter = rememberAsyncImagePainter (profileImagePath.ifEmpty {R.drawable.avatar})
    val ttsManager = appTTSManager()
    var showBottomSheet by remember { mutableStateOf(false) }
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
            modifier = Modifier.fillMaxSize().absoluteOffset(0.dp,5.dp).padding(start = 5.dp, end = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(4f))
            Box(modifier = Modifier.weight(5f)) {
                AppText(
                    text = "$nom $prenom",
                    fontFamily = BelfastGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    color = Color.White,
                    fontSize = 12.sp,
                    style = TextStyle(Color.White),
                    ttsManager = ttsManager
                )
            }
            Box (
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = Icons.Filled.ChangeCircle,
                    tint = Color.White,
                    contentDescription = "change user",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            showBottomSheet = true
                        }

                )

            }

        }
        Box(
            modifier = Modifier.size(110.dp).absoluteOffset((10).dp, (-4).dp),
            contentAlignment= Alignment.Center,

        ) {
//            if (profileImagePath.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color =colorResource(R.color.secondary_color),
                        shape = CircleShape
                    ),
                painter = painter,
                contentScale = ContentScale.Crop,
                contentDescription = "Profil image"
            )

        }


    }
    AppResetModal(
        showBottomSheet = showBottomSheet,
        onDismissRequest = { showBottomSheet = false }
    )
}
