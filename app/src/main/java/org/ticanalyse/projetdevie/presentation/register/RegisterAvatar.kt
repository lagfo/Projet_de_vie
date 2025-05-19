package org.ticanalyse.projetdevie.presentation.register

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme
import java.io.File

@Composable
fun RegisterAvatar (){

    val imageUri = rememberSaveable { mutableStateOf ("") }
    val painter = rememberAsyncImagePainter (imageUri.value.ifEmpty {R.drawable.baseline_photo_camera_24})
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }
    val cameraLaucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {bitmap: Bitmap? ->
        bitmap?.let {
            val file = File.createTempFile("avatar_", ".jpg", context.cacheDir).apply {
                outputStream().use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) }
            }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            imageUri.value = uri.toString()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize(),
        ){
            val (avatar,cameraBtn)=createRefs()
            Box (modifier = Modifier
                .constrainAs(avatar){}){
                Image(
                    painter = painter,
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxWidth(.35f)
                        .aspectRatio(1f)
                        .background(color = Color.Blue)
                        .border(
                            BorderStroke(
                                width=2.dp,
                                color = colorResource(R.color.primary_color)),
                            shape = CircleShape
                        )
                        .clickable{ launcher.launch("image/*") },

                    )
            }
            Box (modifier = Modifier
                .constrainAs(cameraBtn){
                    end.linkTo(avatar.end)
                    bottom.linkTo(avatar.bottom)
                }){
                Image(
                    painter = painterResource (id = R.drawable.baseline_photo_camera_24),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background (Color.Black)
                        .size(50.dp)
                        .padding(10.dp)
                        .clickable { cameraLaucher.launch(null) }
                )
            }



        }
    }



}

@Preview(showBackground = true)
@Composable
fun RegisterAvatarPreview(){
    ProjetDeVieTheme {
        RegisterAvatar ()
    }
}