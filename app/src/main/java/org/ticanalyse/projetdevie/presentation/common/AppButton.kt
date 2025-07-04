package org.ticanalyse.projetdevie.presentation.common

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import org.ticanalyse.projetdevie.R

@Composable
fun AppButton(
    text: String,
    enabled:Boolean=true,
    onClick: () -> Unit
){

    Button(
        onClick = onClick,
        enabled=enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.secondary_color),
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

@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = colorResource(id = R.color.primary_color)
        )
    }
}