package com.example.btremote.compose.remoteActivity

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.btremote.Orientation
import com.example.btremote.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RemoteToolBar(
    editModel: Boolean,
    gyroMode: Boolean,
    onSettingClick:()->Unit,
    onDebugClick:()->Unit,
    onEditClick:()->Unit,
    onRemoteModeClick:()->Unit,
    context: Context = LocalContext.current
) {

    var toolVisible by remember {
        mutableStateOf(false)
    }
    val addButtonAnimate = animateFloatAsState(
        targetValue = if (toolVisible) 45f else 0f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = 0,
            easing = LinearEasing
        )
    )

    AnimatedVisibility(
        visible = !editModel,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 40.dp, start = 5.dp)
        ) {
            IconButton(
                onClick = { toolVisible = !toolVisible }, modifier = Modifier.rotate(
                    addButtonAnimate.value
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_circle_black_24dp),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            AnimatedVisibility(
                visible = toolVisible,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    IconButton(
                        onClick = onSettingClick, modifier = Modifier
                            .padding(top = 10.dp)
                            .size(30.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_settings_black_24dp),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onDebugClick, modifier = Modifier
                            .padding(top = 10.dp)
                            .size(30.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_bug_report_black_36dp),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onEditClick , modifier = Modifier
                            .padding(top = 10.dp)
                            .size(30.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_mode_edit_outline_black_24dp),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onRemoteModeClick , modifier = Modifier
                            .padding(top = 10.dp)
                            .size(30.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = if (gyroMode) R.drawable.gyro else R.drawable.baseline_games_black_24dp),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}