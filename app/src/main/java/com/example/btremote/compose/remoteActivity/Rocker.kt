package com.example.btremote.compose.remoteActivity

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
import com.example.btremote.protocol.leftAngle
import com.example.btremote.protocol.leftDis
import kotlin.math.*

@Composable
fun Rocker(
    modifier: Modifier,
    backgroundSize: Dp,
    rockerSize: Dp,
    onXChange: (Byte) -> Unit,
    onYChange: (Byte) -> Unit,
    enable: Boolean = true
) {
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    Box(modifier = modifier)
    {
        Image(
            painter = painterResource(id = R.drawable.xuniyaogan),
            contentDescription = null,
            modifier = Modifier
                .size(backgroundSize)
                .align(
                    Alignment.Center
                )
        )

        Image(painter = painterResource(id = R.drawable.xuniyaogfan1), contentDescription = null,
            modifier = if (!enable)
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
                            val ly = -(dis * sin(angle)) * 127 / round
                            val lx = (dis * cos(angle)) * 127 / round
                            onYChange(ly.toInt().toByte())
                            onXChange(lx.toInt().toByte())
                        },
                        onDragEnd = {
                            offset = Offset.Zero
                            onXChange(0)
                            onYChange(0)
                        },
                        onDragStart = {
                            onXChange(0)
                            onYChange(0)
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