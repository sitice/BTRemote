package com.example.btremote.compose.rocker

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.btremote.R
import kotlin.math.*

@Composable
fun Rocker(
    modifier: Modifier,
    backgroundSize: Dp,
    rockerSize: Dp,
    editMode: MutableState<Boolean>,
    onDistanceChange: (Int) -> Unit,
    onAngleChange: (Float) -> Unit
) {
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    Box(modifier = modifier)
    {
        Image(
            painter = painterResource(id = R.drawable.xuniyaogan), contentDescription = null, modifier = Modifier
                .size(backgroundSize)
                .align(
                    Alignment.Center
                )
        )

        Image(painter = painterResource(id = R.drawable.xuniyaogfan1), contentDescription = null,
            modifier = if (editMode.value)
                Modifier
                    .size(rockerSize)
                    .align(Alignment.Center)
            else Modifier
                .size(rockerSize)
                .align(Alignment.Center)
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .pointerInput(UInt) {

                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            offset += dragAmount
                            val round = backgroundSize.value
                            val dis = sqrt(offset.x * offset.x + offset.y * offset.y)
                            val angle = atan2(offset.y, offset.x)
                            if (dis > round) {
                                val x = round * cos(angle)
                                val y = round * sin(angle)
                                offset = Offset(x, y)
                            }
                            onDistanceChange(dis.roundToInt())
                            onAngleChange((angle * 180f / PI).toFloat())
                        },
                        onDragEnd = {
                            offset = Offset.Zero
                            onDistanceChange(0)
                            onAngleChange(0f)
                        },
                        onDragStart = {
                            onDistanceChange(0)
                            onAngleChange(0f)
                        },
                        onDragCancel = {}
                    )
                })
    }
}


@Preview
@Composable
fun RockerPreview() {

}