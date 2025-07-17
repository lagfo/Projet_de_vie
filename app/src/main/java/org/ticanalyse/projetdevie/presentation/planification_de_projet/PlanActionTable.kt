package org.ticanalyse.projetdevie.presentation.planification_de_projet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.PlanAction

@Composable
fun PlanActionTable(
    modifier: Modifier = Modifier,
) {
    val headerTitles = listOf("Activité", "Acteur", "Financement", "Période")
    val viewModel= hiltViewModel<PlanificationViewModel>()
    val actions by viewModel.planAction.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = modifier
            .padding(horizontal =8.dp, vertical = 50.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {

        // Header
        item {
            TableHeader(titles = headerTitles)
        }

        // Data rows
        if (actions.isEmpty()) {
            item {
                EmptyTableRow()
            }
        } else {
            itemsIndexed(actions) { index, planAction ->
                TableRow(
                    planAction = planAction,
                    isEven = index % 2 == 0
                )
            }
        }
    }
}

@Composable
private fun TableHeader(titles: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id=R.color.primary_color))
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        titles.forEach { title ->
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun TableRow(
    planAction: PlanAction,
    isEven: Boolean
) {
    val backgroundColor = if (isEven) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .clickable {
                // Handle row click if needed
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TableCell(
            text = planAction.activite,
            modifier = Modifier.weight(1f)
        )
        TableCell(
            text = planAction.acteur,
            modifier = Modifier.weight(1f)
        )
        TableCell(
            text = planAction.financement,
            modifier = Modifier.weight(1f)
        )
        TableCell(
            text = planAction.periode,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TableCell(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.ifEmpty { "-" },
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}



@Composable
private fun EmptyTableRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 32.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = "Aucune donnée disponible",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}