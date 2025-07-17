package org.ticanalyse.projetdevie.presentation.ligne_de_vie

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.Element
import org.ticanalyse.projetdevie.presentation.common.AppButton
import org.ticanalyse.projetdevie.presentation.common.AppInputFieldMultiLine
import org.ticanalyse.projetdevie.presentation.common.AppText
import org.ticanalyse.projetdevie.presentation.common.appSTTManager
import org.ticanalyse.projetdevie.presentation.common.appTTSManager
import org.ticanalyse.projetdevie.ui.theme.Roboto
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalDialogForEdit(
    modifier: Modifier = Modifier,
    item: Element,
    onDismiss:()->Unit,
    viewModel: LigneDeVieViewModel
) {
    val status by viewModel.upsertSuccess.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(status){
        Log.d("TAG", "ModalDialog: status value is $status ")
        if(status){
            Toast.makeText(context, "Modification réussie", Toast.LENGTH_SHORT).show()
            viewModel.resetUpsertStatus()
            onDismiss()
        }
    }

    ModalBottomSheet(
        sheetState =rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest =onDismiss,
        modifier =modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp, 0.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ){
                var dateDebut by rememberSaveable { mutableStateOf("") }
                var isdateDebutValide by rememberSaveable { mutableStateOf(false) }
                var dateFin by rememberSaveable { mutableStateOf("") }
                var isdateFinValide by rememberSaveable { mutableStateOf(false) }
                var dateEncours by rememberSaveable { mutableStateOf("") }
                var isdateEncoursValide by rememberSaveable { mutableStateOf(false) }
                val ttsManager = appTTSManager()
                val sttManager = appSTTManager()
                var description by rememberSaveable { mutableStateOf("") }
                var currentYear= LocalDate.now().year
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Surface(
                        modifier = Modifier.size(width = 30.dp, height = 30.dp),
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
                            contentDescription = item.label,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.background(Color.White)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    AppText(
                        text = item.label,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        color = colorResource(id = R.color.text),
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        ttsManager = ttsManager,
                        isTextAlignCenter = true
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically

                ){
                    Column(
                        modifier = Modifier.weight(1f)
                    ){
                        AppText(
                            text = "Evènement passé",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            color = colorResource(id = R.color.text),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            ttsManager = ttsManager,
                            isTextAlignCenter = true
                        )

                        OutlinedTextField(
                            enabled = dateEncours.isBlank(),
                            value =dateDebut,
                            onValueChange = { it ->
                                if(it.length<=4){
                                    dateDebut=it
                                }
                            },
                            label = {
                                Text(
                                    text="Année de début",
                                    fontSize = 9.99.sp,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Bold

                                )
                            },
                            textStyle = TextStyle(color = Color.Black),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            shape =RoundedCornerShape(50),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor =Color.Black,
                                focusedBorderColor = colorResource(R.color.secondary_color),
                            ),
                            trailingIcon = {
                                IconButton(onClick = {  }) {
                                    Icon(
                                        imageVector = Icons.Filled.DateRange,
                                        contentDescription = "calendar"
                                    )
                                }
                            },
                            supportingText = {
                                Text(
                                    text = if(dateDebut.isNotEmpty()&&dateDebut.isNotBlank()){
                                        if(dateDebut.length==4&&dateDebut.first()=='1'&&dateDebut.toInt()<=currentYear||dateDebut.length==4&&dateDebut.first()=='2'&&dateDebut.toInt()<=currentYear){
                                            isdateDebutValide=true
                                            ""
                                        } else {
                                            isdateDebutValide=false
                                            "Année invalide"
                                        }
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                        OutlinedTextField(
                            enabled = dateEncours.isBlank(),
                            value =dateFin,
                            onValueChange = { it ->
                                if(it.length<=4){
                                    dateFin=it

                                }
                            },
                            supportingText = {
                                Text(
                                    text = if(dateFin.isNotEmpty()&&dateFin.isNotBlank()){
                                        if(dateFin.length==4&&dateFin.first()=='1'&&dateFin.toInt()<=currentYear||dateFin.length==4&&dateFin.first()=='2'&&dateFin.toInt()<=currentYear){
                                            if(dateFin.toInt()<dateDebut.toInt()){
                                                isdateFinValide=false
                                                "L'année de fin est toujours supérieure à l'année de début"
                                            }else{
                                                isdateFinValide=true
                                                ""
                                            }
                                        } else {
                                            isdateFinValide=false
                                            "Année invalide"
                                        }
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.colorScheme.error
                                )
                            },
                            label = {
                                Text(
                                    text="Année de fin",
                                    fontSize = 9.99.sp,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Bold
                                )
                            },
//                        supportingText ={
//                            if(Global.validateAnneeDeFin(dateFin.toInt())){
//                                Text(text = "Année invalide")
//                            }
//                        },
                            textStyle = TextStyle(color = Color.Black),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            shape =RoundedCornerShape(50),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor =Color.Black,
                                focusedBorderColor = colorResource(R.color.secondary_color),
                            ),
                            trailingIcon = {
                                IconButton(onClick = {  }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "calendar"
                                    )
                                }
                            },
                        )
                    }
                    VerticalDivider(
                        modifier = Modifier.height(150.dp), // Adjust height as needed
                        thickness = 1.dp,
                        color = colorResource(R.color.primary_color)
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ){

                        AppText(
                            text = "Evènement en cours",
                            fontFamily = Roboto,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal,
                            color = colorResource(id = R.color.text),
                            fontSize = 12.sp,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            ttsManager = ttsManager,
                            isTextAlignCenter = true
                        )
                        OutlinedTextField(
                            enabled = !(dateDebut.isNotBlank()||dateFin.isNotBlank()),
                            value =dateEncours,
                            onValueChange = { it ->
                                if(it.length<=4){
                                    dateEncours=it
                                }
                            },
                            label = {
                                Text(
                                    text="Année de début",
                                    fontSize = 9.99.sp,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Bold

                                )
                            },
                            textStyle = TextStyle(color = Color.Black),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            ),
                            shape =RoundedCornerShape(50),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedLabelColor = Color.Black,
                                unfocusedLabelColor =Color.Black,
                                focusedBorderColor = colorResource(R.color.secondary_color),
                            ),
                            trailingIcon = {
                                IconButton(onClick = {  }) {
                                    Icon(
                                        imageVector = Icons.Filled.DateRange,
                                        contentDescription = "calendar"
                                    )
                                }
                            },
                            supportingText = {
                                Text(
                                    text = if(dateEncours.isNotEmpty()&&dateEncours.isNotBlank()){
                                        if(dateEncours.length==4 && dateEncours.first()=='1'&&dateEncours.toInt()<=currentYear|| dateEncours.length==4 && dateEncours.first()=='2'&&dateEncours.toInt()<=currentYear){
                                            isdateEncoursValide=true
                                            ""
                                        } else {
                                            isdateEncoursValide=false
                                            "Année invalide"
                                        }
                                    } else {
                                        ""
                                    },
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }

                }

                AppInputFieldMultiLine(
                    value =description,
                    onValueChange = {
                        description=it
                    },
                    label ="Commentaire",
                    ttsManager =ttsManager,
                    sttManager =sttManager,
                )
                //Validate button
                AppButton(text="Valider", onClick ={
                    if(isdateDebutValide&&isdateFinValide){
                        viewModel.addElement(
                            id = item.id,
                            label = item.label,
                            startYear =dateDebut.toInt(),
                            endYear = dateFin.toInt(),
                            inProgressYear =0,
                            duration =dateFin.toInt()-dateDebut.toInt(),
                            labelDescription = description,
                            status =false,
                            creationDate = LocalDate.now().toString()
                        )
                    }else if(isdateEncoursValide){
                        viewModel.addElement(
                            id = item.id,
                            label = item.label,
                            startYear =0,
                            endYear = 0,
                            inProgressYear =dateEncours.toInt(),
                            duration =0,
                            labelDescription = description,
                            status =true,
                            creationDate = LocalDate.now().toString()
                        )
                    }

                })
            }
        }

    }

}

