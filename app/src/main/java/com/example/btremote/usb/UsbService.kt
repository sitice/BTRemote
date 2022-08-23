package com.example.btremote.usb

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Handler
import cn.wch.ch34xuartdriver.CH34xUARTDriver
import com.example.btremote.app.App
import com.example.btremote.bluetooth.*
import com.example.btremote.tools.LogUtil
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

const val USB_OPEN_FAIL = 10
const val USB_INIT_FAIL = 11
const val USB_OPEN_SUCCESS = 12
const val USB_READ = 13
const val USB_WRITE = 14
const val NO_PERMISSION = 15

class USBService(context: Context, private val handler: Handler) {

    private var isOpen = false
    private val tag = "CH340"
    private var connectedThread: ConnectedThread? = null
    private val ACTION_USB_PERMISSION = "cn.wch.wchusbdriver.USB_PERMISSION"
    private val driver = CH34xUARTDriver(
        context.getSystemService(Context.USB_SERVICE) as UsbManager,
        context,
        ACTION_USB_PERMISSION
    )
    val isSupport: Boolean
        get() {
            return driver.UsbFeatureSupported()
        }

    fun getDeviceName(): String? {
        if (driver.EnumerateDevice() == null) {
            return null
        }
        return driver.EnumerateDevice().deviceName
    }

    fun open(bond: Int) {
        if (!isOpen) {
            val result = driver.ResumeUsbPermission()
            if (result == 0) {
                val retval = driver.ResumeUsbList()
                if (retval == -1) {
                    LogUtil.log(tag, "打开失败")
                    handler.obtainMessage(USB_OPEN_FAIL).sendToTarget()
                    driver.CloseDevice()
                } else if (retval == 0) {
                    if (driver.mDeviceConnection != null) {
                        if (!driver.UartInit()) {
                            LogUtil.log(tag, "初始化失败")
                            handler.obtainMessage(USB_INIT_FAIL).sendToTarget()
                            return
                        }
                        LogUtil.log(tag, "打开成功")
                        handler.obtainMessage(USB_OPEN_SUCCESS).sendToTarget()
                        driver.SetConfig(bond, 8, 1, 0, 0)
                        isOpen = true
                        if (connectedThread != null) {
                            close()
                            connectedThread = null
                        }
                        connectedThread = ConnectedThread()
                        connectedThread?.start()
                    } else {
                        LogUtil.log(tag, "打开失败")
                        handler.obtainMessage(USB_OPEN_FAIL).sendToTarget()
                    }
                } else {
                    LogUtil.log(tag, "没有权限")
                    handler.obtainMessage(NO_PERMISSION).sendToTarget()
                }
            }
        } else {
            LogUtil.log(tag, "已经打开了")
        }

    }

    fun write(buffer: ByteArray) {
        val result = driver.WriteData(buffer, buffer.size)
        if (result < 0) {
            handler.obtainMessage(USB_WRITE).sendToTarget()
        }
    }

    fun close() {
        isOpen = false
        try {
            Thread.sleep(200)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        driver.CloseDevice()
    }

    private inner class ConnectedThread : Thread() {
        override fun run() {
            val buffer = ByteArray(4096)
            var bytes: Int
            while (isOpen) {
                bytes = driver.ReadData(buffer, 4096)
                if (bytes > 0) {
                    handler.obtainMessage(USB_READ, bytes, -1, buffer).sendToTarget()
                }
            }
        }

    }
}