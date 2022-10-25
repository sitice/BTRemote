package com.example.btremote.view
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import androidx.core.graphics.ColorUtils
import java.util.*
import android.graphics.PorterDuff

import android.graphics.ComposeShader
import com.example.btremote.R


class ColorPickerView : View {

    companion object {
        const val TAG = "ColorPickerView"
    }

    private lateinit var colorBitmap: Bitmap
    private var arrowBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_color_arrow)
    private lateinit var mDestRect: Rect

    private var viewX = 0f
    private var viewY = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        // 背景区域
        mDestRect = Rect(0, 0, measuredWidth, measuredHeight)

        val shader1: Shader = LinearGradient(0f, 0f, measuredWidth.toFloat(), 0f,
            intArrayOf(
                Color.parseColor("#EBFE00"),
                Color.parseColor("#9FFF00"),
                Color.parseColor("#47FF00"),
                Color.parseColor("#00FF00"),
                Color.parseColor("#00FF00"),
                Color.parseColor("#00FF65"),
                Color.parseColor("#00FFA4"),
                Color.parseColor("#05FFD7"),
                Color.parseColor("#00F2FF"),
                Color.parseColor("#00BFFF"),
                Color.parseColor("#008AFF"),
                Color.parseColor("#1255FF"),
                Color.parseColor("#4B2FFF"),
                Color.parseColor("#822CFF"),
                Color.parseColor("#BD1FFF"),
                Color.parseColor("#F00CFF"),
                Color.parseColor("#FF00EE"),
                Color.parseColor("#FF00C7"),
                Color.parseColor("#FF0086"),
                Color.parseColor("#FF005A"),
                Color.parseColor("#FF1D05"),
                Color.parseColor("#FF6D00"),
                Color.parseColor("#FFAA00"),
                Color.parseColor("#FFDC00")),
            null, Shader.TileMode.CLAMP)
        val shader2: Shader = LinearGradient(0f, 0f, 0f, measuredHeight.toFloat(),
            intArrayOf(
                Color.TRANSPARENT,
                Color.WHITE),
            null, Shader.TileMode.CLAMP)
        val paint = Paint()
        val shaderCompose: Shader = ComposeShader(shader1, shader2, PorterDuff.Mode.SRC_OVER)
        paint.shader = shaderCompose
        colorBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(colorBitmap)
        canvas.drawRect(mDestRect, paint)

        Log.d(TAG, "onSizeChanged w:$measuredWidth h:$measuredHeight")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(colorBitmap, null, mDestRect, null)
        canvas.drawBitmap(arrowBitmap, viewX - 22f.dp / 2, viewY - 22f.dp / 2, null)
//        Log.d(TAG, "onDraw")
    }

    private fun getColor(event: MotionEvent) {
        try {
            val eventX = event.x
            val eventY = event.y
            if (checkXY(eventX, eventY)) {
                viewX = eventX
                viewY = eventY
            } else {
                viewX = when {
                    eventX > width -> width.toFloat()
                    eventX < 0 -> 0f
                    else -> eventX
                }
                viewY = when {
                    eventY > height -> height.toFloat()
                    eventY < 0 -> 0f
                    else -> eventY
                }
            }
            val color: Int = colorBitmap.getPixel(viewX.toInt(), viewY.toInt())
            val r: Int = Color.red(color)
            val g: Int = Color.green(color)
            val b: Int = Color.blue(color)
            val a: Int = Color.alpha(color)
            val r1 = Integer.toHexString(r)
            val g1 = Integer.toHexString(g)
            val b1 = Integer.toHexString(b)
            val a1 = Integer.toHexString(a)
            val hsl = FloatArray(3)
            ColorUtils.RGBToHSL(r, g, b, hsl)
            val text = String.format("%1\$s%2\$s%3\$s%4\$s", if (a1.length == 2) a1 else "0$a1", if (r1.length == 2) r1 else "0$r1", if (g1.length == 2) g1 else "0$g1", if (b1.length == 2) b1 else "0$b1").uppercase(Locale.getDefault())
            if (choiceColorListener != null) {
                choiceColorListener!!.onChoiceColor(color, a1, r, g, b, text, hsl)
//                Log.d(TAG, "onChoiceColor: $text")
            }
        }
        catch (e: Exception) {
            //Log.e(TAG, "getColor: ${e.message}")
        }
    }

    private fun checkXY(x: Float, y: Float): Boolean {
       // Log.d(TAG, "checkXY: x $x y $y mx $width my $height")
        return width >= x && height >= y && x >= 0 && y >= 0
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                if (MotionEvent.ACTION_DOWN == event.action) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                getColor(event)
                invalidate()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (MotionEvent.ACTION_DOWN == event.action) {
                    performClick()
                }
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {}
        }
        return true
    }

    fun setChoiceColorListener(choiceColorListener: IChoiceColorListener?) {
        this.choiceColorListener = choiceColorListener
    }

    private var choiceColorListener: IChoiceColorListener? = null

    interface IChoiceColorListener {
        fun onChoiceColor(color: Int, a1: String?, r: Int, g: Int, b: Int, text: String?, hsl: FloatArray)
    }
}

val Float.dp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)