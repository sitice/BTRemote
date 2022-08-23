package com.example.btremote

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.WindowManager
import kotlinx.coroutines.flow.MutableStateFlow

class Orientation(private val context: Context) {
    private var axis_x = 0
    private var axis_y = 0
    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var gyro: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private var mag: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private var acc: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val accelerValues = FloatArray(3)
    private val magneticValues = FloatArray(3)
    private val listener = object : SensorEventListener {
        override fun onSensorChanged(p0: SensorEvent?) {

            when (p0?.sensor?.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    var i = 0
                    while (i < 3) {
                        accelerValues[i] = p0.values[i]
                        i++
                    }
                    getAngle()
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    var i = 0
                    while (i < 3) {
                        magneticValues[i] = p0.values[i]
                        i++
                    }
                }
                Sensor.TYPE_GYROSCOPE -> {}
                else -> {}
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        }

    }


    val angles = MutableStateFlow(floatArrayOf(0f, 0f, 0f))

    fun init() {

        sensorManager.registerListener(listener, gyro, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, mag, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(listener, acc, SensorManager.SENSOR_DELAY_UI)

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dis = wm.defaultDisplay
        val rotation = dis.rotation

        axis_x = SensorManager.AXIS_X
        axis_y = SensorManager.AXIS_Y
        when (rotation) {
            android.view.Surface.ROTATION_0 -> {}
            android.view.Surface.ROTATION_90 -> {
                axis_x = SensorManager.AXIS_Y
                axis_y = SensorManager.AXIS_MINUS_X; }
            android.view.Surface.ROTATION_180 -> {
                axis_x = SensorManager.AXIS_X
                axis_y = SensorManager.AXIS_MINUS_Y; }
            android.view.Surface.ROTATION_270 -> {
                axis_x = SensorManager.AXIS_MINUS_Y
                axis_y = SensorManager.AXIS_X; }
        }
    }

    fun unregister() {
        sensorManager.unregisterListener(listener)
    }

    fun getAngle() {
        val inR = FloatArray(9)
        // 第一个是方向角度参数，第二个参数是倾斜角度参数
        // 第一个是方向角度参数，第二个参数是倾斜角度参数
        SensorManager.getRotationMatrix(inR, null, accelerValues, magneticValues)

        val outR = FloatArray(9)
        val orientationValues = FloatArray(3)
        SensorManager.remapCoordinateSystem(inR, axis_x, axis_y, outR)
        SensorManager.getOrientation(outR, orientationValues)
        for (i in 0..2) {
            orientationValues[i] = Math.toDegrees(orientationValues[i].toDouble()).toFloat()
        }
       angles.value = orientationValues
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var orientation: Orientation? = null
        fun instance(context: Context): Orientation {
            if (orientation == null) {
                orientation = Orientation(context)
            }
            return orientation as Orientation
        }
    }
}