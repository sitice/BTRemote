package com.example.btremote.wave

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.btremote.app.App
import java.util.*
import kotlin.math.abs

class LineChartPoints(val points: MutableList<Float>) {
    var isVision = true
    var startDrawPoint: Int? = null
    var endDrawPoint: Int? = null
    var maxValue: Float? = null
    var minValue: Float? = null
    var xGridWidth: Float? = null
    var firstSize: Int? = null
    var countSetGridTime = 0
}

class LineChart constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {
    private val leftMargin = 200.dp.value
    private val bottomMargin = 100.dp.value
    private val gridLength = 20.dp.value

    private var nowAxisYMaxVal: Float? = null
    private var nowAxisYMinVal: Float? = null

    private val timer = Timer()

    private var maxY: Float? = null
    private var minY: Float? = null

    private var waveScaleX = 1f
    private var waveScaleY = 1f

    private var touchScaleX = 1f
    private var touchScaleY = 1f

    private val timerTask = object : TimerTask() {
        override fun run() {
            App.DFProtocolList.forEach { dfProtocol ->
//                dfProtocol.dataList.forEach { dfProtocolData ->
//                    if (dfProtocolDatalineChart != null) {
//                        dfProtocolData.lineChart!!.apply {
//                            if (points.isNotEmpty()) {
//                                if (endDrawPoint == null) {
//                                    endDrawPoint = points.size - 1
//                                }
//                                if (startDrawPoint == null) {
//                                    startDrawPoint = points.size - 1
//                                }
//                                if ((endDrawPoint!! - startDrawPoint!!) * (xGridWidth
//                                        ?: 10.dp.value) >= measuredWidth - leftMargin
//                                ) {
//                                    startDrawPoint =
//                                        startDrawPoint!! + points.size - endDrawPoint!! - 1
//                                }
//                                //计算
//                                if (xGridWidth == null) {
//                                    if (firstSize == null) {
//                                        firstSize = points.size
//                                    }
//                                    countSetGridTime += 100
//                                    if (countSetGridTime >= 1000) {
//                                        xGridWidth = gridLength * 10 / (points.size - firstSize!!)
//                                    }
//                                }
//                                endDrawPoint = points.size - 1
//                            }
//                        }
//                    }
//                }
            }

            invalidate()
        }
    }


    init {
        timer.schedule(timerTask, 100, 100)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val axisPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        axisPaint.color = Color.BLACK
        axisPaint.textSize = 20.sp.value

        //X轴
        val xAxisYPos = measuredHeight - bottomMargin
        canvas?.drawLine(leftMargin, xAxisYPos, measuredWidth.toFloat(), xAxisYPos, axisPaint)
        axisPaint.textAlign = Paint.Align.CENTER
        val scaleXNum = ((measuredWidth - leftMargin) / gridLength).toInt()
        for (i in 0..scaleXNum) {
            val isDrawText = i % 5 == 0
            canvas?.drawLine(
                leftMargin + i * gridLength,
                xAxisYPos,
                leftMargin + i * gridLength,
                xAxisYPos + if (isDrawText) 50.dp.value else 20.dp.value,
                axisPaint
            )
        }

        //Y轴
        canvas?.drawLine(
            leftMargin,
            0f,
            leftMargin,
            measuredHeight.toFloat() - bottomMargin,
            axisPaint
        )

        //Y轴0点线
        val yZeroLinePos =
            (measuredHeight - bottomMargin) / 2 + if (nowAxisYMaxVal != null && nowAxisYMinVal != null) (nowAxisYMaxVal!! + nowAxisYMinVal!!) / 2 else 0f

        canvas?.save()
        canvas?.translate(leftMargin, yZeroLinePos)


        //Y轴上刻度
        val scaleYPosNum = (yZeroLinePos / gridLength).toInt()

        val scaleVal =
            if (maxY == null)
                5f
            else if (maxY != minY)
                (maxY!! - minY!!) / 6
            else
                abs(maxY!!) / 3

        axisPaint.textAlign = Paint.Align.RIGHT
        for (i in 0..scaleYPosNum) {
            val isDrawText = i % 5 == 0
            canvas?.drawLine(
                0f,
                -i * gridLength,
                -if (isDrawText) 50.dp.value else 20.dp.value,
                -i * gridLength,
                axisPaint
            )
            if (isDrawText) {
                val value = String.format("%.2f", i / 5 * scaleVal)
                canvas?.drawText(
                    value,
                    0,
                    value.length,
                    -60.dp.value,
                    -i * gridLength,
                    axisPaint
                )
            }
        }
        //Y轴下刻度
        val scaleYNegNum = ((measuredHeight - bottomMargin - yZeroLinePos) / gridLength).toInt()
        for (i in 1..scaleYNegNum) {
            val isDrawText = i % 5 == 0

            canvas?.drawLine(
                0f,
                i * gridLength,
                -if (isDrawText) 50.dp.value else 20.dp.value,
                i * gridLength,
                axisPaint
            )
            if (isDrawText) {
                val value = String.format("-%.2f", i / 5 * scaleVal)
                canvas?.drawText(
                    value,
                    0,
                    value.length,
                    -60.dp.value,
                    i * gridLength,
                    axisPaint
                )
            }
        }

        canvas?.scale(1f, -1f)
        //Y轴0刻度线
        canvas?.drawLine(0f, 0f, measuredWidth.toFloat() - leftMargin, 0f, axisPaint)

        //波形
        val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        wavePaint.color = Color.BLACK
        wavePaint.style = Paint.Style.STROKE
        wavePaint.strokeWidth = 5f
        wavePaint.strokeCap = Paint.Cap.ROUND
        wavePaint.strokeJoin = Paint.Join.ROUND
//        App.DFProtocolList.forEach { protocol ->
//            protocol.dataList.forEach { dfProtocolData ->
//                if (dfProtocolData.lineChart != null) {
//                    dfProtocolData.lineChart!!.apply {
//                        if (isVision) {
//                            if (points.isNotEmpty()) {
//                                if (endDrawPoint == null) {
//                                    endDrawPoint = points.size - 1
//                                }
//                                if (startDrawPoint == null) {
//                                    startDrawPoint = points.size - 1
//                                }
//                                //获取当前曲线的最大值
//                                if (maxValue == null) {
//                                    //先初始化
//                                    maxValue = points[endDrawPoint!!]
//                                } else {
//                                    if (points[endDrawPoint!!] > maxValue!!) {
//                                        maxValue = (points[endDrawPoint!!])
//                                    }
//                                }
//                                //获取当前曲线的最小值
//                                if (minValue == null) {
//                                    //先初始化
//                                    minValue = points[endDrawPoint!!]
//                                } else {
//                                    if (points[endDrawPoint!!] < minValue!!) {
//                                        minValue = (points[endDrawPoint!!])
//                                    }
//                                }
//                                //
//                                if (maxY == null) {
//                                    maxY = maxValue
//                                } else if (maxValue!! > maxY!!) {
//                                    maxY = maxValue
//                                }
//                                if (minY == null) {
//                                    minY = minValue
//                                } else if (minValue!! > minY!!) {
//                                    minY = minValue
//                                }
//
//                                //获取去曲线Y尺度变化
//                                waveScaleY = if (maxY!! != minY!!)
//                                    gridLength * 30 / (maxY!! - minY!!)
//                                else
//                                    gridLength * 15 / abs(maxY!!)
//
//                                //设置坐标轴的最大值和最小值
//                                nowAxisYMaxVal = waveScaleY * maxY!!
//                                nowAxisYMinVal = waveScaleY * minY!!
//                                //画曲线
//                                val path = Path()
//                                path.moveTo(0f, (points[startDrawPoint!!]) * waveScaleY)
//                                for (i in 0..endDrawPoint!! - startDrawPoint!!) {
//                                    path.lineTo(
//                                        (xGridWidth ?: 2.dp.value) * i,
//                                        (points[i + startDrawPoint!!]) * waveScaleY
//                                    )
//                                }
//                                canvas?.drawPath(path, wavePaint)
//                            }
//                        }
//
//                    }
//                }
//            }


//        }



        canvas?.restore()
    }
}

