package org.ticanalyse.projetdevie.presentation.common

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme
import org.ticanalyse.projetdevie.utils.Global.createImageFile
import org.ticanalyse.projetdevie.utils.Global.getUriForFile
import timber.log.Timber
import java.io.File
import java.io.IOException


@Composable
fun ProfileAvatar (
    imageUri: MutableState<String>
){

    val painter = rememberAsyncImagePainter (imageUri.value.ifEmpty {R.drawable.logo})
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri.value = it.toString()
        }
    }

    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var tempPhotoFile by rememberSaveable { mutableStateOf<File?>(null) }

    val cameraLaucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {success ->
        if (success) {
            tempPhotoFile?.let {
                imageUri.value = it.toUri().path ?: ""
            }
        } else {

        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted. Continue the action or initialize features that depend on it.
            val photoFile: File? = try {
                createImageFile(context).also { tempPhotoFile = it }
            } catch (ex: IOException) {
                Timber.e("Error creating image file: ${ex.message}")
                null
            }

            photoFile?.let {
                val uri = getUriForFile(context, it)
                photoUri = uri // Store the URI that will be used (and where the image will be saved)
                cameraLaucher.launch(uri)
            }
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied.
            Toast.makeText(context, "Camera permission denied.", Toast.LENGTH_SHORT).show()
        }
    }
//    val cameraLaucher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicture()
//    ) {bitmap ->
//        bitmap?.let {
//            val file = File.createTempFile("avatar_", ".jpg", context.cacheDir).apply {
//                outputStream().use { out -> bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) }
//            }
//            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
//            imageUri.value = uri.toString()
//        }
//    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout{
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
                        .background(color = colorResource(id=R.color.third_color))
                        .border(
                            BorderStroke(
                                width=3.dp,
                                color = colorResource(R.color.third_color)),
                            shape = CircleShape
                        )
                        .clickable{
                            launcher.launch("image/*")
                        },

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
                        .background (color = colorResource(id=R.color.secondary_color))
                        .border(
                            BorderStroke(
                                width=1.dp,
                                color = Color.White
                            ),
                            shape = CircleShape
                        )
                        .size(40.dp)
                        .padding(10.dp)
                        .aspectRatio(1f)
                        .clickable {
                            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) ==  PackageManager.PERMISSION_GRANTED) {
                                // Permission is granted. Continue the action or initialize features that depend on it.
                                val photoFile: File? = try {
                                    createImageFile(context).also { tempPhotoFile = it }
                                } catch (ex: IOException) {
                                    Timber.e("Error creating image file: ${ex.message}")
                                    null
                                }

                                photoFile?.let {
                                    val uri = getUriForFile(context, it)
                                    photoUri = uri // Store the URI that will be used (and where the image will be saved)
                                    cameraLaucher.launch(uri)
                                }
                            } else {
                                // You can directly ask for the permission.
                                // The registered ActivityResultCallback gets the result of this request.
                                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                            }
//                            launcher.launch("image/*")
                        }
                )
            }



        }
        Text(
            text = stringResource(id=R.string.photo_profil),
            style =  MaterialTheme.typography.labelSmall,
        )
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterAvatarPreview(){
    ProjetDeVieTheme {
        val imageUri = rememberSaveable { mutableStateOf ("") }
        ProfileAvatar (imageUri)
    }
}