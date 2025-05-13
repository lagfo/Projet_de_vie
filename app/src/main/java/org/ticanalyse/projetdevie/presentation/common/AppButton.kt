package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.ui.theme.ProjetDeVieTheme

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit
){

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.primary_color),
            contentColor = Color.White
        ),
        shape = CircleShape
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
    }

}

@Preview(showBackground = true)
@Composable
fun AppButtonPreview() {
    ProjetDeVieTheme {
        AppButton("Android") {}
    }
}