//    private var touchStatus = 0
//    private var firstClickTime: Long? = null
//
//
//    private var firstTouchX = 0f
//    private var firstTouchY = 0f
//    private var secondTouchX = 0f
//    private var secondTouchY = 0f
//    private var firstLengthX = 0f
//    private var firstLengthY = 0f
//    private var secondLengthX = 0f
//    private var secondLengthY = 0f
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event!!.actionMasked) {
//            MotionEvent.ACTION_DOWN -> {
//                firstTouchX = event.x
//                firstTouchY = event.y
//
//                if (touchStatus == 0) {
//                    touchStatus = 1
//                    firstClickTime = System.currentTimeMillis()
//                } else if (touchStatus == 2) {
//                    if (System.currentTimeMillis() - firstClickTime!! < 300) {
//
//                    }
//                    touchStatus = 0
//                }
//            }
//
//            MotionEvent.ACTION_POINTER_DOWN -> {
//                if (event.pointerCount == 2) {
//                    secondTouchX = event.getX(1)
//                    secondTouchY = event.getY(1)
//                    firstLengthX = abs(secondTouchX - firstTouchX)
//                    firstLengthY = abs(secondTouchY - firstTouchY)
//                    if (firstLengthX < 5) {
//                        firstLengthX = 0f
//                    }
//                    if (firstLengthY < 5) {
//                        firstLengthY = 0f
//                    }
//                }
//            }
//
//            MotionEvent.ACTION_MOVE -> {
//                if (event.pointerCount == 2) {
//                    val firstX = event.getX(0)
//                    val firstY = event.getX(0)
//                    val secondX = event.getX(1)
//                    val secondY = event.getX(1)
//
//                    secondLengthX = abs(firstX - secondX)
//                    secondLengthY = abs(firstY - secondY)
//                    if (firstLengthX == 0f) {
//                        secondLengthX = 0f
//                    } else {
//                        touchScaleX += secondLengthX - firstLengthX
//                        Log.d("touchScaleX = ", touchScaleX.toString())
//                    }
//                    if (firstLengthY == 0f) {
//                        secondLengthY = 0f
//                    } else {
//                        touchScaleY += secondLengthY - firstLengthY
//                        Log.d("touchScaleY = ", touchScaleY.toString())
//                    }
//                }
//            }
//            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                if (touchStatus == 1) {
//                    touchStatus = 2
//                }
//            }
//        }
//        return true
//    }

