package com.example.btremote.wifi

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.compose.ui.Modifier
import androidx.core.content.getSystemService
import com.example.btremote.app.App
import com.example.btremote.app.BLUETOOTHStatus
import com.example.btremote.app.WIFIStatus
import com.example.btremote.bluetooth.*
import com.example.btremote.tools.LogUtil
import com.example.btremote.usb.USBService
import com.example.btremote.usb.USB_READ
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.util.*

private const val STATE_NONE = 1
private const val STATE_CONNECTED = 2

@RequiresApi(Build.VERSION_CODES.M)
class WifiService(context: Context, private val handler: Handler) {

    private var mConnectedThread: ConnectedThread? = null

    private var connectState = STATE_NONE

    val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val connectManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * 判断网络类型
     */
    @RequiresApi(Build.VERSION_CODES.R)
    fun getNetStatus() {
        val info = wifiManager.connectionInfo
        App.wifiNameFlow.value = info.ssid.replace("\"", "")
        when (wifiManager.calculateSignalLevel(info.rssi)) {
            1 -> App.wifiStatusFlow.value = WIFIStatus.CONNECTED_1
            2 -> App.wifiStatusFlow.value = WIFIStatus.CONNECTED_2
            3 -> App.wifiStatusFlow.value = WIFIStatus.CONNECTED_3
            4 -> App.wifiStatusFlow.value = WIFIStatus.CONNECTED_4
            else -> App.wifiStatusFlow.value = WIFIStatus.DISCONNECTED
        }
        if (info.rssi == -127) {
            App.wifiStatusFlow.value = WIFIStatus.DISCONNECTED
        }
        if (info.ssid == "<unknown ssid>") {
            App.wifiStatusFlow.value = WIFIStatus.DISCONNECTED
        }
    }

    fun startConnect(ip: String, port: Int) {

        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }
        mConnectedThread = ConnectedThread(ip, port)
        mConnectedThread?.start()
    }

    fun stopConnect() {
        if (mConnectedThread != null) {
            mConnectedThread?.cancel()
            mConnectedThread = null
        }
    }

    fun write(byte: ByteArray) {
        if (mConnectedThread == null) {
            return
        }
        if (connectState != STATE_CONNECTED) {
            return
        }
        mConnectedThread?.write(byte)
    }


    private inner class ConnectedThread(private val ip: String, private val port: Int) : Thread() {
        private lateinit var socket: Socket
        private lateinit var mInStream: InputStream
        private lateinit var mOutStream: OutputStream

        @SuppressLint("MissingPermission")
        override fun run() {
            try {
                socket = Socket(ip, port)
                mInStream = socket.getInputStream()
                mOutStream = socket.getOutputStream()
            } catch (e: IOException) {
                return
            }
            connectState = STATE_CONNECTED
            val buffer = ByteArray(4096)
            var bytes: Int
            while (connectState == STATE_CONNECTED) {
                try {
                    bytes = mInStream.read(buffer)
                    handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget()
                } catch (e: IOException) {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        LogUtil.log("ConnectedThread:cancel", "关闭socket失败")
                        handler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
                    }
                    connectState = STATE_NONE
                    handler.obtainMessage(REC_FAIL).sendToTarget()
                }
            }
        }

        fun write(buffer: ByteArray) {
            try {
                mOutStream.write(buffer)
            } catch (e: IOException) {
                LogUtil.log("ConnectedThread:write", "写入数据失败")
                handler.obtainMessage(SEND_FAIL).sendToTarget()
            }
        }

        fun cancel() {
            connectState = STATE_NONE
            try {
                socket.close()
            } catch (e: IOException) {
                LogUtil.log("ConnectedThread:cancel", "关闭socket失败")
                handler.obtainMessage(CLOSE_CONNECT_FAIL).sendToTarget()
            }
        }
    }
}