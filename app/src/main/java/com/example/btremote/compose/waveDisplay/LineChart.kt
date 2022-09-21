package com.example.btremote.compose.waveDisplay


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.wave.LineChart

@Composable
fun LineChart(modifier: Modifier,) {
    //距离左边屏幕距离
    val marginToLeft = 180f
    //距离屏幕下边距离
    val marginToBottom = 240f

    val gridWidth = 20.dp.value

    Canvas(modifier = modifier)
    {
        drawIntoCanvas { canvas ->
            val axisXPaint = Paint()
            axisXPaint.color = Color.White
            axisXPaint.strokeWidth = 3f
            axisXPaint.style = PaintingStyle.Stroke
            val xAxisYPos = size.height - marginToBottom

            //x轴
            canvas.drawLine(
                Offset(marginToLeft, xAxisYPos),
                Offset(size.width, xAxisYPos),
                axisXPaint
            )
            val scaleXNum = ((size.width - marginToLeft) / gridWidth).toInt()
            (0..scaleXNum).forEach { index ->
                val isDrawText = index % 5 == 0
                canvas.drawLine(
                    Offset(marginToLeft + index * gridWidth, xAxisYPos),
                    Offset(
                        marginToLeft + index * gridWidth,
                        xAxisYPos + if (isDrawText) 50.dp.value else 20.dp.value
                    ),
                    axisXPaint
                )
            }
            //y轴
            canvas.drawLine(
                Offset(marginToLeft, 0f),
                Offset(marginToLeft, size.height - marginToBottom),
                axisXPaint
            )

            //y轴0点线
            val yZeroLinePos = (size.height - marginToBottom) / 2
            canvas.drawLine(
                Offset(marginToLeft, yZeroLinePos),
                Offset(size.width, yZeroLinePos),
                axisXPaint
            )
        }
    }
}

@Preview
@Composable
fun LineChartPreview() {
    LineChart(modifier = Modifier.fillMaxSize())
}