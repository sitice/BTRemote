package com.example.btremote.wifi

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import com.example.btremote.app.App
import com.example.btremote.app.WIFIStatus
import com.example.btremote.bluetooth.*
import com.example.btremote.tools.LogUtil
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import kotlin.concurrent.thread

private const val STATE_NONE = 1
private const val STATE_CONNECTED = 2

const val TCP_CONNECT_FAIL = 16
const val TCP_CONNECT_SUCCESS = 17
const val TCP_READ = 18
const val TCP_CLOSE_FAIL = 19
const val TCP_READ_FAIL = 20
const val TCP_SEND_FAIL = 21

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
    }

    fun startTcpClientConnect(ip: String, port: Int) {

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

        override fun run() {
            try {
                socket = Socket(ip, port)
                mInStream = socket.getInputStream()
                mOutStream = socket.getOutputStream()
            } catch (e: IOException) {
                handler.obtainMessage(TCP_CONNECT_FAIL).sendToTarget()
                return
            }
            handler.obtainMessage(TCP_CONNECT_SUCCESS).sendToTarget()
            connectState = STATE_CONNECTED
            val buffer = ByteArray(4096)
            var bytes: Int
            while (connectState == STATE_CONNECTED) {
                try {
                    bytes = mInStream.read(buffer)
                    handler.obtainMessage(TCP_READ, bytes, -1, buffer).sendToTarget()
                } catch (e: IOException) {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        LogUtil.log("ConnectedThread:cancel", "关闭socket失败")
                        handler.obtainMessage(TCP_CLOSE_FAIL).sendToTarget()
                    }
                    connectState = STATE_NONE
                    handler.obtainMessage(TCP_READ_FAIL).sendToTarget()
                }
            }
        }

        fun write(buffer: ByteArray) {
            thread {
                try {
                    mOutStream.write(buffer)
                } catch (e: IOException) {
                    LogUtil.log("ConnectedThread:write", "写入数据失败")
                    handler.obtainMessage(TCP_SEND_FAIL).sendToTarget()
                }
            }
        }

        fun cancel() {
            connectState = STATE_NONE
            if (this::socket.isInitialized)
                thread {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        LogUtil.log("ConnectedThread:cancel", "关闭socket失败")
                        handler.obtainMessage(TCP_CLOSE_FAIL).sendToTarget()
                    }
                }
        }
    }
}