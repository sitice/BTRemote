package com.example.btremote.compose.remoteActivity

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.btremote.R
import com.example.btremote.protocol.*
import com.example.btremote.viewmodel.RemoteViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun WidgetToolBar(
    editMode: Boolean,
    modifier: Modifier,
    onRotateLeftClick: () -> Unit,
    onRotateRightClick: () -> Unit,
    onEnlargeClick: () -> Unit,
    onReduceClick: () -> Unit,
    onResetClick: () -> Unit,
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
    onOKClick: () -> Unit,
    model: RemoteViewModel = viewModel()
) {

    AnimatedVisibility(
        visible = editMode,
        enter = scaleIn(),
        exit = scaleOut(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.White, RoundedCornerShape(10.dp)), verticalAlignment = Alignment.CenterVertically

        ) {
            model.selectWidget?.let { Text(text = it.name,modifier.padding(start = 10.dp, end = 10.dp)) }
            IconButton(onClick = onRotateLeftClick, modifier = Modifier.size(30.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_rotate_left_black_24dp),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onRotateRightClick, modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_rotate_right_black_24dp),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onEnlargeClick, modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bianxiao),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onReduceClick, modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.bianda),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onResetClick, modifier = Modifier
                    .padding(start = 10.dp)
                    .size(25.dp)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.reset),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onAddClick, modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_circle_black_24dp),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onDeleteClick, modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_delete_forever_black_48dp),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onCancelClick, modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_cancel_black_24dp),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = onOKClick, modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_check_circle_black_24dp),
                    contentDescription = null
                )
            }
        }
    }

}