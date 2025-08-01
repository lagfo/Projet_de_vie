package org.ticanalyse.projetdevie.presentation.planification_de_projet

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.ticanalyse.projetdevie.R
import org.ticanalyse.projetdevie.domain.model.PlanAction

@Composable
fun PlanActionTable(
    modifier: Modifier = Modifier,
    onNavigateToEditPage:(Int)->Unit
) {
    val headerTitles = listOf("Activité","Description", "Acteur", "Financement", "Période","Actions")
    val viewModel= hiltViewModel<PlanificationViewModel>()
    val context = LocalContext.current
    val actions by viewModel.planAction.collectAsStateWithLifecycle()
    var status by remember{ mutableStateOf(false) }
    // Shared scroll state for header and rows
    val scrollState = rememberScrollState()
    val columnWidths = listOf(100.dp, 150.dp, 100.dp, 120.dp, 100.dp, 80.dp)
    val totalWidth = columnWidths.fold(0.dp) { acc, width -> acc + width }
    LaunchedEffect(status){
        if(status){
            Toast.makeText(context, "Ligne Supprimée !", Toast.LENGTH_SHORT).show()
        }
    }


    Column {

        LazyColumn(
            modifier = modifier
                .padding( vertical = 50.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                ),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // Header
            item {
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    headerTitles.forEachIndexed { index, title ->
                        Text(
                            text = title,
                            modifier = Modifier.width(columnWidths[index]),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Data rows
            if (actions.isEmpty()) {
                item {
                    EmptyTableRowWithWidths(columnWidths = columnWidths, scrollState = scrollState)
                }
            } else {
                itemsIndexed(actions) { index, planAction ->
                    TableRowWithScrollableContent(
                        planAction = planAction,
                        isEven = index % 2 == 0,
                        columnWidths = columnWidths,
                        scrollState = scrollState,
                        onEdit = { onNavigateToEditPage(planAction.id) },
                        onDelete = {
                            viewModel.deleteLine(planAction.id)
                            status=true
                        }
                    )
                }
            }
        }

        // Scroll indicator
        ScrollIndicator(
            scrollState = scrollState,
            totalWidth = totalWidth,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

    }
}

@Composable
private fun ScrollIndicator(
    scrollState: ScrollState,
    totalWidth: Dp,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Only show indicator if content is wider than screen
    if (totalWidth > screenWidth) {
        Column(modifier = modifier) {
            // Text hint
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "← Faites glisser pour voir plus →",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Visual scroll bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(2.dp)
                    )
            ) {
                // Scroll thumb
                val scrollProgress = if (scrollState.maxValue > 0) {
                    scrollState.value.toFloat() / scrollState.maxValue.toFloat()
                } else 0f

                val thumbWidth = (screenWidth / totalWidth) * screenWidth

                Box(
                    modifier = Modifier
                        .width(thumbWidth)
                        .height(3.dp)
                        .offset(x = (screenWidth - thumbWidth) * scrollProgress)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun TableRowWithScrollableContent(
    planAction: PlanAction,
    isEven: Boolean,
    columnWidths: List<Dp>,
    scrollState: ScrollState,
    onEdit: (PlanAction) -> Unit = {},
    onDelete: (PlanAction) -> Unit = {}
) {
    val backgroundColor = if (isEven) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }

    Row(
        modifier = Modifier
            .horizontalScroll(scrollState) // Use the same scroll state
            .background(backgroundColor)
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .clickable { /* Handle row click */ },
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val cellData = listOf(
            planAction.activite,
            planAction.activiteDescription,
            planAction.acteur,
            planAction.financement,
            planAction.periode
        )

        cellData.forEachIndexed { index, text ->
            Text(
                text = text,
                modifier = Modifier.width(columnWidths[index]),
                style = MaterialTheme.typography.bodySmall,
                maxLines =20,
                fontSize = 15.sp
            )
        }

        // Actions column
        Row(
            modifier = Modifier.width(columnWidths.last()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { onEdit(planAction) },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }

            IconButton(
                onClick = { onDelete(planAction) },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyTableRowWithWidths(
    columnWidths: List<Dp>,
    scrollState: ScrollState
) {
    Row(
        modifier = Modifier
            .horizontalScroll(scrollState)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 20.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        columnWidths.forEachIndexed { index, width ->
            Box(
                modifier = Modifier.width(width),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (index == 0) "Aucune donnée" else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic,
                    fontSize = 11.sp
                )
            }
        }
    }
}
