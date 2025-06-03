package org.ticanalyse.projetdevie.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun ExoPlayer(
    videoId: Int
) {
    val context = LocalContext.current
//    val showControls by remember { mutableStateOf(true) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle = rememberSaveable { mutableStateOf(Lifecycle.Event.ON_CREATE) }

    val videoUri = "android.resource://" + context.packageName + "/" + videoId

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .also { exoPlayer ->
                val mediaItem = MediaItem.Builder()
                    .setUri(videoUri)
                    .build()
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady = true
            }
    }
    
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.pause()
                }

                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.play()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    exoPlayer.release()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            exoPlayer.release()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
//                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                player = exoPlayer
            }
        },
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
            .aspectRatio(16 / 9f)
            .fillMaxWidth(),
        update = {
            when(lifecycle.value) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                    it.player?.play()
                }
                else -> Unit
            }
        }
    )

//    Box() {
//
//        PlayerSurface(
//            player = exoPlayer,
//            modifier = Modifier.background(MaterialTheme.colorScheme.background)
//                .aspectRatio(16 / 9f)
//                .fillMaxWidth()
//                .padding(16.dp)
//        )
//
//        if (showControls) {
//            PlayPauseButton(exoPlayer)
//        }
//    }
}

