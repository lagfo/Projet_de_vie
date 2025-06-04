package org.ticanalyse.projetdevie.utils

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import org.ticanalyse.projetdevie.R

@OptIn(UnstableApi::class)
@Composable
fun PlayPauseButton(player: Player, modifier: Modifier = Modifier) {
    val state = rememberPlayPauseButtonState(player)
    val icon = if (state.showPlay) painterResource(R.drawable.baseline_play_arrow_24) else painterResource(R.drawable.baseline_pause_24)
    val contentDescription = if (state.showPlay) "Play" else "Pause"
    val graySemiTransparentBG = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    val btnModifier = modifier
            .size(50.dp)
            .background(graySemiTransparentBG, CircleShape)
    Row (
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton (
            onClick = state::onClick,
            modifier = btnModifier,
            enabled = state.isEnabled
        ) {
            Icon(painter = icon, contentDescription = contentDescription)
        }
    }
}